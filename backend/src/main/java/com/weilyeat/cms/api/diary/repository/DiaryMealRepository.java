package com.weilyeat.cms.api.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.DiaryMeal;

public interface DiaryMealRepository extends JpaRepository<DiaryMeal, Long>, QuerydslPredicateExecutor<DiaryMeal> {

    @Query(value = "SELECT COUNT(*) FROM diary_meal dm WHERE dm.user_uid = ?1 AND dm.type = ?2 \n" +
        "AND date_format(now(), '%Y-%m-%d') = date_format(dm.create_date, '%Y-%m-%d')", nativeQuery = true)
    int getTodayMeal(String userUid, int type);
    
}
