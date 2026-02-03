package com.ttwijang.cms.api.user.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.api.user.dto.ClientUserDto;
import com.ttwijang.cms.api.user.dto.mapper.ClientUserMapper;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.api.user.repository.UserRoleRepository;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.BadRequest;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.entity.UserRole;
import com.ttwijang.cms.entity.WithdrawHistory;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ClientUserService {
void join(ClientUserDto.join dto);

    ClientUserDto.info info(SinghaUser authUser);

    void updateInfo(ClientUserDto.update updateDto, SinghaUser authUser);
    
	void withdraw(ClientUserDto.withdraw dto, SinghaUser authUser);

	Map<String, String> findLatLon(String address);

	boolean checkPhoneNumberDuplicate(String phoneNumber);

	String findEmailByPhoneNumber(String phoneNumber);

	String requestPasswordReset(String email, String phoneNumber);

	boolean resetPassword(String resetToken, String newPassword);
}

@Service
@AllArgsConstructor
class ClientUserServiceImpl implements ClientUserService {
    private final UserRepository userRepository;
    
	private final UserRoleRepository userRoleRepository;

	private final TokenStore tokenStore;

	private final PostRepository postRepository;
	
	private final UserFcmTokenRepository userFcmTokenRepository;

	// 임시 비밀번호 재설정 토큰 저장소 (실제 운영환경에서는 Redis 등 사용 권장)
	private final Map<String, PasswordResetToken> resetTokenStore = new HashMap<>();

	// 비밀번호 재설정 토큰 내부 클래스
	private static class PasswordResetToken {
		private final String email;
		private final String phoneNumber;
		private final long expirationTime;

		public PasswordResetToken(String email, String phoneNumber) {
			this.email = email;
			this.phoneNumber = phoneNumber;
			this.expirationTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30분 유효
		}

		public boolean isExpired() {
			return System.currentTimeMillis() > expirationTime;
		}

		public String getEmail() {
			return email;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}
	}

	@Transactional
	@Override
	public void join(ClientUserDto.join dto) {
		// 이메일 중복 체크
		if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new BadRequestException(BadRequest.DUPLICATE_EMAIL);
		}

		// 휴대폰 번호 중복 체크
		if (userRepository.existsByConcatNumber(dto.getConcatNumber())) {
			throw new BadRequestException(BadRequest.DUPLICATE_PHONE_NUMBER);
		}

		// 새로운 User 엔티티 생성
		User user = new User();
		user.setUserId(dto.getEmail());
		user.setEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		user.setEnabled(true);
		user.setNotLocked(true);
		user.setJoinStatus(true);
		user.setPoint(0);

		// DTO에서 데이터 매핑
		user.setActualName(dto.getActualName());
		user.setConcatNumber(dto.getConcatNumber());
		user.setBirth(dto.getBirth());
		user.setGender(dto.getGender());
		user.setMarketingStatus(dto.isMarketingStatus());

		// 주소 정보
		if (dto.getPostCode() != null) user.setPostCode(dto.getPostCode());
		if (dto.getAddress() != null) {
			user.setAddress(dto.getAddress());
			// 주소가 있을 경우에만 좌표 조회
			Map<String, String> map = this.findLatLon(dto.getAddress());
			if(map.containsKey("Lat")) user.setLat(map.get("Lat"));
			if(map.containsKey("Lon")) user.setLon(map.get("Lon"));
		}
		if (dto.getAddressDetail() != null) user.setAddressDetail(dto.getAddressDetail());
		
		userRepository.save(user);

