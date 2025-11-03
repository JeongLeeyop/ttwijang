package com.ttwijang.cms.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.entity.UserRole;

import lombok.AllArgsConstructor;

@Service("userDetailsService")
@AllArgsConstructor
public class SinghaUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

    private final LoginAttemptService loginAttemptService;

	private final HttpServletRequest request;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		if (loginAttemptService.isBlocked(getClientIP())) {
            throw new BadCredentialsException("로그인 5회 이상 실패로 1시간동안 로그인이 차단됩니다.");
        }

		Optional<User> opUser = userRepository.findByUserId(userId);

		User user = opUser.orElseThrow(() -> new UsernameNotFoundException(userId));
		List<GrantedAuthority> authorities = new ArrayList<>();

		List<UserRole> roles = user.getRoles();
		roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRole().getRoleCode())));
		return new SinghaUser(user, authorities);
	}

	private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
