package com.weilyeat.cms.api.mission_record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionRecord;

public interface MissionRecordRepository extends JpaRepository<MissionRecord, String>, QuerydslPredicateExecutor<MissionRecord> {
    @Query(value = "Select Count(*) from mission_record where user_uid = ?1 and mission_uid = ?2 and Date(create_date) = curdate()", nativeQuery= true)
    int countTodayMission(String userUid, String missionUid);
    
    @Query(value = "Select Count(*) from mission_record where user_uid = ?1 and mission_uid = ?2", nativeQuery= true)
    int countMissionByUserUid(String userUid, String missionUid);

    List<MissionRecord> findAll(Predicate search);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MissionRecord mr WHERE mr.userUid = ?1")
    void deleteByUserUid(String userUid);

    void deleteByMissionUid(String missionUid);
}
