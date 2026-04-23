<template>
  <div class="applications-page">
    <!-- 헤더 -->
    <div class="page-header">
      <button class="btn-back" @click="$router.go(-1)">
        <i class="el-icon-arrow-left"></i>
      </button>
      <span class="page-title">신청 내역</span>
    </div>

    <!-- 탭 -->
    <div class="tab-bar">
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'match' }"
        @click="activeTab = 'match'"
      >
        매치
      </button>
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'league' }"
        @click="activeTab = 'league'"
      >
        리그
      </button>
      <div class="tab-indicator" :class="activeTab"></div>
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="state-center">
      <div class="spinner"></div>
    </div>

    <!-- 매치 탭 -->
    <div v-else-if="activeTab === 'match'" class="list-wrap">
      <div v-if="matchApplications.length === 0" class="state-center">
        <div class="empty-icon"><i class="el-icon-football"></i></div>
        <p class="empty-text">신청한 매치가 없습니다</p>
      </div>
      <div v-else class="card-list">
        <div
          v-for="(item, idx) in matchApplications"
          :key="item.applicationUid"
          class="app-card"
          :style="{ animationDelay: idx * 40 + 'ms' }"
          @click="goToMatch(item.matchUid)"
        >
          <div class="card-top">
            <div class="team-name">{{ item.hostTeamName }}</div>
            <span class="status-chip" :class="appStatusClass(item)">
              {{ appStatusLabel(item) }}
            </span>
          </div>
          <div class="card-mid">
            <div class="info-row">
              <i class="el-icon-location-outline"></i>
              <span>{{ item.stadiumName }}</span>
            </div>
            <div class="info-row">
              <i class="el-icon-date"></i>
              <span>{{ item.matchDate }} {{ item.matchTime }}</span>
            </div>
            <div class="info-row">
              <i class="el-icon-trophy"></i>
              <span>{{ matchTypeLabel(item.matchType) }} · {{ matchFormatLabel(item.matchFormat) }}</span>
            </div>
          </div>
          <div class="card-bottom">
            <span class="applied-at">신청일 {{ formatDate(item.appliedAt) }}</span>
            <button
              v-if="isCancellable(item)"
              class="btn-cancel-app"
              @click="handleCancelApplication(item, $event)"
            >신청 취소</button>
            <i v-else class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 리그 탭 (리그 경기 참가 내역) -->
    <div v-else class="list-wrap">
      <div v-if="leagueApplications.length === 0" class="state-center">
        <div class="empty-icon"><i class="el-icon-s-flag"></i></div>
        <p class="empty-text">신청한 리그 경기가 없습니다</p>
      </div>
      <div v-else class="card-list">
        <div
          v-for="(item, idx) in leagueApplications"
          :key="item.applicationUid"
          class="app-card"
          :style="{ animationDelay: idx * 40 + 'ms' }"
          @click="goToLeagueMatch(item.leagueMatchUid)"
        >
          <div class="card-top">
            <div class="team-name">{{ item.leagueName }}</div>
            <span class="status-chip" :class="leagueAppStatusClass(item)">
              {{ leagueAppStatusLabel(item) }}
            </span>
          </div>
          <div class="card-mid">
            <div class="info-row">
              <i class="el-icon-football"></i>
              <span>{{ item.homeTeamName }} vs {{ item.awayTeamName }}</span>
            </div>
            <div class="info-row">
              <i class="el-icon-location-outline"></i>
              <span>{{ item.stadiumName }}</span>
            </div>
            <div class="info-row">
              <i class="el-icon-date"></i>
              <span>{{ item.matchDate }} {{ formatTime(item.matchTime) }}</span>
            </div>
          </div>
          <div class="card-bottom">
            <span class="applied-at">신청일 {{ formatDate(item.appliedAt) }}</span>
            <button
              v-if="isLeagueCancellable(item)"
              class="btn-cancel-app"
              @click="handleCancelLeagueApplication(item, $event)"
            >신청 취소</button>
            <i v-else class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getMyMatchApplications, cancelMyMatchApplication, getMatchConfig } from '@/api/match';
import { getMyLeagueMatchApplications, cancelLeagueMatchApplication } from '@/api/league';

interface LeagueMatchApplicationItem {
  applicationUid: string
  leagueMatchUid: string
  leagueName: string
  homeTeamName: string
  awayTeamName: string
  stadiumName: string
  matchDate: string
  matchTime: string
  matchStatus: string
  applicationStatus: string
  appliedAt: string
}

interface MatchApplication {
  applicationUid: string
  matchUid: string
  hostTeamName: string
  stadiumName: string
  matchDate: string
  matchTime: string
  matchType: string
  matchFormat: string
  matchStatus: string
  applicationStatus: string
  appliedAt: string
}

@Component
export default class MyApplicationsPage extends Vue {
  private activeTab: 'match' | 'league' = 'match';

  private isLoading = false;

