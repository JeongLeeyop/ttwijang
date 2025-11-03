package com.weilyeat.cms.api.payment.repository;

import com.weilyeat.cms.entity.PaymentResult;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentResultRepository extends JpaRepository<PaymentResult, Integer>{
    
}
