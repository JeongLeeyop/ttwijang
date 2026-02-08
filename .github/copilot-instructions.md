````instructions
작업해야하는 요구사항은 아래 파일들에 정리되어 있습니다.
각 파일마다 전체 요구사항이 각각 다른 관점으로 작성되어 있으니, 각 파일의 내용을 비교하면서 부족한 부분은 보충하고, 통합하면서 작업해주세요
./request.md
./request2.md
./request3.md

.github/img 안에는 페이지 디자인 스크린샷이 들어있습니다.
- 현재 프론트에서 사용 중인 컴포넌트와 페이지 구조를 시각적으로 확인할 수 있습니다.
- 만약 추가로 필요한 화면이 있다면 구현해주세요

./NotebookLM Mind Map.png는 대략적인 마인드 맵이 들어있습니다.


# Backend Project Analysis Checklist

## 언어 및 프레임워크 (Language & Framework)
- [x] **Spring Boot 2.5.4** (Java 11)
- [ ] Express.js
- [ ] FastAPI

## 데이터베이스 (Database)
- [ ] PostgreSQL
- [x] **MySQL** (mysql-connector-java)
- [ ] MongoDB
- [ ] Redis

## ORM/ODM
- [x] **Spring Data JPA**
- [x] **MyBatis** (mybatis-spring-boot-starter 2.2.2)
- [x] **QueryDSL** (querydsl-jpa)
- [ ] TypeORM
- [ ] Mongoose

## 인증/인가 방식 (Authentication/Authorization)
- [x] **JWT** (jjwt 0.11.2)
- [x] **OAuth2** (spring-security-oauth2, spring-security-oauth2-client)
- [x] **Spring Security**
- [ ] Session-based

## API 문서화 도구 (API Documentation)
- [x] **Swagger/OpenAPI** (springdoc-openapi 1.6.15)
- [ ] Postman Collection

---

# Current Project Stack Summary

| Category | Technology | Version |
|----------|------------|---------|
| Language | Java | 11 |
| Framework | Spring Boot | 2.5.4 |
| Database | MySQL | Runtime |
| ORM | JPA + MyBatis + QueryDSL | - |
| Security | Spring Security + OAuth2 + JWT | - |
| Build Tool | Maven | - |
| Others | Lombok, MapStruct, Firebase Admin, Apache POI | - |

---

# Frontend Project Analysis Checklist

## 사용 중인 프레임워크 (Framework)
- [ ] React
- [x] **Vue 2.6.11** (vue-class-component, vue-property-decorator)
- [ ] Angular

## 상태 관리 도구 (State Management)
- [ ] Redux
- [ ] Zustand
- [ ] Pinia
- [x] **Vuex 3.4.0** (vuex-module-decorators)

## CSS 프레임워크 (CSS Framework)
- [ ] Tailwind CSS
- [ ] Styled-components
- [x] **SCSS/Sass** (sass, sass-loader)
- [x] **Element UI 2.15.7**

## 테스팅 도구 (Testing Tools)
- [ ] Jest
- [ ] Vitest
- [ ] Cypress
- [ ] (현재 테스트 도구 미설정)

## 빌드 도구 (Build Tools)
- [ ] Vite
- [x] **Webpack** (via @vue/cli-service 4.5.0)
- [ ] Parcel

---

# Current Frontend Stack Summary

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
| Linting | ESLint + Airbnb | 6.7.2 |
| Others | Chart.js, Toast UI Editor, Summernote, FontAwesome | - |

---

# Coding Style Guide

## 컴포넌트 작성 규칙 (Component Rules)

