import request from '@/utils/request';

export const getMatchConfig = () => request({
  url: '/match/config',
  method: 'get',
});

export const updateMatchConfig = (data: { cancelDaysBeforeMatch: number }) => request({
  url: '/match/admin/config',
  method: 'put',
  data,
});
