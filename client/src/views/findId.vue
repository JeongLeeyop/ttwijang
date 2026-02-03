<template>
  <div class="find-id-page content">
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
              @keyup.enter="handleFindId"
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

        <button class="submit-button" @click="handleFindId">
          아이디 찾기
        </button>
      </div>
      </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import AuthLayout from '@/Layout/authLayout.vue';
import {
  findEmail,
} from '@/api/user';
import {
  sendVerificationCode,
  verifyCode,
} from '@/api/sms';

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

  private foundEmail = '';

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
      const response = await findEmail(this.phoneNumber, this.verificationCode);
      this.foundEmail = response.data.email;

      // 성공 다이얼로그로 변경
      this.$alert(`귀하의 아이디는 <strong>${this.foundEmail}</strong> 입니다.`, '아이디 찾기 완료', {
        confirmButtonText: '로그인하기',
        dangerouslyUseHTMLString: true,
        callback: () => {
          this.$router.push({ name: 'EmailLogin' });
        },
      });
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '해당 휴대폰 번호로 가입된 계정을 찾을 수 없습니다.');
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
