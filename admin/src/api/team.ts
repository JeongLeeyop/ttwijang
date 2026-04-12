import request from '@/utils/request';

const PATH = '/team';

export const getTeamList = (params?: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getTeam = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const getDeleteRequestedTeams = () => request({
  url: `${PATH}/delete-requests`,
  method: 'get',
});

export const approveDeleteTeam = (uid: string) => request({
  url: `${PATH}/${uid}/delete-approve`,
  method: 'post',
});

export const rejectDeleteTeam = (uid: string) => request({
  url: `${PATH}/${uid}/delete-reject`,
  method: 'post',
});
