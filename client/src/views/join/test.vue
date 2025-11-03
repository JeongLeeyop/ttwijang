<template>
  <div>
    <h4>상세 페이지</h4>
    <ul>
      <li v-for="(post, index) in Board" :key="post.id">
        <p>{{ Board[index].title }}</p>
      </li>
    </ul>
    <div class="listWrapper">
      <br>
      <div v-for="(el, index) in Board" :key="index" class="list">
        <!-- 페이지 이동 라우터 -->
        ##########################################<br>
        <router-link :to="{path: '/detail/' + el.id, query:{ post: el }}">{{ el.title }}</router-link>
        <br>##########################################
        <p>{{ el.id }}</p>
        <br><br>
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
      Board: Array,
    };
  },
  methods: {
    async boardView() {
      try {
        const result = await axios.get('http://121.146.250.230:8083/board', {
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
  },
};
</script>
