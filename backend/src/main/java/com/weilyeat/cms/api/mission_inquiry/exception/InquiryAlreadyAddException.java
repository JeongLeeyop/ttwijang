package com.weilyeat.cms.api.mission_inquiry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InquiryAlreadyAddException extends RuntimeException {

	private static final long serialVersionUID = 5159732216358561124L;
	
	public InquiryAlreadyAddException() {
		super("이미 오늘은 미션 기록이 등록 되었습니다.");
	}
	
	public InquiryAlreadyAddException(String message) {
		super(message);
	}
}
