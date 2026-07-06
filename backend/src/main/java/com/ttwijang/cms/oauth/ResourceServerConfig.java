package com.ttwijang.cms.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

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
		// tokenExtractor를 별도 지정하지 않으면 기본 BearerTokenExtractor가 사용되어
		// Authorization: Bearer <token> 헤더로 인증한다 (사용자/관리자 앱이 각자 헤더로
		// 토큰을 실어보내므로 브라우저 쿠키를 공유해도 세션이 서로 덮어쓰이지 않는다).
		resources.resourceId(resourceIds).tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()  // HTTP Basic Authentication 비활성화
			.csrf().disable()       // CSRF 비활성화
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
			.antMatchers("/oauth/**").permitAll()  // 모든 OAuth 엔드포인트 허용
			.antMatchers("/api/attached-file/**").permitAll()
			.antMatchers("/api/post/upload").permitAll()
			.antMatchers("/api/team/upload").permitAll()
			.antMatchers("/api/client/**").permitAll()
			.antMatchers("/api/guest/calendar").permitAll()
			.antMatchers("/api/guest", "/api/guest/").permitAll()
			.antMatchers("/api/bank/info").permitAll()
			.antMatchers("/api/admin/**").permitAll()
			.antMatchers("/api/banner/active").permitAll()
			.antMatchers("/api/regions/**").permitAll()
			.antMatchers("/swagger-ui/**").permitAll()
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/api-docs/**").permitAll()
			.antMatchers("/v3/api-docs/**").permitAll()
			.anyRequest().authenticated();
	}
}
