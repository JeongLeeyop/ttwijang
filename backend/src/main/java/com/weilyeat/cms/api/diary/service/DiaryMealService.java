package com.ttwijang.cms.api.diary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.diary.dto.DiaryMealDto;
import com.ttwijang.cms.api.diary.dto.DiaryMealSearch;
import com.ttwijang.cms.api.diary.dto.mapper.DiaryMealMapper;
import com.ttwijang.cms.api.diary.repository.DiaryMealRepository;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface DiaryMealService {
    Page<DiaryMealDto.list> list(SinghaUser authUser, Pageable pageable);
}

@Service
@AllArgsConstructor
class DiaryMealServiceImpl implements DiaryMealService {
    private final DiaryMealRepository diaryMealRepository;

    @Override
    public Page<DiaryMealDto.list> list(SinghaUser authUser, Pageable pageable) {
        DiaryMealSearch search = new DiaryMealSearch();
        search.setUserUid(authUser.getUser().getUid());
        return diaryMealRepository.findAll(search.search(), pageable).map(e -> DiaryMealMapper.INSTANCE.entityToListDto(e));
    }
}
