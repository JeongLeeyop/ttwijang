<template>
  <div class="match-detail-page">
    <!-- Loading State -->
    <div v-if="isLoading" class="loading-container">
      <i class="el-icon-loading"></i>
      <p>불러오는 중...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="error-container">
      <el-alert
        :title="error"
        type="error"
        show-icon
        :closable="false"
      />
      <el-button
        size="small"
        type="primary"
        style="margin-top: 16px;"
        @click="loadDetail"
      >
        다시 시도
      </el-button>
    </div>

    <!-- Content -->
    <div v-else class="detail-content">
      <!-- Hero Section: Stadium Image + Badge -->
      <div class="hero-section">
        <div class="hero-image">
          <img
            :src="stadiumImage"
            alt="구장 이미지"
            @error="handleImageError"
          >
          <div class="hero-overlay"></div>
        </div>
        <div class="hero-badges">
          <span class="badge badge--type">{{ matchTypeBadge }}</span>
          <span v-if="matchFormatBadge" class="badge badge--format">{{ matchFormatBadge }}</span>
          <span v-if="statusBadge" class="badge badge--status" :class="statusBadgeClass">{{ statusBadge }}</span>
        </div>
      </div>

      <!-- Match Info Card -->
      <div class="info-card">
        <div class="info-card__datetime">
          <i class="el-icon-date"></i>
          <span>{{ formattedDate }}</span>
          <span v-if="formattedTime" class="info-card__time">{{ formattedTime }}</span>
        </div>
        <div class="info-card__stadium">
          <i class="el-icon-location-outline"></i>
          <span>{{ stadiumName }}</span>
        </div>
        <div v-if="stadiumAddress" class="info-card__address">
          <span>{{ stadiumAddress }}</span>
        </div>
        <div v-if="displayFee !== null" class="info-card__fee">
          <i class="el-icon-money"></i>
          <span>{{ displayFee === 0 ? '무료' : `${displayFee.toLocaleString()}원` }}</span>
          <span v-if="feeLabel" class="fee-label">{{ feeLabel }}</span>
        </div>
      </div>

      <!-- ==================== 리그 매치 (FR-27) ==================== -->
      <template v-if="detailType === 'league'">
        <div class="versus-section">
          <div class="versus-team">
            <img :src="homeTeamLogo" alt="홈팀" class="versus-logo">
            <span class="versus-name">{{ homeTeamName }}</span>
            <span v-if="homeScore !== null" class="versus-score">{{ homeScore }}</span>
          </div>
          <div class="versus-vs">
            <span v-if="homeScore !== null && awayScore !== null">{{ homeScore }} : {{ awayScore }}</span>
            <span v-else>VS</span>
          </div>
          <div class="versus-team">
            <img :src="awayTeamLogo" alt="원정팀" class="versus-logo">
            <span class="versus-name">{{ awayTeamName }}</span>
            <span v-if="awayScore !== null" class="versus-score">{{ awayScore }}</span>
          </div>
        </div>

        <!-- Accordion: 구장 정보 -->
        <div class="accordion-section">
          <div
            class="accordion-header"
            @click="toggleAccordion('stadium')"
          >
            <span>구장 정보</span>
            <i :class="accordionOpen.stadium ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
          </div>
          <div v-show="accordionOpen.stadium" class="accordion-body">
            <div class="accordion-item">
              <span class="accordion-label">구장명</span>
              <span class="accordion-value">{{ stadiumName }}</span>
            </div>
            <div v-if="stadiumAddress" class="accordion-item">
              <span class="accordion-label">주소</span>
              <span class="accordion-value">{{ stadiumAddress }}</span>
            </div>
          </div>
        </div>

        <!-- Accordion: 리그 규칙 -->
        <div v-if="leagueRules" class="accordion-section">
          <div
            class="accordion-header"
            @click="toggleAccordion('rules')"
          >
            <span>리그 규칙</span>
            <i :class="accordionOpen.rules ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
          </div>
          <div v-show="accordionOpen.rules" class="accordion-body">
            <div class="accordion-text" v-html="leagueRules"></div>
          </div>
        </div>
      </template>

      <!-- ==================== 친선 경기 (FR-28) ==================== -->
      <template v-if="detailType === 'friendly'">
        <div class="versus-section">
          <div class="versus-team">
            <img :src="hostTeamLogo" alt="주최팀" class="versus-logo">
            <span class="versus-name">{{ hostTeamName }}</span>
          </div>
          <div class="versus-vs">VS</div>
          <div class="versus-team">
            <div v-if="opponentTeamName" class="versus-team-filled">
              <img :src="opponentTeamLogo" alt="상대팀" class="versus-logo">
              <span class="versus-name">{{ opponentTeamName }}</span>
            </div>
            <div v-else class="versus-team-empty">
              <div class="empty-logo">?</div>
              <span class="versus-name">대전 상대 모집중</span>
            </div>
          </div>
        </div>

        <div class="team-stats-section">
          <h3 class="section-heading">주최 팀 정보</h3>
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-label">매너 점수</span>
              <span class="stat-value">{{ hostMannerScore }}점</span>
            </div>
            <div v-if="matchFormat" class="stat-item">
              <span class="stat-label">매치 방식</span>
              <span class="stat-value">{{ matchFormatBadge }}</span>
            </div>
          </div>
        </div>

        <div v-if="additionalInfo" class="additional-info-section">
          <h3 class="section-heading">추가 안내</h3>
          <p class="additional-text">{{ additionalInfo }}</p>
        </div>
      </template>

      <!-- ==================== 자유(소셜) 경기 (FR-29) ==================== -->
      <template v-if="detailType === 'free'">
        <div class="host-profile-section">
          <h3 class="section-heading">주최자 정보</h3>
          <div class="host-profile-card">
            <img :src="hostTeamLogo" alt="주최자" class="host-avatar">
            <div class="host-info">
              <span class="host-name">{{ hostTeamName }}</span>
              <div class="host-details">
                <span v-if="hostMannerScore" class="host-detail-item">
                  <i class="el-icon-star-on"></i> 매너 {{ hostMannerScore }}점
                </span>
              </div>
            </div>
          </div>
        </div>

        <div class="recruitment-status-section">
          <h3 class="section-heading">모집 현황</h3>
          <div class="recruitment-bar-wrapper">
            <div class="recruitment-bar">
              <div
                class="recruitment-bar-fill"
                :style="{ width: recruitmentPercent + '%' }"
              ></div>
            </div>
            <span class="recruitment-count">{{ currentMembers }} / {{ maxMembers }}명</span>
          </div>
        </div>

        <div v-if="additionalInfo" class="additional-info-section">
          <h3 class="section-heading">추가 안내</h3>
          <p class="additional-text">{{ additionalInfo }}</p>
        </div>
      </template>

      <!-- ==================== 게스트 모집 (FR-30) ==================== -->
      <template v-if="detailType === 'guest'">
        <div class="guest-team-section">
          <h3 class="section-heading">모집 팀 정보</h3>
          <div class="guest-team-card">
            <img :src="hostTeamLogo" alt="팀" class="guest-team-logo">
            <div class="guest-team-info">
              <span class="guest-team-name">{{ hostTeamName }}</span>
              <span v-if="hostMannerScore" class="guest-team-manner">
                <i class="el-icon-star-on"></i> 매너 {{ hostMannerScore }}점
              </span>
            </div>
          </div>
        </div>

        <div class="guest-recruit-details">
          <div class="recruit-detail-item">
            <span class="recruit-label">모집 포지션</span>
            <span class="recruit-value">{{ positionLabel }}</span>
          </div>
          <div class="recruit-detail-item">
            <span class="recruit-label">참가비</span>
            <span class="recruit-value">{{ guestFeeDisplay }}</span>
          </div>
          <div v-if="guaranteedMinutes" class="recruit-detail-item">
            <span class="recruit-label">보장 시간</span>
            <span class="recruit-value">{{ guaranteedMinutes }}분</span>
          </div>
          <div class="recruit-detail-item">
            <span class="recruit-label">모집 인원</span>
            <span class="recruit-value">{{ currentMembers }} / {{ maxMembers }}명</span>
          </div>
        </div>

        <!-- Accordion: 모집 정보 -->
        <div v-if="additionalInfo" class="accordion-section">
          <div
            class="accordion-header"
            @click="toggleAccordion('recruitInfo')"
          >
            <span>추가 안내 사항</span>
            <i :class="accordionOpen.recruitInfo ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
          </div>
          <div v-show="accordionOpen.recruitInfo" class="accordion-body">
            <p class="accordion-text">{{ additionalInfo }}</p>
          </div>
        </div>

        <!-- Accordion: 구장 정보 -->
        <div class="accordion-section">
          <div
            class="accordion-header"
            @click="toggleAccordion('stadium')"
          >
            <span>구장 정보</span>
            <i :class="accordionOpen.stadium ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
          </div>
          <div v-show="accordionOpen.stadium" class="accordion-body">
            <div class="accordion-item">
              <span class="accordion-label">구장명</span>
              <span class="accordion-value">{{ stadiumName }}</span>
            </div>
            <div v-if="stadiumAddress" class="accordion-item">
              <span class="accordion-label">주소</span>
              <span class="accordion-value">{{ stadiumAddress }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- Apply Button (Fixed Bottom) -->
      <div class="apply-button-wrapper">
        <el-button
          v-if="canApply"
          type="primary"
          class="apply-button"
          :loading="isApplying"
          :disabled="isRecruitmentClosed"
          @click="handleApply"
        >
          {{ applyButtonText }}
        </el-button>
        <el-button
          v-else-if="isRecruitmentClosed"
          class="apply-button apply-button--closed"
          disabled
        >
          모집 완료
        </el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';
import { getMatchDetail, applyToMatch } from '@/api/match';
import { getGuestRecruitmentDetail, applyAsGuest } from '@/api/guest';
import { getLeagueDetail } from '@/api/league';

type DetailType = 'league' | 'friendly' | 'free' | 'guest'

interface AccordionState {
  stadium: boolean
  rules: boolean
  recruitInfo: boolean
}

@Component
export default class MatchDetail extends Vue {
  private isLoading = true

  private isApplying = false

  private error: string | null = null

  private detailType: DetailType = 'friendly'

  private detailData: any = null

  private leagueData: any = null

  private accordionOpen: AccordionState = {
    stadium: false,
    rules: false,
    recruitInfo: false,
  }

  // ==================== Computed Properties ====================

  get matchUid(): string {
    return this.$route.params.uid || '';
  }

  get routeType(): string {
    return (this.$route.query.type as string) || 'match';
  }

  get stadiumImage(): string {
    return 'https://images.unsplash.com/photo-1575361204480-aadea25e6e68?w=800&h=400&fit=crop';
  }

  get matchTypeBadge(): string {
    switch (this.detailType) {
      case 'league': return '리그 매치';
      case 'friendly': return '친선 경기';
      case 'free': return '자유 경기';
      case 'guest': return '게스트 모집';
      default: return '';
    }
  }

  get matchFormatBadge(): string {
    const format = this.detailData?.matchFormat;
    if (!format) return '';
    const formatMap: { [key: string]: string } = {
      FOUR_VS_FOUR: '4 vs 4',
      FIVE_VS_FIVE: '5 vs 5',
      SIX_VS_SIX: '6 vs 6',
      SEVEN_VS_SEVEN: '7 vs 7',
    };
    return formatMap[format] || format;
  }

  get matchFormat(): string {
    return this.detailData?.matchFormat || '';
  }

  get statusBadge(): string {
    const status = this.detailData?.status;
    if (!status) return '';
    const statusMap: { [key: string]: string } = {
      RECRUITING: '모집중',
      MATCHED: '매칭완료',
      COMPLETED: '경기종료',
      CANCELLED: '취소됨',
      SCHEDULED: '예정',
      IN_PROGRESS: '진행중',
      EXPIRED: '만료',
    };
    return statusMap[status] || '';
  }

  get statusBadgeClass(): string {
    const status = this.detailData?.status;
    if (!status) return '';
    if (status === 'RECRUITING' || status === 'SCHEDULED') return 'badge--active';
    if (status === 'COMPLETED' || status === 'EXPIRED' || status === 'CANCELLED') return 'badge--inactive';
    return 'badge--progress';
  }

  get formattedDate(): string {
    const date = this.detailData?.matchDate;
    if (!date) return '';
    const d = new Date(date);
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const dayName = days[d.getDay()];
    return `${year}.${month}.${day} (${dayName})`;
  }

  get formattedTime(): string {
    const time = this.detailData?.matchTime;
    if (!time) return '';
    if (typeof time === 'string' && time.includes(':')) {
      return time.substring(0, 5);
    }
    return String(time);
  }

  get stadiumName(): string {
    return this.detailData?.stadiumName || '정보 없음';
  }

  get stadiumAddress(): string {
    return this.detailData?.stadiumAddress || '';
  }

  get displayFee(): number | null {
    const fee = this.detailData?.fee;
    if (fee === undefined || fee === null) return null;
    return fee;
  }

  get feeLabel(): string {
    if (this.detailType === 'guest') return '/ 1인';
    if (this.detailType === 'friendly' || this.detailType === 'league') return '/ 팀';
    return '';
  }

  // League match fields
  get homeTeamName(): string {
    return this.detailData?.homeTeamName || '';
  }

  get homeTeamLogo(): string {
    const url = this.detailData?.homeTeamLogoUrl;
    if (url) return url;
    return this.generateAvatar(this.homeTeamName);
  }

  get awayTeamName(): string {
    return this.detailData?.awayTeamName || '';
  }

  get awayTeamLogo(): string {
    const url = this.detailData?.awayTeamLogoUrl;
    if (url) return url;
    return this.generateAvatar(this.awayTeamName);
  }

  get homeScore(): number | null {
    const score = this.detailData?.homeScore;
    return score !== undefined && score !== null ? score : null;
  }

  get awayScore(): number | null {
    const score = this.detailData?.awayScore;
    return score !== undefined && score !== null ? score : null;
  }

  get leagueRules(): string {
    return this.leagueData?.rules || '';
  }

  // Friendly/Free match fields
  get hostTeamName(): string {
    return this.detailData?.hostTeamName
      || this.detailData?.teamName
      || '';
  }

  get hostTeamLogo(): string {
    const url = this.detailData?.hostTeamLogoUrl || this.detailData?.teamLogoUrl;
    if (url) return url;
    return this.generateAvatar(this.hostTeamName);
  }

  get opponentTeamName(): string {
    return this.detailData?.guestTeamName
      || this.detailData?.opponentTeamName
      || '';
  }

  get opponentTeamLogo(): string {
    const url = this.detailData?.guestTeamLogoUrl || this.detailData?.opponentTeamLogoUrl;
    if (url) return url;
    return this.generateAvatar(this.opponentTeamName);
  }

  get hostMannerScore(): number {
    return this.detailData?.hostTeamMannerScore
      || this.detailData?.teamMannerScore
      || 0;
  }

  get additionalInfo(): string {
    return this.detailData?.additionalInfo || this.detailData?.description || '';
  }

  // Guest fields
  get positionLabel(): string {
    const pos = this.detailData?.positionType;
    if (!pos) return '전체';
    const posMap: { [key: string]: string } = {
      FIELD: '필드',
      GK: '골키퍼',
      ANY: '전체',
    };
    return posMap[pos] || pos;
  }

  get guestFeeDisplay(): string {
    const fee = this.detailData?.fee;
    if (fee === undefined || fee === null || fee === 0) return '무료';
    return `${fee.toLocaleString()}원`;
  }

  get guaranteedMinutes(): number | null {
    return this.detailData?.guaranteedMinutes || null;
  }

  get currentMembers(): number {
    return this.detailData?.currentGuests || 0;
  }

  get maxMembers(): number {
    return this.detailData?.maxGuests || 0;
  }

  get recruitmentPercent(): number {
    if (this.maxMembers === 0) return 0;
    return Math.min((this.currentMembers / this.maxMembers) * 100, 100);
  }

  get isRecruitmentClosed(): boolean {
    const status = this.detailData?.status;
    if (status === 'COMPLETED' || status === 'CANCELLED' || status === 'EXPIRED' || status === 'MATCHED') {
      return true;
    }
    if (this.detailType === 'guest' || this.detailType === 'free') {
      return this.currentMembers >= this.maxMembers;
    }
    return false;
  }

  get canApply(): boolean {
    return this.detailType !== 'league' || this.detailData?.status === 'SCHEDULED';
  }

  get applyButtonText(): string {
    if (this.isRecruitmentClosed) return '모집 완료';
    if (this.detailType === 'guest') return '게스트 신청하기';
    if (this.detailType === 'friendly') return '매치 신청하기';
    if (this.detailType === 'free') return '참가 신청하기';
    return '신청하기';
  }

  // ==================== Lifecycle ====================

  async created(): Promise<void> {
    await this.loadDetail();
  }

  // ==================== Methods ====================

  private generateAvatar(name: string): string {
    const initials = (name || '?').substring(0, 2);
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(initials)}&background=061da1&color=fff&size=80`;
  }

  private handleImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = 'https://images.unsplash.com/photo-1575361204480-aadea25e6e68?w=800&h=400&fit=crop';
  }

  private toggleAccordion(key: keyof AccordionState): void {
    this.accordionOpen[key] = !this.accordionOpen[key];
  }

  private async loadDetail(): Promise<void> {
    this.isLoading = true;
    this.error = null;

    try {
      const type = this.routeType;
      const { uid } = this.$route.params;

      if (!uid) {
        this.error = '잘못된 접근입니다. (ID 없음)';
        return;
      }

      if (type === 'league') {
        await this.loadLeagueMatchDetail(uid);
      } else if (type === 'guest') {
        await this.loadGuestDetail(uid);
      } else {
        await this.loadMatchDetail(uid);
      }
    } catch (err: unknown) {
      this.handleError(err);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadMatchDetail(uid: string): Promise<void> {
    const response = await getMatchDetail(uid);
    this.detailData = response.data;

    const matchType = this.detailData?.matchType;
    if (matchType === 'FRIENDLY') {
      this.detailType = 'friendly';
    } else {
      this.detailType = 'free';
    }
  }

  private async loadGuestDetail(uid: string): Promise<void> {
    const response = await getGuestRecruitmentDetail(uid);
    this.detailData = response.data;
    this.detailType = 'guest';
  }

  private async loadLeagueMatchDetail(uid: string): Promise<void> {
    const leagueUid = (this.$route.query.leagueUid as string) || '';

    // league match detail 은 schedule 에서 개별 매치 정보를 가져옴
    // 리그 상세로 전체 정보를 가져오고, 해당 매치를 필터링
    if (leagueUid) {
      try {
        const leagueResponse = await getLeagueDetail(leagueUid);
        this.leagueData = leagueResponse.data;

        // 다가오는 경기 + 최근 결과에서 uid 매칭
        const allMatches = [
          ...(this.leagueData?.upcomingMatches || []),
          ...(this.leagueData?.recentResults || []),
        ];
        const matchData = allMatches.find((m: any) => m.uid === uid);

        if (matchData) {
          this.detailData = matchData;
        } else {
          // 매치를 찾지 못하면 기본 데이터 세팅
          this.detailData = {
            uid,
            stadiumName: this.leagueData?.regionSido || '정보 없음',
          };
        }
      } catch {
        this.detailData = {
          uid,
          stadiumName: '정보 없음',
        };
      }
    } else {
      // leagueUid 없으면 match API로 fallback
      const response = await getMatchDetail(uid);
      this.detailData = response.data;
    }

    this.detailType = 'league';
  }

  private handleError(err: unknown): void {
    if (err instanceof Error) {
      this.error = err.message || '데이터를 불러오는데 실패했습니다.';
    } else {
      this.error = '알 수 없는 오류가 발생했습니다.';
    }

    if (process.env.NODE_ENV === 'development') {
      console.error('[MatchDetail Error]', err);
    }
  }

  private async handleApply(): Promise<void> {
    this.isApplying = true;

    try {
      if (this.detailType === 'guest') {
        await applyAsGuest({
          recruitmentUid: this.matchUid,
        });
        this.$message.success('게스트 신청이 완료되었습니다.');
      } else if (this.detailType === 'friendly' || this.detailType === 'free') {
        await applyToMatch({
          matchUid: this.matchUid,
        });
        this.$message.success('매치 신청이 완료되었습니다.');
      }

      // 데이터 갱신
      await this.loadDetail();
    } catch (err: any) {
      const message = err?.response?.data?.message || '신청에 실패했습니다.';
      this.$message.error(message);
    } finally {
      this.isApplying = false;
    }
  }
}
</script>

<style scoped>
.match-detail-page {
  min-height: 100vh;
  background: #f5f5f7;
  padding-bottom: 100px;
}

/* Loading / Error */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  color: #999;
  font-size: 14px;
}

.loading-container i {
  font-size: 32px;
  margin-bottom: 12px;
}

.error-container {
  padding: 40px 20px;
  text-align: center;
}

/* Hero Section */
.hero-section {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.hero-image {
  width: 100%;
  height: 100%;
}

.hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.4));
}

.hero-badges {
  position: absolute;
  bottom: 12px;
  left: 16px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
}

.badge--type {
  background: #061da1;
}

.badge--format {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(4px);
}

.badge--status.badge--active {
  background: #67c23a;
}

.badge--status.badge--inactive {
  background: #909399;
}

.badge--status.badge--progress {
  background: #e6a23c;
}

/* Info Card */
.info-card {
  background: #fff;
  /* margin: -20px 16px 16px; */
  border-radius: 16px;
  padding: 20px;
  position: relative;
  z-index: 1;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.info-card > div {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 14px;
  color: #333;
}

.info-card > div:last-child {
  margin-bottom: 0;
}

.info-card i {
  color: #061da1;
  font-size: 16px;
  width: 20px;
  text-align: center;
}

.info-card__time {
  color: #666;
  margin-left: 4px;
}

.info-card__address {
  padding-left: 28px;
  font-size: 13px;
  color: #999;
}

.fee-label {
  font-size: 12px;
  color: #999;
  margin-left: 4px;
}

/* Versus Section */
.versus-section {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  /* margin: 0 16px 16px; */
  border-radius: 16px;
  padding: 24px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.versus-team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.versus-logo {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.versus-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  text-align: center;
  word-break: keep-all;
}

.versus-score {
  font-size: 28px;
  font-weight: 800;
  color: #061da1;
}

.versus-vs {
  font-size: 20px;
  font-weight: 800;
  color: #ccc;
  flex-shrink: 0;
}

.versus-team-empty .empty-logo {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #bbb;
  font-weight: bold;
}

/* Team Stats */
.section-heading {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

.team-stats-section {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.stat-value {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

/* Host Profile (Free match) */
.host-profile-section {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.host-profile-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.host-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.host-info {
  flex: 1;
}

.host-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  display: block;
  margin-bottom: 4px;
}

.host-details {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.host-detail-item {
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 3px;
}

.host-detail-item i {
  color: #f7c600;
  font-size: 14px;
}

/* Recruitment Status */
.recruitment-status-section {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.recruitment-bar-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.recruitment-bar {
  flex: 1;
  height: 12px;
  background: #eee;
  border-radius: 6px;
  overflow: hidden;
}

.recruitment-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #061da1, #4a7cff);
  border-radius: 6px;
  transition: width 0.4s ease;
}

.recruitment-count {
  font-size: 14px;
  font-weight: 700;
  color: #333;
  white-space: nowrap;
}

/* Guest Team Section */
.guest-team-section {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.guest-team-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.guest-team-logo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.guest-team-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.guest-team-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.guest-team-manner {
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 4px;
}

.guest-team-manner i {
  color: #f7c600;
}

/* Guest Recruit Details */
.guest-recruit-details {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.recruit-detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.recruit-detail-item:last-child {
  border-bottom: none;
}

.recruit-label {
  font-size: 14px;
  color: #666;
}

.recruit-value {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

/* Additional Info */
.additional-info-section {
  background: #fff;
  margin: 0 16px 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.additional-text {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Accordion */
.accordion-section {
  background: #fff;
  margin: 0 16px 12px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  user-select: none;
  transition: background 0.2s;
}

.accordion-header:hover {
  background: #fafafa;
}

.accordion-header i {
  color: #999;
  font-size: 14px;
  transition: transform 0.2s;
}

.accordion-body {
  padding: 0 20px 16px;
}

.accordion-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.accordion-item:last-child {
  border-bottom: none;
}

.accordion-label {
  font-size: 13px;
  color: #999;
}

.accordion-value {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  text-align: right;
  max-width: 60%;
}

.accordion-text {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Apply Button (Fixed bottom) */
.apply-button-wrapper {
  /* position: fixed; */
  bottom: 60px;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: linear-gradient(transparent, #f5f5f7 20%);
  z-index: 99;
}

.apply-button {
  width: 100%;
  height: 52px;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 700;
  background: #061da1 !important;
  border-color: #061da1 !important;
  color: #fff !important;
  box-shadow: 0 4px 12px rgba(6, 29, 161, 0.3);
}

.apply-button:hover {
  background: #0a25c4 !important;
  border-color: #0a25c4 !important;
}

.apply-button--closed {
  background: #ccc !important;
  border-color: #ccc !important;
  color: #fff !important;
  box-shadow: none;
}

.apply-button--closed:hover {
  background: #ccc !important;
  border-color: #ccc !important;
}
</style>
