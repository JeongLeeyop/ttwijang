<template>
  <div class="main mypage-page">
    <div class="mypage-wrapper content">
      <section class="profile-section">
        <div class="profile-header">
          <div class="profile-info">
            <h2 class="profile-name">{{ userProfile.name }}</h2>
            <p class="profile-email">{{ userProfile.email }}</p>
          </div>
          <button class="wallet-btn charge-btn" @click="chargePoints">
              <i class="el-icon-edit"></i>
              프로필 수정
            </button>
        </div>

        <!-- Stats Row Section -->
        <section class="stats-row-section">
          <div class="stat-button">
            <div class="stat-label">매너점수</div>
            <div class="stat-value">{{ userStats.mannerscore }}</div>
          </div>
          <div class="stat-button">
            <div class="stat-label">소속 팀</div>
            <div class="stat-value">{{ userStats.team }}</div>
          </div>
          <div class="stat-button">
            <div class="stat-label">참여 경기수</div>
            <div class="stat-value">{{ userStats.matches }}</div>
          </div>
        </section>

        <!-- Cash/Wallet Section -->
        <section class="wallet-section">
        <div class="wallet-card">
          <div class="wallet-header"><!-- Cash/Wallet Section -->
            <span class="wallet-label">보유 포인트</span>
            <i class="el-icon-info"></i>
          </div>
          <div class="wallet-balance">
            <div class="balance-wrapper">
              <span class="balance-amount">{{ formatCurrency(userWallet.points) }}</span>
              <span class="balance-unit">포인트</span>
            </div>
            <div class="wallet-actions">
            <button class="wallet-btn charge-btn" @click="chargePoints">
              <i class="el-icon-plus"></i>
              캐시 충전
            </button>
            </div>
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
          <div class="menu-item" @click="navigateTo('my-posts')">
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
import { getMyTeams } from '@/api/team';
import { getUserMannerScore } from '@/api/mannerRating';
import { getUserInfo } from '@/api/user';
import { getMatchList } from '@/api/match';

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
  };

  private userWallet = {
    points: 0,
    coupons: 0,
  };

  private isLoading = false;

  async mounted() {
    // 마이페이지 헤더 배경색 설정을 위한 클래스 추가
    const mainLayout = document.querySelector('.main-layout');
    if (mainLayout) {
      mainLayout.classList.add('mypage-active');
    }

    // 로그인 상태 체크
    if (!this.userModule.isLogin) {
      this.$message.warning('로그인이 필요한 페이지입니다.');
      this.$router.replace({ name: 'Login' });
      return;
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
      // 사용자 프로필 정보 로드
      const userInfoResponse = await getUserInfo();
      if (userInfoResponse.data) {
        this.userProfile.name = userInfoResponse.data.name || '';
        this.userProfile.email = userInfoResponse.data.email || '';
        // 아바타 URL 생성 (이름 기반)
        if (this.userProfile.name) {
          const initials = this.userProfile.name.slice(0, 2);
          this.userProfile.avatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=061da1&color=fff&size=80&bold=true`;
        }

        // 매너 점수 로드 (사용자 UID 사용)
        if (userInfoResponse.data.uid) {
          try {
            const mannerResponse = await getUserMannerScore(userInfoResponse.data.uid);
            if (mannerResponse.data) {
              this.userStats.mannerscore = Math.round(mannerResponse.data.averageScore || 0);
            }
          } catch (mannerError) {
            console.warn('매너 점수 로드 실패:', mannerError);
            this.userStats.mannerscore = 0;
          }
        }
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
        if (teamsResponse.data && teamsResponse.data.length > 0) {
          this.userStats.team = teamsResponse.data[0].name;
        } else {
          this.userStats.team = '-';
        }
      } catch (teamError) {
        console.warn('팀 정보 로드 실패:', teamError);
        this.userStats.team = '-';
      }

      // 참여 경기수 로드
      try {
        const matchesResponse = await getMatchList({ status: 'FINISHED' });
        if (matchesResponse.data) {
          this.userStats.matches = matchesResponse.data.totalElements || 0;
        }
      } catch (matchError) {
        console.warn('경기 정보 로드 실패:', matchError);
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
    this.$message.info('프로필 수정 페이지로 이동합니다');
  }

  private chargePoints(): void {
    this.$message.info('포인트 충전 페이지로 이동합니다');
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
