import request from '@/utils/request';

const PATH = '/food';

export const getFoodList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});
