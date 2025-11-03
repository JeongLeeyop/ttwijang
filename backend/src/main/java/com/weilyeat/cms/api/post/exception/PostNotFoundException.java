package com.weilyeat.cms.api.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4689975835844813175L;
	
	public PostNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }
	
	public PostNotFoundException(String message) {
		super(message);
	}
	
	public PostNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
