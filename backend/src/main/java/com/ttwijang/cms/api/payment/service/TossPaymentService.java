package com.ttwijang.cms.api.payment.service;


import javax.transaction.Transactional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ttwijang.cms.api.payment.dto.PaymentRequestDto;
import com.ttwijang.cms.api.payment.dto.PaymentResultDto;
import com.ttwijang.cms.api.payment.repository.PaymentResultRepository;
import com.ttwijang.cms.api.payment.repository.TossPaymentShopRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface TossPaymentService {
    void payment(PaymentRequestDto.approval dto) throws Exception;

    void refund(Integer orderGroupId, PaymentRequestDto.refund dto, SinghaUser authUser);

}

@AllArgsConstructor
@Service
class TossPaymentShopServiceImpl implements TossPaymentService {
    private static final String SUCCESS_PATH = "https://api.tosspayments.com/v1/payments/";
    private static final String FAIL_PATH = "https://test.com/success";
    private static final String VIRTUAL_ACCOUNT_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final WebClient webClient = WebClient.builder().build();
    private final TossPaymentShopRepository tossPaymentShopRepository;
    private final PaymentResultRepository paymentResultRepository;
    private final UserRepository userRepository;
    private final PointHistoryService pointHistoryService;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void payment(PaymentRequestDto.approval dto) throws Exception {
      
    }

    @Override
    @Transactional
    public void refund(Integer orderGroupId, PaymentRequestDto.refund dto, SinghaUser authUser) {
    }

    private PaymentResultDto.success cardPayment(String secretKey, PaymentRequestDto.approval dto) {
        return this.webClient
            .post()
            // .uri(VIRTUAL_ACCOUNT_CONFIRM_URL)
            .uri(SUCCESS_PATH + dto.getPaymentKey())
            .header("Authorization", "Basic " + secretKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(PaymentResultDto.success.class)
            .block();
    }
}
