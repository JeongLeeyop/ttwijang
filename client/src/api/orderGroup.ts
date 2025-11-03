import request from '@/utils/request';

const PATH = '/product/order/group';

export const getOrderGroup = (idx: number) => request({
  url: `${PATH}/${idx}`,
  method: 'get',
});

export const getOrderGroupList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});
