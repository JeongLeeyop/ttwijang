<template>
  <div v-loading="loading">
    <div class="custom__detail">
      <div class="custom__detail-ttl sft">[패키지 구매] {{ packageData.name }}</div>

      <!-- 배송 정보 -->
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
                <input v-model="addressDetail" type="text" placeholder="상세 주소를 입력하세요" style="width: 100%; padding: 8px;" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 패키지 정보 -->
      <div class="home3-3__top__c station">
        <table>
          <caption>패키지 정보</caption>
          <tbody>
            <tr>
              <th>최대 배송 주차 수</th>
              <td>{{ packageData.maxWeekCount }}주</td>
            </tr>
            <tr>
              <th>주당 배송 일수</th>
              <td>{{ packageData.deliveryDaysPerWeek }}일</td>
            </tr>
            <tr>
              <th>최대 상품 갯수</th>
              <td>{{ packageData.selectableProductCount }}개</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td v-if="packageData.deliveryFee === 0">무료</td>
              <td v-else>{{ packageData.deliveryFee | parseKrw }}원</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 주차 선택 -->
      <div class="home3-3__top__c station">
        <p class="tti">주차 선택 (1~{{ packageData.maxWeekCount }}주)</p>
        <div style="margin-top: 10px;">
          <el-input-number
            v-model="selectedWeekCount"
            :min="1"
            :max="packageData.maxWeekCount"
            @change="handleWeekCountChange"
          />
        </div>
      </div>

      <!-- 요일 선택 -->
      <div class="home3-3__top__c station">
        <p class="tti">배송 요일 선택 ({{ selectedDays.length }}/{{ packageData.deliveryDaysPerWeek }}일)</p>
        <div class="week-selector" style="display: flex; gap: 10px; margin-top: 10px;">
          <button
            v-for="day in weekDays"
            :key="day.value"
            @click="handleDayToggle(day.value)"
            :class="['day-btn', { active: selectedDays.includes(day.value) }]"
            :disabled="!selectedDays.includes(day.value) && selectedDays.length >= packageData.deliveryDaysPerWeek"
            style="padding: 10px 20px; border: 1px solid #ddd; background: white; cursor: pointer;"
            :style="{
              background: selectedDays.includes(day.value) ? '#409EFF' : 'white',
              color: selectedDays.includes(day.value) ? 'white' : 'black',
              opacity: (!selectedDays.includes(day.value) && selectedDays.length >= packageData.deliveryDaysPerWeek) ? 0.5 : 1
            }"
          >
            {{ day.label }}
          </button>
        </div>
      </div>

      <!-- 주차별 상품 선택 -->
      <template v-for="weekNum in selectedWeekCount">
        <div :key="weekNum" class="home3-3__top__c station">
          <p class="tti">{{ weekNum }}주차 상품 선택</p>
          <p class="tiii" style="font-size: 14px; color: #666; margin-bottom: 10px;">
            {{ weekNum }}주차: {{ getTotalSelectedCount(weekNum) }}개 선택됨
            | 전체: {{ getTotalSelectedCountAllWeeks() }}/{{ packageData.selectableProductCount }}개 선택됨
          </p>

          <!-- 요일별 상품 선택 -->
          <template v-for="dayNum in selectedDays">
            <div :key="`week-${weekNum}-day-${dayNum}`" style="margin-bottom: 20px; border: 1px solid #eee; padding: 15px;">
              <p style="font-weight: bold; margin-bottom: 10px;">{{ getDayLabel(dayNum) }} 배송 상품</p>

              <!-- 상품 선택 드롭다운 -->
              <div style="margin-bottom: 5px; font-size: 12px; color: #999;">
                사용 가능한 상품: {{ packageData.packageItems ? packageData.packageItems.length : 0 }}개
              </div>
              <div v-if="orderData.weeks[weekNum] && orderData.weeks[weekNum].days[dayNum]">
                <el-select
                  v-model="orderData.weeks[weekNum].days[dayNum].selectedProductIdx"
                  placeholder="상품을 선택하세요"
                  @change="handleProductSelect(weekNum, dayNum)"
                  style="width: 100%; margin-bottom: 10px;"
                  :disabled="!packageData.packageItems || packageData.packageItems.length === 0"
                  :popper-append-to-body="false"
                >
                  <el-option
                    v-for="item in packageData.packageItems || []"
                    :key="item.idx"
                    :label="item.productName"
                    :value="item.productIdx"
                    :disabled="!canSelectProduct()"
                  />
                </el-select>

              </div>

              <div v-else style="color: #ff0000; font-size: 12px; margin-bottom: 10px;">
                주문 데이터가 초기화되지 않았습니다.
              </div>

              <div v-if="!packageData.packageItems || packageData.packageItems.length === 0"
                   style="color: #ff6b6b; font-size: 12px; margin-bottom: 10px;">
                선택 가능한 상품이 없습니다. 패키지 설정을 확인해주세요.
              </div>

              <!-- 선택된 상품 목록 -->
              <div v-if="orderData.weeks[weekNum].days[dayNum].products.length > 0" style="margin-top: 10px;">
                <div
                  v-for="(product, index) in orderData.weeks[weekNum].days[dayNum].products"
                  :key="index"
                  style="display: flex; justify-content: space-between; align-items: center; padding: 10px; background: #f5f5f5; margin-bottom: 5px;"
                >
                  <span>{{ product.name }}</span>
                  <div style="display: flex; align-items: center; gap: 10px;">
                    <el-input-number
                      v-model="product.quantity"
                      :min="1"
                      :max="getRemainingCount() + product.quantity"
                      @change="handleQuantityChange(weekNum, dayNum, index)"
                      size="small"
                    />
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeProduct(weekNum, dayNum, index)"
                    >
                      삭제
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </template>

      <!-- 쿠폰/할인/적립금 -->
      <div class="home3-3__top__c" style="margin-bottom:150px">
        <table>
          <caption>쿠폰 / 할인 / 적립금</caption>
          <tbody>
            <tr>
              <th>사용 가능 포인트</th>
              <td>
                <input
                  v-model.number="usePoint"
                  type="number"
                  @change="handleChangePoint"
                  style="width: 150px; padding: 5px;"
                /> 원
                <button @click="handleUseAllPoint" style="margin-left: 10px; padding: 5px 10px;">전액 사용</button>
                <p style="font-size: 12px; color: #666; margin-top: 5px;">보유: {{ userInfo.point | parseKrw }}P</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 결제 하단 고정 -->
      <div class="order__footer-fixed">
        <div class="home3-3__top__b-1">
          <p class="tii">총결제 금액</p>
          <p class="tiii">{{ getTotalAmount() | parseKrw }}원</p>
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
import { getPackageDetail } from '@/api/product';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';
import { getUserInfo } from '@/api/user';
import moment from 'moment';
import request from '@/utils/request';

