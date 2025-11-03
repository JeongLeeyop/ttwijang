package com.weilyeat.cms.api.tfse.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Tfse;

public interface TfseRepository extends JpaRepository<Tfse, Long>, QuerydslPredicateExecutor<Tfse> {
    List<Tfse> findAll(Predicate search);
    @Query(value="select * from tfse where user_uid = ?1 and Date(tfse_date) = Date(?2)", nativeQuery = true)
    Optional<Tfse> findByUserUidAndTfseDate(String uid, LocalDateTime TfseDateTime);

    void deleteByUserUid(String uid);
}
