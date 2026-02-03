package com.ttwijang.cms.api.review.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.review.dto.ReviewDto;
import com.ttwijang.cms.api.review.dto.ReviewDto.update;
import com.ttwijang.cms.api.review.dto.mapper.ReviewMapper;
import com.ttwijang.cms.api.review.dto.search.ReviewSearch;
import com.ttwijang.cms.api.review.repository.ReviewRepository;
import com.ttwijang.cms.api.review.repository.query.ReviewQuery;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.BadRequest;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Review;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ReviewService {
    Page<ReviewDto.list> list(Pageable pageable, ReviewSearch search, SinghaUser authUser);
    Page<ReviewDto.list> listMyReview(Pageable pageable, ReviewSearch search, SinghaUser authUser);
    ReviewDto.detail detail(Integer idx, SinghaUser authUser);
    void add(ReviewDto.add addDto, SinghaUser authUser);
    void update(Integer idx, ReviewDto.update updateDto, SinghaUser authUser);
    void delete(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewQuery reviewQuery;

    @Override
    public Page<ReviewDto.list> list(Pageable pageable, ReviewSearch search, SinghaUser authUser) {
        List<ReviewDto.list> datas = reviewQuery.list(pageable, authUser, false);
        int count = reviewQuery.getTotalCount(pageable, authUser, false);
        return new PageImpl<>(datas, pageable, count);
    }

    @Override
    public Page<ReviewDto.list> listMyReview(Pageable pageable, ReviewSearch search, SinghaUser authUser) {
        List<ReviewDto.list> datas = reviewQuery.list(pageable, authUser, true);
        int count = reviewQuery.getTotalCount(pageable, authUser, true);
        return new PageImpl<>(datas, pageable, count);
    }

    @Override
    public ReviewDto.detail detail(Integer idx, SinghaUser authUser) {
        Review entity = reviewRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.REVIEW));
        entity.setViewCount(entity.getViewCount() + 1);
        reviewRepository.save(entity);
        return ReviewMapper.INSTANCE.entityToDetailDto(entity);
    }

    @Override
    public void add(ReviewDto.add addDto, SinghaUser authUser) {
        User user = authUser.getUser();

        Review entity = ReviewMapper.INSTANCE.addDtoToEntity(addDto);
        entity.setDeleteStatus(false);
        entity.setViewCount(0);
        entity.setUserUid(authUser.getUser().getUid());
        
        setRelation(entity);

        reviewRepository.save(entity);
    }

    @Override
    public void update(Integer idx, ReviewDto.update updateDto, SinghaUser authUser) {
        Review entity = reviewRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.REVIEW));
        if (!entity.getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);

        entity.getPhotoes().forEach(e -> e.setReview(null));

        entity = ReviewMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        setRelation(entity);
        reviewRepository.save(entity);
    }

    @Override
    public void delete(Integer idx, SinghaUser authUser) {
        Review entity = reviewRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.REVIEW));
        if (!entity.getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);
        
        entity.setDeleteStatus(true);
        reviewRepository.save(entity);
    }

    private void setRelation(Review entity) {
        entity.getPhotoes().forEach(e -> e.setReview(entity));
    }
}
