package com.weilyeat.cms.api.tfse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.PostLike;
import com.weilyeat.cms.entity.TfseLike;

public interface TfseLikeRepository extends JpaRepository<TfseLike, Integer> {

    Optional<TfseLike> findByTfseIdxAndUserUid(Long idx, String userUid);
    
}