### Vue Class Component 스타일
```typescript
@Component({
  components: { ChildComponent }
})
export default class MyComponent extends Vue {
  // 1. Props (위에서 아래로 순서 지정)
  @Prop({ required: true }) readonly id!: number;
  @Prop({ default: '' }) readonly title!: string;

  // 2. Data
  private isLoading = false;
  private items: Item[] = [];

  // 3. Computed
  get computedValue(): string {
    return this.title.toUpperCase();
  }

  // 4. Watch
  @Watch('id')
  onIdChanged(newVal: number, oldVal: number): void {
    this.fetchData();
  }

  // 5. Lifecycle Hooks
  created(): void {
    this.fetchData();
  }

  mounted(): void {
    this.initializeComponent();
  }

  // 6. Methods
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

### 컴포넌트 네이밍 규칙
- **파일명**: PascalCase 사용 (`MyComponent.vue`)
- **컴포넌트명**: 2단어 이상 사용 (`UserProfile`, `BoardList`)
- **기본 컴포넌트**: `Base` 접두사 사용 (`BaseButton`, `BaseInput`)
- **단일 인스턴스**: `The` 접두사 사용 (`TheHeader`, `TheSidebar`)

### Template 규칙
```html
<!-- 속성이 많을 경우 줄바꿈 -->
<el-button
  type="primary"
  size="small"
  :loading="isLoading"
  :disabled="!isValid"
  @click="handleSubmit"
>
  제출
</el-button>

<!-- v-for와 :key 필수 -->
<div v-for="item in items" :key="item.id">
  {{ item.name }}
</div>

<!-- v-if와 v-for 동시 사용 금지 -->
<template v-for="item in items" :key="item.id">
  <div v-if="item.isActive">{{ item.name }}</div>
</template>
```

---

## 스타일링 규칙 (Styling Rules)

### SCSS 구조
```scss
// 1. 변수 import
@import '@/assets/css/variables.scss';

// 2. 컴포넌트 스타일 (BEM 네이밍)
.component-name {
  // Layout
  display: flex;
  flex-direction: column;
  
  // Box Model
  padding: 16px;
  margin-bottom: 20px;
  
  // Typography
  font-size: 14px;
  color: $text-primary;
  
  // Visual
  background-color: $bg-white;
  border-radius: 8px;
  
  // Element
  &__header {
    font-weight: bold;
    margin-bottom: 12px;
  }
  
  &__content {
    flex: 1;
  }
  
  // Modifier
  &--active {
    border-color: $primary-color;
  }
  
  &--disabled {
    opacity: 0.5;
    pointer-events: none;
  }
}
```

### Element UI 커스터마이징
```scss
// Element UI 컴포넌트 오버라이드는 scoped 밖에서
.my-component {
  // scoped 스타일
}

// Element UI 오버라이드
::v-deep {
  .el-table {
    &__header {
      background-color: $bg-light;
    }
  }
  
  .el-button--primary {
    background-color: $primary-color;
  }
}
```

### 반응형 규칙
```scss
// Breakpoints
$breakpoints: (
  'mobile': 768px,
  'tablet': 1024px,
  'desktop': 1280px
);

@mixin mobile {
  @media (max-width: 768px) { @content; }
}

@mixin tablet {
  @media (max-width: 1024px) { @content; }
}
```

---

## 상태 관리 규칙 (State Management Rules)

### Vuex Module 구조
```typescript
// store/modules/user.ts
import { Module, VuexModule, Mutation, Action } from 'vuex-module-decorators';

export interface UserState {
  user: User | null;
  token: string;
  isAuthenticated: boolean;
}

@Module({ namespaced: true, name: 'user' })
export default class UserModule extends VuexModule implements UserState {
  // State
  user: User | null = null;
  token = '';
  isAuthenticated = false;

  // Getters
  get userName(): string {
    return this.user?.name ?? '';
  }

  get userRole(): string {
    return this.user?.role ?? 'guest';
  }

  // Mutations (동기 처리)
  @Mutation
  SET_USER(user: User | null): void {
    this.user = user;
  }

  @Mutation
  SET_TOKEN(token: string): void {
    this.token = token;
    this.isAuthenticated = !!token;
  }

  @Mutation
  CLEAR_AUTH(): void {
    this.user = null;
    this.token = '';
    this.isAuthenticated = false;
  }

