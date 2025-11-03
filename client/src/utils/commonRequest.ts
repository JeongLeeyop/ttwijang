import axios from 'axios';

/* eslint-disable */
const service = axios.create({
  baseURL: process.env.VUE_APP_COMMON_API, // url = base url + request url
  timeout: 5000,
  // withCredentials: true // send cookies when cross-domain requests
});

// Response interceptors
service.interceptors.response.use(
  (response: any) => {
    const res = response.data;
    if (response.status !== 200) {
      return Promise.reject(new Error(res.message || 'Error'));
    }
    return response;
  },
  (error: any) => {
    const res = error.response;
    return Promise.reject(error);
  },
);

export default service;

