# ESLint Rule: no-nested-ternary

## 규칙 설명 (Rule Description)

**규칙명**: `no-nested-ternary`  
**카테고리**: Stylistic Issues  
**권장 설정**: error

이 규칙은 삼항 연산자(ternary operator)를 중첩해서 사용하는 것을 금지합니다. 중첩된 삼항 연산자는 코드 가독성을 크게 떨어뜨리고 유지보수를 어렵게 만듭니다.

## 문제가 되는 코드 (Problematic Code)

### ❌ 잘못된 예시 (Bad)

```typescript
// 중첩된 삼항 연산자 - 읽기 어려움
const value = condition1 ? result1 : (condition2 ? result2 : result3);

// 더 복잡한 중첩
const status = isActive 
  ? 'active' 
  : (isPending ? 'pending' : (isCancelled ? 'cancelled' : 'unknown'));

// 실제 프로젝트 예시
private getDefaultRegionCode(): string {
  const jinju = this.regionOptions.find((opt) => opt.label === '진주시');
  return jinju ? jinju.value : (this.regionOptions.length > 0 ? this.regionOptions[0].value : '');
}
```

## 올바른 코드 (Correct Code)

### ✅ 권장 방법 1: if-else 문 사용

```typescript
// if-else로 명확하게 표현
private getDefaultRegionCode(): string {
  const jinju = this.regionOptions.find((opt) => opt.label === '진주시');
  if (jinju) {
    return jinju.value;
  }
  if (this.regionOptions.length > 0) {
    return this.regionOptions[0].value;
  }
  return '';
}

// 복잡한 조건 처리
function getStatus(isActive: boolean, isPending: boolean, isCancelled: boolean): string {
  if (isActive) {
    return 'active';
  }
  if (isPending) {
    return 'pending';
  }
  if (isCancelled) {
    return 'cancelled';
  }
  return 'unknown';
}
```

### ✅ 권장 방법 2: 단순 삼항 연산자 (중첩 없음)

```typescript
// 단일 삼항 연산자는 허용됨
const message = isValid ? '성공' : '실패';
const count = items.length > 0 ? items.length : 0;

// 조건이 2개뿐이고 간단한 경우
const price = isMember ? discount : regular;
```

### ✅ 권장 방법 3: 조기 반환 (Early Return)

```typescript
function getUserRole(user: User): string {
  if (user.isAdmin) return 'admin';
  if (user.isModerator) return 'moderator';
  if (user.isMember) return 'member';
  return 'guest';
}
```

### ✅ 권장 방법 4: Optional Chaining & Nullish Coalescing

```typescript
// 옵셔널 체이닝과 널 병합 연산자 활용
const regionCode = jinju?.value ?? (this.regionOptions[0]?.value ?? '');

// 더 명확한 방법
const regionCode = jinju?.value || this.regionOptions[0]?.value || '';
```

### ✅ 권장 방법 5: Map/Object를 활용한 매핑

```typescript
// 여러 조건을 매핑으로 처리
const statusMap: Record<string, string> = {
  active: '활성',
  pending: '대기',
  cancelled: '취소',
};

const statusLabel = statusMap[status] || '알 수 없음';

// Switch 문 활용
function getStatusLabel(status: string): string {
  switch (status) {
    case 'active':
      return '활성';
    case 'pending':
      return '대기';
    case 'cancelled':
      return '취소';
    default:
      return '알 수 없음';
  }
}
```

## 왜 이 규칙이 중요한가? (Why This Rule Matters)

### 1. 가독성 향상
```typescript
// ❌ 읽기 어려움 - 조건 흐름 파악 힘듦
return a ? b : c ? d : e ? f : g;

// ✅ 읽기 쉬움 - 조건 흐름 명확
if (a) return b;
if (c) return d;
if (e) return f;
return g;
```

### 2. 유지보수성 향상
```typescript
// ❌ 조건 추가 시 복잡도 기하급수 증가
const result = isA ? 'A' : (isB ? 'B' : (isC ? 'C' : 'D'));

// ✅ 조건 추가 시 명확하고 간단
if (isA) return 'A';
if (isB) return 'B';
if (isC) return 'C';
if (isD) return 'D'; // 새 조건 추가 쉬움
return 'default';
```

