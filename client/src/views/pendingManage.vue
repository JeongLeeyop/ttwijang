<template>
  <div class="pending-manage-page">
    <!-- Header -->
    <!-- <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
      <h1 class="header-title">가입 승인 대기</h1>
    </div> -->

    <!-- Loading -->
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i>
      <p>불러오는 중...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="pendingList.length === 0" class="empty-container">
      <div class="empty-icon">
        <i class="el-icon-folder-checked"></i>
      </div>
      <h2 class="empty-title">대기 중인 신청이 없습니다</h2>
      <p class="empty-desc">새로운 가입 신청이 들어오면 여기에 표시됩니다.</p>
    </div>

    <!-- Pending List -->
    <div v-else class="content">
      <div class="pending-summary">
        <span class="summary-count">{{ pendingList.length }}건</span>
        <span class="summary-label">의 가입 신청이 있습니다</span>
      </div>

      <div class="pending-list">
        <div
          v-for="member in pendingList"
          :key="member.uid"
          class="pending-card"
        >
          <!-- Profile Section -->
          <div class="card-profile">
            <div class="profile-avatar">
              <img
                :src="member.profileImageUrl || defaultAvatar(member.userName)"
                :alt="member.userName"
                class="avatar-img"
              >
            </div>
            <div class="profile-info">
              <div class="profile-name">{{ member.userName || '이름 없음' }}</div>
              <div class="profile-meta">
                <span v-if="member.applicationAge" class="meta-tag">
                  {{ member.applicationAge }}세
                </span>
                <span v-if="genderLabel(member.gender)" class="meta-tag">
                  {{ genderLabel(member.gender) }}
                </span>
                <span v-if="member.position" class="meta-tag">
                  {{ member.position }}
                </span>
              </div>
            </div>
            <div class="profile-date">
              {{ formatRelativeDate(member.createdDate) }}
            </div>
          </div>

          <!-- Application Info -->
          <div class="card-details">
            <div v-if="member.applicationRegion" class="detail-row">
              <i class="el-icon-location-outline detail-icon"></i>
              <span class="detail-label">지역</span>
              <span class="detail-value">{{ member.applicationRegion }}</span>
            </div>
            <div v-if="member.applicationExperience" class="detail-row">
              <i class="el-icon-trophy detail-icon"></i>
              <span class="detail-label">실력/경력</span>
              <span class="detail-value">{{ member.applicationExperience }}</span>
            </div>
            <div class="detail-row">
              <i class="el-icon-time detail-icon"></i>
              <span class="detail-label">신청일시</span>
              <span class="detail-value">{{ formatDate(member.createdDate) }}</span>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="card-actions">
            <button
              class="btn-reject"
              :disabled="processingUid === member.uid"
              @click="handleReject(member)"
            >
              <i class="el-icon-close"></i>
              거절
            </button>
            <button
              class="btn-approve"
              :disabled="processingUid === member.uid"
              @click="handleApprove(member)"
            >
              <i class="el-icon-check"></i>
              승인
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import {
  getPendingRequests, processJoinRequest, getMyTeams,
} from '@/api/team';

interface PendingMember {
  uid: string
  userUid: string
  userName: string
  profileImageUrl: string | null
  role: string
  position: string | null
  status: string
  createdDate: string
  gender: number | null
  birth: string | null
  mannerScore: number | null
  applicationRegion: string | null
  applicationAge: number | null
  applicationExperience: string | null
}

@Component
export default class PendingManage extends Vue {
  private isLoading = false

  private processingUid = ''

  private teamUid = ''

