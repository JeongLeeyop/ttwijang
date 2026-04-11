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
      <div slot="header"><b>지역별 리그 현황</b></div>
      <el-table
        :data="leagueList"
        v-loading="loading"
        :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
        border
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
            {{ scope.row.startDate | parseDate }} ~ {{ scope.row.endDate | parseDate }}
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
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getLeagueList } from '@/api/league';
import { getUserList } from '@/api/user';
import { getTeamList } from '@/api/team';
import { getSidoList } from '@/api/region';

@Component({ name: 'Dashboard' })
export default class extends Vue {
  private loading = false;
  private leagueList: any[] = [];
  private summary = { leagues: 0, teams: 0, users: 0, regions: 0 };

  private statusType(status: string) {
    const map: any = { RECRUITING: 'success', IN_PROGRESS: 'warning', COMPLETED: 'info' };
    return map[status] || 'info';
  }

  private statusLabel(status: string) {
    const map: any = { RECRUITING: '모집중', IN_PROGRESS: '진행중', COMPLETED: '완료' };
    return map[status] || status;
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
      // 백엔드 연동 전 silent fail
    } finally {
      this.loading = false;
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
</style>
