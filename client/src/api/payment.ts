import request from '@/utils/request';

export interface PrepareResponse {
  orderId: string
  orderName: string
  amount: number
}

export function preparePayment(data: { amount: number, orderName?: string }) {
  return request({
    url: '/client/payment/prepare',
    method: 'post',
    data,
  }) as Promise<{ data: PrepareResponse }>;
}
