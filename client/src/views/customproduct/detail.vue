<template>
  <div v-loading="loading">
    <div class="custom__detail">
      <div class="custom__detail-ttl sft">[와로 샐러드] {{ product.name }}</div>
      <div class="custom__detail-banner">
        <img v-if="product.thumbUid" :src="`${apiUrl}/attached-file/${product.thumbUid}`" class="img464-1" alt="">
        <img v-else src="~@/assets/images/tmp_custom_detail_banner.jpg" alt="" />
      </div>
      <div class="home3-3__top__c station">
        <table>
          <caption>제품 정보</caption>
          <tbody>
            <tr>
              <th>배송 주차 수</th>
              <td>{{ product.weekDeliveryCnt }}주</td>
            </tr>
            <tr>
              <th>개당 상품가격</th>
              <td>{{ product.price | parseKrw }}원</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td v-if="product.deliveryFee === 0">무료</td>
              <td v-else>{{ product.deliveryFee | parseKrw}}원</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="home3-3__top__c station" style="margin-bottom:80px;">
      <caption>상품 소개</caption>
      <div class="product__description note-editor" v-html="product.description"></div>
      </div>
      <div class="custom__detail-btn__list" style="position: fixed;">
				<router-link :to = " { name: 'CustomProductOrder', params: { productIdx: product.idx } } " class="custom__detail-btn__item btn02 sft">구매하기</router-link>
      </div>
    </div>
  </div>
</template>
<style>
.note-editor ul, .note-editor ol {
  list-style: none;
}
.note-editor table {
    width: 100%;
    border-collapse: collapse;
}
.note-editor table td, .note-editor table th {
    border: 1px solid #ececec;
    padding: 5px 3px;
}
.note-editor img {
  width:100% !important;
}
.el-progress-bar__innerText {display:none;}
</style>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getProductDetail } from '@/api/product';
import Pagination from '@/components/Pagination/index.vue';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';
import { OrderModule } from '@/store/modules/order';
import { getUserInfo } from '@/api/user';
import { getDiaryMealStatistics } from '@/api/diary';
import moment from 'moment';
import { Loading } from 'element-ui';

@Component({
	name: 'ProductRotation',
	components: {
		Pagination,
  },
})

export default class extends Vue {
	private loading = true;

	private product: any = {};

	private selectedStation = JSON.parse((window as any).localStorage.getItem(storageKey.stationPlace));

	private apiUrl = process.env.VUE_APP_COMMON_API;

  private tossPayments = (window as any).TossPayments('live_ck_4vZnjEJeQVxGBjMzwEM3PmOoBN0k');

	mounted() {
		if (!UserModule.isLogin) {
			this.$message.info('로그인이 필요한 서비스 입니다.');
			this.$router.push({ name: 'Login' });
		}
		this.handleProductList();
    this.getUserInfo();
    this.getDiaryMealStatistics();
	}

  private orderForm = OrderModule.orderForm;

  private station = '';

  private week = {
    mon: false, tue: false, wed: false, thu: false, fri: false,
  };

	private listQuery = {
		page: 1,
		size: 10,
		searchType: 'name',
		searchValue: '',
		productType: 'STATION',
		extraProductStatus: false,
	}

  private listQuery2 = {
    diaryDate: moment(),
    type: 0,
  }

  private productId = this.$route.params.productIdx;

  format(percentage: any) {
      return percentage === 100 ? 'Full' : `${percentage}%`;
  }

	handleProductList() {
		this.loading = true;
		getProductDetail(Number(this.productId)).then((res) => {
			this.product = res.data;
			this.loading = false;
		});
	}

	handleChoice(product: any) {
		this.$router.push({
			name: 'CustomProductDetail',
			params: {
				productId: product.idx,
			},
		});
	}

  private handleWeekCheck(type: number) {
    if (type === 1) {
      this.week.mon = !this.week.mon;
    } else if (type === 2) {
      this.week.tue = !this.week.tue;
    } else if (type === 3) {
      this.week.wed = !this.week.wed;
    } else if (type === 4) {
      this.week.thu = !this.week.thu;
    } else if (type === 5) {
      this.week.fri = !this.week.fri;
    }
  }

  private originAmount = 0;

  private couponList = [];

  private userInfo: any = {
    point: 0,
  }

  private data = {
    amount: 0,
    calorie: 0,
    carbohydrate: 0, // 탄수화물(g)
    protein: 0, // 단백질(g)
    fat: 0, // 지방(g)
    sodium: 0, // 나트륨(mg)
    sugar: 0, // 당(g)
    saturatedFattyAcids: 0, // 포화지방(g)
    vitaminC: 0, // 비타민c(mg)
    transFat: 0, // 트랜스지방(mg)
    cholesterol: 0, // 콜레스테롤(mg)
    calcium: 0, // 칼슘(mg)
    iron: 0, // 철(mg)
    potassium: 0, // 컬륨(mg)
    dietaryFiber: 0, // 식이섬유(g)
  }

  private getDiaryMealStatistics() {
    this.loading = true;
    getDiaryMealStatistics(this.listQuery2).then((res) => {
      this.data = res.data;
      this.loading = false;
    });
  }

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
  });
}

  private handleChooseCoupon() {
    if (this.originAmount - this.orderForm.usePoint - this.getCouponDiscountPrice() < 100) {
      this.$message.info('쿠폰 할인금액은 결재 금액보다 클 수 없습니다.');
      this.orderForm.useCouponIdx = null;
    }
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
  /* eslint-disable */

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
    this.station = JSON.parse((window as any).localStorage.getItem(storageKey.stationPlace));
    this.loading = true;
  this.orderForm.amount = 100;
  this.tossPayments.requestPayment('카드', { // 결제 수단 파라미터
    // 결제 정보 파라미터
    amount: this.orderForm.amount,
    orderId: 'abcabc',
    orderName: '와로샐러드',
    successUrl: `${process.env.VUE_APP_TOSS_SUSSCESS}`,
    failUrl: `${process.env.VUE_APP_TOSS_FAIL}`,
    useAppCardOnly: false,
  }).catch((error: any) => {
    this.loading = false;
    this.$message.info('결제가 취소되었습니다. 다시 시도해 주세요.')
    if (error.code === 'USER_CANCEL') {
      // this.$router.push({ name: 'Home' });
      // 결제 고객이 결제창을 닫았을 때 에러 처리
    } else if (error.code === 'INVALID_CARD_COMPANY') {
      // 유효하지 않은 카드 코드에 대한 에러 처리
      // this.$router.push({ name: 'Home' });
    }
  });
  }
}
</script>
