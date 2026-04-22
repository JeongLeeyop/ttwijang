package com.ttwijang.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 매치 설정 엔티티
 * 단일 행 테이블 (uid = "default")
 */
@Entity
@Table(name = "match_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchConfig {

    @Id
    @Column(name = "uid", length = 50)
    private String uid;

    /**
     * 매치 취소 가능 기준일 (경기 N일 전까지 취소 가능)
     */
    @Column(name = "cancel_days_before_match", nullable = false)
    private int cancelDaysBeforeMatch;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        if (this.uid == null) {
            this.uid = "default";
        }
        if (this.updatedDate == null) {
            this.updatedDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
