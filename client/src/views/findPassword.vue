<template>
    <div class="content find-password-page">
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
              :disabled="isVerified"
            />
            <button
              class="verify-button"
              :disabled="isVerified || resendTimer > 0"
              @click="sendVerificationCode"
            >
              <template v-if="isVerified">
                인증완료
              </template>
              <template v-else-if="resendTimer > 0">
                재발송 ({{ resendTimer }}초)
              </template>
              <template v-else>
                {{ isVerificationSent ? '재발송' : '인증 요청' }}
              </template>
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
              :disabled="isVerified"
              @keyup.enter="handleNext"
            />
            <button
              class="verify-button"
              :disabled="isVerified"
              @click="verifyCode"
            >
              {{ isVerified ? '인증완료' : '인증 확인' }}
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
import {
  requestPasswordReset,
} from '@/api/user';
import {
  sendVerificationCode,
  verifyCode,
} from '@/api/sms';

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

  private resendTimer = 0;

  private timerInterval: number | null = null;

  private async sendVerificationCode() {
    if (!this.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.phoneNumber.length < 10) {
      this.$message.warning('올바른 휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.resendTimer > 0) {
      this.$message.warning(`${this.resendTimer}초 후에 다시 시도해주세요.`);
      return;
    }

    try {
      const response = await sendVerificationCode({
        phoneNumber: this.phoneNumber,
      });
      this.isVerificationSent = true;
      this.startResendTimer();
      this.$message.success('인증번호가 발송되었습니다. 3분 이내에 입력해주세요.');
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '인증번호 발송에 실패했습니다.');
    }
  }

  private async verifyCode() {
    if (!this.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    try {
      const response = await verifyCode({
        phoneNumber: this.phoneNumber,
        verificationCode: this.verificationCode,
      });
      this.isVerified = true;
      this.$message.success('인증이 완료되었습니다.');
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '인증번호가 올바르지 않습니다.');
    }
  }

  private async handleNext() {
    if (!this.email) {
      this.$message.warning('이메일을 입력해주세요.');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.$message.warning('올바른 이메일 형식이 아닙니다.');
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
      // 비밀번호 재설정 요청 API 호출
      const response = await requestPasswordReset(
        this.email,
        this.phoneNumber,
        this.verificationCode,
      );
      const resetToken = response.data.resetToken;

      // 인증 성공 후 비밀번호 재설정 페이지로 이동
      this.$router.push({
        name: 'ResetPassword',
        query: { token: resetToken },
      });
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '입력하신 정보와 일치하는 계정을 찾을 수 없습니다.');
      console.error(error);
    }
  }

  private startResendTimer(): void {
    this.clearTimer();
    this.resendTimer = 60; // 60초 대기

    this.timerInterval = window.setInterval(() => {
      this.resendTimer -= 1;
      if (this.resendTimer <= 0) {
        this.clearTimer();
      }
    }, 1000);
  }

  private clearTimer(): void {
    if (this.timerInterval !== null) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
    this.resendTimer = 0;
  }

  beforeDestroy(): void {
    this.clearTimer();
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
