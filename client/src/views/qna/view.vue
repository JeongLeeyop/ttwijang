<template>
  <div>
    <div class="home4-10-1__wr">
      <div class="home4-10-3__top">
        <div class="home4-10-3__box">
          <p class="home4103__ti">배송날짜 문의</p>
          <p class="home4103__title">원하는 날짜에 배송이 가능할까요?</p>
          <p class="home4103__content">여러 개 주문하고 몇 가지 지연 문자 받았는데 나머지 상품은 먼저 보내주시나요?</p>
          <img src="~@/assets/images/sample2.png" class="fa" alt="">
          <p class="home4101__day">2022.01.01</p>
        </div>
      </div>

      <div class="home4-10-3__mid">
        <div class="home4-10-3__tibox">
          <p class="ti">작성해주신 문의에<br>답변이 도착하였습니다.</p>
        </div>
        <p class="home4103main">안녕하세요. 뛰장 입니다/</p>
        <p class="home4103main">
          현재 문의주신 배송내용은 다른날로 변경 합니다.<br>
          고객센터로 전화 문의 주시면 상세하게 안내 및<br>
          변경 도와드리겠습니다.
        </p>
        <p class="home4101__day a">2022.01.01</p>
      </div>

      <div class="home4-6-2__tail_2">
        <router-link :to="{ name: 'QnaForm' }" class="modify">수정</router-link>
        <router-link :to="{ name: 'QnaConfirm' }" class="delete">삭제</router-link>
        <router-link :to="{ name: 'Qna' }" class="">목록</router-link>
      </div>

      <div class="home4-6-2__tail">
        <router-link :to="{ name: 'Qna' }" class="">목록</router-link>
      </div>

    </div>
  </div>
</template>

<script>
</script>
