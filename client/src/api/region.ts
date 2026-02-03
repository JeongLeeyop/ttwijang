import request from '@/utils/request';

// 시/도 목록 조회
export function getSidoList() {
  return request({
    url: '/api/region/sido',
    method: 'get',
  });
}

// 시/군/구 목록 조회
export function getSigunguList(sidoCode: string) {
  return request({
    url: `/api/region/sigungu/${sidoCode}`,
    method: 'get',
  });
}

// 전체 지역 목록 조회
export function getAllRegions() {
  return request({
    url: '/api/region',
    method: 'get',
  });
}

// 타입 정의
export interface Region {
  code: string
  name: string
  type: 'SIDO' | 'SIGUNGU'
  parentCode?: string
}
