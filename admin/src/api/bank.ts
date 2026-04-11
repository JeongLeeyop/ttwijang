import request from '@/utils/request';

const PATH = '/bank';

export const getBankList = () => request({
  url: PATH,
  method: 'get',
});
