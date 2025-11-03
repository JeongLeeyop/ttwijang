import request from '@/utils/request';

const PATH = '/mission/inquiry';
const USERPATH = '/mission/user/inquiry';

export const addMissionUserInquiry = (data: any) => request({
  url: USERPATH,
  method: 'post',
  data,
});

export const getMissionInquiryList = (pageNum: number) => request({
  url: `${PATH}/page/${pageNum}`,
  method: 'get',
});

export const getMissionInquiryDetail = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const addMissionInquiry = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateMissionInquiry = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data,
});

export const deleteMissionInquiry = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});

export const getMissionInquiryCategory = () => request({
  url: `${PATH}/category/list`,
  method: 'get',
});

export const getMissionInquiryRecordDetail = (uid: string) => request({
  url: `${PATH}/record/${uid}`,
  method: 'get',
});

export const getMissionInquiryRecordList = (params: any) => request({
  url: `${PATH}/record`,
  method: 'get',
  params,
});

export const addMissionInquiryRecord = (data: any) => request({
  url: `${PATH}/record`,
  method: 'post',
  data,
});

export const updateMissionInquiryRecord = (uid: string, data: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'put',
  data,
});

export const deleteMissionInquiryRecord = (uid: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'delete',
});

export const joinMissionInquiry = (data: any) => request({
  url: `${PATH}/user/join`,
  method: 'post',
  data,
});
