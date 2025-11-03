package com.weilyeat.cms.api.payment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.weilyeat.cms.api.payment.dto.PaymentDto;
import com.weilyeat.cms.api.payment.repository.PaymentRequestRepository;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.entity.PaymentRequest;
import com.weilyeat.cms.entity.User;

public interface PaymentService {
}

@Slf4j
@AllArgsConstructor
@Service
class PaymentServiceImpl implements PaymentService {
}
