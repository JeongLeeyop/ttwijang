package com.ttwijang.cms.api.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BoardException extends RuntimeException {
	
	private static final long serialVersionUID = 5732713957795574447L;

	public BoardException(String message) {
        super(message);
    }
}
