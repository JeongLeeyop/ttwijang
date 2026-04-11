import request from '@/utils/request';

const PATH = '/attached-file';

export const upload = (moduleName: string, formData: FormData) => request({
  url: `${moduleName}/upload`,
  method: 'post',
  data: formData,
});

export const getFile = (fileUid: string) => request({
  url: `${PATH}/${fileUid}/info`,
  method: 'get',
});

export const download = (fileUid: string) => request({
  url: `${PATH}/${fileUid}`,
  method: 'get',
});
