package com.ttwijang.cms.api.cash.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.CashTransaction;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, String>, QuerydslPredicateExecutor<CashTransaction> {

    Page<CashTransaction> findByWalletUid(String walletUid, Pageable pageable);

    List<CashTransaction> findByWalletUidAndType(String walletUid, CashTransaction.TransactionType type);

    @Query("SELECT ct FROM CashTransaction ct WHERE ct.walletUid = :walletUid AND ct.createdDate BETWEEN :startDate AND :endDate")
    Page<CashTransaction> findByWalletUidAndDateRange(
            @Param("walletUid") String walletUid,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT SUM(ct.amount) FROM CashTransaction ct WHERE ct.walletUid = :walletUid AND ct.type = :type")
    Long sumAmountByWalletUidAndType(@Param("walletUid") String walletUid, @Param("type") CashTransaction.TransactionType type);
}
