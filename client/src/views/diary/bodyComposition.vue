<template>
  <div>
    <div v-loading="loading" class="diary2-4__mid">
      <div class="diary2-4__mid__a">
        <p class="ti">체성분 상태</p>
        <router-link :to="{ name: 'BodyRecord', query: currentQuery }"><i class="el-icon-edit"></i> 체성분 상태기록</router-link>
      </div>
      <div class="diary2-4__mid__b">
        <div class="diary2-4__mid__circle">
          <p class="ti">체중 kg</p>
          <p class="tti">{{ userInfo.weight }}</p>
        </div>
        <div class="diary2-4__mid__circle color2">
          <p class="ti">골격근량 kg</p>
          <p class="tti">{{ physicalCondition.muscleWeight }}</p>
        </div>
        <div class="diary2-4__mid__circle color3">
          <p class="ti">체지방율 %</p>
          <p class="tti">{{ physicalCondition.fatRate }}</p>
        </div>
      </div>

      <div class="diary2-4__mid__a">
        <p class="ti">체성분 목표</p>
        <router-link :to="{ name: 'BodyPurpose', query: currentQuery }"><i class="el-icon-edit"></i> 체성분 목표설정</router-link>
      </div>
      <div class="diary2-4__bot">
        <p v-if="userInfo.goalDate !== ''" class="ti" style="color:#000;font-weight: 700; font-size: 22px;padding:5px">{{ userInfo.goalDate | getDayOfWeek }}까지</p>
        <div class="diary2-4__bot__a">
          <p><span class="ti">체중 목표</span>
            <span v-if="userInfo.goalWeight !== null && userInfo.goalWeight !== 0" class="tti">{{ (userInfo.weight / userInfo.goalWeight * 100).toFixed(1) }}%</span>
            <span v-else class="tti">0%</span>
          </p>
          <el-progress :text-inside="true" :stroke-width="7" :percentage="(userInfo.weight / userInfo.goalWeight * 100)"></el-progress>
          <p>
            <span class="ttti">{{ userInfo.weight }}kg</span>
            <span class="ttti">{{ userInfo.goalWeight }}kg</span>
          </p>
        </div>

        <div class="diary2-4__bot__a color2">
          <p>
            <span class="ti color2">골격근량 목표</span>
            <span v-if="physicalCondition.purposeMuscleWeight !== null && physicalCondition.purposeMuscleWeight !== 0" class="tti">{{ (physicalCondition.muscleWeight / physicalCondition.purposeMuscleWeight * 100).toFixed(1) }}%</span>
            <span v-else class="tti">0%</span>
          </p>
          <el-progress :text-inside="true" :stroke-width="7" :percentage="(physicalCondition.muscleWeight / physicalCondition.purposeMuscleWeight * 100)"></el-progress>
          <p>
            <span class="ttti">{{ physicalCondition.muscleWeight }}kg</span>
            <span class="ttti">{{ physicalCondition.purposeMuscleWeight }}kg</span>
          </p>
        </div>

        <div class="diary2-4__bot__a color3">
          <p>
            <span class="ti color3">체지방율 목표</span>
            <span v-if="physicalCondition.fatRate !== null && physicalCondition.fatRate !== 0" class="tti">{{ (physicalCondition.purposeFatRate / physicalCondition.fatRate * 100).toFixed(1) }}%</span>
            <span v-else class="tti">0%</span>
          </p>
          <el-progress :text-inside="true" :stroke-width="7" :percentage="(physicalCondition.purposeFatRate / physicalCondition.fatRate * 100)"></el-progress>
          <p>
            <span class="ttti">{{ physicalCondition.fatRate }}%</span>
            <span class="ttti">{{ physicalCondition.purposeFatRate }}%</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getPhysicalCondition } from '@/api/diary';
import {
  Vue,
  Component,
  Watch,
  Prop,
} from 'vue-property-decorator';
import { Message } from 'element-ui';
import { getUserInfo } from '@/api/user';

@Component({
})

export default class extends Vue {
  @Prop({ required: true }) private diaryDate!: any;

  @Watch('diaryDate')
  private handleChangeDiaryDate() {
    this.getPhysicalCondition();
  }

  mounted() {
    this.getPhysicalCondition();
    this.getUserInfo();
  }

  private currentQuery = { ...this.$route.query };

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
      this.currentQuery.goalDate = this.userInfo.goalDate;
      this.currentQuery.goalWeight = this.userInfo.goalWeight;
      this.currentQuery.weight = this.userInfo.weight;
    });
  }

  private userInfo = {
    goalDate: '',
    goalWeight: '',
    weight: '',
  };

  private physicalCondition = {
    weight: 0,
    muscleWeight: 0,
    fatRate: 0,
    purposeWeight: 0,
    purposeMuscleWeight: 0,
    purposeFatRate: 0,
  }

  private loading = true;

  private getPhysicalCondition() {
    this.loading = true;
    getPhysicalCondition({ searchDate: this.diaryDate }).then((res) => {
      this.physicalCondition = res.data;
      this.loading = false;
    }).then(() => {
      if (this.physicalCondition.weight === 0 || this.physicalCondition.muscleWeight === 0 || this.physicalCondition.fatRate === 0) {
        Message('체성분 상태를 기록해주세요.');
      } else if (this.physicalCondition.purposeWeight === null || this.physicalCondition.purposeMuscleWeight === null || this.physicalCondition.purposeFatRate === null) {
        Message('체성분 목표를 설정해주세요.');
      }
    });
  }
}
</script>
