package com.weilyeat.cms.api.settle.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.settle.dto.AdmSettleApplyDto;
import com.weilyeat.cms.api.settle.dto.mapper.AdmSettleApplyMapper;
import com.weilyeat.cms.api.settle.dto.search.AdmSettleApplySearch;
import com.weilyeat.cms.api.settle.repository.SettleApplyRepository;
import com.weilyeat.cms.api.settle.repository.query.SettleApplyItemQuery;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.SettleApply;

import lombok.AllArgsConstructor;

public interface AdmSettleApplyService {
    Page<AdmSettleApplyDto.list> list(Pageable pageable, AdmSettleApplySearch search);
    AdmSettleApplyDto.detail detail(Integer idx);
    void approval(AdmSettleApplyDto.approval dto);
}

@Service
@AllArgsConstructor
class AdmSettleApplyServiceImpl implements AdmSettleApplyService {
    private final SettleApplyRepository settleApplyRepository;
    private final SettleApplyItemQuery settleApplyItemQuery;

    @Override
    public Page<AdmSettleApplyDto.list> list(Pageable pageable, AdmSettleApplySearch search) {
        return settleApplyRepository.findAll(search.search(), pageable).map(e -> AdmSettleApplyMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public AdmSettleApplyDto.detail detail(Integer idx) {
        SettleApply entity = settleApplyRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SETTLE_APPLY));
        
        AdmSettleApplyDto.detail data = AdmSettleApplyMapper.INSTANCE.entityToDetailDto(entity);
        data.setItems(settleApplyItemQuery.list(idx));
        return data;
    }

    @Override
    public void approval(AdmSettleApplyDto.approval dto) {
        List<SettleApply> applyList = settleApplyRepository.findAllById(dto.getApplyIdxList());
        
        for (SettleApply apply : applyList) {
            if (apply.isCancelStatus()) throw new BadRequestException(BadRequest.CANCEL_SETTLE_APPLY);
            if (apply.isApprovalStatus()) throw new BadRequestException(BadRequest.ALREADY_APPROVAL_SETTLE_APPLY);
    
            apply.setApprovalStatus(true);
            apply.setApprovalDate(LocalDateTime.now());
        }

        settleApplyRepository.saveAll(applyList);
    }

    
}
