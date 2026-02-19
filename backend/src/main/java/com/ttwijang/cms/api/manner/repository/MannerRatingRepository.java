package com.ttwijang.cms.api.manner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.MannerRating;

@Repository
public interface MannerRatingRepository extends JpaRepository<MannerRating, String> {

    /**
     * 특정 팀에 대한 모든 매너 평가 조회
     */
    List<MannerRating> findByRatedTeamUidAndTargetType(String ratedTeamUid, MannerRating.RatingTargetType targetType);

    /**
     * 특정 매치에서 특정 팀에 대해 이미 평가했는지 확인
     */
    boolean existsByMatchUidAndRaterUserUidAndRatedTeamUid(String matchUid, String raterUserUid, String ratedTeamUid);

    /**
     * 특정 매치에서 해당 사용자가 이미 매너 평가를 했는지 확인
     */
    boolean existsByMatchUidAndRaterUserUid(String matchUid, String raterUserUid);

    /**
     * 팀의 평균 매너 점수 계산
     */
    @Query("SELECT AVG(mr.score) FROM MannerRating mr WHERE mr.ratedTeamUid = :teamUid AND mr.targetType = 'TEAM'")
    Double getAverageScoreByTeamUid(@Param("teamUid") String teamUid);

    /**
     * 특정 팀에 대한 매너 평가 총 건수
     */
    long countByRatedTeamUidAndTargetType(String ratedTeamUid, MannerRating.RatingTargetType targetType);
}
