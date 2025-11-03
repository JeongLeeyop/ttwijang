package com.weilyeat.cms.api.tfse_comment.service;

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

import com.weilyeat.cms.api.comment.exception.CommentNotFoundException;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.api.tfse.repository.TfseRepository;
import com.weilyeat.cms.api.tfse_comment.dto.TfseCommentDto;
import com.weilyeat.cms.api.tfse_comment.dto.mapper.TfseCommentMapper;
import com.weilyeat.cms.api.tfse_comment.dto.search.TfseCommentSearch;
import com.weilyeat.cms.api.tfse_comment.repository.TfseCommentRepository;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.Tfse;
import com.weilyeat.cms.entity.TfseComment;
import com.weilyeat.cms.entity.User;

import lombok.AllArgsConstructor;

public interface TfseCommentService {
Page<TfseCommentDto.Detail> list(Pageable pageable, TfseCommentSearch tfseCommentSearch,UserDetails userDetail);
void add(TfseCommentDto.Add addDto, UserDetails userDetail);
void update(TfseComment comment, TfseCommentDto.Update updateDto);
void delete(TfseComment comment);
}

@Service
@AllArgsConstructor
class TfseCommentServiceImpl implements TfseCommentService {
private final TfseCommentRepository tfseCommentRepository;
private final TfseRepository tfseRepository;
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

@Override
public Page<TfseCommentDto.Detail> list(Pageable pageable, TfseCommentSearch tfseCommentSearch, @AuthenticationPrincipal UserDetails userDetail) {
    User user = userRepository.findByUserId(userDetail.getUsername().toString()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
    return tfseCommentRepository.findAll(tfseCommentSearch.search(), pageable).map(data -> TfseCommentMapper.INSTANCE.entityToAdmDetailDto(data, user.getUid()));
}

@Override
public void add(TfseCommentDto.Add addDto, @AuthenticationPrincipal UserDetails userDetail) {
    if (userDetail == null || userDetail.getUsername() == null) throw new UserNotFoundException("잘못된 접근입니다.");
    User user = userRepository.findByUserId(userDetail.getUsername().toString()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));

    TfseComment comment = TfseCommentMapper.INSTANCE.addDtoToEntity(addDto);
    Tfse tfse = tfseRepository.findById(addDto.getTfseIdx()).orElseThrow(() -> new NotFoundException(NotFound.TFSE));
    comment.setUserUid(user.getUid());
    comment.setTfseIdx(tfse.getIdx());
    
    if (!StringUtils.hasText(addDto.getWriter())){
        StringBuilder sb = new StringBuilder(user.getActualName());
        for (int i = 1; i < user.getActualName().length(); i++) {
            sb.setCharAt(i, '*');
        }
        comment.setWriter(sb.toString());
    }

    if (StringUtils.hasText(addDto.getPassword())) comment.setPassword(passwordEncoder.encode(addDto.getPassword()));

    if (StringUtils.hasText(addDto.getParentUid())) {
    TfseComment parentComment = tfseCommentRepository.findById(addDto.getParentUid()).orElseThrow(() -> new CommentNotFoundException());
        comment.setParentUid(parentComment.getUid());
        comment.setViewOrder(parentComment.getViewOrder());
        comment.setDepth(parentComment.getDepth() + 1);
    } else {
        int viewOrder = 1;
        Optional<TfseComment> optional = tfseCommentRepository.findTopByTfseIdxOrderByViewOrderDesc(tfse.getIdx());
        if (optional.isPresent()) viewOrder = optional.get().getViewOrder() + 1;
        comment.setParentUid(null);
        comment.setViewOrder(viewOrder);
        comment.setDepth(1);
    }

    tfseCommentRepository.save(comment);
}

@Override
public void update(TfseComment comment, TfseCommentDto.Update updateDto) {
    comment = TfseCommentMapper.INSTANCE.updateDtoToEntity(updateDto, comment);
    if (StringUtils.hasText(updateDto.getPassword())) comment.setPassword(passwordEncoder.encode(updateDto.getPassword()));
    
    tfseCommentRepository.save(comment);
}

@Override
public void delete(TfseComment comment) {
    tfseCommentRepository.delete(comment);        
}

}
    