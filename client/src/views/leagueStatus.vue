<template>
  <div class="main league-schedule-page">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- League Status View -->
      <div class="league-status-section">
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
