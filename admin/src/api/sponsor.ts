import request from '@/utils/request';

const PATH = '/admin/sponsor';

export const getSponsorFee = () => request({
  url: `${PATH}/fee`,
  method: 'get',
});

export const updateSponsorFee = (data: { amount: number }) => request({
  url: `${PATH}/fee`,
  method: 'put',
  data,
});

export const getTeamBannerList = (params?: any) => request({
  url: `${PATH}/team-banners`,
  method: 'get',
  params,
});

export const createTeamBanner = (data: any) => request({
  url: `${PATH}/team-banners`,
  method: 'post',
  data,
});

export const updateTeamBanner = (uid: string, data: any) => request({
  url: `${PATH}/team-banners/${uid}`,
  method: 'put',
  data,
});

export const deleteTeamBanner = (uid: string) => request({
  url: `${PATH}/team-banners/${uid}`,
  method: 'delete',
});
