package com.weilyeat.cms.api.review.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.review.repository.ReviewLikeRepository;
import com.weilyeat.cms.api.review.repository.ReviewRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Review;
import com.weilyeat.cms.entity.ReviewLike;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ReviewLikeService {
    void like(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ReviewLikeServiceImpl implements ReviewLikeService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    public void like(Integer idx, SinghaUser authUser) {
        Review review = reviewRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.REVIEW));
        User user = authUser.getUser();
        Optional<ReviewLike> optional = reviewLikeRepository.findByReviewIdxAndUserUid(review.getIdx(), user.getUid());
        if (optional.isPresent()) {
            reviewLikeRepository.delete(optional.get());
        } else {
            ReviewLike entity = new ReviewLike();
            entity.setReviewIdx(idx);
            entity.setUserUid(user.getUid());
            reviewLikeRepository.save(entity);
        }
    }

    
}