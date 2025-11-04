<template>
  <div class="main">
    <div class="background-wave"></div>

    <!-- Header -->
    <div class="header">
      <div class="header-left">
        <i class="el-icon-s-fold"></i>
      </div>
      <div class="header-center">
        <el-select :popper-append-to-body="false" v-model="selectedRegion" placeholder="지역 선택" size="small">
          <el-option label="서울" value="seoul"></el-option>
          <el-option label="경기" value="gyeonggi"></el-option>
          <el-option label="인천" value="incheon"></el-option>
        </el-select>
      </div>
      <div class="header-right">
        <i class="el-icon-date" @click="goToCalendar"></i>
        <i class="el-icon-bell"></i>
      </div>
    </div>

    <!-- Content -->
    <div class="content">
      <!-- Team Cards Section -->
      <div class="team-section">
        <h2 class="section-title">이 팀이랑 경기 어떠세요?</h2>
        <VueSlickCarousel v-bind="slickOptions" class="team-cards-container">
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
                <span class="tag">{{ team.league }}</span>
                <span class="tag">매너 {{ team.manner }}점</span>
                <span class="tag">{{ team.matchType }}</span>
                <span class="tag">{{ team.teamSize }}</span>
              </div>
              <div class="team-match-info">
                {{ team.matchDate }} ({{ team.matchDay }}) {{ team.matchTime }}
              </div>
              <div class="team-location">
                {{ team.location }} <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </VueSlickCarousel>
      </div>

      <!-- League Schedule Section -->
      <div class="league-section" :class="{ 'expanded': showLeagueStatus }">
        <div class="league-header">
          <div class="league-title-row">
            <el-select v-model="selectedLeague" :popper-append-to-body="false" placeholder="리그 선택" size="small" class="league-select">
              <el-option label="A리그" value="a-league"></el-option>
              <el-option label="B리그" value="b-league"></el-option>
              <el-option label="C리그" value="c-league"></el-option>
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
            <div v-for="(match, index) in recentMatches" :key="index" class="past-match-card">
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

    <!-- Footer -->
    <div class="footer">
      <div class="footer-item">
        <svg class="footer-icon" viewBox="0 0 24 24" fill="currentColor">
          <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="1.5"/>
          <path d="M12 2 L12 22 M2 12 L22 12" stroke="currentColor" stroke-width="1"/>
          <path d="M7 5 Q12 8 17 5 M7 19 Q12 16 17 19" fill="none" stroke="currentColor" stroke-width="1"/>
          <path d="M5 7 Q8 12 5 17 M19 7 Q16 12 19 17" fill="none" stroke="currentColor" stroke-width="1"/>
        </svg>
        <span>리그</span>
      </div>
      <div class="footer-item">
        <svg class="footer-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="5" width="18" height="14" rx="2"/>
          <path d="M3 10 L21 10"/>
          <circle cx="12" cy="14" r="2" fill="currentColor"/>
        </svg>
        <span>매치</span>
      </div>
      <div class="footer-item footer-home">
        <i class="el-icon-s-home"></i>
      </div>
      <div class="footer-item">
        <svg class="footer-icon" viewBox="0 0 24 24" fill="currentColor">
          <circle cx="12" cy="7" r="3"/>
          <path d="M12 12 C8 12 5 14 5 17 L19 17 C19 14 16 12 12 12 Z"/>
          <circle cx="6" cy="9" r="2" opacity="0.7"/>
          <path d="M6 12 C3.5 12 2 13.5 2 15.5 L7 15.5" opacity="0.7"/>
          <circle cx="18" cy="9" r="2" opacity="0.7"/>
          <path d="M18 12 C20.5 12 22 13.5 22 15.5 L17 15.5" opacity="0.7"/>
        </svg>
        <span>게스트</span>
      </div>
      <div class="footer-item">
        <i class="el-icon-user-solid"></i>
        <span>MY</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';

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

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  private selectedRegion = 'seoul'

  private selectedLeague = 'a-league'

  private showLeagueStatus = false

  private currentYear = 2024

  private currentMonthIndex = 0

  private touchStartX = 0

  private touchEndX = 0

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  private slickOptions = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1.2,
    slidesToScroll: 1,
    arrows: false,
    centerMode: false,
    centerPadding: '40px',
    variableWidth: false,
    swipeToSlide: true,
    touchThreshold: 10,
    initialSlide: 0.75,
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
    // Navigate to match detail page
    console.log('Navigate to match:', match);
  }

  private goToCalendar(): void {
    this.$router.push('/calendar');
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
  background: #061da1;
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
    // animation: wave-slide 20s linear infinite;
  }
}