  // Actions (비동기 처리)
  @Action
  async login(credentials: LoginRequest): Promise<void> {
    const response = await authApi.login(credentials);
    this.SET_TOKEN(response.token);
    this.SET_USER(response.user);
  }

  @Action
  async logout(): Promise<void> {
    await authApi.logout();
    this.CLEAR_AUTH();
  }
}
```

### 상태 관리 원칙
- **State**: 직접 변경 금지, Mutation을 통해서만 변경
- **Mutation**: 동기 처리만, 네이밍은 `UPPER_SNAKE_CASE`
- **Action**: 비동기 처리, API 호출, 네이밍은 `camelCase`
- **Getter**: 파생 상태 계산, 네이밍은 `camelCase`

### 컴포넌트에서 Store 사용
```typescript
import { getModule } from 'vuex-module-decorators';
import UserModule from '@/store/modules/user';

@Component
export default class MyComponent extends Vue {
  private userModule = getModule(UserModule, this.$store);

  get userName(): string {
    return this.userModule.userName;
  }

  async handleLogin(): Promise<void> {
    await this.userModule.login(this.credentials);
  }
}
```

---

## 에러 처리 규칙 (Error Handling Rules)

### API 에러 처리 (Axios Interceptor)
```typescript
// utils/request.ts
import axios, { AxiosError } from 'axios';
import { Message } from 'element-ui';

const service = axios.create({
  baseURL: process.env.VUE_APP_API_URL,
  timeout: 30000
});

// Response Interceptor
service.interceptors.response.use(
  (response) => response.data,
  (error: AxiosError) => {
    const { response } = error;
    
    if (!response) {
      Message.error('네트워크 연결을 확인해주세요.');
      return Promise.reject(error);
    }

    const { status, data } = response;
    
    switch (status) {
      case 400:
        Message.error(data.message || '잘못된 요청입니다.');
        break;
      case 401:
        Message.error('로그인이 필요합니다.');
        // 로그인 페이지로 리다이렉트
        router.push('/login');
        break;
      case 403:
        Message.error('접근 권한이 없습니다.');
        break;
      case 404:
        Message.error('요청한 리소스를 찾을 수 없습니다.');
        break;
      case 500:
        Message.error('서버 오류가 발생했습니다.');
        break;
      default:
        Message.error(data.message || '오류가 발생했습니다.');
    }
    
    return Promise.reject(error);
  }
);
```

### 컴포넌트 에러 처리
```typescript
@Component
export default class MyComponent extends Vue {
  private isLoading = false;
  private error: string | null = null;

  async fetchData(): Promise<void> {
    this.isLoading = true;
    this.error = null;
    
    try {
      const data = await api.getData();
      this.processData(data);
    } catch (error) {
      this.handleError(error);
    } finally {
      this.isLoading = false;
    }
  }

  private handleError(error: unknown): void {
    if (error instanceof AxiosError) {
      this.error = error.response?.data?.message || '데이터를 불러오는데 실패했습니다.';
    } else if (error instanceof Error) {
      this.error = error.message;
    } else {
      this.error = '알 수 없는 오류가 발생했습니다.';
    }
    
    // 로깅 (개발 환경에서만)
    if (process.env.NODE_ENV === 'development') {
      console.error('[Error]', error);
    }
  }
}
```

### 전역 에러 핸들러
```typescript
// main.ts
Vue.config.errorHandler = (err, vm, info) => {
  console.error('[Global Error]', err, info);
  
  // 에러 리포팅 서비스로 전송 (Sentry 등)
  if (process.env.NODE_ENV === 'production') {
    // reportError(err, vm, info);
  }
};

