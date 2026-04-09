# League 도메인

## 비즈니스 규칙

| 규칙 | 내용 |
|------|------|
| BR-04 | 지역별 리그 전환 지원 — sido/sigungu 복합 필터 또는 sigungu 단독 조회 |
| BR-11 | 리그 생성/수정/삭제 — **최고관리자 전용** |
| BR-12 | 리그 팀 추가/제거 — **최고관리자 전용** |

## 리그 상태 (LeagueStatus)

```
RECRUITING → IN_PROGRESS → COMPLETED
```

## 관련 엔티티

| 엔티티 | 역할 |
|--------|------|
| `League` | 리그 기본 정보 (기간, 지역, 최대 팀 수) |
| `LeagueTeam` | 리그 참가팀 + 전적 (wins/draws/losses/points/goalDifference) |
| `LeagueMatch` | 리그 경기 일정 및 결과 |

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
