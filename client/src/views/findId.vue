<template>
  <div class="find-id-page content">
    <h1 class="page-title">아이디 찾기</h1>

      <div class="form-container">
        <div class="form-group">
          <label for="name">휴대폰 번호</label>
          <div class="input-with-button">
            <input
              id="name"
              v-model="phoneNumber"
              type="text"
              placeholder="휴대폰 번호를 입력하세요."
              maxlength="11"
            />
            <button class="verify-button" @click="sendVerificationCode">
              인증 요청
            </button>
          </div>
        </div>

        <div class="form-group">
          <label for="verificationCode">인증 번호</label>
          <div class="input-with-button">
            <input
              id="verificationCode"
              v-model="verificationCode"
              type="text"
              placeholder="인증 번호를 입력하세요."
              maxlength="6"
              @keyup.enter="handleFindId"
            />
            <button class="verify-button" @click="verifyCode">
              인증 확인
            </button>
          </div>
        </div>

        <button class="submit-button" @click="handleFindId">
          아이디 찾기
        </button>
      </div>
      </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import AuthLayout from '@/Layout/authLayout.vue';

@Component({
  components: {
    AuthLayout,
  },
})
export default class FindId extends Vue {
  private phoneNumber = '';

  private verificationCode = '';

  private isVerificationSent = false;

  private isVerified = false;

  private sendVerificationCode() {
    if (!this.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.phoneNumber.length < 10) {
      this.$message.warning('올바른 휴대폰 번호를 입력해주세요.');
      return;
    }

    // TODO: 실제 인증번호 발송 API 호출
    this.isVerificationSent = true;
    this.$message.success('인증번호가 발송되었습니다.');
  }

  private verifyCode() {
    if (!this.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    // TODO: 실제 인증번호 확인 API 호출
    this.isVerified = true;
    this.$message.success('인증이 완료되었습니다.');
  }

  private async handleFindId() {
    if (!this.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return;
    }

    if (!this.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    if (!this.isVerified) {
      this.$message.warning('인증 확인을 먼저 진행해주세요.');
      return;
    }

    try {
      // TODO: 실제 아이디 찾기 API 호출
      this.$message.success('귀하의 아이디는 test@example.com 입니다.');
    } catch (error) {
      this.$message.error('아이디 찾기에 실패했습니다.');
      console.error(error);
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
