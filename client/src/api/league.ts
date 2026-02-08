import request from '@/utils/request';

// 리그 목록 조회 (BR-04: 지역별 리그 전환)
export function getLeagueList(params?: {
  regionCode?: string
  regionSido?: string
  regionSigungu?: string
  region?: string
  status?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/league',
    method: 'get',
    params,
  });
}

// 리그 상세 조회
export function getLeagueDetail(uid: string) {
  return request({
    url: `/league/${uid}`,
    method: 'get',
  });
}

// 리그 순위표 조회
export function getLeagueStandings(leagueUid: string) {
  return request({
    url: `/league/${leagueUid}/standings`,
    method: 'get',
  });
}

// 리그 일정 조회 (월별)
export function getLeagueSchedule(leagueUid: string, params?: {
  year?: number
  month?: number
  round?: number
  startDate?: string
  endDate?: string
  page?: number
  size?: number
}) {
  return request({
    url: `/league/${leagueUid}/schedule`,
    method: 'get',
    params,
  });
}

// 리그 참가 신청
export function applyToLeague(leagueUid: string, teamUid: string) {
  return request({
    url: `/league/${leagueUid}/apply`,
    method: 'post',
    data: { teamUid },
  });
}

// 리그 경기 결과 입력 (관리자)
export interface UpdateMatchResultRequest {
  matchUid: string
  homeScore: number
  awayScore: number
}

export function updateMatchResult(data: UpdateMatchResultRequest) {
  return request({
    url: '/league/match/result',
    method: 'post',
    data,
  });
}

// 리그 참여 팀 목록 조회
export function getLeagueTeams(leagueUid: string) {
  return request({
    url: `/league/${leagueUid}/teams`,
    method: 'get',
  });
}

// 내 팀의 리그 정보 조회
export function getMyTeamLeagueInfo(teamUid: string) {
  return request({
    url: `/league/my-team/${teamUid}`,
    method: 'get',
  });
}

// 리그 타입 정의
export interface League {
  uid: string
  name: string
  season: string
  regionSido: string
  regionSigungu?: string
  status: 'RECRUITING' | 'IN_PROGRESS' | 'COMPLETED'
  maxTeams: number
  currentTeams: number
  startDate?: string
  endDate?: string
  description?: string
}

export interface LeagueTeamResponse {
  teamUid: string
  teamName: string
  teamLogoUrl?: string
  leagueName: string
  ranking?: number
  points?: number
}

export interface LeagueStanding {
  teamUid: string
  teamName: string
  teamLogoUrl?: string
  rank: number
  points: number
  matchesPlayed: number
  wins: number
  draws: number
  losses: number
  goalsFor: number
  goalsAgainst: number
  goalDifference: number
}

export interface LeagueMatch {
  uid: string
  leagueUid: string
  round: number
  homeTeamUid: string
  homeTeamName: string
  homeTeamLogoUrl?: string
  awayTeamUid: string
  awayTeamName: string
  awayTeamLogoUrl?: string
  homeScore?: number
  awayScore?: number
  matchDate: string
  matchTime?: string
  stadiumName?: string
  status: 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED'
}

// ==================== 최고관리자 전용 API (BR-11, BR-12, BR-13) ====================

// [관리자] 리그 생성
export interface CreateLeagueRequest {
  name: string
  description?: string
  season?: string
  startDate?: string
  endDate?: string
  regionSido: string
  regionSigungu?: string
  maxTeams?: number
  rules?: string
  bannerFileUid?: string
}

export function createLeague(data: CreateLeagueRequest) {
  return request({
    url: '/league/admin',
    method: 'post',
    data,
  });
}

// [관리자] 리그 수정
export interface UpdateLeagueRequest {
  name?: string
  description?: string
  season?: string
  startDate?: string
  endDate?: string
  maxTeams?: number
  status?: string
  rules?: string
  bannerFileUid?: string
}

export function updateLeague(uid: string, data: UpdateLeagueRequest) {
  return request({
    url: `/league/admin/${uid}`,
    method: 'put',
    data,
  });
}

// [관리자] 리그 삭제
export function deleteLeague(uid: string) {
  return request({
    url: `/league/admin/${uid}`,
    method: 'delete',
  });
}

// [관리자] 리그에 팀 추가 (BR-12)
export function addTeamToLeague(leagueUid: string, teamUid: string) {
  return request({
    url: `/league/admin/${leagueUid}/teams/${teamUid}`,
    method: 'post',
  });
}

// [관리자] 리그에서 팀 제거 (BR-12)
export function removeTeamFromLeague(leagueUid: string, teamUid: string) {
  return request({
    url: `/league/admin/${leagueUid}/teams/${teamUid}`,
    method: 'delete',
  });
}

// [관리자] 리그 매치 생성
export interface CreateLeagueMatchRequest {
  leagueUid: string
  homeTeamUid: string
  awayTeamUid: string
  matchDate: string
  matchTime?: string
  durationMinutes?: number
  stadiumName?: string
  stadiumAddress?: string
  round?: number
}

export function createLeagueMatch(data: CreateLeagueMatchRequest) {
  return request({
    url: '/league/admin/match',
    method: 'post',
    data,
  });
}
