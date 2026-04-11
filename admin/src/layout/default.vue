<template>
  <div>
    <AsideBar />
    <router-view class="content-wrap" />
  </div>
</template>

<script lang="ts">
import AsideBar from './components/aside-bar.vue';

export default {
  components: {
    AsideBar,
  },
};
</script>

<style scoped>
</style>
