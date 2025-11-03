package com.weilyeat.cms.oauth.soical.naver;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.weilyeat.cms.oauth.soical.OAuth2UserInfo;
import com.weilyeat.cms.oauth.soical.SocialType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaverUser extends OAuth2UserInfo {

    private Map<String, Object> properties;
	private Map<String, Object> profile;

	public NaverUser(Map<String, Object> attributes) {
        super(attributes);
        this.profile = (Map<String, Object>) this.attributes.get("response");
    }

    @Override
    public String getId() {
        return (String) profile.get("id").toString();
    }

    public String getName() {
        return (String) profile.get("name");
    }
    
    @Override
    public String getEmail() {
        return (String) profile.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) profile.get("profile_image_url");
    }
    
    @Override
    public String getBirth() {
    	return ((String) profile.get("birthyear")+(String) profile.get("birthday")).replaceAll("[^0-9]", "");
    }
    
    @Override
    public String getGender() {
    	return (String) profile.get("gender");
    }

	@Override
	public String getPhoneNumber() {
		return (String) profile.get("mobile");
	}

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
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
        return (String) profile.get("id").toString();
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
