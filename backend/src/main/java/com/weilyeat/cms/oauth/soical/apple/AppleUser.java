package com.weilyeat.cms.oauth.soical.apple;


import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.weilyeat.cms.oauth.soical.OAuth2UserInfo;
import com.weilyeat.cms.oauth.soical.SocialType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppleUser extends OAuth2UserInfo {

    private Map<String, Object> properties;
	private Map<String, Object> profile;

	public AppleUser(Map<String, Object> attributes) {
        super(attributes);
        this.profile = this.attributes;
    }

    @Override
    public String getId() {
        return (String) profile.get("sub").toString();
    }

    public String getName() {
        return "";
    }
    
    @Override
    public String getEmail() {
        // return (String) profile.get("email");
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }
    
    @Override
    public String getBirth() {
    	return "";
    }
    
    @Override
    public String getGender() {
    	return "";
    }

	@Override
	public String getPhoneNumber() {
		return "";
	}

    @Override
    public SocialType getSocialType() {
        return SocialType.APPLE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        return (String) profile.get("sub").toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
