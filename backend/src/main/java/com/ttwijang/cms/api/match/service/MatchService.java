package com.ttwijang.cms.api.match.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.match.dto.MatchDto;
import com.ttwijang.cms.api.match.repository.FutsalMatchRepository;
import com.ttwijang.cms.api.match.repository.MatchApplicationRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.MatchApplication;
import com.ttwijang.cms.entity.Team;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final FutsalMatchRepository matchRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final TeamRepository teamRepository;

    private static final Map<String, Integer> FORMAT_MAX_PLAYERS = new HashMap<>();
    static {
        FORMAT_MAX_PLAYERS.put("4vs4", 8);
        FORMAT_MAX_PLAYERS.put("5vs5", 10);
        FORMAT_MAX_PLAYERS.put("6vs6", 12);
        FORMAT_MAX_PLAYERS.put("7vs7", 14);
        FORMAT_MAX_PLAYERS.put("FOUR_VS_FOUR", 8);
        FORMAT_MAX_PLAYERS.put("FIVE_VS_FIVE", 10);
        FORMAT_MAX_PLAYERS.put("SIX_VS_SIX", 12);
        FORMAT_MAX_PLAYERS.put("SEVEN_VS_SEVEN", 14);
    }

    /**
     * 매치 방식에 따른 최대 인원 (양 팀 합산)
     */
    public static int getMaxPlayersByFormat(String format) {
        return FORMAT_MAX_PLAYERS.getOrDefault(format, 10);
    }

    /**
     * 매치 생성
     */
    @Transactional
    public MatchDto.DetailResponse createMatch(MatchDto.CreateRequest request, String userUid) {
        Team hostTeam = teamRepository.findByUid(request.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 생성 권한이 없습니다.");
        }

        FutsalMatch match = FutsalMatch.builder()
                .hostTeamUid(request.getHostTeamUid())
                .matchType(request.getMatchType())
                .matchFormat(request.getMatchFormat())
                .matchDate(request.getMatchDate())
                .matchTime(request.getMatchTime())
                .durationHours(request.getDurationHours())
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .fee(request.getFee() != null ? request.getFee() : 0)
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .additionalInfo(request.getAdditionalInfo())
                .status(FutsalMatch.FutsalMatchStatus.RECRUITING)
                .build();

        match = matchRepository.save(match);
        return toDetailResponse(match);
    }

    /**
     * 매치 상세 조회
     */
    @Transactional(readOnly = true)
    public MatchDto.DetailResponse getMatchDetail(String uid) {
        FutsalMatch match = matchRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));
        return toDetailResponse(match);
    }

    /**
     * 매치 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchList(String region, LocalDate date, 
            FutsalMatch.FutsalMatchStatus status, Pageable pageable) {
        Page<FutsalMatch> matches;
        
        if (date != null) {
            matches = matchRepository.findByMatchDate(date, pageable);
        } else if (status != null && region != null && !region.isEmpty()) {
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                matches = matchRepository.findByStatusAndRegionSidoAndSigungu(status, parts[0], parts[1], pageable);
            } else {
                matches = matchRepository.findByStatusAndRegion(status, region, pageable);
            }
        } else if (status != null) {
            matches = matchRepository.findByStatus(status, pageable);
        } else {
            matches = matchRepository.findAll(pageable);
        }
        
        return matches.map(this::toListResponse);
    }

    /**
     * 매치 목록 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchListBySigungu(String sigungu, LocalDate date,
            FutsalMatch.FutsalMatchStatus status, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (date != null) {
            matches = matchRepository.findByMatchDate(date, pageable);
        } else if (status != null && sigungu != null && !sigungu.isEmpty()) {
            matches = matchRepository.findByStatusAndSigungu(status, sigungu, pageable);
        } else if (status != null) {
            matches = matchRepository.findByStatus(status, pageable);
        } else {
            matches = matchRepository.findAll(pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 매치 조회 (캘린더용) - 지역 필터 지원
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByDateRange(LocalDate startDate, LocalDate endDate,
            String region, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (region != null && !region.isEmpty()) {
            // 지역 필터가 있으면 "시도 시군구" 형태에서 파싱하여 regionSido/regionSigungu로 검색
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                matches = matchRepository.findByMatchDateBetweenAndRegionSidoAndRegionSigungu(
                        startDate, endDate, parts[0], parts[1], pageable);
            } else {
                matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
            }
        } else {
            matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 매치 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByDateRangeBySigungu(LocalDate startDate, LocalDate endDate,
            String sigungu, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (sigungu != null && !sigungu.isEmpty()) {
            matches = matchRepository.findByMatchDateBetweenAndRegionSigungu(
                    startDate, endDate, sigungu, pageable);
        } else {
            matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * BR-06: 팀별 매치 조회 (주최팀 또는 상대팀으로 참여한 모든 매치)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeam(String teamUid, Pageable pageable) {
        return matchRepository.findByTeamUid(teamUid, pageable)
                .map(this::toListResponse);
    }

    /**
     * BR-06 + BR-08: 팀별 + 매치 타입별 조회
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeamAndType(String teamUid, FutsalMatch.MatchType matchType, Pageable pageable) {
        return matchRepository.findByTeamUidAndMatchType(teamUid, matchType, pageable)
                .map(this::toListResponse);
    }

    /**
     * 매치 신청
     */
    @Transactional
    public MatchDto.DetailResponse applyMatch(MatchDto.ApplyRequest request, String userUid) {
        FutsalMatch match = matchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        if (match.getStatus() != FutsalMatch.FutsalMatchStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 매치가 아닙니다.");
        }

        Team applicantTeam = teamRepository.findByUid(request.getApplicantTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!applicantTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 신청 권한이 없습니다.");
        }

        // 자기 팀에 신청하는지 확인
        if (match.getHostTeamUid().equals(request.getApplicantTeamUid())) {
            throw new IllegalArgumentException("자신의 팀에는 신청할 수 없습니다.");
        }

        // 이미 신청했는지 확인
        if (matchApplicationRepository.existsByMatchUidAndApplicantTeamUid(
                request.getMatchUid(), request.getApplicantTeamUid())) {
            throw new IllegalArgumentException("이미 신청한 매치입니다.");
        }

        // 정원 초과 확인
        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentApproved = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);
        if (currentApproved >= maxPlayers) {
            throw new IllegalArgumentException("모집 인원이 마감되었습니다.");
        }

        // 신청 저장
        MatchApplication application = MatchApplication.builder()
                .matchUid(match.getUid())
                .applicantTeamUid(request.getApplicantTeamUid())
                .applicantUserUid(userUid)
                .status(MatchApplication.ApplicationStatus.PENDING)
                .message(request.getMessage())
                .build();
        matchApplicationRepository.save(application);

        // 친선경기(FRIENDLY)는 상대팀 매칭 즉시 성사
        if (match.getMatchType() == FutsalMatch.MatchType.FRIENDLY) {
            match.setGuestTeamUid(request.getApplicantTeamUid());
            match.setStatus(FutsalMatch.FutsalMatchStatus.MATCHED);
            match = matchRepository.save(match);
        }

        return toDetailResponse(match);
    }

    /**
     * 매치 취소
     */
    @Transactional
    public void cancelMatch(String matchUid, String userUid) {
        FutsalMatch match = matchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 취소 권한이 없습니다.");
        }

        match.setStatus(FutsalMatch.FutsalMatchStatus.CANCELLED);
        matchRepository.save(match);
    }

    private MatchDto.DetailResponse toDetailResponse(FutsalMatch match) {
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid()).orElse(null);
        Team guestTeam = match.getGuestTeamUid() != null ? 
                teamRepository.findByUid(match.getGuestTeamUid()).orElse(null) : null;

        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentPlayers = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);

        return MatchDto.DetailResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamLogoUrl(hostTeam != null ? hostTeam.getLogoFileUid() : null)
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .guestTeamUid(match.getGuestTeamUid())
                .guestTeamName(guestTeam != null ? guestTeam.getName() : "")
                .guestTeamLogoUrl(guestTeam != null ? guestTeam.getLogoFileUid() : null)
                .matchType(match.getMatchType())
                .matchFormat(match.getMatchFormat())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .durationHours(match.getDurationHours())
                .stadiumName(match.getStadiumName())
                .stadiumAddress(match.getStadiumAddress())
                .fee(match.getFee())
                .regionSido(match.getRegionSido())
                .regionSigungu(match.getRegionSigungu())
                .additionalInfo(match.getAdditionalInfo())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .status(match.getStatus())
                .maxPlayers(maxPlayers)
                .currentPlayers((int) currentPlayers)
                .createdDate(match.getCreatedDate())
                .build();
    }

    private MatchDto.ListResponse toListResponse(FutsalMatch match) {
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid()).orElse(null);
        String region = "";
        if (hostTeam != null) {
            region = (hostTeam.getRegionSido() != null ? hostTeam.getRegionSido() : "")
                    + " " + (hostTeam.getRegionSigungu() != null ? hostTeam.getRegionSigungu() : "");
        }

        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentPlayers = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);

        return MatchDto.ListResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamLogoUrl(hostTeam != null ? hostTeam.getLogoFileUid() : null)
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .matchType(match.getMatchType())
                .matchFormat(match.getMatchFormat())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .stadiumName(match.getStadiumName())
                .region(region.trim())
                .fee(match.getFee())
                .status(match.getStatus())
                .maxPlayers(maxPlayers)
                .currentPlayers((int) currentPlayers)
                .build();
    }
}
