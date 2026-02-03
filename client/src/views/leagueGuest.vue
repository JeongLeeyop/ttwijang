<template>
  <div class="main league-page guest-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">추천 매치를 확인해 보세요!</h2>
        <VueSlickCarousel
          v-if="teamCards.length > 0"
          v-bind="slickOptions"
          class="team-cards-container"
        >
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
        <div class="league-section-handle" @click="toggleLeagueSection">
          <div class="handle-bar"></div>
        </div>
        <div class="league-section-content">
        <div class="league-header">
          <div class="calendar-nav">
          <i class="el-icon-arrow-left" @click="previousMonth"></i>
          <span class="current-month">{{ currentMonth }}</span>
          <i class="el-icon-arrow-right" @click="nextMonth"></i>
        </div>
        </div>

        <!-- Date Selector Row -->
        <div class="date-selector-row">
          <div class="date-item"
               v-for="day in getDaysInMonth(currentYear, currentMonthIndex)"
               :key="day"
               :class="{ 'selected': isSelectedDate(day) }"
               @click="selectDate(day)">
            <div class="day-label">{{ getDayLabel(day) }}</div>
            <div class="day-number">{{ day }}</div>
          </div>
        </div>

        <!-- Upcoming Match Cards -->
        <div class="match-cards">
          <div class="guest-list">
              <div
                v-for="(guest, index) in guestData"
                :key="index"
                class="team-card"
                :class="{ 'recruitment-closed': guest.isRecruitmentClosed }"
              >
                <div class="team-card-left">
                  <img :src="guest.logo" :alt="guest.name" class="team-logo">
                </div>
                <div class="team-card-right">
                  <div class="team-tags">
                    <span class="tag">{{ guest.league }}</span>
                    <span class="tag">매너 {{ guest.manner }}점</span>
                    <span class="tag">{{ guest.matchType }}</span>
                    <span class="tag">{{ guest.teamSize }}</span>
                  </div>
                  <div class="team-match-info">
                    {{ guest.matchDate }} ({{ guest.matchDay }}) {{ guest.matchTime }}
                  </div>
                  <div class="guest-location-row">
                    <div class="guest-location">
                      <img v-if="guest.teamLogo" :src="guest.teamLogo" :alt="guest.name" class="team-icon">
                      <span>{{ guest.location }}</span>
                      <i class="el-icon-arrow-right"></i>
                    </div>
                    <div class="guest-members" v-if="guest.currentMembers !== undefined && guest.maxMembers !== undefined">
                      <i class="el-icon-user"></i>
                      <span>{{ guest.currentMembers }} / {{ guest.maxMembers }}</span>
                    </div>
                  </div>
                </div>
                <div v-if="guest.isRecruitmentClosed" class="recruitment-overlay">
                  <div class="recruitment-status">모집완료</div>
                  <img v-if="guest.teamLogo" :src="guest.teamLogo" :alt="guest.name" class="small-team-logo">
                </div>
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
import { Vue, Component, Watch } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import { getGuestRecruitmentsByDateRange } from '@/api/guest';
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
  private selectedLeague = 'a-league'

  private showLeagueStatus = false

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  private slickOptions = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    centerMode: true,
    centerPadding: '20px',
    swipeToSlide: true,
    touchThreshold: 5,
    initialSlide: 0,
    variableWidth: true,
    autoPlay: true,
  }

  private teamCards: TeamCard[] = []

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  private upcomingMatches: Match[] = []

  private guestData: any[] = []

  private selectedDate: Date = new Date()

  private selectedDay: number = new Date().getDate()

  async created() {
    await this.loadData();
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    await this.loadGuestData();
  }

  @Watch('selectedDate')
  async onDateChange() {
    await this.loadGuestData();
  }

  private async loadData(): Promise<void> {
    this.isLoading = true;
    try {
      await Promise.all([
        this.loadTeamCards(),
        this.loadGuestData(),
      ]);
    } catch (error) {
      console.error('데이터 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadTeamCards(): Promise<void> {
    try {
      const response = await getMatchList({ status: 'OPEN' });
      if (response.data && response.data.content) {
        this.teamCards = response.data.content.slice(0, 5).map((match: any) => ({
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
      if (this.teamCards.length === 0) {
        this.teamCards = this.getDefaultTeamCards();
      }
    } catch (error) {
      console.warn('팀 카드 로드 실패:', error);
      this.teamCards = this.getDefaultTeamCards();
    }
  }

  private async loadGuestData(): Promise<void> {
    try {
      const startDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-01`;
      const endDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-31`;

      const response = await getGuestRecruitmentsByDateRange(startDate, endDate);
      if (response.data && response.data.content) {
        this.guestData = response.data.content.map((guest: any) => ({
          name: guest.teamName || '',
          logo: this.getTeamLogo(guest.teamName),
          league: guest.leagueName || 'A리그',
          manner: guest.teamMannerScore || 4.5,
          matchType: guest.matchType === 'FRIENDLY' ? '친선 경기' : '정규 경기',
          teamSize: this.formatPositionType(guest.positionType),
          matchDate: this.formatDate(guest.matchDate),
          matchDay: this.getDayOfWeek(guest.matchDate),
          matchTime: guest.matchTime || '',
          location: guest.stadiumName || '',
          date: new Date(guest.matchDate),
          teamLogo: this.getTeamLogo(guest.teamName),
          currentMembers: guest.currentGuests || 0,
          maxMembers: guest.maxGuests || 5,
          isRecruitmentClosed: (guest.currentGuests || 0) >= (guest.maxGuests || 5) || guest.status === 'CLOSED',
        }));
      }
      if (this.guestData.length === 0) {
        this.guestData = this.getDefaultGuestData();
      }
    } catch (error) {
      console.warn('게스트 데이터 로드 실패:', error);
      this.guestData = this.getDefaultGuestData();
    }
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

  private formatPositionType(positionType: string): string {
    const positionMap: Record<string, string> = {
      FIELD: '필드',
      GK: '골키퍼',
      ANY: '포지션 무관',
    };
    return positionMap[positionType] || '5 대 5';
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    const bgColors = ['061da1', '0066cc', 'cc0000', 'ff6600', '00cc66', 'ffd700'];
    const colorIndex = teamName.charCodeAt(0) % bgColors.length;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=${bgColors[colorIndex]}&color=fff&size=60`;
  }

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
        name: '서울유나이티드',
        logo: this.getTeamLogo('서울유나이티드'),
        league: 'A리그',
        manner: 4.9,
        matchType: '친선 경기',
        teamSize: '6 대 6',
        matchDate: '05월 11일',
        matchDay: '일',
        matchTime: 'Am 10:00',
        location: '서울풋살장',
      },
      {
        name: '인천블루스',
        logo: this.getTeamLogo('인천블루스'),
        league: 'B리그',
        manner: 4.6,
        matchType: '정규 경기',
        teamSize: '5 대 5',
        matchDate: '05월 12일',
        matchDay: '월',
        matchTime: 'Pm 08:00',
        location: '인천풋살장',
      },
      {
        name: '경기타이탄',
        logo: this.getTeamLogo('경기타이탄'),
        league: 'A리그',
        manner: 4.7,
        matchType: '친선 경기',
        teamSize: '5 대 5',
        matchDate: '05월 13일',
        matchDay: '화',
        matchTime: 'Pm 07:30',
        location: '경기풋살장',
      },
    ];
  }

  private getDefaultGuestData(): any[] {
    return [
      {
        name: '인천블루스',
        logo: this.getTeamLogo('인천블루스'),
        league: 'B리그',
        manner: 4.6,
        matchType: '정규 경기',
        teamSize: '5 대 5',
        matchDate: this.formatDate(new Date().toISOString()),
        matchDay: this.getDayOfWeek(new Date().toISOString()),
        matchTime: 'Pm 08:00',
        location: '아란치FC',
        date: new Date(),
        teamLogo: this.getTeamLogo('아란치FC'),
        currentMembers: 2,
        maxMembers: 5,
        isRecruitmentClosed: false,
      },
    ];
  }

  private toggleLeagueStatus(): void {
    this.showLeagueStatus = !this.showLeagueStatus;
  }

  private toggleLeagueSection(): void {
    this.showLeagueStatus = !this.showLeagueStatus;
  }

  private previousMonth(): void {
    if (this.currentMonthIndex === 0) {
      this.currentMonthIndex = 11;
      this.currentYear -= 1;
    } else {
      this.currentMonthIndex -= 1;
    }
    this.selectedDay = 1;
    this.selectedDate = new Date(this.currentYear, this.currentMonthIndex, 1);
  }

  private nextMonth(): void {
    if (this.currentMonthIndex === 11) {
      this.currentMonthIndex = 0;
      this.currentYear += 1;
    } else {
      this.currentMonthIndex += 1;
    }
    this.selectedDay = 1;
    this.selectedDate = new Date(this.currentYear, this.currentMonthIndex, 1);
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

  get filteredGuests(): any[] {
    return this.guestData.filter((guest) => this.isSameDate(guest.date, this.selectedDate));
  }

  private isSameDate(date1: Date, date2: Date): boolean {
    return date1.getFullYear() === date2.getFullYear()
      && date1.getMonth() === date2.getMonth()
      && date1.getDate() === date2.getDate();
  }

  private getDaysInMonth(year: number, month: number): number[] {
    const daysCount = new Date(year, month + 1, 0).getDate();
    return Array.from({ length: daysCount }, (_, i) => i + 1);
  }

  private getDayLabel(day: number): string {
    const date = new Date(this.currentYear, this.currentMonthIndex, day);
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    return days[date.getDay()];
  }

  private selectDate(day: number): void {
    this.selectedDay = day;
    this.selectedDate = new Date(this.currentYear, this.currentMonthIndex, day);
  }

  private isSelectedDate(day: number): boolean {
    return day === this.selectedDay
      && this.currentYear === this.selectedDate.getFullYear()
      && this.currentMonthIndex === this.selectedDate.getMonth();
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Home Page Specific Styles section */
.league-header{
  margin-bottom:0px;
}

.league-page .league-section {
  padding-top: 30px !important;
  overflow: hidden !important;
}
</style>
