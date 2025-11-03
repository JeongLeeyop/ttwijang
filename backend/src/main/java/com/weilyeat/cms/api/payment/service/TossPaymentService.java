package com.weilyeat.cms.api.payment.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.weilyeat.cms.api.coupon.repository.CouponRepository;
import com.weilyeat.cms.api.coupon.repository.UserCouponRepository;
import com.weilyeat.cms.api.payment.dto.PaymentRequestDto;
import com.weilyeat.cms.api.payment.dto.PaymentResultDto;
import com.weilyeat.cms.api.payment.dto.mapper.PaymentResultMapper;
import com.weilyeat.cms.api.payment.repository.PaymentResultRepository;
import com.weilyeat.cms.api.payment.repository.TossPaymentShopRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.product.dto.ProductOrderDto;
import com.weilyeat.cms.api.product.repository.AdmProductRepository;
import com.weilyeat.cms.api.product.repository.ProductOrderGroupRepository;
import com.weilyeat.cms.api.product.repository.ProductOrderRepository;
import com.weilyeat.cms.api.product.repository.query.ProductOrderQuery;
import com.weilyeat.cms.api.shop.repository.ShopRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.ProductQuantityExceedException;
import com.weilyeat.cms.common.exception.WeekDaysDeniedException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Coupon;
import com.weilyeat.cms.entity.PaymentResult;
import com.weilyeat.cms.entity.ProductOrder;
import com.weilyeat.cms.entity.ProductOrderGroup;
import com.weilyeat.cms.entity.ProductOrderType;
import com.weilyeat.cms.entity.TossPaymentShop;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.entity.UserCoupon;
import com.weilyeat.cms.oauth.SinghaUser;

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
    private final ProductOrderGroupRepository orderGroupRepository;
    private final ProductOrderRepository orderRepository;
    private final AdmProductRepository productRepository;
    private final UserRepository userRepository;
    private final PointHistoryService pointHistoryService;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ShopRepository shopRepository;
    private final ProductOrderQuery productOrderQuery;

    @Override
    public void payment(PaymentRequestDto.approval dto) throws Exception {
        ProductOrderGroup productOrderGroup = orderGroupRepository.findByOrderId(dto.getOrderId()).orElseThrow(NotFoundException::new);

        // 상품 제한 수량 검증
        List<ProductOrderDto.groupOrderCount> result = productOrderQuery.getGroupOrderCount(productOrderGroup.getIdx());
        
        List<Integer> dayNumList = new ArrayList<>();

        if(productOrderGroup.getOrderType() == ProductOrderType.PICKUP){
            int maxHoldCnt = productOrderGroup.getShop().getMaxHoldCnt();
            for(ProductOrderDto.groupOrderCount item : result ){
                dayNumList.add(item.getDayNum());
                List<ProductOrderDto.dayCount> dayCountList = productOrderQuery.getOrderCountList(item.getStartDate(), item.getEndDate(), dayNumList, productOrderGroup.getShopId());
                for(ProductOrderDto.dayCount dayCount : dayCountList ){
                    if(maxHoldCnt < (dayCount.getCount() + item.getCount())) {
                        String str = "";
                        switch(dayCount.getDayNum()){
                            case 1:
                            str = "월";
                            break;
                            case 2:
                            str = "화";
                            break;
                            case 3:
                            str = "수";
                            break;
                            case 4:
                            str = "목";
                            break;
                            case 5:
                            str = "금";
                            break;
                        }
                        throw new ProductQuantityExceedException("구매 가능 수량이 초과되었습니다. ("+ item.getWeekNum()+"주차 "+str+"요일)" ); // 이미취소된건
                    }
                };
                dayNumList.clear();
            }
        }

        productOrderGroup.setPaymentStatus(1);
        productOrderGroup.setPaymentKey(dto.getPaymentKey());
        productRepository.updatePaymentStatus(1, 1, productOrderGroup.getIdx());

        orderGroupRepository.save(productOrderGroup);
        User user = userRepository.findById(productOrderGroup.getUserUid()).orElseThrow(NotFoundException::new);

        if (productOrderGroup.getUsePoint() != null && productOrderGroup.getUsePoint() > 0) {
            if (user.getPoint() < productOrderGroup.getUsePoint()) { // 포인트 없을 시 예외처리
                throw new BadRequestException(BadRequest.NOT_ENOUGH_POINT);
            }
            // user.setPoint(user.getPoint() - productOrderGroup.getUsePoint());
            // userRepository.save(user);

            // 포인트 히스토리
            StringBuilder sb = new StringBuilder();
            sb.append("포인트 사용");
            pointHistoryService.addPoint(productOrderGroup.getUsePoint() * -1, sb.toString(), user.getUid());
        }

        // 쿠폰 사용처리
        if (productOrderGroup.getUseCouponIdx() != null) {    
            UserCoupon userCoupon = userCouponRepository.findById(productOrderGroup.getUseCouponIdx()).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
            if (LocalDateTime.now().isAfter(userCoupon.getExpiredDate())) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);
            if (!userCoupon.getUserUid().equals(user.getUid())) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);
            if (userCoupon.isUseStatus()) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);
            userCoupon.setUseStatus(true);
            userCoupon.setUseDate(LocalDateTime.now());
            userCouponRepository.save(userCoupon);
        }
        

        // 이벤트 쿠폰 발급
        List<Coupon> eventCouponList = couponRepository.getBuyEventCouponList(productOrderGroup.getOriginAmount(), LocalDateTime.now());
        List<UserCoupon> giveCouponList = new ArrayList<UserCoupon>();
        for (Coupon coupon : eventCouponList) {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserUid(user.getUid());
            userCoupon.setCouponIdx(coupon.getIdx());
            userCoupon.setName(coupon.getName());
            userCoupon.setType(coupon.getType());
            userCoupon.setPercentStatus(coupon.isPercentStatus());
            userCoupon.setDiscountPercent(coupon.getDiscountPercent());
            userCoupon.setDiscountPrice(coupon.getDiscountPrice());
            userCoupon.setExpiredDate(coupon.getExpiredDate());
            userCoupon.setOrderIdx(productOrderGroup.getIdx());
            
            giveCouponList.add(userCoupon);
        }
        userCouponRepository.saveAll(giveCouponList);

        TossPaymentShop shop =  tossPaymentShopRepository.findById(1).orElseThrow(BadRequestException::new);
        PaymentResultDto.success successDto = cardPayment(shop.getSecretKey(), dto);
        PaymentResult entity = PaymentResultMapper.INSTANCE.dtoToEntity(successDto);
        paymentResultRepository.save(entity);
    }

    @Override
    @Transactional
    public void refund(Integer orderGroupId, PaymentRequestDto.refund dto, SinghaUser authUser) {
        ProductOrderGroup productOrderGroup = orderGroupRepository.findById(orderGroupId).orElseThrow(NotFoundException::new);
        User user = authUser.getUser();
        if (productOrderGroup.getOrderStatus() < 0) throw new BadRequestException(BadRequest.ALREADY_CANCEL_ORDER); // 이미취소된건

        if (!productOrderGroup.getUserUid().equals(user.getUid())) {
            throw new BadRequestException(BadRequest.NOT_MINE);
        }

        List<ProductOrder> productOrders = new ArrayList<ProductOrder>();
        int amount = 0;
        if (dto.isAllStatus()) {
            productOrders = orderRepository.findAllByGroupId(orderGroupId); // 요청 주차 상품목록
            LocalDate limitCancelDate = LocalDate.now().plusDays(1);
            for (ProductOrder order : productOrders) {
                if (order.getOrderStatus() > -1 && order.getStartDate().isAfter(limitCancelDate)) amount += order.getAmount();
            }
        } else {
            productOrders = orderRepository.findAllByGroupIdAndWeekNum(orderGroupId, dto.getWeek()); // 요청 주차 상품목록
            if (productOrders.stream().anyMatch(x -> x.getOrderStatus() < 0)) throw new BadRequestException(BadRequest.ALREADY_CANCEL_ORDER); // 이미취소된건
            LocalDate limitCancelDate = LocalDate.now().plusDays(1);
            for (ProductOrder order : productOrders) {
                if (order.getStartDate().isAfter(limitCancelDate)) amount += order.getAmount();
            }
        }

        TossPaymentShop shop =  tossPaymentShopRepository.findById(1).orElseThrow(BadRequestException::new);
        dto.setCancelAmount(amount);
        if (!dto.isAllStatus()) dto.setCancelReason("사용자 환불요청");

        @SuppressWarnings("unused")
        PaymentResultDto.success successDto = this.webClient
            .post()
            .uri(SUCCESS_PATH + productOrderGroup.getPaymentKey() + "/cancel")
            .header("Authorization", "Basic " + shop.getSecretKey())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(PaymentResultDto.success.class)
            .block();

        productOrders.forEach(po -> {
            po.setOrderStatus(-1);
            po.setRefundDate(LocalDateTime.now());
        });
        orderRepository.saveAll(productOrders);

        // 모든 상품이 환불되면 productOrderGroup을 -1로
        List<ProductOrder> productOrders2 = new ArrayList<ProductOrder>();
        productOrders2 = orderRepository.findAllByGroupId(orderGroupId); 
        boolean notAllCancelCk = false;
        for (ProductOrder order2 : productOrders2) {
                if (order2.getOrderStatus() != -1) notAllCancelCk = true;
        }
        
        if(!notAllCancelCk) {
            productOrderGroup.setOrderStatus(-1);
            productOrderGroup.setCancelReasonType("부분취소");
            productOrderGroup.setCancelReason("전 상품 부분취소");
            // 포인트나 쿠폰을 돌려주지 않는다.

            // 이벤트 쿠폰 회수 하기   
            List<UserCoupon> eventCouponList = userCouponRepository.findByOrderIdx(productOrderGroup.getIdx());
            if(eventCouponList != null && eventCouponList.size() != 0){
                for(UserCoupon eventCoupon : eventCouponList){
                    userCouponRepository.delete(eventCoupon);
                }
            }
        }

        if (dto.isAllStatus()) {
            productOrderGroup.setOrderStatus(-1);
            productOrderGroup.setCancelReasonType(dto.getCancelReasonType());
            productOrderGroup.setCancelReason(dto.getCancelReason());

            // 포인트 돌려주기
            if(productOrderGroup.getUsePoint() > 0 ){
                pointHistoryService.addPoint(productOrderGroup.getUsePoint(), "사용자 상품 전체 취소", user.getUid());
            }
            // 쿠폰 돌려주기
            if(productOrderGroup.getUseCouponIdx() != null ){
                UserCoupon coupon = userCouponRepository.findById(productOrderGroup.getUseCouponIdx()).orElse(null);
                if(coupon != null){
                    coupon.setUseStatus(false);
                    coupon.setUseDate(null);
                    userCouponRepository.save(coupon);
                }
            }
            // 이벤트 쿠폰 회수 하기   
            List<UserCoupon> eventCouponList = userCouponRepository.findByOrderIdx(productOrderGroup.getIdx());
            if(eventCouponList != null && eventCouponList.size() != 0){
                for(UserCoupon eventCoupon : eventCouponList){
                    userCouponRepository.delete(eventCoupon);
                }
            }
        }
        orderGroupRepository.save(productOrderGroup);
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
