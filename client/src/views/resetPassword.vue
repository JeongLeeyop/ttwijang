<template>
  <div class="reset-password-page">
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
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class ResetPassword extends Vue {
  private newPassword = '';

  private confirmPassword = '';

  private email = '';

  mounted() {
    // 이전 페이지에서 전달받은 이메일
    this.email = this.$route.query.email as string || '';

    if (!this.email) {
      this.$message.warning('잘못된 접근입니다.');
      this.$router.push({ name: 'FindPassword' });
    }
  }

  private goBack() {
    this.$router.go(-1);
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
      // TODO: 실제 비밀번호 재설정 API 호출
      // await resetPassword({ email: this.email, newPassword: this.newPassword });

      this.$message.success('비밀번호가 재설정되었습니다.');

      // 로그인 페이지로 이동
      setTimeout(() => {
        this.$router.push({ name: 'EmailLogin' });
      }, 1500);
    } catch (error) {
      this.$message.error('비밀번호 재설정에 실패했습니다.');
      console.error(error);
    }
  }
}
</script>

<style scoped lang="scss">
.reset-password-page {
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
  .reset-password-page {
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
