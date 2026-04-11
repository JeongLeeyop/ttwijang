import request from '@/utils/request';

const PATH = '/regions';
const ADMIN_PATH = '/admin/regions';

export const getSidoList = () => request({
  url: `${PATH}/sido`,
  method: 'get',
});

export const getSigunguList = (parentCode: string) => request({
  url: `${PATH}/sigungu`,
  method: 'get',
  params: { parentCode },
});

export const createSido = (data: { code: string; name: string; sortOrder?: number }) =>
  request({ url: `${ADMIN_PATH}/sido`, method: 'post', data });

export const createSigungu = (data: { code: string; name: string; parentCode: string; sortOrder?: number }) =>
  request({ url: `${ADMIN_PATH}/sigungu`, method: 'post', data });

export const getRegionLeagues = (sidoCode: string) =>
  request({ url: `${ADMIN_PATH}/${sidoCode}/leagues`, method: 'get' });

export const getRegionTeams = (sidoCode: string) =>
  request({ url: `${ADMIN_PATH}/${sidoCode}/teams`, method: 'get' });
