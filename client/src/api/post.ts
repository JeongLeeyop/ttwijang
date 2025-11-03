import { IPostAdd, IPostUpdate } from '@/types';
import request from '@/utils/request';

const PATH = '/post';

export const getPostList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getPostView = (postUid: string) => request({
  url: `${PATH}/${postUid}/view`,
  method: 'get',
});

export const getPost = (postUid: string) => request({
  url: `${PATH}/${postUid}`,
  method: 'get',
});

export const checkPostPassword = (postUid: string, secret: string) => request({
  url: `${PATH}/${postUid}/password-check`,
  method: 'get',
  params: {
    password: secret,
  },
});

export const addPost = (data: IPostAdd | IPostUpdate) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updatePost = (postUid: string, data: IPostUpdate) => request({
  url: `${PATH}/${postUid}`,
  method: 'put',
  data,
});

export const updateSecretPost = (postUid: string, data: IPostUpdate, secret: string) => request({
  url: `${PATH}/${postUid}`,
  method: 'put',
  data,
  params: {
    password: secret,
  },
});

export const deletePost = (postUid: string) => request({
  url: `${PATH}/${postUid}`,
  method: 'delete',
});

export const deleteSecretPost = (postUid: string, secret: string) => request({
  url: `${PATH}/${postUid}`,
  method: 'delete',
  params: {
    password: secret,
  },
});
