<template>
  <div class="main league-page match-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <!-- 소속 팀이 있는 경우 팀 정보 표시 -->
        <template v-if="myTeamInfo">
          <h2 class="section-title">나의 팀</h2>
          <div class="my-team-card">
            <img
              :src="myTeamInfo.logoUrl || getTeamLogo(myTeamInfo.name)"
              :alt="myTeamInfo.name"
              class="my-team-logo"
            >
            <div class="my-team-info">
              <div class="my-team-name">{{ myTeamInfo.name }}</div>
              <div class="my-team-code">코드: {{ myTeamInfo.teamCode }}</div>
            </div>
          </div>
        </template>
        <!-- 소속 팀이 없는 경우 팀 생성/가입 영역 -->
        <template v-else>
          <h2 class="section-title">나의 팀을 만들어 보세요!</h2>
          <el-button
            style="margin-bottom: 15px;"
            :disabled="!canCreateTeam"
            @click="goToCreateTeam"
          >
            {{ canCreateTeam ? '팀 만들기' : '이미 팀을 생성하였습니다' }}
          </el-button>

          <!-- Team Code Input Form -->
          <div class="team-code-form">
            <div class="team-code-input-wrapper">
              <i class="el-icon-lock"></i>
              <input
                v-model="teamCode"
                type="text"
                class="team-code-input"
                placeholder="팀 코드를 입력하세요."
                :disabled="!canJoinTeam"
                @keyup.enter="joinTeamWithCode"
              >
            </div>
          </div>
          <p v-if="hasPendingRequest" class="pending-notice">
            <i class="el-icon-warning-outline"></i> 팀 가입 대기 중입니다.
          </p>
        </template>
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
import { Vue, Component } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import {
  getTeamByCode,
  joinTeam,
  checkMembershipStatus,
  getMyTeams,
  MembershipStatus,
} from '@/api/team';
import { getMatchesByDateRange } from '@/api/match';
import { getGuestRecruitmentsByDateRange } from '@/api/guest';

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

interface MyTeam {
  uid: string
  name: string
  teamCode: string
  logoUrl?: string
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

  private currentYear = 2025

  private currentMonthIndex = 0

  private touchStartX = 0

  private touchEndX = 0

  private teamCode = ''

  private myTeamInfo: MyTeam | null = null

  private canCreateTeam = true

  private canJoinTeam = true

  private hasPendingRequest = false

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  private slickOptions = {
    dots: false,
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
  }

  private teamCards: TeamCard[] = [
    {
      name: '대성풋살클럽',
      logo: 'https://ui-avatars.com/api/?name=DS&background=061da1&color=fff&size=60',
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
      logo: 'https://ui-avatars.com/api/?name=GN&background=0066cc&color=fff&size=60',
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
      logo: 'https://ui-avatars.com/api/?name=SU&background=cc0000&color=fff&size=60',
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
      logo: 'https://ui-avatars.com/api/?name=IC&background=0099ff&color=fff&size=60',
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
      logo: 'https://ui-avatars.com/api/?name=GG&background=ff6600&color=fff&size=60',
      league: 'A리그',
      manner: 4.7,
      matchType: '친선 경기',
      teamSize: '5 대 5',
      matchDate: '05월 13일',
      matchDay: '화',
      matchTime: 'Pm 07:30',
      location: '경기풋살장',
    },
  ]

  private leagueTable: LeagueTeam[] = [
    {
      name: '최강숏FC',
      logo: 'https://ui-avatars.com/api/?name=CK&background=ffd700&color=000&size=40',
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
      logo: 'https://ui-avatars.com/api/?name=WD&background=061da1&color=fff&size=40',
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
      logo: 'https://ui-avatars.com/api/?name=LN&background=ff8800&color=fff&size=40',
      played: 18,
      wins: 11,
      draws: 4,
      losses: 3,
      points: 37,
      goals: 35,
      conceded: 22,
      difference: 13,
    },
    {
      name: '아란치 FC',
      logo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
      played: 18,
      wins: 10,
      draws: 5,
      losses: 3,
      points: 35,
      goals: 32,
      conceded: 24,
      difference: 8,
    },
    {
      name: '진주고 FC',
      logo: 'https://ui-avatars.com/api/?name=JJ&background=00cc66&color=fff&size=40',
      played: 18,
      wins: 9,
      draws: 6,
      losses: 3,
      points: 33,
      goals: 30,
      conceded: 25,
      difference: 5,
    },
  ]

