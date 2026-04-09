# CLAUDE.md

이 프로젝트는 **Spring Boot(Java 11) 백엔드 + Vue 2(TypeScript) 프론트엔드** 풀스택 풋살 매칭 플랫폼입니다.

---

## 영역별 규칙 위치

| 영역 | CLAUDE.md 위치 |
|------|---------------|
| 백엔드 공통 | `backend/CLAUDE.md` |
| 프론트엔드 공통 | `client/CLAUDE.md` |
| Team 도메인 | `backend/src/main/java/com/ttwijang/cms/api/team/CLAUDE.md` |
| League 도메인 | `backend/src/main/java/com/ttwijang/cms/api/league/CLAUDE.md` |
| Match 도메인 | `backend/src/main/java/com/ttwijang/cms/api/match/CLAUDE.md` |

> Claude는 작업 중인 파일의 디렉토리부터 루트까지 CLAUDE.md를 자동으로 모두 로딩합니다.

---

## 필수 참고 자료

- **요구사항**: `.claude/request.md`, `request2.md`, `request3.md` — 관점별로 작성됨, 비교하며 통합
- **디자인 시안**: `.github/img/` — 페이지별 스크린샷
- **마인드맵**: `.github/NotebookLM Mind Map.png`

---

## 프로젝트 구조

```
/
├── client/          # Vue 2 프론트엔드
├── backend/         # Spring Boot 백엔드
```

---

## 도메인 CLAUDE.md 관리 규칙 (Claude 필독)

백엔드 도메인 디렉토리(`api/{domain}/`)에서 작업할 때 아래를 반드시 수행합니다.

### 1. 작업 시작 시 — 파일 존재 여부 확인
해당 도메인에 `CLAUDE.md`가 없으면 **작업 전에 먼저 생성**합니다.

생성 내용 (서비스 파일을 읽고 추출):
- 비즈니스 규칙 (BR-XX 코드, 주석, 조건 분기)
- 상태 흐름 (enum Status 값과 전이 조건)
- 권한 규칙 (누가 무엇을 할 수 있는가)
- 관련 엔티티 목록
- 주요 서비스 메서드 요약
- 미완성/주석 처리된 기능 (TODO로 표시)

### 2. 작업 중 — 발견한 규칙 즉시 반영
코드를 읽거나 수정하면서 아래를 발견하면 CLAUDE.md를 업데이트합니다:
- 새로운 비즈니스 규칙 또는 제약 조건
- 상태 흐름 변경
- 미완성 기능 (주석 처리된 로직, TODO)
- 도메인 간 의존성 (다른 Service 주입)

### 3. 기능 구현 완료 시 — TODO 체크
구현한 기능이 CLAUDE.md의 TODO 항목이라면 완료로 표시하거나 삭제합니다.

### 도메인 CLAUDE.md 템플릿

```markdown
# {Domain} 도메인

## 비즈니스 규칙
- BR-XX: ...

## 관련 엔티티
- Entity1, Entity2

## 상태 흐름
STATUS_A → STATUS_B (조건)

## 권한 규칙
- 운영자(OWNER)만 ...

## 주요 서비스 메서드
| 메서드 | 설명 |
|--------|------|
| methodName | ... |

## 미완성 기능 (TODO)
- [ ] 기능명 — 이유/현황
```
