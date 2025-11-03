package com.weilyeat.cms.api.push_alarm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PushAlarmAccessException extends RuntimeException {
    public PushAlarmAccessException() {
		super("권한이 없습니다.");
	}
}
