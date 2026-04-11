package com.ttwijang.cms.api.payment.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ttwijang.cms.api.payment.dto.PaymentRequestDto;
import com.ttwijang.cms.api.payment.repository.PaymentRequestRepository;
import com.ttwijang.cms.api.payment.service.TossPaymentService;
import com.ttwijang.cms.entity.PaymentRequest;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final TossPaymentService tossPaymentService;
    private final PaymentRequestRepository paymentRequestRepository;

    @Value("${toss.frontend-result-url}")
    private String frontendResultUrl;

    /**
     * 결제 준비: orderId 생성 + PaymentRequest 저장
     * 클라이언트가 Toss SDK 호출 전에 먼저 호출
     */
    @PostMapping("/prepare")
    public ResponseEntity<Map<String, Object>> prepare(
            OAuth2Authentication auth,
            @RequestBody Map<String, Object> body) {

        SinghaUser user = (SinghaUser) auth.getPrincipal();
        int amount = (int) body.get("amount");
        String orderName = (String) body.getOrDefault("orderName", "캐시 충전");

        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        PaymentRequest req = new PaymentRequest(orderId, orderName, user.getUser().getUid(), amount);
        paymentRequestRepository.save(req);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("orderName", orderName);
        result.put("amount", amount);
        return ResponseEntity.ok(result);
    }

    /**
     * Toss 결제 성공 콜백 (브라우저 리다이렉트)
     * ?paymentKey=xxx&orderId=xxx&amount=xxx
     */
    @GetMapping("/success")
    @Transactional
    public ResponseEntity<Void> success(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) throws Exception {

        PaymentRequestDto.approval dto = new PaymentRequestDto.approval();
        dto.setPaymentKey(paymentKey);
        dto.setOrderId(orderId);
        dto.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        try {
            tossPaymentService.payment(dto);
            headers.setLocation(URI.create(frontendResultUrl
                    + "?success=true&amount=" + amount));
        } catch (WebClientResponseException e) {
            String body = e.getResponseBodyAsString();
            System.err.println("[Toss 오류 응답] " + body);
            String msg = java.net.URLEncoder.encode(body.isEmpty() ? e.getMessage() : body, StandardCharsets.UTF_8);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            headers.setLocation(URI.create(frontendResultUrl + "?success=false&message=" + msg));
        } catch (Exception e) {
            String msg = java.net.URLEncoder.encode(
                    e.getMessage() != null ? e.getMessage() : "결제 처리 중 오류가 발생했습니다.",
                    StandardCharsets.UTF_8);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            headers.setLocation(URI.create(frontendResultUrl + "?success=false&message=" + msg));
        }
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
