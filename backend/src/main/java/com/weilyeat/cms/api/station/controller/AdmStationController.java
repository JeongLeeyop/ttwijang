package com.weilyeat.cms.api.station.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.station.dto.AdmStationDto;
import com.weilyeat.cms.api.station.dto.search.AdmStationSearch;
import com.weilyeat.cms.api.station.service.AdmStationService;
import com.weilyeat.cms.entity.Station;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/station")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdmStationController {
    private final AdmStationService stationService;

    @GetMapping
    public ResponseEntity<Page<AdmStationDto.list>> list(
        @PageableDefault(direction = Direction.DESC, sort = { "createdDate" }) Pageable pageable,
        AdmStationSearch search
    ) {
        return ResponseEntity.ok(stationService.list(pageable, search));
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<AdmStationDto.list>> listAll() {
        return ResponseEntity.ok(stationService.listAll());
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmStationDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(stationService.detail(idx));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AdmStationDto.add addDto) {
        stationService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{idx}")
    public ResponseEntity update(@PathVariable("idx") Integer idx, @RequestBody AdmStationDto.update updateDto) {
        stationService.update(idx, updateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity delete(@PathVariable("idx") Station station) {
        stationService.delete(station);
        return ResponseEntity.ok().build();
    }
}
