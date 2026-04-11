import request from '@/utils/request';
import { IRole } from '@/types/role';

const PATH = '/role';

export const getRoleList = (listQuery: any) => request({
  url: PATH,
  method: 'get',
  params: {
    ...listQuery,
    page: listQuery.page - 1,
  },
});

export const getRole = (roleCode: any) => request({
  url: `${PATH}/${roleCode}`,
  method: 'get',
});

export const getGroupList = (listQuery: any) => request({
  url: `${PATH}/group`,
  method: 'get',
  params: {
    ...listQuery,
  },
});

export const addRole = (data: IRole) => request({
  url: PATH,
  method: 'post',
  data,
});

export const updateRole = (roleCode: string, data: IRole) => request({
  url: `${PATH}/${roleCode}`,
  method: 'put',
  data,
});

export const deleteRole = (roleCode: string) => request({
  url: `${PATH}/${roleCode}`,
  method: 'delete',
});
