package com.ttwijang.cms.api.notification.service;

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
}
