<template>
  <div>
    <homeHeader />
    <router-view />
    <homeFooter />
  </div>
</template>

<script>
import homeHeader from '@/Layout/components/homeHeader.vue';
import homeFooter from '@/Layout/components/homeFooter.vue';

export default {
  components: {
    homeHeader,
    homeFooter,
  },
};
</script>
