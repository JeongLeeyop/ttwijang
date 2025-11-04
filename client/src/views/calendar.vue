<template>
  <!-- Content -->
  <div class="content calendar-page">
    <div class="">
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
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

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
// Calendar page specific styles
.main {
  background: #fff !important;
}
</style>
