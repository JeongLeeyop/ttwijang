package com.weilyeat.cms.api.attached_file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AttachedFileException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AttachedFileException(String message) {
		super(message);
	}
	
	public AttachedFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
