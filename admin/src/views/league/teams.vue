<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">팀 배정</p>
        <p class="usernumber" v-if="league">{{ league.name }}</p>
      </div>
      <div class="user__tab">
        <el-button @click="$router.push({ name: 'LeagueList' })">← 리그 목록으로</el-button>
      </div>
    </div>

    <div class="team-assign-wrap" v-loading="loading">
      <!-- 왼쪽: 전체 팀 목록 -->
      <div class="team-panel">
        <div class="panel-header">
          <b>전체 팀 목록</b>
          <el-input
            v-model="teamSearch"
            placeholder="팀 검색"
            size="small"
            style="width:160px"
            @input="filterTeams"
          />
        </div>
        <el-table :data="filteredAllTeams" border size="small" max-height="500">
          <el-table-column label="팀명" prop="name" min-width="140" />
          <el-table-column label="지역" width="120">
            <template slot-scope="scope">
              {{ scope.row.regionSido }} {{ scope.row.regionSigungu }}
            </template>
          </el-table-column>
          <el-table-column label="" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="success"
                :disabled="isAssigned(scope.row.uid)"
                @click="handleAdd(scope.row)"
              >
                {{ isAssigned(scope.row.uid) ? '배정됨' : '추가 →' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="team-divider">
        <i class="el-icon-d-arrow-right" style="font-size:24px;color:#0f2027"></i>
      </div>

      <!-- 오른쪽: 배정된 팀 목록 -->
      <div class="team-panel">
        <div class="panel-header">
          <b>배정된 팀 목록</b>
          <span style="color:#888;font-size:13px">{{ assignedTeams.length }}팀 / {{ league ? league.maxTeams : '?' }}팀 최대</span>
        </div>
        <el-table :data="assignedTeams" border size="small" max-height="500">
          <el-table-column label="순위" prop="ranking" width="70" align="center" />
          <el-table-column label="팀명" prop="teamName" min-width="140" />
          <el-table-column label="" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="handleRemove(scope.row)"
              >
                제거
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getLeague, getLeagueTeams, addTeamToLeague, removeTeamFromLeague } from '@/api/league';
import { getTeamList } from '@/api/team';

@Component({ name: 'LeagueTeams' })
export default class extends Vue {
  private loading = false;
  private league: any = null;
  private allTeams: any[] = [];
  private assignedTeams: any[] = [];
  private teamSearch = '';
  private filteredAllTeams: any[] = [];

  get leagueUid() {
    return this.$route.params.uid;
  }

  async created() {
    this.loading = true;
    try {
      const [leagueRes, allTeamsRes, assignedRes] = await Promise.all([
        getLeague(this.leagueUid),
        getTeamList({ size: 200 }),
        getLeagueTeams(this.leagueUid),
      ]);
      this.league = leagueRes.data;
      this.allTeams = allTeamsRes.data?.content || allTeamsRes.data || [];
      this.assignedTeams = assignedRes.data || [];
      this.filterTeams();
    } finally {
      this.loading = false;
    }
  }

  filterTeams() {
    const q = this.teamSearch.toLowerCase();
    this.filteredAllTeams = q
      ? this.allTeams.filter((t) => t.name?.toLowerCase().includes(q))
      : [...this.allTeams];
  }

  isAssigned(teamUid: string) {
    return this.assignedTeams.some((t) => (t.uid === teamUid || t.teamUid === teamUid));
  }

  async handleAdd(team: any) {
    try {
      await addTeamToLeague(this.leagueUid, team.uid);
      const res = await getLeagueTeams(this.leagueUid);
      this.assignedTeams = res.data || [];
      this.$message.success(`${team.name} 팀이 배정되었습니다.`);
    } catch {
      this.$message.error('팀 추가 중 오류가 발생했습니다.');
    }
  }

  async handleRemove(assignedTeam: any) {
    const teamUid = assignedTeam.uid || assignedTeam.teamUid;
    const teamName = assignedTeam.name || assignedTeam.teamName;
    await this.$confirm(`${teamName} 팀을 리그에서 제거하시겠습니까?`, '제거 확인', { type: 'warning' });
    try {
      await removeTeamFromLeague(this.leagueUid, teamUid);
      this.assignedTeams = this.assignedTeams.filter(
        (t) => (t.uid || t.teamUid) !== teamUid
      );
      this.$message.success(`${teamName} 팀이 제거되었습니다.`);
    } catch {
      this.$message.error('팀 제거 중 오류가 발생했습니다.');
    }
  }
}
</script>

<style scoped>
.team-assign-wrap {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}
.team-panel {
  flex: 1;
  min-width: 0;
}
.team-divider {
  display: flex;
  align-items: center;
  padding-top: 60px;
}
.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  margin-bottom: 8px;
}
</style>
