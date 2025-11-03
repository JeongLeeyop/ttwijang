package com.ttwijang.cms.api.tfse_comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Comment;
import com.ttwijang.cms.entity.TfseComment;

public interface TfseCommentRepository extends JpaRepository<TfseComment, String>, QuerydslPredicateExecutor<TfseComment> {
    Optional<TfseComment> findTopByTfseIdxOrderByViewOrderDesc(Long tfseIdx);
    Optional<TfseComment> findTopByUidOrderByViewOrderDesc(String uid);
}
