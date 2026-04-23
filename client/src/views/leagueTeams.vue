<template>
  <div class="league-teams-page">
    <div class="page-header">
      <h2 class="page-title">리그 참가 팀</h2>
      <p class="page-subtitle">리그에 참가 중인 모든 팀을 확인하세요</p>
    </div>

    <!-- 필터 섹션 -->
    <div class="filter-section">
      <!-- 지역 -->
      <div class="filter-row">
        <span class="filter-label">지역</span>
        <div class="filter-content">
          <el-select
            v-model="filterSido"
            :popper-append-to-body="false"
            placeholder="시/도"
            size="mini"
            clearable
            class="filter-select"
            @change="onSidoChange"
          >
            <el-option
              v-for="s in sidoList"
              :key="s.code"
              :label="s.name"
              :value="s.name"
            />
          </el-select>
          <el-select
            v-model="filterSigungu"
            :popper-append-to-body="false"
            placeholder="시/군/구"
            size="mini"
            clearable
            class="filter-select"
            @change="resetAndFetch"
          >
            <el-option
              v-for="s in sigunguList"
              :key="s.code"
              :label="s.name"
              :value="s.name"
            />
          </el-select>
        </div>
      </div>

      <!-- 리그 -->
      <div class="filter-row">
        <span class="filter-label">리그</span>
        <div class="filter-content">
          <el-select
            v-model="filterLeagueUid"
            :popper-append-to-body="false"
            placeholder="리그 선택"
            size="mini"
            clearable
            class="filter-select-wide"
            @change="resetAndFetch"
          >
            <el-option
              v-for="l in leagueOptions"
              :key="l.uid"
              :label="l.name"
              :value="l.uid"
            />
          </el-select>
        </div>
      </div>

      <!-- 검색 -->
      <div class="filter-row">
        <span class="filter-label">검색</span>
        <div class="filter-content">
          <el-input
            v-model="keyword"
            placeholder="팀 이름으로 검색"
            size="mini"
            clearable
            class="search-input"
            @keyup.enter.native="resetAndFetch"
            @clear="resetAndFetch"
          >
            <i
              slot="suffix"
              class="el-icon-search search-icon"
              @click="resetAndFetch"
            />
          </el-input>
        </div>
      </div>
    </div>

    <!-- 팀 목록 -->
    <div class="team-list">
      <div
        v-if="isLoading && teams.length === 0"
        class="loading-wrap"
      >
        <i class="el-icon-loading" />
        <span>로딩 중...</span>
      </div>
      <div
        v-else-if="teams.length === 0"
        class="empty-wrap"
      >
        <i class="el-icon-info" />
        <span>참가 팀이 없습니다.</span>
      </div>

      <div
        v-for="team in teams"
        :key="team.teamUid + '_' + team.leagueUid"
        class="team-card"
        @click="goToTeam(team.teamCode)"
      >
        <div class="card-logo">
          <img
            :src="resolveLogoUrl(team.teamLogoUrl, team.teamName)"
            :alt="team.teamName"
            class="team-logo"
          >
        </div>
        <div class="card-body">
          <div class="team-name-row">
            <span class="team-name">{{ team.teamName }}</span>
            <span class="league-badge">{{ team.leagueName }}</span>
          </div>
          <div
            v-if="team.regionSido"
            class="team-region"
          >
            <i class="el-icon-location-outline" />
            <span>{{ team.regionSido }}{{ team.regionSigungu ? ' ' + team.regionSigungu : '' }}</span>
          </div>
          <div class="team-stats">
            <span class="stat-chip">
              {{ team.ranking > 0 ? team.ranking + '위' : '-위' }}
            </span>
            <span class="stat-chip">
              {{ team.points != null ? team.points + '점' : '0점' }}
            </span>
          </div>
        </div>
        <i class="el-icon-arrow-right card-arrow" />
      </div>

      <!-- 더 보기 -->
      <div
        v-if="hasMore"
        class="load-more"
        @click="loadMore"
      >
        <span v-if="isLoading"><i class="el-icon-loading" /></span>
        <span v-else>더 보기</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getAllLeagueTeams, getLeagueList } from '@/api/league';
import { getSidoList, getSigunguList, getAllSigunguList } from '@/api/region';
import { storageKey } from '@/enums/localStorage';

interface LeagueOption {
  uid: string
  name: string
}

interface LeagueTeamItem {
  teamUid: string
  teamCode: string
  teamName: string
  teamLogoUrl: string
  leagueUid: string
  leagueName: string
  regionSido: string
  regionSigungu: string
  ranking: number
  points: number
}

@Component
export default class LeagueTeams extends Vue {
  private isLoading = false

  private teams: LeagueTeamItem[] = []

  private page = 0

  private size = 10

  private totalPages = 0

  private filterSido = ''

  private filterSigungu = ''

  private filterLeagueUid = ''

  private keyword = ''

  private sidoList: { code: string, name: string }[] = []

  private sigunguList: { code: string, name: string }[] = []

  private leagueOptions: LeagueOption[] = []

  get hasMore(): boolean {
    return this.page + 1 < this.totalPages;
  }

  async created() {
    await Promise.all([this.loadSidoList(), this.loadLeagueOptions()]);
    await this.initFilterFromStoredRegion();
    await this.fetchTeams();
  }

