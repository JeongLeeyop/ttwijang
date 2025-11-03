package com.weilyeat.cms.api.challenge_record.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class RecordAlreadyAddException extends RuntimeException {

	private static final long serialVersionUID = 5159732216358561124L;
	
	public RecordAlreadyAddException() {
		super("이미 오늘은 챌린지 기록이 등록 되었습니다.");
	}
	
	public RecordAlreadyAddException(String message) {
		super(message);
	}
}
