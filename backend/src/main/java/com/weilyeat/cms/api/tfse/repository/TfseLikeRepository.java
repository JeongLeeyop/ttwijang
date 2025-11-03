package com.ttwijang.cms.api.tfse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttwijang.cms.entity.PostLike;
import com.ttwijang.cms.entity.TfseLike;

public interface TfseLikeRepository extends JpaRepository<TfseLike, Integer> {

    Optional<TfseLike> findByTfseIdxAndUserUid(Long idx, String userUid);
    
}
