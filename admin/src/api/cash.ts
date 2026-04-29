import request from '@/utils/request';

const PATH = '/admin/cash';

export const getAdminUserWallet = (userUid: string) => request({
  url: `${PATH}/wallet/${userUid}`,
  method: 'get',
});

export const adminAdjustPoint = (data: {
  userUid: string
  amount: number
  type: 'EARN' | 'USE'
  description?: string
}) => request({
  url: `${PATH}/adjust`,
  method: 'post',
  data,
});

export const getAdminUserTransactions = (userUid: string, params?: {
  page?: number
  size?: number
}) => request({
  url: `${PATH}/transactions/${userUid}`,
  method: 'get',
  params: params ? { ...params, page: params.page ? params.page - 1 : 0 } : undefined,
});
