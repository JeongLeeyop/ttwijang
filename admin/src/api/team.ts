import request from '@/utils/request';

const PATH = '/team';

export const getTeamList = (params?: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getTeam = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});