  private recentMatches: Match[] = [
    {
      date: '05월 01일',
      day: '목요일',
      time: '15:00',
      location: '송도풋살장',
      homeTeam: '위더스 FC',
      awayTeam: '아란치 FC',
      homeLogo: 'https://ui-avatars.com/api/?name=WD&background=061da1&color=fff&size=40',
      awayLogo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
      homeScore: 2,
      awayScore: 1,
    },
    {
      date: '05월 09일',
      day: '금요일',
      time: '18:00',
      location: '송도풋살장',
      homeTeam: '최강숏 FC',
      awayTeam: '아란치 FC',
      homeLogo: 'https://ui-avatars.com/api/?name=CK&background=ffd700&color=000&size=40',
      awayLogo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
      homeScore: 5,
      awayScore: 2,
    },
  ]

  private upcomingMatches: Match[] = [
    {
      date: '05월 10일',
      day: '토요일',
      time: '19:00',
      location: '위더스풋살장',
      homeTeam: '위더스 FC',
      awayTeam: '아란치 FC',
      homeLogo: 'https://ui-avatars.com/api/?name=WD&background=061da1&color=fff&size=40',
      awayLogo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
    },
    {
      date: '05월 11일',
      day: '일요일',
      time: '14:00',
      location: '송도풋살장',
      homeTeam: '라이온 FC',
      awayTeam: '진주고 FC',
      homeLogo: 'https://ui-avatars.com/api/?name=LN&background=ff8800&color=fff&size=40',
      awayLogo: 'https://ui-avatars.com/api/?name=JJ&background=00cc66&color=fff&size=40',
    },
  ]

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

  private guestData: any[] = [
    {
      name: '인천블루스',
      logo: 'https://ui-avatars.com/api/?name=IC&background=0099ff&color=fff&size=60',
      league: 'B리그',
      manner: 4.6,
      matchType: '정규 경기',
      teamSize: '5 대 5',
      matchDate: '11월 22일',
      matchDay: '월',
      matchTime: 'Pm 08:00',
      location: '아란치FC',
      date: new Date(2025, 10, 22),
      teamLogo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
      currentMembers: 2,
      maxMembers: 5,
    },
    {
      name: '경기타이탄',
      logo: 'https://ui-avatars.com/api/?name=GG&background=ff6600&color=fff&size=60',
      league: 'A리그',
      manner: 4.7,
      matchType: '친선 경기',
      teamSize: '5 대 5',
      matchDate: '11월 23일',
      matchDay: '화',
      matchTime: 'Pm 07:30',
      location: '아란치FC',
      date: new Date(2025, 10, 23),
      teamLogo: 'https://ui-avatars.com/api/?name=AR&background=ff6600&color=fff&size=40',
      currentMembers: 5,
      maxMembers: 5,
      isRecruitmentClosed: true,
    },
  ]

  private selectedDate: Date = new Date()

  private selectedDay: number = new Date().getDate()

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

  private async joinTeamWithCode(): Promise<void> {
    if (!this.teamCode.trim()) {
      this.$message.warning('팀 코드를 입력해주세요.');
      return;
    }

    // BR-01/BR-02: 가입 가능 여부 확인
    if (!this.canJoinTeam) {
      this.$message.warning('이미 소속된 팀이 있거나, 가입 대기 중인 팀이 있습니다.');
      return;
    }

    try {
      // 팀 코드로 팀 조회
      const teamResponse = await getTeamByCode(this.teamCode);
      const team = teamResponse.data;

      // 팀 가입 신청
      await joinTeam({
        teamUid: team.uid,
        message: '팀 코드를 통한 가입 신청',
      });

      this.$message.success(`"${team.name}" 팀에 가입 신청했습니다.`);
      this.teamCode = '';
      this.hasPendingRequest = true;
      this.canJoinTeam = false;
    } catch (error: any) {
      console.error('Team join failed:', error);
      this.$message.error(error.response?.data?.message || '팀을 찾을 수 없습니다.');
    }
  }

