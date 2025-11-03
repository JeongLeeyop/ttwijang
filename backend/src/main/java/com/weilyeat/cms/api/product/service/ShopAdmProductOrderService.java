package com.weilyeat.cms.api.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.AdmProductOrderDto;
import com.weilyeat.cms.api.product.dto.ShopAdmProductOrderDto;
import com.weilyeat.cms.api.product.dto.mapper.ShopAdmProductOrderMapper;
import com.weilyeat.cms.api.product.repository.AdmProductOrderRepository;
import com.weilyeat.cms.api.product.repository.query.AdmProductOrderQuery;
import com.weilyeat.cms.api.product.repository.query.ShopAdmProductOrderQuery;
import com.weilyeat.cms.api.product.repository.search.AdmProductOrderSearch;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;
import org.springframework.data.domain.PageImpl;

import lombok.AllArgsConstructor;

public interface ShopAdmProductOrderService {
    Page<AdmProductOrderDto.listByDay> list(SinghaUser authUser, AdmProductOrderSearch search, Pageable pageable);
    AdmProductOrderDto.totalStatistics getTotalStatistics(SinghaUser authUser, AdmProductOrderSearch search);

}

@Service
@AllArgsConstructor
class ShopAdmProductOrderServiceImpl implements ShopAdmProductOrderService {
    private final ShopAdmProductOrderQuery shopAdmProductOrderQuery;
    private final AdmProductOrderRepository orderRepository;
    private final AdmProductOrderQuery orderQuery;

    @Override
    public Page<AdmProductOrderDto.listByDay> list(SinghaUser authUser,AdmProductOrderSearch search, Pageable pageable) {
        
        User user = authUser.getUser();
        search.setShopId(user.getShopIdx());
        List<AdmProductOrderDto.listByDay> list = orderQuery.listByDay(search, pageable);
        int count = orderQuery.countByDay(search);
        return new PageImpl<>(list, pageable, count);
        // return orderRepository.findAll(search.search(), pageable).map(x -> ShopAdmProductOrderMapper.INSTANCE.entitiyToListDto(x));
    }

        @Override
    public AdmProductOrderDto.totalStatistics getTotalStatistics(SinghaUser authUser, AdmProductOrderSearch search) {
        User user = authUser.getUser();
        search.setShopId(user.getShopIdx());
        return orderQuery.getTotalStatistics(search);
    }

}
