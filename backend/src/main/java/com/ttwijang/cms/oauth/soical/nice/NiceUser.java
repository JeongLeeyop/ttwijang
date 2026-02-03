package com.ttwijang.cms.oauth.soical.nice;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.ttwijang.cms.oauth.soical.OAuth2UserInfo;
import com.ttwijang.cms.oauth.soical.SocialType;

public class NiceUser extends OAuth2UserInfo {

	public NiceUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("ci");
    }

    public String getName() {
        return (String) attributes.get("name");
    }
    
    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }
    
    @Override
    public String getBirth() {
    	return ((String) attributes.get("birthDate")).replaceAll("[^0-9]", "");
    }
    
    @Override
    public String getGender() {
    	return (String) attributes.get("gendar");
    }

	@Override
	public String getPhoneNumber() {
		return (String) attributes.get("phonNumber");
	}

    @Override
    public SocialType getSocialType() {
        return SocialType.NICE;
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
        return (String) attributes.get("ci");
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
