<template>
  <div class="main league-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">이 팀이랑 경기 어떠세요?</h2>
        <VueSlickCarousel v-bind="slickOptions" class="team-cards-container">
          <div
            v-for="(team, index) in teamCards"
            :key="index"
            class="team-card"
          >
            <div class="team-card-left">
              <img :src="team.logo" :alt="team.name" class="team-logo">
            </div>
            <div class="team-card-right">
              <div class="team-tags">
                <span class="tag">{{ team.league }}</span>
                <span class="tag">매너 {{ team.manner }}점</span>
                <span class="tag">{{ team.matchType }}</span>
                <span class="tag">{{ team.teamSize }}</span>
              </div>
              <div class="team-match-info">
                {{ team.matchDate }} ({{ team.matchDay }}) {{ team.matchTime }}
              </div>
              <div class="team-location">
                {{ team.location }} <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </VueSlickCarousel>
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
import { getLeagueList, getLeagueStandings, getLeagueSchedule } from '@/api/league';
import { getMatchList } from '@/api/match';

interface TeamCard {
  name: string
  logo: string
  league: string
  manner: number
  matchType: string
  teamSize: string
  matchDate: string
  matchDay: string
  matchTime: string
  location: string
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

  private teamCards: TeamCard[] = []

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
      const params: any = {
        status: 'IN_PROGRESS',
      };
      if (this.selectedRegion) {
        // 경남 지역의 시/군/구로 필터링
        params.regionSido = '경남';
        params.regionSigungu = this.selectedRegion;
      }

      const leaguesResponse = await getLeagueList(params);
      if (leaguesResponse.data && leaguesResponse.data.content) {
        this.availableLeagues = leaguesResponse.data.content.map((league: any) => ({
          uid: league.uid,
          name: league.name,
        }));
        // 첫 번째 리그 선택
        if (this.availableLeagues.length > 0) {
          this.selectedLeague = this.availableLeagues[0].uid;
        } else {
          // 선택된 지역에 리그가 없으면 초기화
          this.selectedLeague = '';
          this.leagueTable = [];
          this.upcomingMatches = [];
          this.recentMatches = [];
        }
      }

