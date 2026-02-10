package com.ttwijang.cms.dto.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 알리고 SMS API 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알리고 SMS API 응답")
public class SmsResponse {
    
    @JsonProperty("result_code")
    @Schema(description = "결과 코드 (1: 성공, 음수: 실패)")
    private Integer resultCode;
    
    @Schema(description = "결과 메시지")
    private String message;
    
    @JsonProperty("msg_id")
    @Schema(description = "메시지 고유 ID")
    private String msgId;
    
    @JsonProperty("success_cnt")
    @Schema(description = "성공 건수")
    private Integer successCnt;
    
    @JsonProperty("error_cnt")
    @Schema(description = "실패 건수")
    private Integer errorCnt;
    
    @JsonProperty("msg_type")
    @Schema(description = "메시지 타입 (SMS/LMS/MMS)")
    private String msgType;
}
