package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public CommentNotFoundException() {
        super("댓글을 찾을 수 없습니다.");
    }
}
