package com.weilyeat.cms.api.bank.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.bank.dto.AdmBankDto;
import com.weilyeat.cms.api.bank.dto.mapper.AdmBankMapper;
import com.weilyeat.cms.api.bank.repository.BankRepository;

import lombok.AllArgsConstructor;

public interface AdmBankService {
    List<AdmBankDto.list> list();
}

@Service
@AllArgsConstructor
class AdmBankServiceImpl implements AdmBankService {
    private final BankRepository bankRepository;

    @Override
    public List<AdmBankDto.list> list() {
        return bankRepository.findAll()
            .stream().map(e -> AdmBankMapper.INSTANCE.entityToListDto(e))
            .collect(Collectors.toList());
    }

    
}
