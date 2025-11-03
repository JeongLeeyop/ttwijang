package com.ttwijang.cms.api.payment.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.URLEncoder;
import com.ttwijang.cms.api.payment.dto.PaymentRequestDto;
import com.ttwijang.cms.api.payment.service.PaymentService;
import com.ttwijang.cms.api.payment.service.TossPaymentService;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.CommonException;
import com.ttwijang.cms.common.exception.ProductQuantityExceedException;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/api/client/payment")
public class PaymentController {

    private final PaymentService service;
    private final TossPaymentService tossPaymentService;

    @GetMapping("success")
    @Transactional
    public ResponseEntity success(OAuth2Authentication userDetail, PaymentRequestDto.approval dto) throws URISyntaxException, JsonProcessingException {

        String errorMessage = null;
        // String uri = "http://localhost:3000/delivery";
        String uri = "/delivery";
        URI redirectUri = null;
        try {
            tossPaymentService.payment(dto);
        }  catch (WebClientResponseException e){
            // int statusCode = e.getRawStatusCode();
            // System.out.println("오류 코드: " + statusCode);
            errorMessage = e.getResponseBodyAsString();
            errorMessage = new String(errorMessage.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(errorMessage);
        
            // JsonNode에서 특정 필드의 값을 가져옵니다.
            String code = jsonNode.get("code").asText();
            String message = jsonNode.get("message").asText();
            
            uri = "http://localhost:3000/diet/order";
            // uri = "/diet/order";
            redirectUri = new URI(uri+"?errorMessage=" + java.net.URLEncoder.encode(message, StandardCharsets.UTF_8));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            errorMessage = "Payment failed: " + e.getMessage();
            // uri = "http://localhost:3000/diet/order";
            uri = "/diet/order";
            redirectUri = new URI(uri+"?errorMessage=" + java.net.URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        if(redirectUri==null) redirectUri = new URI(uri);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
