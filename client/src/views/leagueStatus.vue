<template>
  <div class="main league-schedule-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- League Status View -->
      <div class="league-status-section">
        <h3 class="status-title">리그 현황</h3>
        <!-- League Select Filter -->
        <div class="league-filter-row">
          <el-select
            v-model="selectedLeagueUid"
            :popper-append-to-body="false"
            placeholder="리그 선택"
            size="small"
            class="league-filter-select"
            @change="onLeagueSelect"
          >
            <el-option
              v-for="league in availableLeagues"
              :key="league.uid"
              :label="league.name"
              :value="league.uid"
            ></el-option>
          </el-select>
        </div>
        <!-- Calendar Navigation -->
        <div class="calendar-nav">
          <i class="el-icon-arrow-left" @click="previousMonth"></i>
          <span class="current-month">{{ currentMonth }}</span>
          <i class="el-icon-arrow-right" @click="nextMonth"></i>
        </div>

        <!-- League Table -->
        <div class="league-table">
          <table>
            <thead>
              <tr>
                <th>경기</th>
                <th>승점</th>
                <th>승</th>
                <th>무</th>
                <th>패</th>
                <th>득</th>
                <th>실</th>
                <th>차</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(team, index) in leagueTable"
                :key="index"
                :class="{ 'my-team-row': isMyTeam(team.teamUid) }"
              >
                <td class="rank-cell">
                  <span class="rank-number">{{ index + 1 }}</span>
                  <img :src="team.logo" :alt="team.name" class="team-mini-logo">
                </td>
                <td class="points">{{ team.points }}</td>
                <td>{{ team.wins }}</td>
                <td>{{ team.draws }}</td>
                <td>{{ team.losses }}</td>
                <td>{{ team.goals }}</td>
                <td>{{ team.conceded }}</td>
                <td>{{ team.difference }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Past Match Results -->
        <div class="past-matches">
          <div v-for="(match, index) in recentMatches" :key="index" class="past-match-card" style="cursor:pointer" @click="goToMatchDetail(match.uid)">
            <div class="past-match-date">{{ match.date }} ({{ match.day }}) {{ match.time }}</div>
            <div class="past-match-teams">
              <div class="past-team">
                <span class="past-team-name">{{ match.homeTeam }}</span>
                <img :src="match.homeLogo" :alt="match.homeTeam" class="past-team-logo">
              </div>
              <div class="past-match-score">
                <span class="score-number">{{ match.homeScore }}</span>
                <span class="score-divider">-</span>
                <span class="score-number">{{ match.awayScore }}</span>
              </div>
              <div class="past-team">
                <img :src="match.awayLogo" :alt="match.awayTeam" class="past-team-logo">
                <span class="past-team-name">{{ match.awayTeam }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Footer -->
  </div>
</template>

<script lang="ts">
import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator';
import {
  getLeagueAllMatches, getLeagueList, getLeagueDetail,
} from '@/api/league';
import { getMyTeams } from '@/api/team';

interface LeagueTeam {
  teamUid: string
  name: string
  logo: string
  played: number
  wins: number
  draws: number
  losses: number
  points: number
  goals: number
  conceded: number
  difference: number
}

interface Match {
  uid: string
  date: string
  day: string
  time: string
  location: string
  homeTeam: string
  awayTeam: string
  homeLogo: string
  awayLogo: string
  homeScore?: number
  awayScore?: number
}

@Component({})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private isLoading = false

  private currentLeagueUid = ''

  private selectedLeagueUid = ''

  private availableLeagues: { uid: string, name: string }[] = []

  private myTeamUids: string[] = []

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  private allMatches: any[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  async created() {
    await this.loadLeagueData();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    await this.loadLeagueData();
  }

  private isMyTeam(teamUid: string): boolean {
    return !!teamUid && this.myTeamUids.includes(teamUid);
  }

  private resolveLogoUrl(logoUrl: string | null | undefined, name: string): string {
    if (!logoUrl) {
      return `https://ui-avatars.com/api/?name=${encodeURIComponent(name.substring(0, 2))}&background=random&color=fff&size=40`;
    }
    if (logoUrl.startsWith('http') || logoUrl.startsWith('/')) return logoUrl;
    return `/api/attached-file/${logoUrl}`;
  }

  private async onLeagueSelect(): Promise<void> {
    this.currentLeagueUid = this.selectedLeagueUid;
    await this.loadScheduleData();
  }

  private async loadLeagueData(): Promise<void> {
    this.isLoading = true;
    try {
      const routeLeagueUid = this.$route.query.leagueUid as string | undefined;

      // 본인 팀 목록 조회 (순위표 "내 팀" 하이라이트용 — 실패/미가입이어도 조회 자체는 계속 진행)
      try {
        const teamsRes = await getMyTeams();
        const teamData = teamsRes.data;
        let myTeams: any[] = [];
        if (Array.isArray(teamData)) {
          myTeams = teamData;
        } else if (teamData) {
          myTeams = [teamData];
        }
        this.myTeamUids = myTeams.map((t: any) => t.uid);
      } catch (e) {
        this.myTeamUids = [];
      }

      // 가입한 팀/리그 여부와 무관하게 지역 내 전체 리그 목록 조회
      const leagueParams: any = { status: 'IN_PROGRESS' };
      if (this.selectedRegion) {
        leagueParams.regionCode = this.selectedRegion;
      }
      const leagueListRes = await getLeagueList(leagueParams);
      const leagues = leagueListRes.data?.content || leagueListRes.data || [];
      const leagueMap = new Map<string, { uid: string, name: string }>();
      leagues.forEach((l: any) => {
        leagueMap.set(l.uid, { uid: l.uid, name: l.name });
      });

      // URL의 leagueUid가 있지만 지역 리그 목록에 없는 경우 직접 추가 (다른 지역 리그 조회)
      if (routeLeagueUid && !leagueMap.has(routeLeagueUid)) {
        try {
          const leagueRes = await getLeagueDetail(routeLeagueUid);
          const league = leagueRes.data;
          leagueMap.set(routeLeagueUid, { uid: routeLeagueUid, name: league?.name || '리그' });
        } catch (e) {
          leagueMap.set(routeLeagueUid, { uid: routeLeagueUid, name: '리그' });
        }
      }

      this.availableLeagues = Array.from(leagueMap.values());

      // URL 쿼리의 leagueUid 우선, 없으면 첫 번째
      if (routeLeagueUid) {
        this.currentLeagueUid = routeLeagueUid;
        this.selectedLeagueUid = routeLeagueUid;
      } else if (this.availableLeagues.length > 0) {
        this.currentLeagueUid = this.availableLeagues[0].uid;
        this.selectedLeagueUid = this.availableLeagues[0].uid;
      } else {
        this.currentLeagueUid = '';
        this.selectedLeagueUid = '';
      }

      if (this.currentLeagueUid) {
        await this.loadScheduleData();
      } else {
        this.leagueTable = [];
        this.recentMatches = [];
      }
    } catch (error) {
      this.$message.error('리그 정보를 불러오지 못했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private async loadScheduleData(): Promise<void> {
    try {
      const scheduleResponse = await getLeagueAllMatches(this.currentLeagueUid);
      this.allMatches = scheduleResponse.data?.content || scheduleResponse.data || [];
      this.updateDisplay();
    } catch (error) {
      this.$message.error('일정을 불러오지 못했습니다.');
    }
  }

  private toMatchEntry(match: any): Match {
    const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
    const matchDate = new Date(match.matchDate);
    return {
      uid: match.uid,
      date: `${String(matchDate.getMonth() + 1).padStart(2, '0')}월 ${String(matchDate.getDate()).padStart(2, '0')}일`,
      day: dayNames[matchDate.getDay()],
      time: match.matchTime,
      location: match.stadiumName,
      homeTeam: match.homeTeamName,
      awayTeam: match.awayTeamName,
      homeLogo: this.resolveLogoUrl(match.homeTeamLogoUrl, match.homeTeamName),
      awayLogo: this.resolveLogoUrl(match.awayTeamLogoUrl, match.awayTeamName),
      homeScore: match.homeScore,
      awayScore: match.awayScore,
    };
  }

  private computeMonthlyStandings(rawMatches: any[]): LeagueTeam[] {
    const teamMap = new Map<string, any>();
    rawMatches
      .filter((m: any) => m.status === 'COMPLETED')
      .forEach((m: any) => {
        [
          { uid: m.homeTeamUid, name: m.homeTeamName, logoUrl: m.homeTeamLogoUrl },
          { uid: m.awayTeamUid, name: m.awayTeamName, logoUrl: m.awayTeamLogoUrl },
        ].forEach(({ uid, name, logoUrl }) => {
          if (!teamMap.has(uid)) {
            teamMap.set(uid, {
              teamUid: uid, name, logoUrl, played: 0, wins: 0, draws: 0, losses: 0, points: 0, goals: 0, conceded: 0,
            });
          }
        });
        const home = teamMap.get(m.homeTeamUid);
        const away = teamMap.get(m.awayTeamUid);
        const hs = m.homeScore ?? 0;
        const as2 = m.awayScore ?? 0;
        home.played += 1; away.played += 1;
        home.goals += hs; home.conceded += as2;
        away.goals += as2; away.conceded += hs;
        if (hs > as2) { home.wins += 1; home.points += 3; away.losses += 1; } else if (hs < as2) { away.wins += 1; away.points += 3; home.losses += 1; } else { home.draws += 1; home.points += 1; away.draws += 1; away.points += 1; }
      });
    return Array.from(teamMap.values())
      .sort((a, b) => b.points - a.points || (b.goals - b.conceded) - (a.goals - a.conceded))
      .map((t) => ({
        teamUid: t.teamUid,
        name: t.name,
        logo: this.resolveLogoUrl(t.logoUrl, t.name),
        played: t.played,
        wins: t.wins,
        draws: t.draws,
        losses: t.losses,
        points: t.points,
        goals: t.goals,
        conceded: t.conceded,
        difference: t.goals - t.conceded,
      }));
  }

  private updateDisplay(): void {
    const filtered = this.allMatches.filter((m: any) => {
      const d = new Date(m.matchDate);
      return d.getFullYear() === this.currentYear && d.getMonth() === this.currentMonthIndex;
    });
    const sortKey = (m: any) => `${m.matchDate || '9999-12-31'} ${m.matchTime || '99:99'}`;
    this.leagueTable = this.computeMonthlyStandings(filtered);
    this.recentMatches = filtered
      .slice()
      .sort((a: any, b: any) => sortKey(a).localeCompare(sortKey(b)))
      .map((m) => this.toMatchEntry(m));
  }

  private previousMonth(): void {
    if (this.currentMonthIndex === 0) {
      this.currentMonthIndex = 11;
      this.currentYear -= 1;
    } else {
      this.currentMonthIndex -= 1;
    }
    this.updateDisplay();
  }

  private nextMonth(): void {
    if (this.currentMonthIndex === 11) {
      this.currentMonthIndex = 0;
      this.currentYear += 1;
    } else {
      this.currentMonthIndex += 1;
    }
    this.updateDisplay();
  }

  private goToMatchDetail(matchUid: string): void {
    this.$router.push({ path: `/match-detail/${matchUid}`, query: { type: 'league' } });
  }

  private goBackToTeamPage(): void {
    const code = this.$route.query.returnTeamCode as string;
    if (code) {
      this.$router.push({ path: `/team/${code}`, query: { tab: 'league' } });
    } else {
      this.$router.back();
    }
  }
}
</script>

<style scoped>
.league-schedule-page .main {
  background: #fff;
}

.league-schedule-page .content {
  margin-top: 50px;
  padding: 20px;
  background: #fff;
}

.league-status-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.league-filter-row {
  margin-bottom: 8px;
}

.league-filter-select {
  width: 100%;
}

::v-deep .league-filter-select .el-input__inner {
  border-radius: 8px;
  font-size: 13px;
}

.league-table tr.my-team-row {
  background: #e8f0ff;
  font-weight: 700;
  box-shadow: inset 3px 0 0 #061da1;
}

.league-table tr.my-team-row td {
  color: #061da1;
}

.calendar-nav {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.page-back-button i {
  font-size: 18px;
  color: #333;
}
</style>
