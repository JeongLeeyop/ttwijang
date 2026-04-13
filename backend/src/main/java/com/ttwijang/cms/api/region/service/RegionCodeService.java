package com.ttwijang.cms.api.region.service;

import com.ttwijang.cms.api.league.repository.LeagueRepository;
import com.ttwijang.cms.api.region.dto.RegionCodeDto;
import com.ttwijang.cms.api.region.repository.RegionCodeRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.League;
import com.ttwijang.cms.entity.RegionCode;
import com.ttwijang.cms.entity.Team;
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
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

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
     * 전체 시/군/구 목록 조회 (시/도 구분 없이)
     * @return 전체 시/군/구 목록
     */
    public List<RegionCodeDto.Response> getAllSigunguList() {
        return regionCodeRepository.findByLevelAndEnabledTrueOrderBySortOrder(2)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 지역 코드로 시/군/구 이름 조회
     * @param code 지역 코드 (시/군/구 레벨)
     * @return 지역명 (예: "진주시"), 없으면 null
     */
    public String resolveRegionName(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        RegionCode regionCode = regionCodeRepository.findByCode(code);
        return regionCode != null ? regionCode.getName() : null;
    }

    /**
     * 시/도 추가 (관리자 전용)
     */
    @Transactional
    public RegionCodeDto.Response createSido(RegionCodeDto.SidoCreateRequest req) {
        if (regionCodeRepository.findByCode(req.getCode()) != null) {
            throw new IllegalArgumentException("이미 존재하는 코드입니다: " + req.getCode());
        }
        RegionCode regionCode = RegionCode.builder()
                .code(req.getCode())
                .name(req.getName())
                .parentCode(null)
                .level(1)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 99)
                .enabled(true)
                .build();
        return toResponse(regionCodeRepository.save(regionCode));
    }

    /**
     * 시/군/구 추가 (관리자 전용)
     */
    @Transactional
    public RegionCodeDto.Response createSigungu(RegionCodeDto.SigunguCreateRequest req) {
        RegionCode parent = regionCodeRepository.findByCode(req.getParentCode());
        if (parent == null) {
            throw new IllegalArgumentException("존재하지 않는 시/도 코드입니다: " + req.getParentCode());
        }
        if (regionCodeRepository.findByCode(req.getCode()) != null) {
            throw new IllegalArgumentException("이미 존재하는 코드입니다: " + req.getCode());
        }
        RegionCode regionCode = RegionCode.builder()
                .code(req.getCode())
                .name(req.getName())
                .parentCode(req.getParentCode())
                .level(2)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 99)
                .enabled(true)
                .build();
        return toResponse(regionCodeRepository.save(regionCode));
    }

    /**
     * 시/도 코드로 해당 지역의 리그 목록 조회 (관리자 전용)
     */
    public List<RegionCodeDto.RegionLeagueSummary> getLeaguesBySidoCode(String sidoCode) {
        RegionCode sido = regionCodeRepository.findByCode(sidoCode);
        if (sido == null) {
            throw new IllegalArgumentException("존재하지 않는 시/도 코드입니다: " + sidoCode);
        }
        List<League> leagues = leagueRepository.findByRegionSidoOrderByCreatedDateDesc(sido.getName());
        return leagues.stream().map(l -> RegionCodeDto.RegionLeagueSummary.builder()
                .uid(l.getUid())
                .name(l.getName())
                .regionSigungu(l.getRegionSigungu())
                .season(l.getSeason())
                .status(l.getStatus() != null ? l.getStatus().name() : null)
                .maxTeams(l.getMaxTeams())
                .currentTeams(l.getCurrentTeams())
                .build()).collect(Collectors.toList());
    }

    /**
     * 시/도 코드로 해당 지역의 팀 목록 조회 (관리자 전용)
     */
    public List<RegionCodeDto.RegionTeamSummary> getTeamsBySidoCode(String sidoCode) {
        RegionCode sido = regionCodeRepository.findByCode(sidoCode);
        if (sido == null) {
            throw new IllegalArgumentException("존재하지 않는 시/도 코드입니다: " + sidoCode);
        }
        List<Team> teams = teamRepository.findByRegionSidoAndStatusNotOrderByCreatedDateDesc(
                sido.getName(), Team.TeamStatus.DELETED);
        return teams.stream().map(t -> RegionCodeDto.RegionTeamSummary.builder()
                .uid(t.getUid())
                .name(t.getName())
                .regionSigungu(t.getRegionSigungu())
                .status(t.getStatus() != null ? t.getStatus().name() : null)
                .memberCount(t.getMemberCount())
                .build()).collect(Collectors.toList());
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
                .enabled(regionCode.getEnabled())
                .build();
    }
}
