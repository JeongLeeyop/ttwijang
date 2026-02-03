package com.ttwijang.cms.api.product.repository.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.ttwijang.cms.api.product.dto.ProductOrderDto.order;
import com.ttwijang.cms.entity.ProductOrderType;
import com.ttwijang.cms.entity.QProductOrder;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdmProductOrderSearch {
    private String startDate;
    private String endDate;
    private String searchType;
    private String searchValue;
    private String orderType;
    private Integer stationId;
    private Integer shopId;
    private Integer orderStatus;
    private Integer groupOrderStatus;
    private Integer receiveStatus;
    private Integer pickupStatus;
    private Integer settleStatus;

    private String excelType;

    public Predicate search() {
        QProductOrder productOrder = QProductOrder.productOrder;

        BooleanBuilder builder = new BooleanBuilder();

        /*
        if (startDate == null) startDate = DateUtil.startWeekDay(1);
        if (endDate == null) endDate = DateUtil.endWeekDay(1);
        
        builder.and(productOrder.orderDate.loe(endDate));
        builder.and(productOrder.orderDate.goe(startDate));
        */
        builder.and(productOrder.group.paymentStatus.eq(1));

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            StringTemplate formattedStartDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                productOrder.orderDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            builder.and(formattedStartDate.between(startDate, endDate));
        }
        if (shopId != null) builder.and(productOrder.group.shopId.eq(shopId));
        
        if (orderStatus != null) builder.and(productOrder.orderStatus.eq(orderStatus));
        else builder.and(productOrder.orderStatus.ne(-1));
        
        if (receiveStatus != null) builder.and(productOrder.receiveStatus.eq(receiveStatus));
        if (pickupStatus != null) builder.and(productOrder.pickupStatus.eq(pickupStatus));
        if (settleStatus != null) builder.and(productOrder.settleStatus.eq(settleStatus));

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            if (searchType.equals("actualName")) builder.and(productOrder.group.user.actualName.contains(searchValue));
            if (searchType.equals("shopName")) builder.and(productOrder.group.shop.name.contains(searchValue));
            if (searchType.equals("productName")) builder.and(productOrder.productName.contains(searchValue));
        }
        
        if (StringUtils.hasText(orderType)) {
            builder.and(productOrder.group.orderType.eq(ProductOrderType.valueOf(orderType)));
        }

        // builder.and(productOrder.orderStatus.gt(-1));

        return builder;
    }
}
