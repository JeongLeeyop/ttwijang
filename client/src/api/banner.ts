import request from '@/utils/request';

// 배너 생성 (관리자)
export function createBanner(data: {
  title: string
  imageUrl: string
  linkUrl?: string
  displayOrder?: number
  status?: 'ACTIVE' | 'INACTIVE'
  startDate: string
  endDate: string
  regionSido?: string
  regionSigungu?: string
  targetPage?: 'HOME' | 'LEAGUE' | 'MATCH' | 'TEAM' | 'ALL'
}) {
  return request({
    url: '/banner',
    method: 'post',
    data,
  });
}

// 배너 수정 (관리자)
export function updateBanner(uid: string, data: {
  title?: string
  imageUrl?: string
  linkUrl?: string
  displayOrder?: number
  status?: 'ACTIVE' | 'INACTIVE'
  startDate?: string
  endDate?: string
  regionSido?: string
  regionSigungu?: string
  targetPage?: 'HOME' | 'LEAGUE' | 'MATCH' | 'TEAM' | 'ALL'
}) {
  return request({
    url: `/banner/${uid}`,
    method: 'put',
    data,
  });
}

// 배너 삭제 (관리자)
export function deleteBanner(uid: string) {
  return request({
    url: `/banner/${uid}`,
    method: 'delete',
  });
}

// 배너 상세 조회
export function getBannerDetail(uid: string) {
  return request({
    url: `/banner/${uid}`,
    method: 'get',
  });
}

// 배너 목록 조회 (관리자)
export function getBannerList(params?: {
  status?: 'ACTIVE' | 'INACTIVE'
  page?: number
  size?: number
}) {
  return request({
    url: '/banner',
    method: 'get',
    params,
  });
}

// 활성화된 배너 조회 (사용자)
export function getActiveBanners(params?: {
  targetPage?: 'HOME' | 'LEAGUE' | 'MATCH' | 'TEAM' | 'ALL'
  regionSigungu?: string
}) {
  return request({
    url: '/banner/active',
    method: 'get',
    params,
  });
}
