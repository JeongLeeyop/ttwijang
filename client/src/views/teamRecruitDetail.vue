<template>
  <div class="recruit-detail-page">
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i>
    </div>
    <template v-else-if="team">
      <!-- Team Photo -->
      <div v-if="team.teamPhotoFileUid" class="team-photo-section">
        <img
          :src="`/api/attached-file/${team.teamPhotoFileUid}`"
          alt="팀 사진"
          class="team-photo"
        >
      </div>

      <!-- Team Info Header -->
      <div class="detail-header">
        <div class="team-logo-wrap">
          <img
            v-if="team.logoUrl"
            :src="team.logoUrl"
            alt="팀 로고"
            class="team-logo"
          >
          <div v-else class="team-logo-default">
            <i class="el-icon-football"></i>
          </div>
        </div>
        <div class="team-basic-info">
          <h1 class="team-name">{{ team.name }}</h1>
          <div class="team-meta-row">
            <span v-if="team.region" class="meta-label">
              <i class="el-icon-location-outline"></i> {{ team.region }}
            </span>
            <span class="meta-label">
              <i class="el-icon-user"></i> {{ team.memberCount || 0 }}명
            </span>
            <span v-if="team.mannerScore" class="meta-label manner">
              매너 {{ team.mannerScore }}
            </span>
          </div>
        </div>
      </div>

      <!-- Feature Tags -->
      <div v-if="featureTags.length > 0" class="detail-section">
        <h3 class="section-label">팀 특징</h3>
        <div class="tags-wrap">
          <span
            v-for="tag in featureTags"
            :key="tag"
            class="feature-tag"
          >
            {{ tag }}
          </span>
        </div>
      </div>

      <!-- Activity Info -->
      <div class="detail-section">
        <h3 class="section-label">활동 정보</h3>
        <div class="info-grid">
          <div v-if="activeDaysText" class="info-item">
            <span class="info-key">활동 요일</span>
            <span class="info-value">{{ activeDaysText }}</span>
          </div>
          <div v-if="activeTimeSlotsText" class="info-item">
            <span class="info-key">활동 시간</span>
            <span class="info-value">{{ activeTimeSlotsText }}</span>
          </div>
          <div v-if="team.region" class="info-item">
            <span class="info-key">활동 지역</span>
            <span class="info-value">{{ team.region }}</span>
          </div>
          <div v-if="team.monthlyFee != null" class="info-item">
            <span class="info-key">월 회비</span>
            <span class="info-value">{{ team.monthlyFee === 0 ? '무료' : formatNumber(team.monthlyFee) + '원' }}</span>
          </div>
        </div>
      </div>

      <!-- Recruit Target -->
      <div class="detail-section">
        <h3 class="section-label">모집 대상</h3>
        <div class="info-grid">
          <div v-if="genderText" class="info-item">
            <span class="info-key">성별</span>
            <span class="info-value">{{ genderText }}</span>
          </div>
          <div v-if="ageGroupsText" class="info-item">
            <span class="info-key">나이</span>
            <span class="info-value">{{ ageGroupsText }}</span>
          </div>
        </div>
      </div>

      <!-- Description -->
      <div
        v-if="team.recruitmentDescription"
        class="detail-section"
      >
        <h3 class="section-label">추가 사항</h3>
        <div class="description-box">
          {{ team.recruitmentDescription }}
        </div>
      </div>

      <!-- Apply Button -->
      <div class="apply-bar">
        <div v-if="isInvitedJoin" class="invited-notice">
          <i class="el-icon-circle-check"></i>
          <span>초대 링크를 통한 가입입니다. 승인 없이 바로 가입됩니다.</span>
        </div>
        <button
          class="apply-button"
          :disabled="isApplying || hasPendingRequest"
          @click="applyToTeam"
        >
          {{ hasPendingRequest ? '가입 승인 대기중입니다' : (isInvitedJoin ? '바로 가입하기' : '가입 신청하기') }}
        </button>
      </div>
    </template>
    <div v-else class="empty-container">
      <p>팀 정보를 찾을 수 없습니다.</p>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { Message, MessageBox } from 'element-ui';
import {
  getTeamDetail,
  joinTeam,
  joinTeamByCode,
  getTeamByCode,
  joinTeamByInviteCode,
  getTeamByInviteCode,
  checkMembershipStatus,
} from '@/api/team';

@Component
export default class TeamRecruitDetail extends Vue {
  private isLoading = true

  private isApplying = false

  private team: any = null

  private isInvitedJoin = false

  private invitationCode = ''

  private hasPendingRequest = false

  get featureTags(): string[] {
    if (!this.team?.featureTags) return [];
    try {
      return JSON.parse(this.team.featureTags);
    } catch (e) {
      return [];
    }
  }

