<template>
  <div class="email-login-page">
    <div class="header">
      <button class="back-button" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>

    <div class="content">
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

      // TODO: 실제 로그인 API 호출
      // await UserModule.EmailLogin({ email: this.email, password: this.password });

      this.$message.success('로그인 성공');
      this.$router.push({ name: 'Home' });
    } catch (error) {
      this.$message.error('로그인에 실패했습니다.');
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

<style scoped lang="scss">
.email-login-page {
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
    align-items: flex-start;
    justify-content: center;
    padding: 40px 20px;
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

  .remember-me {
    display: flex;
    align-items: center;
    margin-bottom: 24px;

    input[type="checkbox"] {
      width: 18px;
      height: 18px;
      margin: 0;
      margin-right: 8px;
      cursor: pointer;
    }

    label {
      font-size: 14px;
      color: #666666;
      cursor: pointer;
      user-select: none;
    }
  }

  .login-button {
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

    &:hover {
      background-color: #04166e;
    }

    &:active {
      transform: scale(0.98);
    }
  }

  .footer-links {
    margin-top: 24px;
    text-align: center;
    font-size: 14px;

    a {
      color: #666666;
      text-decoration: none;
      transition: color 0.3s ease;

      &:hover {
        color: #061da1;
      }
    }

    .divider {
      margin: 0 12px;
      color: #d0d0d0;
    }
  }
}

@media (max-width: 768px) {
  .email-login-page {
    .content {
      padding: 24px 16px;
    }
  }
}
</style>
