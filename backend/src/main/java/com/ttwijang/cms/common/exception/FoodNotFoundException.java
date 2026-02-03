package com.ttwijang.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FoodNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public FoodNotFoundException() {
        super("음식정보를 찾을 수 없습니다.");
    }

    public FoodNotFoundException(String message) {
		super(message);
	}

	public FoodNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
