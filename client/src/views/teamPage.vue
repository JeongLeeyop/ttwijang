<template>
  <div class="main league-page team-page-view">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Header (Centered) -->
      <div class="team-profile-section">
        <div class="team-profile-logo-wrap">
          <img
            :src="teamInfo.logoUrl || defaultLogo"
            :alt="teamInfo.name"
            class="team-profile-logo"
          >
        </div>
        <h2 class="team-profile-name">{{ teamInfo.name || '팀 이름' }}</h2>
        <p class="team-profile-owner">{{ teamInfo.ownerName || '운영자' }} (운영자)</p>
        <div class="team-profile-buttons">
          <button class="team-btn team-btn-outline" @click="goToDashboard">
            대시 보드
          </button>
          <button class="team-btn team-btn-primary" @click="goToTeamSettings">
            팀 설정
          </button>
        </div>
      </div>
    </div>

    <!-- Tab Section (흰색 영역 - 토글) -->
    <div class="league-section" :class="{ 'expanded': isExpanded }">
      <div class="league-section-handle" @click="toggleSection">
        <div class="handle-bar"></div>
      </div>
      <div class="league-section-content">
        <!-- 구단주 Info Bar -->
        <div class="team-owner-bar">
          <span class="owner-bar-text">
            구단주 : {{ ownerSponsorName || 'OOO' }} ({{ teamInfo.name || '팀명' }})
          </span>
          <span class="owner-bar-link" @click="showOwnerInfo">구단주 정보</span>
        </div>
        <!-- Tab Nav -->
        <div class="team-tab-nav">
          <div
            v-for="tab in tabs"
            :key="tab.key"
            class="team-tab-item"
            :class="{ 'active': activeTab === tab.key }"
            @click="activeTab = tab.key"
          >
            {{ tab.label }}
          </div>
        </div>

        <!-- 커뮤니티 Tab -->
        <div v-show="activeTab === 'community'" class="team-tab-content">
          <div v-if="communityPosts.length === 0" class="empty-state">
            <i class="el-icon-document"></i>
            <p>등록된 게시글이 없습니다.</p>
          </div>
          <div v-else class="community-list">
            <div
              v-for="post in communityPosts"
              :key="post.uid"
              class="community-item"
            >
              <div class="community-item-header">
                <span class="community-writer">{{ post.writer }}</span>
                <span class="community-date">{{ post.createDate }}</span>
              </div>
              <div class="community-item-title">{{ post.title }}</div>
              <div class="community-item-footer">
                <span><i class="el-icon-view"></i> {{ post.viewCount }}</span>
                <span><i class="el-icon-chat-dot-round"></i> {{ post.commentCount || 0 }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 매치경기 Tab -->
        <div v-show="activeTab === 'match'" class="team-tab-content">
          <div class="match-filter-row">
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'ALL' }"
              @click="matchFilter = 'ALL'"
            >전체</span>
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'FRIENDLY' }"
              @click="matchFilter = 'FRIENDLY'"
            >친선경기</span>
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'FREE' }"
              @click="matchFilter = 'FREE'"
            >자체경기</span>
          </div>
          <div v-if="filteredMatches.length === 0" class="empty-state">
            <i class="el-icon-football"></i>
            <p>매치 경기가 없습니다.</p>
          </div>
          <div v-else class="match-list">
            <div
              v-for="match in filteredMatches"
              :key="match.uid"
              class="match-item"
              :class="{ 'completed': match.status === 'COMPLETED' }"
            >
              <div class="match-item-date">
                {{ match.matchDate }} ({{ match.matchDay }}) {{ match.matchTime }}
              </div>
              <div class="match-item-teams">
                <div class="match-team home">
                  <img :src="match.homeLogo || defaultLogo" :alt="match.homeName" class="match-team-logo">
                  <span class="match-team-name">{{ match.homeName }}</span>
                </div>
                <div class="match-score" v-if="match.status === 'COMPLETED'">
                  <span>{{ match.homeScore }}</span>
                  <span class="score-divider">-</span>
                  <span>{{ match.awayScore }}</span>
                </div>
                <div class="match-vs" v-else>VS</div>
                <div class="match-team away">
                  <img :src="match.awayLogo || defaultLogo" :alt="match.awayName" class="match-team-logo">
                  <span class="match-team-name">{{ match.awayName }}</span>
                </div>
              </div>
              <div class="match-item-info">
                <span class="match-type-badge" :class="match.matchType === 'FRIENDLY' ? 'friendly' : 'free'">
                  {{ match.matchType === 'FRIENDLY' ? '친선' : '자체' }}
                </span>
                <span class="match-format">{{ match.matchFormat }}</span>
                <span class="match-stadium">{{ match.stadiumName }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 리그경기 Tab -->
        <div v-show="activeTab === 'league'" class="team-tab-content">
          <div v-if="teamLeagues.length === 0" class="empty-state">
            <i class="el-icon-trophy"></i>
            <p>참가 중인 리그가 없습니다.</p>
          </div>
          <div v-else>
            <!-- League Selector -->
            <div v-if="teamLeagues.length > 1" class="league-selector-row">
              <el-select
                v-model="selectedLeagueUid"
                placeholder="리그 선택"
                size="small"
                :popper-append-to-body="false"
              >
                <el-option
                  v-for="league in teamLeagues"
                  :key="league.uid"
                  :label="league.name"
                  :value="league.uid"
                ></el-option>
              </el-select>
            </div>
            <div v-else class="league-name-row">
              <span class="league-name-label">{{ teamLeagues[0].name }}</span>
            </div>

            <!-- Standings -->
            <div class="league-standings" v-if="leagueStandings.length > 0">
              <div class="standings-table">
                <table>
                  <thead>
                    <tr>
                      <th class="rank-th">순위</th>
                      <th class="team-th">팀</th>
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
                    <tr
                      v-for="(standing, idx) in leagueStandings"
                      :key="idx"
                      :class="{ 'my-team-row': standing.teamUid === teamUid }"
                    >
                      <td class="rank-cell">{{ standing.ranking }}</td>
                      <td class="team-name-cell">{{ standing.teamName }}</td>
                      <td>{{ standing.played }}</td>
                      <td class="points-cell">{{ standing.points }}</td>
                      <td>{{ standing.wins }}</td>
                      <td>{{ standing.draws }}</td>
                      <td>{{ standing.losses }}</td>
                      <td>{{ standing.goalsFor }}</td>
                      <td>{{ standing.goalsAgainst }}</td>
                      <td>{{ standing.goalDifference }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- Recent League Matches -->
            <div class="league-matches-section" v-if="leagueSchedule.length > 0">
              <h4 class="sub-section-title">경기 일정</h4>
              <div
                v-for="(lm, idx) in leagueSchedule"
                :key="idx"
                class="league-match-card"
              >
                <div class="league-match-date">
                  {{ formatDate(lm.matchDate) }} {{ lm.matchTime }}
                </div>
                <div class="league-match-teams">
                  <span class="lm-team-name">{{ lm.homeTeamName }}</span>
                  <span class="lm-score" v-if="lm.status === 'COMPLETED'">
                    {{ lm.homeScore }} - {{ lm.awayScore }}
                  </span>
                  <span class="lm-vs" v-else>VS</span>
                  <span class="lm-team-name">{{ lm.awayTeamName }}</span>
                </div>
                <div class="league-match-venue">{{ lm.stadiumName }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 회원정보 Tab -->
        <div v-show="activeTab === 'members'" class="team-tab-content">
          <div class="members-header">
            <span class="members-count">총 {{ members.length }}명</span>
          </div>
          <div v-if="members.length === 0" class="empty-state">
            <i class="el-icon-user"></i>
            <p>팀원이 없습니다.</p>
          </div>
          <div v-else class="members-list">
            <div
              v-for="member in members"
              :key="member.uid"
              class="member-item"
            >
              <img
                :src="member.profileImageUrl || defaultAvatar"
                :alt="member.userName"
                class="member-avatar"
              >
              <div class="member-info">
                <div class="member-name-row">
                  <span class="member-name">{{ member.userName }}</span>
                  <span class="member-role-badge" :class="getRoleBadgeClass(member.role)">
                    {{ getRoleLabel(member.role) }}
                  </span>
                </div>
                <div class="member-detail-row">
                  <span v-if="member.position" class="member-position">{{ member.position }}</span>
                  <span v-if="member.backNumber" class="member-number">#{{ member.backNumber }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 후원내역 Tab -->
        <div v-show="activeTab === 'sponsorship'" class="team-tab-content">
          <!-- Summary -->
          <div class="sponsorship-summary" v-if="sponsorshipSummary">
            <div class="summary-card">
              <div class="summary-amount">
                {{ formatAmount(sponsorshipSummary.totalSponsorshipAmount) }}
              </div>
              <div class="summary-label">총 후원금액</div>
            </div>
            <div class="summary-stats">
              <div class="stat-item">
                <span class="stat-value">{{ sponsorshipSummary.ownerCount }}</span>
                <span class="stat-label">구단주</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ sponsorshipSummary.regularSponsorCount }}</span>
                <span class="stat-label">정기후원</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ sponsorshipSummary.oneTimeSponsorCount }}</span>
                <span class="stat-label">1회후원</span>
              </div>
            </div>
          </div>

          <!-- Sponsorship List -->
          <div v-if="sponsorships.length === 0" class="empty-state">
            <i class="el-icon-present"></i>
            <p>후원 내역이 없습니다.</p>
          </div>
          <div v-else class="sponsorship-list">
            <div
              v-for="sp in sponsorships"
              :key="sp.uid"
              class="sponsorship-item"
            >
              <div class="sponsorship-item-left">
                <span class="sponsorship-type-badge" :class="getSponsorTypeBadge(sp.sponsorshipType)">
                  {{ getSponsorTypeLabel(sp.sponsorshipType) }}
                </span>
                <div class="sponsorship-info">
                  <span class="sponsorship-name">{{ sp.sponsorName || '익명' }}</span>
                  <span class="sponsorship-date">{{ formatDateTime(sp.createdDate) }}</span>
                </div>
              </div>
              <div class="sponsorship-item-right">
                <span class="sponsorship-amount">{{ formatAmount(sp.amount) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Watch,
  Prop,
} from 'vue-property-decorator';
import { getTeamDetail, getTeamMembers, getTeamByCode } from '@/api/team';
import { getMyTeamMatches } from '@/api/match';
import { getLeaguesByTeam, getLeagueStandings, getLeagueSchedule } from '@/api/league';
import {
  getTeamSponsorships,
  getTeamSponsorshipSummary,
} from '@/api/cash';

interface TabItem {
  key: string
  label: string
}

@Component
export default class TeamPage extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  private isExpanded = false

  private teamUid = ''

  private activeTab = 'community'

  private matchFilter = 'ALL'

  private selectedLeagueUid = ''

  private teamInfo: any = {}

  private communityPosts: any[] = []

  private matchData: any[] = []

  private teamLeagues: any[] = []

  private leagueStandings: any[] = []

  private leagueSchedule: any[] = []

  private members: any[] = []

  private sponsorships: any[] = []

  private sponsorshipSummary: any = null

  private ownerSponsorName = ''

  private isLoading = false

  private tabs: TabItem[] = [
    { key: 'community', label: '커뮤니티' },
    { key: 'match', label: '매치경기' },
    { key: 'league', label: '리그경기' },
    { key: 'members', label: '회원정보' },
    { key: 'sponsorship', label: '후원내역' },
  ]

  get defaultLogo(): string {
    return 'https://ui-avatars.com/api/?name=T&background=061da1&color=fff&size=60';
  }

  get defaultAvatar(): string {
    return 'https://ui-avatars.com/api/?name=U&background=ccc&color=fff&size=40';
  }

  get filteredMatches(): any[] {
    if (this.matchFilter === 'ALL') {
      return this.matchData;
    }
    return this.matchData.filter((m) => m.matchType === this.matchFilter);
  }

  async created() {
    const teamCode = this.$route.params.teamCode || this.$route.query.teamCode as string || '';
    if (teamCode) {
      // teamCode로 팀 조회하여 uid 획득
      try {
        const res = await getTeamByCode(teamCode);
        if (res.data && res.data.uid) {
          this.teamUid = res.data.uid;
        }
      } catch (e) {
        console.error('팀 코드로 조회 실패:', e);
        this.$message.error('존재하지 않는 팀입니다.');
        return;
      }
    } else {
      // 내 팀 조회 시도
      try {
        const { getMyTeams } = await import('@/api/team');
        const res = await getMyTeams();
        if (res.data && res.data.uid) {
          this.teamUid = res.data.uid;
        }
      } catch (e) {
        console.warn('내 팀 조회 실패:', e);
      }
    }
    if (this.teamUid) {
      await this.loadTeamInfo();
    }
  }

  @Watch('activeTab')
  async onTabChange() {
    await this.loadTabData();
  }

  @Watch('selectedLeagueUid')
  async onLeagueChange() {
    if (this.selectedLeagueUid) {
      await this.loadLeagueData();
    }
  }

  @Watch('matchFilter')
  onMatchFilterChange() {
    // filteredMatches computed updates automatically
  }

  private async loadTeamInfo(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getTeamDetail(this.teamUid);
      this.teamInfo = res.data || {};
      await this.loadOwnerSponsor();
      await this.loadTabData();
    } catch (error) {
      console.error('팀 정보 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadOwnerSponsor(): Promise<void> {
    try {
      const res = await getTeamSponsorshipSummary(this.teamUid);
      const summary = res.data;
      if (summary && summary.ownerName) {
        this.ownerSponsorName = summary.ownerName;
      }
    } catch (e) {
      // 구단주 정보 없을 수 있음
    }
  }

  private goToDashboard(): void {
    // 대시보드 페이지로 이동 (추후 구현)
    this.$message.info('대시보드 기능은 준비 중입니다.');
  }

  private goToTeamSettings(): void {
    // 팀 설정 페이지로 이동 (추후 구현)
    this.$message.info('팀 설정 기능은 준비 중입니다.');
  }

  private showOwnerInfo(): void {
    // 구단주 정보 표시 (추후 구현)
    this.$message.info('구단주 정보 기능은 준비 중입니다.');
  }

  private async loadTabData(): Promise<void> {
    switch (this.activeTab) {
      case 'community':
        await this.loadCommunityPosts();
        break;
      case 'match':
        await this.loadMatchData();
        break;
      case 'league':
        await this.loadLeagues();
        break;
      case 'members':
        await this.loadMembers();
        break;
      case 'sponsorship':
        await this.loadSponsorships();
        break;
      default:
        break;
    }
  }

  private async loadCommunityPosts(): Promise<void> {
    try {
      // 팀 커뮤니티 게시글은 기존 게시판 시스템 활용
      // 현재는 빈 목록 표시 (게시판이 팀별로 연결되면 boardUid로 필터)
      this.communityPosts = [];
    } catch (error) {
      console.warn('커뮤니티 로드 실패:', error);
    }
  }

  private async loadMatchData(): Promise<void> {
    try {
      const res = await getMyTeamMatches(this.teamUid, {});
      const matches = res.data?.content || res.data || [];
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      this.matchData = matches.map((m: any) => {
        const matchDate = new Date(m.matchDate);
        return {
          uid: m.uid,
          matchDate: this.formatDate(m.matchDate),
          matchDay: dayNames[matchDate.getDay()],
          matchTime: this.formatTime(m.matchTime),
          matchType: m.matchType,
          matchFormat: m.matchFormat || '',
          stadiumName: m.stadiumName || '',
          status: m.status,
          homeName: m.hostTeamName || this.teamInfo.name || '',
          homeLogo: m.hostTeamLogoUrl || this.teamInfo.logoUrl || this.defaultLogo,
          homeScore: m.homeScore,
          awayName: m.opponentTeamName || m.guestTeamName || '-',
          awayLogo: m.guestTeamLogoUrl || this.defaultLogo,
          awayScore: m.awayScore,
        };
      });
    } catch (error) {
      console.warn('매치 데이터 로드 실패:', error);
      this.matchData = [];
    }
  }

  private async loadLeagues(): Promise<void> {
    try {
      const res = await getLeaguesByTeam(this.teamUid);
      this.teamLeagues = res.data || [];
      if (this.teamLeagues.length > 0 && !this.selectedLeagueUid) {
        this.selectedLeagueUid = this.teamLeagues[0].uid;
        await this.loadLeagueData();
      }
    } catch (error) {
      console.warn('리그 정보 로드 실패:', error);
      this.teamLeagues = [];
    }
  }

  private async loadLeagueData(): Promise<void> {
    if (!this.selectedLeagueUid) return;
    try {
      const [standingsRes, scheduleRes] = await Promise.all([
        getLeagueStandings(this.selectedLeagueUid),
        getLeagueSchedule(this.selectedLeagueUid, {}),
      ]);
      this.leagueStandings = standingsRes.data || [];
      this.leagueSchedule = scheduleRes.data || [];
    } catch (error) {
      console.warn('리그 데이터 로드 실패:', error);
    }
  }

  private async loadMembers(): Promise<void> {
    try {
      const res = await getTeamMembers(this.teamUid);
      this.members = res.data || [];
    } catch (error) {
      console.warn('회원 정보 로드 실패:', error);
      this.members = [];
    }
  }

  private async loadSponsorships(): Promise<void> {
    try {
      const [spRes, summaryRes] = await Promise.all([
        getTeamSponsorships(this.teamUid),
        getTeamSponsorshipSummary(this.teamUid),
      ]);
      this.sponsorships = spRes.data || [];
      this.sponsorshipSummary = summaryRes.data || null;
    } catch (error) {
      console.warn('후원 내역 로드 실패:', error);
      this.sponsorships = [];
    }
  }

  private toggleSection(): void {
    this.isExpanded = !this.isExpanded;
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${month}월 ${day}일`;
  }

  private formatTime(time: string): string {
    if (!time) return '';
    const parts = time.split(':');
    if (parts.length < 2) return time;
    return `${parts[0]}:${parts[1]}`;
  }

  private formatDateTime(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}.${m}.${d}`;
  }

  private formatAmount(amount: number | null | undefined): string {
    if (!amount) return '0원';
    return `${amount.toLocaleString()}원`;
  }

  private getRoleLabel(role: string): string {
    const roleMap: Record<string, string> = {
      OWNER: '운영자',
      MANAGER: '매니저',
      MEMBER: '팀원',
    };
    return roleMap[role] || '팀원';
  }

  private getRoleBadgeClass(role: string): string {
    const classMap: Record<string, string> = {
      OWNER: 'role-owner',
      MANAGER: 'role-manager',
      MEMBER: 'role-member',
    };
    return classMap[role] || 'role-member';
  }

  private getSponsorTypeLabel(type: string): string {
    const typeMap: Record<string, string> = {
      OWNER: '구단주',
      REGULAR: '정기',
      ONE_TIME: '1회',
    };
    return typeMap[type] || '후원';
  }

  private getSponsorTypeBadge(type: string): string {
    const classMap: Record<string, string> = {
      OWNER: 'sponsor-owner',
      REGULAR: 'sponsor-regular',
      ONE_TIME: 'sponsor-onetime',
    };
    return classMap[type] || 'sponsor-onetime';
  }
}
</script>

<style scoped>
/* Team Profile Section - Centered Vertical Layout */
.team-profile-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 16px 0;
  text-align: center;
}

