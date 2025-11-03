<template>
  <div>
    <div class="home4-4__top">
      <p class="ti">받는사람</p>
      <el-input placeholder="홍길동" type="text" v-model="name" />
      <p class="ti">연락처</p>
      <el-input placeholder="010-0000-0000" type="text" v-model="tel" />
      <p class="ti">받는주소</p>
      <el-input placeholder="경남 진주시 동부로 169번길" type="text" v-model="address" />
      <el-input placeholder="윙스타워 0000" type="text" v-model="detail_address" />
      <router-link to="">주소 검색</router-link>
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
      name: '',
      tel: '',
      address: '',
      detail_address: '',
    };
  },
};
</script>
