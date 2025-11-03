import * as vuexModuleDecorators from 'vuex-module-decorators';
import { RouteConfig } from 'vue-router';
import { constantRoutes } from '@/router';
import store from '@/store';

const hasPermission = (roles: string[], route: any) => {
  if (route.meta && route.meta.roles) {
    return roles.some((role) => route.meta.roles.includes(role));
  }
  return true;
};

export const filterAsyncRoutes = (routes: RouteConfig[], roles: string[]) => {
  const res: RouteConfig[] = [];

  routes.forEach((route) => {
    const r = { ...route };
    if (hasPermission(roles, r)) {
      if (r.children) {
        r.children = filterAsyncRoutes(r.children, roles);
      }
      res.push(r);
    }
  });
  return res;
};

export interface IPermissionState {
  routes: RouteConfig[]
  dynamicRoutes: RouteConfig[]
}

@vuexModuleDecorators.Module({ dynamic: true, store, name: 'permission' })
class Permission extends vuexModuleDecorators.VuexModule implements IPermissionState {
  public routes: RouteConfig[] = []

  public dynamicRoutes: RouteConfig[] = []

  @vuexModuleDecorators.Mutation
  private SET_ROUTES(routes: RouteConfig[]) {
    this.routes = constantRoutes.concat(routes);
    this.dynamicRoutes = routes;
  }

  @vuexModuleDecorators.Action
  public GenerateRoutes(roles: string[]) {
    // console.log('GenerateRoutes');
    // let accessedRoutes;
    // if (roles.includes('admin')) {
    //   accessedRoutes = asyncRoutes;
    // } else {
    //   accessedRoutes = filterAsyncRoutes(asyncRoutes, roles);
    // }
    // this.SET_ROUTES(accessedRoutes);
  }
}

export const PermissionModule = vuexModuleDecorators.getModule(Permission);
