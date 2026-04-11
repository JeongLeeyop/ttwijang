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
 * 구단주 신청 금액 설정 엔티티
 * 단일 행 테이블 (uid = "default")
 */
@Entity
@Table(name = "sponsor_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorSetting {

    @Id
    @Column(name = "uid", length = 50)
    private String uid;

    /**
     * 구단주 신청 금액 (캐시 단위, 원)
     */
    @Column(name = "owner_amount", nullable = false)
    private Integer ownerAmount;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        if (this.uid == null) {
            this.uid = "default";
        }
        if (this.ownerAmount == null) {
            this.ownerAmount = 0;
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
