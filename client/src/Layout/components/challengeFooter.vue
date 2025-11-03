<template>
  <div class="board-footer">
    <button class="footer_link_a" @click="handleGoDiary()">
      <img src="~@/assets/images/footer-diary.png" class="footer_img" alt="">
      다이어리
    </button>
    <router-link :to="{ name: 'Home'}" class="footer_home">
      <img src="~@/assets/images/logo-bg.png" class="logo_img" alt="뛰장">
    </router-link>
    <button class="footer_link_b" @click="handleOrder()">
      <img src="~@/assets/images/footer-plan.png" class="footer_img" alt="뛰장">식단 주문
    </button>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { storageKey } from '@/enums/localStorage';

@Component({
})

export default class extends Vue {
  private handleOrder() {
    if (window.localStorage.getItem(storageKey.pickUpPlace) == null) {
      this.$router.push({ name: 'Map' });
    } else {
      this.$router.push({ name: 'Order' });
    }
  }

  private handleGoDiary() {
    this.$router.push({ name: 'Diary' });
  }
}
</script>