@keyframes wave-slide {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
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
  // background: rgba(255, 255, 255, 0.1);
  // backdrop-filter: blur(10px);
  flex-shrink: 0;

  .header-left, .header-right {
    display: flex;
    align-items: center;
    gap: 16px;

    i {
      font-size: 28px;
      color: #ffffff;
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
    justify-content: flex-start;
    margin-left: 16px;

    ::v-deep .el-select {
      width: 140px;

      .el-input__inner {
        background: rgba(255, 255, 255, 0.95);
        border: 1px solid rgba(255, 255, 255, 0.3);
        color: #061da1;
        font-weight: 600;
        font-size: 16px;
        padding: 10px 14px;

        &::placeholder {
          color: rgba(6, 29, 161, 0.7);
        }
      }

      .el-input__icon {
        color: #061da1;
        font-size: 16px;
      }
    }
  }
}

// Content
.content {
  position: relative;
  z-index: 2;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: visible;
  padding-top: 80px;
  padding-bottom: 0;
}

.section-title {
  font-size: 30px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 25px 0;
}

// Team Cards Section
.team-section {
  position: relative;
  z-index: 1;
  flex-shrink: 0;
  margin-bottom: 54px;
  padding: 0 20px;

  ::v-deep .slick-slider {
    .slick-list {
      overflow: visible;
      padding-bottom: 10px;
    }

    .slick-track {
      display: flex !important;
      gap: 12px;
    }

    .slick-slide {
      float: none !important;

      > div {
        padding: 0 6px;
        height: 100%;
        display: flex !important;
      }
    }
  }
}

.team-cards-container {
  cursor: grab;
  user-select: none;

  &:active {
    cursor: grabbing;
  }
}

.team-card {
  width: 100% !important;
  height: 100px !important;
  background: #fff;
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex !important;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
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
  }
}

// League Section
.league-section {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  max-width: 480px;
  background: #fff;
  border-radius: 35px 35px 0 0;
  padding: 20px 0 80px 0;
  margin-bottom: 0;
  width: 100%;
  height: calc(100vh - 280px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  z-index: 50;
  transition: height 0.4s ease-in-out, padding-top 0.4s ease-in-out;

  &.expanded {
    height: calc(100vh - 80px);
    padding-top: 20px;
  }
}

.league-header {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
  padding: 0 20px;
  flex-wrap: wrap;
  border-bottom: 3px solid #c5cad5;

  .league-title-row {
    display: flex;
    align-items: center;
    gap: 12px;

    .league-select {
      width: 140px;
      ::v-deep .el-input__inner {
        background: #061da1;
        color: #ffffff;
        border: none;
        font-weight: 600;
        padding: 20px 12px;
        font-size:20px;
      }

      ::v-deep .el-input__icon {
        color: #ffffff;
      }
    }

    .league-title-text {
      font-size: 26px;
      font-weight: 600;
      color: #061da1;
    }
  }
  .league-button-row {
    flex:0 1 100%;
    text-align: right;
    .status-button {
      padding: 8px 16px;
      background: #fff;
      border: none;
      font-size: 15px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }
  }
}

.league-status-expanded {
  flex: 1;
  overflow-y: auto;
  padding: 16px 20px;
  // background: #f9f9f9;
  border-radius: 0;

  .status-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0 0 16px 0;
    text-align: center;
  }

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(0, 0, 0, 0.05);
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(6, 29, 161, 0.3);
    border-radius: 4px;
  }
}

.calendar-nav {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
  padding: 12px 0;
  margin-bottom: 16px;

  i {
    font-size: 24px;
    color: #061da1;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      color: #0a2bcc;
      transform: scale(1.2);
    }
  }

  .current-month {
    padding: 8px 20px;
    background: #061da1;
    color: #ffffff;
    border-radius: 6px;
    font-size: 18px;
    font-weight: 600;
  }
}

