<template>
  <div class="main" v-loading="loading">
    <div id="logo">
      <router-link to="/">
        <img src="~@/assets/images/logo.png" alt="뛰장">
      </router-link>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';

@Component({
  components: {
  },
})

export default class extends Vue {
  mounted() {
    this.$message.warning('모바일만 이용이 가능합니다.');
  }

  private loading = false;
}
</script>
