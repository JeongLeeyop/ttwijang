<template>
  <div v-loading="loading">
    <div class="home3-3__wr">
      <!-- <p class="ti">주문 정보</p>
      <div class="home3-3__top__a">
        <div class="home3-3__top__a-1">
          <p class="tti">식단형태</p>
          <p class="ttti">: {{ customDiet.dietType }}</p>
          <p class="tti">식단목적</p>
          <p class="ttti">: {{ customDiet.dietPurpose }}</p>
          <p class="tti">식단 유의사항</p>
          <p class="ttti">: {{ customDiet.dietPrecaution ? customDiet.dietPrecaution : '없음' }}</p>
        </div>
        <div class="home3-3__top__a-2">
          <p class="tti">주당 식사 수</p>
          <p class="ttti">: {{ customDiet.dietQuantity }}</p>
          <p class="tti">픽업요일</p>
          <p class="ttti">: {{ getDayOfWeek(customDiet.dietWeek) }}</p>
          <p class="tti">식단관리 기간</p>
          <p class="ttti">: {{ customDiet.dietPeriod }}주</p>
        </div>
      </div>

      <p class="ti">식단 리스트</p>
      <div class="home3-3__top__b">
        <slot v-for="(order, index) in customDiet.orderList">
          <p class="tti" :key="index">[ {{ order.round }}주차 ]<span class="tti color2"> {{ order.startDate | parseDate('YYYY년 MM월 DD일') }} ~ {{ order.endDate | parseDate('MM월 DD일') }}</span></p>
          <div class="home3-3__box" :key="index">
            <slot v-for="(orderItem, index2) in order.orderItemList">
              <p class="ttti" :key="index2">{{ orderItem.itemName }} X{{ orderItem.qty }}개</p>
              <p class="ttti a" :key="index2">{{ orderItem.qty * orderItem.price | parseKrw }}원</p>
            </slot>
          </div>
        </slot>
        <div class="home3-3__top__b-1">
          <p class="tii">총결제 금액</p>
          <p class="tiii">{{ 10000 }}원</p>
        </div>
      </div> -->

      <div class="home3-3__top__c">
        <table>
          <caption>선택된 픽업 장소</caption>
          <tbody>
            <tr>
              <th>장소 이름</th>
              <td>{{ shop.name }}</td>
            </tr>
            <tr>
              <th>장소 주소</th>
              <td>{{ shop.address }} {{ shop.addressDetail }}</td>
            </tr>
            <tr>
              <th>픽업 시간</th>
              <td>
                <el-select placeholder="픽업 시간 선택" :popper-append-to-body="false" v-model="orderForm.pickupTime">
                  <el-option
                    v-for="pickupTime in pickupTimes"
                    :key="pickupTime.time"
                    :label="pickupTime.time"
                    :value="pickupTime.time"
                  />
                </el-select>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="home3-3__top__c">
        <table>
          <caption>쿠폰 / 할인 / 적립금</caption>
          <tbody>
            <tr>
              <th>쿠폰 할인</th>
              <td>
                <el-select v-model="orderForm.useCouponIdx" placeholder="쿠폰을 선택하세요" :popper-append-to-body="false" @change="handleChooseCoupon">
                  <el-option label="선택없음" value="" />
                  <el-option v-for="coupon in couponList" :key="coupon.idx" :label="coupon.name" :value="coupon.idx" />
                </el-select>
              </td>
            </tr>
            <tr>
              <th>포인트 사용</th>
              <td>
                <div class="point-info">
                  <el-input type="number" v-model="orderForm.usePoint" :min="0" :max="userInfo.point - 100" :step="10" @input="handleUsePoint" @change="handleChangePoint"></el-input>
                  <el-button @click="handleUseAllPoint" class="useAll">모두 사용</el-button>
                </div>
                <span>보유 포인트 : {{ userInfo.point | parseKrw }}p</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- <div class="home3-3__top__c">
        <table>
          <caption>결제 수단</caption>
          <tbody>

            <tr>
              <td class="radio">
                <el-radio v-model="payMethod" :label="1"> 간편 결제
                </el-radio>
                <div class="home3-3-1__im">
                  <img src="~@/assets/images/card.png" class="card" alt="결제카드">
                </div>

                <div class="home3-3-1__select">
                  <el-select v-model="payType" placeholder="일시불" :popper-append-to-body="false">
                    <el-option v-for="item in payTypes" :key="item.value" :label="item.label"
                      :value="item.value"></el-option>
                  </el-select>
                </div>
              </td>
            </tr>
            <tr>
              <td class="radio">
                <el-radio v-model="payMethod" :label="2"> 네이버 페이
                </el-radio>
              </td>
            </tr>
            <tr>
              <td class="radio">
                <el-radio v-model="payMethod" :label="3"> 카카오 페이
                </el-radio>
              </td>
            </tr>
            <tr>
              <td class="radio">
                <el-radio v-model="payMethod" :label="4"> 카드 결제
                </el-radio>
              </td>
            </tr>

          </tbody>
        </table>

        <div class="payment-card-add" style="padding: 40px 0 100px 0;">
          <router-link :to="{ name: 'Password' }">결제하기</router-link>
        </div>

      </div> -->

      <div class="home3-3__top">
        <slot v-for="(order, index) in customDiet.orderList">
          <p class="tti" :key="index">[ {{ order.round }}주차 ]<span class="tti color2"> {{ order.startDate | parseDate('YYYY년 MM월 DD일') }} ~ {{ order.endDate | parseDate('MM월 DD일') }}</span></p>
          <div class="home3-3__box" :key="index">
            <slot v-for="(orderItem, index2) in order.orderItemList">
              <p class="ttti" :key="index2">{{ orderItem.itemName }} X{{ orderItem.qty }}개</p>
              <p class="ttti a" :key="index2">{{ orderItem.qty * orderItem.price | parseKrw }}원</p>
            </slot>
          </div>
        </slot>
        <div class="home3-3__top__b-1">
          <p class="tii">총결제 금액</p>
          <p class="tiii">{{  originAmount - orderForm.usePoint - getCouponDiscountPrice() | parseKrw }}원</p>
        </div>
      </div>

      <div class="payment-model" v-if="paymentVisible">
          <div class="home3-3-1__bot">
            <p class="tit">결제수단</p><router-link to="">수정</router-link>
          </div>

          <div class="home3-3-1__im">
            <img src="~@/assets/images/card.png" class="card" alt="결제카드">
          </div>
        </div>
          <div class="home3-3__bot" style="padding: 0px 0 100px 0;">
           <a style="cursor: pointer;" @click="handleOrder()">결제하기</a>
          </div>
      </div>
  </div>