  /* eslint-disable no-bitwise */
  get activeDaysText(): string {
    const val = this.team?.activeDays;
    if (!val) return '';
    const labels = ['일', '월', '화', '수', '목', '금', '토'];
    const days: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) days.push(label);
    });
    return days.join(', ');
  }

  get activeTimeSlotsText(): string {
    const val = this.team?.activeTimeSlots;
    if (!val) return '';
    const labels = ['아침(6~10시)', '낮(10~18시)', '저녁(18~24시)', '심야(24~6시)'];
    const slots: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) slots.push(label);
    });
    return slots.join(', ');
  }

  get ageGroupsText(): string {
    const val = this.team?.ageGroups;
    if (!val) return '';
    if (val === 31) return '나이 무관';
    const labels = ['10대', '20대', '30대', '40대', '50대 이상'];
    const ages: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) ages.push(label);
    });
    return ages.join(', ');
  }
  /* eslint-enable no-bitwise */

  get genderText(): string {
    const val = this.team?.genderType;
    if (val === 0) return '남자';
    if (val === 1) return '여자';
    if (val === 2) return '남녀무관';
    return '';
  }

  get region(): string {
    if (!this.team) return '';
    const parts = [];
    if (this.team.regionSido) parts.push(this.team.regionSido);
    if (this.team.regionSigungu) parts.push(this.team.regionSigungu);
    return parts.join(' ');
  }

  async created() {
    this.invitationCode = this.$route.query.code as string;
    if (this.invitationCode) {
      this.isInvitedJoin = true;
    }
    await this.fetchTeamDetail();
  }

  private async fetchTeamDetail(): Promise<void> {
    this.isLoading = true;
    try {
      let team;
      if (this.invitationCode) {
        const res = await getTeamByInviteCode(this.invitationCode);
        team = res.data || res;
      } else {
        const uid = this.$route.params.teamUid;
        const res = await getTeamDetail(uid);
        team = res.data || res;
      }
      this.team = team;
      if (!this.invitationCode) {
        try {
          const statusRes = await checkMembershipStatus();
          this.hasPendingRequest = statusRes.data?.hasPendingRequest || false;
        } catch (e) {
          // 상태 조회 실패 무시
        }
      }
    } catch (error) {
      Message.error('팀 정보를 불러오지 못했습니다.');
      console.error('Failed to load team detail:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private formatNumber(n: number): string {
    return n.toLocaleString();
  }

  private async applyToTeam(): Promise<void> {
    const confirmMsg = this.isInvitedJoin ? '이 팀에 바로 가입하시겠습니까?' : '이 팀에 가입 신청을 하시겠습니까?';
    const confirmTitle = this.isInvitedJoin ? '가입하기' : '가입 신청';
    const confirmBtnText = this.isInvitedJoin ? '가입하기' : '신청하기';

    try {
      await MessageBox.confirm(confirmMsg, confirmTitle, {
        confirmButtonText: confirmBtnText,
        cancelButtonText: '취소',
        type: 'info',
      });
    } catch (e) {
      return;
    }

    this.isApplying = true;
    try {
      if (this.isInvitedJoin) {
        await joinTeamByInviteCode(this.invitationCode);
        Message.success('팀에 가입되었습니다!');
        this.$router.push('/team-dashboard');
      } else {
        await joinTeam({
          teamUid: this.team.uid,
          message: '회원 모집을 보고 신청합니다.',
        });
        Message.success('가입 신청이 완료되었습니다!');
        this.hasPendingRequest = true;
      }
    } catch (error: any) {
      const serverMsg = error?.response?.data?.message;
      Message.error(serverMsg || (this.isInvitedJoin ? '팀 가입에 실패했습니다.' : '가입 신청에 실패했습니다.'));
    } finally {
      this.isApplying = false;
    }
  }
}
</script>

<style scoped>
.recruit-detail-page {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  background: #fff;
  padding-bottom: 80px;
  margin-top: 60px;
}

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  font-size: 28px;
  color: #999;
}

.empty-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: #999;
  font-size: 14px;
}

.team-photo-section {
  width: 100%;
  height: 220px;
  overflow: hidden;
  background: #e9ecef;
}

.team-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.team-photo-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #061da1 0%, #1a3dcf 100%);
}

.placeholder-logo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.placeholder-icon {
  font-size: 60px;
  color: rgba(255, 255, 255, 0.3);
}

.detail-header {
  display: flex;
  gap: 14px;
  padding: 20px;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.team-logo-wrap {
  flex-shrink: 0;
}

.team-logo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f0f0f0;
}

.team-logo-default {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #adb5bd;
}

.team-basic-info {
  flex: 1;
  min-width: 0;
}

.team-name {
  text-align: left;
  font-size: 20px;
  font-weight: 700;
  color: #333;
  margin: 0 0 6px;
}

.team-meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  font-size: 13px;
  color: #888;
}

.meta-label i {
  margin-right: 2px;
}

.meta-label.manner {
  color: #061da1;
  font-weight: 600;
}

.detail-section {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.section-label {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  margin: 0 0 12px;
}

.tags-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.feature-tag {
  display: inline-block;
  padding: 6px 14px;
  background: #f0f2ff;
  color: #061da1;
  border-radius: 16px;
  font-size: 13px;
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-item {
  display: flex;
  align-items: flex-start;
}

.info-key {
  flex-shrink: 0;
  width: 80px;
  font-size: 13px;
  color: #999;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.description-box {
  font-size: 14px;
  color: #555;
  line-height: 1.7;
  white-space: pre-wrap;
  background: #f9f9f9;
  padding: 16px;
  border-radius: 10px;
}

.apply-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 20px;
  background: #fff;
  border-top: 1px solid #eee;
  z-index: 100;
}

.apply-button {
  display: block;
  width: 100%;
  padding: 14px;
  background: #061da1;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.apply-button:active {
  background: #0415a0;
}

.apply-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.invited-notice {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  margin-bottom: 12px;
  background: #e8f5e9;
  border: 1px solid #81c784;
  border-radius: 8px;
  color: #2e7d32;
  font-size: 13px;
  font-weight: 500;
}

.invited-notice i {
  font-size: 16px;
  flex-shrink: 0;
}
</style>
