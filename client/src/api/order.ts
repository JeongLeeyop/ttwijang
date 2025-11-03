import request from '@/utils/request';

const PATH = '/product/order';

export const getOrderList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getRemainCount = () => request({
  url: `${PATH}/remain/count`,
  method: 'get',
});

export const getOrderCount = (id: any, data: any) => request({
  url: `${PATH}/shop/${id}/count`,
  method: 'post',
  data: {
    ...data,
    shopIdx: id,
  },
});

export const getOrder = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const getCancelOrder = (data: any) => request({
  url: `${PATH}/refund/${data.orderId}`,
  method: 'put',
  data: {
    week: Number(data.week),
    allStatus: data.allStatus,
    cancelReason: data.cancelReason,
    cancelReasonType: data.cancelReasonType,
  },
});
