package com.weilyeat.cms.api.user.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.weilyeat.cms.common.search.SearchDefault;
import com.weilyeat.cms.entity.QUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserSearch extends SearchDefault {
    private String siteUid;
    private String roleCode;
    private String institutionUid;
    private Boolean enabled;

    private String userUid;

    private boolean isManager;

    public Predicate search() {
        QUser user = QUser.user;

        BooleanBuilder builder = super.initSearch(user);

        /*
        if (StringUtils.hasText(siteUid)) builder.and(user.siteUid.eq(siteUid));
        else builder.and(user.siteUid.eq(""));
        */

        if (StringUtils.hasText(roleCode)) builder.and(user.roles.any().role.roleCode.eq(roleCode));
        if (enabled != null) builder.and(user.enabled.eq(enabled));

        if (isManager) builder.and(user.roles.any().roleCode.eq("ROLE_SHOP_ADMIN"));
        else builder.and(user.roles.any().roleCode.notEqualsIgnoreCase("ROLE_SHOP_ADMIN"));

        builder.and(user.joinStatus.eq(true));
        builder.and(user.withdrawStatus.eq(false));
        

        return builder;
    }
}
