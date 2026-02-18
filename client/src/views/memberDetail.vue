<template>
  <div class="main member-detail-page">
    <div class="background-wave"></div>
    <div class="content">
      <!-- Back Button -->
      <div class="detail-top-bar">
        <button class="back-btn" @click="goBack">
          <i class="el-icon-arrow-left"></i>
        </button>
        <span class="top-bar-title">회원 상세</span>
        <div class="top-bar-right"></div>
      </div>

      <!-- Loading -->
      <div v-if="isLoading" class="detail-loading">
        <i class="el-icon-loading"></i>
        <p>로딩 중...</p>
      </div>

      <!-- Content -->
      <div v-else-if="memberInfo" class="detail-content">
        <!-- Profile Section -->
        <div class="profile-section">
          <div class="profile-avatar-wrap">
            <img
              :src="memberInfo.profileImageUrl || defaultAvatar"
              :alt="memberInfo.userName"
              class="profile-avatar"
            >
          </div>
          <h2 class="profile-name">{{ memberInfo.userName || '이름 없음' }}</h2>
          <span
            class="profile-role-badge"
            :class="roleBadgeClass"
          >{{ roleLabel }}</span>
        </div>

        <!-- Info Card -->
        <div class="info-card">
          <div class="info-card-header">
            <span class="info-card-title">기본 정보</span>
          </div>
          <div class="info-row">
            <span class="info-label">성별</span>
            <span class="info-value">{{ genderLabel }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">나이</span>
            <span class="info-value">{{ ageLabel }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">생년월일</span>
            <span class="info-value">{{ birthLabel }}</span>
          </div>
        </div>

        <!-- Team Info Card -->
        <div class="info-card">
          <div class="info-card-header">
            <span class="info-card-title">팀 정보</span>
          </div>
          <div class="info-row">
            <span class="info-label">역할</span>
            <span class="info-value">{{ roleLabel }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">포지션</span>
            <span class="info-value">{{ memberInfo.position || '-' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">등번호</span>
            <span class="info-value">{{ memberInfo.backNumber != null ? memberInfo.backNumber : '-' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">가입일</span>
            <span class="info-value">{{ joinDateLabel }}</span>
          </div>
        </div>

        <!-- Manner Score Card -->
        <div class="info-card manner-card">
          <div class="info-card-header">
            <span class="info-card-title">매너점수</span>
          </div>
          <div class="manner-score-display">
            <span class="manner-score-value">{{ memberInfo.mannerScore || 0 }}</span>
            <span class="manner-score-unit">점</span>
          </div>
        </div>
      </div>

      <!-- Not Found -->
      <div v-else class="detail-empty">
        <i class="el-icon-warning-outline"></i>
        <p>회원 정보를 찾을 수 없습니다.</p>
        <button class="go-back-btn" @click="goBack">돌아가기</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';
import { getTeamMembers } from '@/api/team';

@Component
export default class MemberDetail extends Vue {
  private isLoading = false

  private memberInfo: any = null

  private teamUid = ''

  private memberUid = ''

  private defaultAvatar = 'https://ui-avatars.com/api/?name=U&background=ccc&color=fff&size=80'

  get roleBadgeClass(): string {
    if (!this.memberInfo) return '';
    const classMap: Record<string, string> = {
      OWNER: 'badge-owner',
      MANAGER: 'badge-manager',
      MEMBER: 'badge-member',
    };
    return classMap[this.memberInfo.role] || 'badge-member';
  }

  get roleLabel(): string {
    if (!this.memberInfo) return '-';
    const labelMap: Record<string, string> = {
      OWNER: '운영자',
      MANAGER: '매니저',
      MEMBER: '구단원',
    };
    return labelMap[this.memberInfo.role] || '구단원';
  }

  get genderLabel(): string {
    if (!this.memberInfo || this.memberInfo.gender == null) return '-';
    return this.memberInfo.gender === 0 ? '남자' : '여자';
  }

  get ageLabel(): string {
    if (!this.memberInfo || !this.memberInfo.birth) return '-';
    const today = new Date();
    const birth = new Date(this.memberInfo.birth);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age -= 1;
    }
    return `${age}세`;
  }

  get birthLabel(): string {
    if (!this.memberInfo || !this.memberInfo.birth) return '-';
    return this.memberInfo.birth;
  }

  get joinDateLabel(): string {
    if (!this.memberInfo || !this.memberInfo.createdDate) return '-';
    const d = new Date(this.memberInfo.createdDate);
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}.${m}.${day}`;
  }

  async created() {
    this.memberUid = this.$route.params.memberUid || '';
    this.teamUid = (this.$route.query.teamUid as string) || '';
    if (this.memberUid && this.teamUid) {
      await this.loadMemberDetail();
    }
  }

  private async loadMemberDetail(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getTeamMembers(this.teamUid);
      const members: any[] = res.data || [];
      this.memberInfo = members.find(
        (m: any) => m.uid === this.memberUid,
      ) || null;
    } catch (error) {
      console.error('회원 정보 로드 실패:', error);
      this.memberInfo = null;
    } finally {
      this.isLoading = false;
    }
  }

  private goBack(): void {
    if (window.history.length > 1) {
      this.$router.go(-1);
    } else if (this.teamUid) {
      this.$router.push(`/team?teamUid=${this.teamUid}`);
    } else {
      this.$router.push('/');
    }
  }
}
</script>

<style scoped lang="scss">
.member-detail-page {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 80px;
}

.background-wave {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #1a73e8 0%, #4a90d9 100%);
  border-radius: 0 0 30px 30px;
  z-index: 0;
}

.content {
  position: relative;
  z-index: 1;
  padding: 0 16px;
}

/* Top Bar */
.detail-top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0 12px;
}

.back-btn {
  background: none;
  border: none;
  color: #fff;
  font-size: 22px;
  cursor: pointer;
  padding: 4px 8px;
}

.top-bar-title {
  color: #fff;
  font-size: 17px;
  font-weight: 600;
}

.top-bar-right {
  width: 38px;
}

/* Loading */
.detail-loading {
  text-align: center;
  padding: 80px 0;
  color: #fff;

  i {
    font-size: 32px;
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* Profile Section */
.profile-section {
  text-align: center;
  padding: 20px 0 24px;
}

.profile-avatar-wrap {
  width: 80px;
  height: 80px;
  margin: 0 auto 12px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid rgba(255, 255, 255, 0.5);
  background: #fff;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-name {
  //color: #fff;
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 8px;
}

.profile-role-badge {
  display: inline-block;
  padding: 4px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;

  &.badge-owner {
    background: #ff9800;
  }

  &.badge-manager {
    background: #4caf50;
  }

  &.badge-member {
    background: rgba(255, 255, 255, 0.3);
  }
}

/* Info Cards */
.info-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.info-card-header {
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.info-card-title {
  font-size: 15px;
  font-weight: 700;
  color: #222;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;

  & + .info-row {
    border-top: 1px solid #fafafa;
  }
}

.info-label {
  font-size: 14px;
  color: #888;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

/* Manner Score */
.manner-card {
  text-align: center;
}

.manner-score-display {
  padding: 16px 0 8px;
}

.manner-score-value {
  font-size: 36px;
  font-weight: 800;
  color: #1a73e8;
}

.manner-score-unit {
  font-size: 16px;
  color: #888;
  margin-left: 4px;
}

/* Empty */
.detail-empty {
  text-align: center;
  padding: 80px 0;
  color: #999;

  i {
    font-size: 48px;
    color: #ddd;
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

.go-back-btn {
  margin-top: 20px;
  padding: 10px 24px;
  background: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.detail-content {
  padding-bottom: 20px;
}
</style>
