import request from '@/utils/request';

const PATH = '/post-like';

export const likePost = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'post',
});
