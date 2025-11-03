package com.weilyeat.cms.fcm.firebase;

import com.google.firebase.messaging.*;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    @Autowired
    private PushAlarmService pushAlarmService;

    /* public void sendMessage(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(data, request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
    } */

    /* public void sendMessageWithoutData(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithoutData(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    } */
 
    public void sendMessageToToken(PushNotificationRequest request) 
            throws InterruptedException, ExecutionException, FirebaseMessagingException {

        if(request.getTokenList()==null){
            Message message = getPreconfiguredMessageToToken(request);
            String response = sendAndGetResponse(message);
            logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
        } else{
            MulticastMessage multiMessage = getPreconfiguredMulticastMessageBuilder(request);
            BatchResponse response = sendAndGetResponse(multiMessage);
            logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
        }
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private BatchResponse sendAndGetResponse(MulticastMessage message) throws InterruptedException, ExecutionException, FirebaseMessagingException {
        return FirebaseMessaging.getInstance().sendMulticast(message);
    }



   /*  private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }
 */


     private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    } 

    /* private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    } */

    /* private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic())
                .build();
    } */

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        Message.Builder builder = Message.builder()
                .setNotification(Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getMessage())
                .build());
                
        if (request.getAction() != null) {
            builder.putData("click_action", request.getAction());
        }
        // link 데이터는 action과 중복되므로 제거
        // if (request.getLink() != null) {
        //     builder.putData("link", request.getLink());
        // }
        
        return builder;
    }

    private MulticastMessage getPreconfiguredMulticastMessageBuilder(PushNotificationRequest request) {
        MulticastMessage.Builder builder = MulticastMessage.builder()
        .setNotification(Notification.builder()
            .setTitle(request.getTitle())
            .setBody(request.getMessage())
            .build())
        .addAllTokens(request.getTokenList());
        
        if (request.getAction() != null) {
            builder.putData("click_action", request.getAction());
        }
        // link 데이터는 action과 중복되므로 제거
        // if (request.getLink() != null) {
        //     builder.putData("link", request.getLink());
        // }
        
        return builder.build();
    }

    public void sendMissionAlarm(String userUid, String title, String message) {
        // 입력 파라미터 검증
        if (userUid == null || userUid.trim().isEmpty()) {
            logger.error("Invalid userUid provided for mission alarm");
            return;
        }
        
        if (title == null || title.trim().isEmpty()) {
            title = "미션 알림";
        }
        
        if (message == null || message.trim().isEmpty()) {
            message = "미션을 수행할 시간입니다!";
        }
        
        try {
            // 사용자의 FCM 토큰을 조회
            List<UserFcmToken> userTokens = userFcmTokenRepository.findByUserUid(userUid);
            
            if (userTokens != null && !userTokens.isEmpty()) {
                // 유효한 토큰만 필터링
                List<String> validTokens = userTokens.stream()
                        .map(UserFcmToken::getToken)
                        .filter(token -> token != null && !token.trim().isEmpty())
                        .collect(java.util.stream.Collectors.toList());
                
                if (validTokens.isEmpty()) {
                    logger.warn("No valid FCM tokens found for user: " + userUid);
                    saveFallbackPushAlarm(userUid, "미션 알림 설정 필요", 
                        "미션 알림을 받기 위해 모바일 앱에 다시 로그인해서 알림을 재설정 해주세요");
                    return;
                }
                
                PushNotificationRequest request = new PushNotificationRequest();
                request.setTitle(title);
                request.setMessage(message);
                request.setAction("mission?fromPush=true&showDrawer=true");
                
                // 여러 토큰이 있는 경우 멀티캐스트로 전송
                if (validTokens.size() > 1) {
                    request.setTokenList(validTokens);
                } else {
                    request.setToken(validTokens.get(0));
                }
                
                sendMessageToToken(request);
                logger.info("Mission alarm sent to user: " + userUid + ", Title: " + title);
            } else {
                logger.warn("No FCM tokens found for user: " + userUid + ", unable to send mission alarm");
                saveFallbackPushAlarm(userUid, "미션 알림 설정 필요", 
                    "미션 알림을 받기 위해 모바일 앱에 다시 로그인해서 알림을 재설정 해주세요");
            }
            
        } catch (Exception e) {
            logger.error("Error sending mission alarm to user: " + userUid, e);
            handleFCMError(userUid, e);
        }
    }
    
    private void saveFallbackPushAlarm(String userUid, String title, String content) {
        try {
            PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
            pushAlarmDto.setUserUid(userUid);
            pushAlarmDto.setTitle(title);
            pushAlarmDto.setContent(content);
            pushAlarmDto.setLink("/login");
            pushAlarmDto.setUserUidList(null);
            pushAlarmService.add(pushAlarmDto);
        } catch (Exception alarmException) {
            logger.error("Failed to save fallback push alarm for user: " + userUid, alarmException);
        }
    }
    
    private void handleFCMError(String userUid, Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage != null && (errorMessage.contains("UNREGISTERED") || 
                                    errorMessage.contains("Requested entity was not found") ||
                                    errorMessage.contains("NOT_FOUND") ||
                                    errorMessage.contains("INVALID_REGISTRATION") ||
                                    errorMessage.contains("SENDER_ID_MISMATCH"))) {
            logger.warn("Invalid FCM token detected for user: " + userUid + ", removing token and saving fallback alarm");
            
            // 무효한 토큰 제거
            try {
                userFcmTokenRepository.deleteByUserUid(userUid);
            } catch (Exception deleteException) {
                logger.error("Failed to delete invalid FCM token for user: " + userUid, deleteException);
            }
            
            // 재로그인 안내 알람 저장
            saveFallbackPushAlarm(userUid, "미션 알림 설정 필요", 
                "미션 알림을 받기 위해 모바일 앱에 다시 로그인해서 알림을 재설정 해주세요");
        }
    }
}
