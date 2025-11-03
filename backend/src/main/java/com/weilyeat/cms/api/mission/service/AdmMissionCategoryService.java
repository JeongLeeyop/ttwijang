package com.weilyeat.cms.api.mission.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.dto.MissionCategoryDto;
import com.weilyeat.cms.api.mission.dto.mapper.MissionCategoryMapper;
import com.weilyeat.cms.api.mission.repository.AdmMissionCategoryRepository;

import lombok.AllArgsConstructor;

public interface AdmMissionCategoryService {
	List<MissionCategoryDto.detail> listAll();
}

@Service
@AllArgsConstructor
class AdmMissionCategoryServiceImpl implements AdmMissionCategoryService {

	private final AdmMissionCategoryRepository admMissionCategoryRepository;
	
	@Override
	public List<MissionCategoryDto.detail> listAll() {
		return admMissionCategoryRepository.findAll()
			.stream().map(data -> MissionCategoryMapper.INSTANCE.entityToDetailDto(data))
			.collect(Collectors.toList());
	}
}