@Component({
  name: 'PackageOrder',
})

export default class extends Vue {
  private loading = true;

  private packageData: any = {
    packageItems: [],
    maxWeekCount: 1,
    deliveryDaysPerWeek: 1,
    selectableProductCount: 1,
  };

  private selectedStation = JSON.parse((window as any).localStorage.getItem(storageKey.stationPlace));

  private apiUrl = process.env.VUE_APP_COMMON_API;

  private tossPayments = (window as any).TossPayments(`${process.env.VUE_APP_TOSS_KEY}`);

  private packageIdx = this.$route.params.packageIdx;

  private addressDetail = '';

  private selectedWeekCount = 1;

  private selectedDays: number[] = [];

  private weekDays = [
    { label: '월', value: 1 },
    { label: '화', value: 2 },
    { label: '수', value: 3 },
    { label: '목', value: 4 },
    { label: '금', value: 5 },
    { label: '토', value: 6 },
    { label: '일', value: 0 },
  ];

  private orderData: any = {
    weeks: {},
  };

  private usePoint = 0;

  private userInfo: any = {
    point: 0,
  };

  async mounted() {
    if (!UserModule.isLogin) {
      this.$message.info('로그인이 필요한 서비스 입니다.');
      this.$router.push({ name: 'Login' });
      return;
    }
    await this.handlePackageDetail();
    this.getUserInfo();
    // initializeOrderData는 handlePackageDetail 내부에서 호출됨
  }

