<template>
  <div class="main league-page guest-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">뛰장 리그를 소개합니다!</h2>
        <div v-if="isLoading && upcomingMatchCards.length === 0" class="loading-container">
          <i class="el-icon-loading"></i> 로딩 중...
        </div>
        <VueSlickCarousel
          v-else-if="upcomingMatchCards.length > 0"
          v-bind="slickOptions"
          class="team-cards-container"
        >
          <div
            v-for="(match, index) in upcomingMatchCards"
            :key="index"
            class="team-card"
          >
            <div class="team-card-left">
              <div class="match-vs">
                <img :src="match.homeTeamLogo" :alt="match.homeTeamName" class="team-logo">
              </div>
            </div>
            <div class="team-card-right">
              <div class="team-tags">
                <span class="tag">{{ match.leagueName }}</span>
                <span v-if="match.round" class="tag">{{ match.round }}R</span>
              </div>
              <div class="team-match-info">
                {{ match.matchDate }} ({{ match.matchDay }}) {{ match.matchTime }}
              </div>
              <div class="team-location">
                {{ match.stadiumName }} <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </VueSlickCarousel>
        <div v-else class="empty-message">
          예정된 리그 경기가 없습니다.
        </div>
      </div>

      <!-- League Schedule Section -->
      <div
        class="league-section"
        :class="{ 'expanded': isExpanded, 'dragging': isDragging }"
        :style="sectionStyle"
      >
        <div
          class="league-section-handle"
          @touchstart.prevent="onDragStart"
          @mousedown.prevent="onDragStart"
        >
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
          <div class="date-item all-btn"
               :class="{ 'selected': showAllMonth }"
               @click="selectAll">
            <div class="day-number">전체</div>
          </div>
          <div class="date-item"
               v-for="day in getDaysInMonth(currentYear, currentMonthIndex)"
               :key="day"
               :class="{ 'selected': !showAllMonth && isSelectedDate(day), 'has-guest': hasGuestOnDay(day) }"
               @click="selectDate(day)">
            <div class="day-label">{{ getDayLabel(day) }}</div>
            <div class="day-number">{{ day }}</div>
            <div v-if="hasGuestOnDay(day)" class="match-dot"></div>
          </div>
        </div>

        <!-- Upcoming Match Cards -->
        <div class="match-cards">
          <div v-if="filteredGuests.length === 0" class="no-guest-message">
            <p>{{ showAllMonth ? '이번 달 게스트 모집이 없습니다.' : '선택한 날짜에 게스트 모집이 없습니다.' }}</p>
          </div>
          <div class="guest-list" v-else>
              <div
                v-for="(guest, index) in filteredGuests"
                :key="index"
                class="team-card"
                :class="{ 'recruitment-closed': guest.isRecruitmentClosed }"
                @click="goToGuestDetail(guest)"
              >
                <div class="team-card-left">
                  <img :src="guest.logo" :alt="guest.name" class="team-logo">
                </div>
                <div class="team-card-right">
                  <div class="team-tags">
                    <span class="tag" v-if="guest.positionLabel">{{ guest.positionLabel }}</span>
                    <span class="tag">매너 {{ guest.manner }}점</span>
                    <span class="tag" v-if="guest.feeLabel">{{ guest.feeLabel }}</span>
                    <span class="tag" v-if="guest.guaranteedLabel">{{ guest.guaranteedLabel }}</span>
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
import {
  Vue,
  Component,
  Watch,
  Prop,
} from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import {
  getGuestRecruitmentsByDateRange,
} from '@/api/guest';
import { getUpcomingLeagueMatches } from '@/api/league';

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

interface TeamCard {
  name: string
  logo: string
  manner: number
  positionLabel: string
  feeLabel: string
  matchDate: string
  matchDay: string
  matchTime: string
  location: string
  currentMembers: number
  maxMembers: number
}

interface GuestItem {
  uid: string
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
  date: Date
}

