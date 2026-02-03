package com.ttwijang.cms.api.station.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ttwijang.cms.api.station.dto.AdmStationDto;
import com.ttwijang.cms.api.station.dto.mapper.AdmStationMapper;
import com.ttwijang.cms.api.station.dto.search.AdmStationSearch;
import com.ttwijang.cms.api.station.repository.StationRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.api.user.service.UserService;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.Station;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface AdmStationService {
    Page<AdmStationDto.list> list(Pageable pageable, AdmStationSearch search);
    AdmStationDto.detail detail(Integer idx);
    void add(AdmStationDto.add addDto);
    void update(Integer idx, AdmStationDto.update updateDto);
    void setLanLon(Station entity); // 위도,경도 적용
    List<AdmStationDto.list> listAll();
    void delete(Station entity);
}

@Service
@AllArgsConstructor
@Slf4j
class AdmStationServiceImpl implements AdmStationService {
    private final StationRepository stationRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Page<AdmStationDto.list> list(Pageable pageable, AdmStationSearch search) {
        return stationRepository.findAll(search.search(), pageable).map(e -> AdmStationMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public AdmStationDto.detail detail(Integer idx) {
        Station entity = stationRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
        AdmStationDto.detail dto = AdmStationMapper.INSTANCE.entityToDetailDto(entity);
        return dto;
    }

    @Transactional
    @Override
    public void add(AdmStationDto.add addDto) {
        Station entity = AdmStationMapper.INSTANCE.addDtoToEntity(addDto);
        if (StringUtils.hasText(entity.getAddress())) setLanLon(entity);
        stationRepository.save(entity);
    }

    @Transactional
    @Override
    public void update(Integer idx, AdmStationDto.update updateDto) {
        Station entity = stationRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
        entity = AdmStationMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        if (StringUtils.hasText(entity.getAddress())) setLanLon(entity);
        stationRepository.save(entity);
    }

    @Override
    public void setLanLon(Station station) {
        final String HOST = "http://dapi.kakao.com";
        URI targetUrl = UriComponentsBuilder.fromUriString(HOST+"/v2/local/search/address.json")
            .queryParam("query", station.getAddress())
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
            if (jsonArray.length() > 0) {
                JSONObject addressData = jsonArray.getJSONObject(0);
                if (addressData.has("y")) station.setLat(addressData.getString("y"));
                if (addressData.has("x")) station.setLon(addressData.getString("x"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public List<AdmStationDto.list> listAll() {
        return stationRepository.findByUseStatus(false)
            .stream().map(e -> AdmStationMapper.INSTANCE.entityToListDto(e))
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Station station) {
		stationRepository.delete(station);
    }

}
