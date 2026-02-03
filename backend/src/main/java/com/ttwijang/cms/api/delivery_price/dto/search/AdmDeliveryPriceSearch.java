// package com.ttwijang.cms.api.delivery_price.dto.search;

// import org.springframework.util.StringUtils;

// import com.querydsl.core.BooleanBuilder;
// import com.querydsl.core.types.ConstantImpl;
// import com.querydsl.core.types.Predicate;
// import com.querydsl.core.types.dsl.Expressions;
// import com.querydsl.core.types.dsl.StringTemplate;
// import com.ttwijang.cms.entity.QSettleApply;

// import lombok.Data;

// @Data
// public class AdmDeliveryPriceSearch {
//     private String searchType;
//     private String searchValue;
//     private String startDate;
//     private String endDate;

//     public Predicate search() {
//         BooleanBuilder builder = new BooleanBuilder();
//         QSettleApply settleApply = QSettleApply.settleApply;

//         builder.and(settleApply.cancelStatus.eq(false));
//         if (StringUtils.hasText(searchValue)) {
//             if (searchType.equals("shopName")) builder.and(settleApply.shopName.contains(searchValue));
//         }

//         if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
//             StringTemplate formattedApplyDate = Expressions.stringTemplate(
//                 "DATE_FORMAT({0}, {1})",
//                 settleApply.applyDate,
//                 ConstantImpl.create("%Y-%m-%d")
//             );
//             builder.and(formattedApplyDate.between(startDate, endDate));
//         }

//         return builder;
//     }
// }
