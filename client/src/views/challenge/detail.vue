<template>
  <div v-loading="loading" class="challenge challenge__detail">
    <div class="challenge__detail-carousel">
      <VueSlickCarousel v-bind="set3">
         <div v-for="image in challenge.mainImageList" :key="image.uid" class="carousel__item">
           <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" class="img464-1" alt="">
           <img v-else src="~@/assets/images/tmp_cert_img.jpg" alt="">
          </div>
      </VueSlickCarousel>
      <!-- <VueSlickCarousel v-bind="set2" class="imgca"> -->
          <!-- <div v-for="image in challenge.mainImageList" :key="image.uid"> -->
              <!-- <div class="slide_box2"> -->
                <!-- <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" /> -->
              <!-- </div> -->
          <!-- </div> -->
        <!-- </VueSlickCarousel> -->
    </div>
    <div class="challenge__detail-container">
      <div class="challenge__detail-content">
        <div class="challenge__detail-ttl sft">{{ challenge.title }}</div>
        <div class="challenge__detail-participant">
          <img src="~@/assets/images/challenge_person_icon_02.svg" alt="" />
          <!-- <span class="num">2,500</span>명 챌린지 참여 중 -->
					<span class="challenge__item-participate-txt">{{ challenge.participantCnt | parseKrw }}명 챌린지 참여 중</span>
        </div>
        <div class="challenge__detail-txt" v-html="convertedText(challenge.content)">
          {{ challenge.content }}
        </div>
      </div>
      <div class="challenge__detail-cert">
        <div class="challenge__detail-cert__ttl">챌린지 인증</div>
        <div class="challenge__detail-cert__content">
          <div class="challenge__detail-cert__content-list">
            <div class="challenge__detail-cert__content-item" v-for="(line, index) in linesWithLiTags(challenge.authMethodInfo)" :key="index" v-html="line"></div>
            <!-- <div class="challenge__detail-cert__content-item"> -->
              <!-- 샐러드 외 다른 음식 사진은 등록하지 마세요. -->
            <!-- </div> -->
          </div>
          <div class="challenge__detail-cert__ttl02 bd7">사진예시</div>
          <div class="challenge__detail-cert__img">
            <div v-for="image in challenge.sampleImageList" :key="image.uid" class="challenge__detail-cert__img-item">
              <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" class="img464-1" alt="">
              <img v-else src="~@/assets/images/tmp_cert_img.jpg" alt="">
            </div>
          </div>
        </div>
      </div>
    </div>
		<div class="challenge__detail-picture">
			<div class="challenge__detail-picture__ttl sft">
        <button @click="getChallengeRecordList()" :class="{'active': !listQuery.myFlag}" class="challenge__detail-picture__btn">전체</button>
        <button @click="getChallengeRecordList('my')" :class="{'active': listQuery.myFlag}" class="challenge__detail-picture__btn">내 후기</button>
      </div>
			<div class="challenge__detail-picture__list">
				<div @click="RecordEditHandler(record.uid)" v-for="record in challenge.recordList" :key="record.uid" class="challenge__detail-picture__item">
					<!-- <div class="challenge__detail-picture__item-img"> -->
						<!-- <img src="~@/assets/images/tmp_challenge_detail_picture.jpg" alt=""> -->
					<!-- </div> -->
					<div class="challenge__detail-picture__item-profile">
            <img v-if="record.fileList[0]" :src="`${apiUrl}/attached-file/${record.fileList[0].fileUid}`" alt="">
						<img style="width=100%;max-height: 200px;" v-else src="~@/assets/images/challenge_detail_picture_profile.svg" alt="">
						<span style="margin-right:30px" class="txt">{{ record.createDate | parseDate }}</span>
            <span class="txt">{{ record.writer }} 고객님</span>
					</div>
          <div class="challenge__detail-picture__item-ttl">
            {{ record.content }}
          </div>
				</div>
			</div>
		</div>
    <a v-if="!challenge.userJoinStatus" @click="joinChallenge" class="challenge__detail-button sft">
			<img src="~@/assets/images/challenge_detail_v_icon.svg" alt="">
			챌린지 참여하기
		</a>
    <a  v-else-if="dueOverFlag" class="challenge__detail-button sft">
      챌린지가 종료되었습니다.
    </a>
    <div v-else-if="challenge.todayWriteStatus" style="padding:30px 0 80px">
      오늘은 이미 작성을 완료하셨습니다.
    </div>
		<router-link v-else-if="challenge.userJoinStatus" :to="{ name: 'ChallengeWrite', params: { challengeUid: this.$route.params.challengeUid },}" class="challenge__detail-button sft">
			<img src="~@/assets/images/challenge_detail_v_icon.svg" alt="">
			챌린지 기록 작성
		</router-link>
  </div>
</template>

<script lang='ts'>
import { Vue, Component } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import { getChallengeDetail, joinChallenge, getChallengeRecordList } from '@/api/challenge';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  async mounted() {
		await this.getChallengeDetail();
	}

	private dueOverFlag = false;

  private loading = true;

	private apiUrl = process.env.VUE_APP_COMMON_API;

  private set3 = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 2,
    slidesToScroll: 1,
  };

  private listQuery = {
		challengeUid: this.$route.params.challengeUid,
    myFlag: false,
	}

	private challenge: any = {};

  private async getChallengeDetail() {
		await getChallengeDetail(this.$route.params.challengeUid).then((res) => {
			this.challenge = res.data;
      if (this.challenge.challengeUser !== null) {
        this.dueOverFlag = new Date() >= new Date(this.challenge.challengeUser.dueDate);
      }
		}).catch(() => {
			this.$message.warning('챌린지를 조회하는데 실패했습니다.');
		});
    this.set3 = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
      };
		this.loading = false;
	}

  private async getChallengeRecordList(type: string) {
    this.loading = true;
    if (type === 'my') this.listQuery.myFlag = true;
    else this.listQuery.myFlag = false;
		await getChallengeRecordList(this.listQuery).then((res) => {
			this.challenge.recordList = res.data;
		}).catch(() => {
			this.$message.warning('챌린지 기록을 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

  private async joinChallenge() {
    this.$confirm('정말 챌린지에 참여하시겠습니까?').then(() => {
      joinChallenge({ challengeUid: this.$route.params.challengeUid }).then(() => {
        this.$message.success('챌린지에 참여 하셨습니다.');
        this.$router.push({ name: 'Challenge', params: { boardUid: this.$route.params.boardUid } });
      }).catch((error) => {
        this.$message.error(error.response.data.message || '챌린지에 참여 하는데 실패했습니다.');
      });
    });
	}

  private RecordEditHandler(uid: string) {
    if (this.listQuery.myFlag) {
      this.$router.push({ name: 'ChallengeWrite', params: { challengeUid: this.$route.params.challengeUid, uid } });
    }
  }

  private convertedText(text: string) {
      return text.replace(/\n/g, '</br>');
  }

  private linesWithLiTags(text: string) {
      return text.split('\n');
  }
}
</script>
