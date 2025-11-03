import request from '@/utils/request';

const PATH = '/mission';

export const getMissionList = (params: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...params,
    page: params.page - 1,
  },
});

export const getMissionDetail = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const addMission = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateMission = (uid: string, data: any) => request({
  url: `${PATH}/${uid}`,
  method: 'put',
  data,
});

export const deleteMission = (uid: any) => request({
  url: `${PATH}/${uid}`,
  method: 'delete',
});

export const getMissionCategory = () => request({
  url: `${PATH}/category/list`,
  method: 'get',
});

export const getMissionRecordDetail = (uid: string) => request({
  url: `${PATH}/record/${uid}`,
  method: 'get',
});

export const getMissionRecordList = (params: any) => request({
  url: `${PATH}/record`,
  method: 'get',
  params,
});

export const addMissionRecord = (data: any) => request({
  url: `${PATH}/record`,
  method: 'post',
  data,
});

export const updateMissionRecord = (uid: string, data: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'put',
  data,
});

export const deleteMissionRecord = (uid: any) => request({
  url: `${PATH}/record/${uid}`,
  method: 'delete',
});

export const joinMission = (data: any) => request({
  url: `${PATH}/user/join`,
  method: 'post',
  data,
});

  export const joinMultipleMissions = (data: string[]) => request({
    url: `${PATH}/join/multiple`,
    method: 'post',
    data,
  });

export const abandonMission = (missionUid: string) => request({
  url: `${PATH}/abandon/${missionUid}`,
  method: 'post',
});

export const getMissionSummary = () => request({
  url: `${PATH}/summary`,
  method: 'get',
});
