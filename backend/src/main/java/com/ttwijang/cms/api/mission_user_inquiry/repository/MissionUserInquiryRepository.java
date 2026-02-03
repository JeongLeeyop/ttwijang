package com.ttwijang.cms.api.mission_user_inquiry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.MissionUserInquiry;

public interface MissionUserInquiryRepository extends JpaRepository<MissionUserInquiry, Integer>, QuerydslPredicateExecutor<MissionUserInquiry> {
    @Query(value = "Select Count(*) from mission_user_inquiry where user_uid = ?1 and mission_user_uid = ?2 and Date(create_date) = curdate()", nativeQuery= true)
    int countTodayMissionUser(String userUid, String missionUserUid);

    List<MissionUserInquiry> findAll(Predicate search);
    
    List<MissionUserInquiry> findByUserUid(String userUid);

    void deleteAllByUserUid(String userUid);
}
