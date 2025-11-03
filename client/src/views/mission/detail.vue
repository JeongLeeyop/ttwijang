<template>
  <div v-loading="loading" class="mission__detail">
    <div class="mission__detail-carousel">
      <VueSlickCarousel v-bind="set3">
         <div v-for="image in mission.mainImageList" :key="image.uid" class="carousel__item">
           <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" class="img464-1" alt="">
           <img v-else src="~@/assets/images/tmp_cert_img.jpg" alt="">
          </div>
      </VueSlickCarousel>
      <!-- <VueSlickCarousel v-bind="set2" class="imgca"> -->
          <!-- <div v-for="image in mission.mainImageList" :key="image.uid"> -->
              <!-- <div class="slide_box2"> -->
                <!-- <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" /> -->
              <!-- </div> -->
          <!-- </div> -->
        <!-- </VueSlickCarousel> -->
    </div>
    <div class="mission__detail-container">
      <div class="mission__detail-content">
        <div class="mission__detail-ttl sft">{{ mission.title }}</div>
        <!-- <div class="mission__detail-participant">
          <img src="~@/assets/images/challenge_person_icon_02.svg" alt="" />
					<span class="mission__item-participate-txt">{{ mission.participantCnt | parseKrw }}명 미션 참여 중</span>
        </div> -->
        <div class="mission__detail-inner">
          <span class="mission__item-date">{{ mission.startDate | parseDate }} ~ {{ mission.endDate | parseDate }}</span>
          <div class="mission__item-info-row">
            <span class="mission__item-target-tag">성공 기준 {{ mission.totalDay ? mission.totalDay : 0 }}일</span>
            <span class="mission__item-achieve">{{ mission.achieveEffect ? mission.achieveEffect : 0 }}Kg 감량</span>
          </div>
        </div>
        <div class="mission__detail-txt" v-html="convertedText(mission.content)">
        </div>
      </div>
      <div class="mission__detail-cert">
        <div class="mission__detail-cert__ttl">미션 인증하는 방법</div>
        <div class="mission__detail-cert__content">
          <div class="mission__detail-cert__content-list">
            <div class="mission__detail-cert__content-item" v-for="(line, index) in linesWithLiTags(mission.authMethodInfo)" :key="index" v-html="line"></div>
            <!-- <div class="mission__detail-cert__content-item"> -->
              <!-- 샐러드 외 다른 음식 사진은 등록하지 마세요. -->
            <!-- </div> -->
          </div>
          <div class="mission__detail-cert__ttl02 bd7">사진예시</div>
          <div class="mission__detail-cert__img">
            <div v-for="image in mission.sampleImageList" :key="image.uid" class="mission__detail-cert__img-item">
              <img v-if="image" :src="`${apiUrl}/attached-file/${image.fileUid}`" class="img464-1" alt="">
              <img v-else src="~@/assets/images/tmp_cert_img.jpg" alt="">
            </div>
          </div>
        </div>
      </div>
    </div>
		<div class="mission__detail-picture">
			<div class="mission__detail-picture__ttl sft">
        <!-- <button @click="getMissionRecordList()" :class="{'active': !listQuery.myFlag}" class="mission__detail-picture__btn">전체</button> -->
        <button @click="getMissionRecordList('my')" :class="{'active': listQuery.myFlag}" class="mission__detail-picture__btn">내 인증</button>
      </div>
			<div class="mission__detail-picture__list">
        <!-- 인증글이 없을 때 안내 문구 -->
        <div v-if="!recordList || recordList.length === 0" class="mission__detail-picture__empty">
          <div class="empty-message">
            <p class="empty-text">{{ listQuery.myFlag ? '작성한 인증글이 없습니다.' : '등록된 인증글이 없습니다.' }}</p>
            <p class="empty-subtext">{{ listQuery.myFlag ? '첫 번째 인증글을 작성해보세요!' : '첫 번째 인증글을 기다리고 있어요.' }}</p>
          </div>
        </div>
        <!-- 인증글 목록 -->
        <template v-else>
          <div @click="RecordEditHandler(record.uid)" v-for="record in recordList" :key="record.uid" class="mission__detail-picture__item">
            <!-- <div class="mission__detail-picture__item-img"> -->
              <!-- <img src="~@/assets/images/tmp_challenge_detail_picture.jpg" alt=""> -->
            <!-- </div> -->
            <div class="mission__detail-picture__item-profile">
              <img v-if="record.fileList[0]" :src="`${apiUrl}/attached-file/${record.fileList[0].fileUid}`" alt="">
              <img style="width: 100%; max-height: 200px;" v-else src="~@/assets/images/challenge_detail_picture_profile.svg" alt="">
              <span style="margin-right:30px" class="txt">{{ formatDate(record.createDate) }}</span>
              <span class="txt">{{ record.writer }} 고객님</span>
            </div>
            <div class="mission__detail-picture__item-ttl">
              {{ record.content }}
            </div>
          </div>
        </template>
			</div>
		</div>
    <!-- <a v-if="!mission.userJoinStatus" @click="joinMission" class="mission__detail-button sft">
			<img src="~@/assets/images/challenge_detail_v_icon.svg" alt="">
			미션 참여하기
		</a> -->
    <div class="mission__detail-button-wr">
    <!-- 지난 미션인 경우 -->
      <div v-if="isPastMission()" class="mission__detail-button sft mission__detail-button--completed" :style="getMissionCompletionStyle()">
        {{ getMissionCompletionMessage() }}
      </div>
      <!-- 현재 진행중인 미션인 경우 -->
      <template v-else>
        <a v-if="mission.dueOverFlag" class="mission__detail-button sft">
          미션이 종료되었습니다.
        </a>
        <div v-else-if="mission.todayWriteStatus" class="mission__detail-button sft">
          미션 인증이 완료되었습니다.
        </div>
        <router-link v-else :to="{ name: 'MissionWrite', params: { missionUid: this.$route.params.missionUid },}" class="mission__detail-button sft">
          <!-- <img src="~@/assets/images/challenge_detail_v_icon.svg" alt=""> -->
          맞춤 미션 인증하기
        </router-link>
        <!-- 미션 포기 버튼 (진행 중인 미션에만 표시) -->
  <div v-if="!isPastMission() && !checkMissionCompleted() && !mission.missionUser.abandonStatus" class="mission__abandon-section">
          <button @click="showAbandonConfirm()" class="mission__abandon-button">
            미션 포기하기
          </button>
        </div>
      </template>
    </div>
  </div>
