<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">대시보드</p>
        <p class="usernumber">지역 · 리그 · 팀 현황</p>
      </div>
    </div>

    <!-- 요약 카드 -->
    <div class="dashboard-cards">
      <el-card class="dashboard-card" shadow="hover">
        <div class="card-inner">
          <div class="card-icon" style="background:#16a34a">🏟</div>
          <div class="card-info">
            <p class="card-label">활성 리그</p>
            <p class="card-value">{{ summary.leagues }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="dashboard-card" shadow="hover">
        <div class="card-inner">
          <div class="card-icon" style="background:#2563eb">👥</div>
          <div class="card-info">
            <p class="card-label">전체 팀</p>
            <p class="card-value">{{ summary.teams }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="dashboard-card" shadow="hover">
        <div class="card-inner">
          <div class="card-icon" style="background:#d97706">👤</div>
          <div class="card-info">
            <p class="card-label">전체 유저</p>
            <p class="card-value">{{ summary.users }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="dashboard-card" shadow="hover">
        <div class="card-inner">
          <div class="card-icon" style="background:#7c3aed">📍</div>
          <div class="card-info">
            <p class="card-label">운영 지역</p>
            <p class="card-value">{{ summary.regions }}</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 지역별 리그 현황 -->
    <el-card class="dashboard-table-card" shadow="never">
      <div slot="header"><b>지역별 리그 현황</b> <span style="font-size:12px;color:#888;margin-left:8px">리그를 클릭하면 상세 현황을 확인할 수 있습니다</span></div>
      <el-table
        :data="leagueList"
        v-loading="loading"
        :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
        border
        highlight-current-row
        style="cursor:pointer"
        @row-click="openLeagueDetail"
      >
        <el-table-column label="리그명" prop="name" min-width="200" />
        <el-table-column label="지역" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.regionSido }} {{ scope.row.regionSigungu }}
          </template>
        </el-table-column>
        <el-table-column label="시즌" prop="season" width="120" />
        <el-table-column label="기간" min-width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.startDate">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="팀 수" width="100">
          <template slot-scope="scope">
            {{ scope.row.currentTeams }} / {{ scope.row.maxTeams }}
          </template>
        </el-table-column>
        <el-table-column label="상태" width="100">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.status)" size="small">
              {{ statusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 리그 상세 다이얼로그 -->
    <el-dialog
      :title="selectedLeague ? selectedLeague.name : ''"
      :visible.sync="detailVisible"
      width="860px"
      top="5vh"
    >
      <div v-loading="detailLoading">
        <el-tabs v-model="activeTab">
          <!-- 순위표 탭 -->
          <el-tab-pane label="순위표" name="standings">
            <el-table
              :data="standings"
              border
              size="small"
              :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '6px 0' }"
            >
              <el-table-column label="순위" prop="ranking" width="60" align="center" />
              <el-table-column label="팀명" prop="teamName" min-width="130" />
              <el-table-column label="경기" prop="played" width="60" align="center" />
              <el-table-column label="승" prop="wins" width="50" align="center" />
              <el-table-column label="무" prop="draws" width="50" align="center" />
              <el-table-column label="패" prop="losses" width="50" align="center" />
              <el-table-column label="득점" prop="goalsFor" width="60" align="center" />
              <el-table-column label="실점" prop="goalsAgainst" width="60" align="center" />
              <el-table-column label="득실차" width="70" align="center">
                <template slot-scope="scope">
                  <span :style="{ color: scope.row.goalDifference > 0 ? '#16a34a' : scope.row.goalDifference < 0 ? '#dc2626' : '' }">
                    {{ scope.row.goalDifference > 0 ? '+' : '' }}{{ scope.row.goalDifference }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="승점" prop="points" width="60" align="center">
                <template slot-scope="scope">
                  <b>{{ scope.row.points }}</b>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="standings.length === 0" class="empty-tip">등록된 팀이 없습니다.</div>
          </el-tab-pane>

          <!-- 경기 현황 탭 -->
          <el-tab-pane label="경기 현황" name="matches">
            <el-table
              :data="matches"
              border
              size="small"
              :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '6px 0' }"
            >
              <el-table-column label="R" prop="round" width="50" align="center" />
              <el-table-column label="날짜" width="110">
                <template slot-scope="scope">{{ scope.row.matchDate || '-' }}</template>
              </el-table-column>
              <el-table-column label="홈팀" min-width="110">
                <template slot-scope="scope">{{ scope.row.homeTeamName }}</template>
              </el-table-column>
              <el-table-column label="스코어" width="80" align="center">
                <template slot-scope="scope">
                  <b v-if="scope.row.homeScore !== null && scope.row.homeScore !== undefined">
                    {{ scope.row.homeScore }} : {{ scope.row.awayScore }}
                  </b>
                  <span v-else style="color:#aaa">vs</span>
                </template>
              </el-table-column>
              <el-table-column label="원정팀" min-width="110">
                <template slot-scope="scope">{{ scope.row.awayTeamName }}</template>
              </el-table-column>
              <el-table-column label="구장" min-width="120">
                <template slot-scope="scope">{{ scope.row.stadiumName || '-' }}</template>
              </el-table-column>
              <el-table-column label="상태" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="matchStatusType(scope.row.status)" size="mini">
                    {{ matchStatusLabel(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="matches.length === 0" class="empty-tip">등록된 경기가 없습니다.</div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer">
        <el-button @click="detailVisible = false">닫기</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getLeagueList, getLeague, getAdminLeagueMatches } from '@/api/league';
import { getUserList } from '@/api/user';
import { getTeamList } from '@/api/team';
import { getSidoList } from '@/api/region';

@Component({ name: 'Dashboard' })
export default class extends Vue {
  private loading = false;

  private leagueList: any[] = [];

  private summary = {
 leagues: 0, teams: 0, users: 0, regions: 0,
};

  // 리그 상세 다이얼로그
  private detailVisible = false;

  private detailLoading = false;

  private selectedLeague: any = null;

  private activeTab = 'standings';

  private standings: any[] = [];

  private matches: any[] = [];

  statusType(status: string) {
    const map: any = { RECRUITING: 'success', IN_PROGRESS: 'warning', COMPLETED: 'info' };
    return map[status] || 'info';
  }

  statusLabel(status: string) {
    const map: any = { RECRUITING: '모집중', IN_PROGRESS: '진행중', COMPLETED: '완료' };
    return map[status] || status;
  }

  matchStatusType(s: string) {
    return ({
 SCHEDULED: '', IN_PROGRESS: 'warning', FINISHED: 'success', CANCELLED: 'danger',
} as any)[s] || '';
  }

  matchStatusLabel(s: string) {
    return ({
 SCHEDULED: '예정', IN_PROGRESS: '진행중', FINISHED: '종료', CANCELLED: '취소',
} as any)[s] || s;
  }

  async created() {
    this.loading = true;
    try {
      const [leagueRes, teamRes, userRes, regionRes] = await Promise.all([
        getLeagueList(),
        getTeamList(),
        getUserList({}),
        getSidoList(),
      ]);
      this.leagueList = leagueRes.data?.content || leagueRes.data || [];
      const activeLeagues = this.leagueList.filter((l: any) => l.status !== 'COMPLETED');
      this.summary.leagues = activeLeagues.length;
      this.summary.teams = teamRes.data?.totalElements || (teamRes.data?.content || []).length;
      this.summary.users = userRes.data?.totalElements || 0;
      this.summary.regions = (regionRes.data || []).length;
    } catch (e) {
      // silent fail
    } finally {
      this.loading = false;
    }
  }

  async openLeagueDetail(row: any) {
    this.selectedLeague = row;
    this.detailVisible = true;
    this.activeTab = 'standings';
    this.standings = [];
    this.matches = [];
    this.detailLoading = true;
    try {
      const [detailRes, matchesRes] = await Promise.all([
        getLeague(row.uid),
        getAdminLeagueMatches(row.uid),
      ]);
      this.standings = detailRes.data?.standings || [];
      this.matches = matchesRes.data || [];
    } catch {
      this.$message.error('데이터를 불러오는 중 오류가 발생했습니다.');
    } finally {
      this.detailLoading = false;
    }
  }
}
</script>

<style scoped>
.dashboard-cards {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}
.dashboard-card {
  flex: 1;
  min-width: 180px;
}
.card-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}
.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}
.card-label {
  font-size: 13px;
  color: #888;
  margin: 0;
}
.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #222;
  margin: 4px 0 0;
}
.dashboard-table-card {
  margin-top: 8px;
}
.empty-tip {
  text-align: center;
  color: #aaa;
  padding: 24px 0;
  font-size: 13px;
}
</style>
