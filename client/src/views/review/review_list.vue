<template>
  <div>
    <div class="home4-1-1__wr">
      <div class="home4-1-1__box">
        <p class="ti">2022.04.04(목)<span class="btn color2">식단완료</span></p>
        <div class="home4-1-1__box2">
          <p class="tti">샐러드 / 체중감량 / 8주플랜</p>
          <router-link :to="{ name: 'Step3' }" class=""><img src="~@/assets/images/link-img.png" class="go" alt="이동">
          </router-link>
        </div>
        <p class="ttti">78,000원</p>
        <div class="home4-1-1__bot">
          <router-link :to="{ name: 'ReviewForm' }" class="">후기작성</router-link>
        </div>
      </div>

      <div class="home4-1-1__box">
        <p class="ti">2022.04.04(목)<span class="btn color2">식단완료</span></p>
        <div class="home4-1-1__box2">
          <p class="tti">샐러드 / 체중감량 / 4주플랜</p>
          <router-link :to="{ name: 'Step3' }" class=""><img src="~@/assets/images/link-img.png" class="go" alt="이동">
          </router-link>
        </div>
        <p class="ttti">78,000원</p>
        <div class="home4-1-1__bot">
          <router-link :to="{ name: 'ReviewForm' }" class="">후기작성</router-link>
        </div>
      </div>
    </div>

  <router-link :to="{ name: 'ReviewListEmpty' }" class="">최근 구매완료 목록이 없는경우..</router-link>

  </div>
</template>

<script>
</script>