		// 사용자 권한 추가
		UserRole userRole = new UserRole();
		userRole.setUserUid(user.getUid());
		userRole.setRoleCode("ROLE_USER");
		userRoleRepository.save(userRole);
	}

	@Transactional
	@Override
	public ClientUserDto.info info(SinghaUser authUser) {
		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
		return ClientUserMapper.INSTANCE.entityToInfoDto(user);
	}

	@Transactional
	@Override
	public void updateInfo(ClientUserDto.update updateDto, SinghaUser authUser) {
		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));

		// if (user.isJoinStatus()) throw new BadRequestException(BadRequest.ALREADY_JOIN_USER);

		user = ClientUserMapper.INSTANCE.updateDtoToEntity(updateDto, user);
		
		user.setJoinStatus(true);

		Map<String, String> map = this.findLatLon(user.getAddress());
		if(map.containsKey("Lat")) user.setLat(map.get("Lat"));
		if(map.containsKey("Lon")) user.setLon(map.get("Lon"));

		userRepository.save(user);
		
		UserRole userRole = new UserRole();
		userRole.setUserUid(user.getUid());
		userRole.setRoleCode("ROLE_USER");
		userRoleRepository.save(userRole);

		
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("singha_oauth", user.getUserId());
		for (OAuth2AccessToken token : tokens) {
			tokenStore.removeAccessToken(token);
		}
	}

	@Transactional
	@Override
	public void withdraw(ClientUserDto.withdraw dto, SinghaUser authUser) {
		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
		
		userRoleRepository.deleteByUserUid(user.getUid());
		userFcmTokenRepository.deleteByUserUid(user.getUid());
		
		// 미션 관련 데이터 삭제 (순서 중요: 자식 -> 부모 순)
		// missionFileRepository.deleteByUserUid(user.getUid());       // 미션 파일
		// missionRepository.deleteByUserUid(user.getUid());           // 미션

		// 챌린지 관련 데이터 삭제 (순서 중요: 파일 -> 기록 -> 참여정보)
			// challengeRecordFileRepository.deleteByUserUid(user.getUid());
			// challengeRecordRepository.deleteByUserUid(user.getUid());
			// challengeUserRepository.deleteByUserUid(user.getUid());
		
		WithdrawHistory withdrawHistory = new WithdrawHistory();
		withdrawHistory.setReason(dto.getReason());
		withdrawHistory.setUserName(user.getActualName());
		withdrawHistory.setUserUid(user.getUid());
		withdrawHistory.setUserId(user.getUserId());
		// withdrawHistoryRepository.save(withdrawHistory);
		
		postRepository.deleteWithdrawUser(user.getUid());
		// tfseRepository.deleteByUserUid(user.getUid());
		// selfFeedbackRepository.deleteByUserUid(user.getUid());
		
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("singha_oauth", user.getUserId());
		for (OAuth2AccessToken token : tokens) {
			tokenStore.removeAccessToken(token);
		}
		
		// productOrderGroupRepository.withdrawUser(user.getUid());
		userRepository.delete(user);
	}

    @Override
    public Map<String, String> findLatLon(String address) {
        final String HOST = "http://dapi.kakao.com";
        URI targetUrl = UriComponentsBuilder.fromUriString(HOST+"/v2/local/search/address.json")
            .queryParam("query", address)
            .queryParam("page", "1")
            .queryParam("size", "10")
            .build()
            .encode(StandardCharsets.UTF_8).toUri();
        RestTemplate template = new RestTemplate();
        
        HttpHeaders headers  = new HttpHeaders();
        headers.add("Authorization", "KakaoAK ce5081b76da6325966891111d1231612");
        HttpEntity<String> entity = new HttpEntity<>(headers); 
        
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
        
        ResponseEntity<String> response = template.exchange(targetUrl, HttpMethod.GET, entity, responseType);
        String responseBody = response.getBody();
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("documents");
           
			Map<String, String> map = new HashMap<String, String>();
			if (jsonArray.length() > 0) {
                JSONObject addressData = jsonArray.getJSONObject(0);
                if (addressData.has("y")) map.put("Lat",addressData.getString("y"));
                if (addressData.has("x")) map.put("Lon",addressData.getString("x"));
            }
			return map;
        } catch (Exception e) {
			e.printStackTrace();
        }
		return null;
    }

	@Override
	public boolean checkPhoneNumberDuplicate(String phoneNumber) {
		return userRepository.existsByConcatNumber(phoneNumber);
	}

	@Override
	public String findEmailByPhoneNumber(String phoneNumber) {
		User user = userRepository.findByConcatNumber(phoneNumber)
			.orElseThrow(() -> new NotFoundException("해당 휴대폰 번호로 가입된 계정을 찾을 수 없습니다."));
		return user.getEmail();
	}

	@Override
	public String requestPasswordReset(String email, String phoneNumber) {
		// 이메일과 휴대폰 번호로 사용자 조회
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 계정을 찾을 수 없습니다."));

		// 휴대폰 번호 일치 여부 확인
		if (!user.getConcatNumber().equals(phoneNumber)) {
			throw new BadRequestException(BadRequest.PASSWORD_RESET_MISMATCH);
		}

		// 재설정 토큰 생성 (UUID 사용)
		String resetToken = java.util.UUID.randomUUID().toString();
		resetTokenStore.put(resetToken, new PasswordResetToken(email, phoneNumber));

		return resetToken;
	}

	@Transactional
	@Override
	public boolean resetPassword(String resetToken, String newPassword) {
		// 토큰 검증
		PasswordResetToken tokenData = resetTokenStore.get(resetToken);
		if (tokenData == null) {
			throw new BadRequestException(BadRequest.INVALID_RESET_TOKEN);
		}

		if (tokenData.isExpired()) {
			resetTokenStore.remove(resetToken);
			throw new BadRequestException(BadRequest.EXPIRED_RESET_TOKEN);
		}

		// 사용자 조회 및 비밀번호 변경
		User user = userRepository.findByEmail(tokenData.getEmail())
			.orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

		user.setUserPassword(newPassword);
		userRepository.save(user);

		// 사용된 토큰 삭제
		resetTokenStore.remove(resetToken);

		// 기존 토큰 무효화
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("singha_oauth", user.getUserId());
		for (OAuth2AccessToken token : tokens) {
			tokenStore.removeAccessToken(token);
		}

		return true;
	}

}