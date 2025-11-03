import request from '@/utils/request';

const PATH = '/tfse';

export const getTfse = (tfseIdx: String) => request({
  url: `${PATH}/${tfseIdx}`,
  method: 'get',
});

export const getTfseList = (params: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getTfseCommunityList = (params: any) => request({
  url: `${PATH}/community`,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const addTfse = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const delTfse = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'delete',
});

export const updateSecretStatus = (data: any) => request({
  url: PATH,
  method: 'put',
  data,
});

export const getFeedback = (params: any) => request({
  url: `${PATH}/selfFeedback`,
  method: 'get',
  params,
});

export const addFeedback = (data: any) => request({
  url: `${PATH}/selfFeedback`,
  method: 'post',
  data,
});