  private pendingList: PendingMember[] = []

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
      await this.loadPendingList();
    } else {
      this.$message.error('팀 정보를 찾을 수 없습니다.');
    }
  }

  private async loadPendingList(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getPendingRequests(this.teamUid);
      this.pendingList = Array.isArray(res.data) ? res.data : [];
    } catch (error) {
      console.error('대기 목록 조회 실패:', error);
      this.$message.error('가입 대기 목록을 불러오지 못했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private defaultAvatar(name: string): string {
    const initial = name ? name.charAt(0) : '?';
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initial)}&background=4a90d9&color=fff&size=48`;
  }

  private genderLabel(gender: number | null): string {
    if (gender === 0) return '남성';
    if (gender === 1) return '여성';
    return '';
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

  private formatRelativeDate(dateStr: string): string {
    if (!dateStr) return '';
    const now = new Date();
    const date = new Date(dateStr);
    const diffMs = now.getTime() - date.getTime();
    const diffMin = Math.floor(diffMs / 60000);
    const diffHour = Math.floor(diffMs / 3600000);
    const diffDay = Math.floor(diffMs / 86400000);
    if (diffMin < 1) return '방금 전';
    if (diffMin < 60) return `${diffMin}분 전`;
    if (diffHour < 24) return `${diffHour}시간 전`;
    if (diffDay < 7) return `${diffDay}일 전`;
    return this.formatDate(dateStr);
  }

  private handleApprove(member: PendingMember): void {
    this.$confirm(
      `${member.userName}님의 가입 신청을 승인하시겠습니까?`,
      '가입 승인',
      {
        confirmButtonText: '승인',
        cancelButtonText: '취소',
        type: 'info',
      },
    ).then(() => {
      this.processRequest(member.uid, true);
    }).catch(() => {
      // 취소
    });
  }

  private handleReject(member: PendingMember): void {
    this.$confirm(
      `${member.userName}님의 가입 신청을 거절하시겠습니까?`,
      '가입 거절',
      {
        confirmButtonText: '거절',
        cancelButtonText: '취소',
        type: 'warning',
        confirmButtonClass: 'el-button--danger',
      },
    ).then(() => {
      this.processRequest(member.uid, false);
    }).catch(() => {
      // 취소
    });
  }

  private async processRequest(memberUid: string, approved: boolean): Promise<void> {
    this.processingUid = memberUid;
    try {
      await processJoinRequest({
        memberUid,
        approved,
      });
      const action = approved ? '승인' : '거절';
      this.$message.success(`가입 신청이 ${action}되었습니다.`);
      this.pendingList = this.pendingList.filter((m) => m.uid !== memberUid);
    } catch (error) {
      console.error('가입 신청 처리 실패:', error);
      this.$message.error('처리 중 오류가 발생했습니다.');
    } finally {
      this.processingUid = '';
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }
}
</script>

<style scoped>
.pending-manage-page {
  min-height: 100vh;
  background: #f5f6fa;
}

/* Header */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
  z-index: 100;
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
  margin-left: 8px;
}

/* Loading */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
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
  min-height: 60vh;
  padding: 40px 20px;
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
  font-size: 32px;
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
  color: #999;
  margin: 0;
  line-height: 1.5;
}

/* Content */
.content {
  padding: 70px 16px 24px;
  max-width: 500px;
  margin: 0 auto;
}

/* Summary */
.pending-summary {
  width:100%;
  background: #fff;
  border-radius: 10px;
  padding: 14px 16px;
  margin-bottom: 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.summary-count {
  font-size: 18px;
  font-weight: 800;
  color: #061da1;
}

.summary-label {
  font-size: 14px;
  color: #666;
  margin-left: 4px;
}

/* Pending List */
.pending-list {
  width:100%;
  padding:0 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* Pending Card */
.pending-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.card-profile {
  display: flex;
  align-items: center;
  padding: 16px 16px 12px;
  gap: 12px;
}

.profile-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid #eee;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  flex: 1;
  min-width: 0;
}

.profile-name {
  font-size: 15px;
  font-weight: 700;
  color: #222;
  margin-bottom: 4px;
}

.profile-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.meta-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  background: #f0f2f5;
  color: #555;
  font-size: 12px;
  font-weight: 500;
}

.profile-date {
  font-size: 12px;
  color: #bbb;
  flex-shrink: 0;
  align-self: flex-start;
  margin-top: 2px;
}

/* Card Details */
.card-details {
  padding: 0 16px 12px;
  border-top: 1px solid #f5f5f5;
  margin-top: 0;
}

.detail-row {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #fafafa;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-icon {
  font-size: 14px;
  color: #aaa;
  margin-right: 8px;
  flex-shrink: 0;
}

.detail-label {
  font-size: 13px;
  color: #999;
  min-width: 68px;
  flex-shrink: 0;
}

.detail-value {
  font-size: 13px;
  color: #333;
  font-weight: 500;
}

/* Action Buttons */
.card-actions {
  display: flex;
  border-top: 1px solid #f0f0f0;
}

.btn-reject,
.btn-approve {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 14px 0;
  border: none;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s, opacity 0.15s;
}

.btn-reject {
  background: #f9f9f9;
  color: #ff4757;
  border-right: 1px solid #f0f0f0;
}

.btn-reject:active {
  background: #fff0f0;
}

.btn-reject:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-approve {
  background: #f0f7ff;
  color: #061da1;
}

.btn-approve:active {
  background: #ddeeff;
}

.btn-approve:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-reject i,
.btn-approve i {
  font-size: 14px;
}
</style>
