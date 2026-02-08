package com.ttwijang.cms.api.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.TeamSponsorship;

@Repository
public interface TeamSponsorshipRepository extends JpaRepository<TeamSponsorship, String>, QuerydslPredicateExecutor<TeamSponsorship> {

    List<TeamSponsorship> findByTeamUid(String teamUid);

    List<TeamSponsorship> findBySponsorUid(String sponsorUid);

    List<TeamSponsorship> findByTeamUidAndType(String teamUid, TeamSponsorship.SponsorshipType type);

    List<TeamSponsorship> findByTeamUidAndStatus(String teamUid, TeamSponsorship.SponsorshipStatus status);
}
