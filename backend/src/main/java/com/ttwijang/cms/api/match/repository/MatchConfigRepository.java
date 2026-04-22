package com.ttwijang.cms.api.match.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.MatchConfig;

@Repository
public interface MatchConfigRepository extends JpaRepository<MatchConfig, String> {
    Optional<MatchConfig> findByUid(String uid);
}
