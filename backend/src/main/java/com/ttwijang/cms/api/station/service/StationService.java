package com.ttwijang.cms.api.station.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.station.dto.StationDto;
import com.ttwijang.cms.api.station.dto.mapper.StationMapper;
import com.ttwijang.cms.api.station.dto.search.StationSearch;
import com.ttwijang.cms.api.station.repository.StationRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Station;

import lombok.AllArgsConstructor;

public interface StationService {
    List<StationDto.list> list();
    StationDto.detail detail(Integer idx);
}

@Service
@AllArgsConstructor
class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    @Override
    public List<StationDto.list> list() {
        StationSearch search = new StationSearch();
        List<Station> datas = (List) stationRepository.findAll(search.search());
        return datas.stream().map((e) -> StationMapper.INSTANCE.entityToListDto(e)).collect(Collectors.toList());
    }

    @Override
    public StationDto.detail detail(Integer idx) {
        Station entity = stationRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP));
        StationDto.detail dto = StationMapper.INSTANCE.entityToDetailDto(entity);
        return dto;
    }

}