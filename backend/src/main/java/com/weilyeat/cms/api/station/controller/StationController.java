package com.ttwijang.cms.api.station.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.station.dto.StationDto;
import com.ttwijang.cms.api.station.service.StationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client/station")
public class StationController {
    private final StationService stationService;
    @GetMapping
    public ResponseEntity<List<StationDto.list>> list() {
        return ResponseEntity.ok(stationService.list());
    }
}
