package com.ttwijang.cms.oauth.soical;

import java.io.Serializable;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public abstract class OAuth2UserInfo implements Serializable, OAuth2User, UserDetails {

	protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();
    
    public abstract String getPhoneNumber();
    
    public abstract String getImageUrl();

    public abstract String getGender();
    
    public abstract String getBirth();

    public abstract SocialType getSocialType();

}
