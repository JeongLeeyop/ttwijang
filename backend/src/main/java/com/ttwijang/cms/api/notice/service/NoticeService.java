package com.ttwijang.cms.api.notice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.api.notice.dto.NoticeDto;
import com.ttwijang.cms.api.notice.repository.NoticeRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.entity.Notice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NoticeDto.Response> getNoticeList() {
        return noticeRepository.findAllByOrderByCreatedDateDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeDto.Response getNotice(String uid) {
        Notice notice = noticeRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
        return toResponse(notice);
    }

    @Transactional
    public NoticeDto.Response createNotice(NoticeDto.CreateRequest request) {
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        Notice saved = noticeRepository.save(notice);

        // 전체 사용자에게 공지사항 알림 발송
        userRepository.findAll().forEach(user -> notificationService.createNotification(
                user.getUid(),
                Notification.NotificationType.SYSTEM,
                "[공지] " + saved.getTitle(),
                saved.getContent() != null && saved.getContent().length() > 60
                        ? saved.getContent().substring(0, 60) + "…"
                        : (saved.getContent() != null ? saved.getContent() : ""),
                saved.getUid(),
                "NOTICE",
                "/notice"
        ));

        return toResponse(saved);
    }

    @Transactional
    public NoticeDto.Response updateNotice(String uid, NoticeDto.UpdateRequest request) {
        Notice notice = noticeRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
        if (request.getTitle() != null) notice.setTitle(request.getTitle());
        if (request.getContent() != null) notice.setContent(request.getContent());
        return toResponse(noticeRepository.save(notice));
    }

    @Transactional
    public void deleteNotice(String uid) {
        Notice notice = noticeRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
        noticeRepository.delete(notice);
    }

    private NoticeDto.Response toResponse(Notice notice) {
        return NoticeDto.Response.builder()
                .uid(notice.getUid())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdDate(notice.getCreatedDate())
                .updatedDate(notice.getUpdatedDate())
                .build();
    }
}
