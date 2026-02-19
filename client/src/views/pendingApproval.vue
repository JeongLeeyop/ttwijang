<template>
  <div class="pending-approval-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
      <h1 class="header-title">가입 신청 현황</h1>
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i>
      <p>불러오는 중...</p>
    </div>

    <!-- No Pending -->
    <div v-else-if="!pendingInfo" class="empty-container">
      <div class="empty-icon">
        <i class="el-icon-check"></i>
      </div>
      <h2 class="empty-title">대기 중인 신청이 없습니다</h2>
      <p class="empty-desc">팀에 가입 신청하면 여기에서 상태를 확인할 수 있어요.</p>
      <button class="btn-primary" @click="goToMatch">
        팀 찾아보기
      </button>
    </div>

    <!-- Pending Info -->
    <div v-else class="content" style="margin-top:60px;">
      <!-- Status Badge -->
      <div class="status-section">
        <div class="status-badge">
          <i class="el-icon-time"></i>
          <span>승인 대기중</span>
        </div>
        <p class="status-desc">
          팀 운영자가 가입 신청을 확인하고 있습니다.<br>
          승인되면 자동으로 팀에 합류됩니다.
        </p>
      </div>

      <!-- Team Card -->
      <div class="team-card">
        <div class="team-card-header">
          <div class="team-logo-wrap">
            <img
              :src="pendingInfo.teamLogoUrl || defaultLogo"
              :alt="pendingInfo.teamName"
              class="team-logo-img"
            >
          </div>
          <div class="team-info">
            <h3 class="team-name">{{ pendingInfo.teamName }}</h3>
            <div class="team-meta">
              <span v-if="pendingInfo.region" class="meta-item">
                <i class="el-icon-location-outline"></i>
                {{ pendingInfo.region }}
              </span>
              <span v-if="pendingInfo.memberCount" class="meta-item">
                <i class="el-icon-user"></i>
                {{ pendingInfo.memberCount }}명
              </span>
            </div>
          </div>
        </div>

        <div class="team-card-body">
          <div class="info-row">
            <span class="info-label">운영자</span>
            <span class="info-value">{{ pendingInfo.ownerName || '-' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">팀 코드</span>
            <span class="info-value code">{{ pendingInfo.teamCode }}</span>
          </div>
          <div v-if="pendingInfo.position" class="info-row">
            <span class="info-label">신청 포지션</span>
            <span class="info-value">{{ pendingInfo.position }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">신청일</span>
            <span class="info-value">{{ formatDate(pendingInfo.appliedDate) }}</span>
          </div>
        </div>
      </div>

      <!-- Timeline -->
      <div class="timeline-section">
        <h3 class="section-title">진행 상황</h3>
        <div class="timeline">
          <div class="timeline-item done">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <span class="timeline-label">가입 신청 완료</span>
              <span class="timeline-date">{{ formatDate(pendingInfo.appliedDate) }}</span>
            </div>
          </div>
          <div class="timeline-item active">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <span class="timeline-label">운영자 승인 대기 중</span>
              <span class="timeline-date">검토 중...</span>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <span class="timeline-label">팀 합류</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Cancel Button -->
      <div class="cancel-section">
        <button
          class="btn-cancel"
          :disabled="isCancelling"
          @click="confirmCancel"
        >
          {{ isCancelling ? '취소 중...' : '가입 신청 취소' }}
        </button>
        <p class="cancel-hint">
          신청을 취소하면 다른 팀에 가입할 수 있습니다.
        </p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import {
  getMyPendingInfo, cancelJoinRequest, PendingInfo,
} from '@/api/team';

@Component
export default class PendingApproval extends Vue {
  private isLoading = false

  private isCancelling = false

  private pendingInfo: PendingInfo | null = null

  get defaultLogo(): string {
    return 'https://ui-avatars.com/api/?name=T&background=061da1&color=fff&size=60';
  }

  async created(): Promise<void> {
    await this.loadPendingInfo();
  }

  private async loadPendingInfo(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getMyPendingInfo();
      this.pendingInfo = res.data || null;
    } catch (error) {
      console.error('가입 대기 정보 조회 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hour = String(date.getHours()).padStart(2, '0');
    const minute = String(date.getMinutes()).padStart(2, '0');
    return `${year}.${month}.${day} ${hour}:${minute}`;
  }

  private confirmCancel(): void {
    this.$confirm(
      '가입 신청을 정말 취소하시겠습니까?',
      '신청 취소',
      {
        confirmButtonText: '취소하기',
        cancelButtonText: '돌아가기',
        type: 'warning',
      },
    ).then(() => {
      this.handleCancel();
    }).catch(() => {
      // 사용자가 '돌아가기' 클릭
    });
  }

  private async handleCancel(): Promise<void> {
    this.isCancelling = true;
    try {
      await cancelJoinRequest();
      this.$message.success('가입 신청이 취소되었습니다.');
      this.$router.replace('/match');
    } catch (error) {
      console.error('가입 신청 취소 실패:', error);
      this.$message.error('가입 신청 취소에 실패했습니다.');
    } finally {
      this.isCancelling = false;
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private goToMatch(): void {
    this.$router.push('/match');
  }
}
</script>

<style scoped>
.pending-approval-page {
  min-height: 100vh;
  background: #f5f6fa;
}

.header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
  gap: 8px;
}

.back-button {
  background: none;
  border: none;
  font-size: 22px;
  color: #333;
  cursor: pointer;
  padding: 4px;
}

.header-title {
  font-size: 17px;
  font-weight: 700;
  color: #222;
  margin: 0;
}

/* Loading */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: #999;
}

.loading-container i {
  font-size: 32px;
  margin-bottom: 12px;
}

/* Empty */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: #e8f5e9;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.empty-icon i {
  font-size: 36px;
  color: #4CAF50;
}

.empty-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  margin: 0 0 8px;
}

.empty-desc {
  font-size: 14px;
  color: #888;
  margin: 0 0 24px;
  line-height: 1.5;
}

.btn-primary {
  padding: 12px 32px;
  background: #061da1;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
}

.btn-primary:active {
  opacity: 0.85;
}

/* Content */
.content {
  padding: 16px;
  max-width: 500px;
  margin: 0 auto;
}

/* Status Section */
.status-section {
  text-align: center;
  width: 100%;
  padding: 24px 16px;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 20px;
  background: #FFF3E0;
  color: #E65100;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 12px;
}

.status-badge i {
  font-size: 16px;
}

.status-desc {
  font-size: 13px;
  color: #888;
  line-height: 1.6;
  margin: 0;
}

/* Team Card */
.team-card {
  width: 100%;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.team-card-header {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.team-logo-wrap {
  width: 56px;
  height: 56px;
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

.team-info {
  flex: 1;
  min-width: 0;
}

.team-name {
  font-size: 17px;
  font-weight: 800;
  color: #222;
  margin: 0 0 4px;
  letter-spacing: -0.3px;
}

.team-meta {
  display: flex;
  gap: 12px;
}

.meta-item {
  font-size: 12px;
  color: #888;
  display: flex;
  align-items: center;
  gap: 3px;
}

.meta-item i {
  font-size: 12px;
}

.team-card-body {
  padding: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f8f8f8;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 13px;
  color: #888;
  font-weight: 500;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 600;
}

.info-value.code {
  font-family: 'Courier New', monospace;
  background: #f5f5f5;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 13px;
  letter-spacing: 1px;
}

/* Timeline */
.timeline-section {
  width: 100%;
  background: #fff;
  border-radius: 12px;
  padding: 20px 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  margin: 0 0 16px;
}

.timeline {
  position: relative;
  padding-left: 24px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 7px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: #e0e0e0;
}

.timeline-item {
  position: relative;
  padding-bottom: 20px;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -20px;
  top: 4px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #e0e0e0;
  border: 2px solid #fff;
  z-index: 1;
}

.timeline-item.done .timeline-dot {
  background: #4CAF50;
}

.timeline-item.active .timeline-dot {
  background: #FF9800;
  box-shadow: 0 0 0 4px rgba(255, 152, 0, 0.2);
}

.timeline-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timeline-label {
  font-size: 14px;
  color: #333;
  font-weight: 600;
}

.timeline-item:not(.done):not(.active) .timeline-label {
  color: #bbb;
}

.timeline-date {
  font-size: 12px;
  color: #999;
}

.timeline-item.active .timeline-date {
  color: #FF9800;
  font-weight: 600;
}

/* Cancel */
.cancel-section {
  text-align: center;
  padding: 8px 0 40px;
}

.btn-cancel {
  width: 100%;
  padding: 14px;
  background: #fff;
  color: #ff4757;
  border: 1.5px solid #ff4757;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:active {
  background: #fff5f5;
}

.btn-cancel:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.cancel-hint {
  font-size: 12px;
  color: #999;
  margin: 10px 0 0;
}
</style>