.league-table {
  overflow-x: auto;
  margin-bottom: 20px;

  table {
    width: 100%;
    border-collapse: collapse;
    background: #ffffff;
    border-radius: 8px;
    overflow: hidden;

    thead {
      background: #f5f5f5;

      th {
        padding: 12px 8px;
        text-align: center;
        font-size: 15px;
        font-weight: 600;
        color: #666;
      }
    }

    tbody {
      tr {
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        td {
          padding: 12px 8px;
          text-align: center;
          font-size: 16px;
          color: #333;

          &.rank-cell {
            display: flex;
            align-items: center;
            gap: 8px;
            justify-content: center;

            .rank-number {
              font-weight: 600;
              color: #666;
              min-width: 20px;
              font-size: 16px;
            }

            .team-mini-logo {
              width: 28px;
              height: 28px;
              border-radius: 50%;
            }
          }

          &.points {
            font-weight: 700;
            color: #061da1;
          }
        }
      }
    }
  }
}

.past-matches {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .past-match-card {
    background: #ffffff;
    padding: 12px;
    background: #f7f7f7;
    border: 3px solid #c5cad5;
    border-radius: 20px;

    .past-match-date {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      margin-bottom: 8px;
    }

    .past-match-teams {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .past-team {
        display: flex;
        align-items: center;
        gap: 8px;
        flex: 1;

        .past-team-name {
          font-size: 17px;
          font-weight: 600;
          color: #333;
        }

        .past-team-logo {
          width: 36px;
          height: 36px;
          border-radius: 50%;
        }

        &:first-child {
          justify-content: end;
        }

        &:last-child {
          justify-content: start;
        }
      }

      .past-match-score {
        display: flex;
        align-items: center;
        gap:6px;
        padding: 0 10px;
        margin: 0 15px;
        background: #e9e8f8;
        border-radius: 6px;

        .score-number {
          font-size: 18px;
          font-weight: 700;
          color: #061da1;
        }

        .score-divider {
          font-size: 18px;
          color: #999;
        }
      }
    }
  }
}

// Match Cards
.match-cards {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 20px 20px 20px;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(0, 0, 0, 0.05);
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(6, 29, 161, 0.3);
    border-radius: 4px;
  }
}

.match-card {
  position: relative;
  background: #f7f7f7;
  border: 3px solid #c5cad5;
  border-radius: 20px;
  padding: 16px;
  margin: 0;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background: #f9f9f9;
    transform: translateX(2px);
  }

  .match-arrow {
    position: absolute;
    top: 16px;
    right: 16px;
    font-size: 18px;
    color: #999;
  }

  .match-date-time {
    font-size: 18px;
    font-weight: 600;
    color: #333;
    margin-bottom: 4px;
  }

  .match-location {
    font-size: 15px;
    color: #666;
    margin-bottom: 6px;
  }

  .match-versus {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-right: 32px;

    .match-team {
      display: flex;
      align-items: center;
      gap: 8px;
      flex: 1;

      .match-team-name {
        font-size: 20px;
        font-weight: 600;
        color: #333;
      }

      .match-team-logo {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        object-fit: cover;
      }

      &:first-child {
        justify-content: end;
      }

      &:last-child {
        justify-content: start;
      }
    }

    .vs-text {
      font-size: 28px;
      font-weight: 700;
      color: #061da1;
      padding: 0 12px;
    }
  }
}

// Footer
.footer {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  max-width: 480px;
  width: 100%;
  z-index: 100;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.95);
  border-top: solid 3px #c5cad5;
  // backdrop-filter: blur(10px);
  // box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;

  .footer-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    transition: all 0.3s ease;

    i {
      font-size: 30px;
      color: #666;
    }

    .footer-icon {
      width: 30px;
      height: 30px;
      color: #666;
    }

    span {
      font-size: 13px;
      color: #666;
    }

    &:hover {
      i, span, .footer-icon {
        color: #061da1;
      }
    }

    &.footer-home {
      position: relative;
      top: -40px;

      i {
        font-size: 48px;
        color: #ffffff;
        background: #061da1;
        border-radius: 50%;
        padding: 8px;
        box-shadow: 0 4px 12px rgba(6, 29, 161, 0.3);
      }

      &:hover i {
        background: #0a2bcc;
      }
    }
  }
}

@media (max-width: 768px) {
  .section-title {
    font-size: 18px;
  }

  .team-card {
    min-width: 240px;
    padding: 16px;

    .team-name {
      font-size: 18px;
    }
  }

  .header {
    padding: 12px 16px;

    .header-left i, .header-right i {
      font-size: 24px;
    }
  }

  .team-section {
    padding: 0 16px;
  }

  .league-header {
    padding: 0 16px;
  }

  .league-status-expanded {
    padding: 16px;
  }

  .match-cards {
    padding: 0 16px 16px 16px;
  }
}
</style>
