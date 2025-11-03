import request from '@/utils/request';

const PATH = '/fcm-token';

export const saveFcmToken = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const deleteFcmToken = (token: any) => request({
  url: `${PATH}/${token}`,
  method: 'delete',
});
