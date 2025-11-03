package com.weilyeat.cms.fcm.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationRequest {

    private String title;
    private String message;
    private String topic;
    private String token;
    private String action;
    private String link;
    private List<String> tokenList;

    public PushNotificationRequest(String title, String messageBody, String action, String token, List<String> tokenList) {
        this.title = title;
        this.message = messageBody;
        this.action = action;
        this.token = token;
        this.tokenList = tokenList;
    }

}
