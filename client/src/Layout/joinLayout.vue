<template>
  <div>
    <joinHeader />
    <router-view class="join-wrap" />
  </div>
</template>

<script>
import joinHeader from '@/Layout/components/joinHeader.vue';

export default {
  components: {
    joinHeader,
  },
};
</script>
