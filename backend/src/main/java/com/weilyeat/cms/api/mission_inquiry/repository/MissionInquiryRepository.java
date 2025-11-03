package com.weilyeat.cms.api.mission_inquiry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionInquiry;

public interface MissionInquiryRepository extends JpaRepository<MissionInquiry, Integer>, QuerydslPredicateExecutor<MissionInquiry> {
    @Query(value = "Select Count(*) from mission_inquiry where user_uid = ?1 and mission_uid = ?2 and Date(create_date) = curdate()", nativeQuery= true)
    int countTodayMission(String userUid, String missionUid);

    List<MissionInquiry> findAll(Predicate search);
    Optional<MissionInquiry> findTopByOrderByViewOrderDesc();
}
