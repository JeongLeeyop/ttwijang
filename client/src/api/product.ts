import request from '@/utils/request';

const PATH = '/product';

export const getProductList = (listQuery: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...listQuery,
    page: listQuery.page - 1,
  },
});

export const getProductDetail = (productIdx: number) => request({
  url: `${PATH}/${productIdx}`,
  method: 'get',
});

// Package APIs
const PACKAGE_PATH = '/product/package';

export const getPackageList = (listQuery: any) => request({
  url: PACKAGE_PATH,
  method: 'get',
  params: {
    ...listQuery,
    page: listQuery.page - 1,
  },
});

export const getPackageDetail = (packageIdx: number) => request({
  url: `${PACKAGE_PATH}/${packageIdx}`,
  method: 'get',
});