interface Match {
  uid: string
  date: string
  day: string
  time: string
  location: string
  teamLogo: string | null
  currentMembers: number
  maxMembers: number
  isRecruitmentClosed: boolean
}

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private isExpanded = false

  private isDragging = false

  private dragStartY = 0

  private dragStartTop = 0

  private sectionTop: number | null = null

  private collapsedTop = 253

  private expandedTop = 70

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  get currentMonth(): string {
    return `${this.currentYear}.${String(this.currentMonthIndex + 1).padStart(2, '0')}`;
  }

  get slickOptions() {
    return {
      dots: true,
      infinite: this.upcomingMatchCards.length > 1,
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
      autoplay: this.upcomingMatchCards.length > 1,
      autoplaySpeed: 3000,
      draggable: true,
      swipe: true,
    };
  }

  private upcomingMatchCards: UpcomingMatchCard[] = []

  private teamCards: TeamCard[] = []

  private guestData: GuestItem[] = []

  private selectedDate: Date = new Date()

  private selectedDay: number = new Date().getDate()

  private showAllMonth = true

  async created() {
    await this.loadData();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    await this.loadData();
  }

  @Watch('currentMonthIndex')
  async onMonthChange() {
    await this.loadGuestData();
  }

  @Watch('selectedDate')
  async onDateChange() {
    // filteredGuests computed will update automatically
  }

  private async loadData(): Promise<void> {
    this.isLoading = true;
    try {
      await Promise.all([
        this.loadUpcomingMatches(),
        this.loadGuestData(),
      ]);
    } catch (error) {
      console.error('데이터 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadUpcomingMatches(): Promise<void> {
    try {
      const upcomingParams: any = {
        limit: 20,
      };
      if (this.selectedRegion) {
        upcomingParams.regionCode = this.selectedRegion;
      }
      const upcomingResponse = await getUpcomingLeagueMatches(upcomingParams);
      const upcomingMatches = upcomingResponse.data || [];
      this.upcomingMatchCards = upcomingMatches.map((m: any): UpcomingMatchCard => {
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
      console.warn('리그 일정 로드 실패:', error);
      this.upcomingMatchCards = [];
    }
  }

  private async loadGuestData(): Promise<void> {
    try {
      const startDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-01`;
      const lastDay = new Date(this.currentYear, this.currentMonthIndex + 1, 0).getDate();
      const endDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`;

      const regionParams: any = {};
      if (this.selectedRegion) {
        regionParams.regionCode = this.selectedRegion;
      }

      const response = await getGuestRecruitmentsByDateRange(startDate, endDate, regionParams);
      const allGuests = response.data?.content || response.data || [];
      const guests = allGuests.filter((g: any) => g.status === 'RECRUITING');
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      this.guestData = guests.map((guest: any) => {
        const matchDate = new Date(guest.matchDate);
        const isFull = (guest.currentGuests || 0) >= (guest.maxGuests || 1)
          || guest.status === 'COMPLETED'
          || guest.status === 'CLOSED';

        const logoUrl = (() => {
          const url = guest.teamLogoUrl;
          if (!url) return this.getTeamLogo(guest.teamName);
          if (url.startsWith('http') || url.startsWith('/')) return url;
          return `/api/attached-file/${url}`;
        })();

        return {
          uid: guest.uid,
          name: guest.teamName || '',
          logo: logoUrl,
          positionLabel: this.formatPositionType(guest.positionType),
          manner: guest.teamMannerScore || 0,
          feeLabel: this.formatFee(guest.fee),
          guaranteedLabel: this.formatGuaranteedMinutes(guest.guaranteedMinutes),
          matchDate: this.formatDate(guest.matchDate),
          matchDay: dayNames[matchDate.getDay()],
          matchTime: this.formatTime(guest.matchTime),
          location: guest.stadiumName || '',
          date: matchDate,
          teamLogo: logoUrl,
          currentMembers: guest.currentGuests || 0,
          maxMembers: guest.maxGuests || 0,
          isRecruitmentClosed: isFull,
        };
      });
    } catch (error) {
      console.warn('게스트 데이터 로드 실패:', error);
      this.guestData = [];
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

  private formatPositionType(positionType: string): string {
    const positionMap: Record<string, string> = {
      FIELD: '필드',
      GK: '골키퍼',
      ANY: '포지션 무관',
    };
    return positionMap[positionType] || '포지션 무관';
  }

  private formatFee(fee: number | null | undefined): string {
    if (!fee || fee === 0) return '무료';
    return `${fee.toLocaleString()}원`;
  }

  private formatGuaranteedMinutes(minutes: number | null | undefined): string {
    if (!minutes) return '';
    return `${minutes}분 보장`;
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
    let displayHour = hour;
    if (hour > 12) {
      displayHour = hour - 12;
    } else if (hour === 0) {
      displayHour = 12;
    }
    return `${period} ${String(displayHour).padStart(2, '0')}:${minute}`;
  }

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    const bgColors = ['061da1', '0066cc', 'cc0000', 'ff6600', '00cc66', 'ffd700'];
    const colorIndex = teamName.charCodeAt(0) % bgColors.length;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=${bgColors[colorIndex]}&color=fff&size=60`;
  }

  private toggleLeagueStatus(): void {
    if (this.isExpanded) {
      this.sectionTop = this.collapsedTop;
      this.isExpanded = false;
    } else {
      this.sectionTop = this.expandedTop;
      this.isExpanded = true;
    }
  }

  get sectionStyle(): Record<string, string> {
    if (this.sectionTop !== null) {
      return { top: `${this.sectionTop}px`, transition: this.isDragging ? 'none' : 'top 0.3s ease' };
    }
    return {};
  }

  private onDragStart(e: TouchEvent | MouseEvent): void {
    this.isDragging = true;
    this.dragStartY = 'touches' in e ? e.touches[0].clientY : e.clientY;
    const el = (e.target as HTMLElement).closest('.league-section') as HTMLElement;
    if (el) {
      this.dragStartTop = el.getBoundingClientRect().top;
    }
    document.addEventListener('touchmove', this.onDragMove, { passive: false });
    document.addEventListener('mousemove', this.onDragMove);
    document.addEventListener('touchend', this.onDragEnd);
    document.addEventListener('mouseup', this.onDragEnd);
  }

  private onDragMove(e: TouchEvent | MouseEvent): void {
    if (!this.isDragging) return;
    if ('cancelable' in e && e.cancelable) e.preventDefault();
    const clientY = 'touches' in e ? (e as TouchEvent).touches[0].clientY : (e as MouseEvent).clientY;
    const delta = clientY - this.dragStartY;
    let newTop = this.dragStartTop + delta;
    newTop = Math.max(this.expandedTop, Math.min(newTop, this.collapsedTop));
    this.sectionTop = newTop;
  }

  private onDragEnd(): void {
    this.isDragging = false;
    document.removeEventListener('touchmove', this.onDragMove);
    document.removeEventListener('mousemove', this.onDragMove);
    document.removeEventListener('touchend', this.onDragEnd);
    document.removeEventListener('mouseup', this.onDragEnd);
    if (this.sectionTop === null) return;
    const mid = (this.collapsedTop + this.expandedTop) / 2;
    if (this.sectionTop < mid) {
      this.sectionTop = this.expandedTop;
      this.isExpanded = true;
    } else {
      this.sectionTop = this.collapsedTop;
      this.isExpanded = false;
    }
  }

  private toggleLeagueSection(): void {
    if (this.isExpanded) {
      this.sectionTop = this.collapsedTop;
      this.isExpanded = false;
    } else {
      this.sectionTop = this.expandedTop;
      this.isExpanded = true;
    }
  }

  private previousMonth(): void {
    if (this.currentMonthIndex === 0) {
      this.currentMonthIndex = 11;
      this.currentYear -= 1;
    } else {
      this.currentMonthIndex -= 1;
    }
    this.showAllMonth = true;
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
    this.showAllMonth = true;
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
    this.$router.push({
      path: `/match-detail/${match.uid}`,
      query: { type: 'guest' },
    });
  }

  private goToGuestDetail(guest: any): void {
    if (guest.isRecruitmentClosed) return;
    this.$router.push({
      path: `/match-detail/${guest.uid}`,
      query: { type: 'guest' },
    });
  }

  get guestDaysSet(): Set<number> {
    const days = new Set<number>();
    this.guestData.forEach((g: any) => {
      if (g.date
        && g.date.getFullYear() === this.currentYear
        && g.date.getMonth() === this.currentMonthIndex) {
        days.add(g.date.getDate());
      }
    });
    return days;
  }

  private hasGuestOnDay(day: number): boolean {
    return this.guestDaysSet.has(day);
  }

  get filteredGuests(): any[] {
    if (this.showAllMonth) {
      return this.guestData
        .filter((g: any) => g.date
          && g.date.getFullYear() === this.currentYear
          && g.date.getMonth() === this.currentMonthIndex)
        .sort((a: any, b: any) => a.date.getTime() - b.date.getTime());
    }
    return this.guestData.filter((guest) => guest.date && this.isSameDate(guest.date, this.selectedDate));
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

  private selectAll(): void {
    this.showAllMonth = true;
  }

  private selectDate(day: number): void {
    this.showAllMonth = false;
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

.no-guest-message {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 14px;
}

/* All button in date selector */
.date-item.all-btn {
  min-width: 44px;
  flex-shrink: 0;
  align-items: center;
  left: 0;
  z-index: 2;
  background: #f5f5f5;
}

.date-item.all-btn .day-number {
  font-size: 12px;
  font-weight: 700;
}

.date-item.all-btn.selected {
  background: #061da1;
}

.date-item.all-btn.selected .day-number {
  color: #fff;
}

/* Match dot indicator on calendar dates */
.date-item {
  position: relative;
}

.match-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #061da1;
  margin: 2px auto 0;
}

.date-item.selected .match-dot {
  background: #fff;
}

.date-item.has-guest .day-number {
  font-weight: 700;
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

.team-location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #666;
}

.team-location i {
  font-size: 12px;
}
</style>
