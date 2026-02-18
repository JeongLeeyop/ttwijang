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
            v-for="(card, index) in teamCards"
            :key="index"
            class="team-card"
            @click="navigateToCardDetail(card)"
          >
            <div class="team-card-left">
              <div class="match-vs">
                <img :src="card.teamLogo" :alt="card.teamName" class="team-logo">
              </div>
            </div>
            <div class="team-card-right">
              <div class="team-tags">
                <span class="tag" :class="card.cardType === 'MATCH' ? 'tag-match' : 'tag-guest'">{{ card.cardType === 'MATCH' ? '매치' : '게스트' }}</span>
                <span v-if="card.matchFormat" class="tag">{{ card.matchFormat }}</span>
                <span v-if="card.positionType" class="tag">{{ card.positionLabel }}</span>
              </div>
              <!-- <div class="match-teams-names">
                {{ card.teamName }}
              </div> -->
              <div class="team-match-info">
                {{ card.matchDate }} ({{ card.matchDay }}) {{ card.matchTime }}
              </div>
              <div class="team-location">
                {{ card.stadiumName }} <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </VueSlickCarousel>
        <div v-else class="empty-message">
          모집 중인 매치가 없습니다.
        </div>
      </div>

      <!-- Recruiting Teams Section -->
      <div v-if="recruitingTeams.length > 0" class="recruit-section">
        <div class="recruit-section-header">
          <h2 class="section-title">팀 회원 모집</h2>
          <button class="see-all-btn" @click="$router.push('/team-recruit')">전체보기</button>
        </div>
        <div class="recruit-cards-scroll">
          <div
            v-for="rt in recruitingTeams"
            :key="rt.uid"
            class="recruit-mini-card"
            @click="$router.push(`/team-recruit-detail/${rt.uid}`)"
          >
            <div class="recruit-mini-logo">
              <img
                v-if="rt.logoUrl"
                :src="rt.logoUrl"
                alt="로고"
                class="mini-logo-img"
              >
              <i v-else class="el-icon-football mini-logo-icon"></i>
            </div>
            <span class="recruit-mini-name">{{ rt.name }}</span>
            <span class="recruit-mini-region">{{ rt.region || '' }}</span>
          </div>
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
            <div v-for="(match, index) in recentMatches" :key="index" class="past-match-card" @click="navigateToMatchDetail(match)">
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
            @click="navigateToMatchDetail(match)"
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
import { getGuestRecruitmentList } from '@/api/guest';
import { getRecruitingTeams } from '@/api/team';

interface HomeCard {
  uid: string
  cardType: 'MATCH' | 'GUEST'
  teamName: string
  teamLogo: string
  matchDate: string
  matchDay: string
  matchTime: string
  matchDateRaw: string
  stadiumName: string
  matchFormat?: string
  fee?: number
  positionType?: string
  positionLabel?: string
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

  private isExpanded = false

  private isDragging = false

  private dragStartY = 0

  private dragStartTop = 0

  private sectionTop: number | null = null

  private collapsedTop = 290

  private expandedTop = 70

  private currentYear = new Date().getFullYear()

  private currentMonthIndex = new Date().getMonth()

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  private teamCards: HomeCard[] = []

