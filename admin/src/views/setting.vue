<template>
  <div class="setting-wrap">
    <div class="setting__title">
      <div>
        <p class="tl">환경설정</p>
      </div>

      <div class="setting__btn__box">
        <button @click="$router.go(-1)" class="cancel">취소</button>
        <button class="save" type="submit">저장</button>
      </div>
    </div>

    <form class="setting-login-form" id="setting-login-form">
      <fieldset class="setting-login__fs">
        <div class="setting-login-form__inner">
          <legend>환경설정 로그인</legend>

          <div class="input-box__setting">
            <label for="userId">아이디</label>
            <el-input placeholder="" type="text" id="userId" v-model="userid" />
          </div>

          <div class="input-box__setting">
            <label for="userPw">비밀번호</label>
            <el-input placeholder="" type="password" id="userPw" v-model="userpw" />
          </div>

          <div class="input-box__setting">
            <label for="userPw">비밀번호 확인</label>
            <el-input placeholder="" type="password" id="userPw" v-model="userpwr" />
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script>
export default {

  components: {
  },
  data() {
    return {
      userid: '',
      userpw: '',
      userpwr: '',
    };
  },
};
</script>
