package com.weilyeat.cms.api.diary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.diary.dto.DiaryDto;
import com.weilyeat.cms.api.diary.dto.DiarySearch;
import com.weilyeat.cms.api.diary.dto.DiaryStatisticsSearch;
import com.weilyeat.cms.api.diary.service.DiaryService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/diary")
@AllArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<DiaryDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser, DiarySearch search) {
        return ResponseEntity.ok(diaryService.detail(authUser, search));
    }

    @GetMapping("physical/condition")
    public ResponseEntity<DiaryDto.physicalConditionDetail> physicalConditionDetail(@AuthenticationPrincipal SinghaUser authUser, DiarySearch search) {
        return ResponseEntity.ok(diaryService.physicalConditionDetail(authUser, search));
    }

    @GetMapping("meal/statistics")
    public ResponseEntity<DiaryDto.mealStatistics> mealStatistics(@AuthenticationPrincipal SinghaUser authUser, DiaryStatisticsSearch search) {
        return ResponseEntity.ok(diaryService.mealStatistics(authUser, search));
    }

    @PostMapping("physical/condition")
    public ResponseEntity savePhysicalCondition(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.physicalConditionSave saveDto) {
        diaryService.savePhysicalCondition(authUser, saveDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("physical/purpose")
    public ResponseEntity savePurposePhysical(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.purposePhysicalSave saveDto) {
        diaryService.savePurposePhysical(authUser, saveDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity addDiaryMeal(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.mealItemAdd addDto) {
        diaryService.addDiaryMeal(authUser, addDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("food")
    public ResponseEntity addDiaryMealByFood(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.mealItemAddByFood addDto) {
        diaryService.addDiaryMealByFood(authUser, addDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("history")
    public ResponseEntity addDiaryMealByHistory(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.mealItemAddByHistory addDto) {
        diaryService.addDiaryMealByHistory(authUser, addDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("order")
    public ResponseEntity addDiaryMealByOrder(@AuthenticationPrincipal SinghaUser authUser, @RequestBody DiaryDto.mealItemAddByOrder addDto) {
        diaryService.addDiaryMealByOrder(authUser, addDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity deleteDiaryMeal(@PathVariable("idx") Long idx, @AuthenticationPrincipal SinghaUser authUser) {
        diaryService.deleteDiaryMeal(idx, authUser);
        return ResponseEntity.ok().build();
    }
}
