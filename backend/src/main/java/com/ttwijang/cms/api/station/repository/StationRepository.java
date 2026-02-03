package com.ttwijang.cms.api.station.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Station;

public interface StationRepository extends JpaRepository<Station, Integer>, QuerydslPredicateExecutor<Station> {

    List<Station> findByUseStatus(boolean b);
    
}
