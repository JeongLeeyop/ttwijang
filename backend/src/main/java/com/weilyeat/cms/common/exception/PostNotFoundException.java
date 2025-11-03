package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public PostNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }
}
