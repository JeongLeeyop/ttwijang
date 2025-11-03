package com.weilyeat.cms.api.delivery_price.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.delivery_price.dto.AdmDeliveryPriceDto;
import com.weilyeat.cms.api.delivery_price.dto.mapper.AdmDeliveryPriceMapper;
import com.weilyeat.cms.api.delivery_price.repository.DeliveryPriceRepository;
import com.weilyeat.cms.entity.DeliveryPrice;

import lombok.AllArgsConstructor;

public interface AdmDeliveryPriceService {
    AdmDeliveryPriceDto.detail detail();
    void save(AdmDeliveryPriceDto.save dto);
}

@Service
@AllArgsConstructor
class AdmdeliveryPriceServiceImpl implements AdmDeliveryPriceService {
    private final DeliveryPriceRepository deliveryPriceRepository;

    @Override
    public AdmDeliveryPriceDto.detail detail() {
        Optional<DeliveryPrice> optional = deliveryPriceRepository.findTopByOrderByIdxDesc();
        if (optional.isEmpty()) return null;
        return AdmDeliveryPriceMapper.INSTANCE.entityToDetailDto(optional.get());
    }

    @Override
    public void save(AdmDeliveryPriceDto.save dto) {
        DeliveryPrice entity = AdmDeliveryPriceMapper.INSTANCE.saveDtoToEntity(dto);
        deliveryPriceRepository.save(entity);
    }
    
}
