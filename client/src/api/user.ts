import qs from 'qs';
import axios from 'axios';
import request from '@/utils/request';

const PATH = '/user';

export const tokenCheck = (jwt: any) => {
  const token = {
    token: jwt,
  };
  return axios({
    method: 'post',
    url: '/oauth/check_token',
    data: qs.stringify(token),
  });
};

export const getUserInfo = () => request({
  url: `${PATH}/info`,
  method: 'get',
});

export const join = (data: any) => request({
  url: `${PATH}/join`,
  method: 'post',
  data,
});

export const updateUserInfo = (data: any) => request({
  url: `${PATH}/info`,
  method: 'put',
  data,
});

export const updateShop = (idx: any) => request({
  url: `${PATH}/setShop/${idx}`,
  method: 'put',
});

export const updateStation = (idx: any) => request({
  url: `${PATH}/setStation/${idx}`,
  method: 'put',
});

export const withdrawUser = (data: any) => request({
  url: `${PATH}/withdraw`,
  method: 'delete',
  data,
});

/* eslint-disable */
export const defaultJoinForm = () => {
  return {
    step: 1,
    actualName: '',
    birth: '',
    gender: null,
    concatNumber: '',
    email: '',
    postCode: '',
    address: '',
    addressDetail: '',
    dietExperience: '',
    height: '',
    weight: '',
    goalWeight: '',
    goalDate: '',
    activityLevel: '',
    dietPurpose: '',
    dietPrecaution: '',
    terms1: false,
    terms2: false,
    terms3: false,
    terms4: false,
    marketingStatus: false,
  };
};