// Promise rejection 처리
window.addEventListener('unhandledrejection', (event) => {
  console.error('[Unhandled Promise Rejection]', event.reason);
  event.preventDefault();
});
```

### Form Validation 에러 처리
```typescript
// VeeValidate 사용
@Component
export default class FormComponent extends Vue {
  async submitForm(): Promise<void> {
    const isValid = await this.$validator.validateAll();
    
    if (!isValid) {
      const firstError = this.$validator.errors.items[0];
      Message.warning(firstError?.msg || '입력값을 확인해주세요.');
      return;
    }
    
    await this.saveData();
  }
}
```

### 에러 표시 컴포넌트
```html
<template>
  <div class="error-container" v-if="error">
    <el-alert
      :title="error"
      type="error"
      show-icon
      :closable="false"
    >
      <el-button size="small" @click="retry">
        다시 시도
      </el-button>
    </el-alert>
  </div>
</template>
```

---

# Database Migration

데이터베이스 마이그레이션 스크립트 가이드는 별도 파일로 관리합니다.

📄 **[database-migration-guide.md](./database-migration-guide.md)** 참조

---

## ESLint 규칙 (ESLint Rules)

### TypeScript 인터페이스 규칙
```typescript
// ✅ 올바른 방법: 여러 줄인 경우 구분자 없음
export interface User {
  id: number
  name: string
  email: string
}

// ✅ 한 줄인 경우: 쉼표(,) 사용
export interface Point { x: number, y: number }

// ❌ 잘못된 방법: 여러 줄에 쉼표 사용
export interface User {
  id: number,
  name: string,
  email: string,
}

// ❌ 잘못된 방법: 여러 줄에 세미콜론 사용
export interface User {
  id: number;
  name: string;
  email: string;
}
```

### 네이밍 규칙
```typescript
// ✅ camelCase 사용
export interface ApiResponse {
  resultCode: number
  successCount: number
  errorMessage: string
}

// ❌ snake_case 사용 금지
export interface ApiResponse {
  result_code: number
  success_count: number
  error_message: string
}
```

### 화살표 함수 규칙
```typescript
// ✅ 한 줄로 작성하거나
export const getUser = (id: number) => request({ url: `/user/${id}`, method: 'get' });

// ✅ 줄바꿈 시 괄호로 감싸기
export const getUser = (id: number) => request({
  url: `/user/${id}`,
  method: 'get',
});

// ❌ 화살표 뒤 즉시 줄바꿈 금지
export const getUser = (id: number) =>
  request({
    url: `/user/${id}`,
    method: 'get',
  });
```

### 주요 ESLint 규칙 요약
- **@typescript-eslint/member-delimiter-style**:
  - 여러 줄(multiline): 구분자 없음
  - 한 줄(singleline): 쉼표(,) 사용
- **camelcase**: 변수/속성명은 camelCase 사용
- **implicit-arrow-linebreak**: 화살표 함수는 한 줄 또는 괄호로 감싸기
- **no-trailing-spaces**: 줄 끝 공백 금지 (모든 코드 라인)
- **object-property-newline**: 객체 속성은 각 줄에 하나씩 (복잡한 객체)
- **object-curly-newline**: 객체 리터럴의 중괄호 줄바꿈 규칙
  - 한 줄 객체: 중괄호 안에 줄바꿈 없음 `{ key: value }`
  - 여러 줄 객체: 중괄호 뒤/앞에 줄바꿈 필수
- **lines-between-class-members**: 클래스 멤버 사이 빈 줄 추가

### 객체 리터럴 작성 규칙 (object-curly-newline)
```typescript
// ✅ 올바른 방법: 한 줄 객체 (간단한 경우)
const params = { status: 'active' };

// ✅ 올바른 방법: 여러 줄 객체 (속성이 2개 이상이거나 복잡한 경우)
const params: any = {
  status: 'IN_PROGRESS',
};

const params: any = {
  status: 'IN_PROGRESS',
  page: 0,
  size: 10,
};

// ❌ 잘못된 방법: 중괄호 줄바꿈 불일치
const params: any = { status: 'IN_PROGRESS' }; // 타입 지정된 경우 여러 줄로 작성

const params = {
  status: 'active' }; // 중괄호 불일치

