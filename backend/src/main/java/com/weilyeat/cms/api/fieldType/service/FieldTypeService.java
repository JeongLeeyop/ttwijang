package com.ttwijang.cms.api.fieldType.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.fieldType.repository.FieldTypeRepository;
import com.ttwijang.cms.entity.FieldType;

import lombok.AllArgsConstructor;

public interface FieldTypeService {
	public List<FieldType> listAll();
}

@Service
@AllArgsConstructor
class FieldTypeServiceImpl implements FieldTypeService {

	private final FieldTypeRepository fieldTypeRepository;

	@Override
	public List<FieldType> listAll() {
		return fieldTypeRepository.listAll();
	}

}
