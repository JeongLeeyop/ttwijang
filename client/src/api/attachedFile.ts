import commonRequest from '@/utils/commonRequest';

const PATH = '/attached-file';

export const uploadFile = (moduleName: string, formData: FormData) => commonRequest({
  url: `${moduleName}/upload`,
  method: 'post',
  data: formData,
});

export const getFile = (fileUid: string) => commonRequest({
  url: `${PATH}/${fileUid}/info`,
  method: 'get',
});

export const downloadFile = (fileUid: string) => commonRequest({
  url: `${PATH}/${fileUid}`,
  method: 'get',
});
