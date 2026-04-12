<template>
  <div class="team-dashboard-page">
    <!-- Header -->
    <div class="page-header">
      <button class="btn-back" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
      <span class="page-title">팀 대시보드</span>
    </div>

    <!-- Content -->
    <div class="dashboard-content">
      <!-- Loading -->
      <div v-if="isLoading" class="state-center">
        <div class="spinner"></div>
      </div>

      <template v-else>
        <!-- League Dashboard -->
        <div class="dashboard-section">
          <div class="section-header" @click="openViz('league')">
            <h2 class="section-title">리그 대시보드</h2>
            <span class="viz-btn">
              <i class="el-icon-data-analysis"></i> 시각화
            </span>
          </div>

          <div class="dashboard-list">
            <div class="dashboard-item">
              <span class="item-label">리그 등급</span>
              <span class="item-value">
                {{ dashboard.leagueRanking ? dashboard.leagueRanking + '위' : '-' }}
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">리그 전적</span>
              <span class="item-value">
                <template v-if="dashboard.leaguePlayed > 0">
                  <span class="stat-win">{{ dashboard.leagueWins }}승</span>
                  <span class="stat-sep"> </span>
                  <span class="stat-draw">{{ dashboard.leagueDraws }}무</span>
                  <span class="stat-sep"> </span>
                  <span class="stat-loss">{{ dashboard.leagueLosses }}패</span>
                </template>
                <template v-else>-</template>
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">리그 승률</span>
              <span class="item-value">
                {{ dashboard.leaguePlayed > 0 ? dashboard.leagueWinRate + '%' : '-' }}
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">리그 성적</span>
              <span class="item-value">
                <template v-if="dashboard.leaguePlayed > 0">
                  {{ dashboard.leaguePoints }}점
                  ({{ dashboard.leagueGoalDifference >= 0 ? '+' : '' }}{{ dashboard.leagueGoalDifference }})
                </template>
                <template v-else>-</template>
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">매너 점수</span>
              <span class="item-value manner-score">
                {{ dashboard.mannerScore > 0 ? dashboard.mannerScore.toFixed(1) : '-' }}
              </span>
            </div>
          </div>
        </div>

        <!-- Match Dashboard -->
        <div class="dashboard-section">
          <div class="section-header" @click="openViz('match')">
            <h2 class="section-title">매치 대시보드</h2>
            <span class="viz-btn">
              <i class="el-icon-data-analysis"></i> 시각화
            </span>
          </div>

          <div class="dashboard-list">
            <div class="dashboard-item">
              <span class="item-label">전체 경기수</span>
              <span class="item-value">
                {{ dashboard.matchTotal > 0 ? dashboard.matchTotal + '경기' : '-' }}
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">매치 전적</span>
              <span class="item-value">
                <template v-if="dashboard.matchTotal > 0">
                  <span class="stat-win">{{ dashboard.matchWins }}승</span>
                  <span class="stat-sep"> </span>
                  <span class="stat-draw">{{ dashboard.matchDraws }}무</span>
                  <span class="stat-sep"> </span>
                  <span class="stat-loss">{{ dashboard.matchLosses }}패</span>
                </template>
                <template v-else>-</template>
              </span>
            </div>
            <div class="dashboard-item">
              <span class="item-label">매치 승률</span>
              <span class="item-value">
                {{ dashboard.matchTotal > 0 ? dashboard.matchWinRate + '%' : '-' }}
              </span>
            </div>
          </div>
        </div>
      </template>
    </div>

    <!-- Visualization Modal -->
    <transition name="slide-up">
      <div v-if="vizVisible" class="viz-overlay" @click.self="vizVisible = false">
        <div class="viz-sheet">
          <div class="viz-handle"></div>

          <!-- Match Visualization -->
          <template v-if="vizType === 'match'">
            <h3 class="viz-sheet-title">매치 전적 분석</h3>

            <div v-if="dashboard.matchTotal === 0" class="viz-empty">
              데이터가 없습니다
            </div>
            <template v-else>
              <!-- Donut Chart -->
              <div class="donut-wrap">
                <div
                  class="donut"
                  :style="matchDonutStyle"
                >
                  <div class="donut-center">
                    <span class="donut-pct">{{ dashboard.matchWinRate }}%</span>
                    <span class="donut-label">승률</span>
                  </div>
                </div>
                <div class="donut-legend">
                  <div class="legend-item">
                    <span class="legend-dot win"></span>
                    <span class="legend-text">승 {{ dashboard.matchWins }}</span>
                  </div>
                  <div class="legend-item">
                    <span class="legend-dot draw"></span>
                    <span class="legend-text">무 {{ dashboard.matchDraws }}</span>
                  </div>
                  <div class="legend-item">
                    <span class="legend-dot loss"></span>
                    <span class="legend-text">패 {{ dashboard.matchLosses }}</span>
                  </div>
                </div>
              </div>

              <!-- Stat Bars -->
              <div class="stat-bars">
                <div class="stat-bar-row">
                  <span class="bar-label">승</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill win"
                      :style="{ width: barPct(dashboard.matchWins, dashboard.matchTotal) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.matchWins }}</span>
                </div>
                <div class="stat-bar-row">
                  <span class="bar-label">무</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill draw"
                      :style="{ width: barPct(dashboard.matchDraws, dashboard.matchTotal) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.matchDraws }}</span>
                </div>
                <div class="stat-bar-row">
                  <span class="bar-label">패</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill loss"
                      :style="{ width: barPct(dashboard.matchLosses, dashboard.matchTotal) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.matchLosses }}</span>
                </div>
              </div>

              <!-- Total -->
              <div class="viz-stat-row">
                <div class="viz-stat-card">
                  <span class="viz-stat-val">{{ dashboard.matchTotal }}</span>
                  <span class="viz-stat-lbl">총 경기</span>
                </div>
                <div class="viz-stat-card">
                  <span class="viz-stat-val win-color">{{ dashboard.matchWinRate }}%</span>
                  <span class="viz-stat-lbl">승률</span>
                </div>
              </div>
            </template>
          </template>

          <!-- League Visualization -->
          <template v-else-if="vizType === 'league'">
            <h3 class="viz-sheet-title">리그 성적 분석</h3>

            <div v-if="dashboard.leaguePlayed === 0" class="viz-empty">
              데이터가 없습니다
            </div>
            <template v-else>
              <!-- Rank badge + manner score -->
              <div class="viz-stat-row top">
                <div class="rank-card">
                  <span class="rank-num">{{ dashboard.leagueRanking || '-' }}</span>
                  <span class="rank-unit">위</span>
                  <span class="viz-stat-lbl">현재 순위</span>
                </div>
                <div class="viz-stat-card">
                  <span class="viz-stat-val manner-color">{{ dashboard.mannerScore > 0 ? dashboard.mannerScore.toFixed(1) : '-' }}</span>
                  <span class="viz-stat-lbl">매너 점수</span>
                </div>
              </div>

              <!-- Donut Chart -->
              <div class="donut-wrap">
                <div
                  class="donut"
                  :style="leagueDonutStyle"
                >
                  <div class="donut-center">
                    <span class="donut-pct">{{ dashboard.leagueWinRate }}%</span>
                    <span class="donut-label">승률</span>
                  </div>
                </div>
                <div class="donut-legend">
                  <div class="legend-item">
                    <span class="legend-dot win"></span>
                    <span class="legend-text">승 {{ dashboard.leagueWins }}</span>
                  </div>
                  <div class="legend-item">
                    <span class="legend-dot draw"></span>
                    <span class="legend-text">무 {{ dashboard.leagueDraws }}</span>
                  </div>
                  <div class="legend-item">
                    <span class="legend-dot loss"></span>
                    <span class="legend-text">패 {{ dashboard.leagueLosses }}</span>
                  </div>
                </div>
              </div>

              <!-- Points & Goal diff gauge -->
              <div class="points-row">
                <div class="points-card">
                  <div class="points-bar-wrap">
                    <div class="points-bar" :style="{ width: leaguePointsPct + '%' }"></div>
                  </div>
                  <div class="points-info">
                    <span class="points-val">{{ dashboard.leaguePoints }}점</span>
                    <span class="points-lbl">승점</span>
                  </div>
                </div>
                <div class="goal-diff-card" :class="dashboard.leagueGoalDifference >= 0 ? 'positive' : 'negative'">
                  <span class="goal-diff-val">
                    {{ dashboard.leagueGoalDifference >= 0 ? '+' : '' }}{{ dashboard.leagueGoalDifference }}
                  </span>
                  <span class="goal-diff-lbl">득실차</span>
                </div>
              </div>

              <!-- Stat Bars -->
              <div class="stat-bars">
                <div class="stat-bar-row">
                  <span class="bar-label">승</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill win"
                      :style="{ width: barPct(dashboard.leagueWins, dashboard.leaguePlayed) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.leagueWins }}</span>
                </div>
                <div class="stat-bar-row">
                  <span class="bar-label">무</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill draw"
                      :style="{ width: barPct(dashboard.leagueDraws, dashboard.leaguePlayed) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.leagueDraws }}</span>
                </div>
                <div class="stat-bar-row">
                  <span class="bar-label">패</span>
                  <div class="bar-track">
                    <div
                      class="bar-fill loss"
                      :style="{ width: barPct(dashboard.leagueLosses, dashboard.leaguePlayed) + '%' }"
                    ></div>
                  </div>
                  <span class="bar-count">{{ dashboard.leagueLosses }}</span>
                </div>
              </div>
            </template>
          </template>

          <button class="btn-close-viz" @click="vizVisible = false">닫기</button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getTeamDashboard, TeamDashboard } from '@/api/team';