</template>

<script lang='ts'>
import { Vue, Component } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import {
  getMissionDetail,
  joinMission,
  getMissionRecordList,
  abandonMission,
} from '@/api/mission';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';

@Component({
  components: {
    VueSlickCarousel,
  },
  filters: {
    parseDate(dateString: string) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`;
    },
  },
})
export default class extends Vue {
  async mounted() {
		await this.getMissionDetail();
    await this.getMissionRecordList('my');
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
		missionUid: this.$route.params.missionUid,
    myFlag: true,
	}

	private mission: any = {};

  private recordList: any = [];

  private async getMissionDetail() {
		await getMissionDetail(this.$route.params.missionUid).then((res: any) => {
			this.mission = res.data;
      if (this.mission.missionUser !== null) {
        this.dueOverFlag = new Date() >= new Date(this.mission.dueDate);
      }
		}).catch(() => {
			this.$message.warning('미션을 조회하는데 실패했습니다.');
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

  private async getMissionRecordList(type: string) {
    this.loading = true;
    if (type === 'my') this.listQuery.myFlag = true;
    else this.listQuery.myFlag = false;
		await getMissionRecordList(this.listQuery).then((res: any) => {
			this.recordList = res.data;
		}).catch(() => {
			this.$message.warning('미션 기록을 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

  private async joinMission() {
    this.$confirm('정말 미션에 참여하시겠습니까?').then(() => {
      joinMission({ missionUid: this.$route.params.missionUid }).then(() => {
        this.$message.success('미션에 참여 하셨습니다.');
        this.$router.push({ name: 'Mission', params: { boardUid: this.$route.params.boardUid } });
      }).catch((error: any) => {
        this.$message.error(error.response.data.message || '미션에 참여 하는데 실패했습니다.');
      });
    });
	}

  private RecordEditHandler(uid: string) {
    if (this.listQuery.myFlag) {
      this.$router.push({ name: 'MissionWrite', params: { missionUid: this.$route.params.missionUid, uid } });
    }
  }

  private convertedText(text: string) {
      return text.replace(/\n/g, '</br>');
  }

  private linesWithLiTags(text: string) {
      return text.split('\n');
  }

  // 지난 미션인지 확인하는 메서드 (종료일 + 완료 여부)
  private isPastMission(): boolean {
    if (!this.mission.endDate) return false;

    const today = new Date();
    today.setHours(0, 0, 0, 0); // 시간을 00:00:00으로 설정

    const endDate = new Date(this.mission.endDate);
    endDate.setHours(23, 59, 59, 999); // 종료일 23:59:59로 설정

    const isDatePassed = today > endDate;

    // 미션 완료 여부 확인
    const isMissionCompleted = this.checkMissionCompleted();
    console.log(isDatePassed, isMissionCompleted);

    // 종료일이 지났거나 미션이 완료된 경우 지난 미션으로 처리
    return isDatePassed || isMissionCompleted;
  }

  // 미션 완료 여부를 확인하는 헬퍼 메서드
  private checkMissionCompleted(): boolean {
    if (!this.mission.missionUser) {
      return false;
    }

    const totalDays = this.mission.totalDay || 0;
    const recordCnt = this.mission.missionUser.recordCnt || 0;

    // 필요한 인증 횟수를 모두 완료한 경우
    return recordCnt >= totalDays;
  }

  // 미션 완료 메시지를 반환하는 메서드
  private getMissionCompletionMessage(): string {
    if (!this.mission.missionUser) {
      // 사용자 정보가 없는 경우 종료일 기준으로 판단
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      const endDate = new Date(this.mission.endDate);
      endDate.setHours(23, 59, 59, 999);

      return today > endDate ? '기간이 종료된 미션입니다' : '미션을 완료하셨습니다';
    }

    const totalDays = this.mission.totalDay || 0;
    const recordCnt = this.mission.missionUser.recordCnt || 0;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const endDate = new Date(this.mission.endDate);
    endDate.setHours(23, 59, 59, 999);

    const isDatePassed = today > endDate;

    // 필요한 인증 횟수를 모두 완료한 경우
    if (recordCnt >= totalDays) {
      return '미션을 완료하셨습니다';
    }
    // 기간은 지났지만 인증 횟수가 부족한 경우
    if (isDatePassed) {
      return '실패한 미션입니다';
    }
    // 아직 기간이 남았지만 이미 목표를 달성한 경우 (조기 완료)
    return '미션을 완료하셨습니다';
  }

  // 미션 완료 상태에 따른 스타일을 반환하는 메서드
  private getMissionCompletionStyle(): any {
    if (!this.mission.missionUser) {
      // 사용자 정보가 없는 경우 종료일 기준으로 판단
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      const endDate = new Date(this.mission.endDate);
      endDate.setHours(23, 59, 59, 999);

      if (today > endDate) {
        return { background: '#6c757d', color: 'white', cursor: 'default' }; // 회색 - 기간 종료
      }
      return { background: '#28a745', color: 'white', cursor: 'default' }; // 초록색 - 완료
    }

    const totalDays = this.mission.totalDay || 0;
    const recordCnt = this.mission.missionUser.recordCnt || 0;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const endDate = new Date(this.mission.endDate);
    endDate.setHours(23, 59, 59, 999);

    const isDatePassed = today > endDate;

    // 필요한 인증 횟수를 모두 완료한 경우
    if (recordCnt >= totalDays) {
      return { background: '#28a745', color: 'white', cursor: 'default' }; // 초록색 - 성공
    }
    // 기간은 지났지만 인증 횟수가 부족한 경우
    if (isDatePassed) {
      return { background: '#dc3545', color: 'white', cursor: 'default' }; // 빨간색 - 실패
    }
    // 아직 기간이 남았지만 이미 목표를 달성한 경우 (조기 완료)
    return { background: '#28a745', color: 'white', cursor: 'default' }; // 초록색 - 완료
  }

  // 미션 포기 확인 다이얼로그 표시
  private showAbandonConfirm() {
    this.$confirm(`"${this.mission.title}" 미션을 정말 포기하시겠습니까?<br><br>포기한 미션은 다시 시작할 수 없습니다.`, '미션 포기', {
      confirmButtonText: '포기하기',
      cancelButtonText: '취소',
      type: 'warning',
      dangerouslyUseHTMLString: true,
    }).then(() => {
      this.abandonMissionConfirm();
    }).catch(() => {
      // 취소 시 아무것도 하지 않음
    });
  }

  // 미션 포기 실행
  private async abandonMissionConfirm() {
    try {
      await abandonMission(this.$route.params.missionUid as string);
      this.$message.success(`"${this.mission.title}" 미션을 포기했습니다.`);
      // 미션 목록 페이지로 이동
      this.$router.push({ name: 'Mission' });
    } catch (error) {
      this.$message.error('미션 포기 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  }

  // 날짜 포맷팅 메서드
  private formatDate(dateString: string) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`;
  }
}
</script>

<style scoped>
/* 빈 상태 메시지 스타일 */
.mission__detail-picture__empty {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 200px;
  padding: 40px 20px;
  text-align: center;
}

.empty-message {
  width: 100%;
  text-align: center;
  color: #999;
}

.empty-text {
  font-size: 16px;
  font-weight: 500;
  color: #666;
  margin-bottom: 8px;
}

.empty-subtext {
  font-size: 14px;
  color: #999;
  line-height: 1.5;
}

.mission__detail-button-wr {
  margin-bottom: 100px;
}

/* 완료된 미션 버튼 스타일 */
.mission__detail-button--completed {
  border: none !important;
  text-decoration: none !important;
  pointer-events: none;
  opacity: 0.9;
  cursor: default !important;
}

.mission__detail-button--completed:hover {
  opacity: 0.9;
  transform: none;
}

/* 미션 포기 버튼 스타일 */
.mission__abandon-section {
  padding: 20px 0;
  text-align: center;
}

.mission__abandon-button {
  display: block;
  font-size: 20px;
  color: #FFF;
  padding: 15px;
  margin: 0 20px 0px;
  background: #ff6b6b;
  border-radius: 30px;
  border: none;
  width: calc(100% - 40px);
  font-family: 'SEBANG_Gothic_Regular';
  font-weight: 500;
  transition: background 0.3s, box-shadow 0.3s;
}

.mission__abandon-button:hover {
  background: #e03131;
  color: #fff;
  box-shadow: 0 4px 16px rgba(255, 107, 107, 0.25);
}

/* 모바일 최적화 */
@media (max-width: 768px) {
  .mission__detail-picture__empty {
    width: 100%;
    min-height: 150px;
    padding: 30px 15px;
  }
  .empty-text {
    font-size: 15px;
  }

  .empty-subtext {
    font-size: 13px;
  }
}
</style>
