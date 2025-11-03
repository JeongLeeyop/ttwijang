package com.ttwijang.cms.oauth.soical.apple.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppleLoginInfo {
    private String code;
    private String idToken;
}
