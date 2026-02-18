package com.ttwijang.cms.api.cash.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.CashWallet;

@Repository
public interface CashWalletRepository extends JpaRepository<CashWallet, String>, QuerydslPredicateExecutor<CashWallet> {

    Optional<CashWallet> findByUid(String uid);

    Optional<CashWallet> findByUserUid(String userUid);

    boolean existsByUserUid(String userUid);
}
