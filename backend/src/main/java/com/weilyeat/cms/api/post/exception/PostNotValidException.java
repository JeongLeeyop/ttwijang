package com.weilyeat.cms.api.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PostNotValidException extends RuntimeException {

	private static final long serialVersionUID = 4865955900771767911L;
	
	public PostNotValidException(String message) {
		super(message);
	}
	
	public PostNotValidException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
