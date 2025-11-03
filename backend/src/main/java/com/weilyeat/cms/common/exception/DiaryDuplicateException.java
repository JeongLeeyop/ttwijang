package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DiaryDuplicateException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public DiaryDuplicateException() {
        super("이미 다이어리가 등록된 날짜입니다.");
    }

    public DiaryDuplicateException(String message) {
		super(message);
	}

	public DiaryDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}
}
