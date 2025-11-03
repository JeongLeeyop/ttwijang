package com.ttwijang.cms.api.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttwijang.cms.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    
}
