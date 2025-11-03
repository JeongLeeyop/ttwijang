package com.weilyeat.cms.api.fieldType.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.fieldType.repository.FieldTypeRepository;
import com.weilyeat.cms.entity.FieldType;

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
