<template>
  <div>
    <div class="custom__detail">
      <div class="custom__detail-ttl sft">[와로 샐러드] {{ product.name }}</div>
      <div class="custom__detail-banner">
        <img v-if="product.thumbUid" :src="`${apiUrl}/attached-file/${product.thumbUid}`" class="img464-1" alt="">
        <img v-else src="~@/assets/images/tmp_custom_detail_banner.jpg" alt="" />
      </div>
      <div class="home3-3__top__c station">
        <table>
          <caption>배송 정보</caption>
          <tbody>
            <tr>
              <th>장소 이름</th>
              <td>{{ selectedStation.name }}</td>
            </tr>
            <tr>
              <th>장소 주소</th>
              <td>({{ selectedStation.postCode }}) {{ selectedStation.address }}</td>
            </tr>
            <tr>
              <th>상세 주소 입력</th>
              <td>
								<el-input placeholder="상세 주소를 입력하세요" type="text" v-model="addressDetail" />
              </td>
            </tr>
            <!-- <tr>
              <th>회차 선택
              </th>
              <td>
                  <div class="right">
                    <el-input-number v-model="orderForm.count" :min="1" :max="4"></el-input-number>
                    <span class="txt">원하시는 주차를 선택해주세요. 최소 4주차 까지 가능합니다.</span>
                  </div>
                </td>
              </tr> -->
              </tbody>
        </table>
      </div>
      <div class="home3-3__top__c station">
        <table>
          <caption>상품 정보</caption>
          <tbody>
            <tr>
              <th>배송 주차 횟수</th>
              <td>
                  <div class="right">{{ product.weekDeliveryCnt }}주</div>
                </td>
            </tr>
            <tr>
              <th>개당 상품가격</th>
              <td>
                  <div class="right">{{ product.price | parseKrw}}원</div>
                  <span class="txt">요일 선택시 상품수량에 따라 적용되는 금액입니다.</span>
                </td>
            </tr>
            <tr>
              <th>배송비</th>
              <td>
                  <div class="right" v-if="product.deliveryFee === 0">무료</div>
                  <div class="right" v-else>{{ product.deliveryFee | parseKrw}}원</div>
                </td>
            </tr>
            <tr>
                <th>요일 선택</th>
                <td>
                  <ul class="weeks" style="gap:5px">
                    <li class="weeks__item" :class="{ 'active' : week.mon === true }" @click="handleWeekCheck(1)">
                      <a>월</a>
                    </li>
                    <li class="weeks__item" :class="{ 'active' : week.tue === true }" @click="handleWeekCheck(2)">
                      <a>화</a>
                    </li>
                    <li class="weeks__item" :class="{ 'active' : week.wed === true }" @click="handleWeekCheck(3)">
                      <a>수</a>
                    </li>
                    <li class="weeks__item" :class="{ 'active' : week.thu === true }" @click="handleWeekCheck(4)">
                      <a>목</a>
                    </li>
                    <li class="weeks__item" :class="{ 'active' : week.fri === true }" @click="handleWeekCheck(5)">
                      <a>금</a>
                    </li>
                  </ul>
                  <span class="txt">원하시는 요일을 선택해주세요. 최소 2일 이상 선택해주세요</span>
              </td>
            </tr>
            <!-- <tr>
              <th>상품 수량</th>
              <td>
                  <div class="right">
                    <el-input-number v-model="orderForm.count" :min="1" :max="10"></el-input-number>
                  </div>
                </td>
            </tr>-->
            <tr>
              <th>추가 상품 선택</th>
              <td>
                <el-select placeholder="추가 상품 선택" :popper-append-to-body="false" v-model="extraProduct" @change="handleExtraProductAdd">
                  <el-option v-if="weekCheck() === 0" label="요일을 선택해 주세요" value=''/>
                  <el-option v-else label="추가 상품 선택" value=''/>
                  <el-option-group v-for="(product, index) in extraProducts" :key="index">
                    <el-option v-if="week.mon" :label="'(월) ' + product.name +  ' : ' + parseKrw(product.price) + '원'" :value="{extraProduct: product, dayNum: 1}" />
                    <el-option v-if="week.tue" :label="'(화) ' + product.name +  ' : ' + parseKrw(product.price) + '원'" :value="{extraProduct: product, dayNum: 2}" />
                    <el-option v-if="week.wed" :label="'(수) ' + product.name +  ' : ' + parseKrw(product.price) + '원'" :value="{extraProduct: product, dayNum: 3}" />
                    <el-option v-if="week.thu" :label="'(목) ' + product.name +  ' : ' + parseKrw(product.price) + '원'" :value="{extraProduct: product, dayNum: 4}" />
                    <el-option v-if="week.fri" :label="'(금) ' + product.name +  ' : ' + parseKrw(product.price) + '원'" :value="{extraProduct: product, dayNum: 5}" />
                  </el-option-group>
                </el-select>
                <span class="txt">추가상품은 중복선택이 가능합니다.</span>
              </td>
            </tr>
            <tr>
              <th>현관 비밀번호</th>
              <td>
								<el-input placeholder="현관 비밀번호를 입력해주세요" type="text" v-model="orderForm.memo" />
                <!-- <span class="txt"></span> -->
              </td>
            </tr>
            <!--
            <tr v-if="this.extraProductInfo.idx !== ''">
              <th>추가 상품 가격</th>
              <td>
                <div class="right">{{extraProductInfo.price | parseKrw}}원</div>
                </td>
            </tr>
            <tr>
              <th>추가 상품 수량</th>
              <td>
                <div class="right">
                    <el-input-number v-model="extraProductInfo.count" :min="1" :max="10"></el-input-number>
                  </div>
                </td>
            </tr> -->
          </tbody>
        </table>
        <template v-for="(week, index) in orderForm.week">
          <div :key="index" class="cart__top__b" style="padding-bottom:10px;">
            <p class="tti">[ {{Number(index)}} 주차 ]<span class="tti color2">{{week.startDate}} ~ {{week.endDate}}</span></p>
            <template v-for="(day, index2) in week.day">
              <p class="weeks" :key="index2">[ {{ index2 | filterDay}} ]</p>
              <template v-for="(item, index3) in day.products">
                <div v-if="!item.product.extraProductStatus" :key="index + index2 + index3" class="cart__box" style="align-items: center;">
                  <div class="left">
                  <p class="ttti"><span v-if="item.product.extraProductStatus">[추가] </span>{{item.product.name}}</p>
                  <p v-if="item.product.extraProductStatus" class="ttti a">{{item.product.price}}원</p>
                  </div>
                  <div class="right" style="align-items:center" >
                    <el-input-number v-model="item.count" :min="1" :max="10" @change="handleProduct(item, index, index2, index3)"></el-input-number>
                    <p v-if="item.product.extraProductStatus" class="cancel" style="position:absolute;left:auto;top: auto;right: 4%" @click="handleCancel(item, index, index2)"/>
                  </div>
                </div>
              </template>
              <template v-for="(item, index3) in day.products">
                <div v-if="item.product.extraProductStatus" :key="index + index2 + index3" class="cart__box" style="align-items: center;">
                  <div class="left">
                  <p class="ttti"><span v-if="item.product.extraProductStatus">[추가] </span>{{item.product.name}}</p>
                  <p v-if="item.product.extraProductStatus" class="ttti a">{{item.product.price}}원</p>
                  </div>
                  <div class="right" style="align-items:center" >
                    <el-input-number v-model="item.count" :min="1" :max="10" @change="handleProduct(item, index, index2, index3)"></el-input-number>
                    <p v-if="item.product.extraProductStatus" class="cancel" style="position:absolute;left:auto;top: auto;right: 4%" @click="handleCancel(item, index, index2)"/>
                  </div>
                </div>
              </template>
            </template>
          </div>
        </template>
      </div>
      <div class="home3-3__top__c" style="margin-bottom:150px">
        <table>
          <caption>쿠폰 / 할인 / 적립금</caption>
          <tbody>
            <tr>
              <th>쿠폰 할인</th>
              <td>
                <el-select v-model="orderForm.useCouponIdx" placeholder="쿠폰을 선택하세요" :popper-append-to-body="false" @change="handleChooseCoupon">
                  <el-option label="선택없음" value="" />
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
      <div class="order__footer-fixed">
        <div class="home3-3__top__b-1">
            <p class="tii">총결제 금액</p>
            <p class="tiii">{{  orderForm.amount +  orderForm.deliveryFee - orderForm.usePoint - getCouponDiscountPrice() + deliveryPrice | parseKrw }}원</p>
        </div>
          <div class="home3-3__bot" style="padding: 0px 0 10px 0;">
           <a style="cursor: pointer;" @click="handleOrder()">결제하기</a>
          </div>
        </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getProductDetail, getProductList } from '@/api/product';
