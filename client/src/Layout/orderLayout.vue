<template>
  <div>
    <orderHeader />
    <router-view class="join-wrap" />
  </div>
</template>

<script>
import orderHeader from '@/Layout/components/orderHeader.vue';

export default {
  components: {
    orderHeader,
  },
};
</script>
