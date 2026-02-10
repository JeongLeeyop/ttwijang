import request from '@/utils/request';

const PATH = '/regions';

// 시/도 목록 조회
export function getSidoList() {
  return request({
    url: `${PATH}/sido`,
    method: 'get',
  });
}

// 시/군/구 목록 조회
export function getSigunguList(parentCode: string) {
  return request({
    url: `${PATH}/sigungu`,
    method: 'get',
    params: { parentCode },
  });
}

// 전체 지역 목록 조회
export function getAllRegions() {
  return request({
    url: '/region',
    method: 'get',
  });
}

// 타입 정의
export interface Region {
  uid: string
  code: string
  name: string
  parentCode: string
  level: number
  sortOrder: number
}
