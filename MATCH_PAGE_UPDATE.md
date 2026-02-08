# 매치 페이지 & 마이페이지 업데이트 요약

## 📅 업데이트 일자
2026-02-08

---

## 🎯 주요 변경사항

### 1. 매치 페이지 (match.vue) - 팀 생성/가입 UX 개선

#### 변경 전
- 팀 생성 후: "이미 팀을 생성하였습니다" (비활성 버튼)
- 팀 코드 입력창: 일반 입력 가능

#### 변경 후
- **팀 없음**: "팀 만들기" 버튼 + 팀 코드 입력 가능
- **팀 있음**: "팀 입장하기" 노란색 버튼 + 팀 코드 placeholder로 readonly 표시

---

## 📝 상세 변경 내역

### Match Page (match.vue)

#### 템플릿 변경
```vue
<!-- 이전: 복잡한 v-if/v-else 분기 -->
<template v-if="myTeamInfo">
  <div class="my-team-card">...</div>
</template>
<template v-else>
  <el-button :disabled="!canCreateTeam">
    {{ canCreateTeam ? '팀 만들기' : '이미 팀을 생성하였습니다' }}
  </el-button>
</template>

<!-- 변경 후: 단순화된 구조 -->
<h2 class="section-title">나의 팀을 만들어 보세요!</h2>
<el-button
  :class="{ 'team-enter-button': myTeamInfo !== null }"
  @click="myTeamInfo ? enterMyTeam() : goToCreateTeam()"
>
  {{ myTeamInfo ? '팀 입장하기' : '팀 만들기' }}
</el-button>

<input
  v-if="myTeamInfo"
  :placeholder="myTeamInfo.teamCode"
  readonly
>
<input
  v-else
  v-model="teamCode"
  placeholder="팀 코드를 입력하세요."
>
```

#### 스크립트 변경
1. **불필요한 속성 제거**
   - `createdTeamCode` 삭제
   - `hasTeamMembership` 삭제
   - `displayTeamCode` computed 삭제

2. **API 응답 처리 개선**
```typescript
// /api/team/my 는 단일 객체 또는 배열을 반환할 수 있음
const teamData = teamsResponse.data;
let team = null;

if (Array.isArray(teamData)) {
  team = teamData.length > 0 ? teamData[0] : null;
} else {
  team = teamData;
}
```

3. **팀 정보 로드 로직 단순화**
```typescript
// 팀이 있으면 (만들었든 가입했든) 팀 정보 로드
if (status.hasTeam || hasCreatedTeam) {
  const team = /* ... */;
  if (team && team.uid) {
    this.myTeamInfo = {
      uid: team.uid,
      name: team.name,
      teamCode: team.teamCode,
      logoUrl: team.logoUrl || team.logoFileUid,
    };
  }
}
```

#### CSS 추가
```css
.team-enter-button {
  background-color: #f7c600 !important;
  border-color: #f7c600 !important;
  color: #ffffff !important;
}

.team-enter-button:hover {
  background-color: #e0b400 !important;
  border-color: #e0b400 !important;
}
```

---

### My Page (myPage.vue) - 소속 팀 표시 개선

#### 변경 전
- 고정된 라벨: "소속 팀"
- 팀명만 표시

#### 변경 후
- **나의 팀**: 사용자가 생성한 팀
- **소속 팀**: 사용자가 가입한 팀
- 팀명 표시

#### 템플릿 변경
```vue
<!-- 이전 -->
<div class="stat-label">소속 팀</div>

<!-- 변경 후 -->
<div class="stat-label">{{ userStats.teamLabel }}</div>
```

#### 스크립트 변경
1. **userStats 속성 추가**
```typescript
private userStats = {
  // ...existing properties...
  team: '-',
  teamLabel: '소속 팀', // 새로 추가
};
```

2. **팀 정보 로드 로직 개선**
```typescript
// /api/team/my 응답 처리
const teamData = teamsResponse.data;
let team = null;
if (Array.isArray(teamData)) {
  team = teamData.length > 0 ? teamData[0] : null;
} else {
  team = teamData;
}

if (team && team.name) {
  this.userStats.team = team.name;
  
  // 팀을 생성했는지 확인
  const statusResponse = await checkMembershipStatus();
  const hasCreatedTeam = (statusResponse.data as any).hasCreatedTeam || false;
  
  if (hasCreatedTeam) {
    this.userStats.teamLabel = '나의 팀';
  } else {
    this.userStats.teamLabel = '소속 팀';
  }
}
```

3. **Import 추가**
```typescript
import { getMyTeams, checkMembershipStatus } from '@/api/team';
```

