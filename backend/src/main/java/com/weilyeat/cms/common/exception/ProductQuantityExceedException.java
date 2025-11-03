package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductQuantityExceedException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public ProductQuantityExceedException() {
        super("상품 수량 초과입니다.");
    }

    public ProductQuantityExceedException(String message) {
        super(message);
    }
}
