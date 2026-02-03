package com.ttwijang.cms.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
        // 계정 타입
        Guest("ROLE_GUEST", "손님"),
        USER("ROLE_USER", "일반 사용자");
    
        private final String key;
        private final String title;
        
        }
