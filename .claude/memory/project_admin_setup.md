---
name: 뛰장 관리자 페이지 구축 현황
description: admin 프로젝트(Vue2+TS+ElementUI)와 백엔드 sponsor 도메인 구현 현황
type: project
---

관리자 페이지(admin/)가 기존 프로젝트 코드 기반으로 뛰장 전용으로 재구성되었음.

**Why:** 다른 프로젝트 admin 폴더를 가져와 불필요한 코드 제거 후 뛰장 6개 관리 도메인으로 재구성

**How to apply:** admin 추가 기능 개발 시 아래 구조를 유지

## 프론트엔드 admin 구조 (Vue2 + TypeScript + ElementUI)

| 메뉴 | 라우트 이름 | 파일 |
|------|-----------|------|
| 대시보드 | Dashboard | views/dashboard/index.vue |
| 지역 관리 | RegionList | views/region/index.vue |
| 리그 관리 | LeagueList | views/league/index.vue |
| 리그 팀 배정 | LeagueTeams | views/league/teams.vue |
| 유저 관리 | UserList | views/user/index.vue |
| 배너 관리 | BannerList | views/banner/index.vue |
| 구단주 금액 설정 | SponsorFee | views/sponsor/fee.vue |
| 팀별 배너 설정 | TeamBanner | views/sponsor/team-banner.vue |

## 백엔드 sponsor 도메인 (신규 구현)

- Entity: SponsorSetting (단일행, uid="default"), TeamSponsorBanner
- DB 마이그레이션: V024__Create_Sponsor_Tables.sql
- Admin API: POST/PUT/DELETE /api/admin/sponsor/** (@PreAuthorize ROLE_ADMIN)
- Public API: GET /api/sponsor/fee, GET /api/sponsor/team-banners/team/{teamUid}
- ddl-auto: update 활성화됨 (application.yml)
