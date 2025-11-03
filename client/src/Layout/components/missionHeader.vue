<template>
  <div class="mission-header" :class="linkCheck()">
    <button @click="handleClickBefore()" class="home-header__arrow"><i class="el-icon-arrow-left"></i></button>
    {{ $route.meta.title || 'WEILYEAT' }}
    <slot v-if="$route.name === 'MyMission' || $route.name === 'MissionHistory'">
      <button @click="handleClickClose()" class="home-header__close"><i class="el-icon-close"></i></button>
    </slot>
  </div>
</template>

<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';
import { getAlarmCount, getAlarmList } from '@/api/pushAlarm';
import { getNewAlarmCount } from '@/api/newAlarm';

@Component({
  name: 'AppHeader',
})

export default class extends Vue {
  mounted() {
    this.getAlarmList();
    this.getNewAlarmCount();
  }

  private newAlarmCount = 0;

  private showPopover = false;

  private alarmCount = 0;

  private alarmList = [];

  private alarmList2: any[][] = [];

  private totalElements = 0;

  private totalPages = 0;

  private listQuery = {
    page: 0,
    size: 5,
  };

  private getNewAlarmCount() {
    getNewAlarmCount().then((res) => {
      this.newAlarmCount = res.data;
    });
  }

  private getAlarmCount() {
    getAlarmCount().then((res) => {
      this.alarmCount = res.data;
    });
  }

  private linkCheck() {
    let str = '';
    if (this.$route.name === 'MissionHistory' || this.$route.name === 'MyMission') {
      str = 'grey';
    } else if (this.$route.name === 'MissionDetail' || this.$route.name === 'MissionWrite') {
      str = 'white';
    }
    return str;
  }

  private async getAlarmList() {
    await getAlarmList(this.listQuery).then((res) => {
      this.alarmList = res.data.content;
      this.totalElements = res.data.totalElements;
      this.totalPages = res.data.totalPages;
    });
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

  private handleChangePage(page: number) {
    this.listQuery.page = page;
    this.getAlarmList();
  }

  private handleClickClose() {
   this.$router.go(-1);
  }

  private handleClickBefore() {
    if (this.$route.name === 'MissionInquiry') {
      this.$router.push({ name: 'Home' });
    } else if (this.$route.name === 'MissionInquiry2') {
      this.$router.push({ name: 'MissionInquiry' });
    } else if (this.$route.name === 'MissionInquiry3') {
      this.$router.push({ name: 'MissionInquiry2' });
    } else if (this.$route.name === 'MissionWrite') {
      this.$router.push({ name: 'MissionDetail' });
    } else if (this.$route.name === 'MissionDetail') {
      this.$router.push({ name: 'Mission' });
    } else if (this.$route.name === 'MyMission') {
      this.$router.push({ name: 'Mission' });
    } else if (this.$route.name === 'MissionHistory') {
      this.$router.push({ name: 'Mission' });
    } else if (this.$route.name === 'Mission') {
      this.$router.push({ name: 'Home' });
    } else {
      this.$router.go(-1);
    }
  }
}
</script>