// ✅ import 문에서도 동일 규칙 적용
import { Vue, Component } from 'vue-property-decorator'; // 2개까지는 한 줄 가능

import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator'; // 3개 이상은 여러 줄로
```

### Vue Template에서 주의사항
```html
<!-- ✅ 올바른 방법: 태그와 속성에 trailing spaces 없음 -->
<button
  class="verify-button"
  :disabled="isVerified"
  @click="sendVerificationCode"
>
  인증 요청
</button>

<!-- ❌ 잘못된 방법: <button 뒤에 공백 -->
<button 
  class="verify-button" 
  :disabled="isVerified"
>
  인증 요청
</button>
```

### ESLint 설정 확인 및 자동 수정
프로젝트의 ESLint 설정은 `.eslintrc.js`에서 관리됩니다:
```bash
# ESLint 검사
npm run lint

# ESLint 자동 수정 (대부분의 오류 자동 수정)
npm run lint:fix
```

### 일반적인 ESLint 오류 및 해결방법
| 오류 메시지 | 원인 | 해결방법 |
|------------|------|---------|
| `Expected a line break after this opening brace` | 객체 중괄호 줄바꿈 누락 | 여러 줄 객체는 `{` 뒤에 줄바꿈 추가 |
| `Expected a line break before this closing brace` | 객체 중괄호 줄바꿈 누락 | 여러 줄 객체는 `}` 앞에 줄바꿈 추가 |
| `Trailing spaces not allowed` | 줄 끝에 공백 존재 | 줄 끝 공백 제거 또는 `npm run lint:fix` |
| `Expected linebreaks to be 'LF' but found 'CRLF'` | 줄바꿈 문자 불일치 | Git 설정 또는 에디터 설정 변경 |
| `Missing trailing comma` | import 문 마지막 쉼표 누락 | 여러 줄 import는 마지막에 쉼표 추가 |

### VSCode 설정 권장사항
```json
{
  "files.trimTrailingWhitespace": true,
  "files.insertFinalNewline": true,
  "files.eol": "\n",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": true
  }
}
```
### 프론트 뷰포트 수정 금지
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">

### 공백(Trailing Spaces) 제거 규칙 (no-trailing-spaces)
```typescript
// ✅ 올바른 방법: 줄 끝에 공백 없음
const name = 'John';
if (isValid) {
  return true;
}

// ❌ 잘못된 방법: 줄 끝에 공백 있음 (육안으로는 보이지 않음)
const name = 'John';   // 여기 공백 있음
if (isValid) {         // 여기도 공백 있음
  return true;
}      // 여기도 공백 있음

// 💡 팁: VSCode 설정으로 자동 제거
// settings.json에 추가:
// "files.trimTrailingWhitespace": true
```

---

## 코드 작성 시 주의사항 체크리스트

### TypeScript/JavaScript
- [ ] 인터페이스: 여러 줄은 구분자 없음, 한 줄은 쉼표 사용
- [ ] 변수명: camelCase 사용 (snake_case 금지)
- [ ] 화살표 함수: 한 줄 또는 괄호로 감싸기
- [ ] 객체 리터럴: 한 줄이면 중괄호 안에 줄바꿈 없음, 여러 줄이면 줄바꿈 필수
- [ ] 줄 끝 공백: 모든 라인에서 제거
- [ ] import 문: 3개 이상이면 여러 줄로 작성

### Vue Template
- [ ] 태그와 속성 끝에 trailing spaces 없음
- [ ] 여러 속성은 각 줄에 하나씩 작성
- [ ] v-for와 :key 함께 사용 필수
- [ ] v-if와 v-for 동시 사용 금지

### 코드 제출 전 필수 체크
```bash
# 1. ESLint 자동 수정 실행
npm run lint:fix

# 2. 수정되지 않은 오류 확인
npm run lint

# 3. 수동으로 남은 오류 수정

# 4. 최종 확인
npm run lint
```
````