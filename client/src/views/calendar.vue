<template>
  <div class="main">
    <!-- Header -->
    <div class="header">
      <div class="header-left">
        <i class="el-icon-d-arrow-left" @click="goBack"></i>
      </div>
      <div class="header-center">
        <span class="header-title">경기 일정</span>
      </div>
      <div class="header-right"></div>
    </div>

    <!-- Content -->
    <div class="content">
      <!-- Calendar Section -->
      <div class="calendar-section">
        <!-- Calendar Navigation -->
        <div class="calendar-nav">
          <i class="el-icon-arrow-left" @click="previousMonth"></i>
          <span class="current-month" @click="showYearMonthPicker = true">{{ currentMonth }}</span>
          <i class="el-icon-arrow-right" @click="nextMonth"></i>
        </div>

        <!-- Calendar Grid -->
        <div class="calendar-grid">
          <div class="calendar-weekdays">
            <div class="weekday">일</div>
            <div class="weekday">월</div>
            <div class="weekday">화</div>
            <div class="weekday">수</div>
            <div class="weekday">목</div>
            <div class="weekday">금</div>
            <div class="weekday">토</div>
          </div>
          <div class="calendar-days">
            <div
              v-for="(day, index) in calendarDays"
              :key="index"
              class="calendar-day"
              :class="{
                'other-month': !day.isCurrentMonth,
                'selected': day.isSelected,
                'today': day.isToday
              }"
              @click="selectDate(day)"
            >
              <span class="day-number">{{ day.day }}</span>
              <span v-if="day.isToday" class="today-label">TODAY</span>
              <span class="day-counter">{{ getCountForDate(day.date) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Tabs Section -->
      <div class="tabs-section">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="매치 구함" name="match">
            <div class="match-list">
              <div
                v-for="(match, index) in filteredMatches"
                :key="index"
                class="team-card"
              >
                <div class="team-card-left">
                  <img :src="match.logo" :alt="match.name" class="team-logo">
                </div>
                <div class="team-card-right">
                  <div class="team-tags">
                    <span class="tag">{{ match.league }}</span>
                    <span class="tag">매너 {{ match.manner }}점</span>
                    <span class="tag">{{ match.matchType }}</span>
                    <span class="tag">{{ match.teamSize }}</span>
                  </div>
                  <div class="team-match-info">
                    {{ match.matchDate }} ({{ match.matchDay }}) {{ match.matchTime }}
                  </div>
                  <div class="team-location">
                    {{ match.location }} <i class="el-icon-arrow-right"></i>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="게스트 구함" name="guest">
            <div class="guest-list">
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
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- Year/Month Picker Dialog -->
    <el-dialog
      :visible.sync="showYearMonthPicker"
      width="80%"
      :show-close="false"
      custom-class="year-month-dialog"
    >
      <div class="year-month-picker">
        <div class="picker-section">
          <h3>년도</h3>
          <el-scrollbar class="year-scroll">
            <div
              v-for="year in years"
              :key="year"
              class="picker-item"
              :class="{ 'active': year === selectedYear }"
              @click="selectedYear = year"
            >
              {{ year }}년
            </div>
          </el-scrollbar>
        </div>
        <div class="picker-section">
          <h3>월</h3>
          <el-scrollbar class="month-scroll">
            <div
              v-for="month in 12"
              :key="month"
              class="picker-item"
              :class="{ 'active': month === selectedMonth }"
              @click="selectedMonth = month"
            >
              {{ month }}월
            </div>
          </el-scrollbar>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showYearMonthPicker = false">취소</el-button>
        <el-button type="primary" @click="applyYearMonth">확인</el-button>
      </div>
    </el-dialog>

    <!-- Footer -->
    <FooterNav />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import FooterNav from '@/components/FooterNav.vue';

interface CalendarDay {
  day: number
  date: Date
  isCurrentMonth: boolean
  isSelected: boolean
  isToday: boolean
}

interface MatchData {
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
  date: Date
  teamLogo?: string
  currentMembers?: number
  maxMembers?: number
  isRecruitmentClosed?: boolean
}

@Component({
  components: {
    FooterNav,
  },
})
export default class extends Vue {
  private activeTab = 'match'

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private selectedDate: Date = new Date()

  private showYearMonthPicker = false

  private selectedYear = new Date().getFullYear()

  private selectedMonth = new Date().getMonth() + 1

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  get years(): number[] {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let i = currentYear - 5; i <= currentYear + 5; i += 1) {
      years.push(i);
    }
    return years;
  }

  get calendarDays(): CalendarDay[] {
    const days: CalendarDay[] = [];
    const firstDay = new Date(this.currentYear, this.currentMonthIndex, 1);
    const lastDay = new Date(this.currentYear, this.currentMonthIndex + 1, 0);
    const prevLastDay = new Date(this.currentYear, this.currentMonthIndex, 0);

    const firstDayOfWeek = firstDay.getDay();
    const lastDateOfMonth = lastDay.getDate();
    const prevLastDate = prevLastDay.getDate();

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // Previous month days
    for (let i = firstDayOfWeek - 1; i >= 0; i -= 1) {
      const day = prevLastDate - i;
      const date = new Date(this.currentYear, this.currentMonthIndex - 1, day);
      days.push({
        day,
        date,
        isCurrentMonth: false,
        isSelected: this.isSameDate(date, this.selectedDate),
        isToday: this.isSameDate(date, today),
      });
    }

    // Current month days
    for (let i = 1; i <= lastDateOfMonth; i += 1) {
      const date = new Date(this.currentYear, this.currentMonthIndex, i);
      days.push({
        day: i,
        date,
        isCurrentMonth: true,
        isSelected: this.isSameDate(date, this.selectedDate),
        isToday: this.isSameDate(date, today),
      });
    }

    // Next month days
    const remainingDays = 42 - days.length;
    for (let i = 1; i <= remainingDays; i += 1) {
      const date = new Date(this.currentYear, this.currentMonthIndex + 1, i);
      days.push({
        day: i,
        date,
        isCurrentMonth: false,
        isSelected: this.isSameDate(date, this.selectedDate),
        isToday: this.isSameDate(date, today),
      });
    }

    return days;
  }

  private matchData: MatchData[] = [
    {
      name: '대성풋살클럽',
      logo: 'https://ui-avatars.com/api/?name=DS&background=061da1&color=fff&size=60',
      league: 'B리그',
      manner: 4.8,
      matchType: '친선 경기',
      teamSize: '5 대 5',
      matchDate: '11월 29일',
      matchDay: '금',
      matchTime: 'Pm 07:00',
      location: '대성풋살장',
      date: new Date(2025, 10, 29),
    },
    {
      name: '강남FC',
      logo: 'https://ui-avatars.com/api/?name=GN&background=0066cc&color=fff&size=60',
      league: 'A리그',
      manner: 4.5,
      matchType: '정규 경기',
      teamSize: '5 대 5',
      matchDate: '11월 21일',
      matchDay: '토',
      matchTime: 'Pm 06:00',
      location: '강남풋살장',
      date: new Date(2025, 10, 21),
    },
    {
      name: '서울유나이티드',
      logo: 'https://ui-avatars.com/api/?name=SU&background=cc0000&color=fff&size=60',
      league: 'A리그',
      manner: 4.9,
      matchType: '친선 경기',
      teamSize: '6 대 6',
      matchDate: '11월 21일',
      matchDay: '일',
      matchTime: 'Am 10:00',
      location: '서울풋살장',
      date: new Date(2025, 10, 21),
    },
  ]

  private guestData: MatchData[] = [
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

  get filteredMatches(): MatchData[] {
    return this.matchData.filter((match) => this.isSameDate(match.date, this.selectedDate));
  }

  get filteredGuests(): MatchData[] {
    return this.guestData.filter((guest) => this.isSameDate(guest.date, this.selectedDate));
  }

  private isSameDate(date1: Date, date2: Date): boolean {
    return date1.getFullYear() === date2.getFullYear()
      && date1.getMonth() === date2.getMonth()
      && date1.getDate() === date2.getDate();
  }

  private selectDate(day: CalendarDay): void {
    this.selectedDate = new Date(day.date);
    if (!day.isCurrentMonth) {
      if (day.day > 15) {
        this.previousMonth();
      } else {
        this.nextMonth();
      }
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

  private applyYearMonth(): void {
    this.currentYear = this.selectedYear;
    this.currentMonthIndex = this.selectedMonth - 1;
    this.showYearMonthPicker = false;
  }

  private handleTabClick(): void {
    // Handle tab click if needed
  }

  private getCountForDate(date: Date): any {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // 오늘 이후 날짜만 카운트 표시
    if (date < today) {
      return '';
    }

    if (this.activeTab === 'match') {
      const matchCount = this.matchData.filter((match) => this.isSameDate(match.date, date)).length;
      return matchCount;
    }

    if (this.activeTab === 'guest') {
      const guestCount = this.guestData.filter((guest) => this.isSameDate(guest.date, date)).length;
      return guestCount;
    }

    const matchCount = this.matchData.filter((match) => this.isSameDate(match.date, date)).length;
    const guestCount = this.guestData.filter((guest) => this.isSameDate(guest.date, date)).length;
    return matchCount + guestCount;
  }

  private goBack(): void {
    this.$router.go(-1);
  }
}
</script>

<style lang="scss" scoped>
.main {
  position: relative;
  min-height: 100vh;
  max-height: 100vh;
  max-width: 480px;
  margin: 0 auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
}

.background-wave {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 200%;
    height: 100%;
    background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 100" preserveAspectRatio="none"><defs><linearGradient id="waveGradient" x1="0%" y1="0%" x2="100%" y2="0%"><stop offset="0%" style="stop-color:rgba(0,10,80,0.6);stop-opacity:1" /><stop offset="50%" style="stop-color:rgba(10,30,120,0.8);stop-opacity:1" /><stop offset="100%" style="stop-color:rgba(0,10,80,0.6);stop-opacity:1" /></linearGradient></defs><g><path d="M 140,-50 L 120,-40 L 105,-25 L 90,-5 Q 70,20 50,35 T 20,60 Q 10,75 0,90 L -10,105 L -20,125 L -20,150 L 0,150 L 15,135 L 30,115 Q 45,95 60,80 T 85,55 Q 95,40 110,20 L 125,0 L 135,-20 L 140,-35 Z" fill="url(%23waveGradient)" opacity="0.75"/><path d="M 145,-60 L 125,-48 L 108,-30 L 92,-8 Q 70,22 48,40 T 15,68 Q 5,82 -5,98 L -15,115 L -25,135 L -25,160 L -5,160 L 10,143 L 25,122 Q 42,100 60,83 T 88,56 Q 100,38 118,15 L 132,-5 L 142,-25 L 145,-42 Z" fill="url(%23waveGradient)" opacity="0.7"/><path d="M 150,-70 L 128,-55 L 110,-35 L 92,-10 Q 68,25 43,45 T 8,78 Q -3,93 -12,108 L -22,128 L -30,148 L -30,170 L -8,170 L 8,150 L 25,128 Q 43,105 63,86 T 92,58 Q 105,38 125,10 L 140,-12 L 148,-32 L 150,-50 Z" fill="url(%23waveGradient)" opacity="0.65"/><path d="M 155,-80 L 130,-62 L 112,-40 L 92,-12 Q 65,28 38,50 T 0,88 Q -10,103 -20,120 L -30,142 L -35,160 L -35,180 L -12,180 L 5,158 L 23,133 Q 43,108 65,88 T 95,58 Q 110,35 130,5 L 145,-18 L 152,-40 L 155,-58 Z" fill="url(%23waveGradient)" opacity="0.6"/><path d="M 160,-90 L 132,-68 L 113,-43 L 90,-13 Q 62,32 32,56 T -8,98 Q -18,115 -28,133 L -38,155 L -40,175 L -40,190 L -15,190 L 2,165 L 20,138 Q 42,110 67,90 T 98,58 Q 115,32 135,0 L 150,-25 L 157,-48 L 160,-68 Z" fill="url(%23waveGradient)" opacity="0.55"/></g><g transform="translate(100, 0)"><path d="M 140,-50 L 120,-40 L 105,-25 L 90,-5 Q 70,20 50,35 T 20,60 Q 10,75 0,90 L -10,105 L -20,125 L -20,150 L 0,150 L 15,135 L 30,115 Q 45,95 60,80 T 85,55 Q 95,40 110,20 L 125,0 L 135,-20 L 140,-35 Z" fill="url(%23waveGradient)" opacity="0.75"/><path d="M 145,-60 L 125,-48 L 108,-30 L 92,-8 Q 70,22 48,40 T 15,68 Q 5,82 -5,98 L -15,115 L -25,135 L -25,160 L -5,160 L 10,143 L 25,122 Q 42,100 60,83 T 88,56 Q 100,38 118,15 L 132,-5 L 142,-25 L 145,-42 Z" fill="url(%23waveGradient)" opacity="0.7"/><path d="M 150,-70 L 128,-55 L 110,-35 L 92,-10 Q 68,25 43,45 T 8,78 Q -3,93 -12,108 L -22,128 L -30,148 L -30,170 L -8,170 L 8,150 L 25,128 Q 43,105 63,86 T 92,58 Q 105,38 125,10 L 140,-12 L 148,-32 L 150,-50 Z" fill="url(%23waveGradient)" opacity="0.65"/><path d="M 155,-80 L 130,-62 L 112,-40 L 92,-12 Q 65,28 38,50 T 0,88 Q -10,103 -20,120 L -30,142 L -35,160 L -35,180 L -12,180 L 5,158 L 23,133 Q 43,108 65,88 T 95,58 Q 110,35 130,5 L 145,-18 L 152,-40 L 155,-58 Z" fill="url(%23waveGradient)" opacity="0.6"/><path d="M 160,-90 L 132,-68 L 113,-43 L 90,-13 Q 62,32 32,56 T -8,98 Q -18,115 -28,133 L -38,155 L -40,175 L -40,190 L -15,190 L 2,165 L 20,138 Q 42,110 67,90 T 98,58 Q 115,32 135,0 L 150,-25 L 157,-48 L 160,-68 Z" fill="url(%23waveGradient)" opacity="0.55"/></g></svg>');
    background-size: 100% 100%;
    background-repeat: repeat-x;
  }
}

// Header
.header {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  max-width: 480px;
  width: 100%;
  z-index: 100;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  flex-shrink: 0;
  border-bottom: 1px solid #e0e0e0;

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    i {
      font-size: 28px;
      color: #19222b;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        opacity: 0.8;
        transform: scale(1.1);
      }
    }
  }

  .header-center {
    flex: 1;
    display: flex;
    justify-content: center;

    .header-title {
      font-size: 20px;
      font-weight: 600;
      color: #ffffff;
    }
  }

  .header-right {
    width: 28px;
  }
}

// Content
.content {
  position: relative;
  z-index: 2;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding-top: 60px;
  padding-bottom: 80px;
}

// Calendar Section
.calendar-section {
  background: #fff;
  border-radius: 0 0 35px 35px;
  padding: 16px;
  flex-shrink: 0;
}

.calendar-nav {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
  padding: 8px 0;
  margin-bottom: 12px;

  i {
    font-size: 20px;
    color: #061da1;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      color: #0a2bcc;
      transform: scale(1.2);
    }
  }

  .current-month {
    padding: 6px 16px;
    background: #061da1;
    color: #ffffff;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      background: #0a2bcc;
    }
  }
}

