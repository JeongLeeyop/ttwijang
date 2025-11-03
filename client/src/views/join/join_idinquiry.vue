<template>
  <div class="inquiry_box">
    <p>휴대전화 번호로 아이디 찾기</p>
    <div class="inquiry">
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
  </div>
</template>

<script>
</script>