.team-profile-logo-wrap {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  border: 3px solid rgba(255, 255, 255, 0.4);
  overflow: hidden;
}

.team-profile-logo {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.team-profile-name {
  color: #fff;
  font-size: 20px;
  font-weight: 800;
  margin: 0 0 2px;
  letter-spacing: -0.3px;
}

.team-profile-owner {
  color: rgba(255, 255, 255, 0.85);
  font-size: 13px;
  margin: 0 0 14px;
  font-weight: 400;
}

.team-profile-buttons {
  display: flex;
  gap: 10px;
}

.team-btn {
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
  letter-spacing: -0.2px;
}

.team-btn-outline {
  background: #fff;
  color: #333;
  border: 1.5px solid #fff;
}

.team-btn-outline:active {
  background: #f0f0f0;
}

.team-btn-primary {
  background: #f7c600;
  color: #fff;
  border: 1.5px solid #f7c600;
}

.team-btn-primary:active {
  background: #e0b400;
}

/* 구단주 Info Bar (inside white section) */
.team-owner-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 16px;
  padding: 12px 14px;
  border-bottom: 1px solid #eee;
}

.owner-bar-text {
  color: #333;
  font-size: 13px;
  font-weight: 500;
}

.owner-bar-link {
  color: #f7c600;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
  padding: 4px 10px;
  background: #fff8e1;
  border-radius: 12px;
}

