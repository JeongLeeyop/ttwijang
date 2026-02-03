package com.ttwijang.cms.api.product.dto.search;

import org.springframework.util.StringUtils;

import com.ttwijang.cms.entity.ProductType;
import com.ttwijang.cms.entity.QProductPackage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductPackageSearch {
    private String searchType;
    private String searchValue;
    private String productType;
    
    public Predicate search() {
        QProductPackage qProductPackage = QProductPackage.productPackage;
        BooleanBuilder builder = new BooleanBuilder();
        
        // 삭제되지 않은 상품만 조회
        builder.and(qProductPackage.deleteStatus.eq(false));
        
        // 상품 타입 필터
        if (StringUtils.hasText(productType)) {
            builder.and(qProductPackage.productType.eq(ProductType.valueOf(productType)));
        }
        
        // 검색 조건
        if (StringUtils.hasText(searchValue)) {
            if ("name".equals(searchType)) {
                builder.and(qProductPackage.name.contains(searchValue));
            }
        }
        
        return builder;
    }
}
