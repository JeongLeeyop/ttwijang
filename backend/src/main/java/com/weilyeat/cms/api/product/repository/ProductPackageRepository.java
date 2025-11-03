package com.weilyeat.cms.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.ProductPackage;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, Long>, QuerydslPredicateExecutor<ProductPackage> {
}
