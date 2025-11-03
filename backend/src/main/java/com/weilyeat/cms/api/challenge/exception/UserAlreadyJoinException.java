package com.weilyeat.cms.api.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserAlreadyJoinException extends RuntimeException {

	private static final long serialVersionUID = 5159732216358561124L;
	
	public UserAlreadyJoinException() {
		super("이미 등록된 유저입니다.");
	}
	
	public UserAlreadyJoinException(String message) {
		super(message);
	}
}
