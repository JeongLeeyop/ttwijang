package com.ttwijang.cms.api.product.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.coupon.repository.UserCouponRepository;
import com.ttwijang.cms.api.payment.dto.PaymentDto;
import com.ttwijang.cms.api.payment.repository.PaymentRequestRepository;
import com.ttwijang.cms.api.product.dto.ProductOrderDto;
import com.ttwijang.cms.api.product.dto.mapper.ProductOrderMapper;
import com.ttwijang.cms.api.product.repository.ProductOrderDayRepository;
import com.ttwijang.cms.api.product.repository.ProductOrderGroupRepository;
import com.ttwijang.cms.api.product.repository.ProductOrderRepository;
import com.ttwijang.cms.api.product.repository.ProductOrderWeekRepository;
import com.ttwijang.cms.api.product.repository.ProductRepository;
import com.ttwijang.cms.api.product.repository.query.ProductOrderQuery;
import com.ttwijang.cms.api.product.repository.search.ProductOrderSearch;
import com.ttwijang.cms.api.shop.repository.ShopRepository;
import com.ttwijang.cms.api.station.repository.StationRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.WeekDaysDeniedException;
import com.ttwijang.cms.common.exception.code.BadRequest;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.PaymentRequest;
import com.ttwijang.cms.entity.Product;
import com.ttwijang.cms.entity.ProductOrder;
import com.ttwijang.cms.entity.ProductOrderDay;
import com.ttwijang.cms.entity.ProductOrderGroup;
import com.ttwijang.cms.entity.ProductOrderType;
import com.ttwijang.cms.entity.ProductOrderWeek;
import com.ttwijang.cms.entity.Shop;
import com.ttwijang.cms.entity.ShopHoliday;
import com.ttwijang.cms.entity.Station;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.entity.UserCoupon;
import com.ttwijang.cms.oauth.SinghaUser;
import com.ttwijang.cms.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

public interface ProductOrderService {
    public PaymentDto.createKey order(ProductOrderDto.order dto, SinghaUser authUser);

    public Page<ProductOrderDto.list> list(Pageable pageable, SinghaUser authUser);

    public int getRemainCount(SinghaUser authUser);

    public ProductOrderDto.orderCountList getOrderCount(ProductOrderDto.orderCountList data);
}

