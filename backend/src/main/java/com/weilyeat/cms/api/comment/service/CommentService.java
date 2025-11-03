package com.weilyeat.cms.api.comment.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.weilyeat.cms.api.comment.dto.CommentDto;
import com.weilyeat.cms.api.comment.dto.mapper.CommentMapper;
import com.weilyeat.cms.api.comment.dto.search.CommentSearch;
import com.weilyeat.cms.api.comment.exception.CommentNotFoundException;
import com.weilyeat.cms.api.comment.repository.CommentRepository;
import com.weilyeat.cms.api.post.exception.PostNotFoundException;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.entity.Comment;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.User;

import lombok.AllArgsConstructor;

public interface CommentService {
    Page<CommentDto.Detail> list(Pageable pageable, CommentSearch commentSearch);
    void add(CommentDto.Add addDto, UserDetails userDetail);
    void update(Comment comment, CommentDto.Update updateDto);
    void delete(Comment comment);
}

@Service
@AllArgsConstructor
class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Page<CommentDto.Detail> list(Pageable pageable, CommentSearch commentSearch) {
        return commentRepository.findAll(commentSearch.search(), pageable).map(data -> CommentMapper.INSTANCE.entityToDetailDto(data));
    }

    @Override
    public void add(CommentDto.Add addDto, @AuthenticationPrincipal UserDetails userDetail) {
        if (userDetail == null || userDetail.getUsername() == null) throw new UserNotFoundException("잘못된 접근입니다.");
        User user = userRepository.findByUserId(userDetail.getUsername().toString()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));

        Comment comment = CommentMapper.INSTANCE.addDtoToEntity(addDto);
        Post post = postRepository.findById(addDto.getPostUid()).orElseThrow(() -> new PostNotFoundException());
        comment.setUserUid(user.getUid());
        comment.setPostUid(post.getUid());
        
        if (!StringUtils.hasText(addDto.getWriter())) comment.setWriter(user.getActualName());
        if (StringUtils.hasText(addDto.getPassword())) comment.setPassword(passwordEncoder.encode(addDto.getPassword()));

        if (StringUtils.hasText(addDto.getParentUid())) {
            Comment parentComment = commentRepository.findById(addDto.getParentUid()).orElseThrow(() -> new CommentNotFoundException());
            comment.setParentUid(parentComment.getUid());
            comment.setViewOrder(parentComment.getViewOrder());
            comment.setDepth(parentComment.getDepth() + 1);
        } else {
            int viewOrder = 1;
            Optional<Comment> optional = commentRepository.findTopByOrderByViewOrderDesc();
            if (optional.isPresent()) viewOrder = comment.getViewOrder() + 1;
            comment.setParentUid(null);
            comment.setViewOrder(viewOrder);
            comment.setDepth(1);
        }

        commentRepository.save(comment);
    }

    @Override
    public void update(Comment comment, CommentDto.Update updateDto) {
        comment = CommentMapper.INSTANCE.updateDtoToEntity(updateDto, comment);
        if (StringUtils.hasText(updateDto.getPassword())) comment.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        
        commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);        
    }

}
