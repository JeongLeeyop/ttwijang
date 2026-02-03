package com.ttwijang.cms.api.attached_file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AttachedFileNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AttachedFileNotFoundException(String message) {
		super(message);
	}
	
	public AttachedFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
