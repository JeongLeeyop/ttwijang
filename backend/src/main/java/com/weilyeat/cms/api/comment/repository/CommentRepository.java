package com.weilyeat.cms.api.comment.repository;

import java.util.Optional;

import com.weilyeat.cms.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository extends JpaRepository<Comment, String>, QuerydslPredicateExecutor<Comment> {
    Optional<Comment> findTopByOrderByViewOrderDesc();
}
