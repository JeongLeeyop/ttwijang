package com.weilyeat.cms.api.challenge.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.challenge.dto.ChallengeDto;
import com.weilyeat.cms.api.challenge.dto.ChallengeFileDto;
import com.weilyeat.cms.api.challenge.dto.ChallengeSearch;
import com.weilyeat.cms.api.review.dto.ReviewDto;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.ChallengeFile;
import com.weilyeat.cms.entity.ChallengeFileType;
import com.weilyeat.cms.entity.ChallengeUser;
import com.weilyeat.cms.entity.Review;

@Mapper
public interface ChallengeMapper {
    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);
    Challenge detailDtoToEntity(ChallengeDto.detail deatilDto);
    Challenge updateDtoToEntity(ChallengeDto.update updateDto, @MappingTarget Challenge entity);
    
    ChallengeDto.detail entityToDetailNormal(Challenge Challenge);
    Challenge addDtoToEntityNormal(ChallengeDto.add addDto);
    
    default ChallengeDto.detail entityToDetail(Challenge Challenge) {
        List<ChallengeUser> challengeUserList =Challenge.getChallengeUserList();
        for(ChallengeUser challengeUser : challengeUserList) {

        }
        ChallengeDto.detail dto = entityToDetailNormal(Challenge);
        List<ChallengeFileDto.Detail> mainImageList = new ArrayList<ChallengeFileDto.Detail>();
        List<ChallengeFileDto.Detail> sampleImageList = new ArrayList<ChallengeFileDto.Detail>();
        
        List<ChallengeFile> fileList = Challenge.getFileList();
        
        if(fileList.size() > 0) {
            for(ChallengeFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(ChallengeFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(ChallengeFileMapper.INSTANCE.entityToDetail(file));
            }   
        };
        dto.setMainImageList(mainImageList);
        dto.setSampleImageList(sampleImageList);
        return dto;
    }

    default ChallengeDto.detail entityToDetailMyFlag(Challenge Challenge, ChallengeSearch search) {
        ChallengeDto.detail dto = entityToDetailNormal(Challenge);
        List<ChallengeFileDto.Detail> mainImageList = new ArrayList<ChallengeFileDto.Detail>();
        List<ChallengeFileDto.Detail> sampleImageList = new ArrayList<ChallengeFileDto.Detail>();
        
        if(search.isMyFlag()){
        List<ChallengeUser> challengeUserList =Challenge.getChallengeUserList();
        
        for(ChallengeUser challengeUser : challengeUserList) {
            // guard against orphaned challenge_user rows or null UIDs to avoid NPE
            if (challengeUser.getUser() == null) continue;
            if (Objects.equals(search.getUserUid(), challengeUser.getUserUid())) {
                dto.setUserJoinStatus(true);
                dto.setChallengeUser(ChallengeUserMapper.INSTANCE.entityToDetail(challengeUser));
                break; // user found, no need to continue
            }
        }
    }

        List<ChallengeFile> fileList = Challenge.getFileList();
        
        if(fileList.size() > 0) {
            for(ChallengeFile file : fileList) {
                if (file.getType().toString().equals("MAIN")) mainImageList.add(ChallengeFileMapper.INSTANCE.entityToDetail(file));
                else sampleImageList.add(ChallengeFileMapper.INSTANCE.entityToDetail(file));
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
}
