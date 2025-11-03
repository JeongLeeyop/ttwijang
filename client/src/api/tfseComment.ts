import request from '@/utils/request';

const PATH = '/tfse/comment';

export const getCommentList = (listQuery: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...listQuery,
    page: listQuery.page - 1,
  },
});

export const addComment = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateComment = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data: {
    ...data,
  },
});

export const deleteComment = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});
