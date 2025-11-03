package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public AccountNotFoundException() {
        super("유저를 찾을 수 없습니다.");
    }

    public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
