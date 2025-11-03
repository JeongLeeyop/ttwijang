<template>
  <div>
    <div class="home4-10-1__wr">
      <div class="diary2-2-4__mid">
        <div class="select-box__month">
          <el-select v-model="select" placeholder="기간선택" :popper-append-to-body="false">
            <el-option label="1개월" value="1" />
            <el-option label="3개월" value="2" />
          </el-select>
        </div>
      </div>

      <div class="home4-10-1__mid">
        <div class="home4-10-1__box" v-for="(post, index) in Board" :key="post.uid">
          <p class="home4101__ti">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">{{ Board[index].title }}</p>
          <p class="home4101__day">2022.01.01</p>
        </div>

        <div @click="handleDetail" class="home4-10-1__box">
          <p class="home4101__ti color2">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">원하는 날짜에 배송이 가능할까요?</p>
          <p class="home4101__day">2022.01.01</p>
        </div>

        <div @click="handleDetail" class="home4-10-1__box">
          <p class="home4101__ti color2">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">원하는 날짜에 배송이 가능할까요?</p>
          <p class="home4101__day">2022.01.01</p>
        </div>

        <div @click="handleDetail" class="home4-10-1__box">
          <p class="home4101__ti color2">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">원하는 날짜에 배송이 가능할까요?</p>
          <p class="home4101__day">2022.01.01</p>
        </div>

        <div @click="handleDetail" class="home4-10-1__box">
          <p class="home4101__ti color2">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">원하는 날짜에 배송이 가능할까요?</p>
          <p class="home4101__day">2022.01.01</p>
        </div>

        <div class="pagination-block">
          <el-pagination layout="prev, pager, next" :total="50" />
      </div>

        <div class="home4-10-1__box2">
          <router-link to="" class="">
            <img src="~@/assets/images/call-inq.png" alt="웨일리잇">
          </router-link>
          <router-link to="" class="">
            <img src="~@/assets/images/kakao-inq.png" alt="웨일리잇">
          </router-link>
        </div>
      </div>

      <div class="home4-10-1__bot">
        <router-link :to="{ name: 'QnaForm' }" class="">문의하기</router-link>
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
      select: '',
    };
  },
  methods: {
    async boardView() {
      try {
        const result = await axios.get('http://121.146.250.230:8084/board/', {
          params: {
            boardId: '1',
          },
        });
        // console.log(result.data.content);
        this.Board = result.data.content;
      } catch (err) {
        this.loginError = true;
        throw new Error(err);
      }
    },
    handleDetail() {
      this.$router.push({ name: 'QnaView' });
    },
  },
};
</script>
