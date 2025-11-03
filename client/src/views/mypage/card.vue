<template>
  <div>
    <div class="home4-4__top">
      <div class="home4-5__top">
        <p class="ti">카드 등록하기</p>
        <img src="~@/assets/images/card-bg.png" class="card" alt="결제카드">
      </div>
      <p class="ti">카드번호</p>
      <el-input placeholder="0000 - 0000 - 0000 - 0000" type="text" id="" v-model="card_num" />
      <p class="ti">유효기간</p>
      <el-input placeholder="mm-yy" type="text" id="" v-model="due_date" />
      <p class="ti">CVC</p>
      <el-input placeholder="***" type="password" id="" v-model="cvc" />
    </div>

    <div class="home4-4__tail">
      <router-link :to="{ name: 'Mypage' }">저장</router-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      card_num: '',
      due_date: '',
      cvc: '',
    };
  },
};
</script>
