package com.ttwijang.cms.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>, QuerydslPredicateExecutor<Review> {
    
}
