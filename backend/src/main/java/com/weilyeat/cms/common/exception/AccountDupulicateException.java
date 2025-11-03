package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountDupulicateException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public AccountDupulicateException() {
        super("이미 사용중인 이메일 입니다.");
    }

    public AccountDupulicateException(String message) {
		super(message);
	}

	public AccountDupulicateException(String message, Throwable cause) {
		super(message, cause);
	}
}
