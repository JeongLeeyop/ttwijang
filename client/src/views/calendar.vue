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
              <span
                v-if="getCountForDate(day.date) > 0"
                class="day-counter"
              >{{ getCountForDate(day.date) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Tabs Section -->
      <div class="tabs-section">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="매치 구함" name="match">
            <div v-if="isLoading" class="loading-container">
              <i class="el-icon-loading"></i>
            </div>
            <div v-else-if="filteredMatches.length === 0" class="empty-state">
              <p>해당 날짜에 매치가 없습니다.</p>
            </div>
            <div v-else class="match-list">
              <div
                v-for="(match, index) in filteredMatches"
                :key="index"
                class="team-card"
                @click="goToMatchDetail(match)"
              >
                <div class="team-card-left">
                  <img :src="match.logo" :alt="match.name" class="team-logo">
                </div>
                <div class="team-card-right">
                  <div class="team-tags">
                    <span v-if="match.status" class="tag" :class="'tag-' + match.statusClass">{{ match.statusLabel }}</span>
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
            <div v-if="isLoading" class="loading-container">
              <i class="el-icon-loading"></i>
            </div>
            <div v-else-if="filteredGuests.length === 0" class="empty-state">
              <p>해당 날짜에 게스트 모집이 없습니다.</p>
            </div>
            <div v-else class="guest-list">
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
                    <div
                      v-if="guest.currentMembers !== undefined && guest.maxMembers !== undefined"
                      class="guest-members"
                    >
                      <i class="el-icon-user"></i>
                      <span>{{ guest.currentMembers }} / {{ guest.maxMembers }}</span>
                    </div>
                  </div>
                </div>
                <div v-if="guest.isRecruitmentClosed" class="recruitment-overlay">
                  <div class="recruitment-status">모집완료</div>
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
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getMyTeamMatches } from '@/api/match';
import { getGuestRecruitmentsByDateRange } from '@/api/guest';
import { getMyTeams } from '@/api/team';
import { getToken } from '@/utils/cookies';

interface CalendarDay {
  day: number
  date: string
  isCurrentMonth: boolean
  isSelected: boolean
  isToday: boolean
}

interface MatchItem {
  uid: string
  name: string
  logo: string
  matchType: string
  teamSize: string
  matchDate: string
  matchDay: string
  matchTime: string
  location: string
  dateKey: string
  status?: string
  statusLabel?: string
  statusClass?: string
  teamLogo?: string
  currentMembers?: number
  maxMembers?: number
  isRecruitmentClosed?: boolean
}

@Component
export default class CalendarPage extends Vue {
  private activeTab = 'match'

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private selectedDateStr = this.toDateKey(new Date())

  private showYearMonthPicker = false

  private selectedYear = new Date().getFullYear()

  private selectedMonth = new Date().getMonth() + 1

  private isLoading = false

  private matchData: MatchItem[] = []

  private guestData: MatchItem[] = []

  private myTeamUid = ''

  // 날짜별 카운트 캐시 (매번 filter 호출 방지)
  private matchCountMap: Record<string, number> = {}

  private guestCountMap: Record<string, number> = {}

  async created() {
    await this.initMyTeam();
    await this.loadCalendarData();
  }

  @Watch('currentMonthIndex')
  @Watch('currentYear')
  async onMonthChange() {
    await this.loadCalendarData();
  }

  private toDateKey(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  private async initMyTeam(): Promise<void> {
    const token = getToken();
    if (!token) return;
    try {
      const res = await getMyTeams();
      const data = res.data;
      let team = null;
      if (Array.isArray(data)) {
        team = data.length > 0 ? data[0] : null;
      } else {
        team = data;
      }
      if (team) {
        this.myTeamUid = team.uid || '';
      }
    } catch (e) {
      console.warn('팀 정보 로드 실패:', e);
    }
  }

  private async loadCalendarData(): Promise<void> {
    this.isLoading = true;
    try {
      const startDate = new Date(this.currentYear, this.currentMonthIndex, 1);
      const endDate = new Date(this.currentYear, this.currentMonthIndex + 1, 0);
      const startDateStr = this.toDateKey(startDate);
      const endDateStr = this.toDateKey(endDate);
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      // 매치 데이터 로드 (내 팀의 매치만)
      if (this.myTeamUid) {
        try {
          const matchRes = await getMyTeamMatches(this.myTeamUid, {
            startDate: startDateStr,
            endDate: endDateStr,
            size: 100,
          });
          const matches = matchRes.data?.content || matchRes.data || [];
          this.matchData = matches.map((m: any) => {
            const d = new Date(m.matchDate);
            return {
              uid: m.uid,
              name: m.hostTeamName || '',
              logo: m.hostTeamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent((m.hostTeamName || 'M').substring(0, 2))}&background=random&color=fff&size=60`,
              matchType: m.matchType === 'FRIENDLY' ? '친선 경기' : '자체 경기',
              teamSize: this.formatMatchFormat(m.matchFormat),
              matchDate: `${String(d.getMonth() + 1).padStart(2, '0')}월 ${String(d.getDate()).padStart(2, '0')}일`,
              matchDay: dayNames[d.getDay()],
              matchTime: m.matchTime,
              location: m.stadiumName || '',
              dateKey: this.toDateKey(d),
              status: m.status,
              statusLabel: this.getStatusLabel(m.status),
              statusClass: this.getStatusClass(m.status),
            };
          });
        } catch (e) {
          console.warn('매치 데이터 로드 실패:', e);
          this.matchData = [];
        }
      } else {
        this.matchData = [];
      }

      // 게스트 모집 데이터 로드 (모든 팀)
      try {
        const guestRes = await getGuestRecruitmentsByDateRange(startDateStr, endDateStr);
        const guests = guestRes.data?.content || guestRes.data || [];
        this.guestData = guests.map((g: any) => {
          const d = new Date(g.matchDate);
          const isFull = g.currentGuests >= g.maxGuests;
          return {
            uid: g.uid,
            name: g.teamName || '',
            logo: g.teamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent((g.teamName || 'G').substring(0, 2))}&background=random&color=fff&size=60`,
            matchType: g.matchType === 'FRIENDLY' ? '친선 경기' : '자체 경기',
            teamSize: this.formatMatchFormat(g.matchFormat),
            matchDate: `${String(d.getMonth() + 1).padStart(2, '0')}월 ${String(d.getDate()).padStart(2, '0')}일`,
            matchDay: dayNames[d.getDay()],
            matchTime: g.matchTime,
            location: g.stadiumName || '',
            dateKey: this.toDateKey(d),
            teamLogo: g.teamLogoUrl,
            currentMembers: g.currentGuests,
            maxMembers: g.maxGuests,
            isRecruitmentClosed: isFull,
          };
        });
      } catch (e) {
        console.warn('게스트 데이터 로드 실패:', e);
        this.guestData = [];
      }

      // 날짜별 카운트 맵 구축
      this.buildCountMaps();
    } finally {
      this.isLoading = false;
    }
  }

  private buildCountMaps(): void {
    const mc: Record<string, number> = {};
    this.matchData.forEach((m) => {
      mc[m.dateKey] = (mc[m.dateKey] || 0) + 1;
    });
    this.matchCountMap = mc;

    const gc: Record<string, number> = {};
    this.guestData.forEach((g) => {
      gc[g.dateKey] = (gc[g.dateKey] || 0) + 1;
    });
    this.guestCountMap = gc;
  }

  private getStatusLabel(status: string): string {
    const map: Record<string, string> = {
      RECRUITING: '모집중',
      MATCHED: '매칭완료',
      IN_PROGRESS: '진행중',
      COMPLETED: '종료',
      CANCELLED: '취소',
    };
    return map[status] || status || '';
  }

  private getStatusClass(status: string): string {
    const map: Record<string, string> = {
      RECRUITING: 'recruiting',
      MATCHED: 'matched',
      IN_PROGRESS: 'progress',
      COMPLETED: 'completed',
      CANCELLED: 'cancelled',
    };
    return map[status] || '';
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

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  get years(): number[] {
    const cy = new Date().getFullYear();
    const arr = [];
    for (let i = cy - 5; i <= cy + 5; i += 1) {
      arr.push(i);
    }
    return arr;
  }

  get calendarDays(): CalendarDay[] {
    const days: CalendarDay[] = [];
    const firstDay = new Date(this.currentYear, this.currentMonthIndex, 1);
    const lastDay = new Date(this.currentYear, this.currentMonthIndex + 1, 0);
    const prevLastDay = new Date(this.currentYear, this.currentMonthIndex, 0);

    const firstDayOfWeek = firstDay.getDay();
    const lastDateOfMonth = lastDay.getDate();
    const prevLastDate = prevLastDay.getDate();

    const todayKey = this.toDateKey(new Date());

    // Previous month days
    for (let i = firstDayOfWeek - 1; i >= 0; i -= 1) {
      const day = prevLastDate - i;
      const d = new Date(this.currentYear, this.currentMonthIndex - 1, day);
      const dk = this.toDateKey(d);
      days.push({
        day,
        date: dk,
        isCurrentMonth: false,
        isSelected: dk === this.selectedDateStr,
        isToday: dk === todayKey,
      });
    }

    // Current month days
    for (let i = 1; i <= lastDateOfMonth; i += 1) {
      const d = new Date(this.currentYear, this.currentMonthIndex, i);
      const dk = this.toDateKey(d);
      days.push({
        day: i,
        date: dk,
        isCurrentMonth: true,
        isSelected: dk === this.selectedDateStr,
        isToday: dk === todayKey,
      });
    }

    // Next month days
    const remaining = 42 - days.length;
    for (let i = 1; i <= remaining; i += 1) {
      const d = new Date(this.currentYear, this.currentMonthIndex + 1, i);
      const dk = this.toDateKey(d);
      days.push({
        day: i,
        date: dk,
        isCurrentMonth: false,
        isSelected: dk === this.selectedDateStr,
        isToday: dk === todayKey,
      });
    }

    return days;
  }

  get filteredMatches(): MatchItem[] {
    return this.matchData.filter((m) => m.dateKey === this.selectedDateStr);
  }

  get filteredGuests(): MatchItem[] {
    return this.guestData.filter((g) => g.dateKey === this.selectedDateStr);
  }

  private selectDate(day: CalendarDay): void {
    this.selectedDateStr = day.date;
    if (!day.isCurrentMonth) {
      // 이전/다음 달 날짜 클릭 시 해당 월로 이동
      const parts = day.date.split('-');
      const y = parseInt(parts[0], 10);
      const m = parseInt(parts[1], 10) - 1;
      if (y !== this.currentYear || m !== this.currentMonthIndex) {
        this.currentYear = y;
        this.currentMonthIndex = m;
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
    // 탭 전환 시 카운트가 자동으로 반영됨
  }

  private getCountForDate(dateKey: string): number {
    if (this.activeTab === 'match') {
      return this.matchCountMap[dateKey] || 0;
    }
    return this.guestCountMap[dateKey] || 0;
  }

  private goToMatchDetail(match: MatchItem): void {
    this.$router.push({
      path: `/match-detail/${match.uid}`,
      query: { type: 'match' },
    });
  }

  private goToGuestDetail(guest: MatchItem): void {
    if (guest.isRecruitmentClosed) return;
    this.$router.push({
      path: `/match-detail/${guest.uid}`,
      query: { type: 'guest' },
    });
  }
}
</script>

<style lang="scss" scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
  font-size: 24px;
  color: #061da1;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
  color: #999;
  font-size: 14px;
}

.tag-recruiting {
  background: #e8f5e9 !important;
  color: #2e7d32 !important;
}

.tag-matched {
  background: #e3f2fd !important;
  color: #1565c0 !important;
}

.tag-progress {
  background: #fff3e0 !important;
  color: #e65100 !important;
}

.tag-completed {
  background: #f5f5f5 !important;
  color: #757575 !important;
}

.tag-cancelled {
  background: #ffebee !important;
  color: #c62828 !important;
}
</style>
