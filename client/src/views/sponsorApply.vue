<template>
  <div class="sponsor-apply-page">
    <div class="header">
      <button class="back-button" @click="$router.go(-1)">
        <i class="el-icon-arrow-left"></i>
      </button>
      <h1 class="header-title">구단 후원 신청</h1>
    </div>

    <div class="content">
      <div class="form-container">
        <!-- 캐시 잔액 -->
        <div class="balance-card">
          <span class="balance-label">보유 캐시</span>
          <span class="balance-amount">{{ balance.toLocaleString() }}원</span>
        </div>

        <!-- 구단주 안내 -->
        <div v-if="ownerThreshold > 0" class="owner-info-card">
          <i class="el-icon-star-on"></i>
          <span>
            <strong>{{ ownerThreshold.toLocaleString() }}원 이상</strong> 후원 시,
            구단주가 없는 팀의 구단주가 됩니다.
          </span>
        </div>

        <!-- 팀 검색 -->
        <div class="form-section">
          <label class="form-label">팀 선택</label>
          <el-select
            v-model="selectedTeamUid"
            filterable
            remote
            reserve-keyword
            placeholder="팀 이름 검색"
            :remote-method="searchTeams"
            :loading="teamSearchLoading"
            class="full-width"
            @change="onTeamChange"
          >
            <el-option
              v-for="t in teamOptions"
              :key="t.uid"
              :label="t.name"
              :value="t.uid"
            />
          </el-select>
        </div>

        <!-- 선택된 팀 정보 -->
        <div v-if="selectedTeam" class="selected-team-card">
          <div class="team-info-row">
            <span class="team-name">{{ selectedTeam.name }}</span>
            <span v-if="selectedTeam.sponsorOwnerUid" class="owner-badge has-owner">구단주 있음</span>
            <span v-else class="owner-badge no-owner">구단주 없음</span>
          </div>
          <div class="team-manner">
            매너점수 {{ selectedTeam.mannerScore ? selectedTeam.mannerScore.toFixed(1) : '-' }}
          </div>
        </div>

        <!-- 후원 금액 -->
        <div class="form-section">
          <label class="form-label">후원 금액 (최소 1,000원)</label>
          <el-input
            v-model.number="amount"
            type="number"
            placeholder="금액을 입력하세요"
            :min="1000"
            class="full-width"
          >
            <template slot="append">원</template>
          </el-input>
          <div v-if="amount > 0 && ownerThreshold > 0" class="amount-hint">
            <span v-if="amount >= ownerThreshold && selectedTeam && !selectedTeam.sponsorOwnerUid" class="hint-owner">
              구단주로 등록됩니다!
            </span>
            <span v-else-if="amount >= ownerThreshold && selectedTeam && selectedTeam.sponsorOwnerUid" class="hint-warn">
              이미 구단주가 있어 일반 후원으로 처리됩니다.
            </span>
          </div>
        </div>

        <!-- 후원 메시지 -->
        <div class="form-section">
          <label class="form-label">후원 메시지 (선택)</label>
          <el-input
            v-model="message"
            type="textarea"
            :rows="3"
            placeholder="응원 메시지를 남겨보세요"
            :maxlength="200"
            show-word-limit
          />
        </div>

        <!-- 제출 버튼 -->
        <button
          class="submit-btn"
          :disabled="!canSubmit || isSubmitting"
          @click="submit"
        >
          <span v-if="isSubmitting"><i class="el-icon-loading"></i> 처리 중...</span>
          <span v-else>후원하기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { searchTeams } from '@/api/team';
import { getSponsorFee } from '@/api/sponsor';
import { getWallet, sponsorTeam } from '@/api/cash';

@Component
export default class SponsorApply extends Vue {
  private ownerThreshold = 0;

  private balance = 0;

  private teamOptions: any[] = [];

  private teamSearchLoading = false;

  private selectedTeamUid = '';

  private selectedTeam: any = null;

  private amount = 0;

  private message = '';

  private isSubmitting = false;

  get canSubmit(): boolean {
    return !!this.selectedTeamUid && this.amount >= 1000 && this.amount <= this.balance;
  }

  async created(): Promise<void> {
    await Promise.all([this.fetchFee(), this.fetchBalance()]);
  }

  private async fetchFee(): Promise<void> {
    try {
      const res = await getSponsorFee();
      this.ownerThreshold = res.data?.amount || 0;
    } catch (e) {
      // 무시
    }
  }

  private async fetchBalance(): Promise<void> {
    try {
      const res = await getWallet();
      this.balance = res.data?.balance || 0;
    } catch (e) {
      this.balance = 0;
    }
  }

  private async searchTeams(keyword: string): Promise<void> {
    if (!keyword) return;
    this.teamSearchLoading = true;
    try {
      const res = await searchTeams(keyword);
      this.teamOptions = res.data?.content || res.data || [];
    } finally {
      this.teamSearchLoading = false;
    }
  }

  private onTeamChange(uid: string): void {
    this.selectedTeam = this.teamOptions.find((t) => t.uid === uid) || null;
  }

  private async submit(): Promise<void> {
    if (!this.canSubmit) return;
    if (this.amount > this.balance) {
      this.$message.error('캐시 잔액이 부족합니다.');
      return;
    }

    this.isSubmitting = true;
    try {
      await sponsorTeam({
        teamUid: this.selectedTeamUid,
        amount: this.amount,
        message: this.message || undefined,
      });
      this.$message.success('후원이 완료되었습니다!');
      this.$router.go(-1);
    } catch (e) {
      // axios interceptor가 처리
    } finally {
      this.isSubmitting = false;
    }
  }
}
</script>

<style scoped>
.sponsor-apply-page {
  min-height: 100vh;
  background: #f5f6fa;
}

.header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
  gap: 12px;
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

.content {
  padding: 16px;
}

.form-container {
  max-width: 500px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.balance-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.balance-label {
  font-size: 14px;
  color: #666;
}

.balance-amount {
  font-size: 18px;
  font-weight: 800;
  color: #061da1;
}

.owner-info-card {
  background: #e8eaf6;
  border-radius: 10px;
  padding: 12px 14px;
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: #333;
  line-height: 1.5;
}

.owner-info-card i {
  color: #ffc107;
  font-size: 16px;
  margin-top: 1px;
}

.form-section {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.form-label {
  font-size: 13px;
  font-weight: 600;
  color: #555;
}

.full-width {
  width: 100%;
}

.selected-team-card {
  background: #fff;
  border-radius: 12px;
  padding: 14px 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.team-info-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 4px;
}

.team-name {
  font-size: 15px;
  font-weight: 700;
  color: #222;
}

.owner-badge {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}

.has-owner {
  background: #fce4ec;
  color: #c62828;
}

.no-owner {
  background: #e8f5e9;
  color: #2e7d32;
}

.team-manner {
  font-size: 12px;
  color: #aaa;
}

.amount-hint {
  font-size: 12px;
  margin-top: 2px;
}

.hint-owner {
  color: #2e7d32;
  font-weight: 600;
}

.hint-warn {
  color: #e65100;
}

.submit-btn {
  width: 100%;
  padding: 15px;
  background: #061da1;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: opacity 0.2s;
  margin-top: 6px;
}

.submit-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
</style>
