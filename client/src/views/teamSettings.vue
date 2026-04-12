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
              <span class="menu-label">가입 승인 대기<span v-if="pendingCount > 0" class="pending-badge">{{ pendingCount }}</span></span>
              <i class="el-icon-arrow-right menu-arrow"></i>
            </div>
          </div>

          <!-- 환불 계좌 -->
          <div class="settings-menu-item" @click="openBankDialog">
            <div class="menu-label-row">
              <span class="menu-label">
                환불 계좌
                <span v-if="teamInfo.refundBankName" class="bank-summary"> · {{ teamInfo.refundBankName }} {{ teamInfo.refundBankAccount }}</span>
              </span>
              <span class="menu-tag tag-info" @click.stop="openBankDialog">정보수정</span>
            </div>
          </div>

          <!-- 팀 운영자 위임 -->
          <div class="settings-menu-item" @click="openDelegateDialog">
            <div class="menu-label-row">
              <span class="menu-label">팀 운영자 위임</span>
              <span class="menu-tag tag-select" @click.stop="openDelegateDialog">위임자 선택</span>
            </div>
          </div>
        </div>

        <!-- 환불 계좌 등록 다이얼로그 -->
        <el-dialog title="환불 계좌 등록" :visible.sync="bankDialogVisible" width="340px" :append-to-body="true">
          <el-form :model="bankForm" label-position="top" size="small">
            <el-form-item label="은행명">
              <el-input v-model="bankForm.refundBankName" placeholder="예) 국민은행" />
            </el-form-item>
            <el-form-item label="계좌번호">
              <el-input v-model="bankForm.refundBankAccount" placeholder="예) 123-456-789012" />
            </el-form-item>
          </el-form>
          <span slot="footer">
            <el-button size="small" @click="bankDialogVisible = false">취소</el-button>
            <el-button size="small" type="primary" :loading="bankSaving" @click="saveBankInfo">저장</el-button>
          </span>
        </el-dialog>

        <!-- 운영자 위임 다이얼로그 -->
        <el-dialog
          title="팀 운영자 위임"
          :visible.sync="delegateDialogVisible"
          width="360px"
          :append-to-body="true"
          @close="onDelegateDialogClose"
        >
          <!-- 멤버 선택 단계 -->
          <div v-if="!selectedMember">
            <p class="delegate-desc">위임할 팀원을 선택하세요.<br>위임 후 운영자 권한이 이전됩니다.</p>
            <div v-if="membersLoading" class="delegate-loading">
              <i class="el-icon-loading"></i> 불러오는 중...
            </div>
            <div v-else-if="delegatableMembers.length === 0" class="delegate-empty">
              위임 가능한 팀원이 없습니다.
            </div>
            <div v-else class="member-list">
              <div
                v-for="m in delegatableMembers"
                :key="m.userUid"
                class="member-item"
                @click="selectMember(m)"
              >
                <div class="member-avatar">{{ (m.userName || '?').charAt(0) }}</div>
                <div class="member-info">
                  <span class="member-name">{{ m.userName || '이름 없음' }}</span>
                  <span v-if="m.position" class="member-position">{{ m.position }}</span>
                </div>
                <span class="member-select-btn">선택</span>
              </div>
            </div>
          </div>

          <!-- 확인 단계 -->
          <div v-else class="confirm-step">
            <div class="confirm-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <p class="confirm-msg">
              <strong>{{ selectedMember.userName }}</strong> 님에게<br>
              운영자 권한을 위임하시겠습니까?
            </p>
            <p class="confirm-warn">위임 후 회원으로 전환되며, 다시 되돌릴 수 없습니다.</p>
          </div>

          <span slot="footer">
            <template v-if="!selectedMember">
              <el-button size="small" @click="delegateDialogVisible = false">취소</el-button>
            </template>
            <template v-else>
              <el-button size="small" @click="selectedMember = null">이전</el-button>
              <el-button
                size="small"
                type="danger"
                :loading="delegating"
                @click="confirmDelegate"
              >위임하기</el-button>
            </template>
          </span>
        </el-dialog>

        <!-- 팀 삭제하기 -->
        <div class="danger-section">
          <span class="danger-link" @click="openDeleteDialog">팀 삭제하기</span>
        </div>

        <!-- 팀 삭제 요청 다이얼로그 -->
        <el-dialog
          title="팀 삭제 요청"
          :visible.sync="deleteDialogVisible"
          width="360px"
          :append-to-body="true"
        >
          <div class="confirm-step">
            <div class="confirm-icon" style="color:#ff4757">
              <i class="el-icon-delete"></i>
            </div>
            <p class="confirm-msg">
              <strong>{{ teamInfo.name }}</strong> 팀 삭제를<br>
              최고관리자에게 요청하시겠습니까?
            </p>
            <p class="confirm-warn">
              관리자 검토 후 승인 시 팀이 영구 삭제됩니다.<br>
              삭제 전까지는 팀 활동이 정상 유지됩니다.
            </p>
          </div>
          <span slot="footer">
            <el-button size="small" @click="deleteDialogVisible = false">취소</el-button>
            <el-button
              size="small"
              type="danger"
              :loading="deleteRequesting"
              @click="confirmDeleteRequest"
            >삭제 요청</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import {
  getTeamDetail,
  getMyTeams,
  getPendingRequests,
  getTeamMembers,
  delegateOwner,
  updateTeam,
  requestDeleteTeam,
} from '@/api/team';

