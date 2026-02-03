package com.ttwijang.cms.api.tfse.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.post.repository.PostLikeRepository;
import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.api.tfse.repository.TfseLikeRepository;
import com.ttwijang.cms.api.tfse.repository.TfseRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.entity.PostLike;
import com.ttwijang.cms.entity.Tfse;
import com.ttwijang.cms.entity.TfseLike;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface TfseLikeService {
    void like(Long tfseIdx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class TfseLikeServiceImpl implements TfseLikeService {
    private final TfseRepository tfseRepository;
    private final TfseLikeRepository tfseLikeRepository;

    @Override
    public void like(Long tfseIdx, SinghaUser authUser) {
        Tfse tfse = tfseRepository.findById(tfseIdx).orElseThrow(() -> new NotFoundException(NotFound.POST));
        String userUid = authUser.getUser().getUid();

        Optional<TfseLike> optional = tfseLikeRepository.findByTfseIdxAndUserUid(tfseIdx, userUid);
        
        if (optional.isPresent()) {
            tfseLikeRepository.delete(optional.get());
        } else {
            TfseLike entity = new TfseLike();
            entity.setTfseIdx(tfseIdx);
            entity.setUserUid(userUid);
            tfseLikeRepository.save(entity);
        }
        
    }

}