@Component
export default class TeamDashboardView extends Vue {
  private isLoading = false;

  private vizVisible = false;

  private vizType: 'match' | 'league' = 'match';

  private dashboard: TeamDashboard = {
    matchTotal: 0,
    matchWins: 0,
    matchDraws: 0,
    matchLosses: 0,
    matchWinRate: 0,
    leaguePlayed: 0,
    leagueWins: 0,
    leagueDraws: 0,
    leagueLosses: 0,
    leagueWinRate: 0,
    leaguePoints: 0,
    leagueGoalsFor: 0,
    leagueGoalsAgainst: 0,
    leagueGoalDifference: 0,
    leagueRanking: null,
    mannerScore: 0,
  };

  created(): void {
    const teamUid = this.$route.query.teamUid as string;
    if (teamUid) {
      this.fetchDashboard(teamUid);
    }
  }

  get matchDonutStyle(): object {
    return this.buildDonutStyle(
      this.dashboard.matchWins,
      this.dashboard.matchDraws,
      this.dashboard.matchLosses,
    );
  }

  get leagueDonutStyle(): object {
    return this.buildDonutStyle(
      this.dashboard.leagueWins,
      this.dashboard.leagueDraws,
      this.dashboard.leagueLosses,
    );
  }

  get leaguePointsPct(): number {
    const max = this.dashboard.leaguePlayed * 3;
    if (max === 0) return 0;
    return Math.min(100, Math.round((this.dashboard.leaguePoints / max) * 100));
  }

