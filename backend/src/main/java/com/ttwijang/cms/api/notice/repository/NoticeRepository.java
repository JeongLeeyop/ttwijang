package com.ttwijang.cms.api.notice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String> {
    Optional<Notice> findByUid(String uid);
    List<Notice> findAllByOrderByCreatedDateDesc();
}
