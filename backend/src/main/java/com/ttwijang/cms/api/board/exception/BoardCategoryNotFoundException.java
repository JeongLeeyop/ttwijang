package com.ttwijang.cms.api.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BoardCategoryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7896196336311283962L;

    public BoardCategoryNotFoundException() {
        super("카테고리를 찾을 수 없습니다.");
    }
}
