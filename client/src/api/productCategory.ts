import request from '@/utils/request';

const PATH = '/product/category';

export const getProductCategoryList = () => request({
  url: PATH,
  method: 'get',
});