  private recruitingTeams: any[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  get slickOptions() {
    return {
      // dots: true,
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
      console.log('[HOME] 리그 목록 응답:', leaguesResponse.data);
      if (leaguesResponse.data && leaguesResponse.data.content) {
        this.availableLeagues = leaguesResponse.data.content.map((league: any) => ({
          uid: league.uid,
          name: league.name,
        }));
        console.log('[HOME] availableLeagues:', this.availableLeagues);
        if (this.availableLeagues.length > 0) {
          this.selectedLeague = this.availableLeagues[0].uid;
          console.log('[HOME] selectedLeague:', this.selectedLeague);
          // 리그 데이터 즉시 로드
          await this.loadLeagueData();
        } else {
          this.selectedLeague = '';
          this.leagueTable = [];
          this.upcomingMatches = [];
          this.recentMatches = [];
        }
      }

      // 팀 카드 로드 (일반 매치 + 게스트 모집)
      await this.loadTeamCards();
    } catch (error) {
      console.error('홈 데이터 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadTeamCards(): Promise<void> {
    try {
      const regionParams: any = {};
      if (this.selectedRegion) {
        regionParams.regionCode = this.selectedRegion;
      }

      // 일반 매치 + 게스트 모집 동시 호출
      const [matchRes, guestRes] = await Promise.all([
        getMatchList({ ...regionParams, status: 'RECRUITING', size: 10 }),
        getGuestRecruitmentList({ ...regionParams, status: 'RECRUITING', size: 10 }),
      ]);

      const matchCards: HomeCard[] = (matchRes.data?.content || []).map((m: any): HomeCard => {
        const dateInfo = this.formatMatchDate(m.matchDate);
        return {
          uid: m.uid,
          cardType: 'MATCH',
          teamName: m.hostTeamName || '',
          teamLogo: m.hostTeamLogoUrl || this.getTeamLogo(m.hostTeamName || ''),
          matchDate: dateInfo.date,
          matchDay: dateInfo.day,
          matchTime: this.formatMatchTime(m.matchTime),
          matchDateRaw: m.matchDate || '',
          stadiumName: m.stadiumName || '',
          matchFormat: m.matchFormat || '',
          fee: m.fee || 0,
        };
      });

      const guestCards: HomeCard[] = (guestRes.data?.content || []).map((g: any): HomeCard => {
        const dateInfo = this.formatMatchDate(g.matchDate);
        const posLabels: Record<string, string> = { FIELD: '필드', GK: '골키퍼', ANY: '포지션 무관' };
        return {
          uid: g.uid,
          cardType: 'GUEST',
          teamName: g.teamName || '',
          teamLogo: g.teamLogoUrl || this.getTeamLogo(g.teamName || ''),
          matchDate: dateInfo.date,
          matchDay: dateInfo.day,
          matchTime: this.formatMatchTime(g.matchTime),
          matchDateRaw: g.matchDate || '',
          stadiumName: g.stadiumName || '',
          fee: g.fee || 0,
          positionType: g.positionType || '',
          positionLabel: posLabels[g.positionType] || '',
        };
      });

      // 날짜순 정렬
      this.teamCards = [...matchCards, ...guestCards].sort((a, b) => a.matchDateRaw.localeCompare(b.matchDateRaw));
    } catch (error) {
      console.warn('팀 카드 로드 실패:', error);
      this.teamCards = [];
    }

    // 모집 중인 팀 로드
    try {
      const recruitParams: any = {
        size: 10,
      };
      if (this.selectedRegion) {
        recruitParams.regionCode = this.selectedRegion;
      }
      const recruitRes = await getRecruitingTeams(recruitParams);
      const data = recruitRes.data || recruitRes;
      this.recruitingTeams = data.content || [];
    } catch (e) {
      this.recruitingTeams = [];
    }
  }

  private async loadLeagueData(): Promise<void> {
    console.log('[HOME] loadLeagueData() 호출, selectedLeague:', this.selectedLeague);
    try {
      // 리그 순위표 로드
      const standingsResponse = await getLeagueStandings(this.selectedLeague);
      console.log('[HOME] 순위표 응답:', standingsResponse.data);
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
      const year = this.currentYear;
      const month = this.currentMonthIndex + 1;
      const lastDay = new Date(year, month, 0).getDate(); // 해당 월의 마지막 날
      const startDate = `${year}-${String(month).padStart(2, '0')}-01`;
      const endDate = `${year}-${String(month).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`;
      console.log('[HOME] 일정 조회 기간:', startDate, '~', endDate);
      const scheduleResponse = await getLeagueSchedule(this.selectedLeague, { startDate, endDate });
      console.log('[HOME] 일정 응답:', scheduleResponse.data);

      if (scheduleResponse.data && Array.isArray(scheduleResponse.data)) {
        const allMatches = scheduleResponse.data;
        console.log('[HOME] allMatches 개수:', allMatches.length);
        console.log('[HOME] allMatches 샘플:', allMatches[0]);

        // 최근 경기 (완료된 경기)
        const completedMatches = allMatches.filter((m: any) => m.status === 'COMPLETED');
        console.log('[HOME] COMPLETED 매치:', completedMatches.length, '개');
        this.recentMatches = completedMatches
          .slice(0, 5)
          .map((match: any) => this.transformMatch(match, true));

        // 예정 경기
        const scheduledMatches = allMatches.filter((m: any) => m.status !== 'COMPLETED');
        console.log('[HOME] SCHEDULED 매치:', scheduledMatches.length, '개');
        this.upcomingMatches = scheduledMatches
          .slice(0, 5)
          .map((match: any) => this.transformMatch(match, false));
        console.log('[HOME] upcomingMatches:', this.upcomingMatches.length, '개');
        console.log('[HOME] recentMatches:', this.recentMatches.length, '개');
      } else {
        console.warn('[HOME] scheduleResponse.data가 비어있거나 배열이 아님');
        this.upcomingMatches = [];
        this.recentMatches = [];
      }
    } catch (error) {
      console.warn('리그 데이터 로드 실패:', error);
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
    let displayHour = hour;
    if (hour > 12) {
      displayHour = hour - 12;
    } else if (hour === 0) {
      displayHour = 12;
    }
    return `${period} ${String(displayHour).padStart(2, '0')}:${minute}`;
  }

  private toggleLeagueStatus(): void {
    this.showLeagueStatus = !this.showLeagueStatus;
    if (this.showLeagueStatus) {
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

  private navigateToCardDetail(card: HomeCard): void {
    const type = card.cardType === 'GUEST' ? 'guest' : 'match';
    this.$router.push({
      path: `/match-detail/${card.uid}`,
      query: { type },
    });
  }
}
</script>

<style scoped>
/* Styles moved to style.css - Home Page Specific Styles section */

.team-card {
  min-height: 100px;
  max-height: 120px;
  padding: 12px 16px !important;
}

.team-card-left {
  flex-shrink: 0;
}

.team-card-right {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
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
  font-size: 10px;
  font-weight: 700;
  color: #ff6600;
  background: rgba(255, 102, 0, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  line-height: 1;
}

.team-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.team-tags .tag {
  font-size: 10px;
  padding: 2px 6px;
  line-height: 1.2;
}

.match-teams-names {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.3;
}

.team-match-info {
  font-size: 12px;
  color: #666;
  line-height: 1.3;
}

.team-location {
  font-size: 11px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
  line-height: 1.3;
}

.tag-match {
  background: rgba(6, 29, 161, 0.1) !important;
  color: #061da1 !important;
}

.tag-guest {
  background: rgba(0, 180, 100, 0.1) !important;
  color: #00b464 !important;
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

/* Recruiting Teams Section */
.recruit-section {
  padding: 16px 20px;
}

.recruit-section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.recruit-section-header .section-title {
  margin: 0;
}

.see-all-btn {
  background: none;
  border: none;
  color: #061da1;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  padding: 4px 0;
}

.recruit-cards-scroll {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  padding-bottom: 4px;
}

.recruit-cards-scroll::-webkit-scrollbar {
  display: none;
}

.recruit-mini-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 80px;
  max-width: 80px;
  cursor: pointer;
}

.recruit-mini-card:active {
  opacity: 0.7;
}

.recruit-mini-logo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  overflow: hidden;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 6px;
}

.mini-logo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.mini-logo-icon {
  font-size: 24px;
  color: #adb5bd;
}

.recruit-mini-name {
  font-size: 12px;
  font-weight: 600;
  color: #333;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.recruit-mini-region {
  font-size: 10px;
  color: #999;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
</style>
