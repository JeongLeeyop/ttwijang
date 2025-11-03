<template>
  <div v-loading="loading">
    <div class="home3-2__top">
      <button v-if="weekflag > 1" class="left-arrow" @click="handleLastWeek" />
      <div class="home3-2__top__a">
        <!-- <span class="tih">{{ weekNum }}주차</span><br> -->
        <span class="tih">{{ weekflag }}주차</span><br>
        <span class="tiha">{{startDate}} ~ {{endDate}}</span>
      </div>
      <button class="right-arrow" @click="handleNextWeek" />
    </div>
    <div class="home3-2__mid">
      <p class="ti">
        <img src="~@/assets/images/exclamation.png" class="exclamation" alt="">
        <!-- 원하시는 제품의 수량을 선택해주세요. (구매 가능 수량 {{ this.selectedShop.stock }}개 )  -->
        <!-- {{ this.selectedShop.maxHoldCnt }} -->
        <!-- {{ this.orderCountForm.week[weekNum].day[dayNum].orderCount }} -->
        원하시는 제품의 수량을 선택해주세요. (구매 가능 수량 {{ this.selectedShop.maxHoldCnt - this.orderCountForm.week[weekNum].day[dayNum].orderCount - this.curChooseCnt }}개 )
      </p>
    </div>
    <div class="home3-2__bot pd-bot">
      <ul class="weeks">
        <li class="weeks__item" :class="[{ 'active' : dayNum === 1 }, { 'disabled' : selectedShop.holidays.mon }]" @click="handleDay(1)">
          <a>월<p v-if="orderForm.week[weekflag] && orderForm.week[weekflag].day[1]" class="cnt">{{orderForm.week[weekflag].day[1].count}}</p></a>
        </li>
        <li class="weeks__item" :class="[{ 'active' : dayNum === 2 }, { 'disabled' : selectedShop.holidays.tue }]" @click="handleDay(2)">
          <a>화<p v-if="orderForm.week[weekflag] && orderForm.week[weekflag].day[2]" class="cnt">{{orderForm.week[weekflag].day[2].count}}</p></a>
        </li>
        <li class="weeks__item" :class="[{ 'active' : dayNum === 3 }, { 'disabled' : selectedShop.holidays.wed }]" @click="handleDay(3)">
          <a>수<p v-if="orderForm.week[weekflag] && orderForm.week[weekflag].day[3]" class="cnt">{{orderForm.week[weekflag].day[3].count}}</p></a>
        </li>
        <li class="weeks__item" :class="[{ 'active' : dayNum === 4 }, { 'disabled' : selectedShop.holidays.thu }]" @click="handleDay(4)">
          <a>목<p v-if="orderForm.week[weekflag] && orderForm.week[weekflag].day[4]" class="cnt">{{orderForm.week[weekflag].day[4].count}}</p></a>
        </li>
        <li class="weeks__item" :class="[{ 'active' : dayNum === 5 }, { 'disabled' : selectedShop.holidays.fri }]" @click="handleDay(5)">
          <a>금<p v-if="orderForm.week[weekflag] && orderForm.week[weekflag].day[5]" class="cnt">{{orderForm.week[weekflag].day[5].count}}</p></a>
        </li>
      </ul>
      <ul>
        <template v-for="(item, index) in products">
          <li :key="index" class="home3-2__list" @click="handleChoice(item)">
            <div class="home3-2__box">
              <p class="ti color2">{{item.name}}</p>
              <div class="material imgg">
                <p class="tti">
                  {{item.description}}
                </p>
                <div class="price">{{item.price}}원</div>
              </div>
            </div>
            <img :src="`/api/attached-file/${item.thumbUid}`" alt="">
          </li>
        </template>
      </ul>
    </div>
    <div class="home3-2__fixed" @click="handleBasket">
      <span class="cart_icon">
        <p v-if="orderInfo.count > 0" class="cnt">{{orderInfo.count}}</p>
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getProductRotationList } from '@/api/productLocation';
import { OrderModule } from '@/store/modules/order';
import moment from 'moment';
import { getShopDetail } from '@/api/shop';
import { getOrderCount } from '@/api/order';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';

@Component({
  name: 'ProductRotation',
})

export default class extends Vue {
  private weekNum = 1;

  private dayNum = OrderModule.dayNum;

  private curChooseCnt = 0;

  private maxWeekNum = 4;

  private weekflag = OrderModule.weekNum;

  private startDate: any = '';

  private endDate: any = '';

