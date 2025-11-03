package com.weilyeat.cms.api.user.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserFcmNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserFcmNotFoundException() {
        super();
    }

    public UserFcmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserFcmNotFoundException(String message) {
        super(message);
    }

    public UserFcmNotFoundException(Throwable cause) {
        super(cause);
    }
}
