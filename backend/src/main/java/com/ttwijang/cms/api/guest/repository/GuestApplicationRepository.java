package com.ttwijang.cms.api.guest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.GuestApplication;

@Repository
public interface GuestApplicationRepository extends JpaRepository<GuestApplication, String>, QuerydslPredicateExecutor<GuestApplication> {

    Optional<GuestApplication> findByUid(String uid);

    Optional<GuestApplication> findByRecruitmentUidAndUserUid(String recruitmentUid, String userUid);

    List<GuestApplication> findByRecruitmentUid(String recruitmentUid);

    List<GuestApplication> findByUserUid(String userUid);

    List<GuestApplication> findByRecruitmentUidAndStatus(String recruitmentUid, GuestApplication.ApplicationStatus status);

    boolean existsByRecruitmentUidAndUserUid(String recruitmentUid, String userUid);

    long countByRecruitmentUidAndStatus(String recruitmentUid, GuestApplication.ApplicationStatus status);
}
