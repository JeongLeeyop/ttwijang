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
                <span class="tag">{{ team.positionLabel }}</span>
                <span class="tag">매너 {{ team.manner }}점</span>
                <span class="tag">{{ team.feeLabel }}</span>
              </div>
              <div class="team-match-info">
                {{ team.matchDate }} ({{ team.matchDay }}) {{ team.matchTime }}
              </div>
              <div class="guest-location-row">
                <div class="guest-location">
                  <span>{{ team.location }}</span>
                  <i class="el-icon-arrow-right"></i>
                </div>
                <div class="guest-members" v-if="team.currentMembers !== undefined && team.maxMembers !== undefined">
                  <i class="el-icon-user"></i>
                  <span>{{ team.currentMembers }} / {{ team.maxMembers }}</span>
                </div>
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
          <div v-if="filteredGuests.length === 0" class="no-guest-message">
            <p>선택한 날짜에 게스트 모집이 없습니다.</p>
          </div>
          <div class="guest-list" v-else>
              <div
                v-for="(guest, index) in filteredGuests"
                :key="index"
                class="team-card"
                :class="{ 'recruitment-closed': guest.isRecruitmentClosed }"
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
  getGuestRecruitmentList,
} from '@/api/guest';

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
  positionLabel: string
  manner: number
  feeLabel: string
  guaranteedLabel: string
  matchDate: string
  matchDay: string
  matchTime: string
  location: string
  date: Date
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

  private showLeagueStatus = false

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  get currentMonth(): string {
    return `${this.currentYear}.${String(this.currentMonthIndex + 1).padStart(2, '0')}`;
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

  private guestData: GuestItem[] = []

  private selectedDate: Date = new Date()

  private selectedDay: number = new Date().getDate()

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
      const params: any = {
        status: 'RECRUITING',
      };
      if (this.selectedRegion) {
        params.regionSido = '경남';
        params.regionSigungu = this.selectedRegion;
      }
      const response = await getGuestRecruitmentList(params);
      const recruitments = response.data?.content || response.data || [];

      this.teamCards = recruitments.slice(0, 5).map((guest: any) => {
        const matchDate = new Date(guest.matchDate);
        const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

        return {
          name: guest.teamName || '팀',
          logo: guest.teamLogoUrl || this.getTeamLogo(guest.teamName),
          manner: guest.teamMannerScore || 0,
          positionLabel: this.formatPositionType(guest.positionType),
          feeLabel: this.formatFee(guest.fee),
          matchDate: this.formatDate(guest.matchDate),
          matchDay: dayNames[matchDate.getDay()],
          matchTime: this.formatTime(guest.matchTime),
          location: guest.stadiumName || '',
          currentMembers: guest.currentGuests || 0,
          maxMembers: guest.maxGuests || 0,
        };
      });
    } catch (error) {
      console.warn('추천 카드 로드 실패:', error);
      this.teamCards = [];
    }
  }

  private async loadGuestData(): Promise<void> {
    try {
      const startDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-01`;
      const lastDay = new Date(this.currentYear, this.currentMonthIndex + 1, 0).getDate();
      const endDate = `${this.currentYear}-${String(this.currentMonthIndex + 1).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`;

      const regionParams: any = {};
      if (this.selectedRegion) {
        regionParams.regionSido = '경남';
        regionParams.regionSigungu = this.selectedRegion;
      }

      const response = await getGuestRecruitmentsByDateRange(startDate, endDate, regionParams);
      const guests = response.data?.content || response.data || [];
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      this.guestData = guests.map((guest: any) => {
        const matchDate = new Date(guest.matchDate);
        const isFull = (guest.currentGuests || 0) >= (guest.maxGuests || 1)
          || guest.status === 'COMPLETED'
          || guest.status === 'CLOSED';

        return {
          uid: guest.uid,
          name: guest.teamName || '',
          logo: guest.teamLogoUrl || this.getTeamLogo(guest.teamName),
          positionLabel: this.formatPositionType(guest.positionType),
          manner: guest.teamMannerScore || 0,
          feeLabel: this.formatFee(guest.fee),
          guaranteedLabel: this.formatGuaranteedMinutes(guest.guaranteedMinutes),
          matchDate: this.formatDate(guest.matchDate),
          matchDay: dayNames[matchDate.getDay()],
          matchTime: this.formatTime(guest.matchTime),
          location: guest.stadiumName || '',
          date: matchDate,
          teamLogo: guest.teamLogoUrl || this.getTeamLogo(guest.teamName),
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

  private getTeamLogo(teamName: string): string {
    if (!teamName) return 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=60';
    const initials = teamName.slice(0, 2);
    const bgColors = ['061da1', '0066cc', 'cc0000', 'ff6600', '00cc66', 'ffd700'];
    const colorIndex = teamName.charCodeAt(0) % bgColors.length;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=${bgColors[colorIndex]}&color=fff&size=60`;
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

  get filteredGuests(): GuestItem[] {
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

.no-guest-message {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 14px;
}
</style>