  private async initFilterFromStoredRegion(): Promise<void> {
    const code = localStorage.getItem(storageKey.selectedRegion);
    if (!code) return;
    try {
      const res = await getAllSigunguList();
      const allSigungu: Array<{ code: string, name: string, parentCode: string }> = res.data || [];
      const matched = allSigungu.find((r) => r.code === code);
      if (!matched) return;
      const parentSido = this.sidoList.find((s: any) => s.code === matched.parentCode);
      if (!parentSido) return;
      this.filterSido = parentSido.name;
      const sigRes = await getSigunguList(parentSido.code);
      this.sigunguList = sigRes.data || [];
      this.filterSigungu = matched.name;
    } catch (e) {
      // silent
    }
  }

  private async loadSidoList(): Promise<void> {
    try {
      const res = await getSidoList();
      this.sidoList = res.data || [];
    } catch (e) {
      // silent
    }
  }

  private async loadLeagueOptions(): Promise<void> {
    try {
      const res = await getLeagueList({ status: 'IN_PROGRESS', size: 100 });
      const leagues = res.data?.content || [];
      this.leagueOptions = leagues.map((l: any) => ({ uid: l.uid, name: l.name }));
    } catch (e) {
      // silent
    }
  }

  private async onSidoChange(): Promise<void> {
    this.filterSigungu = '';
    this.sigunguList = [];
    const sido = this.sidoList.find((s) => s.name === this.filterSido);
    if (sido) {
      try {
        const res = await getSigunguList(sido.code);
        this.sigunguList = res.data || [];
      } catch (e) {
        // silent
      }
    }
    this.resetAndFetch();
  }

  private resetAndFetch(): void {
    this.page = 0;
    this.teams = [];
    this.fetchTeams();
  }

  private async fetchTeams(): Promise<void> {
    if (this.isLoading) return;
    this.isLoading = true;
    try {
      const params: any = { page: this.page, size: this.size };
      if (this.filterSido) params.regionSido = this.filterSido;
      if (this.filterSigungu) params.regionSigungu = this.filterSigungu;
      if (this.filterLeagueUid) params.leagueUid = this.filterLeagueUid;
      if (this.keyword) params.keyword = this.keyword;

      const res = await getAllLeagueTeams(params);
      const data = res.data || res;
      if (data.content) {
        this.teams = [...this.teams, ...data.content];
        this.totalPages = data.totalPages || 0;
      }
    } catch (e) {
      // silent
    } finally {
      this.isLoading = false;
    }
  }

  private loadMore(): void {
    this.page += 1;
    this.fetchTeams();
  }

  private resolveLogoUrl(raw: string | null | undefined, name: string): string {
    if (!raw) {
      return `https://ui-avatars.com/api/?name=${encodeURIComponent((name || '??').substring(0, 2))}&background=061da1&color=fff&size=80`;
    }
    if (raw.startsWith('http') || raw.startsWith('/')) {
      return raw;
    }
    return `/api/attached-file/${raw}`;
  }

  private goToTeam(teamCode: string): void {
    this.$router.push(`/team/${teamCode}`);
  }
}
</script>

<style scoped>
.league-teams-page {
  height: calc(100vh - 60px);
  margin-top: 60px;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  background: #f5f5f5;
  padding-bottom: 80px;
}

.page-header {
  background: #061da1;
  padding: 24px 20px 20px;
  color: #fff;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
  margin: 0 0 4px;
}

.page-subtitle {
  font-size: 13px;
  opacity: 0.8;
  margin: 0;
}

/* 필터 */
.filter-section {
  background: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  border-bottom: 1px solid #f5f5f5;
}

.filter-row:last-child {
  border-bottom: none;
}

.filter-label {
  font-size: 12px;
  font-weight: 600;
  color: #888;
  flex-shrink: 0;
  min-width: 34px;
}

.filter-content {
  display: flex;
  gap: 6px;
  flex-wrap: nowrap;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.filter-select {
  width: 110px;
  flex-shrink: 0;
}

.filter-select-wide {
  width: 200px;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  min-width: 0;
}

.search-icon {
  cursor: pointer;
  color: #888;
  padding: 0 4px;
}

/* 팀 목록 */
.team-list {
  padding: 12px 16px;
}

.loading-wrap,
.empty-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 60px 0;
  color: #999;
  font-size: 14px;
}

.loading-wrap i,
.empty-wrap i {
  font-size: 28px;
}

/* 팀 카드 */
.team-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 12px;
  padding: 14px 12px;
  margin-bottom: 10px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: transform 0.15s;
}

.team-card:active {
  transform: scale(0.98);
}

.card-logo {
  flex-shrink: 0;
}

.team-logo {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  object-fit: cover;
  display: block;
}

.card-body {
  flex: 1;
  min-width: 0;
}

.team-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
  flex-wrap: wrap;
}

.team-name {
  font-size: 15px;
  font-weight: 700;
  color: #222;
}

.league-badge {
  display: inline-block;
  padding: 2px 8px;
  background: #f0f2ff;
  color: #061da1;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
}

.team-region {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 12px;
  color: #888;
  margin-bottom: 6px;
}

.team-region i {
  font-size: 12px;
}

.team-stats {
  display: flex;
  gap: 6px;
}

.stat-chip {
  display: inline-block;
  padding: 2px 8px;
  background: #f5f5f5;
  color: #555;
  border-radius: 10px;
  font-size: 11px;
  white-space: nowrap;
}

.card-arrow {
  color: #ccc;
  font-size: 14px;
  flex-shrink: 0;
}

/* 더 보기 */
.load-more {
  text-align: center;
  padding: 16px;
  color: #061da1;
  font-size: 14px;
  cursor: pointer;
}
</style>
