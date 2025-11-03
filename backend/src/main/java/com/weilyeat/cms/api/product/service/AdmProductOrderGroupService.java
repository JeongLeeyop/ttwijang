package com.weilyeat.cms.api.product.service;

import com.weilyeat.cms.api.product.dto.AdmProductOrderGroupDto;
import com.weilyeat.cms.api.product.dto.ProductOrderDto;
import com.weilyeat.cms.api.product.dto.ProductOrderGroupDto;
import com.weilyeat.cms.api.product.dto.mapper.AdmProductOrderGroupMapper;
import com.weilyeat.cms.api.product.repository.AdmProductOrderGroupRepository;
import com.weilyeat.cms.api.product.repository.query.ProductOrderQuery;
import com.weilyeat.cms.api.product.repository.search.AdmProductOrderGroupSearch;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.ProductOrderGroup;
import com.weilyeat.cms.entity.ProductOrderType;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface AdmProductOrderGroupService {
    public Page<AdmProductOrderGroupDto.list> list(AdmProductOrderGroupSearch search, Pageable pageable);
    public void updateStatus(Integer idx, ProductOrderGroupDto.updateStatus dto);
    public void updateReceiveStatusByProductList(List<Integer> idxList, ProductOrderType type);
    public Integer findOrderStatus(Integer orderGroupIdx);
    public Integer findOrderStatus2(Integer orderGroupIdx);

}

@AllArgsConstructor
@Service
class AdmProductOrderGroupServiceImpl implements AdmProductOrderGroupService { 
    private final AdmProductOrderGroupRepository repository;
    private final ProductOrderQuery productOrderQuery;

    @Override
    public Page<AdmProductOrderGroupDto.list> list(AdmProductOrderGroupSearch search, Pageable pageable) {
        return repository.findAll(search.search(), pageable).map(x -> AdmProductOrderGroupMapper.INSTANCE.entityToListDto(x));
    }

    @Override
    public void updateStatus(Integer idx, ProductOrderGroupDto.updateStatus dto) {
        ProductOrderGroup entity = repository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.ORDER));
        entity.setOrderStatus(dto.getOrderStatus());
        repository.save(entity);
    }

    @Transactional
    @Override
    public void updateReceiveStatusByProductList(List<Integer> idxList, ProductOrderType type) {
        List<ProductOrderGroup> orderGroupList = repository.findAllByOrderList(idxList);
        Integer orderStatus = null;
    // 주문 상태 변경
        for(ProductOrderGroup entity : orderGroupList){

            if(type == ProductOrderType.PICKUP) orderStatus = findOrderStatus(entity.getIdx());
            else if(type == ProductOrderType.STATION) orderStatus = findOrderStatus2(entity.getIdx());
            
            entity.setOrderStatus(orderStatus);
            repository.save(entity);
        }
    }

    
    public Integer findOrderStatus(Integer orderGroupIdx) {
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
        else return -2;
    }
    
    // 거점 주문의 orderGroup 상태값 관리
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
}

