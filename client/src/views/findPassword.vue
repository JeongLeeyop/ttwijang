<template>
    <div class="content find-password-page">
    <div class="form-container">
          <h1 class="page-title">비밀번호 찾기</h1>
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="이메일을 입력하세요."
          />
        </div>

        <div class="form-group">
          <label for="phoneNumber">휴대폰 번호</label>
          <div class="input-with-button">
            <input
              id="phoneNumber"
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
              @keyup.enter="handleNext"
            />
            <button class="verify-button" @click="verifyCode">
              인증 확인
            </button>
          </div>
        </div>

        <button class="submit-button" @click="handleNext">
          다음
        </button>
      </div>
      </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component({
  components: {
  },
})
export default class FindPassword extends Vue {
  private email = '';

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

  private async handleNext() {
    if (!this.email) {
      this.$message.warning('이메일을 입력해주세요.');
      return;
    }

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
      // TODO: 실제 비밀번호 찾기 API 호출
      // 인증 성공 후 비밀번호 재설정 페이지로 이동
      this.$router.push({
        name: 'ResetPassword',
        query: { email: this.email },
      });
    } catch (error) {
      this.$message.error('인증에 실패했습니다.');
      console.error(error);
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
