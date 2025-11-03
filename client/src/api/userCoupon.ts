import request from '@/utils/request';

const PATH = '/user/coupon';

export const getUserCouponList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getUsableCouponList = () => request({
  url: `${PATH}/usable`,
  method: 'get',
});

export const donwloadUserCoupon = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'post',
});