  private buildDonutStyle(wins: number, draws: number, losses: number): object {
    const total = wins + draws + losses;
    if (total === 0) {
      return { background: 'conic-gradient(#edf0f7 0% 100%)' };
    }
    const winPct = (wins / total) * 100;
    const drawPct = (draws / total) * 100;
    return {
      background: `conic-gradient(
        #3949ab 0% ${winPct}%,
        #aab2cc ${winPct}% ${winPct + drawPct}%,
        #ef5350 ${winPct + drawPct}% 100%
      )`,
    };
  }

  private barPct(val: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((val / total) * 100);
  }

  private openViz(type: 'match' | 'league'): void {
    this.vizType = type;
    this.vizVisible = true;
  }

  private async fetchDashboard(teamUid: string): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getTeamDashboard(teamUid);
      this.dashboard = res.data;
    } finally {
      this.isLoading = false;
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }
}
</script>

<style scoped>
.team-dashboard-page {
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

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a2340;
  letter-spacing: -0.3px;
}

/* 콘텐츠 */
.dashboard-content {
  padding: 16px;
}

.state-center {
  display: flex;
  justify-content: center;
  padding: 60px 0;
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

/* 섹션 */
.dashboard-section {
  background: #fff;
  border-radius: 16px;
  padding: 18px 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(57, 73, 171, 0.05);
  border: 1px solid #edf0f7;
  width: 100%;
  box-sizing: border-box;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
  cursor: pointer;
}

.section-title {
  font-size: 16px;
  font-weight: 800;
  color: #1a2340;
  margin: 0;
  letter-spacing: -0.3px;
}

.viz-btn {
  font-size: 12px;
  font-weight: 600;
  color: #3949ab;
  background: #e8eaf6;
  padding: 5px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: background 0.2s;
}

.section-header:active .viz-btn {
  background: #c5cae9;
}

/* 리스트 */
.dashboard-list {
  display: flex;
  flex-direction: column;
}

.dashboard-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 13px 0;
  border-bottom: 1px solid #f0f2fa;
}

