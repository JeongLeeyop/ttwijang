<template>
  <div class="main league-page match-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <!-- Loading State for Team Section -->
        <div v-if="isInitialLoading" class="loading-container">
          <i class="el-icon-loading loading-icon"></i>
          <p class="loading-text">팀 정보를 불러오는 중...</p>
        </div>

        <!-- Team Section Content -->
        <template v-else>
          <h2 class="section-title">나의 팀을 만들어 보세요!</h2>
          <el-button
            :class="{ 'team-enter-button': myTeamInfo !== null }"
            style="margin-bottom: 15px;"
            @click="myTeamInfo ? enterMyTeam() : goToCreateTeam()"
          >
            {{ myTeamInfo ? '팀 입장하기' : '팀 만들기' }}
          </el-button>

          <!-- Team Code Input Form -->
          <div class="team-code-form">
            <div class="team-code-input-wrapper">
              <i class="el-icon-lock"></i>
              <input
                v-if="myTeamInfo"
                type="text"
                class="team-code-input"
                :placeholder="myTeamInfo.teamCode"
                readonly
              >
              <input
                v-else
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
          <div v-if="!myTeamInfo" class="no-team-message">
            <p>팀에 소속되면 매치 일정을 확인할 수 있어요!</p>
          </div>
          <div v-else-if="filteredMatches.length === 0" class="no-match-message">
            <p>선택한 날짜에 매치 일정이 없습니다.</p>
          </div>
          <div class="guest-list" v-else>
              <div
                v-for="(match, index) in filteredMatches"
                :key="index"
                class="team-card"
                :class="{ 'recruitment-closed': guest.isRecruitmentClosed }"
                @click="goToMatchDetail(guest)"
              >
                <div class="team-card-left">
                  <img :src="match.logo" :alt="match.name" class="team-logo">
                </div>
                <div class="team-card-right">
                  <div class="team-tags">
                    <span class="tag">{{ match.matchTypeLabel }}</span>
                    <span class="tag">매너 {{ match.manner }}점</span>
                    <span class="tag">{{ match.matchFormatLabel }}</span>
                    <span class="tag match-status-tag" :class="'status-' + match.statusKey">{{ match.statusLabel }}</span>
                  </div>
                  <div class="team-match-info">
                    {{ match.matchDate }} ({{ match.matchDay }}) {{ match.matchTime }}
                  </div>
                  <div class="guest-location-row">
                    <div class="guest-location">
                      <span>{{ match.stadiumName }}</span>
                      <i class="el-icon-arrow-right"></i>
                    </div>
                  </div>
                </div>
                <div v-if="match.isCompleted" class="recruitment-overlay">
                  <div class="recruitment-status">경기 종료</div>
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
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';
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
import { getMyTeamMatches } from '@/api/match';

interface MatchCard {
  uid: string
  name: string
  logo: string
  matchTypeLabel: string
  manner: number
  matchFormatLabel: string
  matchDate: string
  matchDay: string
  matchTime: string
  stadiumName: string
  statusLabel: string
  statusKey: string
  isCompleted: boolean
  date: Date
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

type TeamCard = any

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private showLeagueStatus = false

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private teamCode = ''

  private myTeamInfo: MyTeam | null = null

  private canCreateTeam = true

  private canJoinTeam = true

  private hasPendingRequest = false

  private isLoading = false

  private matchData: MatchCard[] = []

  private selectedDate: Date = new Date()

  private selectedDay: number = new Date().getDate()

  private isInitialLoading = true

  get currentMonth(): string {
    return `${this.currentYear}.${String(this.currentMonthIndex + 1).padStart(2, '0')}`;
  }

