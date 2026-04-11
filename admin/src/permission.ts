import router from '@/router';
import { Message } from 'element-ui';
import { Route } from 'vue-router';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';

const whiteList = ['/login'];

router.beforeEach(async (to: Route, _: Route, next: any) => {
  if (UserModule.token) {
    if (to.name === 'Login') {
      next({ path: '/' });
      return;
    }
    if (UserModule.roles.length === 0) {
      try {
        await UserModule.GetUserInfo();
        const { roles } = UserModule;
        await PermissionModule.GenerateRoutes(roles);
        router.addRoutes(PermissionModule.routes);
        next({ ...to, replace: true });
        return;
      } catch (err) {
        UserModule.ResetToken();
        Message.error('사용자 인증에 실패했습니다.');
        next(`/login?redirect=${to.path}`);
        return;
      }
    }
    next();
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next();
      return;
    }
    next(`/login?redirect=${to.path}`);
  }
});