</template>

<script lang="ts">
import { getOrder } from '@/api/order';
import { getUserInfo } from '@/api/user';
import { getUsableCouponList } from '@/api/userCoupon';
import { storageKey } from '@/enums/localStorage';
import { OrderModule } from '@/store/modules/order';
import { Vue, Component, Prop } from 'vue-property-decorator';
import { getShopPickupTimes } from '@/api/shop';

@Component({
  name: 'ProductPayment',
})

export default class extends Vue {
  mounted() {
    if (OrderModule.orderForm.count === 0) {
      this.$router.push({ name: 'Order' });
    }
    this.getCustomDietDetail();
    this.getUsableCouponList();
    this.getUserInfo();
    this.getPickupTimes();
    this.originAmount = this.orderForm.amount;
  }

  private orderForm = OrderModule.orderForm;

  private originAmount = 0;

  // private tossPayments = (window as any).TossPayments('live_ck_4vZnjEJeQVxGBjMzwEM3PmOoBN0k');
  // private tossPayments = (window as any).TossPayments('test_ck_jZ61JOxRQVEP7ldkoBR3W0X9bAqw');
  private tossPayments = (window as any).TossPayments(`${process.env.VUE_APP_TOSS_KEY}`);

  private loading = true;

  private times = [
    {
      value: '오후 4:00 ~ 오후 4:15',
      label: '오후 4:00 ~ 오후 4:15',
    },
    {
      value: '오후 4:15 ~ 오후 4:30',
      label: '오후 4:15 ~ 오후 4:30',
    },
  ];

  private couponList = [];

  private userInfo: any = {
    point: 0,
  }

  private time = '';

  private payMethod = 1;

  private payType = '';

  private payTypes = [
    {
      value: '일시불',
      label: '일시불',
    },
    {
      value: '할부',
      label: '할부',
    },
  ];

  private customDiet = {
    id: 0,
    dietType: 1,
    dietPurpose: 3,
    dietPrecaution: '',
    dietQuantity: 5,
    dietWeek: 4,
    dietPeriod: 1,
    paymentStatus: false,
    orderDate: '',
    orderList: [],
  };

  private shop: any = {
    idx: null,
    name: '',
    address: '',
    addressDetail: '',
  }

  private pickupTimes = [];

  private paymentVisible = false;

