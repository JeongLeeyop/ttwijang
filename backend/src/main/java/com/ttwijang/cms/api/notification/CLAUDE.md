# Notification 도메인

## 개요

사용자에게 인앱 알림을 저장하고 조회하는 단순 CRUD 도메인.
**DB 저장 방식** (FCM push 연동은 별도 — `UserFcmTokenService` 참고).

## 알림 타입 (NotificationType)

현재 확인된 타입:
- `MATCH` — 매치 관련 알림 (경기 종료 후 매너 평가 요청 등)

## createNotification() 파라미터

```java
createNotification(
    userUid,        // 수신자
    type,           // NotificationType
    title,          // 알림 제목
    content,        // 알림 내용
    referenceUid,   // 연관 데이터 UID (matchUid 등)
    referenceType,  // 연관 데이터 타입 ("MATCH" 등 문자열)
    actionUrl       // 프론트엔드 이동 경로 (e.g. "/match-detail/{uid}?type=match")
)
```

## 주요 서비스 메서드

| 메서드 | 설명 |
|--------|------|
| `createNotification()` | 알림 생성 (다른 도메인 서비스에서 직접 호출) |
| `getNotifications()` | 사용자 알림 목록 (생성일 내림차순 페이지네이션) |
| `getUnreadCount()` | 읽지 않은 알림 개수 |
| `markAsRead()` | 단건 읽음 처리 (`readDate` 기록) |
| `markAllAsRead()` | 전체 읽음 처리 |
| `deleteNotification()` | 알림 삭제 (본인 소유 확인 후) |

## 현재 발송 시점

- **매치 종료** (`MatchService.completeMatch()`) → 매너 평가 요청 알림
  - 홈팀 멤버 전원에게 발송
  - 게스트 참여자에게 발송
  - 상대팀 멤버 발송은 주석 처리됨 (미결)

## 미완성 기능 (TODO)

- [ ] 상대팀 멤버에게 매너 평가 알림 발송 (`MatchService` 주석 처리된 코드 활성화)
- [ ] FCM 푸시 연동 (`UserFcmTokenService` 존재하나 NotificationService와 미연결)
- [ ] 팀 가입 신청/승인/거절 알림
- [ ] 게스트 신청 알림

## 주의사항

`createNotification()`은 `@Transactional` 없이 직접 save — 호출하는 쪽의 트랜잭션에 참여합니다.
