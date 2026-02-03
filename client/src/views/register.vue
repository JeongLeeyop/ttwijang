<template>
  <div class="content register-page">
    <div class="form-container">
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            placeholder="이메일을 입력하세요."
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="비밀번호를 입력하세요."
          />
        </div>

        <div class="form-group">
          <label for="passwordConfirm">비밀번호 확인</label>
          <input
            id="passwordConfirm"
            v-model="form.passwordConfirm"
            type="password"
            placeholder="비밀번호를 한 번 더 입력하세요."
          />
        </div>

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
              :disabled="isVerified"
            />
            <button
              class="verify-button"
              :disabled="isVerified || resendTimer > 0"
              @click="sendVerificationCode"
            >
              <template v-if="isVerified">
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

        <div class="form-group">
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
              @click="verifyCode"
            >
              {{ isVerified ? '인증완료' : '인증 확인' }}
            </button>
          </div>
        </div>

        <div class="terms-section">
          <div class="term-item">
            <input
              id="term1"
              v-model="form.terms.term1"
              type="checkbox"
            />
            <label for="term1">전체동의</label>
          </div>
          <div class="term-item">
            <input
              id="term2"
              v-model="form.terms.term2"
              type="checkbox"
            />
            <label for="term2">만 14세 이상입니다 (필수)</label>
          </div>
          <div class="term-item">
            <input
              id="term3"
              v-model="form.terms.term3"
              type="checkbox"
            />
            <label for="term3">개인정보 수집 및 이용 동의 (필수)</label>
          </div>
          <div class="term-item">
            <input
              id="term4"
              v-model="form.terms.term4"
              type="checkbox"
            />
            <label for="term4">개인정보 제3자 제공 동의 (필수)</label>
          </div>
          <div class="term-item">
            <input
              id="term5"
              v-model="form.terms.term5"
              type="checkbox"
            />
            <label for="term5">개인정보 민감정보 활용 동의 (선택)</label>
          </div>
          <div class="term-item">
            <input
              id="term6"
              v-model="form.terms.term6"
              type="checkbox"
            />
            <label for="term6">이벤트 및 광고 정보 제공 수신 (선택)</label>
          </div>
        </div>

        <button class="submit-button" @click="handleRegister">
          가입하기
        </button>
      </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import AuthLayout from '@/Layout/authLayout.vue';
import {
  register,
  RegisterData,
} from '@/api/user';
import { sendVerificationCode, verifyCode } from '@/api/sms';

@Component({
  components: {
    AuthLayout,
  },
})
export default class Register extends Vue {
  private form = {
    email: '',
    password: '',
    passwordConfirm: '',
    name: '',
    birthYear: '',
    birthMonth: '',
    birthDay: '',
    gender: '',
    phoneNumber: '',
    verificationCode: '',
    terms: {
      term1: false,
      term2: false,
      term3: false,
      term4: false,
      term5: false,
      term6: false,
    },
  };

  private isVerificationSent = false;

  private isVerified = false;

  private resendTimer = 0;

  private timerInterval: number | null = null;

  @Watch('form.phoneNumber')
  onPhoneNumberChange() {
    // 휴대폰 번호 변경 시 인증 상태 초기화
    if (this.isVerified || this.isVerificationSent) {
      this.isVerified = false;
      this.isVerificationSent = false;
      this.form.verificationCode = '';
      this.clearTimer();
    }
  }

