import axios from 'axios';

/* eslint-disable */
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 5000,
  // withCredentials: true // send cookies when cross-domain requests
});

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
    const res = error.response;
    if (res.status === 401) {
      window.location.replace('/login');
      return;
    }
    // alert(res.data.message || '사용자 요청을 실패했습니다.');
    return Promise.reject(error);
  },
);

export default service;
