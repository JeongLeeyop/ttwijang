package com.ttwijang.cms.api.shop.dto.mapper;

import java.time.LocalTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.shop.dto.AdmShopDto;
import com.ttwijang.cms.api.shop.dto.AdmShopPickupTimeDto;
import com.ttwijang.cms.entity.Shop;
import com.ttwijang.cms.entity.ShopPickupTime;

@Mapper
public interface AdmShopMapper {
    AdmShopMapper INSTANCE = Mappers.getMapper(AdmShopMapper.class);

    AdmShopDto.list entityToListDto(Shop entity);

    @Mapping(target = "pickupTimes", ignore = true)
    AdmShopDto.detail entityToDetailDtoNormal(Shop entity);

    @Mapping(target = "pickupTimes", ignore = true)
    Shop addDtoToEntity(AdmShopDto.add addDto);

    @Mapping(target = "pickupTimes", ignore = true)
    Shop updateDtoToEntity(AdmShopDto.update updateDto, @MappingTarget Shop entity);

    default AdmShopDto.detail entityToDetailDto(Shop entity) {
        AdmShopDto.detail dto = entityToDetailDtoNormal(entity);
        for (ShopPickupTime item : entity.getPickupTimes()) {
            if (item.getWeekDay() == 0) dto.getPickupTimes().getMon().add(item.getPickupTime());
            if (item.getWeekDay() == 1) dto.getPickupTimes().getTue().add(item.getPickupTime());
            if (item.getWeekDay() == 2) dto.getPickupTimes().getWed().add(item.getPickupTime());
            if (item.getWeekDay() == 3) dto.getPickupTimes().getThu().add(item.getPickupTime());
            if (item.getWeekDay() == 4) dto.getPickupTimes().getFri().add(item.getPickupTime());
            if (item.getWeekDay() == 5) dto.getPickupTimes().getSat().add(item.getPickupTime());
            if (item.getWeekDay() == 6) dto.getPickupTimes().getSun().add(item.getPickupTime());
        }
        return dto;
    }

    /*
    default Shop addDtoToEntity(AdmShopDto.add addDto) {
        Shop entity = addDtoToEntityNormal(addDto);
        setPickupTimes(entity, addDto.getPickupTimes());
        return entity;
    }

    default Shop updateDtoToEntity(AdmShopDto.update updateDto, Shop entity) {
        entity = updateDtoToEntityNormal(updateDto, entity);
        setPickupTimes(entity, updateDto.getPickupTimes());
        return entity;
    }
    */

    default void setPickupTimes(Shop entity, AdmShopPickupTimeDto.save pickupTimes) {
        for (LocalTime pickupTime : pickupTimes.getMon()) entity.getPickupTimes().add(new ShopPickupTime(0, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getTue()) entity.getPickupTimes().add(new ShopPickupTime(1, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getWed()) entity.getPickupTimes().add(new ShopPickupTime(2, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getThu()) entity.getPickupTimes().add(new ShopPickupTime(3, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getFri()) entity.getPickupTimes().add(new ShopPickupTime(4, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getSat()) entity.getPickupTimes().add(new ShopPickupTime(5, pickupTime, entity));
        for (LocalTime pickupTime : pickupTimes.getSun()) entity.getPickupTimes().add(new ShopPickupTime(6, pickupTime, entity));
    }
}
