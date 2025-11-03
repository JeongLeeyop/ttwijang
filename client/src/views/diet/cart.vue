<template>
  <div v-loading="loading">
    <template v-for="(week, index) in orderForm.week">
      <div :key="index" class="cart__top__b">
        <p class="tti">[ {{Number(index)}} 주차 ]<span class="tti color2">{{week.startDate}} ~ {{week.endDate}}</span></p>
        <template v-for="(day, index2) in week.day">
          <p class="weeks" :key="index2">[ {{ index2 | filterDay}} ] {{ selectedShop.maxHoldCnt - orderCountForm.week[index].day[index2].orderCount }}개 주문 가능</p>
          <template v-for="(item, index3) in day.products">
            <div :key="index3" class="cart__box">
              <div class="left">
              <p class="ttti">{{item.product.name}}</p>
              <p class="ttti a">{{item.product.price}}원</p>
              </div>
              <div class="right">
                <el-input-number v-model="item.count" :min="1" :max="Number(selectedShop.maxHoldCnt - orderCountForm.week[index].day[index2].orderCount - (orderForm.week[index].day[index2].count - item.count))" @change="handleProduct(item, index, index2, index3)"></el-input-number>
                <p class="cancel" @click="handleCancel(item, index, index2)"/>
              </div>
            </div>
          </template>
        </template>
      </div>
    </template>
    <div class="orderDetail__fixed">
      <div class="total-amount-wr">
        <div class="total-amount">
          <p>총 결제금액</p>
          <p>{{orderForm.amount | parseKrw}} 원</p>
        </div>
      </div>
      <div class="orderDetail">
        <a style="cursor: pointer;" class="diet_next_button" @click="handleOrder()">주문하기</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { OrderModule } from '@/store/modules/order';
// import moment from 'moment';
import { Vue, Component } from 'vue-property-decorator';
import { storageKey } from '@/enums/localStorage';
import { getOrderCount } from '@/api/order';
import { ThisTypedComponentOptionsWithArrayProps } from 'vue/types/options';

@Component({
  name: 'OrderCart',
})

export default class extends Vue {
  mounted() {
    this.getOrderCount();
  }

  private loading = false;

  private orderForm = OrderModule.orderForm;

  private orderCountForm: any = '';

  private selectedShop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));

  private getOrderCount() {
    this.loading = true;
    getOrderCount(this.selectedShop.idx, OrderModule.orderForm).then((res) => {
      this.orderCountForm = res.data;
    });
    this.loading = false;
  }

  private handleOrder() {
      this.$router.push({ name: 'Payment' });
  }

  private handleProduct(item: any, weekNum: number, dayNum: number, productNum: number) {
    const dayStock: number = this.selectedShop.maxHoldCnt - this.orderCountForm.week[weekNum].day[dayNum].orderCount;
    const products: any = this.orderForm.week[weekNum].day[dayNum].products;
    const keys: any = Object.keys(products);
    let sum = 0;
    keys.forEach((key: any) => {
      sum += Number(products[key].count);
    });
    if (sum > dayStock) {
      this.$message.info('구매 가능한 수량이 초과했습니다.');
      item.count = Number(dayStock - (sum - this.orderForm.week[weekNum].day[dayNum].products[productNum].count));
    }
      OrderModule.setProduct({
        ...item,
        weekNum: [weekNum],
        dayNum: [dayNum],
      });
  }

  private handleCancel(item: any, weekNum: number, dayNum: number) {
    OrderModule.cancelProduct({
      ...item,
      weekNum: [weekNum],
      dayNum: [dayNum],
    });
  }
}
</script>
