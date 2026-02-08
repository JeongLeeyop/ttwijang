<template>
  <div class="main league-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div v-show="currentView === 'main'" class="team-section">
        <h2 class="section-title">뛰장 리그를 소개합니다!</h2>
        <div v-if="isLoading && upcomingMatchCards.length === 0 && banners.length === 0" class="loading-container">
          <i class="el-icon-loading"></i> 로딩 중...
        </div>
        <!-- 배너 우선 표시 -->
        <VueSlickCarousel
          v-else-if="banners.length > 0"
          v-bind="slickOptions"
          class="team-cards-container banner-carousel"
        >
          <div
            v-for="(banner, index) in banners"
            :key="banner.uid"
            class="banner-card"
            @click="handleBannerClick(banner)"
          >
            <img :src="banner.imageUrl" :alt="banner.title" class="banner-image">
          </div>
        </VueSlickCarousel>
        <!-- 배너 없으면 매치 카드 표시 -->
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
                <span class="vs-badge">VS</span>
                <img :src="match.awayTeamLogo" :alt="match.awayTeamName" class="team-logo">
              </div>
            </div>
            <div class="team-card-right">
              <div class="team-tags">
                <span class="tag">{{ match.leagueName }}</span>
                <span v-if="match.round" class="tag">{{ match.round }}R</span>
              </div>
              <div class="match-teams-names">
                {{ match.homeTeamName }} vs {{ match.awayTeamName }}
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
          예정된 배너 및 리그 경기가 없습니다.
        </div>
      </div>

      <!-- League Schedule Section -->
      <div class="league-section" :class="{ 'expanded': currentView !== 'main' || showLeagueStatus }">
        <!-- Back Button (서브뷰일 때) -->
        <div v-if="currentView !== 'main'" class="league-back-header">
          <button class="league-back-btn" @click="goBack">
            <i class="el-icon-arrow-left"></i>
            <span>돌아가기</span>
          </button>
        </div>
        <div v-if="currentView === 'main'" class="league-section-handle" @click="toggleLeagueSection">
          <div class="handle-bar"></div>
        </div>
        <div class="league-section-content" :class="{ 'sub-view-content': currentView !== 'main' }">
          <!-- 메인 뷰 -->
          <template v-if="currentView === 'main'">
            <div class="league-join-team">
              <div class="join-team-header">
                <h3 class="join-team-title">리그 참가 팀</h3>
                <button class="add-team-button">
                  <i class="el-icon-circle-plus-outline"></i>
                </button>
              </div>
              <div v-if="isLoading" class="loading-container">
                <i class="el-icon-loading"></i> 로딩 중...
              </div>
              <VueSlickCarousel
                v-else-if="leagueParticipatingTeams.length > 0"
                v-bind="joinTeamSlickOptions"
                class="join-team-carousel"
              >
                <div
                  v-for="(team, index) in leagueParticipatingTeams"
                  :key="index"
                  class="join-team-card"
                  @click="navigateToTeam(team.teamUid)"
                >
                  <div class="team-badge-wrapper">
                    <span class="team-league-badge" :style="{ background: team.leagueColor }">{{ team.leagueName }}</span>
                    <div class="team-logo-container">
                      <img :src="team.logo" :alt="team.name" class="join-team-logo">
                    </div>
                  </div>
                  <div class="join-team-name">{{ team.name }}</div>
                </div>
              </VueSlickCarousel>
              <div v-else class="empty-message">
                참가 팀이 없습니다.
              </div>
            </div>
            <div class="league-join-team">
              <div class="join-team-header">
                <h3 class="join-team-title">팀 회원 모집</h3>
                <button class="add-team-button">
                  <i class="el-icon-circle-plus-outline"></i>
                </button>
              </div>
              <div v-if="isLoading" class="loading-container">
                <i class="el-icon-loading"></i> 로딩 중...
              </div>
              <VueSlickCarousel
                v-else-if="recruitingTeams.length > 0"
                v-bind="joinTeamSlickOptions"
                class="join-team-carousel"
              >
                <div
                  v-for="(team, index) in recruitingTeams"
                  :key="index"
                  class="join-team-card"
                  @click="navigateToTeam(team.teamUid)"
                >
                  <div class="team-badge-wrapper">
                    <span class="team-league-badge" :style="{ background: team.leagueColor }">{{ team.leagueName }}</span>
                    <div class="team-logo-container">
                      <img :src="team.logo" :alt="team.name" class="join-team-logo">
                    </div>
                  </div>
                  <div class="join-team-name">{{ team.name }}</div>
                </div>
              </VueSlickCarousel>
              <div v-else class="empty-message">
                모집 중인 팀이 없습니다.
              </div>
            </div>

            <div class="match-cards">
              <div class="league-action-buttons">
                <button class="league-action-btn" @click="showView('schedule')">
                  <i class="el-icon-date"></i>
                  <span>리그 일정</span>
                </button>
                <button class="league-action-btn" @click="showView('status')">
                  <i class="el-icon-s-data"></i>
                  <span>리그 현황</span>
                </button>
              </div>
            </div>
          </template>

          <!-- 리그 일정 뷰 -->
          <LeagueScheduleView
            v-if="currentView === 'schedule'"
            @navigate="showView"
          />

          <!-- 리그 현황 뷰 -->
          <LeagueStatusView
            v-if="currentView === 'status'"
          />
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
  getLeagueList, getLeagueTeams, LeagueTeamResponse, getUpcomingLeagueMatches,
} from '@/api/league';
import { getTeamList } from '@/api/team';
import { getActiveBanners } from '@/api/banner';
import LeagueScheduleView from '@/components/league/LeagueScheduleView.vue';
import LeagueStatusView from '@/components/league/LeagueStatusView.vue';

