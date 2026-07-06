# Team 도메인

## 비즈니스 규칙

| 규칙 | 내용 |
|------|------|
| BR-01 | 1계정 1팀 가입 제한 — APPROVED 상태 팀이 있으면 다른 팀 가입/신청 불가 |
| BR-02 | 1계정 1팀 생성 제한 — ACTIVE 상태 팀을 이미 생성했으면 추가 생성 불가 |

BR-01은 **신청 시, 승인 시** 두 번 모두 검증해야 합니다 (`processJoinRequest` 참고).

## 역할 (MemberRole)

| 역할 | 권한 |
|------|------|
| OWNER | 팀 생성자. 모든 관리 권한 (수정/삭제/가입 승인/위임) |
| MANAGER | 경기 종료 등 일부 관리 권한 (MatchService에서 확인) |
| MEMBER | 일반 팀원 |

## 멤버 상태 흐름 (MemberStatus)

```
가입 신청 → PENDING
운영자 승인 → APPROVED
운영자 거절 → REJECTED
초대 코드로 가입 → 즉시 APPROVED (joinTeamByCode)
```

## 팀 상태 (TeamStatus)

- `ACTIVE` — 정상 운영 중
- `DELETED` — 소프트 삭제 (실제 삭제 X)

## 권한 확인 패턴

```java
// 운영자 확인: team.getOwnerUid()로 비교 (TeamMember 역할 X)
if (!team.getOwnerUid().equals(userUid)) {
    throw new IllegalArgumentException("권한이 없습니다.");
}
```

## 팀 코드

- 8자리 대문자 + 숫자 랜덤 생성 (`SecureRandom`)
- 중복 확인 후 저장 (`existsByTeamCode`)
- 초대 코드로 즉시 가입 가능 (`joinTeamByCode` — APPROVED 처리)
- `teamCode` 컬럼은 DB에 `unique=true, nullable=false` 제약이 있고 소프트 삭제라 DELETED 팀도 행이 남음 →
  삭제된 팀의 코드는 재사용 허용 정책(`existsByTeamCodeAndStatusNot(.., DELETED)`)과 실제 DB 제약이 어긋나
  같은 코드로 재가입 시 `DataIntegrityViolationException`이 발생하는 버그가 있었음.
  `approveDeleteTeam()`에서 삭제 처리 시 `teamCode`를 `"DEL_" + 랜덤값`으로 치환해 원래 코드값을 비워주는 방식으로 해결 (2026-07).

## 회원 모집 (recruitingMembers)

- `saveRecruitment()`: 모집 설정 저장 + `recruitingMembers = true`
- `stopRecruitment()`: `recruitingMembers = false`
- 모집 중 팀 목록 필터: `findRecruitingTeams()` (native query — sort 없는 Pageable 사용)
- `getTeamList`/`getTeamListBySigungu`의 `recruiting=true` 조회는 `findByStatusAndRecruitingMembersTrue*` 사용 — DELETED 팀은 `recruitingMembers`가 남아있어도 목록에서 제외됨 (status=ACTIVE 필수)

## memberCount 관리

팀원 수(`memberCount`)는 DB 집계가 아닌 Team 엔티티에 직접 저장합니다.
가입 승인/초대 코드 가입 시 `+1`, 탈퇴 시 `-1` 직접 업데이트 필요.

## 주요 서비스 메서드

| 메서드 | 설명 |
|--------|------|
| `createTeam` | BR-01, BR-02 검증 후 팀 생성 + 운영자 자동 가입 |
| `joinTeam` | PENDING 상태로 가입 신청 |
| `joinTeamByCode` | 초대 코드로 즉시 APPROVED 가입 |
| `processJoinRequest` | 승인(APPROVED)/거절(REJECTED) 처리, 승인 시 BR-01 재검증 |
| `delegateOwner` | 운영자 위임 — 기존 OWNER → MEMBER, 새 MEMBER → OWNER |
| `deleteTeam` | 소프트 삭제 (status = DELETED) |
| `checkMembershipStatus` | 가입 가능 여부 종합 체크 (BR-01, BR-02) |
