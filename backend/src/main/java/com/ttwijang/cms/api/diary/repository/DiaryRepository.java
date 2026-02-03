package com.ttwijang.cms.api.diary.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ttwijang.cms.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByUserUidAndDiaryDate(String uid, LocalDate diaryDate);

    @Query(value = "SELECT * FROM diary d WHERE d.user_uid = ?1 ORDER BY diary_date DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> findByUserUidOrderByDiaryDateDesc(String uid);

    @Query(value = "SELECT * FROM diary d WHERE d.user_uid = ?1 AND d.purpose_weight is not null AND d.purpose_fat_rate is not null AND d.purpose_muscle_weight \n" +
        "ORDER BY diary_date DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> getLastPurpose(String uid);

    @Query(value = "SELECT * FROM diary d WHERE d.user_uid = ?1 AND d.weight is not null AND d.fat_rate is not null AND d.muscle_weight \n" +
    "ORDER BY diary_date DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> getLastPhysicalCondition(String uid);
    
}
