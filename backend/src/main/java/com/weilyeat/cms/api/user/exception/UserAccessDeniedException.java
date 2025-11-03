package com.weilyeat.cms.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAccessDeniedException extends RuntimeException {
    public UserAccessDeniedException() {
        super("접근 권한이 없습니다.");
    }
}
