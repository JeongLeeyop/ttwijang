package com.ttwijang.cms.api.payment.service;

import java.util.Base64;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.service.CashService;
import com.ttwijang.cms.api.payment.dto.PaymentRequestDto;
import com.ttwijang.cms.api.payment.dto.PaymentResultDto;
import com.ttwijang.cms.api.payment.repository.PaymentRequestRepository;
import com.ttwijang.cms.api.payment.repository.PaymentResultRepository;
import com.ttwijang.cms.entity.PaymentRequest;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.RequiredArgsConstructor;

public interface TossPaymentService {
    void payment(PaymentRequestDto.approval dto) throws Exception;
    void refund(Integer orderGroupId, PaymentRequestDto.refund dto, SinghaUser authUser);
}

@Service
@RequiredArgsConstructor
class TossPaymentServiceImpl implements TossPaymentService {

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final WebClient webClient = WebClient.builder().build();
    private final PaymentRequestRepository paymentRequestRepository;
    private final PaymentResultRepository paymentResultRepository;
    private final CashService cashService;

    @Value("${toss.secret-key}")
    private String secretKey;

    @Override
    @Transactional
    public void payment(PaymentRequestDto.approval dto) throws Exception {
        // 1. orderId로 결제 요청 조회 → userUid 확인
        PaymentRequest paymentRequest = paymentRequestRepository.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 orderId입니다."));

        // 2. 금액 검증
        if (paymentRequest.getAmount() != dto.getAmount()) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        // 3. Toss 결제 승인 API 호출
        String encodedKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
        PaymentResultDto.success result = webClient
                .post()
                .uri(TOSS_CONFIRM_URL)
                .header("Authorization", "Basic " + encodedKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(PaymentResultDto.success.class)
                .block();

        if (result == null) {
            throw new RuntimeException("결제 승인 응답이 없습니다.");
        }

        // 4. 결제 상태 업데이트
        paymentRequest.setStatus(1);
        paymentRequestRepository.save(paymentRequest);

        // 5. 캐시 충전
        CashDto.ChargeRequest chargeRequest = CashDto.ChargeRequest.builder()
                .amount(dto.getAmount())
                .paymentMethod(result.getMethod())
                .paymentReferenceId(dto.getPaymentKey())
                .build();
        cashService.charge(chargeRequest, paymentRequest.getUserUid());
    }

    @Override
    @Transactional
    public void refund(Integer orderGroupId, PaymentRequestDto.refund dto, SinghaUser authUser) {
        // TODO: 환불 구현
    }
}
