package com.ttwijang.cms.api.league.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.league.dto.LeagueDto;
import com.ttwijang.cms.api.league.repository.LeagueMatchRepository;
import com.ttwijang.cms.api.league.repository.LeagueRepository;
import com.ttwijang.cms.api.league.repository.LeagueTeamRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.League;
import com.ttwijang.cms.entity.LeagueMatch;
import com.ttwijang.cms.entity.LeagueTeam;
import com.ttwijang.cms.entity.Team;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueTeamRepository leagueTeamRepository;
    private final LeagueMatchRepository leagueMatchRepository;
    private final TeamRepository teamRepository;

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
                        .teamName(team.getName())
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
     * 다가오는 경기 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.MatchResponse> getUpcomingMatches(String leagueUid, int limit) {
        Page<LeagueMatch> matches = leagueMatchRepository.findByLeagueUidOrderByMatchDateAscMatchTimeAsc(
                leagueUid, PageRequest.of(0, limit));
        
        return matches.getContent().stream()
                .filter(m -> m.getStatus() == LeagueMatch.MatchStatus.SCHEDULED)
                .map(this::toMatchResponse)
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
     */
    @Transactional
    public LeagueDto.MatchResponse updateMatchResult(LeagueDto.MatchResultRequest request) {
        LeagueMatch match = leagueMatchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 경기입니다."));

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
        if (request.getBannerFileUid() != null) league.setBannerFileUid(request.getBannerFileUid());

        league = leagueRepository.save(league);
        return getLeagueDetail(league.getUid());
    }

    /**
     * BR-11: 리그 삭제 (최고관리자 전용)
     */
    @Transactional
    public void deleteLeague(String uid) {
        League league = leagueRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리그입니다."));
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

        // 최대 팀 수 확인
        if (league.getCurrentTeams() != null && league.getMaxTeams() != null
                && league.getCurrentTeams() >= league.getMaxTeams()) {
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
        
        return leagueTeams.stream()
                .map(lt -> {
                    Team team = teamRepository.findByUid(lt.getTeamUid()).orElse(null);
                    if (team != null) {
                        League league = leagueRepository.findByUid(leagueUid).orElse(null);
                        return LeagueDto.LeagueTeamResponse.builder()
                                .teamUid(team.getUid())
                                .teamName(team.getName())
                                .teamLogoUrl(team.getLogoFileUid())
                                .leagueName(league != null ? league.getName() : "")
                                .ranking(lt.getRanking())
                                .points(lt.getPoints())
                                .build();
                    }
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    private LeagueDto.ListResponse toListResponse(League league) {
        return LeagueDto.ListResponse.builder()
                .uid(league.getUid())
                .name(league.getName())
                .season(league.getSeason())
                .region(league.getRegionSido() + " " + (league.getRegionSigungu() != null ? league.getRegionSigungu() : ""))
                .currentTeams(league.getCurrentTeams())
                .maxTeams(league.getMaxTeams())
                .status(league.getStatus())
                .build();
    }

    private LeagueDto.MatchResponse toMatchResponse(LeagueMatch match) {
        Team homeTeam = teamRepository.findByUid(match.getHomeTeamUid()).orElse(null);
        Team awayTeam = teamRepository.findByUid(match.getAwayTeamUid()).orElse(null);

        return LeagueDto.MatchResponse.builder()
                .uid(match.getUid())
                .leagueUid(match.getLeagueUid())
                .homeTeamUid(match.getHomeTeamUid())
                .homeTeamName(homeTeam != null ? homeTeam.getName() : "")
                .awayTeamUid(match.getAwayTeamUid())
                .awayTeamName(awayTeam != null ? awayTeam.getName() : "")
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .stadiumName(match.getStadiumName())
                .stadiumAddress(match.getStadiumAddress())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .status(match.getStatus())
                .round(match.getRound())
                .build();
    }
}
