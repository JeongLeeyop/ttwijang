import request from '@/utils/request';

// 매치 목록 조회
export function getMatchList(params?: {
  date?: string
  status?: string
  regionCode?: string
  region?: string
  regionSido?: string
  regionSigungu?: string
  matchType?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/match',
    method: 'get',
    params,
  });
}

// 매치 상세 조회
export function getMatchDetail(uid: string) {
  return request({
    url: `/match/${uid}`,
    method: 'get',
  });
}

// 캘린더용 매치 조회
export function getMatchesByDateRange(startDate: string, endDate: string, params?: {
  regionCode?: string
  regionSido?: string
  regionSigungu?: string
}) {
  return request({
    url: '/match/calendar',
    method: 'get',
    params: { startDate, endDate, ...params },
  });
}

// 매치 생성
export interface CreateMatchRequest {
  hostTeamUid: string
  matchType: 'FRIENDLY' | 'FREE'
  matchFormat: 'FOUR_VS_FOUR' | 'FIVE_VS_FIVE' | 'SIX_VS_SIX' | 'SEVEN_VS_SEVEN'
  matchDate: string
  matchTime: string
  stadiumName: string
  stadiumAddress?: string
  regionSido?: string
  regionSigungu?: string
  fee?: number
  description?: string
  genderType?: number
  ageGroups?: number
}

export function createMatch(data: CreateMatchRequest) {
  return request({
    url: '/match',
    method: 'post',
    data,
  });
}

// 매치 신청
export interface ApplyMatchRequest {
  matchUid: string
  applicantTeamUid: string
  message?: string
}

export function applyToMatch(data: ApplyMatchRequest) {
  return request({
    url: '/match/apply',
    method: 'post',
    data,
  });
}

// 매치 신청 취소
export function cancelMatchApplication(matchUid: string) {
  return request({
    url: `/match/${matchUid}/cancel`,
    method: 'post',
  });
}

// 매치 삭제
export function deleteMatch(uid: string) {
  return request({
    url: `/match/${uid}`,
    method: 'delete',
  });
}

// 내 팀 매치 목록 조회
export function getMyTeamMatches(teamUid: string, params?: {
  matchType?: string
  page?: number
  size?: number
}) {
  return request({
    url: `/match/team/${teamUid}`,
    method: 'get',
    params,
  });
}

// 매치 타입 정의
export interface CompleteMatchRequest {
  matchUid: string
  homeScore?: number
  awayScore?: number
}

export function completeMatch(data: CompleteMatchRequest) {
  return request({
    url: '/match/complete',
    method: 'post',
    data,
  });
}

// 팀 매너 점수 평가
export interface RateTeamRequest {
  matchUid: string
  ratedTeamUid: string
  score: number
  comment?: string
}

export function rateTeamManner(data: RateTeamRequest) {
  return request({
    url: '/manner-rating/team',
    method: 'post',
    data,
  });
}

export interface FutsalMatch {
  uid: string
  hostTeamUid: string
  hostTeamName: string
  hostTeamLogoUrl?: string
  opponentTeamUid?: string
  opponentTeamName?: string
  opponentTeamLogoUrl?: string
  matchType: 'FRIENDLY' | 'FREE'
  matchFormat: 'FOUR_VS_FOUR' | 'FIVE_VS_FIVE' | 'SIX_VS_SIX' | 'SEVEN_VS_SEVEN'
  matchDate: string
  matchTime: string
  stadiumName: string
  stadiumAddress?: string
  region: string
  fee?: number
  status: 'RECRUITING' | 'MATCHED' | 'COMPLETED' | 'CANCELLED'
  genderType?: number
  ageGroups?: number
  description?: string
}
