package com.weilyeat.cms.api.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.weilyeat.cms.entity.MissionFile;

public interface MissionFileRepository extends JpaRepository<MissionFile, String> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MissionFile mf WHERE mf.missionUid IN " +
           "(SELECT m.uid FROM Mission m WHERE m.userUid = ?1)")
    void deleteByUserUid(String userUid);
}
