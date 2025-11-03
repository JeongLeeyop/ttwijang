package com.ttwijang.cms.api.mission.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission.dto.MissionDto;
import com.ttwijang.cms.api.mission.dto.MissionFileDto;
import com.ttwijang.cms.api.mission.dto.MissionSearch;
import com.ttwijang.cms.api.mission.dto.MissionUserDto;
import com.ttwijang.cms.api.review.dto.ReviewDto;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionFile;
import com.ttwijang.cms.entity.MissionFileType;
import com.ttwijang.cms.entity.MissionUser;
import com.ttwijang.cms.entity.Review;

@Mapper
public interface MissionMapper {
    MissionMapper INSTANCE = Mappers.getMapper(MissionMapper.class);
    Mission detailDtoToEntity(MissionDto.detail deatilDto);
    Mission updateDtoToEntity(MissionDto.update updateDto, @MappingTarget Mission entity);
    
    MissionDto.detail entityToDetailNormal(Mission Mission);
    Mission addDtoToEntityNormal(MissionDto.add addDto);
    
    default MissionDto.detail entityToDetail(Mission Mission) {
        List<MissionUser> missionUserList = Mission.getMissionUserList();
        MissionDto.detail dto = entityToDetailNormal(Mission);
        for(MissionUser missionUser : missionUserList) {
            dto.getMissionUserList().get(0).setActualName(missionUser.getUser().getActualName());
        }

        List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>();
        List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>();
        
        List<MissionFile> fileList = Mission.getFileList();
        
        if(fileList.size() > 0) {
            for(MissionFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(MissionFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(MissionFileMapper.INSTANCE.entityToDetail(file));
            }   
        };
        dto.setMainImageList(mainImageList);
        dto.setSampleImageList(sampleImageList);
        return dto;
    }

    default MissionDto.detail entityToDetailMyFlag(Mission Mission, MissionSearch search) {
        MissionDto.detail dto = entityToDetailNormal(Mission);
        List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>();
        List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>();
        
        if(search.isMyFlag()){
        List<MissionUser> missionUserList = Mission.getMissionUserList();
            for(MissionUser missionUser : missionUserList) {
                if(missionUser.getUserUid().equals(search.getUserUid())) {
                dto.setUserJoinStatus(true);
                 dto.setMissionUser(MissionUserMapper.INSTANCE.entityToDetail(missionUser));   
                }
            }
        }

        List<MissionFile> fileList = Mission.getFileList();
        
        if(fileList.size() > 0) {
            for(MissionFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(MissionFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(MissionFileMapper.INSTANCE.entityToDetail(file));
            }   
        };
        dto.setMainImageList(mainImageList);
        dto.setSampleImageList(sampleImageList);
        return dto;
    }

    default Mission addDtoToEntity(MissionDto.add dto) {
        List<MissionFile> fileList = new ArrayList<MissionFile>();
        dto.getMainImageList().forEach(m ->  {
            MissionFile file = new MissionFile();
            file.setFileUid(m.getFileUid());
            file.setType(MissionFileType.MAIN);
            fileList.add(file);
        });
        dto.getSampleImageList().forEach(e ->  {
            MissionFile file = new MissionFile();
            file.setFileUid(e.getFileUid());
            file.setType(MissionFileType.SAMPLE);
            fileList.add(file);
        });
        Mission entity = addDtoToEntityNormal(dto);
        entity.setFileList(fileList);
        return entity;
    }
}
