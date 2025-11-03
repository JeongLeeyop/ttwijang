package com.weilyeat.cms.api.tfse_feedback.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.SelfFeedback;
import com.weilyeat.cms.entity.Tfse;

public interface SelfFeedbackRepository extends JpaRepository<SelfFeedback, Long>,  QuerydslPredicateExecutor<SelfFeedback> {
    Optional<SelfFeedback> findByUserUidAndSelfFeedbackDate(String uid, LocalDate selfFeedbackDate);
    
    void deleteByUserUid(String uid);    
}
