# League 도메인

## ⚠️ 핵심 개념 구분 — "리그 참가" vs "리그 경기 참가"

두 개념은 **완전히 다른 행위**이며 절대 혼동하면 안 됩니다.

### 1) 리그 참가 (League Participation)
- **주체**: 팀 OWNER
- **진입 경로**: 사용자 홈 배너 → 리그 참가 페이지
- **과금**: 최고관리자가 리그관리에서 설정한 `participationPoints` **유료 차감**
- **저장**: `LeagueTeam` 레코드 생성 — 해당 팀이 리그에 등록
- **API**: `POST /api/league/{leagueUid}/apply` (`LeagueService.applyToLeague`)

### 2) 리그 경기 참가 (League Match Participation)
- **주체**: 팀 회원 — 해당 LeagueMatch의 **homeTeam 또는 awayTeam의 APPROVED 멤버**만 신청 가능
- **진입 경로**: 매치 상세 페이지 (`/match-detail/{matchUid}?type=league&leagueUid=...`)
- **과금**: **무조건 무료** — 금액 설정 불가, 캐시 차감 없음
- **저장**: `LeagueMatchApplication` 레코드 — `status=APPROVED`로 즉시 승인
- **신청 시점**: LeagueMatch 상태 = `SCHEDULED` & `matchDate + matchTime`이 현재보다 미래
- **취소**: `match_config.cancelDaysBeforeMatch` N일 전까지 (매치 취소 정책과 공용)
- **중복 신청 방지**: 기존 APPROVED가 있으면 거부, CANCELLED가 있으면 같은 레코드를 APPROVED로 복원
- **API**:
  - `POST /api/league/match/{matchUid}/apply` — 신청
  - `POST /api/league/match/{matchUid}/cancel-application` — 취소
  - `GET /api/league/match/my-applications` — 내 신청 내역

> **마이페이지 > 신청 내역 > 리그 탭**은 "리그 참가"가 아닌 "**리그 경기 참가**" 내역을 표시합니다.

## 비즈니스 규칙

| 규칙 | 내용 |
|------|------|
| BR-04 | 지역별 리그 전환 지원 — sido/sigungu 복합 필터 또는 sigungu 단독 조회 |
| BR-11 | 리그 생성/수정/삭제 — **최고관리자 전용** |
| BR-12 | 리그 팀 추가/제거 — **최고관리자 전용** |
| BR-13 | 리그 참가는 유료 (`participationPoints`), 리그 경기 참가는 **무조건 무료** |

## 리그 상태 (LeagueStatus)

```
RECRUITING → IN_PROGRESS → COMPLETED
```

## 관련 엔티티

| 엔티티 | 역할 |
|--------|------|
| `League` | 리그 기본 정보 (기간, 지역, 최대 팀 수, `participationPoints`=리그 참가비) |
| `LeagueTeam` | **리그 참가** 기록 — 참가팀 + 전적 (wins/draws/losses/points/goalDifference) |
| `LeagueMatch` | 리그 경기 일정 및 결과 |
| `LeagueMatchApplication` | (신설 예정) **리그 경기 참가** 기록 — 팀 회원 개인이 특정 LeagueMatch에 참가 신청 |

## 순위 및 전적 자동 업데이트

`updateMatchResult()` 호출 시 `updateTeamStats()`가 자동으로 실행됩니다:
- 승: +3점, 무: +1점, 패: 0점
- 득실차(`goalDifference`) = `goalsFor - goalsAgainst`
- `ranking` 필드는 자동 갱신되지 않음 — `findByLeagueUidOrderByRanking()`의 정렬 기준

## 지역 조회 패턴

```java
// 시도 + 시군구 복합
findByRegionAndStatus(sido, sigungu, status, pageable)

// 시군구만 (regionCode 사용 시 — 도 없이)
findBySigunguAndStatus(sigungu, status, pageable)
```

## currentTeams 관리

`League.currentTeams`는 팀 추가/제거 시 직접 업데이트합니다 (`addTeamToLeague`, `removeTeamFromLeague`).
최대 팀 수 초과 시 예외 발생.

## 주요 서비스 메서드

| 메서드 | 설명 |
|--------|------|
| `createLeague` | 리그 생성 (BR-11, 관리자 전용) |
| `addTeamToLeague` | 팀 추가 + currentTeams 증가 (BR-12) |
| `removeTeamFromLeague` | 팀 제거 + currentTeams 감소 (BR-12) |
| `updateMatchResult` | 경기 결과 입력 + 전적 자동 갱신 |
| `getLeagueStandings` | 순위표 조회 (ranking 오름차순) |
| `getLeagueSchedule` | 월별 경기 일정 (year/month 없으면 현재 월) |
| `getUpcomingMatchesByRegion` | 지역별 다가오는 경기 조회 (전체 리그 통합) |