@Slf4j
@Service
class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductRepository productRepository;
    private final ProductOrderWeekRepository weekRepository;
    private final ProductOrderDayRepository dayRepository;
    private final ProductOrderRepository orderRepository;
    private final ProductOrderGroupRepository groupRepository;
    private final ProductOrderQuery productOrderQuery;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final PaymentRequestRepository paymentRequestRepository;
    private final ShopRepository shopRepository;
    private final StationRepository stationRepository;

    public ProductOrderServiceImpl(ProductRepository productRepository,
                                   ProductOrderWeekRepository weekRepository,
                                   ProductOrderDayRepository dayRepository,
                                   ProductOrderRepository orderRepository,
                                   ProductOrderGroupRepository groupRepository,
                                   ProductOrderQuery productOrderQuery,
                                   UserCouponRepository userCouponRepository,
                                   UserRepository userRepository,
                                   PaymentRequestRepository paymentRequestRepository,
                                   ShopRepository shopRepository,
                                   StationRepository stationRepository) {
        this.productRepository = productRepository;
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
        this.orderRepository = orderRepository;
        this.groupRepository = groupRepository;
        this.productOrderQuery = productOrderQuery;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
        this.paymentRequestRepository = paymentRequestRepository;
        this.shopRepository = shopRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    @Override
    public PaymentDto.createKey order(ProductOrderDto.order dto, SinghaUser authUser) {
        String orderId = UUID.randomUUID().toString();
        ProductOrderGroup groupEntity = new ProductOrderGroup();
        Map<Long, ProductOrderDto.orderChildProduct> products = new HashMap<Long, ProductOrderDto.orderChildProduct>();
        List<ProductOrder> productOrderEntities = new ArrayList<ProductOrder>();
        List<ProductOrderWeek> weekEntities = new ArrayList<ProductOrderWeek>();
        List<ProductOrderDay> dayEntities = new ArrayList<ProductOrderDay>();
        User user = authUser.getUser();
        int discountPrice = 0;
        boolean isPackageOrder = "PACKAGE".equals(dto.getOrderType());

        log.info("Processing order - OrderId: {}, OrderType: {}, IsPackage: {}", orderId, dto.getOrderType(), isPackageOrder);
        if (isPackageOrder) {
            log.info("Package order details - WeekCount: {}, StationId: {}, Amount: {}", 
                dto.getWeek() != null ? dto.getWeek().size() : 0, dto.getStationId(), dto.getAmount());
        }

        
        dto.getWeek().forEach((weekKey, weekValue) -> {
            // LocalDate startDate = DateUtil.startWeekDay(weekKey);
            // LocalDate endDate = DateUtil.endWeekDay(weekKey);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(weekValue.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(weekValue.getEndDate(), formatter);
            
            if (groupEntity.getStartDate()==null) groupEntity.setStartDate(startDate);
            groupEntity.setEndDate(endDate);
            
            ProductOrderWeek weekEntity = new ProductOrderWeek(weekKey, startDate, endDate);
            weekRepository.save(weekEntity);
            weekEntities.add(weekEntity);
            
            groupEntity.setWeekNum(groupEntity.getWeekNum() + 1);
            // if (groupEntity.getWeekNum() < weekKey) groupEntity.setWeekNum(weekKey);
            if (weekKey > 5) throw new BadRequestException(BadRequest.PRODUCT_ORDER_LIMIT);
            weekValue.getDay().forEach((dayKey, dayValue) -> {

                ProductOrderDay dayEntity = new ProductOrderDay();
                dayEntity.setOrderDate(DateUtil.date(weekKey, dayKey));
                dayEntity.setWeekNum(weekKey);
                dayEntities.add(dayEntity);

                dayValue.getProducts().forEach((productKey, productValue) -> {
                    Product productEntity = productRepository.findById(productValue.getProduct().getIdx()).orElseThrow(() -> new BadRequestException(BadRequest.NOT_MATCH_PRODUCT));
                    ProductOrder productOrderEntity = new ProductOrder();
                    productOrderEntity.setWeekNum(weekKey);
                    productOrderEntity.setDayNum(dayKey);
                    productOrderEntity.setAmount(productEntity.getPrice());
                    productOrderEntity.setProductId(productEntity.getIdx());
                    productOrderEntity.setAmount(productEntity.getPrice() * productValue.getCount());
                    productOrderEntity.setProductNum(productValue.getCount());
                    productOrderEntity.setStartDate(startDate);
                    productOrderEntity.setEndDate(endDate);
                    productOrderEntity.setOrderDate(DateUtil.date(weekKey, dayKey));
                    productOrderEntity.setWeek(weekEntity);
                    productOrderEntity.setDay(dayEntity);
                    productOrderEntity.setSettleStatus(0);
                    productOrderEntity.setPaymentStatus(0);
                    productOrderEntity.setOrderStatus(0);
                    productOrderEntity.setProductName(productEntity.getName());
                    productOrderEntity.setPickupTime(dto.getPickupTime());
                    productOrderEntities.add(productOrderEntity);

                    if (products.containsKey(productKey)) {
                        productValue.getProduct().setCount(products.get(productKey).getCount() + productValue.getCount());
                    } else {
                        productValue.getProduct().setCount(productValue.getCount());
                    }

                    products.put(productKey, productValue.getProduct());
                });
            });
        });

        if(dto.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            Shop shop = shopRepository.findById(dto.getShopId()).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
            ShopHoliday shopHoliday = shop.getHolidays();
            if (StringUtils.hasText(dto.getPickupTime())) {
                if (!shop.getPickupTimes().stream().anyMatch(x -> x.getPickupTime().toString().equals(dto.getPickupTime()))) {
                    throw new BadRequestException(BadRequest.NOT_VALID_PICKUP_TIME);    
                }
            } else if (dto.getOrderType().equals(ProductOrderType.PICKUP.toString())){
                throw new BadRequestException(BadRequest.NOT_VALID_PICKUP_TIME);
            }
            
            dto.getWeek().forEach((weekKey, weekValue) -> {
                weekValue.getDay().forEach((dayKey, dayValue) -> {
                    if (shopHoliday != null){
                        if(dayKey == 1 && shopHoliday.getMon()) throw new WeekDaysDeniedException();
                        if(dayKey == 2 && shopHoliday.getTue()) throw new WeekDaysDeniedException();
                        if(dayKey == 3 && shopHoliday.getWed()) throw new WeekDaysDeniedException();
                        if(dayKey == 4 && shopHoliday.getThu()) throw new WeekDaysDeniedException();
                        if(dayKey == 5 && shopHoliday.getFri()) throw new WeekDaysDeniedException();
                        if(dayKey == 6 && shopHoliday.getSat()) throw new WeekDaysDeniedException();
                        if(dayKey == 0 && shopHoliday.getSun()) throw new WeekDaysDeniedException();
                    }
                }); 
            });
        }

        if (dto.getUsePoint() != null && dto.getUsePoint() > 0) {
            User user2 = userRepository.findById(user.getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
            if (user2.getPoint() != null && user2.getPoint() < dto.getUsePoint()) throw new BadRequestException(BadRequest.NOT_ENOUGH_POINT);
            discountPrice += dto.getUsePoint();
        }

        if (dto.getUseCouponIdx() != null) {
            UserCoupon userCoupon = userCouponRepository.findById(dto.getUseCouponIdx()).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
            if (LocalDateTime.now().isAfter(userCoupon.getExpiredDate())) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);
            if (!userCoupon.getUserUid().equals(user.getUid())) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);
            if (userCoupon.isUseStatus()) throw new BadRequestException(BadRequest.NOT_VALID_COUPON);

            if (userCoupon.isPercentStatus()) {
                discountPrice += dto.getAmount() / 100 * userCoupon.getDiscountPercent();
            } else {
                discountPrice += userCoupon.getDiscountPrice();
            }

            userCoupon.setUseStatus(false);
            // userCoupon.setUseStatus(true);
            userCouponRepository.save(userCoupon);
        }

        /*
         * 프론트랑 가격비교
         */
        log.debug("Products for price calculation: {}", products);
        log.debug("DTO amount: {}", dto.getAmount());
        
        int amount;
        // 패키지 주문의 경우 가격 검증을 건너뛰거나 다르게 처리
        if (isPackageOrder) {
            amount = dto.getAmount();
        } else {
            amount = productOrderQuery.getTotalPrice(products);
            if (dto.getAmount() != amount) throw new BadRequestException(BadRequest.NOT_MATCH_PRICE);
        }
        
        groupEntity.setUserUid(user.getUid());

        if(dto.getOrderType().equals(ProductOrderType.STATION.toString()) || isPackageOrder) {
            Station station = stationRepository.findById(dto.getStationId()).orElseThrow(() -> new NotFoundException(NotFound.STATION));
            groupEntity.setStationId(station.getIdx());
        } else if(dto.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            groupEntity.setShopId(dto.getShopId());
        }
        
        groupEntity.setOrderType(ProductOrderType.valueOf(dto.getOrderType()));
        groupEntity.setUseCouponIdx(dto.getUseCouponIdx());
        groupEntity.setUsePoint(dto.getUsePoint());
        groupEntity.setDiscountAmount(discountPrice);
        groupEntity.setOriginAmount(amount);
        
        amount -= discountPrice;
        amount += dto.getDeliveryFee();
        
        // groupEntity.setWeekNum(4);
        groupEntity.setAmount(amount);
        groupEntity.setProductNum(dto.getCount());
        groupEntity.setPaymentStatus(0);
        groupEntity.setOrderStatus(0);
        groupEntity.setOrderId(orderId);
        groupEntity.setDeliveryFee(dto.getDeliveryFee());
        groupEntity.setAddressDetail(dto.getAddressDetail());
        groupEntity.setMemo(dto.getMemo());
        
        log.info("Saving order group - OrderId: {}, OrderType: {}, Amount: {}", orderId, dto.getOrderType(), amount);
        groupRepository.save(groupEntity);
        log.info("Order group saved successfully - GroupId: {}, OrderId: {}", groupEntity.getIdx(), orderId);

        weekEntities.stream().forEach(x -> x.setGroupId(groupEntity.getIdx()));
        weekRepository.saveAll(weekEntities);
        dayEntities.stream().forEach(x -> x.setGroupId(groupEntity.getIdx()));
        dayRepository.saveAll(dayEntities);
        
        productOrderEntities.stream().forEach(x-> x.setGroupId(groupEntity.getIdx()));
        orderRepository.saveAll(productOrderEntities);

        String orderName = isPackageOrder ? "패키지 주문" : "와로샐러드";
        PaymentRequest paymentRequest = new PaymentRequest(orderId, orderName, user.getUid());
        paymentRequestRepository.save(paymentRequest);

        return new PaymentDto.createKey(orderId, amount);
    }

    @Override
    public Page<ProductOrderDto.list> list(Pageable pageable, SinghaUser authUser) {
        ProductOrderSearch search = new ProductOrderSearch();
        search.setUserUid(authUser.getUser().getUid());

        return orderRepository.findAll(search.search(), pageable).map(e -> ProductOrderMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public int getRemainCount(SinghaUser authUser) {
        User user = authUser.getUser();
        return orderRepository.getRemainCount(user.getUid());
    }
    @Override
    public ProductOrderDto.orderCountList getOrderCount(ProductOrderDto.orderCountList dto) {
        shopRepository.findById(dto.getShopIdx()).orElseThrow(() -> new NotFoundException(NotFound.SHOP));

        dto.getWeek().forEach((weekKey, weekValue) -> {
            if (weekKey > 5) throw new BadRequestException(BadRequest.PRODUCT_ORDER_LIMIT);
            List<Integer> dayList = new ArrayList<>();
            weekValue.getDay().forEach((dayKey, dayValue) -> {
                dayList.add(dayKey);
            });
            List<ProductOrderDto.dayCount> result = productOrderQuery.getOrderCountList(weekValue.getStartDate(), weekValue.getEndDate(), dayList, dto.getShopIdx());
           weekValue.getDay().forEach((dayKey, dayValue) -> {
                boolean zeroFlag = true;
                for(ProductOrderDto.dayCount item : result ){
                    if(dayKey == item.getDayNum()){
                        dayValue.setOrderCount(item.getCount());
                        zeroFlag = false;
                    }
                }
                // 없는경우
                if(zeroFlag) dayValue.setOrderCount(0);
            });
        });
        return dto;
    }
}
