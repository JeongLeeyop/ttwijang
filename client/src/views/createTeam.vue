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
import { checkMembershipStatus, MembershipStatus, checkTeamCode } from '@/api/team';

@Component
export default class CreateTeam extends Vue {
  private teamName = ''

  private teamCode = ''

  private sponsorAccount = {
    bank: '',
    number: '',
  }

  private isCheckingStatus = true

  async created(): Promise<void> {
    // BR-01: 팀 생성 전 가능 여부 확인
    try {
      const response = await checkMembershipStatus();
      const status = response.data as MembershipStatus;

      if (!status.canCreateTeam) {
        this.$message.warning('이미 팀을 생성하였거나 소속된 팀이 있습니다. 1계정 1팀만 가능합니다.');
        this.$router.replace('/match');
        return;
      }
    } catch (error) {
      console.warn('Membership status check failed:', error);
    } finally {
      this.isCheckingStatus = false;
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private async submitForm(): Promise<void> {
    if (!this.teamName.trim()) {
      this.$message.warning('팀 이름을 입력해주세요.');
      return;
    }
    if (!this.teamCode.trim()) {
      this.$message.warning('팀 코드를 입력해주세요.');
      return;
    }

    // 팀 코드 중복 체크
    try {
      const codeRes = await checkTeamCode(this.teamCode);
      const isAvailable = codeRes.data;
      if (!isAvailable) {
        this.$message.error('이미 사용 중인 팀 코드입니다. 다른 코드를 입력해주세요.');
        return;
      }
    } catch (error) {
      this.$message.error('팀 코드 확인 중 오류가 발생했습니다.');
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
