package com.weilyeat.cms.api.push_alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.PushAlarm;

public interface PushAlarmRepository extends JpaRepository<PushAlarm, Integer>, QuerydslPredicateExecutor<PushAlarm> {
    
}
