package com.ttwijang.cms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserCouponNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2114695658880773412L;
	
	public UserCouponNotFoundException() {
        super("사용자 쿠폰 정보를 찾을 수 없습니다.");
    }
}
