<template>
  <div class="register-page">
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
            />
            <button class="verify-button" @click="sendVerificationCode">
              인증 요청
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
            />
            <button class="verify-button" @click="verifyCode">
              인증 확인
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
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';

@Component
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

  private goBack() {
    this.$router.go(-1);
  }

  private sendVerificationCode() {
    if (!this.form.phoneNumber) {
      this.$message.warning('휴대폰 번호를 입력해주세요.');
      return;
    }

    if (this.form.phoneNumber.length < 10) {
      this.$message.warning('올바른 휴대폰 번호를 입력해주세요.');
      return;
    }

    // TODO: 실제 인증번호 발송 API 호출
    this.isVerificationSent = true;
    this.$message.success('인증번호가 발송되었습니다.');
  }

  private verifyCode() {
    if (!this.form.verificationCode) {
      this.$message.warning('인증번호를 입력해주세요.');
      return;
    }

    // TODO: 실제 인증번호 확인 API 호출
    this.isVerified = true;
    this.$message.success('인증이 완료되었습니다.');
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

    if (!this.isVerified) {
      this.$message.warning('휴대폰 인증을 완료해주세요.');
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
      // TODO: 실제 회원가입 API 호출
      const registerData = {
        email: this.form.email,
        password: this.form.password,
        name: this.form.name,
        birthDate: `${this.form.birthYear}-${String(this.form.birthMonth).padStart(2, '0')}-${String(this.form.birthDay).padStart(2, '0')}`,
        gender: this.form.gender,
        phoneNumber: this.form.phoneNumber,
        marketingAgreed: this.form.terms.term6,
      };

      // await registerUser(registerData);

      this.$message.success('회원가입이 완료되었습니다.');

      // 로그인 페이지로 이동
      setTimeout(() => {
        this.$router.push({ name: 'EmailLogin' });
      }, 1500);
    } catch (error) {
      this.$message.error('회원가입에 실패했습니다.');
      console.error(error);
    }
  }
}
</script>

<style scoped lang="scss">
.register-page {
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
    justify-content: center;
    padding: 40px 20px;
    overflow-y: auto;
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

    input[type="text"],
    input[type="email"],
    input[type="password"] {
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

  .birth-date-group {
    display: flex;
    gap: 8px;

    .birth-select {
      flex: 1;
      padding: 12px 16px;
      border: 1px solid #d0d0d0;
      border-radius: 4px;
      font-size: 14px;
      outline: none;
      background-color: #ffffff;
      cursor: pointer;

      &:focus {
        border-color: #061da1;
      }
    }
  }

  .gender-group {
    display: flex;
    gap: 8px;

    .gender-button {
      flex: 1;
      padding: 12px;
      background-color: #ffffff;
      color: #666666;
      border: 1px solid #d0d0d0;
      border-radius: 4px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: #061da1;
      }

      &.active {
        background-color: #061da1;
        color: #ffffff;
        border-color: #061da1;
      }
    }
  }

  .input-with-button {
    display: flex;
    gap: 8px;

    input {
      flex: 1;
    }

    .verify-button {
      padding: 12px 20px;
      background-color: #ffffff;
      color: #061da1;
      border: 1px solid #061da1;
      border-radius: 4px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      white-space: nowrap;
      transition: all 0.3s ease;

      &:hover {
        background-color: #061da1;
        color: #ffffff;
      }
    }
  }

  .terms-section {
    margin-bottom: 24px;
    padding: 16px;
    background-color: #f8f8f8;
    border-radius: 4px;

    .term-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;

      &:last-child {
        margin-bottom: 0;
      }

      input[type="checkbox"] {
        width: 18px;
        height: 18px;
        margin: 0;
        margin-right: 8px;
        cursor: pointer;
      }

      label {
        font-size: 14px;
        color: #333333;
        cursor: pointer;
        user-select: none;
        margin: 0;
      }

      &:first-child {
        padding-bottom: 12px;
        border-bottom: 1px solid #e0e0e0;
        margin-bottom: 16px;

        label {
          font-weight: 600;
        }
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

    &:hover {
      background-color: #04166e;
    }

    &:active {
      transform: scale(0.98);
    }
  }
}

@media (max-width: 768px) {
  .register-page {
    .content {
      padding: 24px 16px;
    }
  }
}
</style>
