package com.ttwijang.cms.api.manner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.manner.dto.MannerRatingDto;
import com.ttwijang.cms.api.manner.repository.MannerRatingRepository;
import com.ttwijang.cms.api.match.repository.FutsalMatchRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.MannerRating;
import com.ttwijang.cms.entity.Team;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MannerRatingService {

    private final MannerRatingRepository mannerRatingRepository;
    private final FutsalMatchRepository matchRepository;
    private final TeamRepository teamRepository;

    /**
     * 팀 매너 점수 평가
     */
    @Transactional
    public MannerRatingDto.RateResponse rateTeam(MannerRatingDto.RateTeamRequest request, String raterUserUid) {
        // 매치 존재 확인
        FutsalMatch match = matchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        // 매치가 완료 상태인지 확인
        if (match.getStatus() != FutsalMatch.FutsalMatchStatus.COMPLETED) {
            throw new IllegalArgumentException("완료된 매치에서만 매너 점수를 평가할 수 있습니다.");
        }

        // 평가 대상 팀 존재 확인
        Team ratedTeam = teamRepository.findByUid(request.getRatedTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 이미 평가했는지 확인
        if (mannerRatingRepository.existsByMatchUidAndRaterUserUidAndRatedTeamUid(
                request.getMatchUid(), raterUserUid, request.getRatedTeamUid())) {
            throw new IllegalArgumentException("이미 해당 매치에서 매너 점수를 평가했습니다.");
        }

        // 매너 평가 저장
        MannerRating rating = MannerRating.builder()
                .raterUserUid(raterUserUid)
                .ratedTeamUid(request.getRatedTeamUid())
                .matchUid(request.getMatchUid())
                .targetType(MannerRating.RatingTargetType.TEAM)
                .score(request.getScore())
                .comment(request.getComment())
                .build();
        rating = mannerRatingRepository.save(rating);

        // 팀 평균 매너 점수 업데이트
        Double averageScore = mannerRatingRepository.getAverageScoreByTeamUid(request.getRatedTeamUid());
        if (averageScore != null) {
            ratedTeam.setMannerScore(Math.round(averageScore * 100.0) / 100.0);
            teamRepository.save(ratedTeam);
        }

        return MannerRatingDto.RateResponse.builder()
                .uid(rating.getUid())
                .ratedTeamUid(request.getRatedTeamUid())
                .averageScore(averageScore)
                .message("매너 점수가 등록되었습니다.")
                .build();
    }

    /**
     * 팀 매너 점수 조회
     */
    @Transactional(readOnly = true)
    public MannerRatingDto.TeamScoreResponse getTeamScore(String teamUid) {
        Double averageScore = mannerRatingRepository.getAverageScoreByTeamUid(teamUid);
        long totalRatings = mannerRatingRepository.countByRatedTeamUidAndTargetType(
                teamUid, MannerRating.RatingTargetType.TEAM);
        return MannerRatingDto.TeamScoreResponse.builder()
                .teamUid(teamUid)
                .averageScore(averageScore != null ? Math.round(averageScore * 100.0) / 100.0 : 0.0)
                .totalRatings(totalRatings)
                .build();
    }
}
