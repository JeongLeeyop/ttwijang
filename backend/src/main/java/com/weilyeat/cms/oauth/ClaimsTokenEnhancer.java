package com.ttwijang.cms.oauth;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.ttwijang.cms.oauth.soical.OAuth2UserInfo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClaimsTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additional = new HashMap<>();
        if (authentication.getPrincipal() instanceof OAuth2UserInfo) {
            OAuth2UserInfo user = (OAuth2UserInfo) authentication.getPrincipal();
            additional.put("phone", user.getPhoneNumber());
            additional.put("actualName", user.getName());
            additional.put("birth", user.getBirth());
		}
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        token.setAdditionalInformation(additional);
        return accessToken;
    }

}
