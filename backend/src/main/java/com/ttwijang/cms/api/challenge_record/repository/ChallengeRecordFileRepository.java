package com.ttwijang.cms.api.challenge_record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.entity.ChallengeRecordFile;

public interface ChallengeRecordFileRepository extends JpaRepository<ChallengeRecordFile, String> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM challenge_record_file WHERE challenge_record_uid IN (SELECT uid FROM challenge_record WHERE user_uid = ?1)", nativeQuery = true)
	void deleteByUserUid(String userUid);

}
