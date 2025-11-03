package com.weilyeat.cms.api.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weilyeat.cms.api.attached_file.service.AttachedFileService;
import com.weilyeat.cms.api.review.dto.ReviewDto;
import com.weilyeat.cms.api.review.dto.search.ReviewSearch;
import com.weilyeat.cms.api.review.service.ReviewService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/review")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final AttachedFileService attachedFileService;

    @GetMapping
    public ResponseEntity<Page<ReviewDto.list>> list(
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"createDate"}
        ) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser,
        ReviewSearch search
    ) {
        return ResponseEntity.ok(reviewService.list(pageable, search, authUser));
    }

    @GetMapping("own")
    public ResponseEntity<Page<ReviewDto.list>> listMyReview(
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"createDate"}
        ) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser,
        ReviewSearch search
    ) {
        return ResponseEntity.ok(reviewService.listMyReview(pageable, search, authUser));
    }

    @GetMapping("{idx}/detail")
    public ResponseEntity<ReviewDto.detail> detail(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(reviewService.detail(idx, authUser));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody ReviewDto.add addDto, @AuthenticationPrincipal SinghaUser authUser) {
        reviewService.add(addDto, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("upload")
    public ResponseEntity upload(MultipartFile file) {
        return ResponseEntity.ok(attachedFileService.save(file, "review"));
    }

    @PutMapping("{idx}")
    public ResponseEntity update(@PathVariable("idx") Integer idx, @RequestBody ReviewDto.update updateDto, @AuthenticationPrincipal SinghaUser authUser) {
        reviewService.update(idx, updateDto, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity delete(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        reviewService.delete(idx, authUser);
        return ResponseEntity.ok().build();
    }
}
