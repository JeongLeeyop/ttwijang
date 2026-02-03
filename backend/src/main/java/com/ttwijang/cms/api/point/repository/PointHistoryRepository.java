package com.ttwijang.cms.api.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer>, QuerydslPredicateExecutor<PointHistory> {

    @Query(value = "SELECT COUNT(*) FROM point_history ph WHERE ph.user_uid = ?1 AND ph.reason = ?2 AND \n" + 
        "date_format(ph.create_date, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')", nativeQuery = true)
    int countTodayByUserUidAndReason(String userUid, String reason);
    
    @Query(value = "SELECT COUNT(*) FROM point_history ph WHERE ph.user_uid = ?1 AND ph.reason = ?2 AND \n" + 
        "date_format(ph.create_date, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')", nativeQuery = true)
    int countTodayByUserUidAndTfse(String userUid, String reason);
    
    @Query(value = "SELECT COUNT(*) FROM point_history ph WHERE ph.user_uid = ?1 AND ph.reason = ?2 AND \n" + 
        "date_format(ph.create_date, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')", nativeQuery = true)
    int countTodayByUserUidAndChallenge(String userUid, String reason);
}
