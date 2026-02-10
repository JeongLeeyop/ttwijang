<template>
  <div class="main mypage-page">
    <div class="mypage-wrapper content">
      <section class="profile-section">
        <div class="profile-header">
          <div class="profile-info">
            <h2 class="profile-name">{{ userProfile.name }}<span class="profile-name-suffix">님</span></h2>
          </div>
          <button class="profile-edit-btn" @click="editProfile">
            프로필 수정
          </button>
        </div>

        <!-- Stats Row Section -->
        <section class="stats-row-section">
          <div class="stat-button">
            <div class="stat-label">매너점수</div>
            <div class="stat-value">
              <i v-if="isLoadingStats" class="el-icon-loading"></i>
              <span v-else>{{ userStats.mannerscore }}</span>
            </div>
          </div>
          <div class="stat-button" :class="{ 'clickable': myTeamCode }" @click="goToMyTeam">
            <div class="stat-label">
              <i v-if="isLoadingTeam" class="el-icon-loading"></i>
              <span v-else>{{ userStats.teamLabel }}</span>
            </div>
            <div class="stat-value">
              <i v-if="isLoadingTeam" class="el-icon-loading"></i>
              <span v-else>{{ userStats.team }}</span>
            </div>
          </div>
          <div class="stat-button">
            <div class="stat-label">참여 경기수</div>
            <div class="stat-value">
              <i v-if="isLoadingMatches" class="el-icon-loading"></i>
              <span v-else>{{ userStats.matches }}</span>
            </div>
          </div>
        </section>

        <!-- Cash/Wallet Section -->
        <section class="wallet-section">
        <div class="wallet-card">
          <div class="wallet-header">
            <span class="wallet-label">나의 캐쉬</span>
          </div>
          <div class="wallet-balance">
            <div class="balance-wrapper">
              <span class="balance-amount">{{ formatCurrency(userWallet.points) }}</span>
              <span class="balance-unit">원</span>
            </div>
            <button class="wallet-btn charge-btn" @click="chargePoints">
              캐쉬 충전
            </button>
          </div>
        </div>
        </section>

        <!-- Guide Section -->
        <section class="guide-section">
          <div class="guide-card">
            <div class="guide-header">
              <span class="guide-label">매너점수 시스템을 소개합니다.</span>
              <button class="guide-btn" @click="openGuideDetail">
                <i class="el-icon-s-unfold"></i>
                뛰장 가이드
              </button>
            </div>
          </div>
        </section>
      </section>

      <!-- Menu List Section -->
      <section class="menu-section">
        <div class="menu-list">
          <div class="menu-item" @click="navigateTo('my-teams')">
            <div class="menu-icon">
              <i class="el-icon-info"></i>
            </div>
            <span class="menu-text">뛰장 소개</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-matches')">
            <div class="menu-icon">
              <i class="el-icon-document-copy"></i>
            </div>
            <span class="menu-text">신청 내역</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-leagues')">
            <div class="menu-icon">
              <i class="el-icon-user-solid"></i>
            </div>
            <span class="menu-text">팀 내역</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateToCashHistory">
            <div class="menu-icon">
              <i class="el-icon-s-management"></i>
            </div>
            <span class="menu-text">캐쉬 사용내역</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-coupons')">
            <div class="menu-icon">
              <i class="el-icon-location"></i>
            </div>
            <span class="menu-text">구장 제휴</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-coupons')">
            <div class="menu-icon">
              <i class="el-icon-support"></i>
            </div>
            <span class="menu-text">구단 후원 신청</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-coupons')">
            <div class="menu-icon">
              <i class="el-icon-question"></i>
            </div>
            <span class="menu-text">자주 묻는 질문</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('my-coupons')">
            <div class="menu-icon">
              <i class="el-icon-bell"></i>
            </div>
            <span class="menu-text">공지사항</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <div class="menu-item" @click="navigateTo('settings')">
            <div class="menu-icon">
              <i class="el-icon-setting"></i>
            </div>
            <span class="menu-text">설정</span>
            <i class="el-icon-arrow-right"></i>
          </div>
          <!-- <div class="menu-item" @click="navigateTo('help')">
            <div class="menu-icon">
              <i class="el-icon-question"></i>
            </div>
            <span class="menu-text">도움말</span>
            <i class="el-icon-arrow-right"></i>
          </div> -->
          <div class="menu-item logout-item" @click="logout">
            <div class="menu-icon">
              <i class="el-icon-switch-button"></i>
            </div>
            <span class="menu-text">로그아웃</span>
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </section>

      <!-- Footer Spacing -->
      <div class="footer-spacing"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { UserModule } from '@/store/modules/user';
