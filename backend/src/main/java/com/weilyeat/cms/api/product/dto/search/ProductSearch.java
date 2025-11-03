package com.weilyeat.cms.api.product.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.ProductType;
import com.weilyeat.cms.entity.QProduct;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductSearch {
    private Integer shopIdx;
    private String searchType;
    private String searchValue;

    private String dietType;
    private String dietPurpose;
    private String dietPrecaution;

    private String productType;
    private boolean extraProductStatus;
    private Integer weekDeliveryCnt;

    public Predicate Search() {
        BooleanBuilder builder = new BooleanBuilder();
        QProduct product = QProduct.product;

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            if (searchType.equals("name")) builder.and(product.name.contains(searchValue));
        }
        if (StringUtils.hasText(dietType)) builder.and(product.dietType.eq(dietType));
        if (StringUtils.hasText(dietPurpose)) builder.and(product.dietPurpose.eq(dietPurpose));
        if (StringUtils.hasText(dietPrecaution)) builder.and(product.dietPrecaution.eq(dietPrecaution));
        
        if (extraProductStatus) builder.and(product.extraProductStatus.isTrue());
        else builder.and(product.extraProductStatus.isFalse());
        if (weekDeliveryCnt != null && weekDeliveryCnt > 0) builder.and(product.weekDeliveryCnt.eq(weekDeliveryCnt));
        if (StringUtils.hasText(productType)) builder.and(product.productType.eq(ProductType.valueOf(productType)));
        
        builder.and(product.deleteStatus.eq(false));

        return builder;
    }
}
