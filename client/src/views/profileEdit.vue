<template>
  <div class="content profile-edit-page">
    <div class="form-container">
      <div class="form-group">
        <label for="name">이름</label>
        <input
          id="name"
          v-model="form.name"
          type="text"
          placeholder="이름을 입력하세요."
        />
      </div>

      <div class="form-group">
        <label>생년월일</label>
        <div class="birth-date-group">
          <select v-model="form.birthYear" class="birth-select">
            <option value="">년도</option>
            <option v-for="year in years" :key="year" :value="year">
              {{ year }}
            </option>
          </select>
          <select v-model="form.birthMonth" class="birth-select">
            <option value="">월</option>
            <option v-for="month in 12" :key="month" :value="month">
              {{ month }}
            </option>
          </select>
          <select v-model="form.birthDay" class="birth-select">
            <option value="">일</option>
            <option v-for="day in 31" :key="day" :value="day">
              {{ day }}
            </option>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label>성별</label>
        <div class="gender-group">
          <button
            type="button"
            :class="['gender-button', { active: form.gender === '남자' }]"
            @click="form.gender = '남자'"
          >
            남자
          </button>
          <button
            type="button"
            :class="['gender-button', { active: form.gender === '여자' }]"
            @click="form.gender = '여자'"
          >
            여자
          </button>
        </div>
      </div>

      <div class="form-group">
        <label for="phoneNumber">휴대폰 번호</label>
        <div class="input-with-button">
          <input
            id="phoneNumber"
            v-model="form.phoneNumber"
            type="text"
            placeholder="휴대폰 번호를 입력하세요."
            maxlength="11"
            :disabled="isVerified && !isPhoneChanged"
          />
          <button
            class="verify-button"
            :disabled="!isPhoneChanged || resendTimer > 0"
            @click="handleSendVerification"
          >
            <template v-if="isVerified && !isPhoneChanged">
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

      <div v-if="isPhoneChanged && isVerificationSent" class="form-group">
        <label for="verificationCode">인증 번호</label>
        <div class="input-with-button">
          <input
            id="verificationCode"
            v-model="form.verificationCode"
            type="text"
            placeholder="인증 번호를 입력하세요."
            maxlength="6"
            :disabled="isVerified"
          />
          <button
            class="verify-button"
            :disabled="isVerified"
            @click="handleVerifyCode"
          >
            {{ isVerified ? '인증완료' : '인증 확인' }}
          </button>
        </div>
      </div>

      <button
        class="submit-button"
        :disabled="isSaving"
        @click="handleSave"
      >
        <i v-if="isSaving" class="el-icon-loading"></i>
        {{ isSaving ? '저장 중...' : '저장하기' }}
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getUserInfo, updateUserInfo } from '@/api/user';
import { sendVerificationCode, verifyCode } from '@/api/sms';

@Component({
  name: 'ProfileEdit',
})
export default class ProfileEdit extends Vue {
  private form = {
    name: '',
    birthYear: '' as string | number,
    birthMonth: '' as string | number,
    birthDay: '' as string | number,
    gender: '',
    phoneNumber: '',
    verificationCode: '',
  };

  private originalPhone = '';

  private isVerificationSent = false;

  private isVerified = false;

  private resendTimer = 0;

  private timerInterval: number | null = null;

  private isSaving = false;

  private isLoading = false;

  get years(): number[] {
    const currentYear = new Date().getFullYear();
    const result = [];
    for (let year = currentYear; year >= currentYear - 100; year -= 1) {
      result.push(year);
    }
    return result;
  }

  get isPhoneChanged(): boolean {
    return this.form.phoneNumber !== this.originalPhone;
  }

  @Watch('form.phoneNumber')
  onPhoneNumberChange(): void {
    if (this.isPhoneChanged) {
      this.isVerified = false;
      this.isVerificationSent = false;
      this.form.verificationCode = '';
      this.clearTimer();
    }
  }

  async created(): Promise<void> {
    await this.loadUserInfo();
  }

  beforeDestroy(): void {
    this.clearTimer();
  }