      // 추천 팀 카드 로드 (지역 필터 적용)
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
        status: 'RECRUITING',
      };
      if (this.selectedRegion) {
        // 경남 지역의 시/군/구로 필터링
        params.regionSido = '경남';
        params.regionSigungu = this.selectedRegion;
      }

      const matchesResponse = await getMatchList(params);
      if (matchesResponse.data && matchesResponse.data.content) {
        this.teamCards = matchesResponse.data.content.slice(0, 5).map((match: any) => ({
          name: match.teamName || match.team?.name || '팀 모집중',
          logo: this.getTeamLogo(match.teamName || '팀'),
          league: match.leagueName || 'A리그',
          manner: match.teamMannerScore || 4.5,
          matchType: match.matchType === 'FRIENDLY' ? '친선 경기' : '정규 경기',
          teamSize: this.formatMatchFormat(match.matchFormat),
          matchDate: this.formatDate(match.matchDate),
          matchDay: this.getDayOfWeek(match.matchDate),
          matchTime: match.matchTime || '',
          location: match.stadiumName || match.location || '',
        }));
      }

      // API 데이터가 없으면 기본 데이터 표시
      if (this.teamCards.length === 0) {
        this.teamCards = this.getDefaultTeamCards();
      }
    } catch (error) {
      console.warn('팀 카드 로드 실패:', error);
      this.teamCards = this.getDefaultTeamCards();
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

      // 데이터가 없으면 기본값 유지
      if (this.leagueTable.length === 0) {
        this.leagueTable = this.getDefaultLeagueTable();
      }
      if (this.upcomingMatches.length === 0 && !this.showLeagueStatus) {
        this.upcomingMatches = this.getDefaultUpcomingMatches();
      }
      if (this.recentMatches.length === 0 && this.showLeagueStatus) {
        this.recentMatches = this.getDefaultRecentMatches();
      }
    } catch (error) {
      console.warn('리그 데이터 로드 실패:', error);
      // 기본 데이터 사용
      this.leagueTable = this.getDefaultLeagueTable();
      this.upcomingMatches = this.getDefaultUpcomingMatches();
      this.recentMatches = this.getDefaultRecentMatches();
    }
  }

  private transformMatch(match: any, includeScore: boolean): Match {
    const matchObj: Match = {
      uid: match.uid || '',
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

  private formatMatchFormat(format: string): string {
    const formatMap: Record<string, string> = {
      FOUR_VS_FOUR: '4 대 4',
      FIVE_VS_FIVE: '5 대 5',
      SIX_VS_SIX: '6 대 6',
      SEVEN_VS_SEVEN: '7 대 7',
    };
    return formatMap[format] || '5 대 5';
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=40';
    const initials = teamName.slice(0, 2);
    const bgColors = ['061da1', '0066cc', 'cc0000', 'ff6600', '00cc66', 'ffd700'];
    const colorIndex = teamName.charCodeAt(0) % bgColors.length;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=${bgColors[colorIndex]}&color=fff&size=40`;
  }

  // 기본 데이터 (API 로드 실패시 사용)
  private getDefaultTeamCards(): TeamCard[] {
    return [
      {
        name: '대성풋살클럽',
        logo: this.getTeamLogo('대성풋살클럽'),
        league: 'B리그',
        manner: 4.8,
        matchType: '친선 경기',
        teamSize: '5 대 5',
        matchDate: '05월 09일',
        matchDay: '금',
        matchTime: 'Pm 07:00',
        location: '대성풋살장',
      },
      {
        name: '강남FC',
        logo: this.getTeamLogo('강남FC'),
        league: 'A리그',
        manner: 4.5,
        matchType: '정규 경기',
        teamSize: '5 대 5',
        matchDate: '05월 10일',
        matchDay: '토',
        matchTime: 'Pm 06:00',
        location: '강남풋살장',
      },
       {
        name: '강남FC',
        logo: this.getTeamLogo('강남FC'),
        league: 'A리그',
        manner: 4.5,
        matchType: '정규 경기',
        teamSize: '5 대 5',
        matchDate: '05월 10일',
        matchDay: '토',
        matchTime: 'Pm 06:00',
        location: '강남풋살장',
      },
    ];
  }

  private getDefaultLeagueTable(): LeagueTeam[] {
    return [
      {
        name: '최강숏FC',
        logo: this.getTeamLogo('최강숏FC'),
        played: 18,
        wins: 15,
        draws: 2,
        losses: 1,
        points: 47,
        goals: 45,
        conceded: 12,
        difference: 33,
      },
      {
        name: '위더스 FC',
        logo: this.getTeamLogo('위더스 FC'),
        played: 18,
        wins: 12,
        draws: 3,
        losses: 3,
        points: 39,
        goals: 38,
        conceded: 20,
        difference: 18,
      },
      {
        name: '라이온 FC',
        logo: this.getTeamLogo('라이온 FC'),
        played: 18,
        wins: 11,
        draws: 4,
        losses: 3,
        points: 37,
        goals: 35,
        conceded: 22,
        difference: 13,
      },
    ];
  }

  private getDefaultUpcomingMatches(): Match[] {
    return [
      {
        uid: 'default-upcoming-1',
        date: '05월 10일',
        day: '토요일',
        time: '19:00',
        location: '위더스풋살장',
        homeTeam: '위더스 FC',
        awayTeam: '아란치 FC',
        homeLogo: this.getTeamLogo('위더스 FC'),
        awayLogo: this.getTeamLogo('아란치 FC'),
      },
    ];
  }

  private getDefaultRecentMatches(): Match[] {
    return [
      {
        uid: 'default-recent-1',
        date: '05월 01일',
        day: '목요일',
        time: '15:00',
        location: '송도풋살장',
        homeTeam: '위더스 FC',
        awayTeam: '아란치 FC',
        homeLogo: this.getTeamLogo('위더스 FC'),
        awayLogo: this.getTeamLogo('아란치 FC'),
        homeScore: 2,
        awayScore: 1,
      },
    ];
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
    this.$router.push({
      path: `/match-detail/${match.uid}`,
      query: { type: 'match' },
    });
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Home Page Specific Styles section */
</style>
