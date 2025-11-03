package com.ttwijang.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DiaryNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public DiaryNotFoundException() {
        super("다이어리를 찾을 수 없습니다.");
    }

    public DiaryNotFoundException(String message) {
		super(message);
	}

	public DiaryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
