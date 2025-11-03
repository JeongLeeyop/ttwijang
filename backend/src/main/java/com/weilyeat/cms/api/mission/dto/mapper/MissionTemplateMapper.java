package com.weilyeat.cms.api.mission.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission.dto.MissionTemplateDto;
import com.weilyeat.cms.api.mission.dto.MissionFileDto;
import com.weilyeat.cms.entity.MissionTemplate;
import com.weilyeat.cms.entity.MissionFile;
import com.weilyeat.cms.entity.MissionFileType;

@Mapper
public interface MissionTemplateMapper {
    MissionTemplateMapper INSTANCE = Mappers.getMapper(MissionTemplateMapper.class);
    MissionTemplate detailDtoToEntity(MissionTemplateDto.detail deatilDto);
    
    
    MissionTemplate updateDtoToEntityNormal(MissionTemplateDto.update updateDto, @MappingTarget MissionTemplate entity);
    MissionTemplateDto.detail entityToDetailNormal(MissionTemplate Mission);
    MissionTemplate addDtoToEntityNormal(MissionTemplateDto.add addDto);
    
    default MissionTemplateDto.detail entityToDetail(MissionTemplate Mission) {
        MissionTemplateDto.detail dto = entityToDetailNormal(Mission);
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

    default MissionTemplate addDtoToEntity(MissionTemplateDto.add dto) {
        MissionTemplate entity = addDtoToEntityNormal(dto);
        
        List<MissionFile> fileList = new ArrayList<MissionFile>();
        int order = 0;
        for (MissionFileDto.Detail m : dto.getMainImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(m.getFileUid());
            file.setType(MissionFileType.MAIN);
            file.setViewOrder(order++);
            fileList.add(file);
        }
        for (MissionFileDto.Detail e : dto.getSampleImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(e.getFileUid());
            file.setType(MissionFileType.SAMPLE);
            file.setViewOrder(order++);
            fileList.add(file);
        }
        entity.setFileList(fileList);
        return entity;
    }
    
    default MissionTemplate updateDtoToEntity(MissionTemplateDto.update dto, @MappingTarget MissionTemplate targetEntity) {
        List<MissionFile> fileList = targetEntity.getFileList();
        fileList.clear();
        
        int order = 0;
        for (MissionFileDto.Detail m : dto.getMainImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(m.getFileUid());
            file.setType(MissionFileType.MAIN);
            file.setViewOrder(order++);
            file.setTemplateUid(targetEntity.getUid());
            fileList.add(file);
        }
        for (MissionFileDto.Detail e : dto.getSampleImageList()) {
            MissionFile file = new MissionFile();
            file.setFileUid(e.getFileUid());
            file.setType(MissionFileType.SAMPLE);
            file.setViewOrder(order++);
            file.setTemplateUid(targetEntity.getUid());
            fileList.add(file);
        }
        targetEntity.setFileList(fileList);
        MissionTemplate entity = updateDtoToEntityNormal(dto, targetEntity);
        return entity;
    }
}
