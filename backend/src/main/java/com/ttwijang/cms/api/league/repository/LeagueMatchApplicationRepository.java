package com.ttwijang.cms.api.league.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.LeagueMatchApplication;

@Repository
public interface LeagueMatchApplicationRepository extends JpaRepository<LeagueMatchApplication, String> {

    Optional<LeagueMatchApplication> findByUid(String uid);

    Optional<LeagueMatchApplication> findByLeagueMatchUidAndUserUid(String leagueMatchUid, String userUid);

    boolean existsByLeagueMatchUidAndUserUidAndStatus(
            String leagueMatchUid, String userUid, LeagueMatchApplication.ApplicationStatus status);

    List<LeagueMatchApplication> findByLeagueMatchUidAndStatus(
            String leagueMatchUid, LeagueMatchApplication.ApplicationStatus status);

    List<LeagueMatchApplication> findByUserUidOrderByCreatedDateDesc(String userUid);

    void deleteByLeagueMatchUidIn(List<String> leagueMatchUids);
}
