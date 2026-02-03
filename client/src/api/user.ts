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

/**
 * 회원가입
 */
export interface RegisterData {
  email: string
  password: string
  actualName: string
  birth: string // YYYY-MM-DD format
  gender: number // 0: 남성, 1: 여성
  concatNumber: string
  marketingStatus: boolean
}

export const register = (data: RegisterData) => request({
  url: `${PATH}/join`,
  method: 'post',
  data,
});

/**
 * 휴대폰 인증 요청
 */
export const sendPhoneVerification = (phoneNumber: string) => request({
  url: `${PATH}/phone/verification`,
  method: 'post',
  data: { phoneNumber },
});

/**
 * 휴대폰 인증번호 확인
 */
export const verifyPhoneCode = (phoneNumber: string, code: string) => request({
  url: `${PATH}/phone/verify`,
  method: 'post',
  data: { phoneNumber, code },
});

/**
 * 아이디(이메일) 찾기 - 휴대폰 인증 후 이메일 조회
 */
export const findEmail = (phoneNumber: string, verificationCode: string) => request({
  url: `${PATH}/find-email`,
  method: 'post',
  data: { phoneNumber, verificationCode },
});

/**
 * 비밀번호 재설정 요청 (이메일 + 휴대폰 인증)
 */
export const requestPasswordReset = (email: string, phoneNumber: string, verificationCode: string) => request({
  url: `${PATH}/password/reset-request`,
  method: 'post',
  data: { email, phoneNumber, verificationCode },
});

/**
 * 비밀번호 재설정
 */
export const resetPassword = (resetToken: string, newPassword: string) => request({
  url: `${PATH}/password/reset`,
  method: 'post',
  data: { resetToken, newPassword },
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
