import request from '@/utils/request';

// 게스트 모집 목록 조회
export function getGuestRecruitmentList(params?: {
  region?: string
  date?: string
  status?: string
  page?: number
  size?: number
}) {
  return request({
    url: '/api/guest',
    method: 'get',
    params,
  });
}

// 게스트 모집 상세 조회
export function getGuestRecruitmentDetail(uid: string) {
  return request({
    url: `/api/guest/${uid}`,
    method: 'get',
  });
}

// 캘린더용 게스트 모집 조회
export function getGuestRecruitmentsByDateRange(startDate: string, endDate: string) {
  return request({
    url: '/api/guest/calendar',
    method: 'get',
    params: { startDate, endDate },
  });
}

// 게스트 모집 생성
export interface CreateGuestRecruitmentRequest {
  teamUid: string
  matchUid?: string
  matchDate: string
  matchTime: string
  stadiumName: string
  stadiumAddress?: string
  regionSido?: string
  regionSigungu?: string
  genderType?: number
  ageGroups?: number
  positionType?: 'FIELD' | 'GK' | 'ANY'
  maxGuests: number
  fee?: number
  guaranteedMinutes?: number
  additionalInfo?: string
}

export function createGuestRecruitment(data: CreateGuestRecruitmentRequest) {
  return request({
    url: '/api/guest',
    method: 'post',
    data,
  });
}

// 게스트 신청
export interface ApplyGuestRequest {
  recruitmentUid: string
  position?: string
  age?: number
  message?: string
}

export function applyAsGuest(data: ApplyGuestRequest) {
  return request({
    url: '/api/guest/apply',
    method: 'post',
    data,
  });
}

// 게스트 신청 처리
export interface ProcessGuestApplicationRequest {
  applicationUid: string
  approved: boolean
}

export function processGuestApplication(data: ProcessGuestApplicationRequest) {
  return request({
    url: '/api/guest/process',
    method: 'post',
    data,
  });
}

// 내 게스트 신청 목록 조회
export function getMyGuestApplications() {
  return request({
    url: '/api/guest/my-applications',
    method: 'get',
  });
}

// 모집의 신청자 목록 조회
export function getRecruitmentApplications(recruitmentUid: string) {
  return request({
    url: `/api/guest/${recruitmentUid}/applications`,
    method: 'get',
  });
}

// 게스트 모집 취소
export function cancelGuestRecruitment(recruitmentUid: string) {
  return request({
    url: `/api/guest/${recruitmentUid}`,
    method: 'delete',
  });
}

// 게스트 모집 타입 정의
export interface GuestRecruitment {
  uid: string
  teamUid: string
  teamName: string
  teamLogoUrl?: string
  teamMannerScore?: number
  matchDate: string
  matchTime: string
  stadiumName: string
  stadiumAddress?: string
  region: string
  positionType: 'FIELD' | 'GK' | 'ANY'
  maxGuests: number
  currentGuests: number
  fee: number
  guaranteedMinutes?: number
  additionalInfo?: string
  status: 'RECRUITING' | 'COMPLETED' | 'CANCELLED' | 'EXPIRED'
}

export interface GuestApplication {
  uid: string
  recruitmentUid: string
  userUid: string
  userName?: string
  position?: string
  age?: number
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED'
  paymentCompleted: boolean
  createdDate: string
}
