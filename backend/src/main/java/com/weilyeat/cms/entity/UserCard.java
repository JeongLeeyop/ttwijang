package com.ttwijang.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

	
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //카드번호
    @Column
    // @Comment("카드번호")	
    private String cardNum;
    //유효기간
    @Column
	// @Comment("유효기간")
    private String dueDate;
    //cvc
    @Column
	// @Comment("cvc")
    private String cvc;
    //등록일
    @Column
	// @Comment("등록일")
    private String createdDate;

    // FK	id	accountId	account		N		
}