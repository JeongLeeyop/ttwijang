package com.ttwijang.cms.api.fieldType.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ttwijang.cms.entity.FieldType;

public interface FieldTypeRepository extends JpaRepository<FieldType, String>{

	@Query("SELECT ft FROM FieldType ft ORDER BY ft.viewOrder")
	List<FieldType> listAll();

}
