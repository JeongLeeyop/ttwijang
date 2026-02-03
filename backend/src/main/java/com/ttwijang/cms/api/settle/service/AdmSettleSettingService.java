package com.ttwijang.cms.api.settle.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.settle.dto.AdmSettleSettingDto;
import com.ttwijang.cms.api.settle.dto.mapper.AdmSettleSettingMapper;
import com.ttwijang.cms.api.settle.repository.SettleSettingRepository;
import com.ttwijang.cms.entity.SettleSetting;

import lombok.AllArgsConstructor;

public interface AdmSettleSettingService {
    AdmSettleSettingDto.detail detail();
    void save(AdmSettleSettingDto.save dto);
}

@Service
@AllArgsConstructor
class AdmSettleSettingServiceImpl implements AdmSettleSettingService {
    private final SettleSettingRepository settleSettingRepository;

    @Override
    public AdmSettleSettingDto.detail detail() {
        Optional<SettleSetting> optional = settleSettingRepository.findTopByOrderByIdxDesc();
        if (optional.isEmpty()) return null;
        return AdmSettleSettingMapper.INSTANCE.entityToDetailDto(optional.get());
    }

    @Override
    public void save(AdmSettleSettingDto.save dto) {
        SettleSetting entity = AdmSettleSettingMapper.INSTANCE.saveDtoToEntity(dto);
        settleSettingRepository.save(entity);
    }
    
}
