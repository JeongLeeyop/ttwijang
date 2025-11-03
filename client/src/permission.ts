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
  await UserModule.GetUserInfo();
  if (to.meta && to.meta.roles && to.meta.roles.length > 0) {
    // Determine whether the user has logged in
    if (UserModule.token) {
        if (window.localStorage.getItem('isRegister') === 'false') {
          next('/login');
        }
      if (to.path === '/login') {
        // If is logged in, redirect to the home page
        next({ path: '/' });
      } else {
        // Check whether the user has obtained his permission roles
        // eslint-disable-next-line no-lonely-if
        if (UserModule.roles.length === 0) {
          try {
            // Note: roles must be a object array! such as: ['admin'] or ['developer', 'editor']
            // await UserModule.GetUserInfo();
            const roles = UserModule.roles;
            // Generate accessible routes map based on role
            PermissionModule.GenerateRoutes(roles);
            // Dynamically add accessible routes
            PermissionModule.dynamicRoutes.forEach((route) => {
              router.addRoute(route);
            });
            // Hack: ensure addRoutes is complete
            // Set the replace: true, so the navigation will not leave a history record
            next({ ...to, replace: true });
          } catch (err) {
            // Remove token and redirect to login page
            UserModule.LogOut();
            // Message.error(err || 'Has Error');
            next(`/login?redirect=${to.path}`);
          }
        } else {
          next();
        }
      }
    } else {
      console.log('1');
      if (window.localStorage.getItem('jwttoken') !== '' && window.localStorage.getItem('jwttoken') !== null) {
        // UserModule.LogOut()
        // 아이폰에서 세션 유지
        await UserModule.LoadJwt(window.localStorage.getItem('jwttoken'));
        next();
        // next('/Mypage');
      } else {
         next('/login');
      }
    }
  } else {
    // next('/login');
    next();
  }
});

router.afterEach((to: Route) => {
  // set page title
  document.title = getPageTitle(to.meta ? to.meta.title : null);
});
