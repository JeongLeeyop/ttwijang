package com.ttwijang.cms.api.post.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.post.repository.PostLikeRepository;
import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.entity.PostLike;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ClientPostLikeService {
    void like(String postUid, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ClientPostLikeServiceImpl implements ClientPostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public void like(String postUid, SinghaUser authUser) {
        Post post = postRepository.findById(postUid).orElseThrow(() -> new NotFoundException(NotFound.POST));
        String userUid = authUser.getUser().getUid();

        Optional<PostLike> optional = postLikeRepository.findByPostUidAndUserUid(postUid, userUid);
        
        if (optional.isPresent()) {
            postLikeRepository.delete(optional.get());
        } else {
            PostLike entity = new PostLike();
            entity.setPostUid(postUid);
            entity.setUserUid(userUid);
            postLikeRepository.save(entity);
        }
        
    }

    
}