interface Banner {
  uid: string
  title: string
  imageUrl: string
  linkUrl?: string
  displayOrder: number
  targetPage: string
}

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
  leagueUid?: string
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

interface JoinTeam {
  teamUid: string
  name: string
  logo: string
  leagueName: string
  leagueColor: string
}

@Component({
  components: {
    VueSlickCarousel,
    LeagueScheduleView,
    LeagueStatusView,
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private selectedLeague = 'a-league'

  private currentView: 'main' | 'schedule' | 'status' = 'main'

  private showLeagueStatus = false

  private currentYear = 2025

  private currentMonthIndex = 0

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

  private banners: Banner[] = []

  private leagueParticipatingTeams: JoinTeam[] = []

  private recruitingTeams: JoinTeam[] = []

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  async created() {
    await this.loadData();
  }

  @Watch('selectedRegion')
  async onRegionChange() {
    await this.loadData();
  }

  private async loadData(): Promise<void> {
    this.isLoading = true;
    try {
      // 0. 배너 조회 (샘플 데이터 사용)
      this.banners = [
        {
          uid: 'banner-sample-1',
          title: '2026 봄 시즌 리그 참가 모집',
          imageUrl: 'https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
          linkUrl: '/league',
          displayOrder: 1,
          targetPage: 'LEAGUE',
        },
        {
          uid: 'banner-sample-2',
          title: '진주 풋살장 오픈 기념 이벤트',
          imageUrl: 'https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
          linkUrl: 'https://example.com/event',
          displayOrder: 2,
          targetPage: 'LEAGUE',
        },
        {
          uid: 'banner-sample-3',
          title: '뛰장 리그 우승팀 시상식',
          imageUrl: 'https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
          linkUrl: '/league-status',
          displayOrder: 3,
          targetPage: 'LEAGUE',
        },
        {
          uid: 'banner-sample-4',
          title: '신규 회원 가입 이벤트',
          imageUrl: 'https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
          linkUrl: '/join',
          displayOrder: 4,
          targetPage: 'LEAGUE',
        },
        {
          uid: 'banner-sample-5',
          title: '팀 매칭 서비스 오픈!',
          imageUrl: 'https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
          linkUrl: '/match',
          displayOrder: 5,
          targetPage: 'LEAGUE',
        },
      ];

      // 1. 다가오는 리그 경기 조회 (지역 필터 적용)
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

      // 2. 리그 목록 조회 (지역 필터 적용)
      const leagueParams: any = {
        status: 'IN_PROGRESS',
        page: 0,
        size: 10,
      };
      if (this.selectedRegion) {
        leagueParams.regionCode = this.selectedRegion;
      }

      const leagueResponse = await getLeagueList(leagueParams);
      const leagues = leagueResponse.data?.content || [];

      if (leagues.length > 0) {
        // 모든 조회된 리그의 참가 팀 합산 (Promise.all 사용)
        const teamPromises: Promise<JoinTeam[]>[] = leagues.map((league: any) => getLeagueTeams(league.uid)
          .then((leagueTeamsResponse): JoinTeam[] => {
            const leagueTeamsData: LeagueTeamResponse[] = leagueTeamsResponse.data || [];
            return leagueTeamsData.map((team: LeagueTeamResponse): JoinTeam => ({
              teamUid: team.teamUid,
              name: team.teamName,
              logo: team.teamLogoUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(team.teamName.substring(0, 2))}&background=random&color=fff&size=80`,
              leagueName: team.leagueName || 'B리그',
              leagueColor: '#ff8800',
            }));
          })
          .catch((err): JoinTeam[] => {
            console.warn(`Failed to load teams for league ${league.uid}:`, err);
            return [];
          }));

        const allTeamsArrays: JoinTeam[][] = await Promise.all(teamPromises);
        this.leagueParticipatingTeams = allTeamsArrays.reduce(
          (acc: JoinTeam[], teams: JoinTeam[]): JoinTeam[] => acc.concat(teams),
          [],
        );
      } else {
        this.leagueParticipatingTeams = [];
      }

      // 3. 회원 모집 중인 팀 목록 조회 (지역 필터 적용)
      const teamParams: any = {
        page: 0,
        size: 10,
      };
      if (this.selectedRegion) {
        teamParams.regionCode = this.selectedRegion;
      }
      const recruitingTeamsResponse = await getTeamList(teamParams);
      const teams = recruitingTeamsResponse.data?.content || [];

      this.recruitingTeams = teams
        .filter((team: any) => team.recruitingMembers === true)
        .slice(0, 10)
        .map((team: any) => ({
          teamUid: team.uid,
          name: team.name,
          logo: team.logoFileUid || `https://ui-avatars.com/api/?name=${encodeURIComponent(team.name.substring(0, 2))}&background=random&color=fff&size=80`,
          leagueName: 'B리그',
          leagueColor: '#ff8800',
        }));
    } catch (error) {
      console.error('Failed to load data:', error);
      this.$message.error('데이터를 불러오는데 실패했습니다.');
    } finally {
      this.isLoading = false;
    }
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

  private joinTeamSlickOptions = {
    dots: false,
    infinite: false,
    speed: 500,
    slidesToShow: 3.5,
    slidesToScroll: 1,
    arrows: false,
    swipeToSlide: true,
    touchThreshold: 5,
    variableWidth: false,
    responsive: [
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 3,
        },
      },
      {
        breakpoint: 380,
        settings: {
          slidesToShow: 2.5,
        },
      },
    ],
  }

  private joinTeams: JoinTeam[] = []

  private upcomingMatchCards: UpcomingMatchCard[] = []

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
      uid: 'recent-1',
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
      uid: 'recent-2',
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
      uid: 'upcoming-1',
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
      uid: 'upcoming-2',
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

  private formatMatchDate(dateStr: string): { date: string, day: string } {
    const d = new Date(dateStr);
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const dayOfWeek = days[d.getDay()];
    return {
      date: `${month}월 ${day}일`,
      day: dayOfWeek,
    };
  }

  private formatMatchTime(timeStr: string): string {
    if (!timeStr) return '';
    const parts = timeStr.split(':');
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

  private showView(view: 'schedule' | 'status'): void {
    this.currentView = view;
  }

  private goBack(): void {
    this.currentView = 'main';
  }

  private toggleLeagueStatus(): void {
    this.$router.push('/league-status');
  }

  private toggleLeagueSection(): void {
    this.showLeagueStatus = !this.showLeagueStatus;
  }

  private goToSchedule(): void {
    this.$router.push('/league-schedule');
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
      query: { type: 'league', leagueUid: match.leagueUid || '' },
    });
  }

  private navigateToTeam(teamUid: string): void {
    this.$router.push(`/team/${teamUid}`);
  }

  private async resolveRegionName(regionCode: string): Promise<string> {
    // 간단하게 localStorage나 API에서 가져온 지역명 사용
    // 실제로는 RegionCodeService처럼 코드 → 이름 변환 필요
    return '진주시'; // 임시
  }

  private handleBannerClick(banner: Banner): void {
    if (banner.linkUrl) {
      if (banner.linkUrl.startsWith('http')) {
        window.open(banner.linkUrl, '_blank');
      } else {
        this.$router.push(banner.linkUrl);
      }
    }
  }
}
</script>

<style scoped>
.league-page .league-section {
  padding-top: 30px !important;
  overflow: hidden !important;
  transition: top 0.4s ease-in-out;
}

.league-page .league-section.expanded {
  top: 70px !important;
  padding-top: 0 !important;
}

/* 뒤로가기 헤더 */
.league-back-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 16px 20px 8px;
}

.league-back-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 13px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
}

.league-back-btn:hover {
  background: #f5f5f5;
  border-color: #bbb;
}

.league-back-btn i {
  font-size: 14px;
  font-weight: bold;
}

/* 서브뷰 콘텐츠 스크롤 */
.sub-view-content {
  overflow-y: auto !important;
  -webkit-overflow-scrolling: touch;
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

.join-team-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.join-team-card:hover {
  transform: scale(1.05);
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

/* 배너 스타일 */
.banner-carousel {
  margin-bottom: 20px;
}

.banner-card {
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease;
}

.banner-card:hover {
  transform: scale(1.02);
}

.banner-image {
  width: 100%;
  height: auto;
  max-height: 120px;
  object-fit: cover;
  display: block;
}

.banner-title {
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
}
</style>
