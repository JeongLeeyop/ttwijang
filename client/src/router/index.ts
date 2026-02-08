import Vue from 'vue';
import VueRouter, { Route, RouteConfig } from 'vue-router';
import 'element-ui/lib/theme-chalk/index.css';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';
import MobileDetect from 'mobile-detect';

Vue.use(VueRouter);

export const constantRoutes: Array<RouteConfig> = [
	{
		path: '/login',
		name: 'Login',
		component: () => import('@/views/index.vue'),
	},
	{
		path: '',
		name: 'MainLayout',
		component: () => import('@/Layout/mainLayout.vue'),
		children: [
			{
				path: '',
				name: 'Home',
				meta: {
					title: '',
				},
				component: () => import('@/views/home.vue'),
			},
			{
				path: 'league',
				name: 'League',
				meta: {
					title: '',
				},
				component: () => import('@/views/league.vue'),
			},
			{
				path: 'league-status',
				name: 'LeagueStatus',
				meta: {
					title: '리그 현황',
				},
				component: () => import('@/views/leagueStatus.vue'),
			},
			{
				path: 'league-schedule',
				name: 'LeagueSchedule',
				meta: {
					title: '리그 일정',
				},
				component: () => import('@/views/leagueSchedule.vue'),
			},
			{
				path: 'league-guest',
				name: 'LeagueGuest',
				meta: {
					title: '리그 일정',
				},
				component: () => import('@/views/leagueGuest.vue'),
			},
			{
				path: 'mypage',
				name: 'MyPage',
				meta: {
					title: '마이페이지',
				},
				component: () => import('@/views/myPage.vue'),
			},
			{
				path: 'match',
				name: 'Match',
				meta: {
					title: '매치',
				},
				component: () => import('@/views/match.vue'),
			},
		],
	},
	{
		path: '',
		name: 'FutsalLayout',
		component: () => import('@/Layout/futsalLayout.vue'),
		children: [
			{
				path: '/calendar',
				name: 'Calendar',
				meta: {
					title: '경기 일정',
				},
				component: () => import('@/views/calendar.vue'),
			},
		],
	},
	{
		path: '',
		name: 'AuthLayout',
		component: () => import('@/Layout/authLayout.vue'),
		children: [
			{
				path: '/email-login',
				name: 'EmailLogin',
				meta: {
					title: '이메일 로그인',
				},
				component: () => import('@/views/emailLogin.vue'),
			},
			{
				path: '/find-id',
				name: 'FindId',
				meta: {
					title: '아이디 찾기',
				},
				component: () => import('@/views/findId.vue'),
			},
			{
				path: '/find-password',
				name: 'FindPassword',
				meta: {
					title: '비밀번호 찾기',
				},
				component: () => import('@/views/findPassword.vue'),
			},
			{
				path: '/reset-password',
				name: 'ResetPassword',
				meta: {
					title: '비밀번호 재설정',
				},
				component: () => import('@/views/resetPassword.vue'),
			},
			{
				path: '/register',
				name: 'Register',
				meta: {
					title: '회원가입',
				},
				component: () => import('@/views/register.vue'),
			},
			{
				path: '/create-team',
				name: 'CreateTeam',
				meta: {
					title: '팀 생성',
				},
				component: () => import('@/views/createTeam.vue'),
			},
			{
				path: '/upload-team-mark',
				name: 'UploadTeamMark',
				meta: {
					title: '팀 마크 업로드',
				},
				component: () => import('@/views/uploadTeamMark.vue'),
			},
			{
				path: '/team-info',
				name: 'TeamInfo',
				meta: {
					title: '팀 정보',
				},
				component: () => import('@/views/teamInfo.vue'),
			},
			{
				path: '/team-location',
				name: 'TeamLocation',
				meta: {
					title: '팀 활동 지역',
				},
				component: () => import('@/views/teamLocation.vue'),
			},
			{
				path: '/team-complete',
				name: 'TeamComplete',
				meta: {
					title: '팀 생성 완료',
				},
				component: () => import('@/views/teamComplete.vue'),
			},
		],
	},
];

const createRouter = () => new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes: constantRoutes,
});

const wRoutes = [
	{
		path: '/',
		component: () => import('@/views/noIndex.vue'),
		name: 'wMain',
	},
];

const wRouter = () => new VueRouter({
	mode: 'history',
	routes: wRoutes,
});

/* eslint-disable */
let router = createRouter();

const md = new MobileDetect(window.navigator.userAgent);
if (md.mobile()) {
	console.log('CONNECTION WITH MOBILE');
} else {
	console.log('CONNECTION WITH PC');
	// router = wRouter();
}

export function resetRouter() {
	const newRouter = createRouter();
	(router as any).matcher = (newRouter as any).matcher;
}

export default router;
