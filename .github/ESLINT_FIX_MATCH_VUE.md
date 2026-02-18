# ESLint 오류 수정 - match.vue

## 수정일: 2026-02-08

## 수정된 오류

### 1. `object-curly-newline` 오류 (Line 468-469)

**문제:**
```typescript
const formatMap: {
  [key: string]: string
} = 
{
  FOUR_VS_FOUR: '4 대 4',
  // ...
};
```

**원인:**
- 타입 선언과 객체 리터럴이 분리되어 있어 `object-curly-newline` 규칙 위반
- `= {` 사이에 줄바꿈이 있어 중괄호 줄바꿈 규칙 불일치

**해결:**
```typescript
const formatMap: { [key: string]: string } = {
  FOUR_VS_FOUR: '4 대 4',
  FIVE_VS_FIVE: '5 대 5',
  SIX_VS_SIX: '6 대 6',
  SEVEN_VS_SEVEN: '7 대 7',
};
```

**변경 내용:**
- 타입 선언을 한 줄로 작성: `{ [key: string]: string }`
- `=` 바로 뒤에 `{`를 붙여 객체 리터럴 시작
- 객체 내용은 여러 줄로 유지하되, 전체 구조를 일관되게 수정

---

### 2. `@typescript-eslint/no-unused-vars` 경고 (Line 141) ✅ 이미 수정됨

**문제:**
```typescript
import { getMatchesByDateRange } from '@/api/match';
import { getGuestRecruitmentsByDateRange } from '@/api/guest';
```

**원인:**
- `getMatchesByDateRange`를 import했지만 코드에서 사용하지 않음

**해결:**
```typescript
import { getGuestRecruitmentsByDateRange } from '@/api/guest';
```

---

### 3. `object-curly-newline` 오류 - Import 문 (leagueStatus.vue)

**문제:**
```typescript
import { Vue, Component, Watch, Prop } from 'vue-property-decorator';
```

**원인:**
- 4개 이상의 import 항목을 한 줄로 작성하면 `object-curly-newline` 규칙 위반
- ESLint 규칙: 여러 개의 import는 여러 줄로 작성 권장

**해결:**
```typescript
import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator';
```

**변경 내용:**
- import 문을 여러 줄로 변경
- 각 항목 뒤에 쉼표 추가
- 마지막 항목(Prop) 뒤에도 trailing comma 추가

---

## 수정 후 상태

### ESLint 오류: 0개 ✅
- `object-curly-newline` 오류 해결
- `no-unused-vars` 경고 해결

---

## ESLint 규칙 설명 및 코딩 가이드

### `object-curly-newline` 규칙

객체 리터럴의 중괄호 줄바꿈 규칙:

#### ✅ 올바른 패턴

**패턴 1: 한 줄 객체 (간단한 경우)**
```typescript
const config = { enabled: true, timeout: 3000 };
```

**패턴 2: 여러 줄 객체 (일반적인 경우)**
```typescript
const config = {
  enabled: true,
  timeout: 3000,
  retries: 3,
};
```

**패턴 3: 타입과 객체가 함께 있는 경우**
```typescript
// 타입이 간단하면 한 줄로
const formatMap: { [key: string]: string } = {
  OPTION_A: 'Value A',
  OPTION_B: 'Value B',
};

// 타입이 복잡하면 분리
const complexMap: {
  [key: string]: {
    value: string
    enabled: boolean
  }
} = {
  OPTION_A: {
    value: 'Value A',
    enabled: true,
  },
};
```

#### ❌ 잘못된 패턴

```typescript
// ❌ 타입과 객체 사이에 줄바꿈
const formatMap: { [key: string]: string } = 
{
  OPTION_A: 'Value A',
};

// ❌ 타입만 여러 줄, 객체는 한 줄
const formatMap: {
  [key: string]: string
} = { OPTION_A: 'Value A', OPTION_B: 'Value B' };

// ❌ 여러 줄 객체인데 중괄호 줄바꿈 없음
const formatMap = { OPTION_A: 'Value A',
  OPTION_B: 'Value B' };
```

---

## 향후 동일 패턴 작성 가이드

### 1. Map/Dictionary 타입 객체

```typescript
// ✅ 권장: 타입 한 줄 + 객체 여러 줄
private statusMap: { [key: string]: string } = {
  PENDING: '대기중',
  APPROVED: '승인됨',
  REJECTED: '거부됨',
};

// ✅ 항목이 많으면 readonly 고려
private readonly statusMap: { [key: string]: string } = {
  PENDING: '대기중',
  APPROVED: '승인됨',
  REJECTED: '거부됨',
  // ... 더 많은 항목
};
```

### 2. Enum 대신 객체 사용

```typescript
// ✅ 객체로 상수 정의
private readonly MATCH_FORMATS = {
  FOUR_VS_FOUR: '4 대 4',
  FIVE_VS_FIVE: '5 대 5',
  SIX_VS_SIX: '6 대 6',
  SEVEN_VS_SEVEN: '7 대 7',
} as const;

// 타입 추론
type MatchFormat = keyof typeof MATCH_FORMATS;
```

### 3. 복잡한 설정 객체

```typescript
// ✅ 타입과 객체 모두 여러 줄
private slickOptions: {
  dots: boolean
  infinite: boolean
  speed: number
  // ... 더 많은 속성
} = {
  dots: false,
  infinite: true,
  speed: 500,
  slidesToShow: 1,
  // ... 더 많은 설정
};
```

### 4. Import 문 작성 가이드

```typescript
// ✅ 1-2개 항목: 한 줄로 작성
import { Vue } from 'vue-property-decorator';
import { Module, Action } from 'vuex-module-decorators';

// ✅ 3개 이상 항목: 여러 줄로 작성
import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator';

import {
  Module, VuexModule, Mutation, Action, getModule,
} from 'vuex-module-decorators';

// ❌ 4개 이상을 한 줄로 작성
import { Vue, Component, Watch, Prop } from 'vue-property-decorator';

// ❌ 중괄호 줄바꿈 없이 작성
import { Vue, Component, 
  Watch, Prop } from 'vue-property-decorator';
```

**Import 규칙 요약:**
- **1-2개 항목**: 한 줄로 작성 가능
- **3개 이상**: 여러 줄로 작성 권장
- **여러 줄 작성 시**: 시작 중괄호 뒤 줄바꿈, 각 항목마다 쉼표, 마지막 항목에도 trailing comma

---

## 검증 방법

```bash
# ESLint 검사
npm run lint

# ESLint 자동 수정
npm run lint:fix

# 특정 파일만 검사
npm run lint -- client/src/views/match.vue
```

---

## 관련 파일
- `d:\ttwijang\client\src\views\match.vue`
- `.eslintrc.js` (프로젝트 ESLint 설정)
- `.github/copilot-instructions.md` (코딩 스타일 가이드)

---

## 참고사항

### match.vue 페이지 역할
- **BR-06**: 소속 팀의 매치 조회 (팀이 있는 경우)
- **BR-10**: 팀 미소속 사용자도 게스트로 참여 가능
- 게스트(용병) 모집 정보 표시 및 신청 기능

### 지역 필터링 (BR-04)
- `selectedRegion` prop을 통해 지역별 게스트 모집 필터링
- `경남` + `시/군/구` 조합으로 API 요청
