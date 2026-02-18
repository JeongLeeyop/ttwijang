import request from '@/utils/request';

const PATH = '/client/comment';

// 댓글 목록 조회 (게시글별)
export const getComments = (postUid: string, params?: {
  page?: number
  size?: number
}) => request({
  url: PATH,
  method: 'get',
  params: {
    postUid,
    ...params,
  },
});

// 댓글 작성
export interface CommentAdd {
  postUid: string
  contents: string
  parentUid?: string
}

export const addComment = (data: CommentAdd) => request({
  url: PATH,
  method: 'post',
  data,
});

// 댓글 수정
export interface CommentUpdate {
  contents: string
}

export const updateComment = (commentUid: string, data: CommentUpdate) => request({
  url: `${PATH}/${commentUid}`,
  method: 'put',
  data,
});

// 댓글 삭제
export const deleteComment = (commentUid: string) => request({
  url: `${PATH}/${commentUid}`,
  method: 'delete',
});