import Pagination from '@/components/Pagination/index.vue';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';
import { OrderModule2 } from '@/store/modules/order2';
import { getUserInfo } from '@/api/user';
import moment from 'moment';
import { getOrder } from '@/api/order';

@Component({
	name: 'ProductRotation',
	components: {
		Pagination,
  },
})

export default class extends Vue {
	private deliveryPrice = 0;

	private loading = true;

	private product: any = {};

  private extraProducts: any = {};

	private selectedStation = JSON.parse((window as any).localStorage.getItem(storageKey.stationPlace));

	private apiUrl = process.env.VUE_APP_COMMON_API;

  // private tossPayments = (window as any).TossPayments('live_ck_4vZnjEJeQVxGBjMzwEM3PmOoBN0k');
  // private tossPayments = (window as any).TossPayments('test_ck_jZ61JOxRQVEP7ldkoBR3W0X9bAqw');
  private tossPayments = (window as any).TossPayments(`${process.env.VUE_APP_TOSS_KEY}`);

	async mounted() {
    // OrderModule2.setOrderClean();
		if (!UserModule.isLogin) {
			this.$message.info('로그인이 필요한 서비스 입니다.');
			this.$router.push({ name: 'Login' });
		}
    await this.handleProductDetail();
		this.handleProductList();
    this.getUserInfo();
	}

