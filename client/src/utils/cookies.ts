import cookies from 'js-cookie';
import jwtDecode from 'jwt-decode';
import moment from 'moment';

// User
const tokenKey = 'access_token';
export const getTokenDecode = () => jwtDecode(cookies.get(tokenKey) || '');
export const getToken = () => cookies.get(tokenKey);
export const setToken = (token: any) => cookies.set(tokenKey, token);
export const removeToken = () => cookies.remove(tokenKey);
export const setRedirectUrl = (redirectUrl: any) => cookies.set('redirectUrl', redirectUrl);
export const getRedirectUrl = () => cookies.get('redirectUrl');
export const removeRedirectUrl = () => cookies.remove('redirectUrl');

const visitKey = `visitHistory${moment().format('YYYYMMDD')}`;
export const getVisitKey = () => cookies.get(visitKey);
export const setVisitKey = () => cookies.set(visitKey, '1', { expires: 1 });

export const getTokenInfo: any = () => {
  const token = getTokenDecode();
  return token;
};

export const getJwtInfo = (key: any) => {
  return getTokenInfo()[key];
};
