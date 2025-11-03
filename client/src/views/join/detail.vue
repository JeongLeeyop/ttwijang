<template>
  <div>
  <div class="detailWrapper">
    <div class="Detail">
      <h4 class="menuTitle">상세 페이지</h4>
      <!-- <h5>{{ Board[$route.params.id].title }}</h5> -->
      <!-- <p>{{ Board[$route.params.id].content }}</p> -->
      <p>{{ $route.query.post }}</p>
    </div>
  </div>
  <router-view></router-view>
  </div>
</template>

<script>
// import axios from 'axios';

export default {
  mounted() {
    // this.boardView();
  },
  data() {
    return {
      account: JSON.parse(localStorage.getItem('account')),
      postId: 12,
      Board: '',
    };
  },
  Props() {

  },
  methods: {
    /* async boardView() {
      try {
        const result = await axios.get(`http://121.146.250.230:8083/board/post/${this.postId}/account/${this.account.id}`);
        console.log(result.data);
        this.Board = result.data;
        console.log(this.Board);
      } catch (err) {
        this.loginError = true;
        throw new Error(err);
      }
    }, */
  },
};
</script>

<style>
.detailWrapper {
  margin-top: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;

  width: 100%;
}

.detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 600px;
  width: 100%;
}

.menuTitle {
  margin-bottom: 50px;
}
</style>
