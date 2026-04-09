# Frontend — 공통 규칙

## 스택

| Category | Technology | Version |
|----------|------------|---------|
| Framework | Vue 2 | 2.6.11 |
| Language | TypeScript | 4.2.3 |
| State | Vuex | 3.4.0 |
| Router | Vue Router | 3.2.0 |
| UI | Element UI | 2.15.7 |
| CSS | SCSS/Sass | 1.26.5 |
| HTTP | Axios | 0.21.1 |
| Build | Vue CLI (Webpack) | 4.5.0 |

## 컴포넌트 작성 규칙

Vue Class Component 스타일. 멤버 순서: **Props → Data → Computed → Watch → Lifecycle → Methods**

```typescript
@Component({ components: { ChildComponent } })
export default class MyComponent extends Vue {
  @Prop({ required: true }) readonly id!: number;

  private isLoading = false;

  get computedValue(): string { return this.title.toUpperCase(); }

  @Watch('id')
  onIdChanged(newVal: number): void { this.fetchData(); }

  created(): void { this.fetchData(); }

  private async fetchData(): Promise<void> {
    this.isLoading = true;
    try {
      this.items = await api.getItems(this.id);
    } finally {
      this.isLoading = false;
    }
  }
}
```

### 네이밍
- 파일명: PascalCase (`MyComponent.vue`)
- 컴포넌트명: 2단어 이상 (`UserProfile`, `BoardList`)
- 기본 컴포넌트: `Base` 접두사 (`BaseButton`)
- 단일 인스턴스: `The` 접두사 (`TheHeader`)

### Template 규칙
- 속성 여러 개: 줄바꿈
- `v-for`에 `:key` 필수
- `v-if + v-for` 동시 사용 금지
- **뷰포트 메타 태그 수정 금지**: `<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">`

## 스타일링 (SCSS/BEM)

```scss
@import '@/assets/css/variables.scss';

.component-name {
  &__header { font-weight: bold; }
  &--active { border-color: $primary-color; }
  &--disabled { opacity: 0.5; pointer-events: none; }
}

// Element UI 오버라이드
::v-deep .el-table__header { background-color: $bg-light; }
```

브레이크포인트: mobile 768px / tablet 1024px / desktop 1280px

## 상태 관리 (Vuex)

```typescript
@Module({ namespaced: true, name: 'user' })
export default class UserModule extends VuexModule implements UserState {
  @Mutation SET_USER(user: User | null): void { this.user = user; }

  @Action async login(credentials: LoginRequest): Promise<void> {
    const response = await authApi.login(credentials);
    this.SET_USER(response.user);
  }
}
```

- Mutation 네이밍: `UPPER_SNAKE_CASE` (동기)
- Action 네이밍: `camelCase` (비동기/API 호출)
- State는 반드시 Mutation을 통해 변경

## 에러 처리

- Axios interceptor에서 status별 `Message.error()` 처리 (400/401/403/404/500)
- 컴포넌트: `isLoading`, `error` 상태 + try/catch/finally 패턴
- 전역: `Vue.config.errorHandler` + `window.addEventListener('unhandledrejection', ...)`

## ESLint 핵심 규칙

### TypeScript 인터페이스
```typescript
// 여러 줄: 구분자 없음
export interface User {
  id: number
  name: string
}

// 한 줄: 쉼표
export interface Point { x: number, y: number }
```

### 화살표 함수
```typescript
// 한 줄
export const getUser = (id: number) => request({ url: `/user/${id}`, method: 'get' });

// 줄바꿈 시 괄호로 감싸기
export const getUser = (id: number) => request({
  url: `/user/${id}`,
  method: 'get',
});

// 금지: 화살표 뒤 즉시 줄바꿈
export const getUser = (id: number) =>
  request({ ... }); // ❌
```

### import 문
- 2개 이하: 한 줄
- 3개 이상: 여러 줄, 마지막 쉼표 추가

### 기타
- 변수명: camelCase (snake_case 금지)
- trailing spaces 금지
- 클래스 멤버 사이 빈 줄 (`lines-between-class-members`)

## 명령어

```bash
npm run lint -- --fix   # 자동 수정
npm run lint            # 검사
```

## 추가 참고

- `.github/ESLINT_FIX_MATCH_VUE.md` — Vue ESLint 수정 사례
- `.github/eslint-rule-no-nested-ternary.md` — 중첩 삼항 연산자 규칙
