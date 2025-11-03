<template>
  <div v-loading="loading" class="mission">
		<div class="summary">
			<div class="summary-ttl">
				{{ user.actualName }}님의 맞춤 미션 진행 현황
			</div>
			<div class="summary-content">
				<div class="completeCnt">
					<div class="ttl">미션 완료</div>
					<div class="txt">{{ summary.completeCnt }}건</div>
				</div>
				<div class="sucessRate">
					<div class="ttl">미션 성공률</div>
					<div class="txt">{{ summary.successRate ? summary.successRate.toFixed(1) : 0 }}%</div>
				</div>
				<div class="result">
					<div class="ttl">미션 성과</div>
					<div class="txt">-{{ summary.totalWeightLoss ? summary.totalWeightLoss.toFixed(1) : 0 }}kg</div>
				</div>
			</div>
		</div>
		<router-link :to="{ name: 'MyMission'}" class="mission__detail-button blue">새로운 미션 시작하기</router-link>
		<div class="mission__list">
			<div class="mission__list_ttl">진행 중인 미션</div>
			<div v-if="missionList.length === 0" class="mission__empty">
				<div class="empty-message">
					<p>진행 중인 미션이 없습니다.</p>
					<p>새로운 미션을 시작해보세요!</p>
				</div>
			</div>
			<div v-for="mission in missionList" :key="mission.uid" class="mission__item">
				<div class="mission__item-img">
					<img v-if="mission.mainImageList.length > 0 && mission.mainImageList[0]" :src="`${apiUrl}/attached-file/${mission.mainImageList[0].fileUid}`" class="img464-1" alt="">
					<img v-else src="~@/assets/images/tmp_challenge_img.jpg" />
				</div>
				<div class="mission__item-content">
					<div class="mission__item-ttl">
						<span class="tag tag01">{{ mission.title  }}</span>
					</div>
					<div class="mission__item-date">
						{{ mission.startDate | parseDate }} ~ {{ mission.endDate | parseDate }}
					</div>
					<div class="mission__item-reward">
						<span class="txt">미션 완료시 {{ mission.dailyReward  }}원</span>
					</div>
					<!-- 현재 진행중인 미션인 경우 -->
					<div>
						<div class="mission__item-btn" v-if="mission.missionUserList && mission.missionUserList.length > 0 && mission.missionUserList[0].todayWriteStatus > 0">
							<router-link :to="{ name: 'MissionDetail', params: { missionUid : mission.uid } }" class="mission__item-button sft" style="background: #58c0d6;">
								<span>미션 인증완료</span>
							</router-link>
						</div>
						<div class="mission__item-btn" v-else>
							<router-link :to="{ name: 'MissionDetail', params: { missionUid : mission.uid } }" class="mission__item-button sft">
								<span>미션 인증하기</span>
							</router-link>
						</div>
					</div>
				</div>
			</div>
		</div>
		<router-link :to="{ name: 'MissionHistory'}" class="mission__detail-button">지난 미션 확인하기</router-link>
		<el-drawer :visible.sync="drawer" direction="btt" :before-close="handleClose" title="오늘 맞춤 미션 하셨나요?">
			<div class="content">
				<div class="txt">오늘의 미션을 하고 목표를 달성해 보아요.<br>
					꾸준히 하는 것은 어렵지만 함께하면 할 수 있어요.
				</div>
				<div class="btn">
					<router-link :to="{ name: 'MyMission'}" class="mission__detail-button blue">맞춤 미션 바로가기</router-link>
				</div>
			</div>
		</el-drawer>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getMissionList, getMissionSummary } from '@/api/mission';
import { getUserInfo, updateUserInfo } from '@/api/user';
// 쿠키 관리를 위한 간단한 유틸리티 함수들
const setCookie = (name: string, value: string, minutes: number) => {
  const date = new Date();
  date.setTime(date.getTime() + (minutes * 60 * 1000));
  const expires = `expires=${date.toUTCString()}`;
  document.cookie = `${name}=${value};${expires};path=/`;
};

