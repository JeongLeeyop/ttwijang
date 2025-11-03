<template>
  <div class="inquiry_box">
    <el-radio v-model="inquiry" label="0">회원정보에 등록한 휴대전화 번호로 인증</el-radio>
    <div class="inquiry" v-if="inquiry === '0'">
      <div class="phone">
        <div class="box">
          <span class="label">이름</span>
          <el-input placeholder="이름" />
        </div>
        <div class="df">
          <span class="label">휴대전화</span>
          <el-input class="phoneauth" placeholder="전화번호" />
          <button class="auth">인증번호 받기</button>
        </div>
        <div class="df">
          <el-input class="phoneauth" placeholder="인증번호 숫자 입력" />
          <button class="auth" @click="submit">인증번호 확인</button>
        </div>
      </div>
    </div>

    <el-radio v-model="inquiry" label="1">회원정보에 등록한 이메일로 인증</el-radio>
    <div class="inquiry" v-if="inquiry === '1'">
      <div class="phone">
        <div class="box">
          <span class="label">이름</span>
          <el-input placeholder="이름" />
        </div>
        <div class="df">
          <span class="label">이메일 주소</span>
          <el-input class="phoneauth" placeholder="이메일" />
          <button class="auth">인증번호 받기</button>
        </div>
        <div class="df">
          <el-input class="phoneauth" placeholder="인증번호 숫자 입력" />
          <button class="auth" @click="submit">인증번호 확인</button>
        </div>
      </div>
    </div>

    <div class="inquiry" v-if="pwset">
      <div class="box">
        <span class="label">비밀번호를 재설정 해주세요.</span>
        <el-input placeholder="영문,숫자,특수문자 조합 8~20자" type="password" />
      </div>
      <div class="box">
        <span class="label">비밀번호를 확인 해주세요.</span>
        <el-input placeholder="비밀번호 재입력" type="password" />
      </div>
      <button class="submit" @click="submit">확인</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      inquiry: false,
      pwset: false,
    };
  },
  methods: {
    submit() {
      this.pwset = !this.pwset;
    },
  },
};
</script>
