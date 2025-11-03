package com.weilyeat.cms.api.product.repository;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.api.product.dto.AdmProductOrderGroupDto.list;
import com.weilyeat.cms.entity.ProductOrderGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

public interface AdmProductOrderGroupRepository extends JpaRepository<ProductOrderGroup, Integer>, QuerydslPredicateExecutor<ProductOrderGroup> {

    Page<ProductOrderGroup> findAll(Predicate search, Pageable pageable);
 
    @Query(value = "Select a.* from product_order_group a join product_order b on b.group_id = a.idx where b.idx in ?1 group by a.idx", nativeQuery = true)
    List<ProductOrderGroup> findAllByOrderList(List<Integer> idxList);
}
