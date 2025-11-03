<template>
  <div>
    <mapHeader />
    <router-view />
    <mapFooter />
  </div>
</template>

<script>
import mapHeader from '@/Layout/components/mapHeader.vue';
import mapFooter from '@/Layout/components/mapFooter.vue';

export default {
  components: {
    mapHeader,
    mapFooter,
  },
};
</script>
