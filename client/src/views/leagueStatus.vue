<template>
  <div class="main league-schedule-page">
    <div class="background-wave"></div>
    <div v-if="$route.query.returnTeamCode" class="page-back-wrap">
      <button class="page-back-button" @click="goBackToTeamPage">
        <i class="el-icon-arrow-left" />
      </button>
    </div>
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
          <div v-for="(match, index) in recentMatches" :key="index" class="past-match-card">
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
import { getLeagueStandings, getLeagueSchedule, getLeaguesByTeam } from '@/api/league';
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

  private currentYear = 2025

  private currentMonthIndex = new Date().getMonth()

  private isLoading = false

  private currentLeagueUid = ''

  private selectedLeagueUid = ''

  private availableLeagues: { uid: string, name: string }[] = []

  private myTeamUids: string[] = []

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  async created() {
    this.currentYear = new Date().getFullYear();
    await this.loadLeagueData();
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    if (this.currentLeagueUid) {
      await this.loadScheduleData();
    }
  }

  private isMyTeam(teamUid: string): boolean {
    return !!teamUid && this.myTeamUids.includes(teamUid);
  }

  private async onLeagueSelect(): Promise<void> {
    this.currentLeagueUid = this.selectedLeagueUid;
    await this.loadStandingsData();
    await this.loadScheduleData();
  }

  private async loadLeagueData(): Promise<void> {
    this.isLoading = true;
    try {
      // 본인 팀 목록 조회
      let myTeams: any[] = [];
      try {
        const teamsRes = await getMyTeams();
        myTeams = teamsRes.data || [];
      } catch (e) {
        myTeams = [];
      }
      this.myTeamUids = myTeams.map((t: any) => t.uid);

      // 각 팀이 참여 중인 리그 수집 (IN_PROGRESS만)
      const leagueMap = new Map<string, { uid: string, name: string }>();
      await Promise.all(myTeams.map(async (team: any) => {
        try {
          const res = await getLeaguesByTeam(team.uid);
          const teamLeagues: any[] = res.data || [];
          teamLeagues.forEach((l: any) => {
            if (!l.status || l.status === 'IN_PROGRESS') {
              leagueMap.set(l.uid, { uid: l.uid, name: l.name });
            }
          });
        } catch (e) {
          // silent
        }
      }));
      this.availableLeagues = Array.from(leagueMap.values());

      // URL 쿼리의 leagueUid 우선, 없으면 첫 번째
      const routeLeagueUid = this.$route.query.leagueUid as string | undefined;
      if (routeLeagueUid && this.availableLeagues.some((l) => l.uid === routeLeagueUid)) {
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
        await this.loadStandingsData();
        await this.loadScheduleData();
      } else {
        this.leagueTable = [];
        this.recentMatches = [];
      }
    } catch (error) {
      console.error('Failed to load league data:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadStandingsData(): Promise<void> {
    try {
      const standingsResponse = await getLeagueStandings(this.currentLeagueUid);
      const standings = standingsResponse.data || [];

      this.leagueTable = standings.map((team: any) => ({
        teamUid: team.teamUid,
        name: team.teamName,
        logo: team.teamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(team.teamName.substring(0, 2))}&background=random&color=fff&size=40`,
        played: team.matchesPlayed,
        wins: team.wins,
        draws: team.draws,
        losses: team.losses,
        points: team.points,
        goals: team.goalsScored,
        conceded: team.goalsConceded,
        difference: team.goalDifference,
      }));
    } catch (error) {
      console.error('Failed to load standings data:', error);
    }
  }

  private async loadScheduleData(): Promise<void> {
    try {
      const startDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-01`;
      const endDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-31`;

      const scheduleResponse = await getLeagueSchedule(this.currentLeagueUid, {
        startDate,
        endDate,
      });
      const matches = scheduleResponse.data?.content || scheduleResponse.data || [];

      const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];

      this.recentMatches = matches
        .filter((match: any) => match.status === 'COMPLETED')
        .map((match: any) => {
          const matchDate = new Date(match.matchDate);
          return {
            date: `${String(matchDate.getMonth() + 1).padStart(2, '0')}월 ${String(matchDate.getDate()).padStart(2, '0')}일`,
            day: dayNames[matchDate.getDay()],
            time: match.matchTime,
            location: match.stadiumName,
            homeTeam: match.homeTeamName,
            awayTeam: match.awayTeamName,
            homeLogo: match.homeTeamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(match.homeTeamName.substring(0, 2))}&background=random&color=fff&size=40`,
            awayLogo: match.awayTeamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(match.awayTeamName.substring(0, 2))}&background=random&color=fff&size=40`,
            homeScore: match.homeScore,
            awayScore: match.awayScore,
          };
        });
    } catch (error) {
      console.error('Failed to load schedule data:', error);
    }
  }

  private async previousMonth(): Promise<void> {
    if (this.currentMonthIndex === 0) {
      this.currentMonthIndex = 11;
      this.currentYear -= 1;
    } else {
      this.currentMonthIndex -= 1;
    }
  }

  private async nextMonth(): Promise<void> {
    if (this.currentMonthIndex === 11) {
      this.currentMonthIndex = 0;
      this.currentYear += 1;
    } else {
      this.currentMonthIndex += 1;
    }
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
  margin-top: 73px;
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

.page-back-wrap {
  position: fixed;
  top: 85px;
  left: 50%;
  transform: translateX(-50%);
  max-width: 480px;
  width: 100%;
  padding: 0 16px;
  display: flex;
  justify-content: flex-end;
  z-index: 1001;
  pointer-events: none;
}

.page-back-button {
  pointer-events: auto;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.page-back-button i {
  font-size: 18px;
  color: #333;
}
</style>
