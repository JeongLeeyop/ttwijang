import Vue from 'vue';
import VueRouter, { Route, RouteConfig } from 'vue-router';
import counsultView from '@/views/counsult/view.vue';
import 'element-ui/lib/theme-chalk/index.css';
import { UserModule } from '@/store/modules/user';
import { PermissionModule } from '@/store/modules/permission';
import MobileDetect from 'mobile-detect';

Vue.use(VueRouter);

export const constantRoutes: Array<RouteConfig> = [
	{
		path: '/search',
		name: 'Search',
		meta: {
			title: '통합검색',
		},
		component: () => import('@/views/search/index.vue'),
	},
	{
		path: '/counsultView/:id',
		component: counsultView,
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import('@/views/index.vue'),
	},
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
		path: '/',
		name: 'Home',
		meta: {
			title: '',
		},
		component: () => import('@/views/home.vue'),
	},
	/* 회원가입  */
	{
		path: '/join',
		name: 'joinLayout',
		component: () => import('@/Layout/joinLayout.vue'),
		children: [
			{
				path: 'auth',
				name: 'Auth',
				meta: {
					title: '본인인증',
				},
				component: () => import('@/views/join/join_step1.vue'),
			},
			{
				path: 'agree',
				name: 'Agree',
				meta: {
					title: '약관동의',
				},
				component: () => import('@/views/join/join_step.vue'),
			},
			{
				path: 'signup',
				name: 'Signup',
				meta: {
					title: '회원가입',
				},
				component: () => import('@/views/join/join_step3.vue'),
			},
			{
				path: 'info',
				name: 'Info',
				meta: {
					title: '정보입력',
				},
				component: () => import('@/views/join/join_step4.vue'),
			},
			{
				path: 'idinquiry',
				name: 'Idinquiry',
				meta: {
					title: '이메일 찾기',
				},
				component: () => import('@/views/join/join_idinquiry.vue'),
			},
			{
				path: 'pwinquiry',
				name: 'Pwinquiry',
				meta: {
					title: '비밀번호 찾기',
				},
				component: () => import('@/views/join/join_pwinquiry.vue'),
			},
		],
	},
	{
		path: '/board',
		name: 'Board',
		meta: {
			title: '',
		},
		component: () => import('@/Layout/boardLayout.vue'),
		children: [
			{
				path: ':boardUid',
				component: () => import('@/views/sub/board/index.vue'),
				name: 'BoardIndex',
				meta: {
					title: '',
				},
			},
			{
				path: 'post/:boardUid/add',
				component: () => import('@/views/sub/board/form.vue'),
				name: 'PostAdd',
				meta: {
					title: '',
				},
			},
			{
				path: 'post/:boardUid/:postUid/update',
				component: () => import('@/views/sub/board/form.vue'),
				name: 'PostUpdate',
				meta: {
					title: '',
				},
			},
			{
				path: 'post/:boardUid/:postUid',
				component: () => import('@/views/sub/board/detail.vue'),
				name: 'PostDetail',
				meta: {
					title: '',
				},
			},
		],
	},
	/* 다이어리 */
	{
		path: '/diary',
		name: '',
		component: () => import('@/Layout/diaryLayout.vue'),
		meta: {
			title: '다이어리',
		},
		children: [
			{
				path: '',
				name: 'Diary',
				meta: {
					title: '다이어리',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/diary/index.vue'),
			},
			{
				path: 'status',
				name: 'NutritionStatus',
				meta: {
					title: '영양상태',
				},
				component: () => import('@/views/diary/nutritionStatus.vue'),
			},
			{
				path: 'composition',
				name: 'BodyComposition',
				meta: {
					title: '체성분 기록',
				},
				component: () => import('@/views/diary/bodyComposition.vue'),
			},
			{
				path: 'record',
				name: 'BodyRecord',
				meta: {
					title: '체성분 상태기록',
				},
				component: () => import('@/views/diary/components/bodyRecord.vue'),
			},
			{
				path: 'purpose',
				name: 'BodyPurpose',
				meta: {
					title: '체성분 목표설정',
				},
				component: () => import('@/views/diary/components/bodyPurpose.vue'),
			},
			{
				path: 'search',
				name: 'FoodSearch',
				meta: {
					title: '음식검색',
				},
				component: () => import('@/views/diary/components/foodSearch.vue'),
			},
			{
				path: 'recent',
				name: 'FoodRecent',
				meta: {
					title: '최근기록',
				},
				component: () => import('@/views/diary/components/foodRecent.vue'),
			},
			{
				path: 'frequency',
				name: 'FoodFrequency',
				meta: {
					title: '최근기록',
				},
				component: () => import('@/views/diary/components/foodFrequency.vue'),
			},
			{
				path: 'direct',
				name: 'FoodDirect',
				meta: {
					title: '직접입력',
				},
				component: () => import('@/views/diary/components/foodDirect.vue'),
			},
			{
				path: 'history',
				name: 'FoodHistory',
				meta: {
					title: '주문내역',
				},
				component: () => import('@/views/diary/components/foodHistory.vue'),
			},
		],
	},
	/* 맞춤식단 */
	{
		path: '/diet',
		name: '',
		component: () => import('@/Layout/orderLayout.vue'),
		children: [
			{
				path: 'order',
				name: 'Order',
				meta: {
					title: '식단주문',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/diet/dietOrder.vue'),
			},
			{
				path: 'order/detail/:productId',
				name: 'OrderDetail',
				meta: {
					title: '상품 상세보기',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/diet/orderDetail.vue'),
			},
			{
				path: 'cart',
				name: 'Cart',
				meta: {
					title: '장바구니',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/diet/cart.vue'),
			},
			{
				path: 'pay',
				name: 'Payment',
				meta: {
					title: '결제하기',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/order/payment.vue'),
			},
			/*
			{
				path: 'paycard',
				name: 'PayCard',
				meta: {
					title: '결제하기',
				},
				component: () => import('@/views/order/paycard.vue'),
			},
			*/
			{
				path: 'password',
				name: 'Password',
				meta: {
					title: '결제하기',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/order/password.vue'),
			},
			{
				path: 'paycomplete',
				name: 'PayComplete',
				meta: {
					title: '결제완료',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/order/paycomplete.vue'),
			},
		],
	},
	/* 마이페이지 */
	{
		path: '/mypage',
		name: '',
		component: () => import('@/Layout/homeLayout.vue'),
		children: [
			{
				path: '',
				name: 'Mypage',
				meta: {
					title: '마이페이지',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/index.vue'),
			},
			{
				path: 'point',
				name: 'Point',
				meta: {
					title: '포인트',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/point.vue'),
			},
			{
				path: 'coupon',
				name: 'Coupon',
				meta: {
					title: '나의쿠폰',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/coupon.vue'),
			},
			{
				path: 'info',
				name: 'UserInfo',
				meta: {
					title: '정보수정',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/userinfo.vue'),
			},
			{
				path: 'address',
				name: 'Address',
				meta: {
					title: '배송지 관리',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/address.vue'),
			},
			{
				path: 'card',
				name: 'Card',
				meta: {
					title: '결제카드 관리',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mypage/card.vue'),
			},
			{
				path: 'counsult',
				name: 'Counsult',
				meta: {
					title: '영양상담',
				},
				component: () => import('@/views/counsult/index.vue'),
			},
			{
				path: 'counsultview',
				name: 'CounsultView',
				meta: {
					title: '영양상담',
				},
				component: () => import('@/views/counsult/view.vue'),
			},
			{
				path: 'counsultform',
				name: 'CounsultForm',
				meta: {
					title: '영양상담',
				},
				component: () => import('@/views/counsult/form.vue'),
			},
			{
				path: 'counsultConfirm',
				name: 'CounsultConfirm',
				meta: {
					title: '삭제',
				},
				component: () => import('@/views/counsult/confirm.vue'),
			},
			{
				path: 'counsultComplete',
				name: 'CounsultComplete',
				meta: {
					title: '확인',
				},
				component: () => import('@/views/counsult/complete.vue'),
			},
			{
				path: 'feed',
				name: 'Feed',
				meta: {
					title: '건강피드',
				},
				component: () => import('@/views/feed/index.vue'),
			},
			{
				path: 'review',
				name: 'Review',
				meta: {
					title: '식단후기',
				},
				component: () => import('@/views/review/index.vue'),
			},
			{
				path: 'myReview',
				name: 'MyReview',
				meta: {
					title: '내 식단후기',
				},
				component: () => import('@/views/review/myReview.vue'),
			},
			{
				path: 'reviewList',
				name: 'ReviewList',
				meta: {
					title: '작성가능 후기',
				},
				component: () => import('@/views/review/review_list.vue'),
			},
			{
				path: 'reviewListEmpty',
				name: 'ReviewListEmpty',
				meta: {
					title: '작성가능 후기',
				},
				component: () => import('@/views/review/review_list_empty.vue'),
			},
			{
				path: 'reviewform/:idx',
				name: 'ReviewForm',
				meta: {
					title: '',
				},
				component: () => import('@/views/review/form.vue'),
			},
			{
				path: 'reviewEditform/:idx',
				name: 'ReviewEditForm',
				meta: {
					title: '',
				},
				component: () => import('@/views/review/editForm.vue'),
			},
			{
				path: 'reviewConfirm',
				name: 'ReviewConfirm',
				meta: {
					title: '',
				},
				component: () => import('@/views/review/confirm.vue'),
			},
			{
				path: 'reviewComplete',
				name: 'ReviewComplete',
				meta: {
					title: '',
				},
				component: () => import('@/views/review/complete.vue'),
			},
			{
				path: 'notice',
				name: 'Notice',
				meta: {
					title: '공지사항',
				},
				component: () => import('@/views/notice/index.vue'),
			},
			{
				path: 'noticeview',
				name: 'NoticeView',
				meta: {
					title: '공지사항',
				},
				component: () => import('@/views/notice/view.vue'),
			},
			{
				path: 'qna',
				name: 'Qna',
				meta: {
					title: '서비스 문의',
				},
				component: () => import('@/views/qna/index.vue'),
			},
			{
				path: 'qnaform',
				name: 'QnaForm',
				meta: {
					title: '서비스 문의',
				},
				component: () => import('@/views/qna/form.vue'),
			},
			{
				path: 'qnaview',
				name: 'QnaView',
				meta: {
					title: '서비스 문의',
				},
				component: () => import('@/views/qna/view.vue'),
			},
			{
				path: 'qnaConfirm',
				name: 'QnaConfirm',
				meta: {
					title: '확인',
				},
				component: () => import('@/views/qna/confirm.vue'),
			},
			{
				path: 'qnaComplete',
				name: 'QnaComplete',
				meta: {
					title: '확인',
				},
				component: () => import('@/views/qna/complete.vue'),
			},
			{
				path: 'term',
				name: 'Term',
				meta: {
					title: '이용약관',
				},
				// component: () => import('@/views/mypage/terms.vue'),
				component: () => import('@/views/mypage/privacy.vue'),
			},
			{
				path: 'privacy',
				name: 'Privacy',
				meta: {
					title: '개인정보처리방침',
				},
				component: () => import('@/views/mypage/privacy.vue'),
			},
			{
				path: 'withdrawal',
				name: 'Withdrawal',
				meta: {
					title: '회원탈퇴',
				},
				component: () => import('@/views/mypage/withdrawal.vue'),
			},
			{
				path: 'withdrawalConfirm',
				name: 'WithdrawalConfirm',
				meta: {
					title: '회원탈퇴',
				},
				component: () => import('@/views/mypage/withdrawal_confirm.vue'),
			},
			{
				path: 'withdrawalComplete',
				name: 'WithdrawalComplete',
				meta: {
					title: '회원탈퇴',
				},
				component: () => import('@/views/mypage/withdrawal_complete.vue'),
			},
		],
	},
	/* 배송조회 */
	{
		path: '/delivery',
		name: '',
		component: () => import('@/Layout/homeLayout.vue'),
		children: [
			{
				path: '/',
				name: 'Delivery',
				meta: {
					title: '주문조회',
				},
				component: () => import('@/views/delivery/index.vue'),
			},
			{
				path: 'step1',
				name: 'Step1',
				meta: {
					title: '주문상세/주문접수',
				},
				component: () => import('@/views/delivery/delivery_step1.vue'),
			},
			{
				path: 'step2',
				name: 'Step2',
				meta: {
					title: '주문상세/식단제공중',
				},
				component: () => import('@/views/delivery/delivery_step2.vue'),
			},
			{
				path: 'step3',
				name: 'Step3',
				meta: {
					title: '주문상세/배송완료',
				},
				component: () => import('@/views/delivery/delivery_step3.vue'),
			},
			{
				path: ':orderGroupId',
				name: 'DeliveryDetail',
				meta: {
					title: '주문상세',
				},
				component: () => import('@/views/delivery/deliveryDetail.vue'),
			},
			{
				path: 'cancelstep1',
				name: 'cancelStep1',
				meta: {
					title: '주문상세/부분취소',
				},
				component: () => import('@/views/delivery/cancelForm_step1.vue'),
			},
			{
				path: 'cancelstep2',
				name: 'cancelStep2',
				meta: {
					title: '주문상세/전체취소',
				},
				component: () => import('@/views/delivery/cancelForm_step2.vue'),
			},
			{
				path: 'cancelstep3',
				name: 'cancelStep3',
				meta: {
					title: '주문상세/취소확인',
				},
				component: () => import('@/views/delivery/cancelForm_step3.vue'),
			},
			{
				path: 'cancel',
				name: 'cancel',
				meta: {
					title: '주문상세/주문취소',
				},
				component: () => import('@/views/delivery/cancel.vue'),
			},
			{
				path: 'refundstep1',
				name: 'RefundStep1',
				meta: {
					title: '주문상세/배송완료',
				},
				component: () => import('@/views/delivery/refundForm_step1.vue'),
			},
			{
				path: 'refundstep2',
				name: 'RefundStep2',
				meta: {
					title: '주문상세/배송완료',
				},
				component: () => import('@/views/delivery/refundForm_step2.vue'),
			},
		],
	},
	/* 픽업장소 선택 */
	{
		path: '/map',
		name: '',
		component: () => import('@/Layout/mapLayout.vue'),
		children: [
			{
				path: '',
				name: 'Map',
				meta: {
					title: '',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/map/index.vue'),
			},
		],
	},
	// station Map 선택
	{
		path: '/map2',
		name: '',
		component: () => import('@/Layout/mapLayout.vue'),
		children: [
			{
				path: '',
				name: 'Map2',
				meta: {
					title: '',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/map/index2.vue'),
			},
		],
	},
	/* 챌린지 선택 */
	{
		path: '/challenge',
		name: '',
		component: () => import('@/Layout/challengeLayout.vue'),
		children: [
			{
				path: '/',
				name: 'Challenge',
				meta: {
					title: '챌린지',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/challenge/index.vue'),
			},
			{
				path: 'challengeDetail/:challengeUid',
				name: 'ChallengeDetail',
				meta: {
					title: '',
				},
				component: () => import('@/views/challenge/detail.vue'),
			},
			{
				path: 'challengeWrite',
				name: 'ChallengeWrite',
				meta: {
					title: '챌린지 글쓰기',
				},
				component: () => import('@/views/challenge/form.vue'),
			},
		],
	},
	/* 맞춤 미션 */
	{
		path: '/mission',
		name: '',
		component: () => import('@/Layout/missionLayout.vue'),
		children: [
			{
				path: '/',
				name: 'Mission',
				meta: {
					title: '맞춤 미션',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mission/index.vue'),
			},
			{
				path: '/inquiry/step1',
				name: 'MissionInquiry',
				meta: {
					title: '진단 설문',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mission/inquiry/step1.vue'),
			},
			{
				path: '/inquiry/step2',
				name: 'MissionInquiry2',
				meta: {
					title: '진단 설문',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mission/inquiry/step2.vue'),
			},
			{
				path: '/inquiry/step3',
				name: 'MissionInquiry3',
				meta: {
					title: '진단 설문',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/mission/inquiry/step3.vue'),
			},
			{
				path: '/my',
				name: 'MyMission',
				meta: {
					title: '나의 미션',
				},
				component: () => import('@/views/mission/my.vue'),
			},
			{
				path: '/history',
				name: 'MissionHistory',
				meta: {
					title: '지난 미션',
				},
				component: () => import('@/views/mission/history.vue'),
			},
			{
				path: 'missionDetail/:missionUid',
				name: 'MissionDetail',
				meta: {
					title: '맞춤 미션',
				},
				component: () => import('@/views/mission/detail.vue'),
			},
			{
				path: 'missionWrite',
				name: 'MissionWrite',
				meta: {
					title: '맞춤 미션 생성',
				},
				component: () => import('@/views/mission/form.vue'),
			},
		],
	},
	// TFSE 일지
	{
		path: '/tfse',
		name: '',
		component: () => import('@/Layout/tfseLayout.vue'),
		children: [
			{
				path: '/',
				name: 'Tfse',
				meta: {
					title: 'TFSE 일지',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/tfse/index.vue'),
			},
			{
				path: 'tfseWrite',
				name: 'TfseWrite',
				meta: {
					title: 'TFSE 일지',
				},
				component: () => import('@/views/tfse/form.vue'),
			},
			{
				path: 'tfseWrite/:tfseIdx',
				name: 'TfseUpdate',
				meta: {
					title: 'TFSE 일지 수정',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/tfse/form.vue'),
			},
			// 커뮤니티
			{
				path: '/community',
				name: 'Community',
				meta: {
					title: '커뮤니티',
				},
				component: () => import('@/views/tfse/community/index.vue'),
			},
			{
				path: '/communityDetail',
				name: 'CommunityDetail',
				meta: {
					title: '',
				},
				component: () => import('@/views/tfse/community/detail.vue'),
			},
		],
	},
	// 셀프피드백
	{
		path: '/selffeedback',
		name: '',
		component: () => import('@/Layout/tfseLayout.vue'),
		children: [
			{
				path: '/',
				name: 'SelfFeedback',
				meta: {
					title: '셀프 피드백',
				},
				component: () => import('@/views/selffeedback/index.vue'),
			},
			// {
			// 	path: 'tfseWrite',
			// 	name: 'TfseWrite',
			// 	meta: {
			// 		title: 'TFSE 일지',
			// 	},
			// 	component: () => import('@/views/tfse/tfseWrite.vue'),
			// },
		],
	},
	// 맞춤 제품
	{
		path: '/customproduct',
		name: '',
		component: () => import('@/Layout/customProductLayout.vue'),
		children: [
			// {
			// 	path: '/',
			// 	name: 'CustomProduct',
			// 	meta: {
			// 		title: '맞춤제품',
			// 		roles: ['ROLE_USER'],
			// 	},
			// 	component: () => import('@/views/customproduct/customproduct.vue'),
			// },
			{
				path: '/',
				name: 'CustomProduct',
				meta: {
					title: '정기배송 상품',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/customproduct/index.vue'),
			},
			{
				path: 'detail/:productIdx',
				name: 'CustomProductDetail',
				meta: {
					title: '정기배송 상품 상세보기',

				},
				component: () => import('@/views/customproduct/detail.vue'),
			},
			{
				path: 'order/:productIdx',
				name: 'CustomProductOrder',
				meta: {
					title: '정기배송 상품 주문하기',
				},
				component: () => import('@/views/customproduct/order.vue'),
			},
			{
				path: 'package',
				name: 'CustomPackageList',
				meta: {
					title: '패키지 상품',
					roles: ['ROLE_USER'],
				},
				component: () => import('@/views/customproduct/package/index.vue'),
			},
			{
				path: 'package/detail/:packageIdx',
				name: 'CustomPackageDetail',
				meta: {
					title: '패키지 상품 상세보기',
				},
				component: () => import('@/views/customproduct/package/detail.vue'),
			},
			{
				path: 'package/order/:packageIdx',
				name: 'CustomPackageOrder',
				meta: {
					title: '패키지 상품 주문하기',
				},
				component: () => import('@/views/customproduct/package/order.vue'),
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
