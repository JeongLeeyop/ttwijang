package com.ttwijang.cms.common.search;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QAccount;

import lombok.Data;

@Data
public class AccountSearch extends SearchDefault {

    private String name;
    // private String birth;
    private String tel;
    private String email;
    
    public Predicate search() {

        QAccount account = QAccount.account;
        
        BooleanBuilder builder = super.initSearch(account);

        if (StringUtils.hasText(super.getSearchType())) {
            if (super.getSearchType().equals("name")) builder.and(account.name.contains(super.getSearchValue()));
            // if (super.getSearchType().equals("birth")) builder.and(account.birth.contains(super.getSearchValue()));
            if (super.getSearchType().equals("tel")) builder.and(account.tel.contains(super.getSearchValue()));
            if (super.getSearchType().equals("email")) builder.and(account.email.contains(super.getSearchValue()));
        }
         
        builder.and(account.withdrawStatus.eq(false));

        return builder;
    }

}
 