  private getCustomDietDetail() {
    this.loading = false;
    // const dietId: string = this.$route.query.dietId;
    /* if (dietId) {
      getCustomDietDetail(dietId).then((res) => {
        this.loading = false;
        this.customDiet = res.data;
      });
    } else {
      this.$notify.error('주문정보가 존재하지 않습니다.');
      this.$router.push({ name: 'Check' });
    } */
  }

  private handleChooseCoupon() {
    if (this.originAmount - this.orderForm.usePoint - this.getCouponDiscountPrice() < 100) {
      this.$message.info('쿠폰 할인금액은 결재 금액보다 클 수 없습니다.');
      this.orderForm.useCouponIdx = null;
    }
  }

  private handleUsePoint() {
    if (this.orderForm.usePoint < 0) {
      this.orderForm.usePoint = 0;
    }
    if (this.orderForm.usePoint > this.orderForm.amount - this.getCouponDiscountPrice() - 100) {
      this.$message.info('최소 100원 이상은 구매하셔야 합니다.');
      this.orderForm.usePoint = this.orderForm.amount - this.getCouponDiscountPrice() - 100;
    }
    if (this.orderForm.usePoint > this.userInfo.point) {
      this.orderForm.usePoint = this.userInfo.point;
    }
  }

  private handleChangePoint() {
    if (this.orderForm.usePoint % 10 !== 0) {
      this.$message.info('10포인트 단위로 입력해 주세요.');
      this.orderForm.usePoint -= this.orderForm.usePoint % 10;
    }
  }

  private handleUseAllPoint() {
      if (this.orderForm.amount < this.userInfo.point + this.getCouponDiscountPrice()) {
        this.$message.info('포인트 사용은 총결제 금액을 초과할 수 없습니다.');
        this.orderForm.usePoint = this.orderForm.amount - this.getCouponDiscountPrice() - 100;
      } else if (this.orderForm.amount - (this.userInfo.point + this.getCouponDiscountPrice()) < 100) {
        this.$message.info('최소 100원 이상은 구매하셔야 합니다.');
        this.orderForm.usePoint = this.orderForm.amount - this.getCouponDiscountPrice() - 100;
      } else {
        this.orderForm.usePoint = this.userInfo.point;
      }
      this.orderForm.usePoint -= this.orderForm.usePoint % 10;
  }

  private handleOrder() {
    if (!this.orderForm.pickupTime) {
      this.$message.warning('픽업시간을 선택해주세요.');
      return;
    }
    this.shop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));
    this.loading = true;

    getOrder({
      ...this.orderForm,
      shopId: this.shop.idx,
      orderType: 'PICKUP',
    }).then((res) => {
      this.tossPayments.requestPayment('카드', { // 결제 수단 파라미터
        // 결제 정보 파라미터
        amount: res.data.amount,
        orderId: res.data.key,
        orderName: '와로샐러드',
        successUrl: `${process.env.VUE_APP_TOSS_SUSSCESS}`,
        failUrl: `${process.env.VUE_APP_TOSS_FAIL}`,
        useAppCardOnly: false,
      }).catch((error: any) => {
        this.$message.warning(error.message);
        this.loading = false;
        if (error.code === 'USER_CANCEL') {
          this.$router.push({ name: 'Home' });
          // 결제 고객이 결제창을 닫았을 때 에러 처리
        } else if (error.code === 'INVALID_CARD_COMPANY') {
          this.$router.push({ name: 'Home' });
          // 유효하지 않은 카드 코드에 대한 에러 처리
        }
      });
    });
  }

  private getUsableCouponList() {
    getUsableCouponList().then((res) => {
      this.couponList = res.data;
    });
  }

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
    });
  }

  private getPickupTimes() {
    this.shop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));
    if (!this.shop) {
      this.$message.warning('픽업매장을 선택해주세요.');
      this.$router.push({ name: 'Home' });
    }
    getShopPickupTimes(this.shop.idx).then((res) => {
      this.pickupTimes = res.data;
    });
  }

  /* eslint-disable */
  private getCouponDiscountPrice() {
    let discountPrice = 0;

    if (this.orderForm.useCouponIdx) {
      const idx = this.couponList.findIndex((coupon: any) => coupon.idx === this.orderForm.useCouponIdx);
      if (idx > -1) {
        const useCoupon: any = this.couponList[idx];
        if (useCoupon.percentStatus) discountPrice = this.orderForm.amount * useCoupon.discountPercent / 100;
        else discountPrice = useCoupon.discountPrice;
      }
    }
    return discountPrice;
  }
  /* eslint-enable */
}
</script>

<style>

</style>
