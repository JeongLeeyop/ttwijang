package com.weilyeat.cms.oauth.soical.kakao;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.core.GrantedAuthority;

import com.weilyeat.cms.oauth.soical.OAuth2UserInfo;
import com.weilyeat.cms.oauth.soical.SocialType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KakaoUser extends OAuth2UserInfo {

    private Map<String, Object> properties;
	private Map<String, Object> profile;

	public KakaoUser(Map<String, Object> attributes) {
        super(attributes);
        log.debug(attributes.toString());
        this.properties = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    public String getName() {
        if (this.properties.get("name") == null) return "";
        return (String) properties.get("name");
    }
    
    @Override
    public String getEmail() {
        if (this.properties.get("email") == null) return "";
        return (String) properties.get("email");
    }

    @Override
    public String getImageUrl() {
        if (this.properties.get("profile_image_url") == null) return "";
        return (String) profile.get("profile_image_url");
    }
    
    @Override
    public String getBirth() {
        if (this.properties.get("birthyear") == null) return "";
        String birth = "";

        Object objYear = this.properties.get("birthyear");
        Object objDay = this.properties.get("birthday");
        if (objYear != null && objDay != null) {
            birth = objYear + objDay.toString();
        }

        if (Pattern.matches("^(\\d{4})(\\d{2})(\\d{2})$", birth)) {
            birth = birth.replaceAll("^(\\d{4})(\\d{2})(\\d{2})$", "$1-$2-$3");
        }

    	return birth;
    }
    
    @Override
    public String getGender() {
        if (this.properties.get("gender") == null) return "";
        String gender = "";
    	Object objGender = this.properties.get("gender");
        if (objGender != null) {
            gender = objGender.toString();
        }
        if ("male".equalsIgnoreCase(gender.trim())) {
            gender = "M";
        } else {
            gender = "F";
        }

        return gender;
    }

	@Override
	public String getPhoneNumber() {
        if (this.properties.get("phone_number") == null) return "";
        String phone = "";
        Object objPhone = this.properties.get("phone_number");
        if (objPhone != null) {
            phone = objPhone.toString();
        }
        // 한국인 경우 국가코드 제거
        if (phone.startsWith("+82 ")) {
            phone = "0" + phone.replace("+82 ", "");
        }
        // 그렇지 않은 경우 국가코드 보존
		return phone;
	}

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
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
