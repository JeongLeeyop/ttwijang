import request from '@/utils/request';

// 리그 목록 조회
export function getLeagueList(params?: {
  region?: string
  grade?: string
  status?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/api/league',
    method: 'get',
    params,
  });
}

// 리그 상세 조회
export function getLeagueDetail(uid: string) {
  return request({
    url: `/api/league/${uid}`,
    method: 'get',
  });
}

// 리그 순위표 조회
export function getLeagueStandings(leagueUid: string) {
  return request({
    url: `/api/league/${leagueUid}/standings`,
    method: 'get',
  });
}

// 리그 일정 조회
export function getLeagueSchedule(leagueUid: string, params?: {
  round?: number
  startDate?: string
  endDate?: string
  page?: number
  size?: number
}) {
  return request({
    url: `/api/league/${leagueUid}/schedule`,
    method: 'get',
    params,
  });
}

// 리그 참가 신청
export function applyToLeague(leagueUid: string, teamUid: string) {
  return request({
    url: `/api/league/${leagueUid}/apply`,
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
    url: '/api/league/match/result',
    method: 'post',
    data,
  });
}

// 리그 참여 팀 목록 조회
export function getLeagueTeams(leagueUid: string) {
  return request({
    url: `/api/league/${leagueUid}/teams`,
    method: 'get',
  });
}

// 내 팀의 리그 정보 조회
export function getMyTeamLeagueInfo(teamUid: string) {
  return request({
    url: `/api/league/my-team/${teamUid}`,
    method: 'get',
  });
}

// 리그 타입 정의
export interface League {
  uid: string
  name: string
  grade: 'A' | 'B' | 'C'
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
  leagueGrade: string
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
  status: 'SCHEDULED' | 'IN_PROGRESS' | 'FINISHED' | 'CANCELLED'
}
