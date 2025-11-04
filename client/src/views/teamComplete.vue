<template>
  <div class="team-complete-page">
    <!-- Content -->
    <div class="content">
      <div class="complete-container">
        <!-- Team Logo -->
        <div class="team-logo-large">
          <img v-if="teamLogo" :src="teamLogo" :alt="teamName" class="logo-image">
          <div v-else class="logo-placeholder">
            <i class="el-icon-picture"></i>
          </div>
        </div>

        <!-- Team Name -->
        <h1 class="team-name-title">{{ teamName }}</h1>

        <!-- Success Message -->
        <div class="success-message">
          <h2 class="message-title">창단을 축하합니다!</h2>
          <p class="message-text">
            이제 우리팀 초대 링크를 공유하고<br>
            리그에 도전해 보세요!
          </p>
        </div>

        <!-- Action Buttons -->
        <div class="action-buttons">
          <button class="btn-primary" @click="goToTeamInvite">
            팀 페이지 이동하기
          </button>
          <button class="btn-secondary" @click="shareInviteLink">
            초대코드 복사하기
          </button>
        </div>

        <!-- Info Text -->
        <div class="info-text">
          <p>초대 링크가 있으면 승인 없이 바로 팀에 가입할 수 있어요!</p>
          <p>초대 링크는 팀원들에게만 공유하세!</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class TeamCompletePage extends Vue {
  private teamName = ''

  private teamLogo = ''

  private inviteCode = ''

  mounted() {
    this.loadTeamData();
    this.generateInviteCode();
  }

  private loadTeamData(): void {
    // 세션 스토리지에서 팀 정보 가져오기
    const storedTeamData = sessionStorage.getItem('teamFormData');
    if (storedTeamData) {
      const teamData = JSON.parse(storedTeamData);
      this.teamName = teamData.name || '팀';
      this.teamLogo = teamData.logo || '';
    }
  }

  private generateInviteCode(): void {
    // 랜덤 초대 코드 생성 (실제로는 백엔드에서 받아와야 함)
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let result = '';
    for (let i = 0; i < 8; i += 1) {
      result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    this.inviteCode = result;
  }

  private goToTeamInvite(): void {
    // 리그 페이지로 이동
    this.$router.push('/league');
  }

  private async shareInviteLink(): Promise<void> {
    // 초대 코드 복사
    const inviteLink = `https://yourapp.com/join/${this.inviteCode}`;

    try {
      await navigator.clipboard.writeText(inviteLink);
      this.$message.success('초대 링크가 복사되었습니다!');
    } catch (err) {
      // 클립보드 API 지원하지 않는 경우
      const textArea = document.createElement('textarea');
      textArea.value = inviteLink;
      document.body.appendChild(textArea);
      textArea.select();
      try {
        document.execCommand('copy');
        this.$message.success('초대 링크가 복사되었습니다!');
      } catch (e) {
        this.$message.error('복사에 실패했습니다.');
      }
      document.body.removeChild(textArea);
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
