import request from '@/utils/request';

// 지갑 조회
export function getWallet() {
  return request({
    url: '/api/cash/wallet',
    method: 'get',
  });
}

// 캐시 충전
export interface ChargeRequest {
  amount: number
  paymentMethod?: string
  paymentReferenceId?: string
}

export function chargeCash(data: ChargeRequest) {
  return request({
    url: '/api/cash/charge',
    method: 'post',
    data,
  });
}

// 캐시 사용
export interface UseRequest {
  amount: number
  description?: string
  referenceUid?: string
}

export function useCash(data: UseRequest) {
  return request({
    url: '/api/cash/use',
    method: 'post',
    data,
  });
}

// 거래 내역 조회
export function getTransactions(params?: {
  page?: number
  size?: number
}) {
  return request({
    url: '/api/cash/transactions',
    method: 'get',
    params,
  });
}

// 기간별 거래 내역 조회
export function getTransactionsByDateRange(startDate: string, endDate: string, params?: {
  page?: number
  size?: number
}) {
  return request({
    url: '/api/cash/transactions/range',
    method: 'get',
    params: { startDate, endDate, ...params },
  });
}

// 팀 후원하기
export interface SponsorshipRequest {
  teamUid: string
  sponsorshipType: 'OWNER' | 'ONE_TIME' | 'REGULAR'
  amount?: number
  message?: string
}

export function sponsorTeam(data: SponsorshipRequest) {
  return request({
    url: '/api/cash/sponsorship',
    method: 'post',
    data,
  });
}

// 팀 후원 목록 조회
export function getTeamSponsorships(teamUid: string) {
  return request({
    url: `/api/cash/sponsorship/team/${teamUid}`,
    method: 'get',
  });
}

// 내 후원 목록 조회
export function getMySponsorships() {
  return request({
    url: '/api/cash/sponsorship/my',
    method: 'get',
  });
}

// 팀 후원 요약 조회
export function getTeamSponsorshipSummary(teamUid: string) {
  return request({
    url: `/api/cash/sponsorship/team/${teamUid}/summary`,
    method: 'get',
  });
}

// 타입 정의
export interface Wallet {
  uid: string
  userUid: string
  balance: number
  totalCharged: number
  totalUsed: number
  lastChargedDate?: string
}

export interface Transaction {
  uid: string
  walletUid: string
  transactionType: 'CHARGE' | 'USE' | 'REFUND' | 'EARN' | 'CANCEL'
  amount: number
  balanceAfter: number
  description?: string
  createdDate: string
}

export interface Sponsorship {
  uid: string
  teamUid: string
  teamName: string
  sponsorUid: string
  sponsorName?: string
  sponsorshipType: 'OWNER' | 'ONE_TIME' | 'REGULAR'
  amount?: number
  totalAmount?: number
  status: 'ACTIVE' | 'CANCELLED' | 'EXPIRED'
  message?: string
  createdDate: string
}

export interface TeamSponsorshipSummary {
  teamUid: string
  teamName: string
  ownerCount: number
  regularSponsorCount: number
  oneTimeSponsorCount: number
  totalSponsorshipAmount: number
}