.owner-bar-link:active {
  opacity: 0.7;
}

/* Tab Nav */
.team-tab-nav {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 0 16px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
}

.team-tab-nav::-webkit-scrollbar {
  display: none;
}

.team-tab-item {
  flex-shrink: 0;
  padding: 12px 14px;
  font-size: 14px;
  color: #999;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.team-tab-item.active {
  color: #061da1;
  font-weight: 700;
  border-bottom-color: #061da1;
}

/* Tab Content */
.team-tab-content {
  padding: 16px;
  min-height: 300px;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #bbb;
}

.empty-state i {
  font-size: 40px;
  margin-bottom: 12px;
  display: block;
}

.empty-state p {
  font-size: 14px;
  margin: 0;
}

/* Community Tab */
.community-item {
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
}

.community-item:last-child {
  border-bottom: none;
}

.community-item-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.community-writer {
  font-size: 12px;
  color: #666;
  font-weight: 600;
}

.community-date {
  font-size: 11px;
  color: #bbb;
}

.community-item-title {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.community-item-footer {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #999;
}

.community-item-footer i {
  margin-right: 3px;
}

/* Match Tab */
.match-filter-row {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.match-filter-tag {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
  color: #666;
  background: #f5f5f5;
  cursor: pointer;
  transition: all 0.2s ease;
}

.match-filter-tag.active {
  background: #061da1;
  color: #fff;
}

.match-item {
  padding: 14px;
  border: 1px solid #eee;
  border-radius: 12px;
  margin-bottom: 10px;
}

.match-item.completed {
  opacity: 0.7;
}

.match-item-date {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.match-item-teams {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.match-team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  flex: 1;
}

.match-team-logo {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.match-team-name {
  font-size: 12px;
  color: #333;
  font-weight: 600;
  text-align: center;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.match-score {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 20px;
  font-weight: 700;
  color: #333;
}

.score-divider {
  color: #ccc;
}

.match-vs {
  font-size: 14px;
  font-weight: 700;
  color: #061da1;
}

.match-item-info {
  display: flex;
  gap: 8px;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.match-type-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
}

.match-type-badge.friendly {
  background: #e8f4fd;
  color: #1890ff;
}

.match-type-badge.free {
  background: #f0fce8;
  color: #52c41a;
}

/* League Tab */
.league-selector-row {
  margin-bottom: 16px;
}

.league-name-row {
  margin-bottom: 16px;
}

.league-name-label {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.standings-table {
  overflow-x: auto;
  margin-bottom: 20px;
}

.standings-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

.standings-table th {
  background: #f8f9fa;
  color: #666;
  font-weight: 600;
  padding: 8px 4px;
  text-align: center;
  white-space: nowrap;
}

.standings-table td {
  padding: 8px 4px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.standings-table .rank-th {
  width: 40px;
}

.standings-table .team-th {
  text-align: left;
  min-width: 60px;
}

.standings-table .rank-cell {
  font-weight: 700;
  color: #061da1;
}

.standings-table .team-name-cell {
  text-align: left;
  font-weight: 600;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.standings-table .points-cell {
  font-weight: 700;
  color: #f08717;
}

.my-team-row {
  background: #f0f4ff;
}

.my-team-row td {
  color: #061da1;
  font-weight: 600;
}

.sub-section-title {
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin: 0 0 12px;
}

.league-match-card {
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 10px;
  margin-bottom: 8px;
}

.league-match-date {
  font-size: 11px;
  color: #999;
  margin-bottom: 6px;
}

.league-match-teams {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.lm-vs {
  color: #061da1;
  font-weight: 700;
}

.lm-score {
  color: #f08717;
  font-weight: 700;
}

.league-match-venue {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
}

/* Members Tab */
.members-header {
  margin-bottom: 12px;
}

.members-count {
  font-size: 13px;
  color: #666;
  font-weight: 600;
}

.member-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.member-info {
  flex: 1;
}

.member-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 2px;
}

.member-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.member-role-badge {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 600;
}

.role-owner {
  background: #fff3e0;
  color: #f08717;
}

.role-manager {
  background: #e8f4fd;
  color: #1890ff;
}

.role-member {
  background: #f0f0f0;
  color: #999;
}

.member-detail-row {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #999;
}

/* Sponsorship Tab */
.sponsorship-summary {
  background: linear-gradient(135deg, #061da1, #1e3fcf);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  color: #fff;
}

.summary-card {
  text-align: center;
  margin-bottom: 16px;
}

.summary-amount {
  font-size: 24px;
  font-weight: 700;
}

.summary-label {
  font-size: 12px;
  opacity: 0.8;
  margin-top: 4px;
}

.summary-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 18px;
  font-weight: 700;
}

.stat-label {
  font-size: 11px;
  opacity: 0.8;
}

.sponsorship-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.sponsorship-item:last-child {
  border-bottom: none;
}

.sponsorship-item-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sponsorship-type-badge {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
}

.sponsor-owner {
  background: #fff3e0;
  color: #f08717;
}

.sponsor-regular {
  background: #e8f4fd;
  color: #1890ff;
}

.sponsor-onetime {
  background: #f0f0f0;
  color: #666;
}

.sponsorship-info {
  display: flex;
  flex-direction: column;
}

.sponsorship-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.sponsorship-date {
  font-size: 11px;
  color: #999;
}

.sponsorship-amount {
  font-size: 14px;
  font-weight: 700;
  color: #061da1;
}

/* Team Page View Specific */
.team-page-view .league-section {
  top: 280px;
}

.team-page-view .league-section.expanded {
  top: 70px;
}

.league-header {
  margin-bottom: 0;
}

.team-page-view .league-section {
  padding-top: 30px !important;
  overflow: hidden !important;
}
</style>
