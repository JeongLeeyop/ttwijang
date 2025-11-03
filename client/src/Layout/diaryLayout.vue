<template>
  <div>
    <diaryHeader />
    <router-view class="diary-content_wr" />
    <diaryFooter />
  </div>
</template>

<script>
import diaryHeader from '@/Layout/components/diaryHeader.vue';
import diaryFooter from '@/Layout/components/diaryFooter.vue';

export default {
  components: {
    diaryHeader,
    diaryFooter,
  },
};
</script>
