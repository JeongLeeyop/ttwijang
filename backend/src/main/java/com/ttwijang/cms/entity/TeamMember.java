package com.ttwijang.cms.entity;

import java.io.Serializable;
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
 * 팀 멤버 엔티티
 * 팀에 속한 회원 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team_member", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"teamUid", "userUid"})
})
public class TeamMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 팀 UID
    @Column(nullable = false)
    private String teamUid;

    // 사용자 UID
    @Column(nullable = false)
    private String userUid;

    // 역할 (OWNER: 운영자, MANAGER: 매니저, MEMBER: 일반회원)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberRole role;

    // 포지션 (FW, MF, DF, GK 등)
    private String position;

    // 등번호
    private Integer backNumber;

    // 가입 상태 (PENDING: 대기, APPROVED: 승인, REJECTED: 거절)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberStatus status;

    // 가입 신청서 - 지역
    private String applicationRegion;

    // 가입 신청서 - 나이
    private Integer applicationAge;

    // 가입 신청서 - 실력/경력
    @Column(columnDefinition = "TEXT")
    private String applicationExperience;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamUid", insertable = false, updatable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;

    public enum MemberRole {
        OWNER, MANAGER, MEMBER
    }

    public enum MemberStatus {
        PENDING, APPROVED, REJECTED
    }
}
