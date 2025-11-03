<template>
    <div class="home4-1-3__a withdrawal" style="position: relative;padding-bottom: 40%;">
      <el-form ref="form" class="" onsubmit="return false;" :model="form" :rules="rules">
      <div class="diary2-2-3__top">
        <router-link :to="{ name: 'Mypage' }" class="el-button el-button--default"><img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
        </router-link>
      </div>
      <div class="home4-1-4__top withdrawal">
        <p class="ab">회원을 탈퇴하시는 이유는 무엇입니까?</p>
      </div>

      <div class="home4-1-4__mid">
        <el-form-item prop="reason">
          <el-input
            v-model="form.reason"
            class="withdraw-reason"
            type="textarea"
            placeholder="상세 사유를 입력해주세요."
            maxlength="200"
            rows="5"
          />
        </el-form-item>
      </div>

      <div class="home4-1-4__mid">
        <p class="ab">회원탈퇴에 동의하시면 '뛰장 회원탈퇴' 라고 입력해주세요</p>
        <el-form-item prop="confirmTxt">
          <el-input placeholder="뛰장 회원탈퇴" type="text" id="" v-model="form.confirmTxt" />
        </el-form-item>
      </div>

    </el-form>
      <div class="home4-1-4__bot">
        <a href="#" @click="handleWithdraw()" class="link__a">확인</a>
      </div>
    </div>
</template>

<script lang="ts">
/* eslint-disable */
import { Vue, Component } from 'vue-property-decorator';
import { Form } from 'element-ui';
import { withdrawUser } from '@/api/user';
import { UserModule } from '@/store/modules/user';

@Component({
})

export default class extends Vue {
  private form = {
    reason: '',
    confirmTxt: '',
  }

  private confirmTextValidator: any = (rules: any, value: any, callback: Function) => {
    if (value !== '뛰장 회원탈퇴') {
      callback(new Error('정확한 문구를 입력해주세요.'));
    }
    callback();
  };

  private rules: any = {
    reason: [
      { required: 'true', message: '탈퇴사유를 입력해주세요.', trigger: ['blur', 'change'] },
    ],
    confirmTxt: [
      { validator: this.confirmTextValidator, trigger: ['blur', 'change'] },
    ],
  };

  private handleWithdraw() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        withdrawUser(this.form).then(() => {
          this.$alert('회원탈퇴가 완료되었습니다.');
          UserModule.LogOut();
          this.$router.push({ name: 'Home' });
        });
      }
    });
  }
}
</script>

<style lang="scss">
.withdraw-reason {
  .el-textarea__inner {
    margin: 0 auto;
  }
}
</style>