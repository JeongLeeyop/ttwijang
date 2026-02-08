package com.ttwijang.cms.api.region.service;

import com.ttwijang.cms.api.region.dto.RegionCodeDto;
import com.ttwijang.cms.api.region.repository.RegionCodeRepository;
import com.ttwijang.cms.entity.RegionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 지역 코드 Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionCodeService {

    private final RegionCodeRepository regionCodeRepository;

    /**
     * 시/도 목록 조회
     * @return 시/도 목록
     */
    public List<RegionCodeDto.Response> getSidoList() {
        List<RegionCode> sidoList = regionCodeRepository.findByLevelAndEnabledTrueOrderBySortOrder(1);
        return sidoList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 시/군/구 목록 조회
     * @param parentCode 시/도 코드
     * @return 시/군/구 목록
     */
    public List<RegionCodeDto.Response> getSigunguList(String parentCode) {
        List<RegionCode> sigunguList = regionCodeRepository.findByParentCodeAndEnabledTrueOrderBySortOrder(parentCode);
        return sigunguList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity to DTO
     */
    private RegionCodeDto.Response toResponse(RegionCode regionCode) {
        return RegionCodeDto.Response.builder()
                .uid(regionCode.getUid())
                .code(regionCode.getCode())
                .name(regionCode.getName())
                .parentCode(regionCode.getParentCode())
                .level(regionCode.getLevel())
                .sortOrder(regionCode.getSortOrder())
                .build();
    }
}
