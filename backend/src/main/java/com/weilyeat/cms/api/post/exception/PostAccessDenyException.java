package com.ttwijang.cms.api.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PostAccessDenyException extends RuntimeException {

	private static final long serialVersionUID = 5159732216358561124L;
	
	public PostAccessDenyException() {
		super("권한이 없습니다.");
	}
	
	public PostAccessDenyException(String message) {
		super(message);
	}
}