const getCookie = (name: string): string | null => {
  const nameEQ = `${name}=`;
  const ca = document.cookie.split(';');
  for (let i = 0; i < ca.length; i += 1) {
    let c = ca[i];
    while (c.charAt(0) === ' ') c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

@Component({
	components: {
	},
})
export default class extends Vue {
	async mounted() {
		await this.getUserInfo();
		this.getMissionList();
		this.getMissionCategory();
		this.getMissionSummary();
		this.checkDrawerVisibility();
		this.checkPushNotificationRedirect();
	}

	private drawer = false;

	private now = new Date();

	private loading = true;

	private apiUrl = process.env.VUE_APP_COMMON_API;

	private totalElements = 0;

	private listQuery = {
		categoryUid: '',
		myFlag: true,
		status: 'current',
		page: 0,
		size: 5,
	}

	private summary: any = {
		completeCnt: 0,
		successRate: 0,
		totalWeightLoss: 0,
	};

	private missionList: any = [];

	private categoryList: any = [];

	private getMissionList() {
		this.listQuery.myFlag = true;
		getMissionList(this.listQuery).then((res: any) => {
			this.missionList = res.data;
		}).catch(() => {
			this.$message.warning('미션 리스트를 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

	private getMissionCategory() {
		// getMissionCategory().then((res: any) => {
		// 	this.categoryList = res.data;
		// }).catch(() => {
		// 	this.$message.warning('챌린지 리스트를 조회하는데 실패했습니다.');
		// });
		this.loading = false;
	}

	private searchMissionList(categoryUid: string) {
		this.listQuery.categoryUid = categoryUid;
		this.getMissionList();
	}

	private user: any = {};

	private async getUserInfo() {
		await getUserInfo().then((res) => {
			this.user = res.data;
		});
		if (!this.user.missionInquiryStatus) {
			this.$router.push({ name: 'MissionInquiry' });
		}
	}

	private getMissionSummary() {
		getMissionSummary().then((res: any) => {
			this.summary = res.data;
		}).catch(() => {
			// 에러 시 기본값 유지
			console.log('미션 요약 정보를 불러오는데 실패했습니다.');
		});
	}

	private handleClose(done: any) {
		// drawer를 닫을 때 쿠키 설정 (5분간 유효)
		setCookie('missionDrawerClosed', 'true', 5);
		this.drawer = false;
		done();
	}

	// drawer 표시 여부를 확인하는 메서드
	private checkDrawerVisibility() {
		const drawerCookie = getCookie('missionDrawerClosed');
		if (!drawerCookie) {
			// 쿠키가 없으면 drawer 표시
			// drawer 기본적으로 false로 설정
			// this.drawer = true;
		}
	}

	// 푸시 알람으로부터 리다이렉트된 경우 drawer 표시
	private checkPushNotificationRedirect() {
		// URL query parameter 확인
		const fromPush = this.$route.query.fromPush;
		const showDrawer = this.$route.query.showDrawer;
		if (fromPush === 'true' || showDrawer === 'true') {
			// 푸시 알람으로부터 온 경우 drawer 강제 표시
			this.drawer = true;
			// URL에서 query parameter 제거 (깔끔한 URL 유지)
			const newQuery = { ...this.$route.query };
			delete newQuery.fromPush;
			delete newQuery.showDrawer;
			this.$router.replace({
				name: this.$route.name as string,
				params: this.$route.params,
				query: Object.keys(newQuery).length > 0 ? newQuery : undefined,
			});
		}
	}
}

</script>

<style scoped>
.mission__empty {
	text-align: center;
	padding: 40px 20px;
	background-color: #f8f9fa;
	border-radius: 8px;
	margin: 20px 0;
}

.empty-message p {
	margin: 5px 0;
	color: #666;
	font-size: 16px;
}

.empty-message p:first-child {
	font-weight: 600;
	color: #333;
}

/* 완료된 미션 버튼 스타일 */
.mission__item-button--completed {
	border: none !important;
	text-decoration: none !important;
	pointer-events: none;
	opacity: 0.9;
}

.mission__item-button--completed:hover {
	opacity: 0.9;
	transform: none;
}
</style>
