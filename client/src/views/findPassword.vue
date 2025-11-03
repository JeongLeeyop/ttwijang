<template>
  <div class="find-password-page">
    <div class="header">
      <button class="back-button" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>

    <div class="content">
      <h1 class="page-title">비밀번호 찾기</h1>

      <div class="form-container">
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
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class FindPassword extends Vue {
  private email = '';

  private phoneNumber = '';

  private verificationCode = '';

  private isVerificationSent = false;

  private isVerified = false;

  private goBack() {
    this.$router.go(-1);
  }

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

<style scoped lang="scss">
.find-password-page {
  min-height: 100vh;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;

  .header {
    padding: 16px;
    border-bottom: 1px solid #e0e0e0;

    .back-button {
      background: none;
      border: none;
      padding: 8px;
      cursor: pointer;
      color: #000000;
      display: flex;
      align-items: center;
      justify-content: center;

      &:hover {
        opacity: 0.7;
      }
    }
  }

  .content {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px 20px;
  }

  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: #000000;
    margin-bottom: 40px;
    text-align: center;
  }

  .form-container {
    width: 100%;
    max-width: 400px;
  }

  .form-group {
    margin-bottom: 24px;

    label {
      display: block;
      font-size: 14px;
      font-weight: 500;
      color: #333333;
      margin-bottom: 8px;
    }

    input {
      width: 100%;
      padding: 12px 16px;
      border: 1px solid #d0d0d0;
      border-radius: 4px;
      font-size: 14px;
      outline: none;
      box-sizing: border-box;

      &:focus {
        border-color: #061da1;
      }

      &::placeholder {
        color: #999999;
      }
    }
  }

  .input-with-button {
    display: flex;
    gap: 8px;

    input {
      flex: 1;
    }

    .verify-button {
      padding: 12px 20px;
      background-color: #ffffff;
      color: #061da1;
      border: 1px solid #061da1;
      border-radius: 4px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      white-space: nowrap;
      transition: all 0.3s ease;

      &:hover {
        background-color: #061da1;
        color: #ffffff;
      }
    }
  }

  .submit-button {
    width: 100%;
    padding: 16px;
    background-color: #061da1;
    color: #ffffff;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    margin-top: 16px;

    &:hover {
      background-color: #04166e;
    }

    &:active {
      transform: scale(0.98);
    }
  }
}

@media (max-width: 768px) {
  .find-password-page {
    .content {
      padding: 24px 16px;
    }

    .page-title {
      font-size: 20px;
      margin-bottom: 30px;
    }
  }
}
</style>
