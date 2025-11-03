<template>
	<div>
		<div>
			<div class="tfse">
				<div class="tfse__top">
					<p class="tfse__top-date">
						<img src="~@/assets/images/left-arrow.png" class="tfse__top-left__arrow" alt="왼쪽" @click="handleAddDay(-1)">
							{{ tfseDate | parseDate('YYYY년 MM월 DD일') }}
						<img src="~@/assets/images/right-arrow.png" class="tfse__top-right__arrow" alt="오른쪽" @click="handleAddDay(1)">
					</p>

					<div class="tfse__nav-box">
						<router-link :to=" { name: 'Tfse', query: { tfseDate: tfseDate } } " class="tfse__nav">TFSE 일지</router-link>
						<router-link :to=" { name: 'SelfFeedback' } " class="tfse__nav color2">셀프피드백</router-link>
					</div>
				</div>
				<div class="feedback__content">
					<div class="feedback__content-list">
						<div class="feedback__content-item">
							<div class="feedback__content-ttl sft bd6">어느 시점부터 폭식이 시작되었나요?</div>
							<div class="feedback__content-textarea">
								<textarea v-model="selfFeedback.question1" name="feedback_1" id="feedback_1" cols="30" rows="10" placeholder="내용을 입력해주세요"></textarea>
							</div>
						</div>
						<div class="feedback__content-item">
							<div class="feedback__content-ttl sft bd6">무엇이 진짜 원인일까요?</div>
							<div class="feedback__content-textarea">
								<textarea v-model="selfFeedback.question2" name="feedback_2" id="feedback_2" cols="30" rows="10" placeholder="내용을 입력해주세요"></textarea>
							</div>
						</div>
						<div class="feedback__content-item">
							<div class="feedback__content-ttl sft bd6">다음에 똑같은 상황을 마주한다면, 어떻게 하면 좋을까요?</div>
							<div class="feedback__content-textarea">
								<textarea v-model="selfFeedback.question3" name="feedback_3" id="feedback_3" cols="30" rows="10" placeholder="내용을 입력해주세요"></textarea>
							</div>
						</div>
					</div>
					<button @click="addFeedback" class="unid__btn">저장하기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import moment from 'moment';
import { getFeedback, addFeedback } from '@/api/tfse';

@Component({
  components: {
  },
})

export default class extends Vue {
  mounted() {
	this.getFeedback();
	if (this.$route.query.tfseDate) {
			this.tfseDate = this.$route.query.tfseDate;
	} else {
		this.$router.replace({ ...this.$router, query: { ...this.$route.query, tfseDate: this.tfseDate } });
	}
	// if (this.$route.query.tab) this.type = this.$route.query.tab;
  }

  private tfseDate: any = this.$route.query.tfseDate ? this.$route.query.tfseDate : moment().format('YYYY-MM-DD');

  private type: any = 'tfse';

  private listQuery = {
	searchDate: this.tfseDate,
	}

  private selfFeedback: any = {
	selfFeedbackDate: '',
	question1: '',
	question2: '',
	question3: '',
  }

  private getFeedback() {
	getFeedback(this.listQuery).then((res) => {
		this.selfFeedback.question1 = res.data.question1;
		this.selfFeedback.question2 = res.data.question2;
		this.selfFeedback.question3 = res.data.question3;
	});
  }

  private handleAddDay(days: number) {
    this.tfseDate = moment(this.tfseDate, 'YYYY-MM-DD').add(days, 'days').format('YYYY-MM-DD');
	this.listQuery.searchDate = this.tfseDate;
    this.$router.replace({ ...this.$router, query: { tfseDate: this.tfseDate } });
	this.getFeedback();
  }

  private addFeedback() {
	this.selfFeedback.selfFeedbackDate = this.tfseDate;
      addFeedback(this.selfFeedback).then(() => {
        this.$message.info('정상적으로 수정되었습니다.');
		this.$router.push({ name: 'SelfFeedback' });
      });
  }
}
</script>
