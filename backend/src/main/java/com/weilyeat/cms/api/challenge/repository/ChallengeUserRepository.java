package com.weilyeat.cms.api.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.weilyeat.cms.entity.ChallengeUser;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, String>, QuerydslPredicateExecutor<ChallengeUser> {
    int countByUserUidAndChallengeUid(String userUid, String ChallengeUid);
    ChallengeUser findByUserUidAndChallengeUid(String userUid, String ChallengeUid);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM challenge_user WHERE user_uid = ?1", nativeQuery = true)
    void deleteByUserUid(String userUid);
}
