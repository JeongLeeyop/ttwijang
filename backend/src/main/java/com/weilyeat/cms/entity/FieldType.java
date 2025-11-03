package com.weilyeat.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "field_type")
@Getter @Setter
public class FieldType {
	
	// 필드타입 코드
	@Id
    private String typeCode;
	
	// 필드타입 명
	private String typeName;
	
	// 순서
	private int viewOrder;
}
