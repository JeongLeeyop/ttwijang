package com.ttwijang.cms.api.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Integer>, QuerydslPredicateExecutor<Food> {
    
}
