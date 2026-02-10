package com.ttwijang.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @Column(name = "uid", length = 50)
    private String uid;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BannerStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "region_sido", length = 50)
    private String regionSido;

    @Column(name = "region_sigungu", length = 50)
    private String regionSigungu;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_page", length = 50)
    private TargetPage targetPage;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        if (this.uid == null) {
            this.uid = "banner-" + UUID.randomUUID().toString();
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
        if (this.updatedDate == null) {
            this.updatedDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = BannerStatus.INACTIVE;
        }
        if (this.displayOrder == null) {
            this.displayOrder = 0;
        }
    }

    public enum BannerStatus {
        ACTIVE,
        INACTIVE
    }

    public enum TargetPage {
        HOME,
        LEAGUE,
        MATCH,
        TEAM,
        ALL
    }
}
