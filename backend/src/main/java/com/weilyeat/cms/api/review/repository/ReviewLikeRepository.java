package com.weilyeat.cms.api.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Integer> {

    Optional<ReviewLike> findByReviewIdxAndUserUid(Integer idx, String uid);
    
}
