import request from '@/utils/request';

const PATH = '/diary/meal';

export const getLastDiaryMealList = (params: any) => request({
  url: `${PATH}/last`,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});
