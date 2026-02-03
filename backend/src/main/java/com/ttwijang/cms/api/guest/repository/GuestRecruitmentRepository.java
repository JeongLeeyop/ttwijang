package com.ttwijang.cms.api.guest.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.GuestRecruitment;

@Repository
public interface GuestRecruitmentRepository extends JpaRepository<GuestRecruitment, String>, QuerydslPredicateExecutor<GuestRecruitment> {

    Optional<GuestRecruitment> findByUid(String uid);

    Page<GuestRecruitment> findByStatus(GuestRecruitment.RecruitmentStatus status, Pageable pageable);

    Page<GuestRecruitment> findByMatchDate(LocalDate matchDate, Pageable pageable);

    Page<GuestRecruitment> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<GuestRecruitment> findByTeamUid(String teamUid, Pageable pageable);

    @Query("SELECT gr FROM GuestRecruitment gr WHERE gr.status = :status AND gr.regionSido = :sido")
    Page<GuestRecruitment> findByStatusAndRegion(@Param("status") GuestRecruitment.RecruitmentStatus status, 
                                                   @Param("sido") String sido, 
                                                   Pageable pageable);

    // 7일 이내 모집만 가능한 조건 확인
    @Query("SELECT gr FROM GuestRecruitment gr WHERE gr.teamUid = :teamUid AND gr.matchDate BETWEEN :startDate AND :endDate")
    List<GuestRecruitment> findByTeamUidAndDateRange(@Param("teamUid") String teamUid, 
                                                       @Param("startDate") LocalDate startDate, 
                                                       @Param("endDate") LocalDate endDate);
}
