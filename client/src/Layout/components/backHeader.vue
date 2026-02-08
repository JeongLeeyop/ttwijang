<template>
    <div class="header">
        <button class="back-button" @click="goBack">
        <i class="el-icon-d-arrow-left"></i>
        </button>
        <div class="header-center">
          <span class="header-title">{{ headerTitle }}</span>
        </div>
    </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component({
  name: 'AuthHeader',
})
export default class extends Vue {
  private get headerTitle(): string {
    if (this.$route.name === 'EmailLogin') {
      return '로그인';
    }

    const metaTitle = this.$route.meta?.title;
    return metaTitle || '';
  }

  private goBack() {
    this.$router.go(-1);
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
