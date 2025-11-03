import request from '@/utils/request';

const PATH = '/review';

export const getReviewList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getMyReviewList = (params: any) => request({
  url: `${PATH}/own`,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getReviewDetail = (idx: any) => request({
  url: `${PATH}/${idx}/detail`,
  method: 'get',
});

export const addReview = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateReview = (idx:any, data: any) => request({
  url: `${PATH}/${idx}`,
  method: 'put',
  data,
});

export const deleteReview = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'delete',
});
