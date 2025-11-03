package com.ttwijang.cms.oauth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	@Autowired
	public TokenStore tokenStore;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// resources.stateless(true); // 리소스에서 토큰 기반 인증 모두 허용 JUNIT 때문.
		resources.resourceId(resourceIds).tokenStore(tokenStore);
		resources.tokenExtractor(new TokenExtractor() {
			@Override
			public Authentication extract(HttpServletRequest request) {
				Cookie[] cookies = request.getCookies();
				if (cookies == null)
					return null;

				for (Cookie cookie : cookies) {
					if (OAuth2AccessToken.ACCESS_TOKEN.equals(cookie.getName())) {
						PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
								cookie.getValue(), "");
						return authentication;
					}
				}
				return null;
			}
		});
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
			.antMatchers("/api/attached-file/**").permitAll()
			.antMatchers("/api/post/upload").permitAll()
			.antMatchers("/api/client/**").permitAll()
			.antMatchers("/oauth/naver/login").permitAll()
			.antMatchers("/oauth/naver/me").permitAll()
			.antMatchers("/oauth/naver/access").permitAll()
			.antMatchers("/oauth/apple/login").permitAll()
			.antMatchers("/api/bank/info").permitAll()
			.antMatchers("/api/admin/**").permitAll()
			.anyRequest().authenticated();
	}
}
