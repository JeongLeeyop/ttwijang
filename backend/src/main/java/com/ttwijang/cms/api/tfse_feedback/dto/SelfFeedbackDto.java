package com.ttwijang.cms.api.tfse_feedback.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ttwijang.cms.api.user.dto.UserDto;

import lombok.Data;

public class SelfFeedbackDto {
    
    @Data
    public static class list {
        private Long idx;
        private String userUid;
        private LocalDate selfFeedbackDate;
        private String question1;
        private String question2;
        private String question3;
        private LocalDateTime createDate;
        private String writer;
        private UserDto.Simple user;;
    }

    @Data
    public static class detail {
       private Long idx;
       private String userUid;
       private LocalDate selfFeedbackDate;
       private String question1;
       private String question2;
       private String question3;
       private LocalDateTime createDate;
       private String writer;
       private UserDto.Simple user;
    }

    @Data
    public static class update {
       private Long idx;
       private String userUid;
       private LocalDate selfFeedbackDate;
       private String question1;
       private String question2;
       private String question3;
       private LocalDateTime createDate;
    }

    @Data
    public static class add {
       private LocalDate selfFeedbackDate;
       private String question1;
       private String question2;
       private String question3;
    }

}
