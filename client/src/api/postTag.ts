import request from '@/utils/request';

const PATH = '/post-tag';

export const getBestTagList = () => request({
  url: `${PATH}/best`,
  method: 'get',
});
