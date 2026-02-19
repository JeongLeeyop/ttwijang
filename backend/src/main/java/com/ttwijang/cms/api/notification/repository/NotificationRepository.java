package com.ttwijang.cms.api.notification.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    Page<Notification> findByUserUidOrderByCreatedDateDesc(String userUid, Pageable pageable);

    List<Notification> findByUserUidAndIsReadFalse(String userUid);

    long countByUserUidAndIsReadFalse(String userUid);
}