.dashboard-item:last-child {
  border-bottom: none;
}

.item-label {
  font-size: 14px;
  color: #6b7699;
  display: flex;
  align-items: center;
  gap: 6px;
}

.item-label::before {
  content: '';
  width: 3px;
  height: 3px;
  border-radius: 50%;
  background: #c5cae9;
}

.item-value {
  font-size: 14px;
  color: #1a2340;
  font-weight: 600;
}

.stat-win { color: #3949ab; }
.stat-draw { color: #aab2cc; }
.stat-loss { color: #ef5350; }
.manner-score { color: #ff9800; }

/* 시각화 오버레이 */
.viz-overlay {
  position: fixed;
  inset: 0;
  background: rgba(26, 35, 64, 0.4);
  z-index: 200;
  display: flex;
  align-items: flex-end;
}

.viz-sheet {
  width: 100%;
  max-height: 85vh;
  background: #fff;
  border-radius: 24px 24px 0 0;
  padding: 12px 24px 40px;
  overflow-y: auto;
  box-shadow: 0 -8px 32px rgba(57, 73, 171, 0.12);
}

.viz-handle {
  width: 36px;
  height: 4px;
  background: #e8ecf5;
  border-radius: 2px;
  margin: 0 auto 20px;
}

.viz-sheet-title {
  font-size: 18px;
  font-weight: 800;
  color: #1a2340;
  margin: 0 0 24px;
  letter-spacing: -0.3px;
}

.viz-empty {
  text-align: center;
  padding: 40px 0;
  color: #aab2cc;
  font-size: 14px;
}

/* 도넛 차트 */
.donut-wrap {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 24px;
}

.donut {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  flex-shrink: 0;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.donut::after {
  content: '';
  position: absolute;
  width: 76px;
  height: 76px;
  background: #fff;
  border-radius: 50%;
}

.donut-center {
  position: absolute;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.donut-pct {
  font-size: 20px;
  font-weight: 800;
  color: #1a2340;
  line-height: 1.1;
}

.donut-label {
  font-size: 11px;
  color: #aab2cc;
  font-weight: 500;
}

.donut-legend {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}

.legend-dot.win { background: #3949ab; }
.legend-dot.draw { background: #aab2cc; }
.legend-dot.loss { background: #ef5350; }

.legend-text {
  font-size: 13px;
  color: #4a5473;
  font-weight: 500;
}

/* 바 차트 */
.stat-bars {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.stat-bar-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bar-label {
  width: 18px;
  font-size: 13px;
  font-weight: 700;
  color: #6b7699;
  flex-shrink: 0;
}

.bar-track {
  flex: 1;
  height: 10px;
  background: #f0f2fa;
  border-radius: 5px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 5px;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.bar-fill.win { background: linear-gradient(90deg, #3949ab, #5c6bc0); }
.bar-fill.draw { background: #c5cae9; }
.bar-fill.loss { background: linear-gradient(90deg, #ef5350, #e57373); }

.bar-count {
  width: 20px;
  font-size: 13px;
  font-weight: 700;
  color: #1a2340;
  text-align: right;
  flex-shrink: 0;
}

/* 스탯 카드 */
.viz-stat-row {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.viz-stat-row.top {
  margin-bottom: 24px;
}

.viz-stat-card {
  flex: 1;
  background: #f4f6fb;
  border-radius: 12px;
  padding: 14px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.viz-stat-val {
  font-size: 22px;
  font-weight: 800;
  color: #1a2340;
  line-height: 1.1;
}

.viz-stat-lbl {
  font-size: 11px;
  color: #aab2cc;
  font-weight: 500;
}

.win-color { color: #3949ab; }
.manner-color { color: #ff9800; }

/* 리그 전용 */
.rank-card {
  flex: 1;
  background: linear-gradient(135deg, #3949ab, #5c6bc0);
  border-radius: 12px;
  padding: 14px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.rank-num {
  font-size: 28px;
  font-weight: 900;
  color: #fff;
  line-height: 1;
}

.rank-unit {
  font-size: 14px;
  font-weight: 700;
  color: rgba(255,255,255,0.7);
}

.rank-card .viz-stat-lbl {
  color: rgba(255,255,255,0.6);
}

/* 승점 게이지 */
.points-row {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.points-card {
  flex: 1;
  background: #f4f6fb;
  border-radius: 12px;
  padding: 14px;
}

.points-bar-wrap {
  height: 8px;
  background: #e8ecf5;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 10px;
}

.points-bar {
  height: 100%;
  background: linear-gradient(90deg, #3949ab, #7986cb);
  border-radius: 4px;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.points-info {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.points-val {
  font-size: 20px;
  font-weight: 800;
  color: #1a2340;
}

.points-lbl {
  font-size: 12px;
  color: #aab2cc;
}

.goal-diff-card {
  width: 80px;
  border-radius: 12px;
  padding: 14px 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
}

.goal-diff-card.positive {
  background: #e8f5e9;
}

.goal-diff-card.negative {
  background: #fce4ec;
}

.goal-diff-val {
  font-size: 20px;
  font-weight: 800;
}

.goal-diff-card.positive .goal-diff-val { color: #2e7d32; }
.goal-diff-card.negative .goal-diff-val { color: #c62828; }

.goal-diff-lbl {
  font-size: 11px;
  color: #aab2cc;
}

/* 닫기 버튼 */
.btn-close-viz {
  width: 100%;
  padding: 14px;
  border-radius: 12px;
  background: #f4f6fb;
  border: 1px solid #edf0f7;
  color: #6b7699;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 4px;
}

.btn-close-viz:active {
  background: #e8ecf5;
}

/* 트랜지션 */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: opacity 0.25s;
}

.slide-up-enter,
.slide-up-leave-to {
  opacity: 0;
}

.slide-up-enter .viz-sheet,
.slide-up-leave-to .viz-sheet {
  transform: translateY(100%);
}

.slide-up-enter-active .viz-sheet,
.slide-up-leave-active .viz-sheet {
  transition: transform 0.3s cubic-bezier(0.32, 0.72, 0, 1);
}
</style>
