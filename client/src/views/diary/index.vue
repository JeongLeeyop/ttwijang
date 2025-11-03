<template>
  <div class="diary2-2-wrap">
    <div class="diary-top">
      <p class="ti">
        <img v-if="type !== 'body'" img src="~@/assets/images/left-arrow.png" class="left-arrow" alt="왼쪽" @click="handleAddDay(-1)">
          {{ diaryDate | parseDate('YYYY년 MM월 DD일') }}
        <img v-if="type !== 'body'" src="~@/assets/images/right-arrow.png" class="right-arrow" alt="오른쪽" @click="handleAddDay(1)">
      </p>

      <div class="diary__nav__box">
        <a class="diary__nav" :class="{'color2': type === 'diary'}" @click="handleChangeType('diary')">다이어리</a>
        <a class="diary__nav" :class="{'color2': type === 'nutrition'}" @click="handleChangeType('nutrition')">영양상태</a>
        <a class="diary__nav" :class="{'color2': type === 'body'}" @click="handleChangeType('body')">체성분 기록</a>
      </div>

    </div>
    <Diary v-if="type === 'diary'" :diaryDate="diaryDate" />
    <BodyComposition v-if="type === 'body'" :diaryDate="diaryDate" />
    <NutritionStatus v-if="type === 'nutrition'" :diaryDate="diaryDate" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import moment from 'moment';
import Diary from './diary.vue';
import BodyComposition from './bodyComposition.vue';
import NutritionStatus from './nutritionStatus.vue';

@Component({
  components: {
    Diary,
    BodyComposition,
    NutritionStatus,
  },
})

export default class extends Vue {
  mounted() {
    if (this.$route.query.diaryDate) {
      this.diaryDate = this.$route.query.diaryDate;
    } else {
      this.$router.replace({ ...this.$router, query: { ...this.$route.query, diaryDate: this.diaryDate } });
    }
    if (this.$route.query.tab) this.type = this.$route.query.tab;
  }

  private diaryDate: any = moment().format('YYYY-MM-DD');

  private type: any = 'diary';

  private handleAddDay(days: number) {
    this.diaryDate = moment(this.diaryDate, 'YYYY-MM-DD').add(days, 'days').format('YYYY-MM-DD');
    this.$router.replace({ ...this.$router, query: { diaryDate: this.diaryDate } });
  }

  private handleChangeType(value: string) {
    this.type = value;
    this.$router.replace({ ...this.$router, query: { ...this.$route.query, tab: this.type } });
    if (value === 'body') {
      this.diaryDate = moment().format('YYYY-MM-DD');
    }
  }
}
</script>
