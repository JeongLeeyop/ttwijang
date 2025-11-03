package com.weilyeat.cms.api.user.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserDuplicateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserDuplicateException() {
        super();
    }

    public UserDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDuplicateException(String message) {
        super(message);
    }

    public UserDuplicateException(Throwable cause) {
        super(cause);
    }
}