.calendar-grid {
  .calendar-weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 4px;
    margin-bottom: 6px;

    .weekday {
      text-align: center;
      font-size: 14px;
      font-weight: 600;
      color: #666;
      padding: 4px 0;

      &:first-child {
        color: #e74c3c;
      }

      &:last-child {
        color: #3498db;
      }
    }
  }

  .calendar-days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 4px;

    .calendar-day {
      aspect-ratio: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s ease;
      position: relative;

      .day-number {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }

      .today-label {
        font-size: 9px;
        font-weight: 700;
        color: #061da1;
        position: absolute;
        bottom: 2px;
      }

      .day-counter {
        font-size: 11px;
        font-weight: 600;
        color: #ff6b6b;
        margin-top: 1px;
      }

      &.other-month {
        .day-number {
          color: #ccc;
        }
      }

      &.today {
        background: #e3f2fd;

        .day-number {
          color: #061da1;
          font-weight: 700;
        }
      }

      &.selected {
        background: #061da1;

        .day-number {
          color: #fff;
          font-weight: 700;
        }

        .today-label {
          color: #fff;
        }
      }

      &:hover:not(.selected) {
        background: #f5f5f5;
      }
    }
  }
}

// Tabs Section
.tabs-section {
  flex: 1;
  overflow: hidden;
  padding: 0 20px;

  ::v-deep .el-tabs {
    height: 100%;
    display: flex;
    flex-direction: column;

    .el-tabs__header {
      margin: 0 0 16px 0;
    }

    .el-tabs__nav-wrap {
      &::after {
        background-color: #e4e7ed;
      }
    }

    .el-tabs__item {
      font-size: 20px;
      font-weight: 700;
      color: #666;
      text-align: center;

      &.is-active {
        color: #061da1;
      }
    }

    .el-tabs__active-bar {
      background-color: #f08717;
      height: 3px;
    }

    .el-tabs__content {
      flex: 1;
      overflow: hidden;
    }

    .el-tab-pane {
      height: 100%;
      overflow-y: auto;
    }
  }
}