---

## 🎨 UI/UX 개선사항

### Match Page
| 상태 | 버튼 스타일 | 버튼 텍스트 | 입력창 |
|------|------------|------------|--------|
| 팀 없음 | 기본 (파란색) | 팀 만들기 | 활성화, 입력 가능 |
| 팀 있음 | 강조 (노란색) | 팀 입장하기 | readonly, 팀 코드 placeholder |

### My Page
| 조건 | 라벨 | 표시값 |
|------|------|--------|
| 팀 생성함 (hasCreatedTeam: true) | 나의 팀 | 팀명 |
| 팀 가입함 (hasCreatedTeam: false) | 소속 팀 | 팀명 |
| 팀 없음 | 소속 팀 | - |

---

## 🐛 버그 수정

### ESLint 오류 수정
- **문제**: 중첩된 삼항 연산자 (`no-nested-ternary`)
- **해결**: if-else 문으로 변경
```typescript
// Before
const team = Array.isArray(teamData) 
  ? (teamData.length > 0 ? teamData[0] : null) 
  : teamData;

// After
let team = null;
if (Array.isArray(teamData)) {
  team = teamData.length > 0 ? teamData[0] : null;
} else {
  team = teamData;
}
```

---

## 🔧 기술적 개선사항

1. **API 응답 유연성 향상**
   - `/api/team/my` 엔드포인트가 배열 또는 단일 객체를 반환할 수 있도록 처리
   - `Array.isArray()` 체크로 안전한 데이터 파싱

2. **코드 가독성 향상**
   - 복잡한 템플릿 조건문 단순화
   - 불필요한 상태 변수 제거
   - 명확한 변수명 사용 (`teamLabel`)

3. **일관성 있는 스타일**
   - Element UI 버튼 스타일과 조화
   - 노란색 강조 버튼으로 사용자 액션 유도

---

## 📊 API 엔드포인트

### 사용된 API
- `GET /team/membership-status` - 팀 생성/가입 상태 확인
- `GET /team/my` - 내 팀 정보 조회

### API 응답 구조
```typescript
// MembershipStatus
{
  hasTeam: boolean,           // 팀에 소속되어 있는지
  hasCreatedTeam: boolean,    // 팀을 생성했는지
  hasPendingRequest: boolean, // 가입 대기 중인지
  canJoinTeam: boolean,       // 팀 가입 가능한지
  canCreateTeam: boolean      // 팀 생성 가능한지
}

// Team Data (단일 객체 또는 배열)
{
  uid: string,
  name: string,
  teamCode: string,
  logoUrl: string | null,
  // ...other properties
}
```

---

## ✅ 테스트 시나리오

### Match Page
1. ✅ 팀이 없을 때: "팀 만들기" 버튼, 팀 코드 입력 가능
2. ✅ 팀을 생성했을 때: "팀 입장하기" 노란색 버튼, 팀 코드 readonly
3. ✅ 팀에 가입했을 때: "팀 입장하기" 노란색 버튼, 팀 코드 readonly
4. ✅ 버튼 클릭: 팀 페이지로 이동 (`/team/:uid`)

### My Page
1. ✅ 팀 없음: "소속 팀: -"
2. ✅ 팀 생성함: "나의 팀: [팀명]"
3. ✅ 팀 가입함: "소속 팀: [팀명]"
4. ✅ API 실패 시: 기본값 "소속 팀: -"

---

## 🚀 다음 단계

### 권장 개선사항
1. **팀 페이지 구현**
   - `/team/:uid` 라우트 추가
   - 팀 상세 정보, 멤버 목록, 경기 일정 표시

2. **팀 생성 플로우 개선**
   - 팀 생성 후 자동으로 팀 페이지로 이동
   - 팀 코드 복사 기능 추가

3. **에러 처리 강화**
   - API 실패 시 사용자 친화적인 메시지 표시
   - 재시도 기능 추가

4. **로딩 상태 표시**
   - Skeleton UI 추가
   - 로딩 스피너 개선

---

## 📌 참고사항

### 코딩 스타일
- Vue 2 + TypeScript + Class Component
- ESLint Airbnb 스타일 가이드 준수
- camelCase 변수명, PascalCase 컴포넌트명

### 브라우저 호환성
- 모던 브라우저 지원 (Chrome, Firefox, Safari, Edge)
- 모바일 반응형 디자인

---

## 👥 기여자
- GitHub Copilot
- 개발팀

---

## 📄 관련 문서
- [Database Migration Guide](./database-migration-guide.md)
- [Coding Instructions](./.github/copilot-instructions.md)
- [API Documentation](./backend/README.md)
