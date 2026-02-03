package com.ttwijang.cms.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import com.ttwijang.cms.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

  List<Product> findAllBySellStatus(boolean i);
  List<Product> findAllByIdxIn(List<Long> productsIdx);

}
