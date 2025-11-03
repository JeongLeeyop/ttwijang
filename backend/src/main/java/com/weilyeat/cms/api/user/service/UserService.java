package com.ttwijang.cms.api.user.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.api.product.repository.ProductOrderGroupRepository;
import com.ttwijang.cms.api.user.dto.UserDto;
import com.ttwijang.cms.api.user.dto.mapper.UserMapper;
import com.ttwijang.cms.api.user.dto.search.UserSearch;
import com.ttwijang.cms.api.user.exception.UserDuplicateException;
import com.ttwijang.cms.api.user.exception.UserNotFoundException;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.api.user.repository.UserRoleRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface UserService {

	Page<UserDto.Page> list(UserSearch userSearch, Pageable pageable);
	UserDto.Detail get(String userUid);
	void add(UserDto.JoinAdmin userDto);
	void update(User user, UserDto.Update userDto);
	void delete(User user);
	Boolean userIdCheck(String userId);
	void setEnabled(String userUid, boolean enable);
    List<UserDto.Detail> listByRole(String roleUid);
	void addShopAdmin(String userId, String userPassword, Integer shopIdx);
    Page<UserDto.manager> getManagerList(UserSearch userSearch, Pageable pageable);
    void addManager(UserDto.addManager dto);
	void updateManager(User user, UserDto.updateManager dto);
	Map<String, String> findLatLon(String address);
}

@Slf4j
@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final PostRepository postRepository;
	private final TokenStore tokenStore;
	private final ProductOrderGroupRepository productOrderGroupRepository;
	private final UserFcmTokenRepository userFcmTokenRepository;

	@Override
	public Page<UserDto.Page> list(UserSearch userSearch, Pageable pageable) {
		Page<UserDto.Page> userList = userRepository.findAll(userSearch.search(), pageable)
				.map(data -> UserMapper.INSTANCE.entityToPageDto(data));
		return userList;
	}

	@Override
	public UserDto.Detail get(String userUid) {
		User user = userRepository.findById(userUid).orElseThrow(() -> new NotFoundException(NotFound.USER));
		return UserMapper.INSTANCE.entityToDetailDto(user);
	}

	@Transactional
	@Override
	public void add(UserDto.JoinAdmin userDto) {
		User user = UserMapper.INSTANCE.joinDtoToEntity(userDto);
		user.setEnabled(true);
		user.setNotLocked(true);
		if (!userIdCheck(user.getUserId())) throw new UserDuplicateException("이미 사용중인 아이디 입니다.");

		Map<String, String> map = this.findLatLon(user.getAddress());
		if(map.containsKey("Lat")) user.setLat(map.get("Lat"));
		if(map.containsKey("Lon")) user.setLon(map.get("Lon"));
		
		userRepository.save(user);
		List<UserRole> userRoles = setRoles(user.getUid(), userDto.getRoles());
		userRoleRepository.saveAll(userRoles);
	}

	@Transactional
	@Override
	public void update(User user, UserDto.Update userDto) {
		String password = user.getUserPassword();
		UserMapper.INSTANCE.updateDtoToEntity(userDto, user);
		if (!StringUtils.hasText(userDto.getUserPassword())) user.setOriginalUserPassword(password);

		userRoleRepository.deleteByUserUid(user.getUid());

		Map<String, String> map = this.findLatLon(user.getAddress());
		if(map.containsKey("Lat")) user.setLat(map.get("Lat"));
		if(map.containsKey("Lon")) user.setLon(map.get("Lon"));

		userRepository.save(user);

		List<UserRole> userRoles = setRoles(user.getUid(), userDto.getRoles());
		
		userRoleRepository.saveAll(userRoles);
	}

	@Transactional
	@Override
	public void delete(User user) {
		// user.setWithdrawStatus(true);
		// userRepository.save(user);
		userRepository.delete(user);
		postRepository.deleteWithdrawUser(user.getUid());
		userFcmTokenRepository.deleteByUserUid(user.getUid());

		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("singha_oauth", user.getUserId());
		for (OAuth2AccessToken token : tokens) {
			tokenStore.removeAccessToken(token);
		}

		productOrderGroupRepository.withdrawUser(user.getUid());
	}

	@Transactional
	@Override
	public void setEnabled(String userUid, boolean enabled) {
		User user = userRepository.findById(userUid).orElseThrow(() -> new UserNotFoundException());
		user.setEnabled(enabled);
		userRepository.save(user);
	}

	@Override
	public Boolean userIdCheck(String userId) {
		return userRepository.findByUserId(userId).isEmpty();
	}

	private List<UserRole> setRoles(String userUid, List<String> roles) {
		List<UserRole> userRoles = new ArrayList<UserRole>();
		roles.stream().forEach(role -> {
			UserRole userRole = new UserRole();
			userRole.setUserUid(userUid);
			userRole.setRoleCode(role);
			userRoles.add(userRole);
		});
		return userRoles;
	}

	@Override
	public List<UserDto.Detail> listByRole(String roleUid) {
		List<User> entities = userRepository.findByRolesRoleCode(roleUid);
		List<UserDto.Detail> userList = UserMapper.INSTANCE.entityToDetailDto(entities);
		return userList;
	}

	@Override
	public void addShopAdmin(String userId, String userPassword, Integer shopIdx) {
		User user = new User();
		user.setUserId(userId);
		user.setUserPassword(userPassword);
		user.setJoinStatus(true);
		user.setEnabled(true);
		user.setNotLocked(true);
		user.setShopIdx(shopIdx);
		user.setPoint(0);
		if (!userIdCheck(user.getUserId())) throw new UserDuplicateException("이미 사용중인 아이디 입니다.");

		userRepository.save(user);
		List<UserRole> userRoles = setRoles(user.getUid(), Arrays.asList("ROLE_SHOP_ADMIN"));
		userRoleRepository.saveAll(userRoles);
	}

	@Override
	public Page<UserDto.manager> getManagerList(UserSearch userSearch, Pageable pageable) {
		userSearch.setManager(true);
		Page<UserDto.manager> managerList = userRepository.findAll(userSearch.search(), pageable)
				.map(data -> UserMapper.INSTANCE.entityToManagerDto(data));
		return managerList;
	}

	@Override
	public void addManager(UserDto.addManager dto) {
		User entity = UserMapper.INSTANCE.addManagerDtoToEntity(dto);
		entity.setJoinStatus(true);
		entity.setEnabled(true);
		entity.setNotLocked(true);

		if (!userIdCheck(entity.getUserId())) throw new UserDuplicateException("이미 사용중인 아이디 입니다.");
		entity = userRepository.save(entity);

		List<UserRole> userRoles = setRoles(entity.getUid(), Arrays.asList("ROLE_SHOP_ADMIN"));
		userRoleRepository.saveAll(userRoles);
	}

	@Override
	public void updateManager(User user, UserDto.updateManager dto) {
		String password = user.getUserPassword();
		UserMapper.INSTANCE.updateManagerDtoToEntity(dto, user);
		if (!StringUtils.hasText(dto.getUserPassword())) user.setOriginalUserPassword(password);
		userRepository.save(user);
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
        HttpEntity entity = new HttpEntity<>(headers); 
        
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