  handlePackageDetail() {
    this.loading = true;
    return getPackageDetail(Number(this.packageIdx)).then((res) => {
      this.packageData = res.data;

      // packageItems가 없거나 빈 배열인 경우 빈 배열로 초기화
      if (!this.packageData.packageItems) {
        this.packageData.packageItems = [];
        console.warn('packageItems가 없습니다. 빈 배열로 초기화합니다.');
      }

      // 패키지 데이터가 로드된 후에 주문 데이터 초기화
      this.initializeOrderData();

      // DOM 업데이트를 확실히 기다린 후 처리
      this.$nextTick(() => {
        this.$forceUpdate();
        this.loading = false;
      });
    }).catch((error) => {
      console.error('패키지 데이터 로드 실패:', error);
      this.loading = false;
      this.$message.error('패키지 정보를 불러올 수 없습니다.');
    });
  }

  getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
    });
  }

  initializeOrderData() {
    for (let i = 1; i <= this.packageData.maxWeekCount; i += 1) {
      this.$set(this.orderData.weeks, i, {
        days: {},
      });
      for (let d = 0; d <= 6; d += 1) {
        this.$set(this.orderData.weeks[i].days, d, {
          selectedProductIdx: null,
          products: [],
        });
      }
    }
  }

  handleWeekCountChange() {
    // 주차 수 변경 시 추가 처리 필요 시 구현
  }

  handleDayToggle(dayNum: number) {
    const index = this.selectedDays.indexOf(dayNum);
    if (index > -1) {
      // 이미 선택된 요일이면 제거
      this.selectedDays.splice(index, 1);
      // 해당 요일의 모든 주차 데이터 초기화
      for (let weekNum = 1; weekNum <= this.selectedWeekCount; weekNum += 1) {
        this.orderData.weeks[weekNum].days[dayNum].products = [];
        this.orderData.weeks[weekNum].days[dayNum].selectedProductIdx = null;
      }
    } else if (this.selectedDays.length < this.packageData.deliveryDaysPerWeek) {
    // 선택되지 않은 요일이고 아직 최대 선택 수에 도달하지 않았으면 추가
        this.selectedDays.push(dayNum);
        this.selectedDays.sort((a, b) => a - b);
    }
  }

  handleProductSelect(weekNum: number, dayNum: number) {
    const selectedIdx = this.orderData.weeks[weekNum].days[dayNum].selectedProductIdx;
    if (!selectedIdx) return;

    // 이미 추가된 상품인지 확인
    const existingProduct = this.orderData.weeks[weekNum].days[dayNum].products.find(
      (p: any) => p.productIdx === selectedIdx,
    );
    if (existingProduct) {
      this.$message.warning('이미 추가된 상품입니다.');
      return;
    }

    // 남은 선택 가능 수량 확인
    if (!this.canSelectProduct()) {
      this.$message.warning(`최대 선택 가능한 상품 갯수(${this.packageData.selectableProductCount}개)를 초과했습니다.`);
      return;
    }

    // 선택된 상품 정보 찾기
    const productInfo = this.packageData.packageItems.find(
      (item: any) => item.productIdx === selectedIdx,
    );

    if (productInfo) {
      this.orderData.weeks[weekNum].days[dayNum].products.push({
        productIdx: productInfo.productIdx,
        idx: productInfo.idx,
        name: productInfo.productName,
        price: productInfo.productPrice,
        quantity: 1,
      });
      // 선택 초기화
      this.orderData.weeks[weekNum].days[dayNum].selectedProductIdx = null;
    }
  }

  handleQuantityChange(weekNum: number, dayNum: number, index: number) {
    const totalCount = this.getTotalSelectedCountAllWeeks();
    if (totalCount > this.packageData.selectableProductCount) {
      this.$message.warning('최대 선택 가능한 상품 갯수를 초과했습니다.');
      this.orderData.weeks[weekNum].days[dayNum].products[index].quantity -= (totalCount - this.packageData.selectableProductCount);
    }
  }

  removeProduct(weekNum: number, dayNum: number, index: number) {
    this.orderData.weeks[weekNum].days[dayNum].products.splice(index, 1);
  }

  getTotalSelectedCount(weekNum: number): number {
    let total = 0;
    this.selectedDays.forEach((dayNum: number) => {
      const dayData = this.orderData.weeks[weekNum].days[dayNum];
      if (dayData && dayData.products) {
        total += dayData.products.reduce((sum: number, p: any) => sum + p.quantity, 0);
      }
    });
    return total;
  }

  // 전체 주차의 총 선택된 상품 수
  getTotalSelectedCountAllWeeks(): number {
    let total = 0;
    for (let weekNum = 1; weekNum <= this.selectedWeekCount; weekNum += 1) {
      total += this.getTotalSelectedCount(weekNum);
    }
    return total;
  }

  getRemainingCount(): number {
    return this.packageData.selectableProductCount - this.getTotalSelectedCountAllWeeks();
  }

  canSelectProduct(): boolean {
    return this.getTotalSelectedCountAllWeeks() < this.packageData.selectableProductCount;
  }

  getDayLabel(dayNum: number): string {
    const day = this.weekDays.find((d) => d.value === dayNum);
    return day ? day.label : '';
  }

  handleChangePoint() {
    if (this.usePoint % 10 !== 0) {
      this.$message.info('10포인트 단위로 입력해 주세요.');
      this.usePoint -= this.usePoint % 10;
    }
    if (this.usePoint > this.userInfo.point) {
      this.usePoint = this.userInfo.point;
    }
    if (this.usePoint < 0) {
      this.usePoint = 0;
    }
  }

  handleUseAllPoint() {
    const totalAmount = this.getTotalAmount();
    if (totalAmount <= 100) {
      this.$message.info('상품을 선택해주세요');
      this.usePoint = 0;
    } else if (totalAmount < this.userInfo.point) {
      this.$message.info('포인트 사용은 총결제 금액을 초과할 수 없습니다.');
      this.usePoint = totalAmount - 100;
    } else if (totalAmount - this.userInfo.point < 100) {
      this.$message.info('최소 100원 이상은 구매하셔야 합니다.');
      this.usePoint = totalAmount - 100;
    } else {
      this.usePoint = this.userInfo.point;
    }
    this.usePoint -= this.usePoint % 10;
  }

  getTotalAmount(): number {
    let total = 0;

    // 모든 주차의 상품 가격 합산
    for (let weekNum = 1; weekNum <= this.selectedWeekCount; weekNum += 1) {
      // Avoid function-in-loop by extracting logic
      const selectedDays = this.selectedDays;
      for (let i = 0; i < selectedDays.length; i += 1) {
        const dayNum = selectedDays[i];
        const dayData = this.orderData.weeks[weekNum].days[dayNum];
        if (dayData && dayData.products) {
          total += dayData.products.reduce(
            (sum: number, p: any) => sum + (p.price * p.quantity),
            0,
          );
        }
      }
    }

    // 배송비 추가
    total += this.packageData.deliveryFee || 0;

    // 포인트 차감
    total -= this.usePoint;

    return Math.max(total, 0);
  }

  convertPackageToOrderFormat() {
    const week: any = {};

    // 상품 가격 합계 계산 (포인트와 배송비 제외)
    let productTotal = 0;

    // 패키지 주문 데이터를 일반 주문 형식으로 변환
    for (let weekNum = 1; weekNum <= this.selectedWeekCount; weekNum += 1) {
      const startDate = moment().add(weekNum, 'weeks').startOf('isoWeek').format('YYYY-MM-DD');
      const endDate = moment().add(weekNum, 'weeks').startOf('isoWeek').add(4, 'days')
      .format('YYYY-MM-DD');

      week[weekNum] = {
        startDate,
        endDate,
        day: {},
      };

      for (let i = 0; i < this.selectedDays.length; i += 1) {
        const dayNum = this.selectedDays[i];
        week[weekNum].day[dayNum] = {
          products: {},
        };
        const dayData = this.orderData.weeks[weekNum].days[dayNum];
        if (dayData && dayData.products) {
          for (let index = 0; index < dayData.products.length; index += 1) {
            const product = dayData.products[index];
            // 상품 가격 합계에 추가
            productTotal += product.price * product.quantity;

            // 같은 상품이 이미 있으면 수량을 누적
            if (week[weekNum].day[dayNum].products[product.productIdx]) {
              week[weekNum].day[dayNum].products[product.productIdx].count += product.quantity;
            } else {
              week[weekNum].day[dayNum].products[product.productIdx] = {
                product: {
                  idx: product.productIdx,
                  name: product.name,
                  price: product.price,
                  extraProductStatus: false,
                },
                count: product.quantity,
              };
            }
          }
        }
      }
    }

    // 총 금액 계산 (상품 가격 + 배송비)
    const totalAmount = productTotal + (this.packageData.deliveryFee || 0);

    return {
      week,
      amount: totalAmount,
      deliveryFee: this.packageData.deliveryFee || 0,
      usePoint: this.usePoint,
      stationId: this.selectedStation.idx,
      addressDetail: this.addressDetail,
      orderType: 'PACKAGE',
      count: this.getTotalSelectedCountAllWeeks(),
      memo: '',
    };
  }

  handleOrder() {
    // 유효성 검사
    if (!this.addressDetail) {
      this.$message.warning('상세 주소를 입력해주세요.');
      return;
    }

    if (this.selectedDays.length === 0) {
      this.$message.warning('배송 요일을 선택해주세요.');
      return;
    }

    if (this.selectedDays.length !== this.packageData.deliveryDaysPerWeek) {
      this.$message.warning(`${this.packageData.deliveryDaysPerWeek}일을 선택해주세요.`);
      return;
    }

    // 전체 상품 선택 확인
    const totalSelectedCount = this.getTotalSelectedCountAllWeeks();
    if (totalSelectedCount === 0) {
      this.$message.warning('상품을 선택해주세요.');
      return;
    }
    if (totalSelectedCount !== this.packageData.selectableProductCount) {
      this.$message.warning(
        `정확히 ${this.packageData.selectableProductCount}개의 상품을 선택해주세요. (현재: ${totalSelectedCount}개)`,
      );
      return;
    }

    const totalAmount = this.getTotalAmount();
    if (totalAmount < 100) {
      this.$message.warning('최소 결제 금액은 100원입니다.');
      return;
    }

    // 주문 데이터 준비 - 일반 주문 형식으로 변환
    let convertedOrderData;
    try {
      convertedOrderData = this.convertPackageToOrderFormat();
      console.log('Converted order data:', convertedOrderData);
    } catch (error) {
      console.error('Error in convertPackageToOrderFormat:', error);
      this.$message.error('주문 데이터 변환 중 오류가 발생했습니다.');
      return;
    }
    // 결제 진행 전 주문 데이터 저장
    this.loading = true;
    request({
      url: '/product/order',
      method: 'post',
      data: convertedOrderData,
    }).then((res: any) => {
      this.tossPayments.requestPayment('카드', {
        amount: res.data.amount,
        orderId: res.data.key,
        orderName: `${this.packageData.name}`,
        successUrl: `${process.env.VUE_APP_TOSS_SUSSCESS}`,
        failUrl: `${process.env.VUE_APP_TOSS_FAIL}`,
        useAppCardOnly: false,
      }).catch((error: any) => {
        this.loading = false;
        this.$message.info('결제가 취소되었습니다. 다시 시도해 주세요.');
      });
    }).catch((error) => {
      this.loading = false;
      this.$message.error('주문 처리 중 오류가 발생했습니다.');
      console.error('주문 저장 실패:', error);
    });
  }
}
</script>

<style scoped>
.day-btn {
  transition: all 0.3s;
}

.day-btn.active {
  font-weight: bold;
}

.day-btn:disabled {
  cursor: not-allowed;
}
</style>