  private matchApplications: MatchApplication[] = [];

  private leagueApplications: LeagueMatchApplicationItem[] = [];

  private cancelDaysBeforeMatch = 1;

  created(): void {
    this.loadData();
  }

  @Watch('activeTab')
  onTabChange(): void {
    if (this.activeTab === 'league' && this.leagueApplications.length === 0) {
      this.loadLeagues();
    }
  }

  private async loadData(): Promise<void> {
    this.isLoading = true;
    try {
      await Promise.all([this.loadMatches(), this.loadMatchConfig()]);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadMatchConfig(): Promise<void> {
    try {
      const res = await getMatchConfig();
      this.cancelDaysBeforeMatch = res.data?.cancelDaysBeforeMatch ?? 1;
    } catch (e) {
      // 기본값 유지
    }
  }

  private async loadMatches(): Promise<void> {
    try {
      const res = await getMyMatchApplications();
      this.matchApplications = res.data || [];
    } catch (e) {
      // ignore
    }
  }

  private async loadLeagues(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getMyLeagueMatchApplications();
      this.leagueApplications = res.data || [];
    } catch (e) {
      // ignore
    } finally {
      this.isLoading = false;
    }
  }

  private goToMatch(matchUid: string): void {
    this.$router.push({ name: 'MatchDetail', params: { uid: matchUid } });
  }

  private isCancellable(item: MatchApplication): boolean {
    if (item.applicationStatus !== 'APPROVED') return false;
    if (item.matchStatus !== 'RECRUITING' && item.matchStatus !== 'MATCHED') return false;
    if (!item.matchDate) return false;
    const matchDate = new Date(item.matchDate);
    const deadline = new Date();
    deadline.setDate(deadline.getDate() + this.cancelDaysBeforeMatch);
    return matchDate > deadline;
  }

  private async handleCancelApplication(item: MatchApplication, event: Event): Promise<void> {
    event.stopPropagation();
    try {
      await this.$confirm(
        `경기 ${this.cancelDaysBeforeMatch}일 전까지만 취소 가능합니다. 신청을 취소하시겠습니까?`,
        '신청 취소',
        { confirmButtonText: '취소하기', cancelButtonText: '닫기', type: 'warning' },
      );
    } catch {
      return;
    }
    try {
      await cancelMyMatchApplication(item.matchUid);
      this.$message.success('신청이 취소되었습니다.');
      await this.loadMatches();
    } catch (e: any) {
      this.$message.error(e?.response?.data?.message || '취소 중 오류가 발생했습니다.');
    }
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    return dateStr.substring(0, 10);
  }

  private matchStatusClass(status: string): string {
    const map: Record<string, string> = {
      RECRUITING: 'status-recruiting',
      MATCHED: 'status-matched',
      COMPLETED: 'status-completed',
      CANCELLED: 'status-cancelled',
    };
    return map[status] || '';
  }

  private matchStatusLabel(status: string): string {
    const map: Record<string, string> = {
      RECRUITING: '모집 중',
      MATCHED: '매칭 완료',
      COMPLETED: '경기 종료',
      CANCELLED: '취소됨',
    };
    return map[status] || status;
  }

  private appStatusClass(item: MatchApplication): string {
    if (item.applicationStatus === 'CANCELLED' || item.applicationStatus === 'REJECTED') {
      return 'status-cancelled';
    }
    return this.matchStatusClass(item.matchStatus);
  }

  private appStatusLabel(item: MatchApplication): string {
    if (item.applicationStatus === 'CANCELLED') return '취소 완료';
    if (item.applicationStatus === 'REJECTED') return '신청 거절';
    return this.matchStatusLabel(item.matchStatus);
  }

  private leagueAppStatusClass(item: LeagueMatchApplicationItem): string {
    if (item.applicationStatus === 'CANCELLED') return 'status-cancelled';
    const map: Record<string, string> = {
      SCHEDULED: 'status-recruiting',
      IN_PROGRESS: 'status-matched',
      COMPLETED: 'status-completed',
      CANCELLED: 'status-cancelled',
    };
    return map[item.matchStatus] || '';
  }

  private leagueAppStatusLabel(item: LeagueMatchApplicationItem): string {
    if (item.applicationStatus === 'CANCELLED') return '취소 완료';
    const map: Record<string, string> = {
      SCHEDULED: '경기 예정',
      IN_PROGRESS: '경기 중',
      COMPLETED: '경기 종료',
      CANCELLED: '경기 취소',
    };
    return map[item.matchStatus] || item.matchStatus;
  }

  private isLeagueCancellable(item: LeagueMatchApplicationItem): boolean {
    if (item.applicationStatus !== 'APPROVED') return false;
    if (item.matchStatus !== 'SCHEDULED') return false;
    if (!item.matchDate) return false;
    const matchDate = new Date(item.matchDate);
    const deadline = new Date();
    deadline.setDate(deadline.getDate() + this.cancelDaysBeforeMatch);
    return matchDate > deadline;
  }

  private async handleCancelLeagueApplication(
    item: LeagueMatchApplicationItem,
    event: Event,
  ): Promise<void> {
    event.stopPropagation();
    try {
      await this.$confirm(
        `경기 ${this.cancelDaysBeforeMatch}일 전까지만 취소 가능합니다. 신청을 취소하시겠습니까?`,
        '신청 취소',
        { confirmButtonText: '취소하기', cancelButtonText: '닫기', type: 'warning' },
      );
    } catch {
      return;
    }
    try {
      await cancelLeagueMatchApplication(item.leagueMatchUid);
      this.$message.success('신청이 취소되었습니다.');
      await this.loadLeagues();
    } catch (e: any) {
      this.$message.error(e?.response?.data?.message || '취소 중 오류가 발생했습니다.');
    }
  }

  private goToLeagueMatch(leagueMatchUid: string): void {
    this.$router.push({
      path: `/match-detail/${leagueMatchUid}`,
      query: { type: 'league' },
    });
  }

  private formatTime(time: string): string {
    if (!time) return '';
    if (typeof time === 'string' && time.includes(':')) {
      return time.substring(0, 5);
    }
    return time;
  }

  private matchTypeLabel(type: string): string {
    const map: Record<string, string> = { FRIENDLY: '친선', FREE: '자유' };
    return map[type] || type;
  }

  private matchFormatLabel(format: string): string {
    const map: Record<string, string> = {
      FOUR_VS_FOUR: '4vs4',
      FIVE_VS_FIVE: '5vs5',
      SIX_VS_SIX: '6vs6',
      SEVEN_VS_SEVEN: '7vs7',
    };
    return map[format] || format;
  }
}
</script>

<style scoped>
.applications-page {
  min-height: 100vh;
  background: #f4f6fb;
}

/* 헤더 */
.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
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
}

