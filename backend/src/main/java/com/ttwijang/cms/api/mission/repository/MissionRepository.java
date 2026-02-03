package com.ttwijang.cms.api.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, String>, QuerydslPredicateExecutor<Mission> {
    List<Mission> findAll(Predicate search);

    @Query("SELECT m FROM Mission m WHERE m.alarmStatus = true AND m.alarmTime = :alarmTime AND " +
           "CURRENT_DATE >= m.startDate AND CURRENT_DATE <= m.endDate AND " +
           "((DAYOFWEEK(CURRENT_DATE) = 2 AND m.mon = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 3 AND m.tue = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 4 AND m.wed = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 5 AND m.thu = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 6 AND m.fri = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 7 AND m.sat = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 1 AND m.sun = true))")
    List<Mission> findMissionsForAlarm(@Param("alarmTime") String alarmTime);

    @Query("SELECT DISTINCT m FROM Mission m " +
           "LEFT JOIN FETCH m.missionUserList mu " +
           "WHERE m.alarmStatus = true AND mu.approveStatus = true AND m.alarmTime = :alarmTime AND " +
           "CURRENT_DATE >= m.startDate AND CURRENT_DATE <= m.endDate AND " +
           "((DAYOFWEEK(CURRENT_DATE) = 2 AND m.mon = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 3 AND m.tue = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 4 AND m.wed = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 5 AND m.thu = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 6 AND m.fri = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 7 AND m.sat = true) OR " +
           "(DAYOFWEEK(CURRENT_DATE) = 1 AND m.sun = true))")
    List<Mission> findMissionsForAlarmWithUsers(@Param("alarmTime") String alarmTime);
    
    // 진행 중인 미션 조회 (완료되지 않은 미션만, 포기하지 않은 미션만)
    @Query("SELECT DISTINCT m FROM Mission m " +
           "JOIN m.missionUserList mu " +
           "LEFT JOIN MissionRecord mr ON mr.missionUid = m.uid AND mr.userUid = mu.userUid " +
           "WHERE mu.userUid = :userUid AND mu.approveStatus = true AND mu.abandonStatus = false " +
           "AND CURRENT_DATE >= m.startDate AND CURRENT_DATE <= m.endDate " +
           "AND m.deleteStatus = false " +
           "GROUP BY m.uid " +
           "HAVING COUNT(mr.uid) < m.totalDay")
    List<Mission> findCurrentMissions(@Param("userUid") String userUid);
    
    // 지난 미션 조회 (완료된 미션 + 실패한 미션 + 포기한 미션)
    @Query("SELECT DISTINCT m FROM Mission m " +
           "JOIN m.missionUserList mu " +
           "LEFT JOIN MissionRecord mr ON mr.missionUid = m.uid AND mr.userUid = mu.userUid " +
           "WHERE mu.userUid = :userUid AND mu.approveStatus = true " +
           "AND m.deleteStatus = false " +
           "AND (mu.abandonStatus = true OR " +
           "     CURRENT_DATE > m.endDate OR " +
           "     (SELECT COUNT(mr2.uid) FROM MissionRecord mr2 WHERE mr2.missionUid = m.uid AND mr2.userUid = mu.userUid) >= m.totalDay) " +
           "GROUP BY m.uid")
    List<Mission> findPastMissions(@Param("userUid") String userUid);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Mission m WHERE m.userUid = ?1")
    void deleteByUserUid(String userUid);
}
