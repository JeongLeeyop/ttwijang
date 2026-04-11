package com.ttwijang.cms.api.sponsor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.SponsorSetting;

@Repository
public interface SponsorSettingRepository extends JpaRepository<SponsorSetting, String> {

    Optional<SponsorSetting> findByUid(String uid);
}
