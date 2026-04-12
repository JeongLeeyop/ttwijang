package com.ttwijang.cms.api.faq.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.faq.dto.FaqDto;
import com.ttwijang.cms.api.faq.repository.FaqRepository;
import com.ttwijang.cms.entity.Faq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional(readOnly = true)
    public List<FaqDto.Response> getFaqList() {
        return faqRepository.findAllByOrderByDisplayOrderAscCreatedDateAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public FaqDto.Response createFaq(FaqDto.CreateRequest request) {
        Faq faq = Faq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .category(request.getCategory())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
        return toResponse(faqRepository.save(faq));
    }

    @Transactional
    public FaqDto.Response updateFaq(String uid, FaqDto.UpdateRequest request) {
        Faq faq = faqRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FAQ입니다."));
        if (request.getQuestion() != null) faq.setQuestion(request.getQuestion());
        if (request.getAnswer() != null) faq.setAnswer(request.getAnswer());
        if (request.getCategory() != null) faq.setCategory(request.getCategory());
        if (request.getDisplayOrder() != null) faq.setDisplayOrder(request.getDisplayOrder());
        return toResponse(faqRepository.save(faq));
    }

    @Transactional
    public void deleteFaq(String uid) {
        Faq faq = faqRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FAQ입니다."));
        faqRepository.delete(faq);
    }

    private FaqDto.Response toResponse(Faq faq) {
        return FaqDto.Response.builder()
                .uid(faq.getUid())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .category(faq.getCategory())
                .displayOrder(faq.getDisplayOrder())
                .createdDate(faq.getCreatedDate())
                .build();
    }
}
