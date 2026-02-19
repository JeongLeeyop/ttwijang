package com.ttwijang.cms.api.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.notification.repository.NotificationRepository;
import com.ttwijang.cms.entity.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 알림 생성
     */
    @Transactional
    public void createNotification(String userUid, Notification.NotificationType type,
                                   String title, String content,
                                   String referenceUid, String referenceType,
                                   String actionUrl) {
        Notification notification = Notification.builder()
                .userUid(userUid)
                .type(type)
                .title(title)
                .content(content)
                .referenceUid(referenceUid)
                .referenceType(referenceType)
                .isRead(false)
                .actionUrl(actionUrl)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * 알림 목록 조회 (페이지네이션)
     */
    @Transactional(readOnly = true)
    public Page<Notification> getNotifications(String userUid, Pageable pageable) {
        return notificationRepository.findByUserUidOrderByCreatedDateDesc(userUid, pageable);
    }

    /**
     * 읽지 않은 알림 개수 조회
     */
    @Transactional(readOnly = true)
    public long getUnreadCount(String userUid) {
        return notificationRepository.countByUserUidAndIsReadFalse(userUid);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(String uid, String userUid) {
        notificationRepository.findById(uid).ifPresent(notification -> {
            if (notification.getUserUid().equals(userUid)) {
                notification.setIsRead(true);
                notification.setReadDate(LocalDateTime.now());
                notificationRepository.save(notification);
            }
        });
    }

    /**
     * 전체 알림 읽음 처리
     */
    @Transactional
    public void markAllAsRead(String userUid) {
        List<Notification> unreadList = notificationRepository.findByUserUidAndIsReadFalse(userUid);
        LocalDateTime now = LocalDateTime.now();
        unreadList.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadDate(now);
        });
        notificationRepository.saveAll(unreadList);
    }

    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteNotification(String uid, String userUid) {
        notificationRepository.findById(uid).ifPresent(notification -> {
            if (notification.getUserUid().equals(userUid)) {
                notificationRepository.delete(notification);
            }
        });
    }
}
