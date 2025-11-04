<template>
  <div class="create-team-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

    <!-- Content -->
    <div class="content">
      <div class="form-container">
        <h2 class="page-title">팀 이름을 작성해 주세요!</h2>

        <!-- Team Name -->
        <div class="form-group">
          <label>팀 이름</label>
          <input
            v-model="teamName"
            type="text"
            placeholder="팀 이름을 입력하세요."
            class="form-input"
          >
        </div>

        <!-- Team Code -->
        <div class="form-group">
          <label>팀 코드</label>
          <input
            v-model="teamCode"
            type="text"
            placeholder="팀 코드를 입력하세요."
            class="form-input"
          >
          <p class="form-hint">팀 코드는 팀 페이지 URL 주소로 사용됩니다.</p>
        </div>

        <!-- Sponsor Account - Bank Name -->
        <div class="form-group">
          <label>팀 후원 계좌번호</label>
          <input
            v-model="sponsorAccount.bank"
            type="text"
            placeholder="은행명 (예: 국민은행, 신한은행)"
            class="form-input"
          >
        </div>

        <!-- Sponsor Account - Account Number -->
        <div class="form-group">
          <input
            v-model="sponsorAccount.number"
            type="text"
            placeholder="계좌번호"
            class="form-input"
          >
        </div>

        <!-- Submit Button -->
        <button class="submit-button" @click="submitForm">다 음</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class CreateTeam extends Vue {
  private teamName = ''

  private teamCode = ''

  private sponsorAccount = {
    bank: '',
    number: '',
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private submitForm(): void {
    if (!this.teamName.trim()) {
      this.$message.warning('팀 이름을 입력해주세요.');
      return;
    }
    if (!this.teamCode.trim()) {
      this.$message.warning('팀 코드를 입력해주세요.');
      return;
    }
    if (!this.sponsorAccount.bank.trim()) {
      this.$message.warning('은행명을 입력해주세요.');
      return;
    }
    if (!this.sponsorAccount.number.trim()) {
      this.$message.warning('계좌번호를 입력해주세요.');
      return;
    }
    const teamData = {
      name: this.teamName,
      code: this.teamCode,
      sponsorBank: this.sponsorAccount.bank,
      sponsorAccount: this.sponsorAccount.number,
    };

    // 세션 스토리지에 저장
    sessionStorage.setItem('teamFormData', JSON.stringify(teamData));

    console.log('Creating team:', teamData);
    this.$message.success('팀 정보가 저장되었습니다!');
    this.$router.push('/upload-team-mark');
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
