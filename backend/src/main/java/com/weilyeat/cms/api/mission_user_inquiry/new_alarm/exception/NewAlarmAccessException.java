package com.weilyeat.cms.api.mission_user_inquiry.new_alarm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NewAlarmAccessException extends RuntimeException {
    public NewAlarmAccessException() {
		super("권한이 없습니다.");
	}
}
