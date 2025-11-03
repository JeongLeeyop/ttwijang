<template>
  <div>
    <div class="home4-6-1__top">
      <button @click="selectTag" :to="{ name: '' }">전체</button>
      <button @click="selectTag" :to="{ name: '' }">식단관리</button>
      <button @click="selectTag" :to="{ name: '' }">영양관리</button>
      <button @click="selectTag" :to="{ name: '' }">다이어트</button>
      <button @click="selectTag" :to="{ name: '' }">식습관</button>
    </div>

    <div class="home4-6-1__wr">
      <p class="ti">인기글</p>
      <div class="home4-6-1__mid">
        <p class="b">
          <span class="tti b">체중감량을 그만하고 이제···..</span>
          <span class="tti">2022-04-30</span>
        </p>
        <p class="b">
          <span class="tti b">아몬드 5일만 먹던데요···</span>
          <span class="tti">2022-04-30</span>
        </p>
        <p class="a">
          <span class="tti">식사량 상관없이 늦게 먹으면···</span>
          <span class="tti">2022-04-30</span>
        </p>
      </div>
      <p class="ti">게시글</p>
      <div v-for="(el, index) in Board" :key="index" class="home4-6-1__bot a">
        <p class="boxa">
          <span class="boti">식단관리</span><span class="boti a">전문가 답변</span>
        </p>
        <p class="boxb">
          <router-link :to="{ path: '/CounsultView/' + el.id, query: { post: el } }"><span class="botiii">{{
            Board[index].title
          }}</span></router-link>
        </p>
        <p class="boxc">
          <span class="botiv">34</span><span class="botv">1일전</span>
        </p>
      </div>

      <div class="home4-6-1__bot a">
        <p class="boxa">
          <span class="boti">식단관리</span>
        </p>
        <p class="boxb">
          <router-link :to="{ name: 'CounsultView' }"><span class="botiii">체중감량 질문드립니다!!</span></router-link>
        </p>
        <p class="boxc">
          <span class="botiv a">1,334</span><span class="botv">1일전</span>
        </p>
      </div>

      <div class="home4-6-1__bot b">
        <p class="boxa">
          <span class="boti">식단관리</span>
        </p>
        <p class="boxb">
          <router-link :to="{ name: 'CounsultView' }"><span class="botiii">[질문] 다이어트중 체중감량에 대해서 질문드립니다. 답변 부탁드리겠습니다.</span></router-link>
        </p>
        <p class="boxc">
          <span class="botiv a">1,334</span><span class="botv">1일전</span>
        </p>
      </div>

      <div class="home4-6-1__bot b">
        <p class="boxa">
          <span class="boti">식단관리</span><span class="boti a">전문가 답변</span>
        </p>
        <p class="boxb">
          <router-link :to="{ name: 'CounsultView' }"><span class="botiii">체중감량에 도움이되는 식사에 관하여 질문드립니다.</span></router-link>
        </p>
        <p class="boxc">
          <span class="botiv a">1,334</span><span class="botv">1일전</span>
        </p>
      </div>

      <div class="home4-6-1__bot c">
        <p class="boxa">
          <span class="boti">식단관리</span><span class="boti a">전문가 답변</span>
        </p>
        <p class="boxb">
          <router-link :to="{ name: 'CounsultView' }"><span class="botiii">체중감소원인에 대한 질문드립니다</span></router-link>
        </p>
        <p class="boxc">
          <span class="botiv a">1,334</span><span class="botv">1일전</span>
        </p>
      </div>

      <div class="pagination-block">
        <el-pagination layout="prev, pager, next" :total="50" />
      </div>

      <div class="home4-6-1__tail">
        <router-link :to="{ name: 'CounsultForm' }">상담내용 작성하기</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  mounted() {
    this.boardView();
  },
  data() {
    return {
      Board: '',
    };
  },
  methods: {
    async boardView() {
      try {
        const result = await axios.get('http://121.146.250.230:8084/api/client/board', {
          params: {
            boardId: '4',
          },
        });
        // console.log(result.data.content);
        this.Board = result.data.content;
      } catch (err) {
        this.loginError = true;
        throw new Error(err);
      }
    },
    async selectTag() {
      alert(1);
      try {
        const result = await axios.get('http://121.146.250.230:8084/api/client/board', {
          params: {
            boardId: '4',
            tadId: '3',
          },
        });
        // console.log(result.data.content);
        this.Board = result.data.content;
      } catch (err) {
        this.loginError = true;
        throw new Error(err);
      }
    },
  },
};
</script>
