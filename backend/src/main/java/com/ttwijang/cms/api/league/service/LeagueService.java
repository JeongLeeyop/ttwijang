package com.ttwijang.cms.api.league.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.service.CashService;
import com.ttwijang.cms.api.league.dto.LeagueDto;
import com.ttwijang.cms.api.league.repository.LeagueMatchApplicationRepository;
import com.ttwijang.cms.api.league.repository.LeagueMatchRepository;
import com.ttwijang.cms.api.league.repository.LeagueRepository;
import com.ttwijang.cms.api.league.repository.LeagueTeamRepository;
import com.ttwijang.cms.api.manner.repository.MannerRatingRepository;
import com.ttwijang.cms.api.match.repository.MatchConfigRepository;
import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.api.team.repository.TeamMemberRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.League;
import com.ttwijang.cms.entity.LeagueMatch;
import com.ttwijang.cms.entity.LeagueMatchApplication;
import com.ttwijang.cms.entity.LeagueTeam;
import com.ttwijang.cms.entity.MatchConfig;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamMember;
import com.ttwijang.cms.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueTeamRepository leagueTeamRepository;
    private final LeagueMatchRepository leagueMatchRepository;
    private final LeagueMatchApplicationRepository leagueMatchApplicationRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final MannerRatingRepository mannerRatingRepository;
    private final MatchConfigRepository matchConfigRepository;
    private final CashService cashService;
    private final NotificationService notificationService;

    /**
     * 리그 목록 조회
     * BR-04: 지역별 리그 전환 지원 (시/군/구 포함)
     */
    @Transactional(readOnly = true)
    public Page<LeagueDto.ListResponse> getLeagueList(String regionSido, String regionSigungu, 
            League.LeagueStatus status, Pageable pageable) {
        Page<League> leagues;
        if (regionSido != null && !regionSido.isEmpty()) {
            leagues = leagueRepository.findByRegionAndStatus(regionSido, regionSigungu, status, pageable);
        } else if (regionSigungu != null && !regionSigungu.isEmpty()) {
            // regionCode로 조회 시: 도 필터 없이 시/군/구 이름으로만 조회
            leagues = leagueRepository.findBySigunguAndStatus(regionSigungu, status, pageable);
        } else if (status != null) {
            leagues = leagueRepository.findByStatus(status, pageable);
        } else {
            leagues = leagueRepository.findAll(pageable);
        }
        return leagues.map(this::toListResponse);
    }

    /**
     * 리그 상세 조회
     */
    @Transactional(readOnly = true)
    public LeagueDto.DetailResponse getLeagueDetail(String uid) {
        League league = leagueRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        List<LeagueDto.LeagueStandingResponse> standings = getLeagueStandings(uid);
        List<LeagueDto.MatchResponse> upcomingMatches = getUpcomingMatches(uid, 5);
        List<LeagueDto.MatchResponse> recentResults = getRecentResults(uid, 5);

        return LeagueDto.DetailResponse.builder()
                .uid(league.getUid())
                .name(league.getName())
                .description(league.getDescription())
                .season(league.getSeason())
                .startDate(league.getStartDate())
                .endDate(league.getEndDate())
                .regionSido(league.getRegionSido())
                .regionSigungu(league.getRegionSigungu())
                .currentTeams(league.getCurrentTeams())
                .maxTeams(league.getMaxTeams())
                .status(league.getStatus())
                .rules(league.getRules())
                .introContent(league.getIntroContent())
                .participationPoints(league.getParticipationPoints())
                .standings(standings)
                .upcomingMatches(upcomingMatches)
                .recentResults(recentResults)
                .build();
    }

    /**
     * 리그 순위표 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.LeagueStandingResponse> getLeagueStandings(String leagueUid) {
        List<LeagueTeam> leagueTeams = leagueTeamRepository.findByLeagueUidOrderByRanking(leagueUid);
        List<LeagueDto.LeagueStandingResponse> standings = new ArrayList<>();
        
        int ranking = 1;
        for (LeagueTeam lt : leagueTeams) {
            Team team = teamRepository.findByUid(lt.getTeamUid()).orElse(null);
            if (team != null) {
                standings.add(LeagueDto.LeagueStandingResponse.builder()
                        .ranking(ranking++)
                        .teamUid(team.getUid())
                        .teamCode(team.getTeamCode())
                        .teamName(team.getName())
                        .teamLogoUrl(team.getLogoFileUid())
                        .played(lt.getPlayed())
                        .wins(lt.getWins())
                        .draws(lt.getDraws())
                        .losses(lt.getLosses())
                        .goalsFor(lt.getGoalsFor())
                        .goalsAgainst(lt.getGoalsAgainst())
                        .goalDifference(lt.getGoalDifference())
                        .points(lt.getPoints())
                        .mannerScore(lt.getMannerScore())
                        .build());
            }
        }
        return standings;
    }

    /**
     * 리그 일정 조회 (월별)
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getLeagueSchedule(String leagueUid, Integer year, Integer month) {
        // year/month가 없으면 현재 월 기준
        LocalDate now = LocalDate.now();
        int targetYear = year != null ? year : now.getYear();
        int targetMonth = month != null ? month : now.getMonthValue();
        
        LocalDate startDate = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        List<LeagueMatch> matches = leagueMatchRepository.findByLeagueUidAndMatchDateBetween(
                leagueUid, startDate, endDate);
        
        return matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    /**
     * [관리자] 리그 전체 경기 목록 조회 (날짜 제한 없음)
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getAllLeagueMatches(String leagueUid) {
        return leagueMatchRepository.findByLeagueUid(leagueUid).stream()
                .sorted(Comparator.comparing(LeagueMatch::getMatchDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(LeagueMatch::getRound, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    /**
     * 다가오는 경기 조회 (DB 레벨에서 SCHEDULED 필터링)
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getUpcomingMatches(String leagueUid, int limit) {
        List<LeagueMatch> matches = leagueMatchRepository.findScheduledByLeagueUid(
                leagueUid, PageRequest.of(0, limit));

        return matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    /**
     * 팀이 참가 중인 리그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.ListResponse> getLeaguesByTeam(String teamUid) {
        List<LeagueTeam> leagueTeams = leagueTeamRepository.findByTeamUid(teamUid);
        return leagueTeams.stream()
                .map(lt -> leagueRepository.findByUid(lt.getLeagueUid()).orElse(null))
                .filter(league -> league != null)
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 최근 경기 결과 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getRecentResults(String leagueUid, int limit) {
        List<LeagueMatch> matches = leagueMatchRepository.findCompletedMatchesByLeagueUid(
                leagueUid, PageRequest.of(0, limit));
        
        return matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    /**
     * 경기 결과 입력 (순위 자동 업데이트)
     * - 이미 COMPLETED 상태면 거부 (전적 중복 누적 방지)
     * - 점수 음수 입력 차단
     */
    @Transactional
    public LeagueDto.MatchResponse updateMatchResult(LeagueDto.MatchResultRequest request) {
        LeagueMatch match = leagueMatchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 경기입니다."));

        if (match.getStatus() == LeagueMatch.MatchStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 결과가 입력된 경기입니다.");
        }
        if (match.getStatus() == LeagueMatch.MatchStatus.CANCELLED) {
            throw new IllegalArgumentException("취소된 경기에는 결과를 입력할 수 없습니다.");
        }
        if (request.getHomeScore() < 0 || request.getAwayScore() < 0) {
            throw new IllegalArgumentException("점수는 0 이상이어야 합니다.");
        }

        match.setHomeScore(request.getHomeScore());
        match.setAwayScore(request.getAwayScore());
        match.setStatus(LeagueMatch.MatchStatus.COMPLETED);
        match = leagueMatchRepository.save(match);

        // 팀 전적 업데이트
        updateTeamStats(match);

        return toMatchResponse(match);
    }

    /**
     * 팀 전적 업데이트
     */
    private void updateTeamStats(LeagueMatch match) {
        LeagueTeam homeTeam = leagueTeamRepository.findByLeagueUidAndTeamUid(
                match.getLeagueUid(), match.getHomeTeamUid()).orElse(null);
        LeagueTeam awayTeam = leagueTeamRepository.findByLeagueUidAndTeamUid(
                match.getLeagueUid(), match.getAwayTeamUid()).orElse(null);

        if (homeTeam != null && awayTeam != null) {
            int homeScore = match.getHomeScore();
            int awayScore = match.getAwayScore();

            // 경기 수 증가
            homeTeam.setPlayed(homeTeam.getPlayed() + 1);
            awayTeam.setPlayed(awayTeam.getPlayed() + 1);

            // 득/실점 업데이트
            homeTeam.setGoalsFor(homeTeam.getGoalsFor() + homeScore);
            homeTeam.setGoalsAgainst(homeTeam.getGoalsAgainst() + awayScore);
            awayTeam.setGoalsFor(awayTeam.getGoalsFor() + awayScore);
            awayTeam.setGoalsAgainst(awayTeam.getGoalsAgainst() + homeScore);

            // 득실차 계산
            homeTeam.setGoalDifference(homeTeam.getGoalsFor() - homeTeam.getGoalsAgainst());
            awayTeam.setGoalDifference(awayTeam.getGoalsFor() - awayTeam.getGoalsAgainst());

            // 승/무/패 및 승점 계산
            if (homeScore > awayScore) {
                // 홈팀 승
                homeTeam.setWins(homeTeam.getWins() + 1);
                homeTeam.setPoints(homeTeam.getPoints() + 3);
                awayTeam.setLosses(awayTeam.getLosses() + 1);
            } else if (homeScore < awayScore) {
                // 원정팀 승
                awayTeam.setWins(awayTeam.getWins() + 1);
                awayTeam.setPoints(awayTeam.getPoints() + 3);
                homeTeam.setLosses(homeTeam.getLosses() + 1);
            } else {
                // 무승부
                homeTeam.setDraws(homeTeam.getDraws() + 1);
                homeTeam.setPoints(homeTeam.getPoints() + 1);
                awayTeam.setDraws(awayTeam.getDraws() + 1);
                awayTeam.setPoints(awayTeam.getPoints() + 1);
            }

            leagueTeamRepository.save(homeTeam);
            leagueTeamRepository.save(awayTeam);
        }
    }

    /**
     * BR-11: 리그 생성 (최고관리자 전용)
     */
    @Transactional
    public LeagueDto.DetailResponse createLeague(LeagueDto.CreateRequest request) {
        League league = League.builder()
                .name(request.getName())
                .description(request.getDescription())
                .season(request.getSeason())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .maxTeams(request.getMaxTeams())
                .currentTeams(0)
                .status(League.LeagueStatus.RECRUITING)
                .rules(request.getRules())
                .introContent(request.getIntroContent())
                .participationPoints(request.getParticipationPoints() != null ? request.getParticipationPoints() : 0)
                .bannerFileUid(request.getBannerFileUid())
                .build();

        league = leagueRepository.save(league);
        return getLeagueDetail(league.getUid());
    }

    /**
     * BR-11: 리그 수정 (최고관리자 전용)
     */
    @Transactional
    public LeagueDto.DetailResponse updateLeague(String uid, LeagueDto.UpdateRequest request) {
        League league = leagueRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        if (request.getName() != null) league.setName(request.getName());
        if (request.getDescription() != null) league.setDescription(request.getDescription());
        if (request.getSeason() != null) league.setSeason(request.getSeason());
        if (request.getStartDate() != null) league.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) league.setEndDate(request.getEndDate());
        if (request.getMaxTeams() != null) league.setMaxTeams(request.getMaxTeams());
        if (request.getStatus() != null) league.setStatus(request.getStatus());
        if (request.getRules() != null) league.setRules(request.getRules());
        if (request.getIntroContent() != null) league.setIntroContent(request.getIntroContent());
        if (request.getParticipationPoints() != null) league.setParticipationPoints(request.getParticipationPoints());
        if (request.getBannerFileUid() != null) league.setBannerFileUid(request.getBannerFileUid());

        league = leagueRepository.save(league);
        return getLeagueDetail(league.getUid());
    }

    /**
     * BR-11: 리그 삭제 (최고관리자 전용)
     * - 연관된 LeagueMatchApplication / LeagueMatch / LeagueTeam을 명시적으로 정리하여 고아 레코드 방지
     */
    @Transactional
    public void deleteLeague(String uid) {
        League league = leagueRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        // 1) 리그 매치 신청 내역 삭제
        List<LeagueMatch> matches = leagueMatchRepository.findByLeagueUid(uid);
        if (!matches.isEmpty()) {
            List<String> matchUids = matches.stream()
                    .map(LeagueMatch::getUid)
                    .collect(Collectors.toList());
            leagueMatchApplicationRepository.deleteByLeagueMatchUidIn(matchUids);
        }
        // 2) 리그 매치 삭제
        leagueMatchRepository.deleteAll(matches);
        // 3) 리그 참가 팀 삭제
        leagueTeamRepository.deleteAll(leagueTeamRepository.findByLeagueUid(uid));
        // 4) 리그 본체 삭제
        leagueRepository.delete(league);
    }

    /**
     * BR-12: 리그에 팀 추가 (최고관리자 전용)
     */
    @Transactional
    public LeagueDto.LeagueTeamResponse addTeamToLeague(String leagueUid, String teamUid) {
        League league = leagueRepository.findByUid(leagueUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 이미 등록된 팀인지 확인
        if (leagueTeamRepository.findByLeagueUidAndTeamUid(leagueUid, teamUid).isPresent()) {
            throw new IllegalArgumentException("이미 리그에 등록된 팀입니다.");
        }

        // 최대 팀 수 확인 — 실제 LeagueTeam 카운트 기준
        long actualCount = leagueTeamRepository.countByLeagueUid(leagueUid);
        if (league.getMaxTeams() != null && actualCount >= league.getMaxTeams()) {
            throw new IllegalArgumentException("리그 최대 참가 팀 수를 초과했습니다.");
        }

        LeagueTeam leagueTeam = LeagueTeam.builder()
                .leagueUid(leagueUid)
                .teamUid(teamUid)
                .ranking(0)
                .played(0)
                .wins(0)
                .draws(0)
                .losses(0)
                .goalsFor(0)
                .goalsAgainst(0)
                .goalDifference(0)
                .points(0)
                .mannerScore(0.0)
                .build();

        leagueTeamRepository.save(leagueTeam);

        // 참가 팀 수 업데이트
        league.setCurrentTeams((league.getCurrentTeams() != null ? league.getCurrentTeams() : 0) + 1);
        leagueRepository.save(league);

        return LeagueDto.LeagueTeamResponse.builder()
                .teamUid(team.getUid())
                .teamName(team.getName())
                .teamLogoUrl(team.getLogoFileUid())
                .leagueName(league.getName())
                .ranking(0)
                .points(0)
                .build();
    }

    /**
     * BR-12: 리그에서 팀 제거 (최고관리자 전용)
     */
    @Transactional
    public void removeTeamFromLeague(String leagueUid, String teamUid) {
        League league = leagueRepository.findByUid(leagueUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        LeagueTeam leagueTeam = leagueTeamRepository.findByLeagueUidAndTeamUid(leagueUid, teamUid)
                .orElseThrow(() -> new IllegalArgumentException("리그에 등록되지 않은 팀입니다."));

        leagueTeamRepository.delete(leagueTeam);

        league.setCurrentTeams(Math.max(0, (league.getCurrentTeams() != null ? league.getCurrentTeams() : 0) - 1));
        leagueRepository.save(league);
    }

    /**
     * 사용자 리그 참여 신청 (팀 OWNER 전용)
     * - 캐시 차감 (participationPoints > 0)
     * - 팀 배정 자동 처리
     * - 팀 전체 멤버 알림 발송
     */
    @Transactional
    public LeagueDto.LeagueTeamResponse applyToLeague(String leagueUid, String userUid) {
        League league = leagueRepository.findByUid(leagueUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        // 모집 중인 리그만 참가 가능 (IN_PROGRESS / COMPLETED 차단 — 캐시 차감 방지)
        if (league.getStatus() != League.LeagueStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 리그만 참가 신청이 가능합니다.");
        }

        // 사용자가 OWNER인 ACTIVE 팀 조회
        List<Team> ownedTeams = teamRepository.findByOwnerUid(userUid);
        Team team = ownedTeams.stream()
                .filter(t -> t.getStatus() == Team.TeamStatus.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("운영 중인 팀이 없습니다. 팀 운영자만 리그에 참여할 수 있습니다."));

        // 이미 참여한 팀 여부 확인
        if (leagueTeamRepository.findByLeagueUidAndTeamUid(leagueUid, team.getUid()).isPresent()) {
            throw new IllegalArgumentException("이미 리그에 참여한 팀입니다.");
        }

        // 최대 팀 수 확인 — 실제 LeagueTeam 카운트 기준 (currentTeams 동기화 어긋남 방지)
        long actualCount = leagueTeamRepository.countByLeagueUid(leagueUid);
        if (league.getMaxTeams() != null && actualCount >= league.getMaxTeams()) {
            throw new IllegalArgumentException("리그 최대 참가 팀 수에 도달했습니다.");
        }

        // 캐시 차감 (참여 포인트가 있는 경우)
        int points = league.getParticipationPoints() != null ? league.getParticipationPoints() : 0;
        if (points > 0) {
            cashService.use(CashDto.UseRequest.builder()
                    .amount(points)
                    .description("리그 참여비 - " + league.getName())
                    .referenceUid(leagueUid)
                    .referenceType("LEAGUE")
                    .build(), userUid);
        }

        // LeagueTeam 생성
        LeagueTeam leagueTeam = LeagueTeam.builder()
                .leagueUid(leagueUid)
                .teamUid(team.getUid())
                .ranking(0)
                .played(0)
                .wins(0)
                .draws(0)
                .losses(0)
                .goalsFor(0)
                .goalsAgainst(0)
                .goalDifference(0)
                .points(0)
                .mannerScore(0.0)
                .build();
        leagueTeamRepository.save(leagueTeam);

        league.setCurrentTeams((league.getCurrentTeams() != null ? league.getCurrentTeams() : 0) + 1);
        leagueRepository.save(league);

        // 팀 전체 APPROVED 멤버에게 알림 발송
        List<TeamMember> members = teamMemberRepository.findByTeamUidAndStatus(team.getUid(), TeamMember.MemberStatus.APPROVED);
        String actionUrl = "/league-detail/" + leagueUid;
        for (TeamMember member : members) {
            notificationService.createNotification(
                    member.getUserUid(),
                    Notification.NotificationType.LEAGUE,
                    "리그 참여 완료",
                    "우리 팀이 '" + league.getName() + "'에 참여했습니다.",
                    leagueUid,
                    "LEAGUE",
                    actionUrl
            );
        }

        return LeagueDto.LeagueTeamResponse.builder()
                .teamUid(team.getUid())
                .teamName(team.getName())
                .leagueName(league.getName())
                .ranking(0)
                .points(0)
                .build();
    }

    /**
     * 리그 매치 생성 (최고관리자 전용)
     */
    @Transactional
    public LeagueDto.MatchResponse createLeagueMatch(LeagueDto.CreateMatchRequest request) {
        League league = leagueRepository.findByUid(request.getLeagueUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));

        LeagueMatch match = LeagueMatch.builder()
                .leagueUid(request.getLeagueUid())
                .homeTeamUid(request.getHomeTeamUid())
                .awayTeamUid(request.getAwayTeamUid())
                .matchDate(request.getMatchDate())
                .matchTime(request.getMatchTime())
                .durationMinutes(request.getDurationMinutes())
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .status(LeagueMatch.MatchStatus.SCHEDULED)
                .round(request.getRound())
                .build();

        match = leagueMatchRepository.save(match);
        return toMatchResponse(match);
    }

    /**
     * 리그 참가 팀 목록 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.LeagueTeamResponse> getLeagueTeams(String leagueUid) {
        List<LeagueTeam> leagueTeams = leagueTeamRepository.findByLeagueUidOrderByRanking(leagueUid);
        League league = leagueRepository.findByUid(leagueUid).orElse(null);

        return leagueTeams.stream()
                .map(lt -> {
                    Team team = teamRepository.findByUid(lt.getTeamUid()).orElse(null);
                    if (team == null) return null;
                    return LeagueDto.LeagueTeamResponse.builder()
                            .teamUid(team.getUid())
                            .teamCode(team.getTeamCode())
                            .teamName(team.getName())
                            .teamLogoUrl(team.getLogoFileUid())
                            .leagueUid(leagueUid)
                            .leagueName(league != null ? league.getName() : "")
                            .regionSido(league != null ? league.getRegionSido() : "")
                            .regionSigungu(league != null ? league.getRegionSigungu() : "")
                            .ranking(lt.getRanking())
                            .points(lt.getPoints())
                            .build();
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    /**
     * 전체 리그 참가 팀 목록 조회 (지역/리그/키워드 필터 + 페이징)
     */
    @Transactional(readOnly = true)
    public Page<LeagueDto.LeagueTeamResponse> getAllLeagueTeams(
            String regionSido, String regionSigungu, String leagueUid, String keyword, Pageable pageable) {

        // 1. 필터 조건에 맞는 리그 목록 수집
        Map<String, League> leagueCache = new HashMap<>();

        if (leagueUid != null && !leagueUid.isEmpty()) {
            leagueRepository.findByUid(leagueUid).ifPresent(l -> leagueCache.put(l.getUid(), l));
        } else {
            List<League> allLeagues = leagueRepository.findAll();
            for (League l : allLeagues) {
                if (l.getStatus() != League.LeagueStatus.IN_PROGRESS) continue;
                // 시/군/구 우선 매칭 — 시/군/구가 지정되면 도는 무시 (시/군/구 이름은 사실상 고유)
                if (regionSigungu != null && !regionSigungu.isEmpty()) {
                    if (!regionSigungu.equals(l.getRegionSigungu())) continue;
                } else if (regionSido != null && !regionSido.isEmpty()) {
                    if (!regionSido.equals(l.getRegionSido())) continue;
                }
                leagueCache.put(l.getUid(), l);
            }
        }

        if (leagueCache.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 2. 해당 리그 팀 목록 조회
        List<String> targetLeagueUids = new ArrayList<>(leagueCache.keySet());
        List<LeagueTeam> leagueTeams = leagueTeamRepository.findByLeagueUidIn(targetLeagueUids);

        // 3. 팀 정보 매핑 + 키워드 필터
        List<LeagueDto.LeagueTeamResponse> responses = leagueTeams.stream()
                .map(lt -> {
                    Team team = teamRepository.findByUid(lt.getTeamUid()).orElse(null);
                    if (team == null) return null;
                    if (keyword != null && !keyword.isEmpty()
                            && !team.getName().contains(keyword)) return null;
                    League league = leagueCache.get(lt.getLeagueUid());
                    return LeagueDto.LeagueTeamResponse.builder()
                            .teamUid(team.getUid())
                            .teamCode(team.getTeamCode())
                            .teamName(team.getName())
                            .teamLogoUrl(team.getLogoFileUid())
                            .leagueUid(lt.getLeagueUid())
                            .leagueName(league != null ? league.getName() : "")
                            .regionSido(league != null ? league.getRegionSido() : "")
                            .regionSigungu(league != null ? league.getRegionSigungu() : "")
                            .ranking(lt.getRanking())
                            .points(lt.getPoints())
                            .build();
                })
                .filter(r -> r != null)
                .collect(Collectors.toList());

        // 4. 인메모리 페이징
        int total = responses.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        if (start >= total) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        return new PageImpl<>(responses.subList(start, end), pageable, total);
    }

    /**
     * 지역별 다가오는 리그 경기 조회 (모든 리그 통합)
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getUpcomingMatchesByRegion(String regionSigungu, int limit) {
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(0, limit);
        List<LeagueMatch> matches;

        if (regionSigungu != null && !regionSigungu.isEmpty()) {
            matches = leagueMatchRepository.findUpcomingByRegionSigungu(regionSigungu, today, pageable);
        } else {
            matches = leagueMatchRepository.findUpcomingAll(today, pageable);
        }

        return matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
    }

    private LeagueDto.ListResponse toListResponse(League league) {
        String sigungu = league.getRegionSigungu() != null ? league.getRegionSigungu() : "";
        return LeagueDto.ListResponse.builder()
                .uid(league.getUid())
                .name(league.getName())
                .season(league.getSeason())
                .region((league.getRegionSido() != null ? league.getRegionSido() : "") + (sigungu.isEmpty() ? "" : " " + sigungu))
                .regionSido(league.getRegionSido())
                .regionSigungu(league.getRegionSigungu())
                .startDate(league.getStartDate())
                .endDate(league.getEndDate())
                .currentTeams(league.getCurrentTeams())
                .maxTeams(league.getMaxTeams())
                .status(league.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public LeagueDto.MatchResponse getLeagueMatchDetail(String matchUid) {
        return getLeagueMatchDetail(matchUid, null);
    }

    @Transactional(readOnly = true)
    public LeagueDto.MatchResponse getLeagueMatchDetail(String matchUid, String userUid) {
        LeagueMatch match = leagueMatchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그 경기입니다."));
        return toMatchResponse(match, userUid);
    }

    private LeagueDto.MatchResponse toMatchResponse(LeagueMatch match) {
        return toMatchResponse(match, null);
    }

    private LeagueDto.MatchResponse toMatchResponse(LeagueMatch match, String currentUserUid) {
        Team homeTeam = teamRepository.findByUid(match.getHomeTeamUid()).orElse(null);
        Team awayTeam = teamRepository.findByUid(match.getAwayTeamUid()).orElse(null);
        League league = leagueRepository.findByUid(match.getLeagueUid()).orElse(null);

        // 참여자 명단 (APPROVED 신청자)
        List<LeagueMatchApplication> approvedApps = leagueMatchApplicationRepository
                .findByLeagueMatchUidAndStatus(match.getUid(), LeagueMatchApplication.ApplicationStatus.APPROVED);
        List<LeagueDto.LeagueMatchParticipant> participants = approvedApps.stream()
                .map(app -> {
                    User u = userRepository.findById(app.getUserUid()).orElse(null);
                    Team t = teamRepository.findByUid(app.getTeamUid()).orElse(null);
                    return LeagueDto.LeagueMatchParticipant.builder()
                            .uid(app.getUserUid())
                            .name(u != null ? u.getActualName() : "알 수 없음")
                            .teamName(t != null ? t.getName() : "")
                            .profileImageUrl(u != null ? u.getProfileImageUrl() : null)
                            .build();
                })
                .collect(Collectors.toList());

        boolean alreadyApplied = false;
        boolean hasRatedManner = false;
        boolean isTeamOwner = false;
        String myTeamUid = null;
        if (currentUserUid != null && !currentUserUid.isEmpty()) {
            alreadyApplied = leagueMatchApplicationRepository
                    .existsByLeagueMatchUidAndUserUidAndStatus(
                            match.getUid(), currentUserUid,
                            LeagueMatchApplication.ApplicationStatus.APPROVED);
            hasRatedManner = mannerRatingRepository
                    .existsByMatchUidAndRaterUserUid(match.getUid(), currentUserUid);
            // 홈팀/원정팀의 OWNER 또는 MANAGER 여부 확인
            isTeamOwner = isTeamManagerOrOwner(homeTeam, currentUserUid)
                    || isTeamManagerOrOwner(awayTeam, currentUserUid);
            // 사용자가 소속된 팀 확인 (홈팀 또는 원정팀)
            if (teamMemberRepository.existsByTeamUidAndUserUidAndStatus(
                    match.getHomeTeamUid(), currentUserUid, TeamMember.MemberStatus.APPROVED)) {
                myTeamUid = match.getHomeTeamUid();
            } else if (teamMemberRepository.existsByTeamUidAndUserUidAndStatus(
                    match.getAwayTeamUid(), currentUserUid, TeamMember.MemberStatus.APPROVED)) {
                myTeamUid = match.getAwayTeamUid();
            }
        }

        return LeagueDto.MatchResponse.builder()
                .uid(match.getUid())
                .leagueUid(match.getLeagueUid())
                .leagueName(league != null ? league.getName() : "")
                .leagueStatus(league != null ? league.getStatus() : null)
                .homeTeamUid(match.getHomeTeamUid())
                .homeTeamName(homeTeam != null ? homeTeam.getName() : "")
                .homeTeamLogoUrl(homeTeam != null ? homeTeam.getLogoFileUid() : null)
                .awayTeamUid(match.getAwayTeamUid())
                .awayTeamName(awayTeam != null ? awayTeam.getName() : "")
                .awayTeamLogoUrl(awayTeam != null ? awayTeam.getLogoFileUid() : null)
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .durationMinutes(match.getDurationMinutes())
                .stadiumName(match.getStadiumName())
                .stadiumAddress(match.getStadiumAddress())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .status(match.getStatus())
                .round(match.getRound())
                .alreadyApplied(alreadyApplied)
                .hasRatedManner(hasRatedManner)
                .isTeamOwner(isTeamOwner)
                .myTeamUid(myTeamUid)
                .participants(participants)
                .build();
    }

    private boolean isTeamManagerOrOwner(Team team, String userUid) {
        if (team == null || userUid == null) return false;
        if (userUid.equals(team.getOwnerUid())) return true;
        return teamMemberRepository.findByTeamUidAndUserUid(team.getUid(), userUid)
                .map(m -> m.getRole() == TeamMember.MemberRole.OWNER
                        || m.getRole() == TeamMember.MemberRole.MANAGER)
                .orElse(false);
    }

    /**
     * 리그 경기 참가 신청
     * - 참가 자격: 해당 LeagueMatch의 homeTeam 또는 awayTeam의 APPROVED 멤버만
     * - 시점: 경기 시작 전까지 (matchDate + matchTime이 현재보다 미래)
     * - 참가비: 무조건 무료
     * - 중복 신청 방지 (기존 CANCELLED 레코드는 상태 복원하여 재신청 허용)
     */
    @Transactional
    public void applyToLeagueMatch(String matchUid, String userUid) {
        LeagueMatch match = leagueMatchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그 경기입니다."));

        if (match.getStatus() == LeagueMatch.MatchStatus.CANCELLED) {
            throw new IllegalArgumentException("취소된 경기입니다.");
        }
        if (isMatchStarted(match)) {
            throw new IllegalArgumentException("이미 시작된 경기는 신청할 수 없습니다.");
        }

        // 사용자의 소속 팀 — 홈팀 또는 원정팀 APPROVED 멤버 여부 확인
        String belongingTeamUid = null;
        if (teamMemberRepository.existsByTeamUidAndUserUidAndStatus(
                match.getHomeTeamUid(), userUid, TeamMember.MemberStatus.APPROVED)) {
            belongingTeamUid = match.getHomeTeamUid();
        } else if (teamMemberRepository.existsByTeamUidAndUserUidAndStatus(
                match.getAwayTeamUid(), userUid, TeamMember.MemberStatus.APPROVED)) {
            belongingTeamUid = match.getAwayTeamUid();
        }
        if (belongingTeamUid == null) {
            throw new IllegalArgumentException("해당 경기에 참여하는 팀의 승인된 멤버만 신청할 수 있습니다.");
        }

        // 기존 신청 레코드가 있으면 상태 복원 (재신청)
        LeagueMatchApplication application = leagueMatchApplicationRepository
                .findByLeagueMatchUidAndUserUid(matchUid, userUid)
                .orElse(null);
        if (application != null) {
            if (application.getStatus() == LeagueMatchApplication.ApplicationStatus.APPROVED) {
                throw new IllegalArgumentException("이미 신청한 경기입니다.");
            }
            application.setStatus(LeagueMatchApplication.ApplicationStatus.APPROVED);
            application.setTeamUid(belongingTeamUid);
            application.setProcessedDate(java.time.LocalDateTime.now());
        } else {
            application = LeagueMatchApplication.builder()
                    .leagueMatchUid(matchUid)
                    .teamUid(belongingTeamUid)
                    .userUid(userUid)
                    .status(LeagueMatchApplication.ApplicationStatus.APPROVED)
                    .build();
        }
        leagueMatchApplicationRepository.save(application);
    }

    /**
     * 리그 경기 참가 신청 취소
     * - 취소 기한: match_config.cancelDaysBeforeMatch (N일 전까지)
     */
    @Transactional
    public void cancelLeagueMatchApplication(String matchUid, String userUid) {
        LeagueMatch match = leagueMatchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그 경기입니다."));

        LeagueMatchApplication application = leagueMatchApplicationRepository
                .findByLeagueMatchUidAndUserUid(matchUid, userUid)
                .filter(a -> a.getStatus() == LeagueMatchApplication.ApplicationStatus.APPROVED)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역을 찾을 수 없습니다."));

        if (match.getMatchDate() == null) {
            throw new IllegalArgumentException("경기 일자 정보가 없습니다.");
        }
        MatchConfig config = matchConfigRepository.findByUid("default")
                .orElse(MatchConfig.builder().uid("default").cancelDaysBeforeMatch(1).build());
        LocalDate cancelDeadline = LocalDate.now().plusDays(config.getCancelDaysBeforeMatch());
        if (!match.getMatchDate().isAfter(cancelDeadline)) {
            throw new IllegalArgumentException(
                    "취소 기한이 지났습니다. 경기 " + config.getCancelDaysBeforeMatch() + "일 전까지만 취소 가능합니다.");
        }

        application.setStatus(LeagueMatchApplication.ApplicationStatus.CANCELLED);
        application.setProcessedDate(java.time.LocalDateTime.now());
        leagueMatchApplicationRepository.save(application);
    }

    /**
     * 내 리그 경기 신청 내역 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MyLeagueMatchApplicationResponse> getMyLeagueMatchApplications(String userUid) {
        List<LeagueMatchApplication> apps = leagueMatchApplicationRepository
                .findByUserUidOrderByCreatedDateDesc(userUid);
        List<LeagueDto.MyLeagueMatchApplicationResponse> result = new ArrayList<>();
        for (LeagueMatchApplication app : apps) {
            LeagueMatch match = leagueMatchRepository.findByUid(app.getLeagueMatchUid()).orElse(null);
            if (match == null) continue;
            Team homeTeam = teamRepository.findByUid(match.getHomeTeamUid()).orElse(null);
            Team awayTeam = teamRepository.findByUid(match.getAwayTeamUid()).orElse(null);
            League league = leagueRepository.findByUid(match.getLeagueUid()).orElse(null);
            result.add(LeagueDto.MyLeagueMatchApplicationResponse.builder()
                    .applicationUid(app.getUid())
                    .leagueMatchUid(match.getUid())
                    .leagueName(league != null ? league.getName() : "")
                    .homeTeamName(homeTeam != null ? homeTeam.getName() : "")
                    .awayTeamName(awayTeam != null ? awayTeam.getName() : "")
                    .stadiumName(match.getStadiumName())
                    .matchDate(match.getMatchDate())
                    .matchTime(match.getMatchTime() != null ? match.getMatchTime().toString() : null)
                    .matchStatus(match.getStatus() != null ? match.getStatus().name() : "")
                    .applicationStatus(app.getStatus() != null ? app.getStatus().name() : "")
                    .appliedAt(app.getCreatedDate())
                    .build());
        }
        return result;
    }

    private boolean isMatchStarted(LeagueMatch match) {
        java.time.LocalDate date = match.getMatchDate();
        if (date == null) return false;
        java.time.LocalDateTime startAt = match.getMatchTime() != null
                ? java.time.LocalDateTime.of(date, match.getMatchTime())
                : date.atTime(23, 59, 59);
        return java.time.LocalDateTime.now().isAfter(startAt);
    }
}
