import request from '@/utils/request';

// 팀 목록 조회
export function getTeamList(params?: {
  region?: string
  activityDay?: string
  ageGroup?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/team',
    method: 'get',
    params,
  });
}

// 내 팀 목록 조회
export function getMyTeams() {
  return request({
    url: '/team/my',
    method: 'get',
  });
}

// 팀 상세 조회
export function getTeamDetail(uid: string) {
  return request({
    url: `/team/${uid}`,
    method: 'get',
  });
}

// 팀 코드로 조회
export function getTeamByCode(teamCode: string) {
  return request({
    url: `/team/code/${teamCode}`,
    method: 'get',
  });
}

// 팀 생성
export interface CreateTeamRequest {
  name: string
  teamCode: string
  description?: string
  logoUrl?: string
  establishedYear?: number
  activityDays?: string
  activityTimes?: string
  regionSido?: string
  regionSigungu?: string
  homeStadium?: string
  ageGroups?: number
  genderType?: number
  skillLevel?: number
}

export function createTeam(data: CreateTeamRequest) {
  return request({
    url: '/team',
    method: 'post',
    data,
  });
}

// 팀 정보 수정
export interface UpdateTeamRequest extends Partial<CreateTeamRequest> {
  uid: string
}

export function updateTeam(data: UpdateTeamRequest) {
  return request({
    url: `/team/${data.uid}`,
    method: 'put',
    data,
  });
}

// 팀 삭제
export function deleteTeam(uid: string) {
  return request({
    url: `/team/${uid}`,
    method: 'delete',
  });
}

// 팀 가입 신청
export interface JoinTeamRequest {
  teamUid: string
  position?: string
  message?: string
}

export function joinTeam(data: JoinTeamRequest) {
  return request({
    url: '/team/join',
    method: 'post',
    data,
  });
}

// 팀 가입 신청 처리
export interface ProcessJoinRequest {
  memberUid: string
  approved: boolean
}

export function processJoinRequest(data: ProcessJoinRequest) {
  return request({
    url: '/team/process-join',
    method: 'post',
    data,
  });
}

// 팀 멤버 목록 조회
export function getTeamMembers(teamUid: string) {
  return request({
    url: `/team/${teamUid}/members`,
    method: 'get',
  });
}

// 팀 탈퇴
export function leaveTeam(teamUid: string) {
  return request({
    url: `/team/${teamUid}/leave`,
    method: 'post',
  });
}

// 운영자 위임
export function delegateOwner(teamUid: string, newOwnerUid: string) {
  return request({
    url: `/team/${teamUid}/delegate`,
    method: 'post',
    data: { newOwnerUid },
  });
}

// 팀 코드 중복 확인
export function checkTeamCode(teamCode: string) {
  return request({
    url: `/team/check-code/${teamCode}`,
    method: 'get',
  });
}

// 팀 검색
export function searchTeams(keyword: string) {
  return request({
    url: '/team/search',
    method: 'get',
    params: { keyword },
  });
}
