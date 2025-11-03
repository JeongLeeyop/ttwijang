package com.weilyeat.cms.api.user.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QUserRole;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserRoleSearch {
    
    private String userUid;
    private String roleCode;

    public Predicate search() {

        QUserRole userRole = QUserRole.userRole;

        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(userUid)) builder.and(userRole.userUid.contains(userUid));
        if(StringUtils.hasText(roleCode)) builder.and(userRole.role.roleCode.contains(roleCode));

        return builder;
    }
}
