<template>
  <div class="home-header">
    <button @click="handleClickBefore()" class="home-header__arrow"><img src="~@/assets/images/arrow-ico.png"
        alt="뒤로가기"></button>
    {{ $route.meta.title || 'ttwijang' }}
    <router-link :to="{ name: 'Mypage' }" class="home-header__user" :class="{'alert': newAlarmCount >= 1}"><img src="~@/assets/images/user.png"
        alt="마이페이지"></router-link>
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

  private showPopover = false;

  private alarmCount = 0;

  private newAlarmCount = 0;

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

  private handleClickBefore() {
    const arr = ['Point', 'Coupon', 'UserInfo', 'Delivery', 'MyReview', 'Term'];
    if (this.$route.name === 'Mypage') {
      this.$router.push({ name: 'Home' });
    } else if (arr.includes(this.$route.name ? this.$route.name : '')) {
      this.$router.push({ name: 'Mypage' });
    } else {
      this.$router.go(-1);
    }
  }
}
</script>
