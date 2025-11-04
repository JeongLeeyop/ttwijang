<template>
  <div class="header">
    <div class="header-left">
        <i class="el-icon-s-fold"></i>
        <el-select :popper-append-to-body="false" v-model="selectedRegion" placeholder="지역 선택" size="small">
          <el-option label="서울" value="seoul"></el-option>
          <el-option label="경기" value="gyeonggi"></el-option>
          <el-option label="인천" value="incheon"></el-option>
        </el-select>
      </div>
      <div class="header-center">
      </div>
      <div class="header-right">
        <i class="el-icon-date" @click="goToCalendar"></i>
        <i class="el-icon-bell"></i>
        <el-popover v-model="showPopover" placement="bottom-end" width="450" trigger="click" popper-class="alarm" place-
          :popper-append-to-body="false" :title="alarmList.length > 0 ? '띵동! 알림이 도착했어요 🎶' : ''">
          <div @click="showPopover = false" class="alarm-close">
            <i class="el-icon-close"></i>
          </div>
          <div v-if="alarmList2.length > 0">
            <div class="alarm-item-day-wr" v-for="(item, index) in alarmList2" :key="index">
              <div class="alarm-item-day">{{ item[0].createDate | parseDate('YYYY-MM-DD') }}</div>
              <div v-if="item.length > 0">
                <a :href="item2.link" class="alarm-item-wr" v-for="item2 in item" :key="item2.id">
                  <div class="alarm-info">
                    <div class="title">{{ item2.title }}</div>
                    <div class="date">{{ item2.createDate | parseDate('YYYY-MM-DD HH:mm') }}</div>
                  </div>
                  <div class="alarm-content">{{ item2.content }}</div>
                </a>
              </div>
            </div>
          </div>
          <div class="empty" v-else>
            <div class="logo"><img src="@/assets/images/logo2.png" /></div>
            <div class="txt">도착한 알림이 없어요</div>
          </div>
          <el-button type="text" slot="reference" class="header__bell alarm" :class="{'alert': newAlarmCount >= 1}"></el-button>
        </el-popover>
      </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component({
  name: 'MainHeader',
})
export default class extends Vue {
  @Prop({ default: '' }) private title!: string;

  @Prop({ default: false }) private showNotification!: boolean;

  @Prop({ default: true }) private showBack!: boolean;

  private selectedRegion = 'seoul'

  private showPopover = false;

  private alarmCount = 0;

  private newAlarmCount = 0;

  private alarmList: any = [];

  private alarmList2: any[][] = [];

  private totalElements = 0;

  private totalPages = 0;

  private listQuery = {
    page: 0,
    size: 5,
  };

  mounted() {
    this.initializeSampleAlarms();
  }

  private goBack() {
    this.$router.go(-1);
  }

  private handleNotification() {
    this.$emit('notification-click');
  }

  private goToCalendar(): void {
    this.$router.push('/calendar');
  }

  private initializeSampleAlarms() {
    // 샘플 알람 데이터 생성
    const sampleAlarms = [
      {
        id: 1,
        title: '리그 경기 예정',
        content: '내일 오후 7시에 경기가 있습니다.',
        createDate: '2025-11-04 14:30:00',
        link: '#',
      },
      {
        id: 2,
        title: '팀 가입 신청 승인',
        content: '팀 "강남FC"의 가입 신청이 승인되었습니다.',
        createDate: '2025-11-04 10:15:00',
        link: '#',
      },
      {
        id: 3,
        title: '경기 결과 등록',
        content: '지난 경기의 결과가 등록되었습니다. 확인해주세요.',
        createDate: '2025-11-03 18:45:00',
        link: '#',
      },
      {
        id: 4,
        title: '매너점수 변경',
        content: '경기에서의 매너점수가 변경되었습니다. (4.8점)',
        createDate: '2025-11-03 17:20:00',
        link: '#',
      },
      {
        id: 5,
        title: '공지사항',
        content: '플랫폼 점검이 11월 10일 예정되어 있습니다.',
        createDate: '2025-11-02 09:00:00',
        link: '#',
      },
    ];

    this.alarmList = sampleAlarms;
    this.newAlarmCount = 3;

    // 날짜별로 그룹화
    const itemMap: Map<string, any[]> = new Map();
    this.alarmList.forEach((item: any) => {
      const date: string = item.createDate.substring(0, 10); // 날짜 부분만 추출
      if (!itemMap.has(date)) {
        itemMap.set(date, []);
      }
      const itemList: any[] | undefined = itemMap.get(date);
      if (itemList) {
        itemList.push(item);
      }
    });
    this.alarmList2 = Array.from(itemMap.values());
  }

  private getNewAlarmCount() {
    // API 제거 - 샘플 데이터 사용
  }

  private getAlarmCount() {
    // API 제거 - 샘플 데이터 사용
  }

  private async getAlarmList() {
    // API 제거 - 샘플 데이터 사용
  }

  private handleChangePage(page: number) {
    // 페이지 변경 시 알람 리스트 새로고침 (현재는 샘플 데이터 사용)
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
