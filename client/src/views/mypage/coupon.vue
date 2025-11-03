<template>
  <div class="home4-2__wr">
    <div v-loading="loading" class="home4-2">
      <slot v-if="couponList.length > 0">
        <div v-for="(coupon, index) in couponList" :key="index" class="home4-2__box" :class="coupon.downloadStatus ? '' : 'blue'">
          <div class="home4-2__box__a">
            <a v-if="coupon.downloadStatus" href="#">받기<br>완료</a>
            <a v-else href="#" @click="handleDownloadCoupon(coupon)">다운<br>로드</a>
          </div>
          <div class="home4-2__box__b">
            <p class="ti">{{ coupon.name }}</p>
            <p class="tti">{{ getCouponDiscount(coupon) }} 할인 쿠폰</p>
            <div class="home4-2__box__b1">
              <p class="ti">{{ coupon.expiredDate | parseDate('YYYY.MM.DD HH:mm:ss') }} 까지</p>
              <p class="ti a">{{ getRemainExpiredDate(coupon) }}</p>
            </div>
          </div>
        </div>
      </slot>
      <slot v-else>
        <div>발급된 쿠폰이 없습니다.</div>
      </slot>
    </div>
    <Pagination
      :total="totalElements"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.size"
      @pagination="getCouponList"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';
import { getUserCouponList, donwloadUserCoupon } from '@/api/userCoupon';
import { parseKrw } from '@/filters';
import moment from 'moment';

@Component({
  components: {
    Pagination,
  },
})
export default class extends Vue {
  mounted() {
    this.getCouponList();
  }

  private loading = true;

  private couponList = [];

  private totalElements = 0;

  private listQuery = {
    page: 1,
    size: 10,
  }

  private getCouponList() {
    this.loading = true;
    getUserCouponList(this.listQuery).then((res) => {
      this.loading = false;
      this.couponList = res.data.content;
      this.totalElements = res.data.totalElements;
    });
  }

  private handleDownloadCoupon(coupon: any) {
    donwloadUserCoupon(coupon.idx).then(() => {
      this.$message.success('쿠폰이 다운로드 되었습니다.');
      coupon.downloadStatus = true;
    }).catch((error) => {
      // error message가 출력이 안됨 확인할 수 없음
      this.$message.info('픽업이 완료되지 않아 다운로드 하실 수 없습니다.');
      if (error.response.data.message === 'NOT_PICKUP_ORDER') {
        // this.$message.info('픽업이 완료되지 않아 다운로드 하실 수 없습니다.');
      }
    });
  }

  private getCouponDiscount(coupon: any) {
    if (coupon.percentStatus) {
      return `${coupon.discountPercent}%`;
    }
    return `${parseKrw(coupon.discountPrice)}원`;
  }

  private getRemainExpiredDate(coupon: any) {
    const remainDays = moment(coupon.expiredDate).diff(moment(), 'days');
    if (remainDays > 0) return `D-${remainDays}`;
    return 'D-DAY';
  }
}
</script>
