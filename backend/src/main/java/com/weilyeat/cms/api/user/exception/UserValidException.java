package com.weilyeat.cms.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserValidException extends RuntimeException {

	private static final long serialVersionUID = 2144172611182314253L;
	
	public UserValidException() {
		super();
	}
	
	public UserValidException(String message) {
		super(message);
	}
	
}
