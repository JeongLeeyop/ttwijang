package com.weilyeat.cms.api.challenge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.challenge.dto.ChallengeCategoryDto;
import com.weilyeat.cms.api.challenge.dto.mapper.ChallengeCategoryMapper;
import com.weilyeat.cms.api.challenge.repository.AdmChallengeCategoryRepository;

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
