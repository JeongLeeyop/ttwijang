package com.weilyeat.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ProductPackageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    
    private Long packageIdx; // 패키지 ID
    private Long productIdx; // 상품 ID
    
    @ManyToOne
    @JoinColumn(name = "productIdx", insertable = false, updatable = false)
    private Product product;
}
