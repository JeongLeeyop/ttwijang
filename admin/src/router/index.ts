import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';

Vue.use(VueRouter);

export const constantRoutes: Array<RouteConfig> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue'),
    meta: { hidden: true },
  },
  {
    path: '/',
    name: 'Index',
    component: () => import('@/layout/default.vue'),
    meta: { hidden: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home.vue'),
      },
      {
        path: 'setting',
        name: 'Setting',
        component: () => import('@/views/setting.vue'),
        meta: { hidden: true },
      },
    ],
  },
];

export const asyncRoutes: RouteConfig[] = [
  /* 대시보드 */
  {
    path: '/dashboard',
    component: () => import('@/layout/default.vue'),
    meta: { title: '대시보드', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '대시보드', roles: ['ROLE_ADMIN'] },
      },
    ],
  },

  /* 지역 관리 */
  {
    path: '/region',
    component: () => import('@/layout/default.vue'),
    meta: { title: '지역 관리', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'RegionList',
        component: () => import('@/views/region/index.vue'),
        meta: { title: '지역 관리', roles: ['ROLE_ADMIN'] },
      },
    ],
  },

  /* 리그 관리 */
  {
    path: '/league',
    component: () => import('@/layout/default.vue'),
    meta: { title: '리그 관리', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'LeagueList',
        component: () => import('@/views/league/index.vue'),
        meta: { title: '리그 목록', roles: ['ROLE_ADMIN'] },
      },
      {
        path: 'teams/:uid',
        name: 'LeagueTeams',
        component: () => import('@/views/league/teams.vue'),
        meta: { title: '팀 배정', roles: ['ROLE_ADMIN'], hidden: true },
      },
    ],
  },

  /* 유저 관리 */
  {
    path: '/user',
    component: () => import('@/layout/default.vue'),
    meta: { title: '유저 관리', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'UserList',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '유저 관리', roles: ['ROLE_ADMIN'] },
      },
    ],
  },

  /* 배너 관리 */
  {
    path: '/banner',
    component: () => import('@/layout/default.vue'),
    meta: { title: '배너 관리', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'BannerList',
        component: () => import('@/views/banner/index.vue'),
        meta: { title: '배너 관리', roles: ['ROLE_ADMIN'] },
      },
    ],
  },

  /* 리그 구단주 관리 */
  {
    path: '/sponsor',
    component: () => import('@/layout/default.vue'),
    meta: { title: '리그 구단주 관리', roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: 'fee',
        name: 'SponsorFee',
        component: () => import('@/views/sponsor/fee.vue'),
        meta: { title: '구단주 금액 설정', roles: ['ROLE_ADMIN'] },
      },
      {
        path: 'team-banner',
        name: 'TeamBanner',
        component: () => import('@/views/sponsor/team-banner.vue'),
        meta: { title: '팀별 배너 설정', roles: ['ROLE_ADMIN'] },
      },
    ],
  },

  { path: '*', redirect: '/dashboard', meta: { hidden: true } },
];

const createRouter = () => new VueRouter({
  scrollBehavior: () => ({ x: 0, y: 0 }),
  routes: constantRoutes,
});

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  (router as any).matcher = (newRouter as any).matcher;
}

export default router;
