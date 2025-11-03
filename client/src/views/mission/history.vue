<template>
	<div v-loading="loading" class="mission grey">
		<div class="join-wr mission-history">
			<el-form ref="form" class="step1" onsubmit="return false;" :model="form" :rules="rules">
				<div class="info-wr">
					<div class="mission-wr">
						<div v-if="myMission.length === 0" class="mission-empty">
							<div class="empty-message">
								<p>지난 미션이 없습니다.</p>
								<p>새로운 미션을 시작해서 기록을 만들어보세요!</p>
							</div>
						</div>
						<div class="mission-item" v-for="mission in myMission" :key="mission.idx">
							<div class="category" :class="handleCategory(mission.category)">{{ mission.category }}</div>
							<div class="mission-item-inner">
								<div class="ttl">{{ mission.title }}</div>
								<div class="date">{{ formatDate(mission.startDate) }} ~ {{ formatDate(mission.endDate) }}</div>
								<div class="reward" :class="mission.dailyReward >= 0 ? 'plus' : 'minus'">
									<span>
										<slot>{{ getMissionReward(mission) }}원 포인트 </slot>
										<slot v-if="mission.dailyReward >= 0">{{ getMissionRewardStatus(mission) }}</slot>
										<!-- <slot v-else>차감</slot> -->
									</span>
								</div>
							</div>
							<div class="status" :class="getMissionStatus(mission)" v-if="mission.missionUserList.length > 0">
								<span v-if="mission.missionUserList[0].abandonStatus" class="status-abandon">✗ 포기</span>
								<span v-else-if="mission.missionUserList[0].recordCnt >= mission.totalDay" class="status-success">✓ 성공</span>
								<span v-else-if="mission.missionUserList[0].recordCnt < mission.totalDay" class="status-fail">✗ 실패</span>
							</div>
							<div class="arrow"><router-link :to="{ name: 'MissionDetail', params: { missionUid : mission.uid } }"><i class="el-icon-arrow-right"></i></router-link></div>
						</div>
					</div>
					<a @click="handleMission()" class="mission__detail-button blue">맞춤 미션 시작하기</a>
				</div>
			</el-form>
		</div>
	</div>
</template>

<style>
.el-form.step3 .select-box__gen .el-form-item__content {
	justify-content: end;
	display: flex;
}

.el-form.step3 .step-box .el-input__inner {
	padding-right: 30px;
}

.mission-empty {
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

.status.success {
	/* background-color: #e7f7e7; */
}

.status.fail {
	/* background-color: #fff2f0; */
}

.status.abandon {
	/* background-color: #f6f6f6; */
}

.status-success {
	color: #52c41a !important;
	font-weight: bold;
}

.status-fail {
	color: #ff4d4f !important;
	font-weight: bold;
}

.status-abandon {
	color: #999999 !important;
	font-weight: bold;
}
</style>

<script lang="ts">
import { getUserInfo, updateUserInfo } from '@/api/user';
import { getMissionList, getMissionCategory } from '@/api/mission';
import { Component, Vue } from 'vue-property-decorator';

@Component({
	components: {
	},
})
export default class extends Vue {
	async mounted() {
		await this.getUserInfo();
		this.getMissionList();
	}

	private myMission: any = [];

	private loading = true;

	private listQuery = {
		categoryUid: '',
		myFlag: true,
		status: 'past',
		page: 0,
		size: 20,
	}

	private form = {
		missionId: 1,
	}

	private addressVisible = false;

	private rules: any = {
	};

	private getUserInfo() {
		getUserInfo().then((res) => {
			this.form = res.data;
		});
	}

	private handleUpdateUserInfo() {
		if (!this.form.missionId) {
			this.$message.warning('항목을 모두 선택해주세요.');
		} else {
			this.$router.push({ name: 'MissionInquiry2' });
		}
	}

	private handleMission() {
		this.$router.push({ name: 'Mission' });
	}

	private handleCategory(category: any) {
		let str = '';
		if (category === '야식') {
			str = 'orange';
		} else if (category === '운동') {
			str = 'blue';
		} else if (category === '식단') {
			str = 'green';
		} else if (category === '습관') {
			str = 'purple';
		} else {
			str = 'grey';
		}
		return str;
	}

	private getMissionStatus(mission: any) {
		if (mission.missionUserList.length > 0) {
			const missionUser = mission.missionUserList[0];

			// 포기한 미션인지 확인
			if (missionUser.abandonStatus) {
				return 'abandon';
			}

			const recordCnt = missionUser.recordCnt || 0;
			return recordCnt >= mission.totalDay ? 'success' : 'fail';
		}
		return 'fail';
	}

	private formatDate(dateString: string) {
		if (!dateString) return '';
		const date = new Date(dateString);
		return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`;
	}

	private getMissionReward(mission: any): number {
		if (!mission.missionUserList || mission.missionUserList.length === 0) {
			return 0;
		}

		const missionUser = mission.missionUserList[0];

		// 포기한 미션이면 보상 없음
		if (missionUser.abandonStatus) {
			return 0;
		}

		// 미션을 완료한 경우에만 전체 보상 지급
		const recordCnt = missionUser.recordCnt || 0;
		if (recordCnt >= mission.totalDay) {
			return mission.dailyReward || 0;
		}

		// 미션을 완료하지 못한 경우 보상 없음
		return 0;
	}

	private getMissionRewardStatus(mission: any): string {
		if (!mission.missionUserList || mission.missionUserList.length === 0) {
			return '보상 없음';
		}

		const missionUser = mission.missionUserList[0];

		// 포기한 미션
		if (missionUser.abandonStatus) {
			return '보상 없음';
		}

		// 미션 완료 여부에 따른 상태
		const recordCnt = missionUser.recordCnt || 0;
		if (recordCnt >= mission.totalDay) {
			return '획득';
		}
		return '보상 없음';
	}

	private getMissionList() {
		this.listQuery.myFlag = true;
		getMissionList(this.listQuery).then((res: any) => {
			this.myMission = res.data;
			this.loading = false;
		}).catch(() => {
			this.$message.warning('미션 히스토리를 조회하는데 실패했습니다.');
			this.loading = false;
		});
	}
}
</script>
