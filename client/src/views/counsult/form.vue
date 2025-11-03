<template>
  <div>
    <div class="home4-6-3__wr">
      <div class="home4-6-3__top">
        <p class="ti">
          질문 분야를 선택하세요. <span class="tia">*</span>
        </p>
        <el-radio-group class="myradiogroup" v-model="writeForm.category_id">
          <el-radio-button label="5" value="5">식단관리</el-radio-button>
          <el-radio-button label="6" value="6">영양관리</el-radio-button>
          <el-radio-button label="4" value="4">다이어트</el-radio-button>
          <el-radio-button label="7" value="7">식습관</el-radio-button>
        </el-radio-group>
      </div>

      <div class="home4-6-3__mid">
        <div class="home4-6-3__mid__box">
          <p class="ti">무엇이 궁금하세요?</p>
          <el-checkbox class="tii" v-model="writeForm.secret_yn" true-label="N" false-label="Y">비공개</el-checkbox>
        </div>

        <el-input placeholder="제목을 입력해주세요." type="text" id="" class="tibox" v-model="writeForm.title" />
        <el-input placeholder="궁금한 내용을 상세히 적어주세요." type="text" id="" class="tibox2" v-model="writeForm.content" />

        <!-- <p class="ti">태그를 입력해주세요.</p>
        <el-input placeholder="#입력 후 단어를 작성해주세요." type="text" id="" class="tibox3" v-model="writeForm.tag" />

        <div><el-tag :key="tag" v-for="tag in tags" closable :disable-transitions="false"
            @close="handleClose(tag)">
            {{ tag }}
          </el-tag>
          <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="mini"
            @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
        </div> -->

        <p class="ti"> 첨부할 이미지가 있나요?</p>
        <ul>
          <li><img src="~@/assets/images/qna.png" class="" alt=""></li>
          <li><img src="~@/assets/images/qna-img.png" class="" alt=""></li>
          <li><img src="~@/assets/images/qna-img.png" class="" alt=""></li>
        </ul>
      </div>

      <div class="home4-6-3__tail">
        <button @click="submitForm()">입력하기</button>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import axios from 'axios';

export default {
  data() {
    return {
      writeForm: {
        category_id: '',
        secret_yn: 'Y',
        title: '',
        content: '',
        tag: '',
      },
      tags: ['Tag 1', 'Tag 2', 'Tag 3'],  
      inputVisible: false,
      inputValue: '',
      Account: JSON.parse(localStorage.getItem('account')),
    };
  },
  mounted() { 
  },
  methods: {
    async submitForm() {

      this.$router.push({ name: 'CounsultComplete' });

      // console.log(this.writeForm.category_id);
      // console.log(this.secretYn);
      // console.log(this.writeForm.title);
      // console.log(JSON.parse(localStorage.getItem('account')).id)
      // console.log(this.writeForm.content);
      // console.log(this.tags)
      try {
        const result = await axios.post('121.146.250.230:8083/board/', null, {
          params: {
            boardId: '4',
            accountId: JSON.parse(localStorage.getItem('account')).id,
            categoryId: this.writeForm.category_id,
            secretYn: this.writeForm.secret_yn,
            title: this.writeForm.title,
            content: this.writeForm.content,
          },
        });
        
        if (result.status === 200) {
          alert('작성 완료');
          this.$router.push({ path: 'Counsult' });
        } else {
          alert('작성 실패');
        }
      } catch (err) {
        alert(1);
        throw new Error(err);
      }
    },
    handleClose(tag) {
      this.tags.splice(this.tags.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.tags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
  },
};
</script>

<style>
.el-tag+.el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

</style>