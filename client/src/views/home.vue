<template>
  <div class="main league-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">이 팀이랑 경기 어떠세요?</h2>
        <div v-if="isLoading && teamCards.length === 0" class="loading-container">
          <i class="el-icon-loading"></i> 로딩 중...
        </div>
        <VueSlickCarousel
          v-else-if="teamCards.length > 0"
          v-bind="slickOptions"
          class="team-cards-container"
        >
          <div
            v-for="(team, index) in teamCards"
            :key="index"
            class="team-card"
          >
            <div class="team-card-left">
              <div class="match-vs">
                <img :src="team.homeTeamLogo" :alt="team.homeTeamName" class="team-logo">
                <span class="vs-badge">VS</span>
                <img :src="team.awayTeamLogo" :alt="team.awayTeamName" class="team-logo">
              </div>
            </div>
            <div class="team-card-right">
              <div class="team-tags">
                <span class="tag">{{ team.leagueName }}</span>
                <span v-if="team.round" class="tag">{{ team.round }}R</span>
              </div>
              <div class="match-teams-names">
                {{ team.homeTeamName }} vs {{ team.awayTeamName }}
              </div>
              <div class="team-match-info">
                {{ team.matchDate }} ({{ team.matchDay }}) {{ team.matchTime }}
              </div>
              <div class="team-location">
                {{ team.stadiumName }} <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </VueSlickCarousel>
        <div v-else class="empty-message">
          예정된 리그 경기가 없습니다.
        </div>
      </div>

      <!-- League Schedule Section -->
      <div class="league-section" :class="{ 'expanded': showLeagueStatus }">
        <div class="league-header">
          <div class="league-title-row">
            <el-select v-model="selectedLeague" :popper-append-to-body="false" placeholder="리그 선택" size="small" class="league-select">
              <el-option
                v-for="league in availableLeagues"
                :key="league.uid"
                :label="league.name"
                :value="league.uid"
              ></el-option>
            </el-select>
            <span class="league-title-text">경기 일정</span>
          </div>
          <div class="league-button-row">
            <button class="status-button" @click="toggleLeagueStatus">현황보기</button>
          </div>
        </div>

        <!-- League Status Expanded View -->
        <div v-if="showLeagueStatus" class="league-status-expanded">
          <h3 class="status-title">리그 현황</h3>
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

        <!-- Upcoming Match Cards -->
        <div v-if="!showLeagueStatus" class="match-cards">
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
    </div>

    <!-- Footer -->
  </div>
</template>

<script lang="ts">
import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import { getLeagueList, getLeagueStandings, getLeagueSchedule, getUpcomingLeagueMatches } from '@/api/league';

