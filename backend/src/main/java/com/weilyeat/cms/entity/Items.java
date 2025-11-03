package com.weilyeat.cms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class Items{							
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    // @Comment("상품명")	
    private String name;
	@Column
    // @Comment("재료")
    private String material;
	@Column
    // @Comment("설명")
    private String description;
	@Column
    // @Comment("중량")
    private String weight;
	@Column
    // @Comment("출처")
    private String supplier;
	@Column
    // @Comment("칼로리")
    private String calorie;
	@Column
    // @Comment("가격")
    private String price;
	@Column
    // @Comment("판매가능여부")
    private String status;
	@Column
    // @Comment("식단형태")
    private String dietType;
	@Column
    // @Comment("식단목적")
    private String dietPurpose;
	@Column
    // @Comment("유의사항")
    private String dietPrecaution;

	@Column
    private String itemFileUid;
    
    @Column
    private Long foodId;
    
	@Column
    private Long shopId;
    
    // 첨부파일 목록
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @Comment("상품사진")
    @JoinColumn(name = "itemFileUid", insertable = false, updatable = false)
	private AttachedFile attachedFile;

    @OneToOne 
    @JoinColumn(name="foodId", insertable= false, updatable=false)
    private Food food;

}