package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostDeletedException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public PostDeletedException() {
        super("삭제된 게시글 입니다.");
    }
}
