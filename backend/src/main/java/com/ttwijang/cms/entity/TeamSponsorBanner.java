package com.ttwijang.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.GeneratedValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 팀별 후원 배너 엔티티
 * 사용자 앱 '나의 팀 → 후원내역'에 표시될 배너를 관리자가 등록
 * 팀당 하나의 배너 레코드, 복수 이미지는 imageUrls(JSON 배열)로 관리
 */
@Entity
@Table(name = "team_sponsor_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSponsorBanner {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uid", length = 50)
    private String uid;

    @Column(name = "team_uid", nullable = false, length = 50)
    private String teamUid;

    @Column(name = "team_name", length = 100)
    private String teamName;

    // 복수 이미지 URL을 JSON 배열 문자열로 저장 (예: ["url1","url2"])
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
