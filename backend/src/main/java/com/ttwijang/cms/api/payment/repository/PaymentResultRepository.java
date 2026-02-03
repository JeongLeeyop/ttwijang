package com.ttwijang.cms.api.payment.repository;

import com.ttwijang.cms.entity.PaymentResult;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentResultRepository extends JpaRepository<PaymentResult, Integer>{
    
}
