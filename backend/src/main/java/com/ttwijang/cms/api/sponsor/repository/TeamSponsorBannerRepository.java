package com.ttwijang.cms.api.sponsor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.TeamSponsorBanner;

@Repository
public interface TeamSponsorBannerRepository extends JpaRepository<TeamSponsorBanner, String> {

    Optional<TeamSponsorBanner> findByUid(String uid);

    List<TeamSponsorBanner> findByTeamUid(String teamUid);

    Page<TeamSponsorBanner> findAll(Pageable pageable);

    Optional<TeamSponsorBanner> findFirstByTeamUid(String teamUid);
}