import { getWallet } from '@/api/cash';
import { getMyTeams, checkMembershipStatus } from '@/api/team';
import { getUserMannerScore } from '@/api/mannerRating';
import { getUserInfo } from '@/api/user';
import { getMatchList } from '@/api/match';
import { getToken, getTokenInfo } from '@/utils/cookies';

@Component({
  name: 'MyPage',
})
export default class MyPage extends Vue {
  private userModule = UserModule;

  private userProfile = {
    name: '',
    email: '',
    avatar: 'https://ui-avatars.com/api/?name=?&background=061da1&color=fff&size=80&bold=true',
  };

  private userStats = {
    matches: 0,
    wins: 0,
    losses: 0,
    totalMatches: 0,
    avgGoals: 0,
    winRate: 0,
    ranking: 0,
    mannerscore: 0,
    team: '-',
    teamLabel: '소속 팀',
  };

  private userWallet = {
    points: 0,
    coupons: 0,
  };

  private isLoading = false;

  private isLoadingStats = true;

  private isLoadingTeam = true;

  private isLoadingMatches = true;

  private myTeamCode = '';

  async mounted() {
    // 마이페이지 헤더 배경색 설정을 위한 클래스 추가
    const mainLayout = document.querySelector('.main-layout');
    if (mainLayout) {
      mainLayout.classList.add('mypage-active');
    }

    // 토큰이 있으면 사용자 정보가 로드될 때까지 대기
    const token = getToken();
    if (token && !this.userModule.isLogin) {
      // permission.ts에서 GetUserInfo()를 호출했으므로 잠시 대기
      await new Promise((resolve) => setTimeout(resolve, 100));
    }

    // 로그인 상태 체크 (토큰도 없고 로그인도 안 되어 있으면)
    if (!token && !this.userModule.isLogin) {
      this.$message.warning('로그인이 필요한 페이지입니다.');
      this.$router.replace({ name: 'Login' });
      return;
    }

    // Store에 저장된 기본 정보 먼저 반영
    if (this.userModule.infoName) {
      this.userProfile.name = this.userModule.infoName;
    }
    if (this.userModule.infoEmail) {
      this.userProfile.email = this.userModule.infoEmail;
    }

    await this.loadUserData();
  }

  beforeDestroy(): void {
    // 페이지 벗어날 때 클래스 제거
    const mainLayout = document.querySelector('.main-layout');
    if (mainLayout) {
      mainLayout.classList.remove('mypage-active');
    }
  }