  private async loadUserInfo(): Promise<void> {
    this.isLoading = true;
    try {
      const response = await getUserInfo();
      if (response.data) {
        const user = response.data;
        this.form.name = user.actualName || user.name || '';
        this.form.gender = user.gender === 0 ? '남자' : '여자';
        this.form.phoneNumber = user.concatNumber || user.tel || '';
        this.originalPhone = this.form.phoneNumber;

        // 생년월일 파싱
        if (user.birth) {
          const birthStr = String(user.birth);
          if (birthStr.includes('-')) {
            const parts = birthStr.split('-');
            this.form.birthYear = Number(parts[0]);
            this.form.birthMonth = Number(parts[1]);
            this.form.birthDay = Number(parts[2]);
          } else if (birthStr.length === 8) {
            this.form.birthYear = Number(birthStr.substring(0, 4));
            this.form.birthMonth = Number(birthStr.substring(4, 6));
            this.form.birthDay = Number(birthStr.substring(6, 8));
          }
        }

        // 번호가 이미 있으면 인증된 것으로 간주
        if (this.form.phoneNumber) {
          this.isVerified = true;
        }
      }
    } catch (error) {
      console.error('사용자 정보 로드 실패:', error);
      this.$message.error('사용자 정보를 불러오는데 실패했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private async handleSendVerification(): Promise<void> {
    if (!this.form.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.form.phoneNumber.length < 10) {
      this.$message.warning('올바른 휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.resendTimer > 0) {
      this.$message.warning(`${this.resendTimer}초 후에 다시 시도해주세요.`);
      return;
    }

    try {
      const response = await sendVerificationCode({
        phoneNumber: this.form.phoneNumber,
      });

      if (response.data.success) {
        this.isVerificationSent = true;
        this.startResendTimer();
        this.$message.success('인증번호가 발송되었습니다.');
      } else {
        this.isVerificationSent = true;
        this.$message.error(response.data.message || '인증번호 발송에 실패했습니다.');
      }
    } catch (error: any) {
      this.isVerificationSent = true;
      this.$message.error(error.response?.data?.message || '인증번호 발송에 실패했습니다.');
    }
  }

  private async handleVerifyCode(): Promise<void> {
    if (!this.form.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    if (this.form.verificationCode.length !== 6) {
      this.$message.warning('인증번호는 6자리입니다.');
      return;
    }

    try {
      const response = await verifyCode({
        phoneNumber: this.form.phoneNumber,
        verificationCode: this.form.verificationCode,
      });

      if (response.data.success) {
        this.isVerified = true;
        this.originalPhone = this.form.phoneNumber;
        this.$message.success('인증이 완료되었습니다.');
      } else {
        this.$message.error(response.data.message || '인증번호가 올바르지 않습니다.');
      }
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '인증번호가 올바르지 않거나 만료되었습니다.');
    }
  }

  private startResendTimer(): void {
    this.clearTimer();
    this.resendTimer = 60;

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

  private validateForm(): boolean {
    if (!this.form.name) {
      this.$message.warning('이름을 입력해주세요.');
      return false;
    }

    if (!this.form.birthYear || !this.form.birthMonth || !this.form.birthDay) {
      this.$message.warning('생년월일을 선택해주세요.');
      return false;
    }

    if (!this.form.gender) {
      this.$message.warning('성별을 선택해주세요.');
      return false;
    }

    if (!this.form.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return false;
    }

    if (this.isPhoneChanged && !this.isVerified) {
      this.$message.warning('변경된 휴대폰 번호의 인증을 완료해주세요.');
      return false;
    }

    return true;
  }

  private async handleSave(): Promise<void> {
    if (!this.validateForm()) {
      return;
    }

    this.isSaving = true;
    try {
      const birthStr = `${this.form.birthYear}-${String(this.form.birthMonth).padStart(2, '0')}-${String(this.form.birthDay).padStart(2, '0')}`;
      const data = {
        actualName: this.form.name,
        birth: birthStr,
        gender: this.form.gender === '남자' ? 0 : 1,
        concatNumber: this.form.phoneNumber,
      };

      await updateUserInfo(data);
      this.$message.success('프로필이 수정되었습니다.');

      setTimeout(() => {
        this.$router.go(-1);
      }, 1000);
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '프로필 수정에 실패했습니다.';
      this.$message.error(errorMsg);
      console.error('프로필 수정 실패:', error);
    } finally {
      this.isSaving = false;
    }
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
