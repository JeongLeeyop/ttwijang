<template>
  <div class="main">
    <div class="main-bg-pattern"></div>
    <div class="main-wrap">
      <div class="main-logo">
        <div class="brand-logo">
          <span class="brand-icon">⚽</span>
          <span class="brand-name">뛰장</span>
        </div>
        <p class="brand-sub">풋살 리그 관리 시스템</p>
      </div>

      <div class="login-wrap">
        <div class="login-title">
          <p>관리자 로그인</p>
        </div>

        <el-form ref="loginForm" class="login-form" :rules="rules" :model="loginForm" onsubmit="return false;">
          <fieldset class="login__fs">
            <div class="login-form__inner">
              <legend>회원로그인</legend>

              <div class="input-box__login">
                <el-form-item prop="username">
                  <label for="username">ID</label>
                  <el-input placeholder="아이디를 입력해주세요" type="text" id="username" v-model="loginForm.username" />
                </el-form-item>
              </div>

              <div class="input-box__login">
                <el-form-item prop="password">
                  <label for="password">PW</label>
                  <el-input placeholder="비밀번호를 입력해주세요" type="password" id="password" v-model="loginForm.password" />
                </el-form-item>
              </div>
              <button class="login-submit" type="submit" @click="handleLogin()">로그인</button>
            </div>

          </fieldset>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ElForm } from 'element-ui/types/form';
import { UserModule } from '@/store/modules/user';

@Component({
  name: 'LoginIndex',
})
export default class extends Vue {
  created() {
    // if (UserModule.token) this.$router.push({ name: 'Menu' });
  }

  private loginForm = {
    username: '',
    password: '',
  };

  private rules = {
    username: [{ required: true, message: '아이디를 입력하세요.', trigger: 'blur' }],
    password: [{ required: true, message: '비밀번호를 입력하세요.', trigger: 'blur' }],
  }

  private loading: boolean = false;

  private handleLogin() {
    (this.$refs.loginForm as ElForm).validate((valid: boolean) => {
      if (valid) {
        this.loading = true;
        UserModule.Login(this.loginForm).then(() => {
          this.loading = false;
          const redirect = this.$route.query.redirect as string;
          this.$router.push({ path: redirect || '/dashboard' }).catch(() => {});
        }).catch(() => {
          this.loading = false;
        });
      }
    });
  }
}
</script>
