import request from '@/utils/request';

const PATH = '/newAlarm';

export const getNewAlarmCount = () => request({
  url: `${PATH}/count`,
  method: 'get',
});

export const checkNewAlarm = (id: String) => request({
  url: `${PATH}/check/${id}`,
  method: 'put',
});