  private async loadUserData(): Promise<void> {
    this.isLoading = true;
    try {
      // 사용자 프로필 정보 로드 (API → Store → JWT 순으로 fallback)
      let userUid = '';
      try {
        const userInfoResponse = await getUserInfo();
        if (userInfoResponse.data) {
          this.userProfile.name = userInfoResponse.data.actualName || userInfoResponse.data.name || '';
          this.userProfile.email = userInfoResponse.data.email || '';
          userUid = userInfoResponse.data.uid || userInfoResponse.data.userId || '';
        }
      } catch (infoError) {
        console.warn('사용자 정보 API 호출 실패, Store 데이터 사용:', infoError);
      }

      // API로 이름을 못 가져온 경우 Store fallback
      if (!this.userProfile.name && this.userModule.infoName) {
        this.userProfile.name = this.userModule.infoName;
      }
      if (!this.userProfile.email && this.userModule.infoEmail) {
        this.userProfile.email = this.userModule.infoEmail;
      }

      // 여전히 이름이 없으면 JWT에서 시도
      if (!this.userProfile.name) {
        try {
          const tokenData: any = getTokenInfo();
          if (tokenData && tokenData.name) {
            this.userProfile.name = tokenData.name;
          }
        } catch (e) {
          // JWT 디코딩 실패 시 무시
        }
      }

      // 매너 점수 로드
      if (userUid) {
        try {
          const mannerResponse = await getUserMannerScore(userUid);
          if (mannerResponse.data) {
            this.userStats.mannerscore = Math.round(mannerResponse.data.averageScore || 0);
          }
        } catch (mannerError) {
          console.warn('매너 점수 로드 실패:', mannerError);
          this.userStats.mannerscore = 0;
        } finally {
          this.isLoadingStats = false;
        }
      } else {
        this.isLoadingStats = false;
      }

      // 지갑 정보 로드
      try {
        const walletResponse = await getWallet();
        if (walletResponse.data) {
          this.userWallet.points = walletResponse.data.balance || 0;
        }
      } catch (walletError) {
        console.warn('지갑 정보 로드 실패:', walletError);
      }

      // 내 팀 정보 로드
      try {
        const teamsResponse = await getMyTeams();
        const teamData = teamsResponse.data;
        // /api/team/my 는 단일 객체 또는 배열을 반환할 수 있음
        let team = null;
        if (Array.isArray(teamData)) {
          team = teamData.length > 0 ? teamData[0] : null;
        } else {
          team = teamData;
        }
        if (team && team.name) {
          this.userStats.team = team.name;
          this.myTeamCode = team.teamCode || team.uid || '';
          // 팀을 생성했는지 확인
          try {
            const statusResponse = await checkMembershipStatus();
            const hasCreatedTeam = (statusResponse.data as any).hasCreatedTeam || false;
            if (hasCreatedTeam) {
              this.userStats.teamLabel = '나의 팀';
            } else {
              this.userStats.teamLabel = '소속 팀';
            }
          } catch (statusError) {
            console.warn('팀 생성 여부 확인 실패:', statusError);
            this.userStats.teamLabel = '소속 팀';
          }
        } else {
          this.userStats.team = '-';
          this.userStats.teamLabel = '소속 팀';
        }
      } catch (teamError) {
        console.warn('팀 정보 로드 실패:', teamError);
        this.userStats.team = '-';
        this.userStats.teamLabel = '소속 팀';
      } finally {
        this.isLoadingTeam = false;
      }

      // 참여 경기수 로드
      try {
        const matchesResponse = await getMatchList({ status: 'COMPLETED' });
        if (matchesResponse.data) {
          this.userStats.matches = matchesResponse.data.totalElements || 0;
        }
      } catch (matchError) {
        console.warn('경기 정보 로드 실패:', matchError);
      } finally {
        this.isLoadingMatches = false;
      }
    } catch (error) {
      console.error('사용자 데이터 로드 실패:', error);
      this.$message.error('사용자 정보를 불러오는데 실패했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private formatCurrency(value: number): string {
    return value.toLocaleString('ko-KR');
  }

  private editProfile(): void {
    this.$router.push({ name: 'ProfileEdit' });
  }

  private chargePoints(): void {
    this.$router.push({ name: 'CashCharge' });
  }

  private exchangePoints(): void {
    this.$message.info('포인트 교환 페이지로 이동합니다');
  }

  private viewPointHistory(): void {
    this.$message.info('포인트 내역 페이지로 이동합니다');
  }

  private openGuide(type: string): void {
    const guideMap: { [key: string]: string } = {
      league: '리그 시작하기',
      match: '경기 예약하기',
      team: '팀 관리하기',
    };
    this.$message.info(`${guideMap[type]} 가이드를 엽니다`);
  }

  private navigateTo(): void {
    this.$message.info('준비중입니다.');
  }

  private goToMyTeam(): void {
    if (this.myTeamCode) {
      this.$router.push({ name: 'TeamPage', params: { teamCode: this.myTeamCode } });
    } else {
      this.$message.info('소속된 팀이 없습니다.');
    }
  }

  private navigateToCashHistory(): void {
    this.$router.push({ name: 'CashHistory' });
  }

  private openGuideDetail(): void {
    this.$message.info('뛰장 가이드를 엽니다.');
  }

  private logout(): void {
    this.$confirm('로그아웃하시겠습니까?', '확인', {
      confirmButtonText: '로그아웃',
      cancelButtonText: '취소',
      type: 'warning',
    })
      .then(async () => {
        try {
          // Vuex 로그아웃 액션 실행 (토큰 및 세션 정보 삭제)
          await this.userModule.LogOut();
          this.$message.success('로그아웃되었습니다.');
          // 로그인 페이지로 이동
          this.$router.push({ name: 'Login' });
        } catch (error) {
          console.error('로그아웃 실패:', error);
          this.$message.error('로그아웃에 실패했습니다.');
        }
      })
      .catch(() => {
        // 취소 시 아무 작업 없음
      });
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
