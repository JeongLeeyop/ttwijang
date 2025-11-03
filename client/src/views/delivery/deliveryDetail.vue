<template>
  <div class="home4__wr">
    <div class="home3-3__wr">
      <p class="ti a" v-if="this.$route.params.orderStatus === 0">주문이 접수 되었습니다.</p>
      <p class="ti a" v-else-if="this.$route.params.orderStatus === 1">서비스가 제공 중입니다.</p>
      <p class="ti a" v-else-if="this.$route.params.orderStatus === 2">맞춤식단 배송을 완료하였습니다.</p>
      <p class="ti a" v-else-if="this.$route.params.orderStatus === -1">주문이 취소되었습니다.</p>
      <div v-if="detail.reviewStatus" class="home4-1-2__tail a btn">
        <router-link :to="{ name: 'MyReview' }" class="btn2">내 후기 보기</router-link>
      </div>
      <div v-if="writableReview()" class="home4-1-2__tail a btn">
        <router-link :to="{ name: 'ReviewForm', params: { idx: this.$route.params.orderGroupId, orderStatus: this.$route.params.orderStatus } }" class="btn">후기작성</router-link>
      </div>
      <p class="ti color2">주문 정보</p>
      <div class="home3-3__top__a">
        <div class="home3-3__top__a-1">
          <p class="tti">식단관리 기간</p>
          <p class="ttti"> {{ getTotalWeek() }}주</p>
          <p class="tti">총 식사 일수</p>
          <p class="ttti"> {{ getTotalMealDate() }}일</p>
          <p class="tti">총 상품 수량</p>
          <p class="ttti"> {{ detail.productNum }}개</p>
        </div>
      </div>

      <p class="ti">식단 리스트</p>
      <div class="home3-3__top__b">
        <div v-for="(week, index) in detail.week" :key="index">
          <div>
            <p class="tti">[ {{ index }}주차 ]<span class="tti color2"> {{ week.startDate }} ~ {{ week.endDate }}</span></p>
            <div class="home3-3__box">
              <div v-for="(product, productIndex) in week.products" :key="productIndex">
                <!--
                <div v-if="shouldDisplayItem(product.dayNum, week.startDate)" class="weeks">
                  [ {{ product.dayNum | filterDay }} ]
                </div>
                -->
                <div class="weeks">[ {{ product.dayNum | filterDay}} ]</div>
                <div class="item">
                  <p class="ttti">{{ product.productName }} X{{ product.productNum }}개</p>
                  <p class="ttti a">{{ product.amount | parseKrw }}원</p>
                </div>
              </div>
            </div>
          </div>
          <a
            v-if="detail.orderType === 'PICKUP'&& getTotalWeek() != 1 && week.products[0].orderStatus != -1 && week.products[0].pickupStatus == 0 && computeRefundDate(week.startDate, week.products[0].dayNum) >= 0"
            @click="handleRefundVisible(index)"
          >
            <p class="cancel_box">부분취소</p>
          </a>
          <p class="ti a status" v-if="week.products[0].orderStatus == -1" :key="index">주문이 취소 되었습니다.</p>
        </div>
        <div class="home3-3__top__c">
        <table>
          <caption>할인금액</caption>
          <tbody>
            <tr>
              <th>사용 포인트</th>
              <td>{{ detail.usePoint | parseKrw  }} 포인트</td>
            </tr>
            <tr>
              <th>총 할인 금액</th>
              <td>{{detail.discountAmount | parseKrw  }} 원</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td v-if="detail.deliveryFee === 0">무료</td>
              <td v-else>{{ detail.deliveryFee | parseKrw  }} 원</td>
            </tr>
            <tr>
              <th>전체 금액</th>
              <td>{{ detail.amount + detail.discountAmount | parseKrw  }} 원</td>
            </tr>
          </tbody>
        </table>
      </div>
        <div class="home3-3__top__b-1">
          <p class="tii">총결제 금액</p>
          <p class="tiii">{{ detail.amount | parseKrw }}원</p>
        </div>
      </div>
      <div class="home3-3__top__c">
        <table v-if="detail.orderType === 'PICKUP'">
          <caption>선택된 픽업 장소</caption>
          <tbody>
            <tr>
              <th>장소 이름</th>
              <td>{{ detail.shop.name }}</td>
            </tr>
            <tr>
              <th>장소 주소</th>
              <td>{{ detail.shop.address }} {{ detail.shop.addressDetail }}</td>
            </tr>
            <tr>
              <th>픽업 시간</th>
              <td v-if="detail.week[0]"> {{ detail.week[0].products[0].pickupTime }} </td>
              <td v-else-if="detail.week[1]"> {{ detail.week[1].products[0].pickupTime }} </td>
              <td v-else-if="detail.week[2]"> {{ detail.week[2].products[0].pickupTime }} </td>
              <td v-else-if="detail.week[3]"> {{ detail.week[3].products[0].pickupTime }} </td>
              <td v-else-if="detail.week[4]"> {{ detail.week[4].products[0].pickupTime }} </td>
              <td v-else-if="detail.week[5]"> {{ detail.week[5].products[0].pickupTime }} </td>
            </tr>
          </tbody>
        </table>
        <table v-else-if="detail.orderType === 'STATION'">
          <caption>선택된 배송지</caption>
          <tbody>
            <tr>
              <th>장소 이름</th>
              <td>{{ detail.station.name }}</td>
            </tr>
            <tr>
              <th>장소 주소</th>
              <td>{{ detail.station.address }}</td>
            </tr>
            <tr>
              <th>상세 주소</th>
              <td>{{ detail.addressDetail }}</td>
            </tr>
            <tr>
              <th>현관 비밀번호</th>
              <td>{{ detail.memo }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div
      v-if="this.$route.params.orderStatus !== -1 && this.$route.params.orderStatus === 0 && allRefundCk"
      class="home4-1-2__tail">
      <a @click="handleAllRefundVisible()" class="">전체취소</a>
    </div>
    </div>
    <div v-show="refundVisible" class="home4-1-3__a">
      <div class="home4-1-3__top">
        <p class="ab">정말로 취소하시겠습니까?</p>
      </div>
      <div class="home4-1-3__mid">
        <a class="link__a" @click="handleRefund()">네</a>
        <a class="link__b" @click="handleRefundVisible()">아니요</a>
      </div>
    </div>
    <div v-show="completeVisible" class="home4-1-3__a">
      <div class="home4-1-3__top">
        <p class="ab">완료되었습니다.</p>
      </div>
      <div class="home4-1-3__mid">
        <a  @click="handleCompleteRefundVisible()">확인</a >
      </div>
      this.$router.push({ name : '/delivery' });
    </div>
    <div v-show="allRefundVisible" class="home4-1-3__a">
      <el-form ref="form" class="" onsubmit="return false;" :model="form" :rules="rules">
      <div class="diary2-2-3__top">
        <a @click="handleCloseAllRefundVisible()" class="el-button el-button--default"><img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
        </a>
      </div>
      <div class="home4-1-4__top">
        <p class="ab">주문을 취소하시는 이유는 무엇입니까?</p>
      </div>

      <div class="home4-1-4__mid">
        <el-form-item prop="cancelReasonType">
          <el-select :popper-append-to-body="false" v-model="form.cancelReasonType" @change="categoryEvent()" placeholder="카테고리를 선택해주세요.">
            <el-option value="단순변심" label="단순변심" />
            <el-option value="다른 상품 추가 후 재주문 예정" label="다른 상품 추가 후 재주문 예정" />
            <el-option value="다른 상품 잘못 주문" label="다른 상품 잘못 주문" />
            <el-option value="기타" label="기타 ( 직접입력 )" />
          </el-select>
        </el-form-item>
        <el-form-item prop="cancelReason">
          <el-input
            v-model="form.cancelReason"
            type="textarea"
            ref="content"
            :rows="6"
            placeholder="상세 사유를 입력해주세요."
            style="text-align: center"
            maxlength="200"
          />
        </el-form-item>
      </div>
    </el-form>
      <div class="home4-1-4__bot">
        <a href="#" @click="handleCompleteAllRefund()" class="link__a">취소 완료</a>
      </div>
    </div>
  </div>
</template>
<style>
.home4-1-3__a {
    content: "";
    position: absolute;
    width: 480px;
    height: 120%;
    top: -60px;
    left: 50%;
    right: 0;
    bottom: 0;
    transform: translateX(-50%);
    background-color: rgba(0,0,0,.8);
    z-index: 1000;
}
</style>

<script lang="ts">
import { getOrderGroup } from '@/api/orderGroup';
import { Vue, Component } from 'vue-property-decorator';
import moment from 'moment';
import { getCancelOrder } from '@/api/order';
import { JoinModule } from '@/store/join';
import { Form } from 'element-ui';
import { ElForm } from 'element-ui/types/form';

@Component({
  name: 'DeliveryDetail',
})

export default class extends Vue {
  private groupIdx = Number(this.$route.params.orderGroupId);

  private detail: any = {
    week: {},
    reviewStatus: false,
    reviewExpiredStatus: true,
    lastPickupStatus: false,
    productNum: 0,
    shop: {},
    station: {},
  };

  private allRefundCk = false;

  private selectedWeek: null | number = null;

  private refundVisible = false;

  private completeVisible = false;

  private allRefundVisible = false;

  private previousItem: any = '';

  private previousWeek: any = '';

  private curDate: any = moment().format('YYYY-MM-DD');

  private form = {
    cancelReason: '',
    cancelReasonType: '',
  }

  private rules = {
    cancelReason: [
      { required: true, message: '상세사유를 입력해주세요.', trigger: 'blur' },
    ],
    cancelReasonType: [
      { required: true, message: '취소사유를 선택해주세요.', trigger: 'blur' },
    ],
  }

  async mounted() {
    await this.getOrderGroup();
  }

  private computeRefundDate(startDate: any, dayNum: any) {
    const res: any = moment(startDate).add(dayNum, 'day').add(-3, 'day').format('YYYY-MM-DD');
    const res2: any = moment(res).diff(this.curDate, 'days');
    return res2;
  }

  private async getOrderGroup() {
    await getOrderGroup(this.groupIdx).then((res) => {
      this.detail = res.data;
    }).then(() => {
      let startDate = '';
      let dayNum = '';
      if (this.detail.week[1]) {
        startDate = this.detail.week[1].startDate;
        dayNum = this.detail.week[1].products[0].dayNum;
      } else if (this.detail.week[2]) {
        startDate = this.detail.week[2].startDate;
        dayNum = this.detail.week[2].products[0].dayNum;
      } else if (this.detail.week[3]) {
        startDate = this.detail.week[3].startDate;
        dayNum = this.detail.week[3].products[0].dayNum;
      } else if (this.detail.week[4]) {
        startDate = this.detail.week[4].startDate;
        dayNum = this.detail.week[4].products[0].dayNum;
      }
      this.allRefundCk = this.computeRefundDate(startDate, dayNum) >= 0;
    });
  }

  private writableReview() {
    return this.detail.orderType === 'PICKUP' && this.detail.lastPickupStatus && !this.detail.reviewExpiredStatus && !this.detail.reviewStatus;
  }

  private getTotalWeek() {
    let totalWeek: any = 0;
    const weeks = Object.keys(this.detail.week);
    if (weeks.length > 0) totalWeek = weeks.length;
    return totalWeek;
  }

  private getTotalMealDate() {
    const days: any = [];
    const weeks: any = Object.keys(this.detail.week);
    for (let i = 0; i < weeks.length; i += 1) {
      this.detail.week[weeks[i]].products.forEach((product: any) => {
        const dayKey = `${product.weekNum}-${product.dayNum}`;
        if (days.indexOf(dayKey) < 0) days.push(dayKey);
      });
    }
    return days.length;
  }

  private handleRefundVisible(week: number) {
    this.refundVisible = !this.refundVisible;
    if (this.refundVisible) this.selectedWeek = week;
    else this.selectedWeek = null;
  }

  private shouldDisplayItem(item: any, week: any) {
    if (this.previousItem === item && this.previousWeek === week) {
      this.previousItem = item;
      this.previousWeek = week;
      return false;
    }
    this.previousItem = item;
    this.previousWeek = week;
    return true;
  }

  private handleRefund() {
    getCancelOrder({
      orderId: this.groupIdx,
      week: this.selectedWeek,
    }).then((res) => {
      this.completeVisible = true;
      this.refundVisible = false;
      this.getOrderGroup();
    });
  }

  private handleCompleteRefundVisible() {
    this.completeVisible = false;
    this.$router.push({ name: 'Delivery' });
  }

  // 전체 취소
  private handleAllRefundVisible() {
    this.allRefundVisible = true;
    (window as any).scrollTo(0, 0);
  }

  private handleCloseAllRefundVisible() {
    this.allRefundVisible = false;
  }

  private handleCompleteAllRefund() {
    (this.$refs.form as ElForm).validate((valid: boolean) => {
      if (valid) {
        getCancelOrder({
          orderId: this.groupIdx,
          week: 1,
          allStatus: true,
          cancelReasonType: this.form.cancelReasonType,
          cancelReason: this.form.cancelReason,
        }).then(() => {
          this.allRefundVisible = false;
          this.completeVisible = true;
          this.getOrderGroup();
        }).catch(() => {
          this.$message.error('전체 취소를 실패했습니다.');
        });
      }
    });
  }

  private categoryEvent() {
    if (this.form.cancelReasonType === '기타') {
      this.categoryFlag = true;
    } else {
      this.categoryFlag = false;
    }
  }

  private categoryFlag = false;
}
</script>
