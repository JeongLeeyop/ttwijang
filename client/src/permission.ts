import router from '@/router';
import { Message } from 'element-ui';
import { Route } from 'vue-router';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';
import settings from '@/settings';
import { getToken } from '@/utils/cookies';

const whiteList = ['/login', '/'];

const getPageTitle = (key: string) => {
  return `${settings.title}`;
};

// 앱 시작 시 토큰이 있으면 사용자 정보 로드
const initializeAuth = async () => {
  const token = getToken();
  if (token && !UserModule.isLogin) {
    try {
      await UserModule.GetUserInfo();
    } catch (error) {
      console.error('Failed to load user info:', error);
    }
  }
};

const authReady = initializeAuth();

router.beforeEach(async (to: Route, _: Route, next: any) => {
  await authReady;
  const requiresAuth = to.matched.some((record) => record.meta && record.meta.requiresAuth);
  if (requiresAuth && !UserModule.isLogin) {
    Message.warning('로그인이 필요한 페이지입니다.');
    next({ path: '/login', query: { redirect: to.fullPath } });
    return;
  }
  next();
});

router.afterEach((to: Route) => {
  // set page title
  document.title = getPageTitle(to.meta ? to.meta.title : null);
});
