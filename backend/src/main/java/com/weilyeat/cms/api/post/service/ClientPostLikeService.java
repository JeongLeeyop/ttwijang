package com.weilyeat.cms.api.post.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.post.repository.PostLikeRepository;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.PostLike;
import com.weilyeat.cms.oauth.SinghaUser;

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