@Component
export default class TeamSettings extends Vue {
  private teamUid = ''

  private teamInfo: any = {}

  private pendingCount = 0

  private bankDialogVisible = false

  private bankSaving = false

  private bankForm = { refundBankName: '', refundBankAccount: '' }

  private delegateDialogVisible = false

  private membersLoading = false

  private members: any[] = []

  private selectedMember: any = null

  private delegating = false

  private deleteDialogVisible = false

  private deleteRequesting = false

  get defaultLogo(): string {
    return 'https://ui-avatars.com/api/?name=T&background=061da1&color=fff&size=60';
  }

  get delegatableMembers(): any[] {
    return this.members.filter((m) => m.userUid !== this.teamInfo.ownerUid);
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

  private openBankDialog(): void {
    this.bankForm = {
      refundBankName: this.teamInfo.refundBankName || '',
      refundBankAccount: this.teamInfo.refundBankAccount || '',
    };
    this.bankDialogVisible = true;
  }

  private async saveBankInfo(): Promise<void> {
    this.bankSaving = true;
    try {
      await updateTeam({
        uid: this.teamUid,
        refundBankName: this.bankForm.refundBankName,
        refundBankAccount: this.bankForm.refundBankAccount,
      });
      this.teamInfo.refundBankName = this.bankForm.refundBankName;
      this.teamInfo.refundBankAccount = this.bankForm.refundBankAccount;
      this.bankDialogVisible = false;
      this.$message.success('환불 계좌가 저장되었습니다');
    } catch (e) {
      this.$message.error('저장 실패');
    } finally {
      this.bankSaving = false;
    }
  }

  private async openDelegateDialog(): Promise<void> {
    this.selectedMember = null;
    this.members = [];
    this.delegateDialogVisible = true;
    this.membersLoading = true;
    try {
      const res = await getTeamMembers(this.teamUid);
      this.members = Array.isArray(res.data) ? res.data : [];
    } catch (e) {
      this.$message.error('팀원 목록 조회에 실패했습니다.');
      this.delegateDialogVisible = false;
    } finally {
      this.membersLoading = false;
    }
  }

  private selectMember(member: any): void {
    this.selectedMember = member;
  }

  private async confirmDelegate(): Promise<void> {
    if (!this.selectedMember) return;
    this.delegating = true;
    try {
      await delegateOwner(this.teamUid, this.selectedMember.userUid);
      this.$message.success(`${this.selectedMember.userName} 님에게 운영자 권한이 위임되었습니다.`);
      this.delegateDialogVisible = false;
      this.$router.push('/');
    } catch (e) {
      this.$message.error('위임에 실패했습니다.');
    } finally {
      this.delegating = false;
    }
  }

  private onDelegateDialogClose(): void {
    this.selectedMember = null;
    this.members = [];
  }

  private openDeleteDialog(): void {
    this.deleteDialogVisible = true;
  }

  private async confirmDeleteRequest(): Promise<void> {
    this.deleteRequesting = true;
    try {
      await requestDeleteTeam(this.teamUid);
      this.$message.success('삭제 요청이 접수되었습니다. 관리자 검토 후 처리됩니다.');
      this.deleteDialogVisible = false;
      this.$router.push('/');
    } catch (e) {
      this.$message.error('삭제 요청에 실패했습니다.');
    } finally {
      this.deleteRequesting = false;
    }
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
  top: -2px;
  position: relative;
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

.bank-summary {
  font-size: 12px;
  color: #888;
  font-weight: 400;
}

/* Delegate Dialog */
.delegate-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 14px;
}

.delegate-loading,
.delegate-empty {
  text-align: center;
  padding: 24px 0;
  color: #aaa;
  font-size: 13px;
}

.member-list {
  max-height: 300px;
  overflow-y: auto;
}

.member-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 6px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.1s;
}

.member-item:last-child {
  border-bottom: none;
}

.member-item:hover {
  background: #f9f9f9;
}

.member-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: #061da1;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.member-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.member-name {
  font-size: 14px;
  font-weight: 600;
  color: #222;
}

.member-position {
  font-size: 12px;
  color: #999;
}

.member-select-btn {
  font-size: 12px;
  font-weight: 700;
  color: #FF9800;
  padding: 4px 10px;
  border: 1px solid #FF9800;
  border-radius: 6px;
}

/* Confirm Step */
.confirm-step {
  text-align: center;
  padding: 8px 0 4px;
}

.confirm-icon {
  font-size: 44px;
  color: #FF9800;
  margin-bottom: 12px;
}

.confirm-msg {
  font-size: 15px;
  font-weight: 600;
  color: #222;
  line-height: 1.7;
  margin-bottom: 8px;
}

.confirm-warn {
  font-size: 12px;
  color: #ff4757;
}
</style>
