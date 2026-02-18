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
import {
  createTeam,
  CreateTeamRequest,
  checkMembershipStatus,
  MembershipStatus,
} from '@/api/team';

@Component
export default class TeamCompletePage extends Vue {
  private teamName = ''

  private teamLogo = ''

  private inviteCode = ''

  private teamUid = ''

  private isLoading = false

  async mounted() {
    this.loadTeamData();
    await this.submitTeamCreation();
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

  private async submitTeamCreation(): Promise<void> {
    try {
      this.isLoading = true;

      // BR-01: 팀 생성 전 최종 검증
      const statusResponse = await checkMembershipStatus();
      const status = statusResponse.data as MembershipStatus;
      if (!status.canCreateTeam) {
        this.$message.error('이미 팀을 생성하였거나 소속된 팀이 있습니다.');
        sessionStorage.removeItem('teamFormData');
        sessionStorage.removeItem('teamInfoData');
        sessionStorage.removeItem('teamLocationData');
        this.$router.replace('/match');
        return;
      }

      // 세션 스토리지에서 데이터 수집
      const teamFormData = JSON.parse(sessionStorage.getItem('teamFormData') || '{}');
      const teamInfoData = JSON.parse(sessionStorage.getItem('teamInfoData') || '{}');
      const teamLocationData = JSON.parse(sessionStorage.getItem('teamLocationData') || '{}');

      // 나이대 비트마스크 변환
      const ageGroupMap: { [key: string]: number } = {
        '10s': 1, '20s': 2, '30s': 4, '40s': 8, '50s': 16, '60s': 32,
      };
      let ageGroups = 0;
      if (teamInfoData.ageRanges) {
        teamInfoData.ageRanges.forEach((age: string) => {
          ageGroups += ageGroupMap[age] || 0;
        });
      }

      // 성별 변환
      const genderMap: { [key: string]: number } = {
        male: 0, female: 1, mixed: 2,
      };

      // 활동 요일 비트마스크 변환
      const dayBitmask: Record<string, number> = {
        mon: 1, tue: 2, wed: 4, thu: 8, fri: 16, sat: 32, sun: 64,
      };
      let activeDays = 0;
      if (teamInfoData.activeDays) {
        teamInfoData.activeDays.forEach((day: string) => {
          activeDays += dayBitmask[day] || 0;
        });
      }

      // 활동 시간 비트마스크 변환
      const timeBitmask: Record<string, number> = {
        dawn: 1, morning: 2, afternoon: 4, evening: 8, night: 16,
      };
      let activeTimeSlots = 0;
      if (teamInfoData.activeTimes) {
        teamInfoData.activeTimes.forEach((time: string) => {
          activeTimeSlots += timeBitmask[time] || 0;
        });
      }

      const createRequest: CreateTeamRequest = {
        name: teamFormData.name,
        teamCode: teamFormData.code,
        logoFileUid: teamFormData.logoFileUid,
        activeDays: activeDays || undefined,
        activeTimeSlots: activeTimeSlots || undefined,
        regionSido: teamLocationData.city,
        regionSigungu: teamLocationData.district,
        homeStadium: teamLocationData.stadiumName,
        genderType: genderMap[teamInfoData.gender] ?? 2,
        ageGroups,
      };

      const response = await createTeam(createRequest);
      this.teamUid = response.data.uid;
      this.inviteCode = response.data.teamCode;

      // 세션 스토리지 정리
      sessionStorage.removeItem('teamFormData');
      sessionStorage.removeItem('teamInfoData');
      sessionStorage.removeItem('teamLocationData');
    } catch (error: any) {
      console.error('Team creation failed:', error);
      this.$message.error(error.response?.data?.message || '팀 생성에 실패했습니다.');
      this.$router.push('/create-team');
    } finally {
      this.isLoading = false;
    }
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
