<template>
  <div class="content reset-password-page">
    <h1 class="page-title">비밀번호 찾기</h1>

      <div class="form-container">
        <div class="form-group">
          <label for="newPassword">비밀번호 재설정</label>
          <input
            id="newPassword"
            v-model="newPassword"
            type="password"
            placeholder="재설정 할 비밀번호를 입력하세요."
          />
        </div>

        <div class="form-group">
          <label for="confirmPassword">비밀번호 확인</label>
          <input
            id="confirmPassword"
            v-model="confirmPassword"
            type="password"
            placeholder="비밀번호를 한 번 더 입력하세요."
            @keyup.enter="handleResetPassword"
          />
        </div>

        <button class="submit-button" @click="handleResetPassword">
          비밀번호 찾기
        </button>
      </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import AuthLayout from '@/Layout/authLayout.vue';
import { resetPassword } from '@/api/user';

@Component({
  components: {
    AuthLayout,
  },
})
export default class ResetPassword extends Vue {
  private newPassword = '';

  private confirmPassword = '';

  private resetToken = '';

  mounted() {
    // 이전 페이지에서 전달받은 재설정 토큰
    this.resetToken = this.$route.query.token as string || '';

    if (!this.resetToken) {
      this.$message.warning('잘못된 접근입니다.');
      this.$router.push({ name: 'FindPassword' });
    }
  }

  private validatePassword(): boolean {
    if (!this.newPassword) {
      this.$message.warning('새 비밀번호를 입력해주세요.');
      return false;
    }

    if (this.newPassword.length < 8) {
      this.$message.warning('비밀번호는 8자 이상이어야 합니다.');
      return false;
    }

    if (!this.confirmPassword) {
      this.$message.warning('비밀번호 확인을 입력해주세요.');
      return false;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.$message.warning('비밀번호가 일치하지 않습니다.');
      return false;
    }

    return true;
  }

  private async handleResetPassword() {
    if (!this.validatePassword()) {
      return;
    }

    try {
      // 비밀번호 재설정 API 호출
      await resetPassword(this.resetToken, this.newPassword);

      this.$message.success('비밀번호가 재설정되었습니다.');

      // 로그인 페이지로 이동
      setTimeout(() => {
        this.$router.push({ name: 'EmailLogin' });
      }, 1500);
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '비밀번호 재설정에 실패했습니다.');
      console.error(error);
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
