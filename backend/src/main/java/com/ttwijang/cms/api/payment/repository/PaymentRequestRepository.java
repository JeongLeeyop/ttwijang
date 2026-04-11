package com.ttwijang.cms.api.payment.repository;

import java.util.Optional;
import com.ttwijang.cms.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Integer> {
    Optional<PaymentRequest> findByOrderId(String orderId);
}
