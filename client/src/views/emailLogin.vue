<template>
    <div class="content email-login-page">
    <div class="form-container">
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder=""
            @keyup.enter="handleLogin"
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder=""
            @keyup.enter="handleLogin"
          />
        </div>

        <div class="remember-me">
          <input
            id="remember"
            v-model="rememberMe"
            type="checkbox"
          />
          <label for="remember">아이디 저장</label>
        </div>

        <button class="login-button" @click="handleLogin">
          로그인
        </button>

        <div class="footer-links">
          <a href="#" @click.prevent="findId">아이디 찾기</a>
          <span class="divider">|</span>
          <a href="#" @click.prevent="findPassword">비밀번호 찾기</a>
          <span class="divider">|</span>
          <a href="#" @click.prevent="register">회원가입</a>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { UserModule } from '@/store/modules/user';

@Component
export default class EmailLogin extends Vue {
  private email = '';

  private password = '';

  private rememberMe = false;

  mounted() {
    // 저장된 이메일 불러오기
    const savedEmail = localStorage.getItem('savedEmail');
    if (savedEmail) {
      this.email = savedEmail;
      this.rememberMe = true;
    }
  }

  private goBack() {
    this.$router.go(-1);
  }

  private async handleLogin() {
    if (!this.email) {
      this.$message.warning('이메일을 입력해주세요.');
      return;
    }

    if (!this.password) {
      this.$message.warning('비밀번호를 입력해주세요.');
      return;
    }

    try {
      // 아이디 저장 처리
      if (this.rememberMe) {
        localStorage.setItem('savedEmail', this.email);
      } else {
        localStorage.removeItem('savedEmail');
      }

      // 이메일 로그인 API 호출
      await UserModule.EmailLogin({ email: this.email, password: this.password });

      this.$message.success('로그인 성공');
      this.$router.push({ name: 'Home' });
    } catch (error: any) {
      const errorMsg = error.message || '로그인에 실패했습니다.';
      this.$message.error(errorMsg);
      console.error(error);
    }
  }

  private findId() {
    this.$router.push({ name: 'FindId' });
  }

  private findPassword() {
    this.$router.push({ name: 'FindPassword' });
  }

  private register() {
    this.$router.push({ name: 'Register' });
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
