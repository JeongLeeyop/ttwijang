import request from '@/utils/request';

const PATH = '/shop';

export const getShopList = () => request({
  url: PATH,
  method: 'get',
});

export const getShopPickupTimes = (idx: number) => request({
  url: `${PATH}/${idx}/pickup/time`,
  method: 'get',
});

export const getShopDetail = (id: any) => request({
  url: `${PATH}/${id}`,
  method: 'get',
});
