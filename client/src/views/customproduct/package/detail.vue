<template>
  <div v-loading="loading">
    <div class="custom__detail">
      <div class="custom__detail-ttl sft">[패키지] {{ packageData.name }}</div>
      <div class="custom__detail-banner">
        <img v-if="packageData.thumbUid" :src="`${apiUrl}/attached-file/${packageData.thumbUid}`" class="img464-1" alt="">
        <img v-else src="~@/assets/images/tmp_custom_detail_banner.jpg" alt="" />
      </div>
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
              <th>선택 가능한 상품 갯수</th>
              <td>{{ packageData.selectableProductCount }}개</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td v-if="packageData.deliveryFee === 0">무료</td>
              <td v-else>{{ packageData.deliveryFee | parseKrw}}원</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="home3-3__top__c station">
        <caption>포함된 상품 목록</caption>
        <div class="custom__productlist-list" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px;">
          <div v-for="item in packageData.packageItems" :key="item.idx" class="custom__productlist-item" style="pointer-events: none;">
            <div class="custom__productlist-item__img">
              <img v-if="item.productThumbUid" :src="`${apiUrl}/attached-file/${item.productThumbUid}`" class="img464-1" alt="">
              <img v-else src="~@/assets/images/tmp_custom_productlist_img.svg" alt="">
            </div>
            <div class="custom__productlist-item__ttl">
              {{ item.productName }}
            </div>
            <div class="custom__productlist-item__cost">
              {{ item.productPrice | parseKrw }}원
            </div>
          </div>
        </div>
      </div>

      <div class="home3-3__top__c station" style="margin-bottom:80px;">
        <caption>상품 소개</caption>
        <div class="product__description note-editor" v-html="packageData.description"></div>
      </div>

      <div class="custom__detail-btn__list" style="position: fixed;">
        <router-link :to="{ name: 'CustomPackageOrder', params: { packageIdx: packageData.idx } }" class="custom__detail-btn__item btn02 sft">구매하기</router-link>
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
</style>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getPackageDetail } from '@/api/product';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';

@Component({
  name: 'PackageDetail',
})

export default class extends Vue {
  private loading = true;

  private packageData: any = {
    packageItems: [],
  };

  private apiUrl = process.env.VUE_APP_COMMON_API;

  private packageIdx = this.$route.params.packageIdx;

  mounted() {
    if (!UserModule.isLogin) {
      this.$message.info('로그인이 필요한 서비스 입니다.');
      this.$router.push({ name: 'Login' });
    }
    this.handlePackageDetail();
  }

  handlePackageDetail() {
    this.loading = true;
    getPackageDetail(Number(this.packageIdx)).then((res) => {
      this.packageData = res.data;
      this.loading = false;
    });
  }
}
</script>
