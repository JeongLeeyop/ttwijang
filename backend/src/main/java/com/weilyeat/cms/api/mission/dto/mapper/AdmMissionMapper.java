package com.ttwijang.cms.api.mission.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission.dto.MissionDto;
import com.ttwijang.cms.api.mission.dto.MissionFileDto;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionFile;
import com.ttwijang.cms.entity.MissionFileType;

@Mapper
public interface AdmMissionMapper {
    AdmMissionMapper INSTANCE = Mappers.getMapper(AdmMissionMapper.class);
    Mission detailDtoToEntity(MissionDto.detail deatilDto);
    
    
    Mission updateDtoToEntityNormal(MissionDto.update updateDto, @MappingTarget Mission entity);
    MissionDto.detail entityToDetailNormal(Mission Mission);
    Mission addDtoToEntityNormal(MissionDto.add addDto);
    
    default MissionDto.detail entityToDetail(Mission Mission) {
        MissionDto.detail dto = entityToDetailNormal(Mission);
        List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>();
        List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>();
        
        List<MissionFile> fileList = Mission.getFileList();
        
        if(fileList.size() > 0) {
            for(MissionFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(AdmMissionFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(AdmMissionFileMapper.INSTANCE.entityToDetail(file));
            }   
        };
        dto.setMainImageList(mainImageList);
        dto.setSampleImageList(sampleImageList);
        return dto;
    }

    default Mission addDtoToEntity(MissionDto.add dto) {
        Mission entity = addDtoToEntityNormal(dto);
        
        List<MissionFile> fileList = new ArrayList<MissionFile>();
        int order = 0;
        for (MissionFileDto.Save m : dto.getMainImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(m.getFileUid());
            file.setType(MissionFileType.MAIN);
            file.setViewOrder(order++);
            file.setMissionUid(entity.getUid());
            fileList.add(file);
        }
        for (MissionFileDto.Save e : dto.getSampleImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(e.getFileUid());
            file.setType(MissionFileType.SAMPLE);
            file.setViewOrder(order++);
            file.setMissionUid(entity.getUid());
            fileList.add(file);
        }
        entity.setFileList(fileList);
        return entity;
    }
    
    default Mission updateDtoToEntity(MissionDto.update dto, @MappingTarget Mission targetEntity) {
        List<MissionFile> fileList = targetEntity.getFileList();
        fileList.clear();
        
        int order = 0;
        for (MissionFileDto.Save m : dto.getMainImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(m.getFileUid());
            file.setType(MissionFileType.MAIN);
            file.setViewOrder(order++);
            file.setMissionUid(targetEntity.getUid());
            fileList.add(file);
        }
        for (MissionFileDto.Save e : dto.getSampleImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(e.getFileUid());
            file.setType(MissionFileType.SAMPLE);
            file.setViewOrder(order++);
            file.setMissionUid(targetEntity.getUid());
            fileList.add(file);
        }
        targetEntity.setFileList(fileList);
        Mission entity = updateDtoToEntityNormal(dto, targetEntity);
        return entity;
    }
}
