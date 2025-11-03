package com.weilyeat.cms.api.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    
}
