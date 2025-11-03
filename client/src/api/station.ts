import request from '@/utils/request';

const PATH = 'station';

export const getStationList = () => request({
  url: PATH,
  method: 'get',
});

export const getStationDetail = (id: any) => request({
  url: `${PATH}/${id}`,
  method: 'get',
});
