import request from '@/utils/request';

const PATH = '/review/like';

export const likeReview = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'post',
});
