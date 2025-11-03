package com.ttwijang.cms.api.challenge.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.challenge.dto.ChallengeDto;
import com.ttwijang.cms.api.challenge.dto.ChallengeFileDto;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.ChallengeFile;
import com.ttwijang.cms.entity.ChallengeFileType;

@Mapper
public interface AdmChallengeMapper {
    AdmChallengeMapper INSTANCE = Mappers.getMapper(AdmChallengeMapper.class);
    Challenge detailDtoToEntity(ChallengeDto.detail deatilDto);
    
    
    Challenge updateDtoToEntityNormal(ChallengeDto.update updateDto, @MappingTarget Challenge entity);
    ChallengeDto.detail entityToDetailNormal(Challenge Challenge);
    Challenge addDtoToEntityNormal(ChallengeDto.add addDto);
    
    default ChallengeDto.detail entityToDetail(Challenge Challenge) {
        ChallengeDto.detail dto = entityToDetailNormal(Challenge);
        List<ChallengeFileDto.Detail> mainImageList = new ArrayList<ChallengeFileDto.Detail>();
        List<ChallengeFileDto.Detail> sampleImageList = new ArrayList<ChallengeFileDto.Detail>();
        
        List<ChallengeFile> fileList = Challenge.getFileList();
        
        if(fileList.size() > 0) {
            for(ChallengeFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(AdmChallengeFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(AdmChallengeFileMapper.INSTANCE.entityToDetail(file));
            }   
        };
        dto.setMainImageList(mainImageList);
        dto.setSampleImageList(sampleImageList);
        return dto;
    }

    default Challenge addDtoToEntity(ChallengeDto.add dto) {
        List<ChallengeFile> fileList = new ArrayList<ChallengeFile>();
        dto.getMainImageList().forEach(m ->  {
            ChallengeFile file = new ChallengeFile();
            file.setFileUid(m.getFileUid());
            file.setType(ChallengeFileType.MAIN);
            fileList.add(file);
        });
        dto.getSampleImageList().forEach(e ->  {
            ChallengeFile file = new ChallengeFile();
            file.setFileUid(e.getFileUid());
            file.setType(ChallengeFileType.SAMPLE);
            fileList.add(file);
        });
        Challenge entity = addDtoToEntityNormal(dto);
        entity.setFileList(fileList);
        return entity;
    }
    
    default Challenge updateDtoToEntity(ChallengeDto.update dto, @MappingTarget Challenge targetEntity) {
        List<ChallengeFile> fileList = targetEntity.getFileList();
        fileList.clear();
        dto.getMainImageList().forEach(m ->  {
            ChallengeFile file = new ChallengeFile();
            file.setFileUid(m.getFileUid());
            file.setType(ChallengeFileType.MAIN);
            fileList.add(file);
        });
        dto.getSampleImageList().forEach(e ->  {
            ChallengeFile file = new ChallengeFile();
            file.setFileUid(e.getFileUid());
            file.setType(ChallengeFileType.SAMPLE);
            fileList.add(file);
        });
        targetEntity.setFileList(fileList);
        Challenge entity = updateDtoToEntityNormal(dto, targetEntity);
        return entity;
    }
}
