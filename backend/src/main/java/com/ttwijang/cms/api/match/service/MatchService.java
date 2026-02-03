package com.ttwijang.cms.api.match.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.match.dto.MatchDto;
import com.ttwijang.cms.api.match.repository.FutsalMatchRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.Team;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final FutsalMatchRepository matchRepository;
    private final TeamRepository teamRepository;

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
        } else if (status != null && region != null) {
            matches = matchRepository.findByStatusAndRegion(status, region, pageable);
        } else if (status != null) {
            matches = matchRepository.findByStatus(status, pageable);
        } else {
            matches = matchRepository.findAll(pageable);
        }
        
        return matches.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 매치 조회 (캘린더용)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByDateRange(LocalDate startDate, LocalDate endDate, 
            Pageable pageable) {
        return matchRepository.findByMatchDateBetween(startDate, endDate, pageable)
                .map(this::toListResponse);
    }

    /**
     * 팀별 매치 조회
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeam(String teamUid, Pageable pageable) {
        return matchRepository.findByHostTeamUid(teamUid, pageable)
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

        // 매치 성사
        match.setGuestTeamUid(request.getApplicantTeamUid());
        match.setStatus(FutsalMatch.FutsalMatchStatus.MATCHED);
        match = matchRepository.save(match);

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

        return MatchDto.DetailResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .hostTeamGrade(hostTeam != null ? hostTeam.getGrade() : "")
                .guestTeamUid(match.getGuestTeamUid())
                .guestTeamName(guestTeam != null ? guestTeam.getName() : "")
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
                .createdDate(match.getCreatedDate())
                .build();
    }

    private MatchDto.ListResponse toListResponse(FutsalMatch match) {
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid()).orElse(null);

        return MatchDto.ListResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .matchType(match.getMatchType())
                .matchFormat(match.getMatchFormat())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .stadiumName(match.getStadiumName())
                .region(match.getRegionSido() + " " + (match.getRegionSigungu() != null ? match.getRegionSigungu() : ""))
                .fee(match.getFee())
                .status(match.getStatus())
                .build();
    }
}
