package com.weilyeat.cms.common.search;

import java.lang.reflect.Field;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class SearchDefault {
    
    private String searchType;
    private String searchValue;

    public BooleanBuilder initSearch(Object searchData) {

        BooleanBuilder builder = new BooleanBuilder();
        if(!(searchType == null || searchType.equals("") || !StringUtils.hasText(searchValue))) {
            Field field;
            try {
                field = searchData.getClass().getField(searchType);
                StringPath stringPath = (StringPath) field.get(searchData);
                builder.and(stringPath.contains(searchValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder;
    }
}
