package com.weilyeat.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WeekDaysDeniedException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public WeekDaysDeniedException() {
        super("선택하실 수 없는 요일이 있습니다.");
    }

    public WeekDaysDeniedException(String message) {
        super(message);
    }
}
