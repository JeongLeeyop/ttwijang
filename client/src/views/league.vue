<template>
  <div class="main league-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">뛰장 리그를 소개합니다!</h2>
        <VueSlickCarousel v-bind="slickOptions" class="team-cards-container">
          <div
            v-for="(team, index) in teamCards"
            :key="index"
            class="team-card"
          >
            배너 영역
          </div>
        </VueSlickCarousel>
      </div>

      <!-- League Schedule Section -->
      <div class="league-section" :class="{ 'expanded': showLeagueStatus }">
        <div class="league-section-handle" @click="toggleLeagueSection">
          <div class="handle-bar"></div>
        </div>
        <div class="league-section-content">
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

        <!-- League Status Expanded View -->

        <div class="match-cards">
          <div class="league-action-buttons">
            <router-link to="/league-schedule" class="league-action-btn-link">
              <button class="league-action-btn">
                <i class="el-icon-date"></i>
                <span>리그 일정</span>
              </button>
            </router-link>
            <router-link to="/league-status" class="league-action-btn-link">
              <button class="league-action-btn">
                <i class="el-icon-s-data"></i>
                <span>리그 현황</span>
              </button>
            </router-link>
          </div>
        </div>
        </div>
      </div>
    </div>

    <!-- Footer -->
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import { getLeagueList, getLeagueTeams, LeagueTeamResponse } from '@/api/league';
import { getTeamList } from '@/api/team';

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
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private selectedLeague = 'a-league'

  private showLeagueStatus = false

  private currentYear = 2025

  private currentMonthIndex = 0

  private touchStartX = 0

  private touchEndX = 0

  private isLoading = false

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
      // 1. 리그 목록 조회 (지역 필터 적용)
      const leagueParams: any = {
        status: 'IN_PROGRESS',
        page: 0,
        size: 10,
      };
      if (this.selectedRegion) {
        leagueParams.regionSido = '경남';
        leagueParams.regionSigungu = this.selectedRegion;
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
        teamParams.regionSido = '경남';
        teamParams.regionSigungu = this.selectedRegion;
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
    console.log('Navigate to match:', match);
  }

  private navigateToTeam(teamUid: string): void {
    this.$router.push(`/team/${teamUid}`);
  }
}
</script>

<style scoped>
.league-page .league-section {
  padding-top: 30px !important;
  overflow: hidden !important;
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
</style>
