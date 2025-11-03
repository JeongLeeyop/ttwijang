<template>
		<div>
			<div v-loading="loading" class="tfse">
				<div class="tfse__top">
					<p class="tfse__top-date">
						<img src="~@/assets/images/left-arrow.png" class="tfse__top-left__arrow" alt="왼쪽"
							@click="handleAddDay(-1)">
						{{ tfseDate | parseDate('YYYY년 MM월 DD일') }}
						<img src="~@/assets/images/right-arrow.png" class="tfse__top-right__arrow" alt="오른쪽"
							@click="handleAddDay(1)">
					</p>

					<div class="tfse__nav-box">
						<router-link :to="{ name: 'Tfse' }" class="tfse__nav color2">TFSE 일지</router-link>
						<router-link :to="{ name: 'SelfFeedback', query: { tfseDate: tfseDate } }" class="tfse__nav">셀프피드백</router-link>
					</div>
				</div>
				<div class="tfse__content">
					<div class="community__list">
						<div v-for="(tfse, index) in tfseData" :key="index"  class="community__item" @click="editTfse(tfse.idx)">
							<div class="community__top">
								<span class="community__top-time">{{ tfse.tfseDate | parseTimeAt }}</span>
								<div class="community__top-side">
									포만감
									<span class="community__top-side__num">{{ tfse.satietyScore }}</span>
								</div>
							</div>
							<div class="community__content">
								<div class="community__content-ttl sft">{{ tfse.foodText }}</div>
								<div class="community__content-txt">{{ tfse.emotionText }}</div>
							</div>
							<div class="tfse__switch">
								<div class="tfse__switch-wrap">
									<span class="tfse__switch-txt">공개여부</span>
									<div class="tfse__switch-btn" @click.stop>
										<input type="checkbox" :id="'switch'+index" :checked="!tfse.secretStatus" @change="updateSecretStatus(tfse.idx, $event.target.checked)">
										<label :for="'switch'+index" class="switch_label">
											<span class="onf_btn"></span>
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<button class="tfse__content--none" @click="writeHandler">
						<img src="~@/assets/images/tfse_none_icon.svg" alt="">
					</button>
				</div>
				<button @click="writeHandler" class="unid__btn mg-bottom">일지 추가하기</button>
		</div>
		</div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { Route } from 'vue-router';
import moment from 'moment';
import { getTfseList, updateSecretStatus } from '@/api/tfse';

@Component({
	components: {
	},
})

export default class extends Vue {
	mounted() {
		this.getTfseList();
		if (this.$route.query.tfseDate) {
			this.tfseDate = this.$route.query.tfseDate;
		} else {
			this.$router.replace({ ...this.$router, query: { ...this.$route.query, tfseDate: this.tfseDate } });
		}
		// if (this.$route.query.tab) this.type = this.$route.query.tab;
	}

	// 라우트가 변경될 때마다 호출되는 네비게이션 가드
	beforeRouteUpdate(to: Route, from: Route, next: () => void) {
		this.getTfseList();
		next();
	}

	private loading = true;

	private tfseDate: any = this.$route.query.tfseDate ? this.$route.query.tfseDate : moment().format('YYYY-MM-DD');

	private listQuery = {
		searchDate: this.tfseDate,
	}

	private tfseData: any = [];

	// private type: any = 'diary';

	private handleAddDay(days: number) {
		this.tfseDate = moment(this.tfseDate, 'YYYY-MM-DD').add(days, 'days').format('YYYY-MM-DD');
		this.listQuery.searchDate = this.tfseDate;
		this.$router.replace({ ...this.$router, query: { tfseDate: this.tfseDate } });
		this.getTfseList();
	}

	private updateSecretStatus(idx: number, secretStatus: boolean) {
		this.loading = true;
		secretStatus = !secretStatus;
		updateSecretStatus({ idx, secretStatus }).then((res) => {
			this.loading = false;
		});
	}

	private getTfseList() {
		getTfseList(this.listQuery).then((res) => {
			this.tfseData = res.data;
			this.loading = false;
		});
	}

	private editTfse(idx: any) {
		this.$router.push({ name: 'TfseUpdate', params: { tfseIdx: idx } });
	}

	private writeHandler() {
		if (this.tfseData.length >= 6) {
			this.$message.info('최대 6개까지 작성이 가능합니다');
		} else this.$router.push({ name: 'TfseWrite', query: { tfseDate: this.tfseDate } });
	}
}
</script>
