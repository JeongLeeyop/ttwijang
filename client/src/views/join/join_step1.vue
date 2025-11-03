<template>
  <div>
    <div class="join-wr">
      <div class="join_step1-top">
        <div class="step1-top__box">
          <p class="tl">본인인증을<br>해주세요!</p>
          <p class="ttl">안전한 맞춤 식단 배송 서비스의<br>이용을 위해 최초 1회<br>본인 인증이 필요합니다.</p>
          <p class="tail__text">* 본인 인증 시 수집한 개인정보는 서비스 이외<br>다른 용도로 사용하지 않습니다.</p>
        </div>
        <div class="step1-bot__box">
          <router-link :to="{ name: 'Agree'}" class="asign">인증하기</router-link>
          <!-- <router-link :to="{ name: 'Agree'}" class="asign">인증하기</router-link> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {
  },
};
</script>
