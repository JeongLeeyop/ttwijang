import moment from 'moment';

export const parseDate = (stringDate: string, format: string) => {
  if (stringDate) {
    const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD');
    return parsingDate;
  }
  return '-';
};

export const parseConcatNumber = (data: string) => {
  if (data) {
    const filterDate = data.replace(/[^0-9]/g, '').replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, '$1-$2-$3');
    return filterDate;
  }
  return data;
};

export const parsefilterDate = (data: any) => {
  if (data) {
    const cloneData = `0${data}`;
    return cloneData;
  }
  return data;
};

export const parseDateTime = (stringDate: string, format: string) => {
  if (stringDate) {
    const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD HH:mm');
    return parsingDate;
  }
  return '-';
};

export const parseTime = (stringTime: string, format: string) => {
  const parsingDate = moment(stringTime, 'HH:mm:ss').format(format || 'HH:mm');
  return parsingDate;
};

export const numToString = (num: number | string) => {
  const number = Number(num);
  /* eslint-disable */
  if (isNaN(number)) return num;
  return Number(number).toLocaleString('ko-KR');
};

export const calendarParseDate = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD');
  return parsingDate;
};

export const doubleDate = (startDate: string, endDate: string) => {
  const dates = [];

  const currDate = moment(startDate).startOf('day');
  const lastDate = moment(endDate).startOf('day');

  while (currDate.add(1, 'days').diff(lastDate) < 0) {
    dates.push(currDate.clone().toDate());
  }
  return dates;
};

export const parseKrw = (value: string) => {
  if (value) {
    return value.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ',');
  } return 0;
};

export const removeTag = (value: string) => {
  if (value) {
    const filterValue = value.replace(/(<([^>]+)>)/ig, '');
    return filterValue;
  }
  return value;
};

