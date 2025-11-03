<template>
  <div>
    <div class="home4-1-3__a">
      <div class="home4-1-3__top">
        <p class="ab">작성 가능한 후기가 존재하지 않습니다.</p>
      </div>

      <div class="home4-1-3__mid">
        <a  @click="goBack()" class="link__a">확인</a >
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
