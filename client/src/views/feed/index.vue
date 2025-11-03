<template>
  <div>
    <div class="home4-6-4__wr">
      <div class="home4-6-4__top">
        <el-input v-model="search" placeholder="검색어를 입력하세요" />
        <button>#5대영양소</button>
        <button>#다이어트</button>
        <button>#식단조절</button>
      </div>

      <div class="home4-6-3__top a">
        <el-radio-group class="myradiogroup" v-model="a">
          <el-radio-button label="영양정보"></el-radio-button>
          <el-radio-button label="식재료"></el-radio-button>
          <el-radio-button label="레시피"></el-radio-button>
          <el-radio-button label="다이어트"></el-radio-button>
        </el-radio-group>
      </div>

      <div class="home4-6-4__mid" v-for="(post, index) in Board" :key="post.uid">
        <VueSlickCarousel v-bind="settings">
          <div><img src="~@/assets/images/img464-1.png" class="img464-1" alt=""></div>
          <div>123</div>
          <div>456</div>
          <div>789</div>
        </VueSlickCarousel>
        <p class="p4-6-4 a">{{ Board[index].title }}</p>
      </div>

      <div class="home4-6-4__mid">
        <VueSlickCarousel v-bind="settings">
          <div><img src="~@/assets/images/img464-2.png" class="img464-2" alt=""></div>
          <div>123</div>
          <div>456</div>
          <div>789</div>
        </VueSlickCarousel>
        <p class="p4-6-4 a black">출산 후<span class="p4-6-4__a">#다이어트</span>로 감량 성공!</p>
        <p class="p4-6-4">리즈시절 요정 몸매 되찾았어요.</p>
        <p class="p4-6-4 b">세끼 다 먹는 건강한 <span class="p4-6-4__a">#다이어트</span>로 완성한 15kg 감량!</p>
      </div>

      <div class="home4-6-4__mid tail">
        <VueSlickCarousel v-bind="settings">
          <div><img src="~@/assets/images/img464-3.png" class="img464-3" alt=""></div>
          <div>123</div>
          <div>456</div>
          <div>789</div>
        </VueSlickCarousel>
        <p class="p4-6-4 a">출산 후<span class="p4-6-4__a">#다이어트</span>로 감량 성공!</p>
        <p class="p4-6-4">리즈시절 요정 몸매 되찾았어요.</p>
        <p class="p4-6-4 b">세끼 다 먹는 건강한 <span class="p4-6-4__a">#다이어트</span>로 완성한 15kg 감량!</p>
      </div>
    </div>
  </div>
</template>

<script>
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import axios from 'axios';

export default {
  components: {
    VueSlickCarousel,
  },
  mounted() {
    this.boardView();
  },
  data() {
    return {
      a: 'a',
      b: 'b',
      settings: {
        lazyLoad: 'ondemand',
        dots: true,
        fade: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
      },
      Board: '',
    };
  },
  methods: {
    async boardView() {
      try {
        const result = await axios.get('http://121.146.250.230:8083/board/5', {
        });
        // console.log(result.data);
        this.Board = result.data;
      } catch (err) {
        this.loginError = true;
        throw new Error(err);
      }
    },
  },
};
</script>
