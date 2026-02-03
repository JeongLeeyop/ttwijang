package com.ttwijang.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PostSearchNotValidExcpetion extends RuntimeException {
	
	private static final long serialVersionUID = 1409609160674522685L;

	public PostSearchNotValidExcpetion(String message) {
		super(message);
	}
	
	public PostSearchNotValidExcpetion(String message, Throwable cause) {
		super(message, cause);
	}
}
