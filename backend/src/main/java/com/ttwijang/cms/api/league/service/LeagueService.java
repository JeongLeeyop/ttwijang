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
     */
    @Transactional(readOnly = true)
    public Page<LeagueDto.ListResponse> getLeagueList(String region, League.LeagueStatus status, Pageable pageable) {
        Page<League> leagues;
        if (status != null) {
            if (region != null && !region.isEmpty()) {
                leagues = leagueRepository.findByRegionSidoAndStatus(region, status, pageable);
            } else {
                leagues = leagueRepository.findByStatus(status, pageable);
            }
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
                .grade(league.getGrade())
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
        LocalDate startDate = LocalDate.of(year, month, 1);
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
     * 등급별 리그 조회
     */
    @Transactional(readOnly = true)
    public List<LeagueDto.ListResponse> getLeaguesByGrade(String grade) {
        return leagueRepository.findByGrade(grade).stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    private LeagueDto.ListResponse toListResponse(League league) {
        return LeagueDto.ListResponse.builder()
                .uid(league.getUid())
                .name(league.getName())
                .grade(league.getGrade())
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
