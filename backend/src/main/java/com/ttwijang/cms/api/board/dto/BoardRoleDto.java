package com.ttwijang.cms.api.board.dto;

import javax.validation.constraints.NotBlank;

import com.ttwijang.cms.common.EnumField;
import com.ttwijang.cms.entity.BoardAction;

import lombok.Data;

public class BoardRoleDto {
    @Data
    public static class Detail {
        private String roleCode;
        private String action;
    }

    @Data
    public static class Save {
        @NotBlank(message = "권한 고유값을 입력하세요.")
        private String roleCode;
        
        /*
        @NotBlank(message = "기능을 입력하세요.")
        @EnumField(enumClass = BoardAction.class, message = "READ, WRITE, DELETE, REPLY, COMMENT 중 하나로 입력하세요.")
        private String action;
        */
    }
}
