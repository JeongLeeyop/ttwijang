package com.weilyeat.cms.api.user.service;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.repository.MissionRepository;
import com.weilyeat.cms.api.mission.repository.MissionFileRepository;
import com.weilyeat.cms.api.mission.repository.MissionUserRepository;
import com.weilyeat.cms.api.mission_record.repository.MissionRecordRepository;
import com.weilyeat.cms.api.mission_record.repository.MissionRecordFileRepository;
import com.weilyeat.cms.api.mission_user_inquiry.repository.MissionUserInquiryRepository;
import com.weilyeat.cms.api.challenge_record.repository.ChallengeRecordFileRepository;
import com.weilyeat.cms.api.challenge_record.repository.ChallengeRecordRepository;
import com.weilyeat.cms.api.challenge.repository.ChallengeUserRepository;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.api.product.repository.ProductOrderGroupRepository;
import com.weilyeat.cms.api.product.service.ProductOrderService;
import com.weilyeat.cms.api.shop.repository.ShopRepository;
import com.weilyeat.cms.api.station.repository.StationRepository;
import com.weilyeat.cms.api.tfse.repository.TfseRepository;
import com.weilyeat.cms.api.tfse_feedback.repository.SelfFeedbackRepository;
import com.weilyeat.cms.api.user.dto.ClientUserDto;
import com.weilyeat.cms.api.user.dto.mapper.ClientUserMapper;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.api.user.repository.UserRoleRepository;
import com.weilyeat.cms.api.withdraw.repository.WithdrawHistoryRepository;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.entity.UserRole;
import com.weilyeat.cms.entity.WithdrawHistory;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ClientUserService {
    void join(ClientUserDto.join dto, SinghaUser authUser);

    ClientUserDto.info info(SinghaUser authUser);

    void updateInfo(ClientUserDto.update updateDto, SinghaUser authUser);
    
	void setShop(Integer idx, SinghaUser authUser);
	
	void setStation(Integer idx, SinghaUser authUser);

	void withdraw(ClientUserDto.withdraw dto, SinghaUser authUser);

	Map<String, String> findLatLon(String address);
}

@Service
@AllArgsConstructor
class ClientUserServiceImpl implements ClientUserService {
    private final UserRepository userRepository;
    
	private final ShopRepository shopRepository;
	
	private final StationRepository stationRepository;

	private final UserRoleRepository userRoleRepository;

	private final TokenStore tokenStore;

	private final WithdrawHistoryRepository withdrawHistoryRepository;

	private final PostRepository postRepository;
	
	private final TfseRepository tfseRepository;
	
	private final SelfFeedbackRepository selfFeedbackRepository;

	private final ProductOrderService productOrderService;

	private final ProductOrderGroupRepository productOrderGroupRepository;

	private final UserFcmTokenRepository userFcmTokenRepository;

	private final MissionRepository missionRepository;

	private final MissionFileRepository missionFileRepository;

	private final MissionUserRepository missionUserRepository;

	private final MissionRecordRepository missionRecordRepository;

	private final MissionRecordFileRepository missionRecordFileRepository;

	private final MissionUserInquiryRepository missionUserInquiryRepository;	

	private final ChallengeRecordRepository challengeRecordRepository;

	private final ChallengeRecordFileRepository challengeRecordFileRepository;

	private final ChallengeUserRepository challengeUserRepository;

	@Transactional
	@Override
	public void join(ClientUserDto.join dto, SinghaUser authUser) {
		User user = authUser.getUser();

		if (user.isJoinStatus()) throw new BadRequestException(BadRequest.ALREADY_JOIN_USER);

		user = ClientUserMapper.INSTANCE.joinDtoToEntity(dto, user);
		user.setJoinStatus(true);
		user.setPoint(0);

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
		user.setRegisterInfoStatus(true);

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

	@Override
	public void setShop(Integer idx, SinghaUser authUser) {
		shopRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
		user.setShopIdx(idx);
		userRepository.save(user);
	}

	@Override
	public void setStation(Integer idx, SinghaUser authUser) {
		stationRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.STATION));
		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
		user.setStationIdx(idx);
		userRepository.save(user);
	}

	@Transactional
	@Override
	public void withdraw(ClientUserDto.withdraw dto, SinghaUser authUser) {
		int remainOrderCount = productOrderService.getRemainCount(authUser);
		if (remainOrderCount > 0) throw new BadRequestException(BadRequest.REMAIN_ORDER);

		User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
		
		userRoleRepository.deleteByUserUid(user.getUid());
		userFcmTokenRepository.deleteByUserUid(user.getUid());
		
		// 미션 관련 데이터 삭제 (순서 중요: 자식 -> 부모 순)
		missionUserInquiryRepository.deleteAllByUserUid(user.getUid()); // 미션 사용자 문의
		missionRecordFileRepository.deleteByUserUid(user.getUid()); // 미션 기록 파일
		missionRecordRepository.deleteByUserUid(user.getUid());     // 미션 기록
		missionUserRepository.deleteByUserUid(user.getUid());       // 미션 유저 참여 정보
		// missionFileRepository.deleteByUserUid(user.getUid());       // 미션 파일
		// missionRepository.deleteByUserUid(user.getUid());           // 미션

		// 챌린지 관련 데이터 삭제 (순서 중요: 파일 -> 기록 -> 참여정보)
			challengeRecordFileRepository.deleteByUserUid(user.getUid());
			challengeRecordRepository.deleteByUserUid(user.getUid());
			challengeUserRepository.deleteByUserUid(user.getUid());
		
		WithdrawHistory withdrawHistory = new WithdrawHistory();
		withdrawHistory.setReason(dto.getReason());
		withdrawHistory.setUserName(user.getActualName());
		withdrawHistory.setUserUid(user.getUid());
		withdrawHistory.setUserId(user.getUserId());
		withdrawHistoryRepository.save(withdrawHistory);
		
		postRepository.deleteWithdrawUser(user.getUid());
		tfseRepository.deleteByUserUid(user.getUid());
		selfFeedbackRepository.deleteByUserUid(user.getUid());
		
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("singha_oauth", user.getUserId());
		for (OAuth2AccessToken token : tokens) {
			tokenStore.removeAccessToken(token);
		}
		
		productOrderGroupRepository.withdrawUser(user.getUid());
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

}