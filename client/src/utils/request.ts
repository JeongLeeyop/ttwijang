import axios from 'axios';
import { Message } from 'element-ui';
import router from '@/router';
import { removeToken } from '@/utils/cookies';

/* eslint-disable */
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 10000,
  // withCredentials: true // send cookies when cross-domain requests
});

// Request interceptor — attach JWT token
service.interceptors.request.use((config) => {
  const token = window.localStorage.getItem('jwttoken');
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let isRedirectingToLogin = false;

const handleUnauthorized = () => {
  if (isRedirectingToLogin) return;
  isRedirectingToLogin = true;

  removeToken();
  window.localStorage.removeItem('jwttoken');

  const currentPath = router.currentRoute.fullPath;
  if (currentPath !== '/login') {
    Message.warning('로그인 세션이 만료되었습니다. 다시 로그인해주세요.');
    router.replace({ path: '/login', query: { redirect: currentPath } }).catch(() => {});
  }

  setTimeout(() => { isRedirectingToLogin = false; }, 1500);
};

// Response interceptors
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (response.status !== 200) {
      return Promise.reject(new Error(res.message || 'Error'));
    }
    return response;
  },
  (error) => {
    // 네트워크 오류/타임아웃 등 — error.response 없음
    if (!error.response) {
      return Promise.reject(error);
    }
    if (error.response.status === 401) {
      handleUnauthorized();
      return Promise.reject(error);
    }
    return Promise.reject(error);
  },
);

export default service;
