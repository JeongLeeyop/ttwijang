package com.weilyeat.cms.api.mission_record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.weilyeat.cms.entity.MissionRecordFile;

public interface MissionRecordFileRepository extends JpaRepository<MissionRecordFile, String> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MissionRecordFile mrf WHERE mrf.missionRecordUid IN " +
           "(SELECT mr.uid FROM MissionRecord mr WHERE mr.userUid = ?1)")
    void deleteByUserUid(String userUid);

    @Modifying
    @Transactional
    @Query("DELETE FROM MissionRecordFile mrf WHERE mrf.missionRecordUid IN " +
           "(SELECT mr.uid FROM MissionRecord mr WHERE mr.missionUid = ?1)")
    void deleteByMissionUid(String missionUid);

    void deleteByMissionRecordUid(String missionRecordUid);
}
