package com.weilyeat.cms.api.settle.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.settle.dto.AdmSettleApplyDto;
import com.weilyeat.cms.api.settle.dto.search.AdmSettleApplySearch;
import com.weilyeat.cms.api.settle.service.AdmSettleApplyService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/settle/apply")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdmSettleApplyController {
    private final AdmSettleApplyService admSettleApplyService;

    @GetMapping
    public ResponseEntity<Page<AdmSettleApplyDto.list>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"applyDate", "idx" }) Pageable pageable,
        AdmSettleApplySearch search
    ) {
        return ResponseEntity.ok(admSettleApplyService.list(pageable, search));
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmSettleApplyDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(admSettleApplyService.detail(idx));
    }

    @PostMapping("approval")
    public ResponseEntity approval(@RequestBody AdmSettleApplyDto.approval dto) {
        admSettleApplyService.approval(dto);
        return ResponseEntity.ok().build();
    }
    
}
