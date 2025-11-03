package com.weilyeat.cms.api.tfse.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.post.repository.PostLikeRepository;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.api.tfse.repository.TfseLikeRepository;
import com.weilyeat.cms.api.tfse.repository.TfseRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.PostLike;
import com.weilyeat.cms.entity.Tfse;
import com.weilyeat.cms.entity.TfseLike;
import com.weilyeat.cms.oauth.SinghaUser;

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
