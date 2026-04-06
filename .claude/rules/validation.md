# Validation & ESLint Rules

## ESLint 핵심 규칙

### TypeScript 인터페이스
```typescript
// ✅ 여러 줄: 구분자 없음
export interface User {
  id: number
  name: string
}

// ✅ 한 줄: 쉼표 사용
export interface Point { x: number, y: number }
```

### 네이밍: camelCase 사용, snake_case 금지

### 화살표 함수
```typescript
// ✅ 한 줄
export const getUser = (id: number) => request({ url: `/user/${id}`, method: 'get' });

// ✅ 줄바꿈 시 괄호로 감싸기
export const getUser = (id: number) => request({
  url: `/user/${id}`,
  method: 'get',
});

// ❌ 화살표 뒤 즉시 줄바꿈 금지
export const getUser = (id: number) =>
  request({ ... });
```

### 객체 리터럴
```typescript
// ✅ 한 줄
const params = { status: 'active' };

// ✅ 여러 줄 (속성 2개 이상이거나 타입 지정)
const params: any = {
  status: 'IN_PROGRESS',
  page: 0,
};
```

### import 문
- 2개 이하: 한 줄 `import { Vue, Component } from 'vue-property-decorator';`
- 3개 이상: 여러 줄, 마지막에 쉼표 추가

### 기타
- 줄 끝 공백(trailing spaces) 금지
- `lines-between-class-members`: 클래스 멤버 사이 빈 줄

## 자주 발생하는 ESLint 오류

| 오류 | 원인 | 해결 |
|------|------|------|
| `Expected a line break after this opening brace` | 객체 줄바꿈 누락 | `{` 뒤 줄바꿈 추가 |
| `Trailing spaces not allowed` | 줄 끝 공백 | `npm run lint:fix` |
| `Expected linebreaks to be 'LF' but found 'CRLF'` | 줄바꿈 불일치 | Git/에디터 설정 변경 |
| `Missing trailing comma` | import 마지막 쉼표 누락 | 쉼표 추가 |

## 코드 제출 전 체크리스트

### TypeScript
- [ ] 인터페이스: 여러 줄은 구분자 없음, 한 줄은 쉼표
- [ ] 변수명: camelCase
- [ ] 화살표 함수: 한 줄 또는 괄호로 감싸기
- [ ] 객체 리터럴: 한 줄/여러 줄 중괄호 일관성
- [ ] 줄 끝 공백 제거
- [ ] import 3개 이상: 여러 줄

### Vue Template
- [ ] 태그/속성 끝 trailing spaces 없음
- [ ] 여러 속성: 각 줄에 하나씩
- [ ] `v-for` + `:key` 필수
- [ ] `v-if` + `v-for` 동시 사용 금지

## 명령어

```bash
npm run lint:fix   # 자동 수정
npm run lint       # 검사
```

## VSCode 권장 설정

```json
{
  "files.trimTrailingWhitespace": true,
  "files.insertFinalNewline": true,
  "files.eol": "\n",
  "editor.codeActionsOnSave": { "source.fixAll.eslint": true }
}
```

## ESLint 추가 참고

- `.github/ESLINT_FIX_MATCH_VUE.md` — Vue 파일 ESLint 수정 사례
- `.github/eslint-rule-no-nested-ternary.md` — 중첩 삼항 연산자 규칙
