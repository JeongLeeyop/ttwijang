package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TagTooManyException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public TagTooManyException() {
        super("태그 수량 초과입니다.");
    }
}
