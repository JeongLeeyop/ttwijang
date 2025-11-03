<template>
  <div>
    <div class="home3-4__wr">
      <div class="home3-4__top">
        <p class="ti">비밀번호를 입력해주세요</p>
        <img src="~@/assets/images/password.png" class="" alt="password">
      </div>

      <div class="home3-4__bot">
        <router-link :to="{ name: 'PayComplete' }">
          <img src="~@/assets/images/password-bg.png" class="pass_img" alt="password">
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
</script>
