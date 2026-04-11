import request from '@/utils/request';

const PATH = '/league';

export const getLeagueList = (params?: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getLeague = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const createLeague = (data: any) => request({
  url: `${PATH}/admin`,
  method: 'post',
  data,
});

export const updateLeague = (uid: string, data: any) => request({
  url: `${PATH}/admin/${uid}`,
  method: 'put',
  data,
});

export const deleteLeague = (uid: string) => request({
  url: `${PATH}/admin/${uid}`,
  method: 'delete',
});

export const getLeagueTeams = (leagueUid: string) => request({
  url: `${PATH}/${leagueUid}/teams`,
  method: 'get',
});

export const addTeamToLeague = (leagueUid: string, teamUid: string) => request({
  url: `${PATH}/admin/${leagueUid}/teams/${teamUid}`,
  method: 'post',
});

export const removeTeamFromLeague = (leagueUid: string, teamUid: string) => request({
  url: `${PATH}/admin/${leagueUid}/teams/${teamUid}`,
  method: 'delete',
});

export const createLeagueMatch = (data: any) => request({
  url: `${PATH}/admin/match`,
  method: 'post',
  data,
});

export const updateMatchResult = (data: any) => request({
  url: `${PATH}/match/result`,
  method: 'post',
  data,
});
