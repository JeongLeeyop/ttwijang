# Match 도메인

## 엔티티: FutsalMatch

팀 간 친선경기 또는 게스트 모집 매치를 관리합니다.

## 매치 타입 (MatchType)

| 타입 | 설명 |
|------|------|
| `FRIENDLY` | 팀 vs 팀 친선경기 — 인원이 다 차면 자동으로 MATCHED 상태 전환 |
| `LEAGUE` | 리그 경기 (별도 League 도메인과 연계) |

## 매치 상태 흐름 (FutsalMatchStatus)

```
RECRUITING → MATCHED → COMPLETED
           ↘ CANCELLED
```

## 포맷별 최대 인원 (양 팀 합산)

```java
FORMAT_MAX_PLAYERS = {
    "4vs4" / "FOUR_VS_FOUR" → 8명,
    "5vs5" / "FIVE_VS_FIVE" → 10명,
    "6vs6" / "SIX_VS_SIX"  → 12명,
    "7vs7" / "SEVEN_VS_SEVEN" → 14명
}
```

## 매치 신청 (`applyMatch`)

1. 상태 `RECRUITING` 확인
2. 정원 초과 확인
3. 참가비(`fee`) > 0이면 `CashService.use()`로 자동 차감
4. FRIENDLY 타입: `guestTeamUid` 설정 + 정원 차면 `MATCHED`로 전환

**주의**: 현재 코드에서 권한 검증(팀 운영자 확인, 자기 팀 신청 방지)이 주석 처리되어 있음 — 의도적 미결 상태

## 매치 종료 (`completeMatch`)

- 호스트팀 운영자 또는 MANAGER 역할이 종료 가능
- 종료 후 `sendMannerRatingNotifications()` 자동 실행
  - 홈팀 멤버 → 상대팀 매너 평가 알림
  - 게스트 참여자 → 홈팀 매너 평가 알림
  - 상대팀 멤버 알림은 현재 주석 처리됨 (미결)

## 권한 확인 패턴

```java
// 운영자 또는 MANAGER 역할 허용 (MatchService.toDetailResponse 참고)
boolean isTeamOwner = hostTeam.getOwnerUid().equals(currentUserUid)
    || teamMemberRepository.findByTeamUidAndUserUid(...)
        .map(m -> m.getRole() == MANAGER || m.getRole() == OWNER)
        .orElse(false);
```

## 참여자 목록 구성

매치 상세 조회 시 참여자 = **팀 신청자(MatchApplication)** + **게스트 신청자(GuestApplication)**를 합산하여 반환합니다.

## 지역 조회 패턴

```java
// "시도 시군구" 문자열을 split(" ", 2)로 파싱
// regionCode 사용 시: sigungu만으로 조회하는 별도 메서드 사용
getMatchListBySigungu() / getMatchesByDateRangeBySigungu()
```

## 관련 도메인 의존성

- `CashService` — 참가비 차감
- `NotificationService` — 매너 평가 알림 발송
- `GuestRecruitment` / `GuestApplication` — 게스트 모집 (guest 도메인)
- `MannerRating` — 매너 점수 기록 여부 확인 (manner 도메인)
