package com.weilyeat.cms.api.board.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QBoard;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearch {
	private String searchType;
	private String searchValue;
	
	public Predicate search() {
		
		QBoard board = QBoard.board;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if (StringUtils.hasText(searchType)) {
			switch (searchType) {
			case "name":
				builder.and(board.name.contains(searchValue));
				break;
			case "skin":
				builder.and(board.skin.contains(searchValue));
				break;
			default:
				break;
			}
		}
		return builder;
	}
	
}
