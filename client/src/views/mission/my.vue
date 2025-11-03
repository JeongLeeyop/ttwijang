<template>
	<div v-loading="loading" class="mission grey">
		<div class="join-wr mission-my">
			<el-form ref="form" class="step1" onsubmit="return false;" :model="form" :rules="rules">
				<div class="info-wr">
					<div class="mission-wr">
						<div v-if="myMission.length === 0" class="mission-empty">
							<div class="empty-message">
								<p>선택 가능한 미션이 없습니다.</p>
								<p>관리자에게 문의해주세요.</p>
							</div>
						</div>
						<div v-else>
							<div class="mission-item" v-for="mission in myMission" :key="mission.idx">
								<div class="category" :class="handleCategory(mission.missionCategory.name)">{{ mission.missionCategory.name }}</div>
								<div class="ttl">{{ mission.title }}</div>
								<el-checkbox class="checkbox" v-model="selectedMissions" :label="mission.uid"></el-checkbox>
							</div>
						</div>
					</div>
					<a style="cursor: pointer;" @click="handleMission()" class="mission__detail-button blue">맞춤 미션 시작하기</a>
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
</style>

<script lang="ts">
import { getUserInfo, updateUserInfo } from '@/api/user';
import { getMissionList, joinMission, joinMultipleMissions } from '@/api/mission';
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

	private selectedMissions: string[] = [];

	private listQuery = {
		categoryUid: '',
		myFlag: true,
		status: 'unapproved',
		page: 0,
		size: 20,
	}

	private form = {
		uid: '',
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
		if (!this.form.uid) {
			this.$message.warning('항목을 모두 선택해주세요.');
		} else {
			this.$router.push({ name: 'MissionInquiry2' });
		}
	}

	private handleMission() {
		if (!this.selectedMissions || this.selectedMissions.length === 0) {
			this.$message.warning('미션을 선택해주세요.');
			return;
		}

		const missionCount = this.selectedMissions.length;
		const missionText = missionCount === 1 ? '미션을' : `${missionCount}개의 미션을`;

		this.$confirm(`정말로 선택한 ${missionText} 신청하시겠습니까?`, '미션 신청', {
			confirmButtonText: '신청',
			cancelButtonText: '취소',
			type: 'warning',
		}).then(() => {
			this.loading = true;
			// 다중 미션 신청 API 호출
			joinMultipleMissions(this.selectedMissions).then(() => {
				this.$message.success(`${missionCount}개의 미션 신청이 완료되었습니다.`);
				this.$router.push({ name: 'Mission' });
				this.loading = false;
			}).catch(() => {
				this.$message.error('미션 신청에 실패했습니다.');
				this.loading = false;
			});
		}).catch(() => {
			this.$message.info('미션 신청이 취소되었습니다.');
		});
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

	private getMissionList() {
		this.listQuery.myFlag = true;
		this.listQuery.status = 'unapproved';
		getMissionList(this.listQuery).then((res: any) => {
			this.myMission = res.data;
			this.loading = false;
		}).catch(() => {
			this.$message.warning('미션 리스트를 조회하는데 실패했습니다.');
			this.loading = false;
		});
	}
}
</script>
