package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommonException extends RuntimeException {
    
    private static final long serialVersionUID = 176477432217217602L;

    public CommonException() {
        super("찾을 수 없습니다.");
    }

    public CommonException(String message) {
        super(message);
    }

    
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}
}
