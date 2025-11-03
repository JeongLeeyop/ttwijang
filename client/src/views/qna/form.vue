<template>
  <div>
    <div class="home4-10-1__wr">
      <div class="diary2-2-4__mid">
        <div class="select-box__month">
          <el-select :popper-append-to-body="false" placeholder="카테고리를 선택해주세요." v-model="qnaForm.category_id">
            <el-option value="1" label="픽업문의" />
            <el-option value="2" label="서비스문의" />
            <el-option value="3" label="쿠폰/포인트문의" />
            <el-option value="4" label="환불문의" />
          </el-select>
        </div>
      </div>
      <div class="home4-10-2__mid">
        <el-input v-model="qnaForm.title" placeholder="제목을 입력해주세요."></el-input>
        <el-input type="textarea" placeholder="상세 사유를 입력해주세요" maxlength="600" show-word-limit
          v-model="qnaForm.content" />
        <div class="home4-10-2__box">
          <ul>
            <li><img src="~@/assets/images/qna.png" alt="" class="qna"></li>
            <li><img src="~@/assets/images/qna-img.png" alt="" class="qna"></li>
            <li><img src="~@/assets/images/qna-img.png" alt="" class="qna"></li>
          </ul>
        </div>
      </div>
      <div class="home4-10-1__bot">
        <router-link :to="{ name: 'QnaComplete' }" class="">문의하기</router-link>
        <!-- <button @click="submitForm()">작성하기</button> -->
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      qnaForm: {
        boardId: '5',
        category_id: '',
        content: '',
        title: '',
        secretYn: '',
        tag: '',
        accountId: '',
      },
      Account: '',
      // Account: JSON.parse(localStorage.getItem('account')),
    };
  },
  /* mounted() {
    console.log(JSON.parse(localStorage.getItem('account')).name);
  }, */
  methods: {
    async submitForm() {
      // console.log(this.qnaForm.category_id);
      // console.log(this.qnaForm.title);
      // console.log(this.qnaForm.content);
      // console.log(this.Account.name);
      try {
        const result = await axios.post('http://localhost:8080/board', null, {
          params: {
            boardId: '5',
            categoryId: this.qnaForm.category_id,
            title: this.qnaForm.title,
            secretYn: 'Y',
            // accountId: JSON.parse(localStorage.getItem('account')).id,
            accountId: '1',
            content: this.qnaForm.content,
            tag: 'null',
          },
        });
        if (result.status === 200) {
          alert('작성 완료');
          this.$router.push({ path: 'Qna' });
          // localStorage.removeItem("emailch");
        } else {
          alert('작성 실패');
        }
      } catch (err) {
        alert(1);
        throw new Error(err);
      }
    },
  },
};
</script>
