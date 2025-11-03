import request from '@/utils/request';

const PATH = '/diary';

export const getDiaryMealList = (params: any) => request({
  url: PATH,
  method: 'get',
  params,
});

export const getPhysicalCondition = (params: any) => request({
  url: `${PATH}/physical/condition`,
  method: 'get',
  params,
});

export const getDiaryMealStatistics = (params: any) => request({
  url: `${PATH}/meal/statistics`,
  method: 'get',
  params,
});

export const addDiaryMeal = (data: any) => request({
  url: PATH,
  method: 'post',
  data,
});

export const addDiaryMealByFood = (data: any) => request({
  url: `${PATH}/food`,
  method: 'post',
  data,
});

export const addDiaryMealByHistory = (data: any) => request({
  url: `${PATH}/history`,
  method: 'post',
  data,
});

export const addDiaryMealByOrder = (data: any) => request({
  url: `${PATH}/order`,
  method: 'post',
  data,
});

export const savePhysicalCondition = (data: any) => request({
  url: `${PATH}/physical/condition`,
  method: 'post',
  data,
});

export const savePurposePhysical = (data: any) => request({
  url: `${PATH}/physical/purpose`,
  method: 'post',
  data,
});

export const deleteDiaryMeal = (idx: any) => request({
  url: `${PATH}/${idx}`,
  method: 'delete',
});
