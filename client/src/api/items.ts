import request from '@/utils/request';

const PATH = '/items';

export const getItemsList = (params: any) => request({
  url: PATH,
  method: 'get',
  params,
});