  private loading = true;

  private products: any = [];

  private apiUrl = process.env.VUE_APP_BASE_API;

  private orderCountForm: any = '';

  private orderForm: any = OrderModule.orderForm;

  private orderInfo: any = OrderModule.orderInfo;

  private selectedShop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));

  mounted() {
    if (!UserModule.isLogin) {
      this.$message.info('로그인이 필요한 서비스 입니다.');
      this.$router.push({ name: 'Login' });
    }
    // OrderModule.setDayNum(1);
    this.computeStartEndDateAndWeekNum(moment(), this.weekflag);
    this.handleProductRotationList();
    this.checkHoliday();
    this.getOrderCount();
    if (this.$route.query.errorMessage) {
      this.$message.info(`${this.$route.query.errorMessage}`);
    }
    this.getCurChooseCnt();
  }

  private async getOrderCount() {
    this.loading = true;
    this.orderCountForm = {
      week: {
        [this.weekNum]: {
          day: {
            [this.dayNum]: { orderCount: 0 },
          },
          startDate: this.startDate,
          endDate: this.endDate,
        },
      },
    };
    await getOrderCount(this.selectedShop.idx, this.orderCountForm).then((res) => {
      this.orderCountForm = res.data;
    });
    this.loading = false;
  }

  private async checkHoliday() {
    await getShopDetail(this.selectedShop.idx).then((res) => {
      this.selectedShop.holidays = res?.data?.holidays;
    });
    const holidays = this.selectedShop.holidays;
    if (holidays != null && holidays.mon && holidays.tue && holidays.wed && holidays.thu && holidays.fri) {
      this.$message.warning('선택가능한 요일이 없습니다. 다른 매장을 선택해주세요.');
      this.$router.push({ name: 'Home' });
      return;
    }

    // if (this.dayNum === 1 && holidays?.mon) {
    //   if (!holidays.tue) this.dayNum = 2;
    //   else if (!holidays.wed) this.dayNum = 3;
    //   else if (!holidays.thu) this.dayNum = 4;
    //   else if (!holidays.fri) this.dayNum = 5;
    // } else if (this.dayNum === 2 && holidays?.tue) {
    //   if (!holidays.wed) this.dayNum = 3;
    //   else if (!holidays.thu) this.dayNum = 4;
    //   else if (!holidays.fri) this.dayNum = 5;
    //   else if (!holidays.mon) this.dayNum = 1;
    // } else if (this.dayNum === 3 && holidays?.wed) {
    //   if (!holidays.thu) this.dayNum = 4;
    //   else if (!holidays.fri) this.dayNum = 5;
    //   else if (!holidays.mon) this.dayNum = 1;
    //   else if (!holidays.tue) this.dayNum = 2;
    // } else if (this.dayNum === 4 && holidays?.thu) {
    //   if (!holidays.fri) this.dayNum = 5;
    //   else if (!holidays.mon) this.dayNum = 1;
    //   else if (!holidays.tue) this.dayNum = 2;
    //   else if (!holidays.wed) this.dayNum = 3;
    // } else if (this.dayNum === 5 && holidays?.fri) {
    //   if (!holidays.mon) this.dayNum = 1;
    //   else if (!holidays.tue) this.dayNum = 2;
    //   else if (!holidays.wed) this.dayNum = 3;
    //   else if (!holidays.thu) this.dayNum = 4;
    // }

  const days = ['mon', 'tue', 'wed', 'thu', 'fri'];
  const dayIndex = this.dayNum - 1; // this.dayNum이 1부터 시작하므로 0부터 시작하는 인덱스로 변환

  if (holidays[days[dayIndex]]) {
    for (let i = 1; i < days.length; i += 1) {
      const nextDayIndex = (dayIndex + i) % days.length;
      if (!holidays[days[nextDayIndex]]) {
        this.dayNum = nextDayIndex + 1; // 인덱스를 다시 1부터 시작하는 값으로 변환
        break;
      }
    }
  }

    // if (!holidays?.mon) this.dayNum = 1;
    // else if (!holidays?.tue) this.dayNum = 2;
    // else if (!holidays?.wed) this.dayNum = 3;
    // else if (!holidays?.thu) this.dayNum = 4;
    // else if (!holidays?.fri) this.dayNum = 5;
    OrderModule.setDayNum(this.dayNum);
    this.getOrderCount();
    this.getCurChooseCnt();
  }

  computeStartEndDateAndWeekNum(date: any, num: number) {
    const lastMonday = moment(date).add(num, 'weeks').startOf('isoWeek');
    const friday = moment(lastMonday).add(4, 'days');
    this.startDate = lastMonday.format('YYYY-MM-DD');
    this.endDate = friday.format('YYYY-MM-DD');
    OrderModule.setStartDate(this.startDate);
    OrderModule.setEndDate(this.endDate);
    this.weekNum = lastMonday.diff(moment(lastMonday).startOf('month').startOf('isoWeek'), 'weeks');
    if (moment(lastMonday).startOf('month').startOf('isoWeek').date() < 4) {
      this.weekNum += 1;
    }
    OrderModule.setWeekNum(Number(this.weekflag));
  }

  handleProductRotationList() {
    this.loading = true;
    getProductRotationList(this.weekNum).then((res) => {
      this.loading = false;
      this.products = res.data.products;
    });
  }

  getCurChooseCnt() {
    if (this.orderForm.week[this.weekflag] && this.orderForm.week[this.weekflag].day[this.dayNum]) {
      this.curChooseCnt = this.orderForm.week[this.weekflag].day[this.dayNum].count;
    } else {
      this.curChooseCnt = 0;
    }
  }

  handleDay(num: number) {
    this.dayNum = num;
    let flag = false;
    if (num === 1 && this.selectedShop.holidays.mon) flag = true;
    if (num === 2 && this.selectedShop.holidays.tue) flag = true;
    if (num === 3 && this.selectedShop.holidays.wed) flag = true;
    if (num === 4 && this.selectedShop.holidays.thu) flag = true;
    if (num === 5 && this.selectedShop.holidays.fri) flag = true;
    if (num === 6 && this.selectedShop.holidays.sat) flag = true;
    if (num === 0 && this.selectedShop.holidays.sun) flag = true;

    if (flag) {
      this.$message.warning('해당 요일은 선택하실 수 없습니다.');
      return;
    }

    OrderModule.setDayNum(num);
    this.getOrderCount();
    this.getCurChooseCnt();
  }

  handleLastWeek() {
    if (this.weekflag === 1) {
      return;
    }
    this.weekflag -= 1;

    OrderModule.setDayNum(this.dayNum);
    this.computeStartEndDateAndWeekNum(this.startDate, -1);
    this.handleProductRotationList();
    this.getOrderCount();
    this.getCurChooseCnt();
  }

  handleNextWeek() {
    if (this.maxWeekNum <= this.weekflag) {
      this.$message('최대 4주차까지 구매 가능합니다.');
      return;
    }
    this.weekflag += 1;

    OrderModule.setDayNum(this.dayNum);
    this.computeStartEndDateAndWeekNum(this.startDate, 1);
    this.handleProductRotationList();
    this.getOrderCount();
    this.getCurChooseCnt();
  }

  handleChoice(product: any) {
    // console.log(this.orderForm.week[this.weekNum].day[this.dayNum].count);
    // console.log(this.selectedShop.maxHoldCnt);
    // console.log(this.orderCountForm.week[this.weekNum].day[this.dayNum].orderCount);
    // if (this.orderCountForm === '') {
      // alert(1);
    // }

    // 해당 일 상점 재고 구하기
    let shopStock = 0;
    shopStock = this.selectedShop.maxHoldCnt - this.orderCountForm.week[this.weekNum].day[this.dayNum].orderCount;

    // 해당 일 현재 장바구니 수량 구하기
    let curDayCount = 0;
    let cnt = 0;
    if (this.orderForm.week[this.weekflag] && this.orderForm.week[this.weekflag].day[this.dayNum] && this.orderForm.week[this.weekflag].day[this.dayNum].count) {
      curDayCount = this.orderForm.week[this.weekflag].day[this.dayNum].count;
      if (this.orderForm.week[this.weekflag].day[this.dayNum].products[product.idx]) {
        cnt = this.orderForm.week[this.weekflag].day[this.dayNum].products[product.idx].count;
      }
    }
    this.$router.push({
        name: 'OrderDetail',
        params: {
          productId: product.idx,
          shopStock: `${shopStock}`,
          stock: `${shopStock - curDayCount}`,
          cnt: `${cnt}`,
        },
      });
      // stock: `${this.selectedShop.maxHoldCnt - this.orderCountForm.week[this.weekNum].day[this.dayNum].orderCount}`,
  }

  handleBasket() {
    this.$router.push({
      name: 'Cart',
    });
  }
}
</script>
