package com.ttwijang.cms.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 앱 시작 시 DB 전체 테이블의 collation을 utf8mb4_unicode_ci로 통일합니다.
 * MySQL 8 기본값(utf8mb4_0900_ai_ci)과 기존 변환된 테이블(utf8mb4_unicode_ci) 간
 * JOIN 시 발생하는 'Illegal mix of collations' 오류를 방지합니다.
 */
@Configuration
public class CollationFixConfig {

    private static final Logger log = LoggerFactory.getLogger(CollationFixConfig.class);
    private static final String TARGET_COLLATION = "utf8mb4_unicode_ci";
    private static final String TARGET_CHARSET = "utf8mb4";

    private final DataSource dataSource;

    public CollationFixConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fixCollations() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // FK 검사 비활성화 (ALTER TABLE 시 FK 호환성 오류 방지)
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");

            // 데이터베이스 기본 collation 변경
            String dbName = conn.getCatalog();
            stmt.executeUpdate(String.format(
                "ALTER DATABASE `%s` CHARACTER SET %s COLLATE %s",
                dbName, TARGET_CHARSET, TARGET_COLLATION));

            // 현재 DB의 모든 테이블 조회하여 collation 불일치 테이블만 변환
            ResultSet rs = stmt.executeQuery(
                "SELECT TABLE_NAME FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = DATABASE() " +
                "AND TABLE_COLLATION IS NOT NULL " +
                "AND TABLE_COLLATION != '" + TARGET_COLLATION + "'"
            );

            int fixedCount = 0;
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                try (Statement alterStmt = conn.createStatement()) {
                    alterStmt.executeUpdate(String.format(
                        "ALTER TABLE `%s` CONVERT TO CHARACTER SET %s COLLATE %s",
                        tableName, TARGET_CHARSET, TARGET_COLLATION));
                    fixedCount++;
                    log.info("[CollationFix] Converted table: {}", tableName);
                } catch (Exception e) {
                    log.warn("[CollationFix] Failed to convert table {}: {}", tableName, e.getMessage());
                }
            }

            // FK 검사 다시 활성화
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

            // post 테이블의 legacy NOT NULL 컬럼들을 nullable로 변경
            fixLegacyNotNullColumns(conn);

            // post_like 테이블을 JPA 엔티티에 맞게 재생성
            fixPostLikeTable(conn);

            if (fixedCount > 0) {
                log.info("[CollationFix] Fixed collation for {} tables to {}", fixedCount, TARGET_COLLATION);
            } else {
                log.info("[CollationFix] All tables already use {}", TARGET_COLLATION);
            }

        } catch (Exception e) {
            log.error("[CollationFix] Failed to fix collations: {}", e.getMessage());
        }
    }

    /**
     * V007 마이그레이션에서 생성된 NOT NULL 컬럼 중 JPA 엔티티에 없는 컬럼들을 nullable로 변경합니다.
     * board_id (BIGINT NOT NULL), account_id (VARCHAR NOT NULL) 등이 해당됩니다.
     */
    private void fixLegacyNotNullColumns(Connection conn) {
        // column_name, table_name, nullable_type 순서
        String[][] fixes = {
            {"post", "board_id", "BIGINT"},
            {"post", "account_id", "VARCHAR(36)"},
            {"post_like", "post_id", "BIGINT"},
            {"post_like", "account_id", "VARCHAR(36)"},
        };

        for (String[] fix : fixes) {
            String table = fix[0];
            String column = fix[1];
            String colType = fix[2];
            try {
                // 해당 컬럼이 NOT NULL인지 확인
                ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT IS_NULLABLE FROM information_schema.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() " +
                    "AND TABLE_NAME = '" + table + "' " +
                    "AND COLUMN_NAME = '" + column + "'"
                );
                if (rs.next() && "NO".equals(rs.getString("IS_NULLABLE"))) {
                    conn.createStatement().executeUpdate(
                        "ALTER TABLE `" + table + "` MODIFY COLUMN `" + column + "` " + colType + " NULL"
                    );
                    log.info("[CollationFix] Made {}.{} nullable", table, column);
                }
            } catch (Exception e) {
                log.warn("[CollationFix] Failed to fix {}.{}: {}", table, column, e.getMessage());
            }
        }
    }

    /**
     * post_like 테이블을 JPA 엔티티(PostLike.java)에 맞게 재생성합니다.
     * V007 마이그레이션이 생성한 스키마(id, post_id, account_id)와
     * JPA 엔티티(idx, post_uid, user_uid)가 불일치하므로 테이블을 재생성합니다.
     */
    private void fixPostLikeTable(Connection conn) {
        try {
            // idx 컬럼이 AUTO_INCREMENT인지 확인 — 이미 올바른 상태이면 스킵
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT EXTRA FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'post_like' AND COLUMN_NAME = 'idx'"
            );
            if (rs.next() && rs.getString("EXTRA").contains("auto_increment")) {
                return; // 이미 정상
            }

            conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            conn.createStatement().executeUpdate("DROP TABLE IF EXISTS post_like");
            conn.createStatement().executeUpdate(
                "CREATE TABLE post_like (" +
                "  idx INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  post_uid VARCHAR(255)," +
                "  user_uid VARCHAR(255)," +
                "  create_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "  UNIQUE KEY uk_post_like (post_uid, user_uid)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );
            conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            log.info("[CollationFix] Recreated post_like table to match JPA entity");
        } catch (Exception e) {
            log.warn("[CollationFix] Failed to fix post_like table: {}", e.getMessage());
        }
    }
}
