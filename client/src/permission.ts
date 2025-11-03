import router from '@/router';
import { Message } from 'element-ui';
import { Route } from 'vue-router';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';
import settings from '@/settings';

const whiteList = ['/login', '/'];

const getPageTitle = (key: string) => {
  return `${settings.title}`;
};

router.beforeEach(async (to: Route, _: Route, next: any) => {
  next();
});

router.afterEach((to: Route) => {
  // set page title
  document.title = getPageTitle(to.meta ? to.meta.title : null);
});
