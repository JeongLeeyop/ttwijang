package com.weilyeat.cms.api.mission_inquiry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.MissionInquiryPage;

public interface MissionInquiryPageRepository extends JpaRepository<MissionInquiryPage, Integer>, QuerydslPredicateExecutor<MissionInquiryPage> {
    List<MissionInquiryPage> findAll(Predicate search);
    Optional<MissionInquiryPage> findByPageNumAndUseStatusIsTrue(Integer pageNum);
}