  private orderForm = OrderModule2.orderForm;

  private extraProduct: any = '';
  // private extraProduct: any = {
    // idx: '',
    // dayNum: 1,
    // price: '',
  // }

  private rules = {
    // title: [
      // { required: true, message: '제목을 입력하세요.', trigger: 'blur' },
    // ],
    // categoryList: [
      // { validator: this.categoryValidator, trigger: 'blur' },
    // ],
    addressDetail: [
      { required: true, message: '상세 주소를 입력하세요.', trigger: 'blur' },
    ],
  };

  private week = {
    mon: false, tue: false, wed: false, thu: false, fri: false,
  };

  private parseKrw = (value: string) => {
  const number = Number(value);
  // eslint-disable-next-line no-restricted-globals
  if (isNaN(number)) return value;
  return Number(number).toLocaleString('ko-KR');
};

	private listQuery = {
		page: 1,
		size: 10,
		searchType: 'name',
		searchValue: '',
		productType: 'STATION',
		extraProductStatus: true,
	}

  private productId = this.$route.params.productIdx;

	async handleProductDetail() {
		this.loading = true;
		await getProductDetail(Number(this.productId)).then((res) => {
			this.product = res.data;
      OrderModule2.setDeliveryFee(this.product.deliveryFee);
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

  handleProductAdd(product: any) {
    for (let i = 1; i <= this.product.weekDeliveryCnt; i += 1) {
      const lastMonday = moment().add(i, 'weeks').startOf('isoWeek');
      const friday = moment(lastMonday).add(4, 'days');
      const startDate = lastMonday.format('YYYY-MM-DD');
      const endDate = friday.format('YYYY-MM-DD');
      OrderModule2.setStartDate(startDate);
      OrderModule2.setEndDate(endDate);
      let weekNum = lastMonday.diff(moment(lastMonday).startOf('month').startOf('isoWeek'), 'weeks');
      if (moment(lastMonday).startOf('month').startOf('isoWeek').date() < 4) {
        weekNum += 1;
      }
      OrderModule2.setWeekNum(i);
      OrderModule2.addProduct({
        product: this.product,
        count: 1,
    });
    }
  }

  // 해당요일 전체 상품 삭제
  private handleProductCancel(dayNum: number) {
    for (let i = 1; i <= this.product.weekDeliveryCnt; i += 1) {
      const products: any = this.orderForm.week[i].day[dayNum].products;
      const keys: any = Object.keys(products);
      keys.forEach((key: any) => {
        OrderModule2.cancelProduct({
          count: 1,
          product: products[key].product,
          weekNum: [i],
          dayNum: [dayNum],
        });
    });
    }
  }

  private handleProduct(item: any, weekNum: number, dayNum: number, productNum: number) {
    const products: any = this.orderForm.week[weekNum].day[dayNum].products;
    const keys: any = Object.keys(products);
    let sum = 0;
    keys.forEach((key: any) => {
      sum += Number(products[key].count);
    });
    OrderModule2.setProduct({
      ...item,
      weekNum: [weekNum],
      dayNum: [dayNum],
    });
  }

  private handleExtraProductAdd(item: any) {
    for (let i = 1; i <= this.product.weekDeliveryCnt; i += 1) {
      const lastMonday = moment().add(i, 'weeks').startOf('isoWeek');
      const friday = moment(lastMonday).add(4, 'days');
      const startDate = lastMonday.format('YYYY-MM-DD');
      const endDate = friday.format('YYYY-MM-DD');
      OrderModule2.setStartDate(startDate);
      OrderModule2.setEndDate(endDate);
      let weekNum = lastMonday.diff(moment(lastMonday).startOf('month').startOf('isoWeek'), 'weeks');
      if (moment(lastMonday).startOf('month').startOf('isoWeek').date() < 4) {
        weekNum += 1;
      }
      OrderModule2.setWeekNum(i);
      OrderModule2.setDayNum(item.dayNum);
      OrderModule2.addProduct({
        product: item.extraProduct,
        count: 1,
    });
    this.extraProduct = '';
  }
  }

  private weekCheck() {
    let count = 0;
    if (this.week.mon) count += 1;
    if (this.week.tue) count += 1;
    if (this.week.wed) count += 1;
    if (this.week.thu) count += 1;
    if (this.week.fri) count += 1;

    // if (count >= this.product.weekDeliveryCnt) {
      // this.$message.info('최대 선택가능한 요일을 초과하였습니다.');
      // return false;
    // }
    return count;
  }

  private handleWeekCheck(type: number) {
    let setflag = false;
    if (type === 1 && this.week.mon) {
      this.week.mon = !this.week.mon;
      setflag = true;
    } else if (type === 2 && this.week.tue) {
      this.week.tue = !this.week.tue;
      setflag = true;
    } else if (type === 3 && this.week.wed) {
      this.week.wed = !this.week.wed;
      setflag = true;
    } else if (type === 4 && this.week.thu) {
      this.week.thu = !this.week.thu;
      setflag = true;
    } else if (type === 5 && this.week.fri) {
      this.week.fri = !this.week.fri;
      setflag = true;
    }
    if (setflag) {
      this.handleProductCancel(type);
      return true;
    }

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
    OrderModule2.setDayNum(type);
    this.handleProductAdd(this.product);

    return true;
  }

  private originAmount = 0;

  private couponList = [];

  private addressDetail = '';

  private userInfo: any = {
    point: 0,
  }

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
  });
}

