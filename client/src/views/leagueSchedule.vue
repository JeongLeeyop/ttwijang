<template>
  <div class="main league-schedule-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- League Status View -->
      <div class="league-status-section">
        <div class="league-header">
          <div class="league-title-row" :style="$route.query.returnTeamCode ? { marginBottom: '10px' } : {}">
            <el-select v-model="selectedLeague" :popper-append-to-body="false" placeholder="리그 선택" size="small" class="league-select" @change="onLeagueChange">
              <el-option
                v-for="league in leagues"
                :key="league.uid"
                :label="league.name"
                :value="league.uid"
              ></el-option>
            </el-select>
            <span class="league-title-text">경기 일정</span>
          </div>
          <div class="league-button-row" v-if="!$route.query.returnTeamCode">
            <router-link to="/league-status" class="league-action-btn-link">
              <button class="status-button">현황보기</button>
            </router-link>
          </div>
        </div>
        <div class="match-cards">
          <div
            v-for="(match, index) in upcomingMatches"
            :key="index"
            class="match-card"
            @click="$router.push({ path: `/match-detail/${match.uid}`, query: { type: 'league' } })"
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
    </div>

    <!-- Footer -->
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Watch,
  Prop,
} from 'vue-property-decorator';
import { getLeagueList, getLeagueAllMatches } from '@/api/league';

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

interface LeagueOption {
  uid: string
  name: string
}

@Component({})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private isLoading = false

  private leagues: LeagueOption[] = []

  private selectedLeague = ''

  private upcomingMatches: Match[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  async created() {
    await this.loadLeagues();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    await this.loadLeagues();
  }

  @Watch('selectedLeague')
  async onLeagueChange() {
    if (this.selectedLeague) {
      await this.loadScheduleData();
    }
  }

  private async loadLeagues(): Promise<void> {
    this.isLoading = true;
    try {
      const params: any = {
        status: 'IN_PROGRESS',
      };
      if (this.selectedRegion) {
        params.regionCode = this.selectedRegion;
      }
      const response = await getLeagueList(params);
      const leagueList = response.data?.content || response.data || [];

      this.leagues = leagueList.map((league: any) => ({
        uid: league.uid,
        name: league.name,
      }));

      // 첫 번째 리그 선택
      if (this.leagues.length > 0) {
        this.selectedLeague = this.leagues[0].uid;
      }
    } catch (error) {
      this.$message.error('리그 목록을 불러오지 못했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private async loadScheduleData(): Promise<void> {
    if (!this.selectedLeague) return;

    this.isLoading = true;
    try {
      const response = await getLeagueAllMatches(this.selectedLeague);
      const matches = response.data?.content || response.data || [];

      const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];

      this.upcomingMatches = matches
        .map((match: any) => {
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
          };
        });
    } catch (error) {
      console.error('Failed to load schedule data:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private previousMonth(): void {
    if (this.currentMonthIndex === 0) {
      this.currentYear -= 1;
      this.currentMonthIndex = 11;
    } else {
      this.currentMonthIndex -= 1;
    }
  }

  private nextMonth(): void {
    if (this.currentMonthIndex === 11) {
      this.currentYear += 1;
      this.currentMonthIndex = 0;
    } else {
      this.currentMonthIndex += 1;
    }
  }

  private resolveLogoUrl(logoUrl: string | null | undefined, name: string): string {
    if (!logoUrl) {
      return `https://ui-avatars.com/api/?name=${encodeURIComponent(name.substring(0, 2))}&background=random&color=fff&size=40`;
    }
    if (logoUrl.startsWith('http') || logoUrl.startsWith('/')) return logoUrl;
    return `/api/attached-file/${logoUrl}`;
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
</style>
