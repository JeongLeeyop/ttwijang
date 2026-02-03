package com.ttwijang.cms.api.fieldType.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.fieldType.service.FieldTypeService;
import com.ttwijang.cms.entity.FieldType;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/field-type")
@AllArgsConstructor
public class FieldTypeController {
	
	private final FieldTypeService fieldTypeService;
	
	@GetMapping("/all")
	public ResponseEntity<List<FieldType>> listAll() {
		return ResponseEntity.ok(fieldTypeService.listAll());
	}
}
