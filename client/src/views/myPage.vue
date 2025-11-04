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

@Component({
  name: 'MyPage',
})
export default class MyPage extends Vue {
  private userProfile = {
    name: '오형래',
    email: 'kim.chulsu@example.com',
    avatar: 'https://ui-avatars.com/api/?name=CS&background=061da1&color=fff&size=80&bold=true',
  };

  private userStats = {
    matches: 15,
    wins: 10,
    losses: 5,
    totalMatches: 45,
    avgGoals: 2.3,
    winRate: 68,
    ranking: 42,
    mannerscore: 95,
    team: '위더스 FC',
  };

  private userWallet = {
    points: 25000,
    coupons: 3,
  };

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

  private navigateTo(path: string): void {
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
      .then(() => {
        this.$router.push('/login');
      })
      .catch(() => {
        // Cancel logout
      });
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
