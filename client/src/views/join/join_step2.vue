<template>
  <div>
    <div class="join-wr">

      <div class="step2-top">
        <el-checkbox v-model="agreeForm.terms">서비스 이용약관 (필수)
          <el-button type="text" @click="terms = !terms">내용보기</el-button></el-checkbox>
        <textarea class="nft" v-show="terms">
첫번째 약관
                  </textarea>
        <el-checkbox v-model="agreeForm.terms2">개인정보처리방침 (필수)
          <el-button type="text" @click="terms2 = !terms2">내용보기</el-button></el-checkbox>
        <textarea class="nft" v-show="terms2">
두번째 약관
                  </textarea>
        <el-checkbox v-model="agreeForm.terms3">개인민감정보 처리방침 (필수)
          <el-button type="text" @click="terms3 = !terms3">내용보기</el-button></el-checkbox>
        <textarea class="nft" v-show="terms3">
세번째 약관
                  </textarea>
        <el-checkbox v-model="agreeForm.terms4">위치정보 이용약관 (필수)
          <el-button type="text" @click="terms4 = !terms4">내용보기</el-button></el-checkbox>
        <textarea class="nft" v-show="terms4">
네번째 약관
                  </textarea>
        <el-checkbox v-model="agreeForm.terms5">마케팅 정보수신 (선택)
          <el-button type="text" @click="terms5 = !terms5">내용보기</el-button></el-checkbox>
        <textarea class="nft" v-show="terms5">
다섯번째 약관
                  </textarea>
        <div class="button-color__gray">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" class="check-all"
            @change="handleCheckAllChange">
            뛰장 약관 전체동의123
          </el-checkbox>
        </div>
      </div>

      <div class="step2-bot">
        <div class="button-color__blue">
          <router-link :to="{ name: 'Signup' }" class="agree">동의하기</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {
  },
  data() {
    return {
      terms: false,
      terms2: false,
      terms3: false,
      terms4: false,
      terms5: false,
      agreeForm: {
        terms: '',
        terms2: '',
        terms3: '',
        terms4: '',
        terms5: '',
      },
      allTerms: false,
    };
  },
  watch: {
    'agreeForm.terms': function () {
      this.handleAllTerms();
    },
    'agreeForm.terms2': function () {
      this.handleAllTerms();
    },
    'agreeForm.terms3': function () {
      this.handleAllTerms();
    },
    'agreeForm.terms4': function () {
      this.handleAllTerms();
    },
    'agreeForm.terms5': function () {
      this.handleAllTerms();
    },
  },
  methods: {
    open() {
      /*
      this.$alert('약관내용', '제목', {
        confirmButtonText: '닫기',
      });
      */
     this.termsVisible = !this.termsVisible;
    },
    handleAllTerms() {
      if (this.agreeForm.terms && this.agreeForm.terms2 && this.agreeForm.terms3 && this.agreeForm.terms4 && this.agreeForm.terms5) this.allTerms = true;
      else this.allTerms = false;
    },
    handleAllTermCheck(e) {
      this.agreeForm.terms = e.target.checked;
      this.agreeForm.terms2 = e.target.checked;
      this.agreeForm.terms3 = e.target.checked;
      this.agreeForm.terms4 = e.target.checked;
      this.agreeForm.terms5 = e.target.checked;
    },
  },
};
</script>