  private handleChooseCoupon() {
    if (this.orderForm.amount - this.orderForm.usePoint - this.getCouponDiscountPrice() < 100) {
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

  private station: any = {
    idx: null,
    name: '',
    address: '',
    addressDetail: '',
  };

  private handleUseAllPoint() {
      if (this.orderForm.amount <= 100) {
        this.$message.info('상품을 선택해주세요');
        this.orderForm.usePoint = 0;
      } else if (this.orderForm.amount < this.userInfo.point + this.getCouponDiscountPrice()) {
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
    if (this.addressDetail === '') {
      this.$message.info('상세 주소를 입력해주세요.');
      return false;
    }

    if (this.weekCheck() < 2) {
      this.$message.info('원하시는 요일을 2일이상 선택해주세요.');
      return false;
    }

    this.station = JSON.parse((window as any).localStorage.getItem(storageKey.stationPlace));
    this.loading = true;
    getOrder({
      ...this.orderForm,
      stationId: this.station.idx,
      addressDetail: this.addressDetail,
      orderType: 'STATION',
    }).then((res) => {
        this.tossPayments.requestPayment('카드', { // 결제 수단 파라미터
        // 결제 정보 파라미터
        // amount: this.orderForm.amount - this.orderForm.usePoint - this.getCouponDiscountPrice() + this.deliveryPrice,
        amount: res.data.amount,
        orderId: res.data.key,
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
    });
  }

  handleProductList() {
		this.loading = true;
		getProductList(this.listQuery).then((res) => {
			this.extraProducts = res.data.content;
			this.loading = false;
		});
	}

    private handleProductChange(value: string): void {
    console.log(1)
  }

  private handleCancel(item: any, weekNum: number, dayNum: number) {
    OrderModule2.cancelProduct({
      ...item,
      weekNum: [weekNum],
      dayNum: [dayNum],
    });
  }
}
</script>
