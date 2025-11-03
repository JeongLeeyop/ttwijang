<template>
  <div class="home4__wr" v-loading="loading">
    <div class="home4-1-1__wr">
      <slot v-if="list.length > 0">
        <template v-for="(item, index) in list">
          <div :key="index" class="home4-1-1__box">
            <p class="ti">{{item.createDate | parseDate}}
          <span v-if="item.orderType === 'PICKUP'" class="btn color4">픽업상품</span>
          <span v-else-if="item.orderType === 'STATION'" class="btn color5">배달상품</span>
          <slot v-if="item.orderType === 'PICKUP'">
            <span v-if="item.orderStatus === -1" class="btn color0">전체취소</span>
            <span v-else-if="item.orderStatus === 0" class="btn color1">주문접수</span>
            <span v-else-if="item.orderStatus === 1" class="btn color3">서비스제공중</span>
            <span v-else-if="item.orderStatus === 2" class="btn color2">픽업완료</span>
          </slot>
          <slot v-else-if="item.orderType === 'STATION'">
            <span v-if="item.orderStatus === -1" class="btn color0">전체취소</span>
            <span v-else-if="item.orderStatus === 0" class="btn color1">주문접수</span>
            <span v-else-if="item.orderStatus === 1" class="btn color3">서비스제공중</span>
            <span v-else-if="item.orderStatus === 2" class="btn color2">배달완료</span>
          </slot>
            <!-- <span v-if="item.orderStatus === 2" class="btn">배달완료</span> -->
          </p>
          <div class="home4-1-1__box2">
            <p class="tti">{{ item.startDate }} ~ {{ item.endDate }} [ {{ item.weekNum }} 주 ]</p>
            <router-link :to="{ name: 'DeliveryDetail', params: { orderGroupId: item.idx, orderStatus: item.orderStatus },  }" class=""><img src="~@/assets/images/link-img.png" class="go" alt="이동">
            </router-link>
          </div>
          <p class="ttti">{{item.amount | parseKrw}}원</p>
        </div>
      </template>
    </slot>
    <slot v-else>
      <div>조회된 내역이 없습니다.</div>
      </slot>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { getOrderGroupList } from '@/api/orderGroup';

@Component({
  name: 'Delivery',
})

export default class extends Vue {
  private loading = true;

  private query = {
    page: 1,
    limit: 10,
  }

  private list = [];

  mounted() {
    this.getOrderGroup();
  }

  private getOrderGroup() {
    getOrderGroupList(this.query).then((res) => {
      this.list = res.data.content;
      this.loading = false;
    });
  }
}
</script>
