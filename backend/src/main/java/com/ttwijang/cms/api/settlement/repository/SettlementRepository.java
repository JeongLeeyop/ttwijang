package com.ttwijang.cms.api.settlement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ttwijang.cms.entity.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, String> {

    Optional<Settlement> findByTeamUidAndPeriod(String teamUid, String period);

    Page<Settlement> findAllByOrderByCreatedDateDesc(Pageable pageable);

    /**
     * 월별 팀별 참가비 집계 — 경기 완료 후 N일 경과한 건만
     */
    @Query(nativeQuery = true, value =
        "SELECT fm.host_team_uid AS teamUid, t.name AS teamName, " +
        "  t.owner_uid AS ownerUid, t.refund_bank_name AS bankName, t.refund_bank_account AS bankAccount, " +
        "  SUM(ct.amount) AS totalAmount, COUNT(ct.uid) AS itemCount " +
        "FROM cash_transaction ct " +
        "JOIN futsal_match fm ON ct.reference_uid = fm.uid " +
        "JOIN team t ON fm.host_team_uid = t.uid " +
        "WHERE ct.type = 'USE' AND ct.reference_type = 'MATCH' " +
        "  AND fm.status = 'COMPLETED' " +
        "  AND fm.match_date <= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
        "  AND DATE_FORMAT(ct.created_date, '%Y-%m') = :period " +
        "GROUP BY fm.host_team_uid, t.name, t.owner_uid, t.refund_bank_name, t.refund_bank_account")
    List<Object[]> getMonthlyRawSummary(@Param("period") String period, @Param("days") int days);

    /**
     * 월별 특정 팀 건별 내역 (모달용) — N일 경과 필터 포함
     */
    @Query(nativeQuery = true, value =
        "SELECT t.name AS teamName, fm.stadium_name AS stadiumName, " +
        "  DATE_FORMAT(fm.match_date, '%Y-%m-%d') AS matchDate, " +
        "  u.actual_name AS userName, " +
        "  ct.amount AS amount, ct.created_date AS createdDate " +
        "FROM cash_transaction ct " +
        "JOIN futsal_match fm ON ct.reference_uid = fm.uid " +
        "JOIN team t ON fm.host_team_uid = t.uid " +
        "LEFT JOIN user u ON ct.user_uid = u.uid " +
        "WHERE ct.type = 'USE' AND ct.reference_type = 'MATCH' " +
        "  AND fm.host_team_uid = :teamUid " +
        "  AND fm.status = 'COMPLETED' " +
        "  AND fm.match_date <= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
        "  AND DATE_FORMAT(ct.created_date, '%Y-%m') = :period " +
        "ORDER BY ct.created_date DESC")
    List<Object[]> getModalItemsRaw(
        @Param("teamUid") String teamUid,
        @Param("period") String period,
        @Param("days") int days);

    /**
     * 건별 내역 조회 (탭용, 날짜 범위 + 선택적 팀명 필터)
     */
    @Query(nativeQuery = true, value =
        "SELECT t.name AS teamName, fm.stadium_name AS stadiumName, " +
        "  DATE_FORMAT(fm.match_date, '%Y-%m-%d') AS matchDate, " +
        "  u.actual_name AS userName, " +
        "  ct.amount AS amount, ct.created_date AS createdDate " +
        "FROM cash_transaction ct " +
        "JOIN futsal_match fm ON ct.reference_uid = fm.uid " +
        "JOIN team t ON fm.host_team_uid = t.uid " +
        "LEFT JOIN user u ON ct.user_uid = u.uid " +
        "WHERE ct.type = 'USE' AND ct.reference_type = 'MATCH' " +
        "  AND fm.status = 'COMPLETED' " +
        "  AND fm.match_date <= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
        "  AND (:teamName IS NULL OR t.name LIKE CONCAT('%', :teamName, '%')) " +
        "  AND ct.created_date BETWEEN :startDate AND :endDate " +
        "ORDER BY ct.created_date DESC",
        countQuery =
        "SELECT COUNT(*) FROM cash_transaction ct " +
        "JOIN futsal_match fm ON ct.reference_uid = fm.uid " +
        "JOIN team t ON fm.host_team_uid = t.uid " +
        "WHERE ct.type = 'USE' AND ct.reference_type = 'MATCH' " +
        "  AND fm.status = 'COMPLETED' " +
        "  AND fm.match_date <= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
        "  AND (:teamName IS NULL OR t.name LIKE CONCAT('%', :teamName, '%')) " +
        "  AND ct.created_date BETWEEN :startDate AND :endDate")
    Page<Object[]> getDetailItemsRaw(
        @Param("teamName") String teamName,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("days") int days,
        Pageable pageable);
}
