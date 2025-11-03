<template>
  <div class="notice">
    <div class="home4-9-1__top">
      <div class="home4-9-1__box"  v-for="(post, index) in Board" :key="post.uid">
        <p class="home491__text">{{ Board[index].title }}</p>
        <p class="home491__day">{{ Board[index].createdDate }}</p>
      </div>

      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test</p>
        <p class="home491__day">2023-03-10</p>
      </div>
      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test2</p>
        <p class="home491__day">2023-03-10</p>
      </div>
      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test3</p>
        <p class="home491__day">2023-03-10</p>
      </div>
      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test4</p>
        <p class="home491__day">2023-03-10</p>
      </div>
      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test4</p>
        <p class="home491__day">2023-03-10</p>
      </div>
      <div @click="handleDetail" class="home4-9-1__box">
        <p class="home491__text">test4</p>
        <p class="home491__day">2023-03-10</p>
      </div>

      <div class="pagination-block">
          <el-pagination layout="prev, pager, next" :total="50" />
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
        const result = await axios.get('http://121.146.250.230:8084/board', {
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
      this.$router.push({ name: 'NoticeView' });
    },
  },
};
</script>
