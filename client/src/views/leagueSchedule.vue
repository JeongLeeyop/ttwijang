<template>
  <div class="main league-schedule-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- League Status View -->
      <div class="league-status-section">
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
            <router-link to="/league-status" class="league-action-btn-link">
              <button class="status-button">현황보기</button>
            </router-link>
          </div>
        </div>
        <div class="match-cards">
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
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

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

@Component({})
export default class extends Vue {
  private currentYear = 2025

  private currentMonthIndex = 0

  get currentMonth(): string {
    return `${this.currentYear}년 ${this.currentMonthIndex + 1}월`;
  }

  private selectedLeague = 'a-league'

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
}
</script>

<style scoped>
.league-schedule-page .main {
  background: #fff;
}

.league-schedule-page .content {
  margin-top: 73px;
  padding: 20px;
  background: #fff;
}

.league-status-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
