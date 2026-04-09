# Guest 도메인

## 개요

팀이 경기에서 부족한 인원을 채우기 위해 **게스트(개인 사용자)를 모집**하는 기능.
Match 도메인의 FutsalMatch와 연계되지만, 독립적으로도 생성 가능합니다.

## 관련 엔티티

| 엔티티 | 역할 |
|--------|------|
| `GuestRecruitment` | 팀이 올리는 게스트 모집 공고 |
| `GuestApplication` | 개인 사용자의 게스트 신청 |

## 모집 상태 흐름 (RecruitmentStatus)

```
RECRUITING → COMPLETED (currentGuests >= maxGuests 시 자동)
           → CANCELLED  (팀 운영자가 취소)
```

## 신청 상태 (ApplicationStatus)

- 신청 즉시 **APPROVED** (별도 승인 플로우 없음)
- `processApplication()`으로 수동 승인/거절도 가능

## 핵심 비즈니스 규칙

- 모집 생성: **오늘부터 7일 이내** 경기만 가능 (`MAX_RECRUITMENT_DAYS = 7`)
- 중복 모집 방지: 같은 팀이 같은 매치(또는 같은 날짜)에 이미 RECRUITING/COMPLETED 모집 있으면 불가
- 자기 소속팀/운영팀의 게스트 모집에는 **신청 불가**
  - 팀 운영자(ownerUid) 확인
  - APPROVED 팀원 확인
- 참가비(`fee`) > 0 이면 신청 즉시 `CashService.use()`로 자동 차감
- `currentGuests` 도달 시 자동으로 `COMPLETED` 상태 전환

## currentGuests 관리

`GuestRecruitment.currentGuests`는 직접 증가시킵니다:
- `applyAsGuest()`: 즉시 +1
- `processApplication()`: approved 시 +1

실제 카운트와 다를 수 있어 `toListResponse()`에서 DB 재집계:
```java
int actualCurrentGuests = applicationRepository.countByRecruitmentUidAndStatus(...APPROVED);
```

## 참여자 목록 구성

게스트 모집 상세에서 참여자 = **팀 신청자(MatchApplication)** + **게스트 신청자(GuestApplication)** 합산.
Match 도메인과 동일한 방식.

## 도메인 의존성

- `CashService` — 참가비 차감
- `MatchService.getMaxPlayersByFormat()` — 매치 포맷별 최대 인원 계산
- `TeamRepository`, `TeamMemberRepository` — 소속팀 확인
- `MatchApplicationRepository` — 팀 참여자 명단 조회

## 지역 조회 패턴

Match 도메인과 동일:
- `"시도 시군구"` 문자열 split
- regionCode 사용 시 `*BySigungu()` 별도 메서드 호출
