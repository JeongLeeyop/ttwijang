import request from '@/utils/request';

const PATH = '/point/history';

export const getPointHistoryList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});
