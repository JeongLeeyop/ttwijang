<template>
  <div>
    <div class="home4-6-2__wr">
      <div class="home4-6-2__top">
        <router-link to="">식단관리</router-link>
        <p class="ti">비공개 / 1일전</p>
      </div>

      <div class="home4-6-2__tob">
        <!-- <p class="tii">{{ $route.query.post.title}}</p> -->
        <p class="tii">체중감량 질문드립니다!</p>
      </div>

      <div class="home4-6-2__text">
        <p class="ti"><span>체중감량을 하려고 해서 질문드립니다 제가 탄수화물을 평소에 많이먹게되서 단백질 비타민함량을 늘려야할것같습니다 . 어떤걸 섭취해야하나요 </span></p>
        <img src="~@/assets/images/sample.png" class="fa" alt="">
        <!-- <p class="ti"><span>{{ $route.query.post.content }}</span></p> -->
      </div>

      <div class="home4-6-2__mid">
        <button class="mb default" @click="isFavorite = true" v-show="!isFavorite">누르기전{{ like }} <span class="mti">도움이
            되었어요</span></button>
        <button class="mb full" @click="isFavorite = false" v-show="isFavorite">누른후{{ like + 1 }} <span class="mti">도움이
            되었어요</span></button>
      </div>

      <div class="home4-6-2__bot">
        <p class="ti">전문가 답변</p>
        <div class="home4-6-2__box">
          <img src="~@/assets/images/face.png" class="fa" alt="">
          <p class="tii">
            <span class="answer">홍길동 영양사의 답변</span>
            <span class="antail">4시간전</span>
          </p>
        </div>
        <div class="home4-6-2__box2">
          <p class="tiii"><span>체중감량을 그만하고 이제···..</span>
            <span>아몬드 5일만 먹던데요···</span>
          </p>
        </div>
      </div>
      <div class="home4-6-2__tail_2">
        <router-link :to="{ name: 'CounsultForm' }" class="modify">수정</router-link>
        <router-link :to="{ name: 'CounsultConfirm' }" class="delete">삭제</router-link>
        <router-link :to="{ name: 'Counsult' }" class="">목록</router-link>
      </div>
      <div class="home4-6-2__tail">
        <router-link :to="{ name: 'Counsult' }" class="">목록</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      like: 0,
      isFavorite: false,
      Board: '',
    };
  },
  methods: {
    goodgood() {
      this.like += 1;
      // console.log('');
    },
  },
};
</script>
