package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 알림 엔티티
 * 사용자 알림 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 수신자 사용자 UID
    @Column(nullable = false)
    private String userUid;

    // 알림 유형 (MATCH, GUEST, TEAM, LEAGUE, SPONSORSHIP, SYSTEM)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NotificationType type;

    // 알림 제목
    @Column(nullable = false, length = 200)
    private String title;

    // 알림 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 관련 엔티티 UID
    private String referenceUid;

    // 관련 엔티티 타입
    private String referenceType;

    // 읽음 여부
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead;

    // 클릭 시 이동 URL
    private String actionUrl;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime readDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;

    public enum NotificationType {
        MATCH, GUEST, TEAM, LEAGUE, SPONSORSHIP, SYSTEM
    }
}
