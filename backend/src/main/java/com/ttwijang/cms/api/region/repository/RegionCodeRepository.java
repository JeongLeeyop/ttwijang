package com.ttwijang.cms.api.region.repository;

import com.ttwijang.cms.entity.RegionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 지역 코드 Repository
 */
@Repository
public interface RegionCodeRepository extends JpaRepository<RegionCode, String> {

    /**
     * 레벨별 지역 코드 조회
     * @param level 1: 시/도, 2: 시/군/구
     * @return 지역 코드 목록
     */
    List<RegionCode> findByLevelAndEnabledTrueOrderBySortOrder(Integer level);

    /**
     * 부모 코드로 하위 지역 조회
     * @param parentCode 부모 코드
     * @return 지역 코드 목록
     */
    List<RegionCode> findByParentCodeAndEnabledTrueOrderBySortOrder(String parentCode);

    /**
     * 코드로 지역 조회
     * @param code 지역 코드
     * @return 지역 코드
     */
    RegionCode findByCode(String code);
}
