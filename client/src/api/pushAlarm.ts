import request from '@/utils/request';

const PATH = '/push-alarm';

export const getAlarmList = (params: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getAlarmCount = () => request({
  url: `${PATH}/count`,
  method: 'get',
});

export const deleteAlarm = (id: number) => request({
  url: `${PATH}/${id}`,
  method: 'delete',
});