.btn-back:active {
  background: #e8ecf5;
}

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a2340;
  letter-spacing: -0.3px;
  flex: 1;
  text-align: left;
}

/* 탭 */
.tab-bar {
  display: flex;
  position: relative;
  background: #fff;
  border-bottom: 1px solid #edf0f7;
}

.tab-btn {
  flex: 1;
  padding: 14px 0;
  border: none;
  background: transparent;
  font-size: 14px;
  font-weight: 600;
  color: #aab2cc;
  cursor: pointer;
  transition: color 0.2s;
}

.tab-btn.active {
  color: #3949ab;
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  height: 2px;
  width: 50%;
  background: #3949ab;
  border-radius: 2px 2px 0 0;
  transition: left 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.tab-indicator.match {
  left: 0;
}

.tab-indicator.league {
  left: 50%;
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

/* 카드 목록 */
.list-wrap {
  padding: 20px 16px;
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-card {
  background: #fff;
  border: 1px solid #edf0f7;
  border-radius: 16px;
  padding: 16px 18px;
  box-shadow: 0 2px 8px rgba(57, 73, 171, 0.04);
  animation: fadeUp 0.35s ease both;
  cursor: pointer;
  transition: border-color 0.18s, transform 0.18s;
}

.app-card:active {
  transform: scale(0.98);
  border-color: #c5cae9;
}

.league-card {
  cursor: default;
}

.league-card:active {
  transform: none;
  border-color: #edf0f7;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.team-name {
  font-size: 15px;
  font-weight: 700;
  color: #1a2340;
  letter-spacing: -0.3px;
}

.status-chip {
  font-size: 11px;
  font-weight: 700;
  padding: 3px 9px;
  border-radius: 20px;
}

.status-recruiting {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-matched {
  background: #e8eaf6;
  color: #3949ab;
}

.status-completed {
  background: #f4f6fb;
  color: #6b7699;
}

.status-cancelled {
  background: #fce4ec;
  color: #c62828;
}

.rank-chip {
  font-size: 12px;
  font-weight: 700;
  background: linear-gradient(135deg, #3949ab, #5c6bc0);
  color: #fff;
  padding: 3px 10px;
  border-radius: 20px;
}

.card-mid {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 12px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 7px;
  font-size: 13px;
  color: #6b7699;
}

.info-row i {
  font-size: 13px;
  color: #aab2cc;
  flex-shrink: 0;
}

.card-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 10px;
  border-top: 1px solid #f0f2fa;
}

.applied-at {
  font-size: 12px;
  color: #aab2cc;
}

.card-bottom i {
  font-size: 13px;
  color: #c5cae9;
}

.btn-cancel-app {
  font-size: 12px;
  font-weight: 600;
  color: #c62828;
  background: #fce4ec;
  border: none;
  border-radius: 20px;
  padding: 4px 12px;
  cursor: pointer;
  transition: background 0.15s;
}

.btn-cancel-app:active {
  background: #f8bbd0;
}
</style>
