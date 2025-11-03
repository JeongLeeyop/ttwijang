<template>
  <div class="home-header">
    <button @click="handleClickBefore()" class="home-header__arrow"><img src="~@/assets/images/arrow-ico.png" alt="뒤로가기"></button>
    {{ $route.meta.title || 'WEILYEAT' }}
  </div>
</template>

<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';
import { getAlarmCount, getAlarmList } from '@/api/pushAlarm';

@Component({
  name: 'AppHeader',
})
export default class extends Vue {
    private handleClickBefore() {
    if (this.$route.name === 'Agree') {
      this.$router.push({ name: 'Login' });
    } else if (this.$route.name === 'Signup') {
      this.$router.push({ name: 'Agree' });
    } else if (this.$route.name === 'Info') {
      this.$router.push({ name: 'Signup' });
    } else {
      this.$router.go(-1);
    }
  }
}
</script>
