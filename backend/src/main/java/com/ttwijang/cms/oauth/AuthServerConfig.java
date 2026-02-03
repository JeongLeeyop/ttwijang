package com.ttwijang.cms.oauth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter tokenConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClaimsTokenEnhancer tokenEnhancer;

    @Autowired
    @Qualifier("oauth")
    private DataSource dataSourceOAuth;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(dataSourceOAuth);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(actualNameTokenEnhancer(), tokenConverter));

        endpoints.tokenStore(tokenStore) // 저장소 타입 - JDBC STORE
                .tokenEnhancer(chain) // 토큰 커스텀
                .accessTokenConverter(tokenConverter) // 토큰컨버터 - JWT
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");

    }

    @Bean
	public TokenEnhancer actualNameTokenEnhancer() {
		return new TokenEnhancer() {
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                SinghaUser user = (SinghaUser)authentication.getPrincipal();
				Map<String, Object> additionalInfo = new HashMap<>();
		        String actualName = "";
                if (user.getUser() != null) actualName = user.getUser().getActualName();
                additionalInfo.put("actualName", actualName);
                additionalInfo.put("status", user.getUser().isJoinStatus());
		        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		        return accessToken;
			}
		};
	}
}
