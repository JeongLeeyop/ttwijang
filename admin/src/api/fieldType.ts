import request from '@/utils/request';

const PATH = '/field-type';

export const getFieldTypeList = () => request({
  url: `${PATH}/all`,
  method: 'get',
});
