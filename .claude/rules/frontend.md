# Frontend Rules

## 스택 요약

| Category | Technology | Version |
|----------|------------|---------|
| Framework | Vue 2 | 2.6.11 |
| Language | TypeScript | 4.2.3 |
| State Management | Vuex | 3.4.0 |
| Router | Vue Router | 3.2.0 |
| UI Library | Element UI | 2.15.7 |
| CSS | SCSS/Sass | 1.26.5 |
| HTTP Client | Axios | 0.21.1 |
| Build Tool | Vue CLI (Webpack) | 4.5.0 |
| Others | Chart.js, Toast UI Editor, Summernote, FontAwesome | - |

## 컴포넌트 작성 규칙

Vue Class Component 스타일로 작성. 순서: Props → Data → Computed → Watch → Lifecycle → Methods

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
- 속성 여러 개: 줄바꿈, `v-for`에 `:key` 필수, `v-if + v-for` 동시 사용 금지
- 뷰포트 메타 태그 수정 금지: `<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">`

## 스타일링 (SCSS/BEM)

```scss
@import '@/assets/css/variables.scss';

.component-name {
  &__header { font-weight: bold; }
  &--active { border-color: $primary-color; }
  &--disabled { opacity: 0.5; pointer-events: none; }
}

// Element UI 오버라이드는 ::v-deep 사용
::v-deep .el-table__header { background-color: $bg-light; }
```

브레이크포인트: mobile 768px / tablet 1024px / desktop 1280px

## 상태 관리 (Vuex)

```typescript
@Module({ namespaced: true, name: 'user' })
export default class UserModule extends VuexModule implements UserState {
  user: User | null = null;

  get userName(): string { return this.user?.name ?? ''; }

  @Mutation SET_USER(user: User | null): void { this.user = user; }

  @Action async login(credentials: LoginRequest): Promise<void> {
    const response = await authApi.login(credentials);
    this.SET_TOKEN(response.token);
    this.SET_USER(response.user);
  }
}
```

- State: Mutation을 통해서만 변경
- Mutation 네이밍: `UPPER_SNAKE_CASE` (동기)
- Action 네이밍: `camelCase` (비동기/API 호출)

## 에러 처리

Axios interceptor에서 status별 `Message.error()` 처리 (400/401/403/404/500).
컴포넌트는 `isLoading`, `error` 상태 관리 + try/catch/finally 패턴 사용.
전역: `Vue.config.errorHandler` + `window.addEventListener('unhandledrejection', ...)` 설정.