interface UpcomingMatchCard {
  uid: string
  leagueName: string
  homeTeamName: string
  homeTeamLogo: string
  awayTeamName: string
  awayTeamLogo: string
  matchDate: string
  matchDay: string
  matchTime: string
  stadiumName: string
  round?: number
}

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

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private selectedLeague = ''

  private availableLeagues: { uid: string, name: string }[] = []

  private showLeagueStatus = false

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  private teamCards: UpcomingMatchCard[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  get slickOptions() {
    return {
      dots: true,
      infinite: this.teamCards.length > 1,
      speed: 500,
      slidesToShow: 1,
      slidesToScroll: 1,
      arrows: false,
      centerMode: true,
      centerPadding: '60px',
      swipeToSlide: true,
      touchThreshold: 5,
      initialSlide: 0,
      variableWidth: false,
      autoplay: this.teamCards.length > 1,
      autoplaySpeed: 3000,
      draggable: true,
      swipe: true,
    };
  }

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  private upcomingMatches: Match[] = []

  async created() {
    await this.loadHomeData();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    await this.loadHomeData();
  }

  @Watch('selectedLeague')
  async onLeagueChange() {
    if (this.selectedLeague) {
      await this.loadLeagueData();
    }
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    if (this.selectedLeague) {
      await this.loadLeagueData();
    }
  }

  private async loadHomeData(): Promise<void> {
    this.isLoading = true;
    try {
      // 리그 목록 로드 (지역 필터 적용)
      const leagueParams: any = {
        status: 'IN_PROGRESS',
      };
      if (this.selectedRegion) {
        leagueParams.regionCode = this.selectedRegion;
      }

      const leaguesResponse = await getLeagueList(leagueParams);
      if (leaguesResponse.data && leaguesResponse.data.content) {
        this.availableLeagues = leaguesResponse.data.content.map((league: any) => ({
          uid: league.uid,
          name: league.name,
        }));
        if (this.availableLeagues.length > 0) {
          this.selectedLeague = this.availableLeagues[0].uid;
        } else {
          this.selectedLeague = '';
          this.leagueTable = [];
          this.upcomingMatches = [];
          this.recentMatches = [];
        }
      }

      // 팀 카드 로드 (지역별 다가오는 리그 경기)
      await this.loadTeamCards();
    } catch (error) {
      console.error('홈 데이터 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadTeamCards(): Promise<void> {
    try {
      const params: any = {
        limit: 20,
      };
      if (this.selectedRegion) {
        params.regionCode = this.selectedRegion;
      }
      const response = await getUpcomingLeagueMatches(params);
      const matches = response.data || [];
      this.teamCards = matches.map((m: any): UpcomingMatchCard => {
        const dateInfo = this.formatMatchDate(m.matchDate);
        return {
          uid: m.uid,
          leagueName: m.leagueName || '',
          homeTeamName: m.homeTeamName || '',
          homeTeamLogo: m.homeTeamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent((m.homeTeamName || 'H').substring(0, 2))}&background=061da1&color=fff&size=60`,
          awayTeamName: m.awayTeamName || '',
          awayTeamLogo: m.awayTeamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent((m.awayTeamName || 'A').substring(0, 2))}&background=ff6600&color=fff&size=60`,
          matchDate: dateInfo.date,
          matchDay: dateInfo.day,
          matchTime: this.formatMatchTime(m.matchTime),
          stadiumName: m.stadiumName || '',
          round: m.round,
        };
      });
    } catch (error) {
      console.warn('팀 카드 로드 실패:', error);
      this.teamCards = [];
    }
  }

  private async loadLeagueData(): Promise<void> {
    try {
      // 리그 순위표 로드
      const standingsResponse = await getLeagueStandings(this.selectedLeague);
      if (standingsResponse.data && standingsResponse.data.standings) {
        this.leagueTable = standingsResponse.data.standings.map((team: any) => ({
          name: team.teamName,
          logo: this.getTeamLogo(team.teamName),
          played: team.played,
          wins: team.wins,
          draws: team.draws,
          losses: team.losses,
          points: team.points,
          goals: team.goalsFor,
          conceded: team.goalsAgainst,
          difference: team.goalDifference,
        }));
      }

      // 리그 일정 로드
      const startDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-01`;
      const endDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-31`;
      const scheduleResponse = await getLeagueSchedule(this.selectedLeague, { startDate, endDate });

      if (scheduleResponse.data && scheduleResponse.data.content) {
        const allMatches = scheduleResponse.data.content;

        // 최근 경기 (완료된 경기)
        this.recentMatches = allMatches
          .filter((m: any) => m.status === 'COMPLETED')
          .slice(0, 5)
          .map((match: any) => this.transformMatch(match, true));

        // 예정 경기
        this.upcomingMatches = allMatches
          .filter((m: any) => m.status !== 'COMPLETED')
          .slice(0, 5)
          .map((match: any) => this.transformMatch(match, false));
      }

    } catch (error) {
      console.warn('리그 데이터 로드 실패:', error);
    }
  }

  private transformMatch(match: any, includeScore: boolean): Match {
    const matchObj: Match = {
      date: this.formatDate(match.matchDate),
      day: this.getDayOfWeek(match.matchDate),
      time: match.matchTime || '',
      location: match.stadiumName || '',
      homeTeam: match.homeTeamName || '',
      awayTeam: match.awayTeamName || '',
      homeLogo: this.getTeamLogo(match.homeTeamName),
      awayLogo: this.getTeamLogo(match.awayTeamName),
    };
    if (includeScore) {
      matchObj.homeScore = match.homeScore ?? 0;
      matchObj.awayScore = match.awayScore ?? 0;
    }
    return matchObj;
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${month}월 ${day}일`;
  }

  private getDayOfWeek(dateStr: string): string {
    if (!dateStr) return '';
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const date = new Date(dateStr);
    return days[date.getDay()];
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    const bgColors = ['061da1', '0066cc', 'cc0000', 'ff6600', '00cc66', 'ffd700'];
    const colorIndex = teamName.charCodeAt(0) % bgColors.length;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=${bgColors[colorIndex]}&color=fff&size=60`;
  }

  private formatMatchDate(dateStr: string): { date: string, day: string } {
    if (!dateStr) return { date: '', day: '' };
    const date = new Date(dateStr);
    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return {
      date: `${month}월 ${day}일`,
      day: dayNames[date.getDay()],
    };
  }

  private formatMatchTime(timeStr: string): string {
    if (!timeStr) return '';
    const parts = timeStr.split(':');
    if (parts.length < 2) return timeStr;
    const hour = parseInt(parts[0], 10);
    const minute = parts[1];
    const period = hour < 12 ? 'Am' : 'Pm';
    const displayHour = hour > 12 ? hour - 12 : (hour === 0 ? 12 : hour);
    return `${period} ${String(displayHour).padStart(2, '0')}:${minute}`;
  }

  private toggleLeagueStatus(): void {
    this.showLeagueStatus = !this.showLeagueStatus;
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

  private handleTouchStart(event: TouchEvent): void {
    this.touchStartX = event.touches[0].clientX;
  }

  private handleTouchMove(event: TouchEvent): void {
    this.touchEndX = event.touches[0].clientX;
  }

  private handleTouchEnd(): void {
    const difference = this.touchStartX - this.touchEndX;
    if (Math.abs(difference) > 50) {
      // Swipe detected
    }
  }

  private navigateToMatchDetail(match: Match): void {
    console.log('Navigate to match:', match);
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Home Page Specific Styles section */

.match-vs {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.match-vs .team-logo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.vs-badge {
  font-size: 11px;
  font-weight: 700;
  color: #ff6600;
  background: rgba(255, 102, 0, 0.1);
  padding: 1px 6px;
  border-radius: 4px;
}

.match-teams-names {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.loading-container {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
}

.loading-container i {
  font-size: 24px;
  margin-bottom: 10px;
}

.empty-message {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
}
</style>
