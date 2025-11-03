package com.ttwijang.cms.api.board.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QBoardCategory;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardCategorySearch extends SearchDefault {
	public Predicate search() {
        QBoardCategory boardCategory = QBoardCategory.boardCategory;
        
        BooleanBuilder builder = super.initSearch(boardCategory);
        builder.and(boardCategory.parentUid.isNull());

        return builder;
    }
}
