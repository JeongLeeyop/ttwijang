package com.ttwijang.cms.api.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.entity.MissionUser;

public interface MissionUserRepository extends JpaRepository<MissionUser, String>, QuerydslPredicateExecutor<MissionUser> {
    int countByUserUidAndMissionUid(String userUid, String MissionUid);
    int countByUserUidAndMissionUidAndApproveStatus(String userUid, String MissionUid, boolean approveStatus);
    MissionUser findByUserUidAndMissionUid(String userUid, String MissionUid);
    List<MissionUser> findByUserUid(String userUid);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MissionUser mu WHERE mu.userUid = ?1")
    void deleteByUserUid(String userUid);

    void deleteByMissionUid(String missionUid);
}
