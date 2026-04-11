import qs from 'qs';
import axios from 'axios';
import request from '@/utils/request';

const PATH = '/user';

export const login = (data: any) => {
  data.grant_type = 'password';
  return axios({
    method: 'post',
    url: '/oauth/token',
    data: qs.stringify(data),
    headers: {
      Authorization: 'Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=',
    },
  });
};

export const getUserList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page ? params.page - 1 : 0,
  },
});

export const getUser = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const addUser = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateUser = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data,
});

export const deleteUser = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});

export const enableUser = (uid: string, enabled: boolean) => request({
  url: `${PATH}/${uid}/enable`,
  method: 'put',
  data: { enabled },
});
