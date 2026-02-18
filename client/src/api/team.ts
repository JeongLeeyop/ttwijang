import request from '@/utils/request';

// 팀 목록 조회
export function getTeamList(params?: {
  regionCode?: string
  region?: string
  regionSido?: string
  regionSigungu?: string
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
  logoFileUid?: string
  establishedYear?: number
  activeDays?: number
  activeTimeSlots?: number
  regionSido?: string
  regionSigungu?: string
  homeStadium?: string
  homeStadiumAddress?: string
  ageGroups?: number
  genderType?: number
  skillLevel?: number
  bankName?: string
  bankAccount?: string
  monthlyFee?: number
  featureTags?: string
  recruitingMembers?: boolean
  recruitmentDescription?: string
  teamPhotoFileUid?: string
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
    url: '/team/join/process',
    method: 'post',
    data,
  });
}

// 팀 가입 대기 목록 조회 (운영자용)
export function getPendingRequests(teamUid: string) {
  return request({
    url: `/team/${teamUid}/pending`,
    method: 'get',
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

// BR-01/BR-02: 팀 가입/생성 가능 여부 확인
export interface MembershipStatus {
  hasTeam: boolean
  hasPendingRequest: boolean
  hasCreatedTeam: boolean
  canJoinTeam: boolean
  canCreateTeam: boolean
}

export function checkMembershipStatus() {
  return request({
    url: '/team/membership-status',
    method: 'get',
  });
}

// 초대 코드로 팀 가입
export function joinTeamByCode(teamCode: string) {
  return request({
    url: `/team/join/code/${teamCode}`,
    method: 'post',
  });
}

// 회원 모집 설정 저장
export interface RecruitmentRequest {
  featureTags?: string
  activeDays?: number
  activeTimeSlots?: number
  regionSido?: string
  regionSigungu?: string
  monthlyFee?: number
  genderType?: number
  ageGroups?: number
  teamPhotoFileUid?: string
  recruitmentDescription?: string
}

export function saveRecruitment(teamUid: string, data: RecruitmentRequest) {
  return request({
    url: `/team/${teamUid}/recruitment`,
    method: 'post',
    data,
  });
}

// 회원 모집 중인 팀 목록 조회
export function getRecruitingTeams(params?: {
  regionCode?: string
  regionSido?: string
  regionSigungu?: string
  genderType?: number
  ageGroups?: number
  activeDays?: number
  activeTimeSlots?: number
  featureTag?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/team/recruiting',
    method: 'get',
    params,
  });
}

// 회원 모집 종료
export function stopRecruitment(teamUid: string) {
  return request({
    url: `/team/${teamUid}/recruitment/stop`,
    method: 'post',
  });
}

// 팀 사진 업로드
export function uploadTeamPhoto(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: '/team/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}

// 내 가입 대기 정보 조회
export interface PendingInfo {
  memberUid: string
  teamUid: string
  teamName: string
  teamCode: string
  teamLogoUrl: string | null
  ownerName: string | null
  memberCount: number
  region: string
  position: string | null
  appliedDate: string
}

export function getMyPendingInfo() {
  return request({
    url: '/team/my/pending',
    method: 'get',
  });
}

// 가입 신청 취소
export function cancelJoinRequest() {
  return request({
    url: '/team/my/pending',
    method: 'delete',
  });
}