  get filteredMatches(): MatchCard[] {
    return this.matchData.filter((m) => this.isSameDate(m.date, this.selectedDate));
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

  private teamCards: TeamCard[] = []

  private leagueTable: LeagueTeam[] = []

  private recentMatches: Match[] = []

  private upcomingMatches: Match[] = []

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

  private async joinTeamWithCode(): Promise<void> {
    if (!this.teamCode.trim()) {
      this.$message.warning('팀 코드를 입력해주세요.');
      return;
    }

    if (!this.canJoinTeam) {
      this.$message.warning('이미 소속된 팀이 있거나, 가입 대기 중인 팀이 있습니다.');
      return;
    }

    try {
      const teamResponse = await getTeamByCode(this.teamCode);
      const team = teamResponse.data;

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
    if (!this.canCreateTeam) {
      this.$message.warning('이미 팀을 생성하였습니다. 1계정 1팀만 생성 가능합니다.');
      return;
    }
    this.$router.push('/create-team');
  }

  private enterMyTeam(): void {
    if (!this.canCreateTeam && this.myTeamInfo) {
      this.$router.push(`/team/${this.myTeamInfo.teamCode}`);
    }
  }

  async created() {
    this.currentYear = new Date().getFullYear();
    this.currentMonthIndex = new Date().getMonth();
    this.selectedDay = new Date().getDate();
    this.selectedDate = new Date();
    await this.loadMembershipStatus();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    if (this.myTeamInfo) {
      await this.loadTeamMatches();
    }
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    if (this.myTeamInfo) {
      await this.loadTeamMatches();
    }
  }

  private async loadMembershipStatus(): Promise<void> {
    try {
      const response = await checkMembershipStatus();
      const status = response.data as MembershipStatus;
      console.log('Membership Status:', status);
      this.canCreateTeam = status.canCreateTeam;
      this.canJoinTeam = status.canJoinTeam;
      this.hasPendingRequest = status.hasPendingRequest;

      const hasCreatedTeam = (status as any).hasCreatedTeam || false;

      // 팀이 있으면 (만들었든 가입했든) 팀 정보 로드
      if (status.hasTeam || hasCreatedTeam) {
        try {
          const teamsResponse = await getMyTeams();
          console.log('My Teams Response:', teamsResponse.data);
          // /api/team/my 는 단일 객체 또는 배열을 반환할 수 있음
          const teamData = teamsResponse.data;
          let team = null;
          if (Array.isArray(teamData)) {
            team = teamData.length > 0 ? teamData[0] : null;
          } else {
            team = teamData;
          }
          if (team && team.uid) {
            this.myTeamInfo = {
              uid: team.uid,
              name: team.name,
              teamCode: team.teamCode,
              logoUrl: team.logoUrl || team.logoFileUid,
            };
            console.log('Set myTeamInfo:', this.myTeamInfo);
          }
        } catch (error) {
          console.warn('Failed to load team info:', error);
        }
      }
    } catch (error) {
      console.warn('Membership status check failed:', error);
    }
  }

  private async loadTeamMatches(): Promise<void> {
    if (!this.myTeamInfo) return;

    this.isLoading = true;
    try {
      const response = await getMyTeamMatches(this.myTeamInfo.uid);
      const matches = response.data?.content || response.data || [];
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      this.matchData = matches.map((match: any) => {
        const matchDate = new Date(match.matchDate);
        const isCompleted = match.status === 'COMPLETED' || match.status === 'CANCELLED';

        return {
          uid: match.uid,
          name: match.hostTeamName || this.myTeamInfo!.name,
          logo: match.hostTeamLogoUrl || this.getTeamLogo(match.hostTeamName || this.myTeamInfo!.name),
          matchTypeLabel: this.formatMatchType(match.matchType),
          manner: match.hostTeamMannerScore || 0,
          matchFormatLabel: this.formatMatchFormat(match.matchFormat),
          matchDate: `${String(matchDate.getMonth() + 1).padStart(2, '0')}월 ${String(matchDate.getDate()).padStart(2, '0')}일`,
          matchDay: dayNames[matchDate.getDay()],
          matchTime: this.formatTime(match.matchTime),
          stadiumName: match.stadiumName || '',
          statusLabel: this.formatStatus(match.status),
          statusKey: (match.status || '').toLowerCase(),
          isCompleted,
          date: matchDate,
        };
      });
    } catch (error) {
      console.error('Failed to load team matches:', error);
      this.matchData = [];
    } finally {
      this.isLoading = false;
    }
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=061da1&color=fff&size=60`;
  }

  private formatMatchType(type: string): string {
    const typeMap: Record<string, string> = {
      FRIENDLY: '친선 경기',
      FREE: '자체 경기',
    };
    return typeMap[type] || type || '';
  }

  private formatMatchFormat(format: string): string {
    const formatMap: Record<string, string> = {
      FOUR_VS_FOUR: '4 대 4',
      FIVE_VS_FIVE: '5 대 5',
      SIX_VS_SIX: '6 대 6',
      SEVEN_VS_SEVEN: '7 대 7',
    };
    return formatMap[format] || format || '';
  }

  private formatStatus(status: string): string {
    const statusMap: Record<string, string> = {
      RECRUITING: '모집중',
      MATCHED: '매칭완료',
      IN_PROGRESS: '진행중',
      COMPLETED: '경기종료',
      CANCELLED: '취소됨',
    };
    return statusMap[status] || status || '';
  }

  private formatTime(time: string): string {
    if (!time) return '';
    const parts = time.split(':');
    if (parts.length < 2) return time;
    const hour = parseInt(parts[0], 10);
    const minute = parts[1];
    const period = hour < 12 ? 'Am' : 'Pm';
    const displayHour = hour > 12 ? hour - 12 : hour;
    return `${period} ${String(displayHour).padStart(2, '0')}:${minute}`;
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

/* Loading State */
.team-section .loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  padding: 40px 20px;
}

.team-section .loading-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 12px;
}

.team-section .loading-text {
  font-size: 14px;
  color: #606266;
  margin: 0;
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

.no-team-message,
.no-match-message {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 14px;
}

.match-status-tag {
  font-weight: 600;
}

.match-status-tag.status-recruiting {
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
  border-color: rgba(64, 158, 255, 0.3);
}

.match-status-tag.status-matched {
  color: #67c23a;
  background: rgba(103, 194, 58, 0.1);
  border-color: rgba(103, 194, 58, 0.3);
}

.match-status-tag.status-completed {
  color: #909399;
}

.match-status-tag.status-cancelled {
  color: #f56c6c;
}
.team-enter-button {
  background-color: #f7c600 !important;
  border-color: #f7c600 !important;
  color: #ffffff !important;
}

.team-enter-button:hover {
  background-color: #e0b400 !important;
  border-color: #e0b400 !important;
}
</style>