### 3. 디버깅 용이성
```typescript
// ❌ 중첩 삼항 - 어느 조건에서 문제인지 찾기 어려움
const value = x ? a : y ? b : z ? c : d;

// ✅ if-else - 각 분기에 breakpoint 설정 가능
if (x) {
  return a; // 디버깅 지점 1
}
if (y) {
  return b; // 디버깅 지점 2
}
if (z) {
  return c; // 디버깅 지점 3
}
return d; // 디버깅 지점 4
```

### 4. 에러 발생 가능성 감소
```typescript
// ❌ 괄호 위치 실수로 로직 오류 발생 가능
const result = a ? b : c ? d : e ? f : g; // 우선순위 혼동

// ✅ 명확한 흐름으로 실수 방지
if (a) return b;
if (c) return d;
if (e) return f;
return g;
```

## 예외 상황 (Exceptions)

중첩 삼항 연산자가 허용되는 경우는 **없습니다**. 항상 if-else 문이나 다른 명확한 방법을 사용하세요.

## 프로젝트 적용 규칙

### ESLint 설정 (.eslintrc.js)
```javascript
module.exports = {
  rules: {
    'no-nested-ternary': 'error', // 중첩 삼항 연산자 금지
  }
};
```

### 코드 리뷰 체크리스트
- [ ] 삼항 연산자가 중첩되어 있지 않은가?
- [ ] 조건문이 3개 이상인 경우 if-else로 변경했는가?
- [ ] 코드 가독성이 향상되었는가?

## 자동 수정 방법

```bash
# ESLint 자동 수정 (일부 경우 자동 수정 불가능)
npm run lint:fix

# 수동 수정 필요
# no-nested-ternary는 대부분 수동으로 리팩토링 필요
```

## 추가 참고 자료

- [ESLint Rule: no-nested-ternary](https://eslint.org/docs/latest/rules/no-nested-ternary)
- [Airbnb JavaScript Style Guide - Ternary](https://github.com/airbnb/javascript#comparison--nested-ternaries)
- [Clean Code: 조건문 작성하기](https://github.com/ryanmcdermott/clean-code-javascript#avoid-nested-ternaries)

## 관련 ESLint 규칙

- `no-unneeded-ternary`: 불필요한 삼항 연산자 금지 (예: `x ? true : false` → `x`)
- `no-ternary`: 모든 삼항 연산자 금지 (과도하게 엄격, 일반적으로 사용 안 함)
- `multiline-ternary`: 삼항 연산자의 줄바꿈 규칙

---

## 실전 예시 모음

### 예시 1: null/undefined 체크
```typescript
// ❌ 중첩 삼항
const name = user ? user.name : profile ? profile.name : 'Guest';

// ✅ Optional chaining
const name = user?.name ?? profile?.name ?? 'Guest';

// ✅ if-else
let name = 'Guest';
if (user?.name) {
  name = user.name;
} else if (profile?.name) {
  name = profile.name;
}
```

### 예시 2: 상태 라벨 표시
```typescript
// ❌ 중첩 삼항
const label = status === 'active' ? '활성' : status === 'pending' ? '대기' : '취소';

// ✅ Object mapping
const statusLabels: Record<string, string> = {
  active: '활성',
  pending: '대기',
  cancelled: '취소',
};
const label = statusLabels[status] || '알 수 없음';
```

### 예시 3: 우선순위 기반 선택
```typescript
// ❌ 중첩 삼항
const value = primary ? primary : secondary ? secondary : fallback;

// ✅ Nullish coalescing
const value = primary ?? secondary ?? fallback;

// ✅ Logical OR (falsy 값 처리 시)
const value = primary || secondary || fallback;
```

### 예시 4: Vue 템플릿에서
```vue
<!-- ❌ 중첩 삼항 -->
<div :class="isActive ? 'active' : isPending ? 'pending' : 'inactive'">

<!-- ✅ Computed property 사용 -->
<div :class="statusClass">

<script>
computed: {
  statusClass(): string {
    if (this.isActive) return 'active';
    if (this.isPending) return 'pending';
    return 'inactive';
  }
}
</script>
```

---

**마지막 업데이트**: 2026년 2월 8일  
**적용 프로젝트**: ttwijang-1 (Vue 2 + TypeScript + Spring Boot)
