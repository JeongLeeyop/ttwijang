<template>
  <div class="member-detail-page">
    <!-- 헤더 -->
    <div class="page-header">
      <button class="btn-back" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
      <span class="page-title">회원 상세</span>
      <div style="width:36px"></div>
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="state-center">
      <div class="spinner"></div>
    </div>

    <!-- 콘텐츠 -->
    <div v-else-if="memberInfo" class="member-content">
      <!-- 프로필 히어로 -->
      <div class="profile-hero">
        <div class="avatar-ring">
          <img
            :src="memberInfo.profileImageUrl || defaultAvatar"
            :alt="memberInfo.userName"
            class="profile-avatar"
          >
        </div>
        <h2 class="profile-name">{{ memberInfo.userName || '이름 없음' }}</h2>
        <span class="role-badge" :class="roleBadgeClass">{{ roleLabel }}</span>
      </div>

      <!-- 기본 정보 -->
      <div class="info-card">
        <p class="card-title">기본 정보</p>
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

      <!-- 팀 정보 -->
      <div class="info-card">
        <p class="card-title">팀 정보</p>
        <div class="info-row">
          <span class="info-label">역할</span>
          <span class="info-value">{{ roleLabel }}</span>
        </div>
        <div class="info-row">
          <span class="info-label">가입일</span>
          <span class="info-value">{{ joinDateLabel }}</span>
        </div>
      </div>
    </div>

    <!-- 없음 -->
    <div v-else class="state-center">
      <div class="empty-icon"><i class="el-icon-warning-outline"></i></div>
      <p class="empty-text">회원 정보를 찾을 수 없습니다</p>
      <button class="btn-go-back" @click="goBack">돌아가기</button>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getTeamMembers } from '@/api/team';

@Component
export default class MemberDetail extends Vue {
  private isLoading = false

  private memberInfo: any = null

  private teamUid = ''

  private memberUid = ''

  private defaultAvatar = 'https://ui-avatars.com/api/?name=U&background=e8eaf6&color=3949ab&size=80&bold=true'

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
      this.memberInfo = members.find((m: any) => m.uid === this.memberUid) || null;
    } catch (error) {
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

<style scoped>
.member-detail-page {
  min-height: 100vh;
  background: #f4f6fb;
}

/* 헤더 */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #edf0f7;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.btn-back {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f4f6fb;
  border: none;
  color: #3d4a6b;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-back:active {
  background: #e8ecf5;
}

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a2340;
  letter-spacing: -0.3px;
}

/* 공통 상태 */
.state-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 16px;
}

.spinner {
  width: 36px;
  height: 36px;
  border: 3px solid #e8ecf5;
  border-top-color: #3949ab;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #edf0f7;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #aab2cc;
}

.empty-text {
  font-size: 14px;
  color: #aab2cc;
  margin: 0;
}

.btn-go-back {
  padding: 10px 28px;
  background: #3949ab;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

/* 콘텐츠 */
.member-content {
  padding: 0 16px 40px;
}

/* 프로필 히어로 */
.profile-hero {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px 0 28px;
  gap: 10px;
}

.avatar-ring {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  padding: 3px;
  background: linear-gradient(135deg, #3949ab, #7986cb);
  box-shadow: 0 4px 16px rgba(57, 73, 171, 0.25);
}

.profile-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #fff;
}

.profile-name {
  font-size: 22px;
  font-weight: 800;
  color: #1a2340;
  margin: 0;
  letter-spacing: -0.4px;
}

.role-badge {
  font-size: 12px;
  font-weight: 700;
  padding: 4px 14px;
  border-radius: 20px;
}

.badge-owner {
  background: #fff3e0;
  color: #e65100;
}

.badge-manager {
  background: #e8f5e9;
  color: #2e7d32;
}

.badge-member {
  background: #e8eaf6;
  color: #3949ab;
}

/* 정보 카드 */
.info-card {
  background: #fff;
  border-radius: 16px;
  padding: 18px 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(57, 73, 171, 0.05);
  border: 1px solid #edf0f7;
}

.card-title {
  font-size: 13px;
  font-weight: 700;
  color: #aab2cc;
  margin: 0 0 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 11px 0;
  border-bottom: 1px solid #f4f6fb;
}

.info-row:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.info-row:first-of-type {
  padding-top: 0;
}

.info-label {
  font-size: 14px;
  color: #6b7699;
}

.info-value {
  font-size: 14px;
  color: #1a2340;
  font-weight: 600;
}
</style>
