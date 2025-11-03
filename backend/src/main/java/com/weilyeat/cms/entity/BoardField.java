package com.ttwijang.cms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="board_field")
@Getter @Setter @ToString
public class BoardField {
	
	// 필드 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
	
	// 게시판 고유값
	@Column(insertable = false, updatable = false)
	private String boardUid;
	
	// 필드 명
	private String fieldName;
	
    // 필드 타입 고유값
	private String fieldTypeCode;
	
	// 입력 길이(파일 개수)
	private int inputLimit;
	
	// 파일 용량 제한
	private Integer fileSizeLimit;
	
	// 필수입력 여부
	private boolean requiredState;
	
	// 검색 가능 여부
	private boolean searchState;
	
	// 순서
	@Column(insertable = false, updatable = false)
	private int viewOrder;
	
	@OneToMany(mappedBy = "field", cascade = CascadeType.REMOVE)
	private List<PostData> dataList;

	// 필트타입
	@OneToOne(optional = false)
	@JoinColumn(name = "fieldTypeCode", insertable = false, updatable = false)
	private FieldType fieldType;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "boardUid", insertable = false, updatable = false)
	private Board board;
	
}
