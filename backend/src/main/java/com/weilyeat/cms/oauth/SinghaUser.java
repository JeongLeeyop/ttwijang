package com.ttwijang.cms.oauth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class SinghaUser extends User {

	private static final long serialVersionUID = -1164526427887641766L;

	private com.ttwijang.cms.entity.User user;

	public SinghaUser(com.ttwijang.cms.entity.User user, List<GrantedAuthority> authorities) {
		super(user.getUserId(), user.getUserPassword(), user.isEnabled(), true, true, user.isNotLocked(), authorities);
		this.user = user;
	}

}
