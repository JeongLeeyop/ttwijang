import request from '@/utils/request';

const PATH = '/tfse/like';

export const likeTfse = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'post',
});
