<template>
  <div class="league-schedule-view">
    <div class="league-header">
      <div class="league-title-row">
        <el-select
          v-model="selectedLeague"
          :popper-append-to-body="false"
          placeholder="리그 선택"
          size="small"
          class="league-select"
          @change="onLeagueChange"
        >
          <el-option
            v-for="league in leagues"
            :key="league.uid"
            :label="league.name"
            :value="league.uid"
          ></el-option>
        </el-select>
        <span class="league-title-text">경기 일정</span>
      </div>
      <div class="league-button-row">
        <button class="status-button" @click="$emit('navigate', 'status')">현황보기</button>
      </div>
    </div>
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i> 로딩 중...
    </div>
    <div v-else class="match-cards">
      <div v-if="upcomingMatches.length === 0" class="empty-message">
        예정된 경기가 없습니다.
      </div>
      <div
        v-for="(match, index) in upcomingMatches"
        :key="index"
        class="match-card"
      >
        <i class="el-icon-arrow-right match-arrow"></i>
        <div class="match-date-time">{{ match.date }} ({{ match.day }}) {{ match.time }}</div>
        <div class="match-location">{{ match.location }}</div>
        <div class="match-versus">
          <div class="match-team">
            <span class="match-team-name">{{ match.homeTeam }}</span>
            <img :src="match.homeLogo" :alt="match.homeTeam" class="match-team-logo">
          </div>
          <span class="vs-text">VS</span>
          <div class="match-team">
            <img :src="match.awayLogo" :alt="match.awayTeam" class="match-team-logo">
            <span class="match-team-name">{{ match.awayTeam }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getLeagueList, getLeagueSchedule } from '@/api/league';

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

interface LeagueOption {
  uid: string
  name: string
}

@Component({})
export default class LeagueScheduleView extends Vue {
  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private isLoading = false

  private leagues: LeagueOption[] = []

  private selectedLeague = ''

  private upcomingMatches: Match[] = []

  async created() {
    await this.loadLeagues();
  }

  @Watch('selectedLeague')
  async onLeagueChange() {
    if (this.selectedLeague) {
      await this.loadScheduleData();
    }
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    if (this.selectedLeague) {
      await this.loadScheduleData();
    }
  }

  private async loadLeagues(): Promise<void> {
    this.isLoading = true;
    try {
      const response = await getLeagueList({ status: 'IN_PROGRESS' });
      const leagueList = response.data?.content || response.data || [];

      this.leagues = leagueList.map((league: any) => ({
        uid: league.uid,
        name: league.name,
      }));

      if (this.leagues.length > 0) {
        this.selectedLeague = this.leagues[0].uid;
      }
    } catch (error) {
      console.error('Failed to load leagues:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadScheduleData(): Promise<void> {
    if (!this.selectedLeague) return;

    this.isLoading = true;
    try {
      const response = await getLeagueSchedule(this.selectedLeague, {
        year: this.currentYear,
        month: this.currentMonthIndex + 1,
      });
      const matches = response.data?.content || response.data || [];

      const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];

      this.upcomingMatches = matches
        .filter((match: any) => match.status === 'SCHEDULED')
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
          };
        });
    } catch (error) {
      console.error('Failed to load schedule data:', error);
    } finally {
      this.isLoading = false;
    }
  }
}
</script>

<style scoped>
.league-schedule-view {
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
</style>
