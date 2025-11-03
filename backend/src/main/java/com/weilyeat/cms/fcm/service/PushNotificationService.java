package com.weilyeat.cms.fcm.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.weilyeat.cms.fcm.firebase.FCMService;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    private Map<String, String> defaults = new HashMap<String, String>();

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private FCMService fcmService;

    public PushNotificationService() {
        this.defaults.put("topic", "weilyeat");
        this.defaults.put("title", "Common topic - Hello");
        this.defaults.put("message", "Sending test message \uD83D\uDE42");
        this.defaults.put("token", "ss22t03wz208eg:APA2idkkow223FE_0v5yHxqCLTyxAQafj6nWaqi4QzwZTW004q1PUux63UsFN");
        this.defaults.put("payloadMessageId", "123");
        this.defaults.put("payloadData", "Hello. This is payload content.");
    }

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    // 토픽 사용안함
    
    /* public void sendPushNotification(String title, String content, String link) {
        try {
            fcmService.sendMessageWithoutData(getSamplePushNotificationRequest(title, content, link));
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    } */

   /*  public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    } */

   /*  public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    } */


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException | FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
    }

/* 
    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", defaults.get("payloadMessageId"));
        pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
        return pushData;
    }


    private PushNotificationRequest getSamplePushNotificationRequest(String title, String content, String link) {
        PushNotificationRequest request = new PushNotificationRequest(title, content, defaults.get("topic"));
        request.setAction(link);
        return request;
    } */
}
