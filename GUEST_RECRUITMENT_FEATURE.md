# 게스트 모집 탭 기능 구현

## 개요
팀 페이지(`/team/dev?tab=match`)의 매치경기 탭에 **게스트 모집 탭**을 추가하여 팀 관리자가 진행 중인 게스트 모집을 한눈에 확인하고 관리(취소)할 수 있도록 구현했습니다.

## 추가된 기능

### 1. 게스트 모집 필터 탭
- **위치**: match-filter-row에 새로운 탭 추가
- **가시성**: 팀 관리자(isOwner)만 보임
- **필터 옵션**:
  - 전체
  - 친선경기
  - 자체경기
  - **게스트 모집** (신규)

### 2. 게스트 모집 카드 표시
게스트 모집 탭을 클릭하면 다음 정보를 표시하는 카드 목록을 볼 수 있습니다:
- **장소**: 스타디움 이름
- **상태**: 모집중/완료/취소됨/마감 (상태별 배지)
- **날짜 및 시간**: 매치 날짜 및 시작 시간
- **포지션**: 필드/골키퍼/전체
- **모집 현황**: 현재 참가 게스트 수 / 최대 모집 수
- **참가비**: 있는 경우에만 표시

### 3. 관리 기능 (관리자만 가능)
각 게스트 모집 카드의 오른쪽 상단의 메뉴(⋯) 버튼을 통해:

#### 취소
- 게스트 모집 취소 기능
- 취소 전 확인 다이얼로그 표시
- 취소 시 승인된 게스트에게 알림 발송
- 취소 후 리스트 자동 새로고침

### 4. 데이터 로드
- 팀 UID 기반으로 해당 팀의 모든 게스트 모집 목록 로드
- 매치 날짜 기준 오름차순 정렬 (가까운 날짜부터 표시)
- 매치경기 탭 클릭 시 자동으로 매치 데이터와 함께 로드

## 구현 상세

### 파일 변경사항

#### `/client/src/views/teamPage.vue`

**Imports 추가**:
```typescript
import {
  getTeamRecruitments, cancelGuestRecruitment,
} from '@/api/guest';
```

**Data 필드 추가**:
- `guestRecruitments: any[]` - 게스트 모집 목록
- `openMenuGuestUid: string` - 메뉴 오픈 상태

**메서드 추가**:
- `loadGuestRecruitments()` - 게스트 모집 목록 로드
- `toggleGuestMenu(uid)` - 메뉴 토글
- `handleGuestMenuCommand(command, recruitment)` - 게스트 모집 취소 처리
- `getGuestStatusLabel(status)` - 상태 텍스트 변환
- `getGuestStatusClass(status)` - 상태별 CSS 클래스
- `getPositionLabel(position)` - 포지션 텍스트 변환
- `formatGuestDate(dateStr)` - 날짜 포매팅

**템플릿 추가**:
- match-filter-row에 게스트 모집 탭 추가
- 게스트 모집 카드 리스트 섹션
- 취소 메뉴

**스타일 추가**:
- `.guest-card-list` - 카드 리스트 컨테이너
- `.guest-card` - 카드 스타일
- `.guest-card-header` - 헤더 영역
- `.guest-card-title` - 제목 스타일
- `.guest-status-badge` - 상태 배지 (모집중, 완료, 취소됨, 마감)
- `.guest-more-menu` - 메뉴 팝업
- `.guest-card-info` - 정보 표시 영역
- `.guest-info-row` - 정보 행 (날짜, 포지션, 모집현황, 참가비)

## 사용자 경험

### 관리자 시점
1. 팀 페이지의 매치경기 탭 클릭
2. match-filter-row에서 "게스트 모집" 탭 확인
3. "게스트 모집" 탭 클릭하여 진행 중인 게스트 모집 목록 확인
4. 각 카드의 메뉴(⋯) 버튼을 통해:
   - **취소**: 게스트 모집 취소

### 일반 사용자 시점
- 게스트 모집 탭이 보이지 않음 (관리자 전용)

## API 연동

사용되는 API:
- `getTeamRecruitments(teamUid)` - 팀의 게스트 모집 목록 조회
- `cancelGuestRecruitment(recruitmentUid)` - 게스트 모집 취소

## 상태 정의

### 게스트 모집 상태
- `RECRUITING`: 모집중 (모집 진행 중)
- `COMPLETED`: 완료 (경기 완료)
- `CANCELLED`: 취소됨 (관리자 또는 팀이 취소)
- `EXPIRED`: 마감 (경기 시간 지남)

### 포지션 타입
- `FIELD`: 필드
- `GK`: 골키퍼
- `ANY`: 전체 (제한 없음)

## 테스트 항목

- [x] 빌드 성공 (npm run build)
- [x] 린트 검사 (npm run lint)
- [x] 관리자만 게스트 모집 탭 표시
- [x] 게스트 모집 목록 로드
- [x] 취소 메뉴 기능
- [x] 상태 배지 및 포지션 레이블 표시
- [x] 메뉴 토글 기능
- [x] 취소 시 확인 다이얼로그
- [x] 취소 후 리스트 새로고침
