import request from '@/utils/request';

const PATH = '/banner';

export const getBannerList = (params?: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getBanner = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const createBanner = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateBanner = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data,
});

export const deleteBanner = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});
