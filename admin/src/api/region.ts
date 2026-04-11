import request from '@/utils/request';

const PATH = '/regions';

export const getSidoList = () => request({
  url: `${PATH}/sido`,
  method: 'get',
});

export const getSigunguList = (parentCode: string) => request({
  url: `${PATH}/sigungu`,
  method: 'get',
  params: { parentCode },
});
