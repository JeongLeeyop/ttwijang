package com.ttwijang.cms.api.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.mission.dto.MissionCategoryDto;
import com.ttwijang.cms.api.mission.service.AdmMissionCategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission/category")
@AllArgsConstructor
public class MissionCategoryController {
	private final AdmMissionCategoryService admMissionCategoryService;
	
	@GetMapping("/list")
	public ResponseEntity<List<MissionCategoryDto.detail>> listAll() {
		return ResponseEntity.ok(admMissionCategoryService.listAll());
	}
}
