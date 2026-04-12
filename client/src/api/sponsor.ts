import request from '@/utils/request';

// 구단주 기준 금액 조회
export function getSponsorFee() {
  return request({ url: '/sponsor/fee', method: 'get' });
}

export function getTeamBanners(teamUid: string) {
  return request({
    url: `/sponsor/team-banners/team/${teamUid}`,
    method: 'get',
  });
}
