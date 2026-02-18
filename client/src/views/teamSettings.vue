<template>
  <div class="team-settings-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

    <!-- Content -->
    <div class="content">
      <div class="form-container">
        <!-- Team Info Header -->
        <div class="team-info-header">
          <div class="team-logo-wrap">
            <img
              :src="teamInfo.logoUrl || defaultLogo"
              :alt="teamInfo.name"
              class="team-logo-img"
            >
          </div>
          <div class="team-name-text">{{ teamInfo.name || '팀 이름' }}</div>
        </div>

        <!-- Menu List -->
        <div class="settings-menu-list">
          <!-- 프로필 수정 -->
          <div class="settings-menu-item" @click="goToProfileEdit">
            <span class="menu-label">프로필 수정</span>
            <i class="el-icon-arrow-right menu-arrow"></i>
          </div>

          <!-- 가입 승인 대기 -->
          <div class="settings-menu-item" @click="goToPendingManage">
            <div class="menu-label-row">
              <span class="menu-label">가입 승인 대기</span>
              <span v-if="pendingCount > 0" class="pending-badge">{{ pendingCount }}</span>
              <i class="el-icon-arrow-right menu-arrow"></i>
            </div>
          </div>

          <!-- 환불 계좌 -->
          <div class="settings-menu-item" @click="showPreparing">
            <div class="menu-label-row">
              <span class="menu-label">환불 계좌</span>
              <span class="menu-tag tag-info" @click.stop="showPreparing">정보수정</span>
            </div>
          </div>

          <!-- 팀 운영자 위임 -->
          <div class="settings-menu-item" @click="showPreparing">
            <div class="menu-label-row">
              <span class="menu-label">팀 운영자 위임</span>
              <span class="menu-tag tag-select" @click.stop="showPreparing">위임자 선택</span>
            </div>
          </div>
        </div>

        <!-- 팀 삭제하기 -->
        <div class="danger-section">
          <span class="danger-link" @click="showPreparing">팀 삭제하기</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getTeamDetail, getMyTeams, getPendingRequests } from '@/api/team';

@Component
export default class TeamSettings extends Vue {
  private teamUid = ''

  private teamInfo: any = {}

  private pendingCount = 0

  get defaultLogo(): string {
    return 'https://ui-avatars.com/api/?name=T&background=061da1&color=fff&size=60';
  }

  async created(): Promise<void> {
    this.teamUid = (this.$route.query.teamUid as string) || '';
    if (!this.teamUid) {
      try {
        const res = await getMyTeams();
        const team = Array.isArray(res.data) ? res.data[0] : res.data;
        if (team && team.uid) {
          this.teamUid = team.uid;
        }
      } catch (e) {
        console.warn('내 팀 조회 실패:', e);
      }
    }
    if (this.teamUid) {
      await this.loadTeamInfo();
      await this.loadPendingCount();
    }
  }

  private async loadTeamInfo(): Promise<void> {
    try {
      const res = await getTeamDetail(this.teamUid);
      this.teamInfo = res.data || {};
    } catch (error) {
      console.error('팀 정보 로드 실패:', error);
    }
  }

  private async loadPendingCount(): Promise<void> {
    try {
      const res = await getPendingRequests(this.teamUid);
      const list = res.data;
      this.pendingCount = Array.isArray(list) ? list.length : 0;
    } catch (error) {
      console.warn('대기 목록 조회 실패:', error);
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private goToProfileEdit(): void {
    this.$router.push({
      path: '/team-profile-edit',
      query: { teamUid: this.teamUid },
    });
  }

  private goToPendingManage(): void {
    this.$router.push({
      path: '/pending-manage',
      query: { teamUid: this.teamUid },
    });
  }

  private showPreparing(): void {
    this.$message.info('준비 중인 기능입니다.');
  }
}
</script>

<style scoped>
.team-settings-page {
  min-height: 100vh;
  background: #f5f6fa;
}

.header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.back-button {
  background: none;
  border: none;
  font-size: 22px;
  color: #333;
  cursor: pointer;
  padding: 4px;
}

.content {
  padding: 16px;
}

.form-container {
  max-width: 500px;
  margin: 0 auto;
}

/* Team Info Header */
.team-info-header {
  display: flex;
  align-items: center;
  gap: 14px;
  background: #fff;
  border-radius: 12px;
  padding: 20px 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.team-logo-wrap {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid #eee;
}

.team-logo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.team-name-text {
  font-size: 18px;
  font-weight: 800;
  color: #222;
  letter-spacing: -0.3px;
}

/* Settings Menu */
.settings-menu-list {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
}

.settings-menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.15s;
}

.settings-menu-item:last-child {
  border-bottom: none;
}

.settings-menu-item:active {
  background: #f9f9f9;
}

.menu-label {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.menu-label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.menu-arrow {
  color: #ccc;
  font-size: 16px;
}

.pending-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  border-radius: 10px;
  background: #ff4757;
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  margin-left: 8px;
}

.menu-tag {
  padding: 4px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
}

.tag-info {
  background: #4CAF50;
  color: #fff;
}

.tag-select {
  background: #FF9800;
  color: #fff;
}

/* Danger Section */
.danger-section {
  text-align: center;
  padding: 16px 0;
}

.danger-link {
  font-size: 14px;
  color: #ff4757;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.15s;
}

.danger-link:active {
  opacity: 0.7;
}
</style>
