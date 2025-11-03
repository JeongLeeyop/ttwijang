<template>
  <div class="home-header">
    <button @click="handleClickBefore()" class="home-header__arrow"><img src="~@/assets/images/arrow-ico.png" alt="뒤로가기"></button>
    <button @click="handleClickHome()" class="home-header__arrow2"><img src="~@/assets/images/home-ico.png" alt="뒤로가기"></button>
    {{ $route.meta.title || 'ttwijang' }}
    <el-popover
          v-model="showPopover"
          placement="bottom"
          width="450"
          trigger="click"
          popper-class = "alarm"
          place-
          :popper-append-to-body="false"
          :title="alarmList.length > 0 ? '띵동! 알림이 도착했어요 🎶' : ''"
          >
          <div @click="showPopover = false" class="alarm-close">
            <i class="el-icon-close"></i>
          </div>
          <div v-if="alarmList2.length > 0">
          <div class="alarm-item-day-wr" v-for="(item, index) in alarmList2" :key="index">
            <div class="alarm-item-day">{{ item[0].createDate | parseDate('YYYY-MM-DD')}}</div>
            <div v-if="item.length > 0">
              <a :href="item2.link" class="alarm-item-wr" v-for="item2 in item" :key="item2.id">
                <div class="alarm-info">
                  <div class="title">{{ item2.title }}</div>
                  <div class="date">{{ item2.createDate | parseDate('YYYY-MM-DD HH:mm')}}</div>
                </div>
                <div class="alarm-content">{{ item2.content }}</div>
              </a>
            </div>
          </div>
          </div>
          <div class="empty" v-else>
              <div class="logo"><img src="@/assets/images/logo2.png"/></div>
              <div class="txt">도착한 알림이 없어요</div>
            </div>
          <el-button type="text" slot="reference" class="diary-header__user alarm"></el-button>
        </el-popover>
    <router-link :to="{ name: 'Mypage' }" class="home-header__user" :class="{'alert': newAlarmCount >= 1}"><img src="~@/assets/images/user.png" alt="마이페이지"></router-link>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { getAlarmCount, getAlarmList } from '@/api/pushAlarm';
import { getNewAlarmCount } from '@/api/newAlarm';
import { storageKey } from '@/enums/localStorage';
import { getOrderCount } from '@/api/order';
import { getShopDetail } from '@/api/shop';
import { OrderModule } from '@/store/modules/order';
import moment from 'moment';

@Component({
  name: 'SubLayoutHeader',
})
export default class extends Vue {
  mounted() {
    this.getAlarmList();
    this.getNewAlarmCount();
    // this.maxHoldCntCheck();
    this.shopHolidayCheck();
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

  private selectedShop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));

  private maxHoldCntCheck() {
    if (this.selectedShop.maxHoldCnt === 0) {
      this.$message.info('주문 가능 수량이 모두 소진되었습니다. 다른 매장을 선택해 주세요.');
      this.$router.push({ name: 'Map' });
    }
  }

  private async shopHolidayCheck() {
      await getShopDetail(this.selectedShop.idx).then((res) => {
        this.selectedShop = res.data;
        window.localStorage.setItem(storageKey.pickUpPlace, JSON.stringify(this.selectedShop));
      });
    const currentDate = moment().format('YYYY-MM-DD');
    if (this.selectedShop.holidayStartDate <= currentDate && this.selectedShop.holidayEndDate >= currentDate) {
      this.$message.info('해당 상점은 오늘 휴무일입니다. 다른 매장을 선택해 주세요.');
      this.$router.push({ name: 'Map' });
    }
  }

  private getOrderCount() {
    getOrderCount(this.selectedShop.idx, OrderModule.orderForm.week).then((res) => {
      console.log(res.data);
    });
  }

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
    if (this.$route.name === 'OrderDetail' || this.$route.name === 'Cart') {
      this.$router.push({ name: 'Order' });
    } else if (this.$route.name === 'Order') {
      this.$router.push({ name: 'Home' });
    } else if (this.$route.name === 'Payment') {
      this.$router.push({ name: 'Cart' });
    } else {
      this.$router.go(-1);
    }
  }

  private handleClickHome() {
      this.$router.push({ name: 'Home' });
  }
}
</script>
