import axios from 'axios';
import qs from 'qs';

// OAuth2 Basic Authorization Header (base64 encoded 'singha_oauth:singhascrect!@#$')
const OAUTH_AUTH_HEADER = 'Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=';

/**
 * 이메일/비밀번호 로그인
 * OAuth2 password grant type 사용
 */
export const emailLogin = (email: string, password: string) => {
  const data = qs.stringify({
    grant_type: 'password',
    username: email,
    password,
  });

  return axios({
    url: '/oauth/token',
    method: 'post',
    headers: {
      Authorization: OAUTH_AUTH_HEADER,
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data,
  });
};

export const getKakaoLogin = (accessToken: any) => {
  return axios({
    url: '/oauth/token',
    method: 'post',
    headers: {
      Authorization: OAUTH_AUTH_HEADER,
      'x-auth-token': `Kakao ${accessToken}`,
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  });
};

export const getNaverAccess = (code: any) => {
  return axios({
    url: '/oauth/naver/access',
    method: 'get',
    params: {
      code,
    },
  });
};

export const getNaverLogin = (code: any) => {
  return axios({
    url: '/oauth/naver/login',
    method: 'get',
    params: {
      code,
    },
  });
};

export const getNaverMe = (accessToken: any) => {
  return axios({
    url: '/oauth/naver/me',
    method: 'get',
    params: {
      accessToken,
    },
  });
};

/* eslint-disable */
export const getAppleLogin = (data: any) => {
  return axios({
    url: '/oauth/apple/login',
    method: 'post',
    data,
  });
};
/* eslint-enable */

export const getNiceLogin = (encodeData: any) => {
  return axios({
    url: '/oauth/token',
    method: 'post',
    headers: {
      Authorization: OAUTH_AUTH_HEADER,
      'x-auth-token': `Nice ${encodeData}`,
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  });
};

export const getAccessToken = (code: any) => {
  const data: any = {};
  data.grant_type = 'authorization_code';
  data.client_id = process.env.VUE_APP_KAKAO_CLIENT_ID;
  data.client_secret = process.env.VUE_APP_KAKAO_CLIENT_SECRET;
  data.redirect_uri = process.env.VUE_APP_KAKAO_REDIRECT_URL;
  data.code = code;

  return axios({
    url: 'https://kauth.kakao.com/oauth/token',
    method: 'post',
    data: qs.stringify(data),
  });
};
