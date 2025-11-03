package com.weilyeat.cms.api.challenge_record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.ChallengeRecord;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, String>, QuerydslPredicateExecutor<ChallengeRecord> {
    @Query(value = "Select Count(*) from challenge_record where user_uid = ?1 and challenge_uid = ?2 and Date(create_date) = curdate()", nativeQuery= true)
    int countTodayChallenge(String userUid, String challengeUid);

    List<ChallengeRecord> findAll(Predicate search);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM challenge_record WHERE user_uid = ?1", nativeQuery = true)
    void deleteByUserUid(String userUid);
}
