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
					requiresAuth: true,
				},
				component: () => import('@/views/home.vue'),
			},
			{
				path: 'league',
				name: 'League',
				meta: {
					title: '',
					requiresAuth: true,
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
					requiresAuth: true,
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
					requiresAuth: true,
				},
				component: () => import('@/views/match.vue'),
			},
			{
				path: 'team/:teamCode?',
				name: 'TeamPage',
				meta: {
					title: '팀 페이지',
				},
				component: () => import('@/views/teamPage.vue'),
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
			{
				path: '/match-detail/:uid',
				name: 'MatchDetail',
				meta: {
					title: '경기 상세',
				},
				component: () => import('@/views/matchDetail.vue'),
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
			{
				path: '/match-create',
				name: 'MatchCreate',
				meta: {
					title: '매치 일정 만들기',
				},
				component: () => import('@/views/MatchCreate.vue'),
			},
			{
				path: '/guest-recruit',
				name: 'GuestRecruit',
				meta: {
					title: '게스트 모집',
				},
				component: () => import('@/views/GuestRecruit.vue'),
			},
			{
				path: '/profile-edit',
				name: 'ProfileEdit',
				meta: {
					title: '프로필 수정',
				},
				component: () => import('@/views/profileEdit.vue'),
			},
			{
				path: '/cash-charge',
				name: 'CashCharge',
				meta: {
					title: '캐쉬 충전',
				},
				component: () => import('@/views/cashCharge.vue'),
			},
			{
				path: '/cash-history',
				name: 'CashHistory',
				meta: {
					title: '캐쉬 사용내역',
				},
				component: () => import('@/views/cashHistory.vue'),
			},
			{
				path: '/payment/result',
				name: 'PaymentResult',
				meta: {
					title: '결제 결과',
				},
				component: () => import('@/views/paymentResult.vue'),
			},
			{
				path: '/recruitment-create',
				name: 'RecruitmentCreate',
				meta: {
					title: '회원 모집하기',
				},
				component: () => import('@/views/recruitmentCreate.vue'),
			},
			{
				path: '/team-recruit-detail/:teamUid',
				name: 'TeamRecruitDetail',
				meta: {
					title: '팀 모집 상세',
				},
				component: () => import('@/views/teamRecruitDetail.vue'),
			},
			{
				path: '/team-recruit',
				name: 'TeamRecruit',
				meta: {
					title: '회원 모집',
				},
				component: () => import('@/views/teamRecruit.vue'),
			},
			{
				path: '/league-teams',
				name: 'LeagueTeams',
				meta: {
					title: '리그 참가 팀',
				},
				component: () => import('@/views/leagueTeams.vue'),
			},
			{
				path: '/team-dashboard',
				name: 'TeamDashboard',
				meta: {
					title: '팀 대시보드',
				},
				component: () => import('@/views/teamDashboard.vue'),
			},
			{
				path: '/my-applications',
				name: 'MyApplications',
				meta: { title: '신청 내역' },
				component: () => import('@/views/myApplications.vue'),
			},
			{
				path: '/notice',
				name: 'Notice',
				meta: { title: '공지사항' },
				component: () => import('@/views/notice.vue'),
			},
			{
				path: '/faq',
				name: 'Faq',
				meta: { title: '자주 묻는 질문' },
				component: () => import('@/views/faq.vue'),
			},
			{
				path: '/sponsor-apply',
				name: 'SponsorApply',
				meta: { title: '구단 후원 신청' },
				component: () => import('@/views/sponsorApply.vue'),
			},
			{
				path: '/guide',
				name: 'Guide',
				meta: { title: '뛰장 가이드' },
				component: () => import('@/views/guide.vue'),
			},
			{
				path: '/about',
				name: 'About',
				meta: { title: '뛰장 소개' },
				component: () => import('@/views/about.vue'),
			},
			{
				path: '/team-settings',
				name: 'TeamSettings',
				meta: {
					title: '팀 설정',
				},
				component: () => import('@/views/teamSettings.vue'),
			},
			{
				path: '/team-profile-edit',
				name: 'TeamProfileEdit',
				meta: {
					title: '팀 프로필 수정',
				},
				component: () => import('@/views/teamProfileEdit.vue'),
			},
			{
				path: '/pending-approval',
				name: 'PendingApproval',
				meta: {
					title: '가입 신청 현황',
				},
				component: () => import('@/views/pendingApproval.vue'),
			},
			{
				path: '/pending-manage',
				name: 'PendingManage',
				meta: {
					title: '가입 승인 관리',
				},
				component: () => import('@/views/pendingManage.vue'),
			},
			{
				path: '/member-detail/:memberUid',
				name: 'MemberDetail',
				meta: {
					title: '회원 상세',
				},
				component: () => import('@/views/memberDetail.vue'),
			},
			{
				path: '/league-detail/:uid',
				name: 'LeagueDetail',
				meta: {
					title: '리그 소개',
				},
				component: () => import('@/views/leagueDetail.vue'),
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
