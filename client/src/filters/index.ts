import moment from 'moment';
import 'moment/locale/ko';

export const parseDateAndAddDay = (stringDate: string, format: string, number: number) => {
  const parsingDate = moment(stringDate).add(number, 'day').format(format || 'YYYY.MM.DD');
  return parsingDate;
};

export const getDayOfWeek = (stringDate: string, format: string) => {
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD');
  const selectedDate = new Date(stringDate);
  const dayIndex = selectedDate.getDay();
  return `${parsingDate}(${daysOfWeek[dayIndex]})`;
};

export const parseDate = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD');
  return parsingDate;
};

export const parseDateD = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'DD');
  return parsingDate;
};

export const parseDateY = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'YYYY.MM');
  return parsingDate;
};

export const parseDateMonth = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'MM.DD');
  return parsingDate;
};

export const parseDateTime = (stringDate: string, format: string) => {
  const parsingDate = moment(stringDate).format(format || 'YYYY.MM.DD HH:mm');
  return parsingDate;
};

export const parseTime = (stringTime: string, format: string) => {
  const parsingTime = moment(stringTime, 'HH:mm:ss').format(format || 'HH:mm');
  return parsingTime;
};

export const parseTimeAt = (stringTime: string, format: string) => {
  const parsingTime = moment(stringTime).format(format || 'A hh:mm');
  return parsingTime;
};

export const parseConcatNumber = (data: string) => {
  if (data) {
    const filterDate = data.replace(/[^0-9]/g, '').replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, '$1-$2-$3');
    return filterDate;
  }
  return data;
};

export const removeTag = (value: string) => {
  const filterValue = value.replace(/(<([^>]+)>)/ig, '');
  return filterValue;
};

export const parseKrw = (value: string) => {
  const number = Number(value);
  // eslint-disable-next-line no-restricted-globals
  if (isNaN(number)) return value;
  return Number(number).toLocaleString('ko-KR');
};

export const numToString = (num: string) => {
  const number = Number(num);
  // eslint-disable-next-line no-restricted-globals
  if (isNaN(number)) return num;
  return Number(number).toLocaleString('ko-KR');
};

export const filterDay = (dayNum: number) => {
  dayNum = Number(dayNum);
  let day = '';
  if (dayNum === 0) day = '일요일';
  if (dayNum === 1) day = '월요일';
  if (dayNum === 2) day = '화요일';
  if (dayNum === 3) day = '수요일';
  if (dayNum === 4) day = '목요일';
  if (dayNum === 5) day = '금요일';
  if (dayNum === 6) day = '토요일';
  return day;
};
