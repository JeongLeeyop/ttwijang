package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AttachedFileNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public AttachedFileNotFoundException() {
        super("파일을 찾을 수 없습니다.");
    }

    public AttachedFileNotFoundException(String message) {
		super(message);
	}

	public AttachedFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
