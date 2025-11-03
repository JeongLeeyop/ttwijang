package com.weilyeat.cms.api.shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.shop.dto.AdmShopDto;
import com.weilyeat.cms.api.shop.dto.ShopDto;
import com.weilyeat.cms.api.shop.dto.ShopDto.pickupTime;
import com.weilyeat.cms.api.shop.dto.mapper.ShopMapper;
import com.weilyeat.cms.api.shop.dto.search.ShopSearch;
import com.weilyeat.cms.api.shop.repository.ShopRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Shop;
import com.weilyeat.cms.entity.ShopPickupTime;

import lombok.AllArgsConstructor;

public interface ShopService {
    List<ShopDto.list> list();
    List<ShopDto.pickupTime> getPickupTimes(Integer idx);
    ShopDto.detail detail(Integer idx);
}

@Service
@AllArgsConstructor
class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public List<ShopDto.list> list() {
        ShopSearch search = new ShopSearch();
        List<Shop> datas = (List) shopRepository.findAll(search.search());
        return datas.stream().map((e) -> ShopMapper.INSTANCE.entityToListDto(e)).collect(Collectors.toList());
    }

    @Override
    public List<pickupTime> getPickupTimes(Integer idx) {
        Shop entity = shopRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
        
        List<ShopDto.pickupTime> pickupTimes = new ArrayList<ShopDto.pickupTime>();
        for (ShopPickupTime pickupTime : entity.getPickupTimes()) {
            // TODO 임의로 월요일(0) 검색, 추후 요일별로 바뀌면 비즈니스 로직 개선
            if (pickupTime.getWeekDay() == 0) {
                pickupTimes.add(new ShopDto.pickupTime(pickupTime.getPickupTime().toString()));
            }
        }
        return pickupTimes;
    }

    @Override
    public ShopDto.detail detail(Integer idx) {
        Shop entity = shopRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
        ShopDto.detail dto = ShopMapper.INSTANCE.entityToDetailDto(entity);
        return dto;
    }

}