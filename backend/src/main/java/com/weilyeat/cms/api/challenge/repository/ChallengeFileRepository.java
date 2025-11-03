package com.weilyeat.cms.api.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.ChallengeRecordFile;

public interface ChallengeFileRepository extends JpaRepository<ChallengeRecordFile, String> {
}
