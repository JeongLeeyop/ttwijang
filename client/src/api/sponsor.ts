import request from '@/utils/request';

export function getTeamBanners(teamUid: string) {
  return request({
    url: `/sponsor/team-banners/team/${teamUid}`,
    method: 'get',
  });
}
