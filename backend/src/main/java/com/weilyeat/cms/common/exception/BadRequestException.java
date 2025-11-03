package com.weilyeat.cms.common.exception;

import com.weilyeat.cms.common.exception.code.BadRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(BadRequest message) {
        super(message.toString());
    }
}