  private get years(): number[] {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let year = currentYear; year >= currentYear - 100; year -= 1) {
      years.push(year);
    }
    return years;
  }

  @Watch('form.terms.term1')
  onAllTermsChange(val: boolean) {
    if (val) {
      this.form.terms.term2 = true;
      this.form.terms.term3 = true;
      this.form.terms.term4 = true;
      this.form.terms.term5 = true;
      this.form.terms.term6 = true;
    }
  }

  @Watch('form.terms', { deep: true })
  onTermsChange() {
    const {
        term2,
        term3,
        term4,
        term5,
        term6,
    } = this.form.terms;
    if (term2 && term3 && term4 && term5 && term6) {
      this.form.terms.term1 = true;
    } else {
      this.form.terms.term1 = false;
    }
  }

  private async sendVerificationCode() {
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
      // SMS 인증번호 발송 API 호출
      const response = await sendVerificationCode({
        phoneNumber: this.form.phoneNumber,
      });

      if (response.data.success) {
        this.isVerificationSent = true;
        this.startResendTimer();
        this.$message.success('인증번호가 발송되었습니다. 3분 이내에 입력해주세요.');
      } else {
        this.$message.error(response.data.message || '인증번호 발송에 실패했습니다.');
      }
    } catch (error: any) {
      this.$message.error(error.response?.data?.message || '인증번호 발송에 실패했습니다.');
    }
  }

  private async verifyCode() {
    if (!this.form.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    if (this.form.verificationCode.length !== 6) {
      this.$message.warning('인증번호는 6자리입니다.');
      return;
    }

    try {
      // SMS 인증번호 검증 API 호출
      const response = await verifyCode({
        phoneNumber: this.form.phoneNumber,
        verificationCode: this.form.verificationCode,
      });

      if (response.data.success) {
        this.isVerified = true;
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
    this.resendTimer = 60; // 60초 대기

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

  beforeDestroy(): void {
    this.clearTimer();
  }

  private validateForm(): boolean {
    if (!this.form.email) {
      this.$message.warning('이메일을 입력해주세요.');
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.form.email)) {
      this.$message.warning('올바른 이메일 형식이 아닙니다.');
      return false;
    }

    if (!this.form.password) {
      this.$message.warning('비밀번호를 입력해주세요.');
      return false;
    }

    if (this.form.password.length < 8) {
      this.$message.warning('비밀번호는 8자 이상이어야 합니다.');
      return false;
    }

    if (!this.form.passwordConfirm) {
      this.$message.warning('비밀번호 확인을 입력해주세요.');
      return false;
    }

    if (this.form.password !== this.form.passwordConfirm) {
      this.$message.warning('비밀번호가 일치하지 않습니다.');
      return false;
    }

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

    // 인증 완료 여부 강화 검증
    if (!this.isVerified) {
      this.$message.error('휴대폰 인증을 완료해주세요.');
      return false;
    }

    if (!this.isVerificationSent) {
      this.$message.error('인증번호를 먼저 요청해주세요.');
      return false;
    }

    if (!this.form.terms.term2) {
      this.$message.warning('만 14세 이상 동의는 필수입니다.');
      return false;
    }

    if (!this.form.terms.term3) {
      this.$message.warning('개인정보 수집 및 이용 동의는 필수입니다.');
      return false;
    }

    if (!this.form.terms.term4) {
      this.$message.warning('개인정보 제3자 제공 동의는 필수입니다.');
      return false;
    }

    return true;
  }

  private async handleRegister() {
    if (!this.validateForm()) {
      return;
    }

    try {
      // 실제 회원가입 API 호출
      const registerData: RegisterData = {
        email: this.form.email,
        password: this.form.password,
        name: this.form.name,
        birthDate: `${this.form.birthYear}-${String(this.form.birthMonth).padStart(2, '0')}-${String(this.form.birthDay).padStart(2, '0')}`,
        gender: this.form.gender as '남자' | '여자',
        phoneNumber: this.form.phoneNumber,
        marketingAgreed: this.form.terms.term6,
      };

      await register(registerData);

      this.$message.success('회원가입이 완료되었습니다.');

      // 로그인 페이지로 이동
      setTimeout(() => {
        this.$router.push({ name: 'EmailLogin' });
      }, 1500);
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '회원가입에 실패했습니다.';
      this.$message.error(errorMsg);
      console.error(error);
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Login/Auth Pages Styles section */
</style>
