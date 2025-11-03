import request from '@/utils/request';

const PATH = '/challenge';

export const getChallengeList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getChallengeDetail = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const addChallenge = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateChallenge = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data,
});

export const deleteChallenge = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});

export const getChallengeCategory = () => request({
  url: `${PATH}/category/list`,
  method: 'get',
});

export const getChallengeRecordDetail = (uid: string) => request({
  url: `${PATH}/record/${uid}`,
  method: 'get',
});

export const getChallengeRecordList = (params: any) => request({
  url: `${PATH}/record`,
  method: 'get',
  params,
});

export const addChallengeRecord = (data: any) => request({
  url: `${PATH}/record`,
  method: 'post',
  data,
});

export const updateChallengeRecord = (uid: string, data: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'put',
  data,
});

export const deleteChallengeRecord = (uid: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'delete',
});

export const joinChallenge = (data: any) => request({
  url: `${PATH}/user/join`,
  method: 'post',
  data,
});
