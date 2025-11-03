import request from '@/utils/request';

const PATH = '/product/rotation';

export const getProductDeatil = (productIdx: number) => request({
  url: `${PATH}/${productIdx}`,
  method: 'get',
});

export const getProductRotationList = (weekNum: number) => request({
  url: `${PATH}/${weekNum}`,
  method: 'get',
});
