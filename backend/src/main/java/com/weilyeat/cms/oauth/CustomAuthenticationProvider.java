package com.weilyeat.cms.oauth;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilyeat.cms.api.coupon.repository.CouponRepository;
import com.weilyeat.cms.api.coupon.repository.UserCouponRepository;
// import com.weilyeat.cms.api.nice.service.NiceService;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.entity.Coupon;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.entity.UserCoupon;
import com.weilyeat.cms.entity.UserRole;
import com.weilyeat.cms.oauth.soical.OAuth2UserInfo;
import com.weilyeat.cms.oauth.soical.SocialType;
import com.weilyeat.cms.oauth.soical.apple.AppleApi;
import com.weilyeat.cms.oauth.soical.apple.AppleUser;
import com.weilyeat.cms.oauth.soical.kakao.KakaoApi;
import com.weilyeat.cms.oauth.soical.kakao.KakaoUser;
import com.weilyeat.cms.oauth.soical.naver.NaverApi;
import com.weilyeat.cms.oauth.soical.naver.NaverUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private KakaoApi kakaoClient;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NaverApi naverApi;

    @Autowired
    private AppleApi appleApi;
    
    // @Autowired
    // private NiceService niceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) {
        HashMap<String, Object> details = (HashMap<String, Object>) authentication.getDetails();
        
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) throw new BadCredentialsException("로그인 5회 이상 실패로 1시간동안 로그인이 차단됩니다.");
        if (details.get("provider_token") != null) {
            OAuth2UserInfo userInfo = getUserInfo(details.get("provider_token"));
            
            SocialType provider = userInfo.getSocialType();
            String providerId = userInfo.getId();
            Optional<User> optional = userRepository.findByProviderAndProviderId(provider, providerId);
            User user = null;
            if (optional.isEmpty()) {
                user = registerUser(userInfo);
            } else {
                user = optional.get();
            }
            
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            return new UsernamePasswordAuthenticationToken(new SinghaUser(user, authorities), provider.toString() + providerId, authorities);
            // return new AnonymousAuthenticationToken(userInfo.getId(), userInfo, AuthorityUtils.createAuthorityList("ROLE_USER"));
            /*
            return new OAuth2AuthenticationToken(
                userInfo, AuthorityUtils.createAuthorityList("ROLE_USER"), userInfo.getId()
            );
            */
        }

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if (StringUtils.hasText(user.getProviderId()) && StringUtils.hasText(user.getProvider().toString())) {
            throw new BadCredentialsException("잘못된 접근입니다.");
        }
        
        if (details.get("provider_token") == null) {
            if (!this.passwordEncoder.matches(password, user.getUserPassword())) {
                loginAttemptService.loginFailed(ip);
                throw new BadCredentialsException("패스워드가 일치하지않습니다.");
            }
        }

		List<GrantedAuthority> authorities = new ArrayList<>();

		List<UserRole> roles = user.getRoles();
		roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRole().getRoleCode())));

        return new UsernamePasswordAuthenticationToken(new SinghaUser(user, authorities), password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private OAuth2UserInfo getUserInfo(final Object extractorToken) {
        String convert_str = extractorToken.toString();
        String[] xHeader = convert_str.split(" ");
        String provider = xHeader[0];
        String accessToken = xHeader[1];
        SocialType type = SocialType.valueOf(provider.trim().toUpperCase());

        if (xHeader.length == 2) {
            String access_token = xHeader[1];

            if (type == SocialType.KAKAO) {
                ResponseEntity<HashMap<String, Object>> response = kakaoClient.me(accessToken);
                if (response.hasBody()) {
                    assert response.getBody() != null;
                    return new KakaoUser(response.getBody());
                }
            }

            if (type == SocialType.NAVER) {
                ResponseEntity<HashMap<String, Object>> response = naverApi.me(access_token);
                if (response.hasBody()) {
                    assert response.getBody() != null;
                    return new NaverUser(response.getBody());
                }
            }
            
            if (type == SocialType.APPLE) {
                String[] check = access_token.split("\\.");
                Base64.Decoder decoder = Base64.getDecoder();
                String payload = new String(decoder.decode(check[1]));
                JSONObject jsonObject = new JSONObject(payload);

                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(jsonObject.toString(), Map.class);
                } catch (Exception e) {
                    throw new BadRequestException(BadRequest.NOT_VALID_ID_TOKEN);
                }
                return new AppleUser(map);
            }

            if (xHeader[0].equalsIgnoreCase(SocialType.NICE.toString())) {
                // return niceService.getInfo(access_token);
                return null;
            }
        }
        return null;
    }

    private User registerUser(OAuth2UserInfo userInfo) {
        User user = new User();
        user.setUserId(userInfo.getSocialType().toString() + "/" + userInfo.getId());
        user.setUserPassword(userInfo.getSocialType().toString() + userInfo.getId());
        user.setJoinStatus(false);
        user.setProvider(userInfo.getSocialType());
        user.setProviderId(userInfo.getId());
        user.setEnabled(true);
        user.setNotLocked(true);
        user.setPoint(0);
        user.setRoles(new ArrayList<UserRole>());
        user = userRepository.save(user);

        List<Coupon> eventCouponList = couponRepository.getJoinEventCouponList(LocalDateTime.now());
        List<UserCoupon> giveCouponList = new ArrayList<UserCoupon>();
        for (Coupon coupon : eventCouponList) {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserUid(user.getUid());
            userCoupon.setCouponIdx(coupon.getIdx());
            userCoupon.setName(coupon.getName());
            userCoupon.setType(coupon.getType());
            userCoupon.setPercentStatus(coupon.isPercentStatus());
            userCoupon.setDiscountPercent(coupon.getDiscountPercent());
            userCoupon.setDiscountPrice(coupon.getDiscountPrice());
            userCoupon.setExpiredDate(coupon.getExpiredDate());
            
            giveCouponList.add(userCoupon);
        }
        userCouponRepository.saveAll(giveCouponList);

        return user;
    }
}