.match-list,
.guest-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-bottom: 20px;
}

.team-card {
  background: #fff;
  display: flex;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 100px;
  background: #f7f7f7;
  border: 3px solid #c5cad5;
  border-radius: 20px;
  padding: 16px;
  position: relative;

  &.recruitment-closed {
    cursor: not-allowed;
    pointer-events: none;
  }

  .team-card-left {
    flex-shrink: 0;
    display: flex;
    align-items: center;

    .team-logo {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      object-fit: cover;
    }
  }

  .team-card-right {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
    justify-content: center;

    .team-tags {
      display: flex;
      flex-wrap: nowrap;
      gap: 6px;
      overflow-x: auto;

      &::-webkit-scrollbar {
        display: none;
      }

      .tag {
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 13px;
        font-weight: 600;
        white-space: nowrap;

        &:nth-child(1) {
          background: #e3f2fd;
          color: #1976d2;
        }

        &:nth-child(2) {
          background: #fff3e0;
          color: #f57c00;
        }

        &:nth-child(3) {
          background: #f3e5f5;
          color: #7b1fa2;
        }

        &:nth-child(4) {
          background: #e8f5e9;
          color: #388e3c;
        }
      }
    }

    .team-match-info {
      font-size: 18px;
      font-weight: 700;
      color: #333;
      text-align: left;
    }

    .team-location {
      font-size: 15px;
      color: #000;
      display: flex;
      align-items: center;
      gap: 4px;

      i {
        font-size: 12px;
      }
    }

    .guest-location-row {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .guest-location {
        font-size: 15px;
        color: #000;
        display: flex;
        align-items: center;
        gap: 6px;

        .team-icon {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          object-fit: cover;
        }

        i {
          font-size: 12px;
        }
      }

      .guest-members {
        display: flex;
        align-items: center;
        gap: 4px;
        padding: 4px 10px;
        background: #061da1;
        border-radius: 8px;
        font-size: 13px;
        font-weight: 600;
        color: #fff;

        i {
          font-size: 14px;
        }
      }
    }
  }

  .recruitment-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(6, 29, 161, 0.6);
    border-radius: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    z-index: 10;

    .recruitment-status {
      font-size: 18px;
      font-weight: 700;
      color: #fff;
      margin-bottom: 8px;
    }

    .small-team-logo {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      object-fit: cover;
      position: absolute;
      bottom: 8px;
      right: 8px;
    }
  }
}

// Year/Month Picker Dialog
::v-deep .year-month-dialog {
  border-radius: 20px;

  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    padding: 20px;
  }

  .el-dialog__footer {
    padding: 0 20px 20px;
  }
}

.year-month-picker {
  display: flex;
  gap: 20px;

  .picker-section {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #333;
      margin: 0 0 12px 0;
      text-align: center;
    }

    .year-scroll,
    .month-scroll {
      height: 300px;

      ::v-deep .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }

    .picker-item {
      padding: 12px 16px;
      text-align: center;
      font-size: 16px;
      color: #666;
      cursor: pointer;
      border-radius: 8px;
      margin-bottom: 8px;
      transition: all 0.3s ease;

      &:hover {
        background: #f5f5f5;
      }

      &.active {
        background: #061da1;
        color: #fff;
        font-weight: 600;
      }
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;

  ::v-deep .el-button {
    padding: 10px 24px;
    font-size: 16px;
    border-radius: 8px;

    &--primary {
      background: #061da1;
      border-color: #061da1;

      &:hover {
        background: #0a2bcc;
        border-color: #0a2bcc;
      }
    }
  }
}
</style>
