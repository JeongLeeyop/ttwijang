# CLAUDE.md

이 프로젝트는 **Spring Boot(Java 11) 백엔드 + Vue 2(TypeScript) 프론트엔드** 풀스택 프로젝트입니다.

---

## 규칙 파일 참조

| 영역 | 파일 |
|------|------|
| 프론트엔드 (Vue 2, Vuex, SCSS, 에러 처리) | [`rules/frontend.md`](rules/frontend.md) |
| 백엔드 (Spring Boot, MySQL, JPA, 보안) | [`rules/backend.md`](rules/backend.md) |
| 코드 품질 / ESLint / 검증 체크리스트 | [`rules/validation.md`](rules/validation.md) |

---

## 프로젝트 구조 개요

```
/
├── client/          # Vue 2 프론트엔드
├── src/             # Spring Boot 백엔드
├── rules/           # 코딩 규칙 (frontend / backend / validation)
```

---

## 작업 시 필수 참고 자료

- **요구사항**: `.github/request.md`, `request2.md`, `request3.md` — 관점별로 작성됨, 비교하며 통합
- **디자인 시안**: `.github/img/` — 페이지별 스크린샷
- **마인드맵**: `.github/NotebookLM Mind Map.png`

---

## 핵심 원칙 요약

### 프론트엔드
- Vue Class Component 스타일, 순서: Props → Data → Computed → Watch → Lifecycle → Methods
- Vuex Mutation: `UPPER_SNAKE_CASE` (동기), Action: `camelCase` (비동기)
- SCSS는 BEM 방식, Element UI 오버라이드는 `::v-deep` 사용
- **뷰포트 메타 태그 수정 금지**

### 백엔드
- Spring Boot 2.5.4 / Java 11 / MySQL / JPA + MyBatis + QueryDSL
- JWT + OAuth2 + Spring Security 인증 구조

### 코드 품질
- ESLint 자동 수정: `npm run lint:fix` → `npm run lint` 순으로 확인
- 인터페이스 여러 줄: 구분자 없음 / 한 줄: 쉼표
- camelCase 강제, trailing spaces 금지
- import 3개 이상: 여러 줄로 작성
