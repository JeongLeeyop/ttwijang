package com.weilyeat.cms.api.product.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.ProductOrderDto;
import com.weilyeat.cms.api.product.dto.ProductOrderGroupDto;
import com.weilyeat.cms.api.product.dto.mapper.ProductOrderGroupMapper;
import com.weilyeat.cms.api.product.dto.mapper.ProductOrderMapper;
import com.weilyeat.cms.api.product.repository.ProductOrderGroupRepository;
import com.weilyeat.cms.api.product.repository.ProductOrderWeekRepository;
import com.weilyeat.cms.api.product.repository.query.ProductOrderQuery;
import com.weilyeat.cms.entity.ProductOrderGroup;
import com.weilyeat.cms.entity.ProductOrderWeek;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface ProductOrderGroupService {
    public Page<ProductOrderGroupDto.list> list(Pageable pageable, SinghaUser authUser);
    public ProductOrderGroupDto.detail detail(Integer groupId, SinghaUser authUser);
    public Integer findOrderStatus(Integer orderGroupIdx);
    public Integer findOrderStatus2(Integer orderGroupIdx);
}
@Slf4j
@AllArgsConstructor
@Service
class ProductOrderGroupServiceImpl implements ProductOrderGroupService {
    private final ProductOrderGroupRepository repository;
    private final ProductOrderWeekRepository weekRepository;
    private final ProductOrderQuery productOrderQuery;

    @Override
    public Integer findOrderStatus(Integer orderGroupIdx) {
        // pickupCnt : 픽업배달 미완료된 주문 수
        // receiveCnt : 상품수령 미완료된 주문 수
        // 관계
        // 0 : 전체가 완료됨
        // totalCnt : 전체가 미완료됨
        ProductOrderDto.orderStatus orderStatus = productOrderQuery.getOrderStatus(orderGroupIdx);
        int totalCnt = orderStatus.getTotalCnt();
        // 부분취소된 항목을 제외
        // 주문의 totalCnt가 0이면 전체 취소 (혹은 OrderGroup에 Order값이 없거나 누락됨)
        if (totalCnt == 0) return -1;
        // 전부 픽업대기중, 배송대기중이면 : 주문접수중
        else if(orderStatus.getPickupCnt() == totalCnt && orderStatus.getReceiveCnt() == totalCnt) return 0;
        // 전부 픽업완료 되었을때 (+배송대기중이 하나라도 있으면 안됨) : 픽업완료
        else if (orderStatus.getPickupCnt() == 0 && orderStatus.getReceiveCnt() == 0) return 2;
        // 전부 픽업완료되지 않았을때 : 서비스제공중
        else if (orderStatus.getPickupCnt() != 0) return 1;
        // 배송이 되지 않았는데 픽업이 전부 완료 되었을때
        else if (orderStatus.getPickupCnt() == 0 && orderStatus.getReceiveCnt() != 0) return 2;
        // 에러
        else return -2;
    }

    @Override
    public Integer findOrderStatus2(Integer orderGroupIdx) {
        ProductOrderDto.orderStatus orderStatus = productOrderQuery.getOrderStatus2(orderGroupIdx);
        int totalCnt = orderStatus.getTotalCnt();
        // 부분취소된 항목을 제외
        // 주문의 totalCnt가 0이면 전체 취소 (혹은 OrderGroup에 Order값이 없거나 누락됨)
        if (totalCnt == 0) return -1;
        else if(orderStatus.getDeliveryReadyCnt() == totalCnt) return 0;
        else if (orderStatus.getDeliveryCompleteCnt() == totalCnt) return 2;
        else if (orderStatus.getDeliveryCompleteCnt() < totalCnt) return 1;
        else return -2;
    }
    
    @Override
    public Page<ProductOrderGroupDto.list> list(Pageable pageable, SinghaUser authUser) {
        Page<ProductOrderGroup> entities = repository.findAllByUserUidAndPaymentStatus(pageable, authUser.getUser().getUid(), 1);

        // 주문상태값이 관리 되면 삭제될 예정
        entities.stream().forEach(x -> {
                if(x.getOrderType().equals("PICKUP")){
                    Integer orderStatus = null;
                    orderStatus = findOrderStatus(x.getIdx());
                    x.setOrderStatus(orderStatus);
                } else if(x.getOrderType().equals("STATION")){
                    Integer orderStatus = null;
                    orderStatus = findOrderStatus2(x.getIdx());
                    x.setOrderStatus(orderStatus);
                }
        });

        return entities.map(x -> ProductOrderGroupMapper.INSTANCE.entitiyToListDto(x));
    }
 
    @Override
    public ProductOrderGroupDto.detail detail(Integer groupId, SinghaUser authUser) {
        ProductOrderGroup entity = repository.findByIdxAndUserUid(groupId, authUser.getUser().getUid());
        ProductOrderGroupDto.detail dto = ProductOrderGroupMapper.INSTANCE.entitiyToDetailDto(entity);

        List<ProductOrderWeek> weekEntities = weekRepository.findAllByGroupId(entity.getIdx());
        weekEntities.stream().forEach(x -> {
            ProductOrderGroupDto.weekDetail weekDetailDto = new ProductOrderGroupDto.weekDetail();
            weekDetailDto.setStartDate(x.getStartDate());
            weekDetailDto.setEndDate(x.getEndDate());
            weekDetailDto.setProducts(ProductOrderMapper.INSTANCE.entitiesToDto(x.getProducts()));
            dto.getWeek().put(x.getWeekNum(), weekDetailDto);
        });

        ProductOrderDto.lastOrder lastOrder = productOrderQuery.getLastOrder(groupId);
        dto.setReviewExpiredStatus(LocalDate.now().isAfter(lastOrder.getPickupDate().plusDays(14)));
        dto.setLastPickupStatus(lastOrder.isPickupStatus());
        return dto;
    }
}
