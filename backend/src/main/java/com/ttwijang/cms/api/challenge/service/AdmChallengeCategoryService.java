package com.ttwijang.cms.api.challenge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.challenge.dto.ChallengeCategoryDto;
import com.ttwijang.cms.api.challenge.dto.mapper.ChallengeCategoryMapper;
import com.ttwijang.cms.api.challenge.repository.AdmChallengeCategoryRepository;

import lombok.AllArgsConstructor;

public interface AdmChallengeCategoryService {
	List<ChallengeCategoryDto.detail> listAll();
}

@Service
@AllArgsConstructor
class AdmChallengeCategoryServiceImpl implements AdmChallengeCategoryService {

	private final AdmChallengeCategoryRepository admChallengeCategoryRepository;
	
	@Override
	public List<ChallengeCategoryDto.detail> listAll() {
		return admChallengeCategoryRepository.findAll()
			.stream().map(data -> ChallengeCategoryMapper.INSTANCE.entityToDetailDto(data))
			.collect(Collectors.toList());
	}
}
