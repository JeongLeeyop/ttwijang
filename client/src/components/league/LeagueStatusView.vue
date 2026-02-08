<template>
  <div class="league-status-view">
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
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i> 로딩 중...
    </div>
    <template v-else>
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
            <tr v-for="(team, index) in leagueTable" :key="index">
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
        <div v-if="recentMatches.length === 0" class="empty-message">
          최근 경기 결과가 없습니다.
        </div>
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
    </template>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getLeagueList, getLeagueStandings, getLeagueSchedule } from '@/api/league';

interface LeagueTeam {
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
export default class LeagueStatusView extends Vue {
  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private isLoading = false

  private currentLeagueUid = ''

  private selectedLeagueUid = ''

  private availableLeagues: { uid: string, name: string }[] = []

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  async created() {
    await this.loadLeagueData();
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    if (this.currentLeagueUid) {
      await this.loadScheduleData();
    }
  }

  private async onLeagueSelect(): Promise<void> {
    this.currentLeagueUid = this.selectedLeagueUid;
    await this.loadStandingsData();
    await this.loadScheduleData();
  }

  private async loadLeagueData(): Promise<void> {
    this.isLoading = true;
    try {
      const leagueResponse = await getLeagueList({ status: 'IN_PROGRESS' });
      const leagues = leagueResponse.data?.content || leagueResponse.data || [];

      this.availableLeagues = leagues.map((league: any) => ({
        uid: league.uid,
        name: league.name,
      }));

      if (leagues.length > 0) {
        this.currentLeagueUid = leagues[0].uid;
        this.selectedLeagueUid = leagues[0].uid;

        await this.loadStandingsData();
        await this.loadScheduleData();
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

  private previousMonth(): void {
    if (this.currentMonthIndex === 0) {
      this.currentMonthIndex = 11;
      this.currentYear -= 1;
    } else {
      this.currentMonthIndex -= 1;
    }
  }

  private nextMonth(): void {
    if (this.currentMonthIndex === 11) {
      this.currentMonthIndex = 0;
      this.currentYear += 1;
    } else {
      this.currentMonthIndex += 1;
    }
  }
}
</script>

<style scoped>
.league-status-view {
  padding: 0 20px;
}

.loading-container {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
}

.empty-message {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
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
</style>
