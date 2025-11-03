<template>
  <div v-loading="loading">
    <div class="orderDetail__bot pd-bot">
      <div class="orderDetail__box">
        <p class="ti color2">{{product.name}}</p>
        <!-- <div class="price">9,400원</div> -->
        <div class="img">
          <img :src="`/api/attached-file/${product.thumbUid}`" alt="">
        </div>
        <div class="material">
          <!-- <p class="tti">{{product.description}}</p> -->
          <div class="kcal">{{product.calorie}} kcal</div>
          <div class="orderDetail__count">
            <p class="ttl">수량</p>
            <el-input-number v-model="count" :min="0" :max="Number(stock)+Number(productCnt)"></el-input-number>
            <p class="ttl2">( 최대 {{ Number(stock)+Number(productCnt) }}개 )</p>
          </div>
          <div class="orderDetail__mid">
            <p class="tti">영양정보</p>
            <div class="orderDetail__mid__a">
              <ul>
                <li><p class="th">탄수화물(g)</p><p class="td">{{product.carbohydrate}}</p></li>
                <li><p class="th">단백질(g)</p><p class="td">{{product.protein}}</p></li>
                <li><p class="th">지방(g)</p><p class="td">{{product.fat}}</p></li>
                <li><p class="th">나트륨(mg)</p><p class="td">{{product.sodium}}</p></li>
                <li><p class="th">당(g)</p><p class="td">{{product.sugar}}</p></li>
                <li><p class="th">포화지방산(g)</p><p class="td">{{product.saturatedfat}}</p></li>
                <li><p class="th">트랜스지방(mg)</p><p class="td">{{product.transFat}}</p></li>
                <li><p class="th">콜레스테롤(mg)</p><p class="td">{{product.cholesterol}}</p></li>
                <li><p class="th">칼슘(mg)</p><p class="td">{{product.calcium}}</p></li>
                <li><p class="th">철(mg)</p><p class="td">{{product.iron}}</p></li>
                <li><p class="th">칼륨(mg)</p><p class="td">{{product.potassium}}</p></li>
                <li><p class="th">식이섬유(g)</p><p class="td">{{product.dietaryFiber}}</p></li>
                <li><p class="th">비타민C(mg)</p><p class="td">{{product.vitaminc}}</p></li>
              </ul>
              <p class="caption"><span>출처</span> 와로샐러드</p>
            </div>
          </div>
          <div class="orderDetail__mid">
            <p class="tti">재료</p>
            <div class="orderDetail__mid__a" style="word-break: normal;    text-align: left;">
                {{product.material}}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="orderDetail__fixed">
      <div class="total-amount-wr">
        <div class="total-amount">
          <p>담은 금액</p>
          <p>{{count * product.price}} 원</p>
        </div>
      </div>
      <div class="orderDetail">
        <a style="cursor: pointer;" class="diet_next_button" @click="handleConfirm()">{{count}}개 담기</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getProductDetail } from '@/api/product';
import { OrderModule } from '@/store/modules/order';
import { Vue, Component } from 'vue-property-decorator';
import { storageKey } from '@/enums/localStorage';
import { getOrderCount } from '@/api/order';

@Component({
  name: 'OrderDetail',
})

export default class extends Vue {
  private apiUrl = process.env.VUE_APP_BASE_API;

  private productId = this.$route.params.productId;

  private product: any = {};

  private loading = true;

  private selectedShop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));

  private shopStock = this.$route.params.shopStock;

  private stock = this.$route.params.stock;

  private count = Number(this.$route.params.cnt);

  private productCnt = Number(this.$route.params.cnt);
  // private count = 0;

  mounted() {
    this.getProductDetail();
    this.getOrderCount();
    console.log(OrderModule.orderForm);
  }

  private getOrderCount() {
    getOrderCount(this.selectedShop.idx, OrderModule.orderForm).then((res) => {
      console.log(res);
    });
  }

  private getProductDetail() {
    this.loading = true;
    getProductDetail(Number(this.productId)).then((res) => {
      this.loading = false;
      this.product = res.data;
    });
  }

  private handleConfirm() {
    if (Number(this.count) < 1) {
      this.$message.info('수량을 한개이상 선택해주세요.');
    } else if (Number(this.shopStock) < Number(this.count)) {
      this.$message.info('최대 구매 수량보다 많이 선택하실 수 없습니다.');
    // if (this.count > this.selectedShop.stock || this.count + OrderModule.orderForm.count > this.selectedShop.stock) {
      // this.$message.info(`최대 구매 수량보다 많이 선택하실 수 없습니다. 최대 구매 수량 : ${this.selectedShop.stock}개`);
    } else {
      if (Number(this.count) > 0) {
        OrderModule.addProduct({
          product: this.product,
          count: this.count,
        });
      }
      this.$router.push({
        name: 'Order',
      });
    }
  }
}
</script>
