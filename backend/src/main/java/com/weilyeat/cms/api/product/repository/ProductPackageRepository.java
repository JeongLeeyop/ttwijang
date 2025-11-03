package com.ttwijang.cms.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.ProductPackage;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, Long>, QuerydslPredicateExecutor<ProductPackage> {
}
