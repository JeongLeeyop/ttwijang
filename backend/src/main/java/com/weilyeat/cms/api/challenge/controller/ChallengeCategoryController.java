package com.weilyeat.cms.api.challenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.challenge.dto.ChallengeCategoryDto;
import com.weilyeat.cms.api.challenge.service.AdmChallengeCategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/challenge/category")
@AllArgsConstructor
public class ChallengeCategoryController {
	private final AdmChallengeCategoryService admChallengeCategoryService;
	
	@GetMapping("/list")
	public ResponseEntity<List<ChallengeCategoryDto.detail>> listAll() {
		return ResponseEntity.ok(admChallengeCategoryService.listAll());
	}
}
