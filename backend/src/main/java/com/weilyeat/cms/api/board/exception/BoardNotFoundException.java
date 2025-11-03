package com.weilyeat.cms.api.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public BoardNotFoundException() {
        super("게시판을 찾을 수 없습니다.");
    }
}
