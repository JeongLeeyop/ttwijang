package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 모집 엔티티
 * 팀 회원 모집 공고 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_recruitment")
public class MemberRecruitment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 모집 팀 UID
    @Column(nullable = false)
    private String teamUid;

    // 모집 제목
    @Column(nullable = false, length = 200)
    private String title;

    // 모집 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 팀 특징 태그 (JSON 형태: ["실력 키워요", "리그 나가요", "선출 코치"])
    @Column(columnDefinition = "TEXT")
    private String featureTags;

    // 활동 요일 (비트마스크)
    private Integer activeDays;

    // 활동 시간대 (비트마스크)
    private Integer activeTimeSlots;

    // 활동 지역 - 도/시
    private String regionSido;

    // 활동 지역 - 시/군/구
    private String regionSigungu;

    // 월 회비
    private Integer monthlyFee;

    // 모집 성별 (0: 남자, 1: 여자, 2: 무관)
    private Integer genderType;

    // 모집 연령대 (비트마스크)
    private Integer ageGroups;

    // 모집 인원
    private Integer targetCount;

    // 현재 신청 인원
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer currentApplicants;

    // 팀 단체 사진 UID
    private String teamPhotoUid;

    // 모집 상태 (RECRUITING: 모집중, COMPLETED: 모집완료, CLOSED: 마감)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RecruitmentStatus status;

    // 모집 마감일
    private LocalDate closingDate;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamUid", insertable = false, updatable = false)
    private Team team;

    public enum RecruitmentStatus {
        RECRUITING, COMPLETED, CLOSED
    }
}
