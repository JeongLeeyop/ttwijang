<template>
    <div class="header">
        <button class="back-button" @click="goBack">
        <i class="el-icon-d-arrow-left"></i>
        </button>
    </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component({
  name: 'AuthHeader',
})
export default class extends Vue {
  private goBack() {
    this.$router.go(-1);
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
