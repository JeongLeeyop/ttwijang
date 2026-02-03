package com.ttwijang.cms.api.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.mission.dto.MissionCategoryDto;
import com.ttwijang.cms.api.mission.service.AdmMissionCategoryService;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/api/mission/category")
@AllArgsConstructor
public class AdmMissionCategoryController {
	private final AdmMissionCategoryService admMissionCategoryService;
	
	@GetMapping("/list")
	public ResponseEntity<List<MissionCategoryDto.detail>> listAll() {
		return ResponseEntity.ok(admMissionCategoryService.listAll());
	}
}