  private goToCreateTeam(): void {
    // BR-01: 팀 생성 가능 여부 확인
    if (!this.canCreateTeam) {
      this.$message.warning('이미 팀을 생성하였습니다. 1계정 1팀만 생성 가능합니다.');
      return;
    }
    this.$router.push('/create-team');
  }

  async created() {
    this.currentYear = new Date().getFullYear();
    this.currentMonthIndex = new Date().getMonth();
    this.selectedDay = new Date().getDate();
    this.selectedDate = new Date();
    await Promise.all([
      this.loadGuestData(),
      this.loadMembershipStatus(),
    ]);
  }

  private async loadMembershipStatus(): Promise<void> {
    try {
      const response = await checkMembershipStatus();
      const status = response.data as MembershipStatus;
      this.canCreateTeam = status.canCreateTeam;
      this.canJoinTeam = status.canJoinTeam;
      this.hasPendingRequest = status.hasPendingRequest;

      // 소속 팀이 있는 경우 팀 정보 로드
      if (status.hasTeam) {
        const teamsResponse = await getMyTeams();
        if (teamsResponse.data && teamsResponse.data.length > 0) {
          const team = teamsResponse.data[0];
          this.myTeamInfo = {
            uid: team.uid,
            name: team.name,
            teamCode: team.teamCode,
            logoUrl: team.logoUrl || team.logoFileUid,
          };
        }
      }
    } catch (error) {
      console.warn('Membership status check failed:', error);
    }
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=061da1&color=fff&size=60`;
  }

  private async loadGuestData(): Promise<void> {
    try {
      const today = new Date();
      const startDate = today.toISOString().split('T')[0];
      const endDate = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

      const response = await getGuestRecruitmentsByDateRange(startDate, endDate);
      if (response.data) {
        const guests = response.data.content || response.data || [];
        const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

        this.guestData = guests.map((guest: any) => {
          const matchDate = new Date(guest.matchDate);
          const isFull = guest.currentGuests >= guest.maxGuests;

          return {
            uid: guest.uid,
            name: guest.teamName,
            logo: guest.teamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(guest.teamName?.substring(0, 2) || 'T')}&background=random&color=fff&size=60`,
            league: '',
            manner: guest.teamMannerScore || 0,
            matchType: guest.matchType === 'FRIENDLY' ? '친선 경기' : '자체 경기',
            teamSize: this.formatMatchFormat(guest.matchFormat),
            matchDate: `${String(matchDate.getMonth() + 1).padStart(2, '0')}월 ${String(matchDate.getDate()).padStart(2, '0')}일`,
            matchDay: dayNames[matchDate.getDay()],
            matchTime: guest.matchTime,
            location: guest.stadiumName,
            date: matchDate,
            teamLogo: guest.teamLogoUrl,
            currentMembers: guest.currentGuests,
            maxMembers: guest.maxGuests,
            isRecruitmentClosed: isFull,
          };
        });
      }
    } catch (error) {
      console.error('Failed to load guest data:', error);
    }
  }

  private formatMatchFormat(format: string): string {
    const formatMap: { [key: string]: string } = {
      FOUR_VS_FOUR: '4 대 4',
      FIVE_VS_FIVE: '5 대 5',
      SIX_VS_SIX: '6 대 6',
      SEVEN_VS_SEVEN: '7 대 7',
    };
    return formatMap[format] || format;
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

.my-team-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.my-team-logo {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.my-team-info {
  flex: 1;
}

.my-team-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.my-team-code {
  font-size: 13px;
  color: #999;
}

.pending-notice {
  font-size: 13px;
  color: #e6a23c;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
