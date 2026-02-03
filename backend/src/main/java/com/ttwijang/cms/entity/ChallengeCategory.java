package com.ttwijang.cms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "challenge_category")
@Getter @Setter @ToString
public class ChallengeCategory {
	
	// 카테고리 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

	// 카테고리 이름
	private String name;
	
	// 카테고리 설명
	private String descript;

    // 순서
    private int viewOrder;
    
    // 생성일 
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;
}
