<template>
  <div>
    <div class="home4-1-3__a">
      <div class="home4-1-3__top">
        <p class="ab">정말로 계정을 삭제하시겠습니까?</p>
      </div>
      <div class="home4-1-4__mid">
        <p class="ab">회원탈퇴 후 사용하시던 포인트 및 쿠폰 혜택은 복원할 수 없습니다.</p>
        <p class="ab">작성된 게시글 등의 데이터는 삭제되지 않으니 반드시 탈퇴 전 직접 삭제해주세요.</p>
      </div>

      <div class="home4-1-3__mid">
        <router-link :to="{ name: 'WithdrawalComplete' }" class="link__a">네</router-link>
        <router-link :to="{ name: 'Mypage' }" class="link__b">아니요</router-link>
      </div>
    </div>
  </div>
</template>

<script>
</script>
