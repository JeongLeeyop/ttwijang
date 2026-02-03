import request from '@/utils/request';

// 매너 평가 제출
export interface SubmitMannerRatingRequest {
  ratedUserUid: string
  matchUid?: string
  guestRecruitmentUid?: string
  ratingType: 'MATCH' | 'GUEST'
  score: number
  comment?: string
}

export function submitMannerRating(data: SubmitMannerRatingRequest) {
  return request({
    url: '/api/manner-rating',
    method: 'post',
    data,
  });
}

// 사용자 매너 점수 조회
export function getUserMannerScore(userUid: string) {
  return request({
    url: `/api/manner-rating/user/${userUid}`,
    method: 'get',
  });
}

// 팀 매너 점수 조회
export function getTeamMannerScore(teamUid: string) {
  return request({
    url: `/api/manner-rating/team/${teamUid}`,
    method: 'get',
  });
}

// 내 평가 목록 조회 (내가 받은 평가)
export function getMyRatings() {
  return request({
    url: '/api/manner-rating/my',
    method: 'get',
  });
}

// 타입 정의
export interface MannerRating {
  uid: string
  raterUid: string
  raterName?: string
  ratedUserUid: string
  matchUid?: string
  guestRecruitmentUid?: string
  ratingType: 'MATCH' | 'GUEST'
  score: number
  comment?: string
  createdDate: string
}

export interface UserMannerScore {
  userUid: string
  averageScore: number
  totalRatings: number
}

export interface TeamMannerScore {
  teamUid: string
  averageScore: number
  totalRatings: number
}
