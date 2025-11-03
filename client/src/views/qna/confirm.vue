<template>
  <div>
    <div class="home4-1-3__a">
      <div class="home4-1-3__top">
        <p class="ab">정말로 삭제하시겠습니까?</p>
      </div>

      <div class="home4-1-3__mid">
        <router-link :to="{ name: 'QnaComplete' }" class="link__a">네</router-link>
        <a  @click="goBack()" class="link__b">아니요</a >
      </div>
    </div>
  </div>
</template>

<script>

export default {
  components: {
  },
  data() {
    return {
      value1: null,
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
