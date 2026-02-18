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
      <!-- Hero Section: Stadium Image + Badges -->
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
          <span
            v-if="matchFormatBadge"
            class="badge badge--format"
          >{{ matchFormatBadge }}</span>
        </div>
      </div>

      <!-- Date / Stadium / Fee Card -->
      <div class="info-card">
        <div class="info-row">
          <i class="el-icon-date"></i>
          <span>{{ formattedDateTime }}</span>
        </div>
        <div class="info-row">
          <i class="el-icon-location-outline"></i>
          <span class="stadium-name-text">{{ stadiumName }}</span>
        </div>
        <div
          v-if="stadiumAddress"
          class="info-row info-row--sub"
        >
          <span>{{ stadiumAddress }}</span>
        </div>
        <div
          v-if="displayFee !== null"
          class="info-row info-row--fee"
        >
          <span class="fee-value">{{ displayFee === 0 ? '무료' : `${displayFee.toLocaleString()}원` }}</span>
          <span class="fee-label">/ {{ feeLabel }}</span>
          <span
            v-if="statusBadge"
            class="status-chip"
            :class="statusBadgeClass"
          >{{ statusBadge }}</span>
        </div>
      </div>

      <!-- =============== 리그 매치 =============== -->
      <template v-if="detailType === 'league'">
        <!-- 팀 정보 -->
        <div class="section-block">
          <h3 class="section-title">팀 정보</h3>
          <div class="versus-row">
            <div class="versus-team">
              <img
                :src="homeTeamLogo"
                alt="홈팀"
                class="versus-logo"
              >
              <span class="versus-name">{{ homeTeamName }}</span>
            </div>
            <div class="versus-vs">
              <template v-if="homeScore !== null && awayScore !== null">
                {{ homeScore }} : {{ awayScore }}
              </template>
              <template v-else>VS</template>
            </div>
            <div class="versus-team">
              <img
                :src="awayTeamLogo"
                alt="원정팀"
                class="versus-logo"
              >
              <span class="versus-name">{{ awayTeamName }}</span>
            </div>
          </div>
        </div>

        <!-- 리그 매치 상세 정보 -->
        <div class="section-block">
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">- 팀 명</span>
              <span class="info-list-value">{{ homeTeamName }} vs {{ awayTeamName }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 홈 구장</span>
              <span class="info-list-value">{{ stadiumName }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 팀 회원수</span>
              <span class="info-list-value">{{ detailData?.homeTeamMemberCount || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 평균 나이</span>
              <span class="info-list-value">{{ detailData?.averageAge || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 전적 (전체)</span>
              <span class="info-list-value">{{ leagueRecord }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 리그 전적</span>
              <span class="info-list-value">{{ leagueStandingRecord }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- =============== 친선 경기 =============== -->
      <template v-if="detailType === 'friendly'">
        <!-- 매치 정보 -->
        <div class="section-block">
          <h3 class="section-title">매치 정보</h3>
          <div class="match-host-card">
            <img
              :src="hostTeamLogo"
              alt="주최팀"
              class="host-logo"
            >
            <div class="host-info">
              <span class="host-name">{{ hostTeamName }}</span>
              <span
                v-if="hostMannerScore"
                class="host-manner"
              >매너 {{ hostMannerScore }}점</span>
            </div>
          </div>
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">- 성별</span>
              <span class="info-list-value">{{ genderText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 연령대</span>
              <span class="info-list-value">{{ ageGroupsText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 시간</span>
              <span class="info-list-value">{{ durationText }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 수</span>
              <span class="info-list-value">{{ detailData?.matchCount || 1 }}경기</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 매너 점수</span>
              <span class="info-list-value">{{ hostMannerScore || '-' }}점</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 리그 등급</span>
              <span class="info-list-value">{{ detailData?.leagueGrade || '-' }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- =============== 자유(소셜) 경기 =============== -->
      <template v-if="detailType === 'free'">
        <!-- 매치 정보 -->
        <div class="section-block">
          <h3 class="section-title">매치 정보</h3>
          <div class="match-host-card">
            <div class="host-avatar-circle">
              <img
                :src="hostTeamLogo"
                alt="주최자"
                class="host-logo"
              >
            </div>
            <div class="host-info">
              <span class="host-name">{{ hostTeamName }}</span>
            </div>
          </div>
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">- 성별</span>
              <span class="info-list-value">{{ genderText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 연령대</span>
              <span class="info-list-value">{{ ageGroupsText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 시간</span>
              <span class="info-list-value">{{ durationText }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 수</span>
              <span class="info-list-value">{{ detailData?.matchCount || 1 }}경기</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 필드/골키퍼</span>
              <span class="info-list-value">{{ positionLabel }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- =============== 게스트 모집 =============== -->
      <template v-if="detailType === 'guest'">
        <!-- 매치 정보 -->
        <div class="section-block">
          <h3 class="section-title">매치 정보</h3>
          <div class="match-host-card">
            <img
              :src="hostTeamLogo"
              alt="모집팀"
              class="host-logo"
            >
            <div class="host-info">
              <span class="host-name">{{ hostTeamName }}</span>
              <span
                v-if="hostMannerScore"
                class="host-manner"
              >매너 {{ hostMannerScore }}점</span>
            </div>
          </div>
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">- 성별</span>
              <span class="info-list-value">{{ genderText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 연령대</span>
              <span class="info-list-value">{{ ageGroupsText || '-' }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 시간</span>
              <span class="info-list-value">{{ durationText }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 경기 수</span>
              <span class="info-list-value">{{ detailData?.matchCount || 1 }}경기</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 매너 점수</span>
              <span class="info-list-value">{{ hostMannerScore || '-' }}점</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">- 리그 등급</span>
              <span class="info-list-value">{{ detailData?.leagueGrade || '-' }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- =============== 참여자 명단 (공통) =============== -->
      <div class="section-block">
        <h3 class="section-title">참여자 명단</h3>
        <div
          v-if="participants.length > 0"
          class="participant-grid"
        >
          <div
            v-for="p in participants"
            :key="p.uid || p.name"
            class="participant-item"
          >
            <div
              class="participant-avatar"
              :style="{ background: getAvatarColor(p.name) }"
            >
              {{ getInitial(p.name) }}
            </div>
            <span class="participant-name">{{ p.name }}</span>
          </div>
        </div>
        <div
          v-else
          class="empty-participants"
        >
          <span>아직 참여자가 없습니다.</span>
        </div>
      </div>

      <!-- =============== Accordions (공통) =============== -->
      <!-- 모집 정보 (게스트만) -->
      <div
        v-if="detailType === 'guest'"
        class="accordion-section"
      >
        <div
          class="accordion-header"
          @click="toggleAccordion('recruitInfo')"
        >
          <span>모집 정보</span>
          <i class="el-icon-heart"></i>
        </div>
        <div
          v-show="accordionOpen.recruitInfo"
          class="accordion-body"
        >
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">모집 포지션</span>
              <span class="info-list-value">{{ positionLabel }}</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">참가비</span>
              <span class="info-list-value">{{ guestFeeDisplay }}</span>
            </div>
            <div
              v-if="guaranteedMinutes"
              class="info-list-item"
            >
              <span class="info-list-label">보장 시간</span>
              <span class="info-list-value">{{ guaranteedMinutes }}분</span>
            </div>
            <div class="info-list-item">
              <span class="info-list-label">모집 인원</span>
              <span class="info-list-value">{{ currentMembers }} / {{ maxMembers }}명</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 구장 정보 -->
      <div class="accordion-section">
        <div
          class="accordion-header"
          @click="toggleAccordion('stadium')"
        >
          <span>구장 정보</span>
          <i class="el-icon-heart"></i>
        </div>
        <div
          v-show="accordionOpen.stadium"
          class="accordion-body"
        >
          <div class="info-list">
            <div class="info-list-item">
              <span class="info-list-label">구장명</span>
              <span class="info-list-value">{{ stadiumName }}</span>
            </div>
            <!-- <div
              v-if="stadiumAddress"
              class="info-list-item"
            >
              <span class="info-list-label">주소</span>
              <span class="info-list-value">{{ stadiumAddress }}</span>
            </div> -->
          </div>
        </div>
      </div>

      <!-- 리그 규칙 (리그만) / 매치 규칙 (나머지) -->
      <div
        v-if="detailType === 'league' && leagueRules"
        class="accordion-section"
      >
        <div
          class="accordion-header"
          @click="toggleAccordion('rules')"
        >
          <span>리그 규칙</span>
          <i class="el-icon-heart"></i>
        </div>
        <div
          v-show="accordionOpen.rules"
          class="accordion-body"
        >
          <div
            class="accordion-text"
            v-html="leagueRules"
          ></div>
        </div>
      </div>
      <div
        v-if="detailType !== 'league'"
        class="accordion-section"
      >
        <div
          class="accordion-header"
          @click="toggleAccordion('rules')"
        >
          <span>매치 규칙</span>
          <i class="el-icon-heart"></i>
        </div>
        <div
          v-show="accordionOpen.rules"
          class="accordion-body"
        >
          <p class="accordion-text">{{ detailData?.matchRules || '별도의 특별한 규칙은 없습니다.' }}</p>
        </div>
      </div>

      <!-- 추가 사항 (리그 제외) -->
      <div
        v-if="detailType !== 'league' && additionalInfo"
        class="accordion-section"
      >
        <div
          class="accordion-header"
          @click="toggleAccordion('additional')"
        >
          <span>추가 사항</span>
          <i class="el-icon-heart"></i>
        </div>
        <div
          v-show="accordionOpen.additional"
          class="accordion-body"
        >
          <p class="accordion-text">{{ additionalInfo }}</p>
        </div>
      </div>

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
  additional: boolean
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
    additional: false,
  }

  private avatarColors = [
    '#061da1', '#e74c3c', '#27ae60', '#f39c12',
    '#8e44ad', '#16a085', '#d35400', '#2c3e50',
  ]

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
    if (status === 'RECRUITING' || status === 'SCHEDULED') return 'status--active';
    if (status === 'COMPLETED' || status === 'EXPIRED' || status === 'CANCELLED') return 'status--inactive';
    return 'status--progress';
  }

  get formattedDateTime(): string {
    const date = this.detailData?.matchDate;
    if (!date) return '';
    const d = new Date(date);
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const year = String(d.getFullYear()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const dayName = days[d.getDay()];
    let result = `${year}월 ${month}월 ${day}일 (${dayName}요일)`;
    const time = this.detailData?.matchTime;
    if (time) {
      const t = typeof time === 'string' && time.includes(':') ? time.substring(0, 5) : String(time);
      result += ` ${t}`;
    }
    return result;
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
    if (this.detailType === 'guest') return '게스트 당';
    if (this.detailType === 'free') return '개인 당';
    return '팀 당';
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
    if (this.detailType === 'guest') {
      return this.detailData?.currentGuests || 0;
    }
    return this.detailData?.currentPlayers || 0;
  }

  get maxMembers(): number {
    if (this.detailType === 'guest') {
      return this.detailData?.maxGuests || 0;
    }
    return this.detailData?.maxPlayers || 0;
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
    if (this.detailType === 'guest' || this.detailType === 'free' || this.detailType === 'friendly') {
      return this.maxMembers > 0 && this.currentMembers >= this.maxMembers;
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

  /* eslint-disable no-bitwise */
  get genderText(): string {
    const val = this.detailData?.genderType;
    if (val === 0) return '남자';
    if (val === 1) return '여자';
    if (val === 2) return '남녀무관';
    return '';
  }

  get ageGroupsText(): string {
    const val = this.detailData?.ageGroups;
    if (!val) return '';
    if (val === 31) return '나이 무관';
    const labels = ['10대', '20대', '30대', '40대', '50대 이상'];
    const ages: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) ages.push(label);
    });
    return ages.join(', ');
  }
  /* eslint-enable no-bitwise */

  get durationText(): string {
    const hours = this.detailData?.durationHours;
    if (!hours) return '-';
    return `${hours}시간`;
  }

  get leagueRecord(): string {
    const d = this.detailData;
    if (!d) return '-';
    const wins = d.totalWins || 0;
    const draws = d.totalDraws || 0;
    const losses = d.totalLosses || 0;
    return `${wins}승 ${draws}무 ${losses}패`;
  }

  get leagueStandingRecord(): string {
    if (!this.leagueData?.standings?.length) return '-';
    const home = this.leagueData.standings.find((s: any) => s.teamUid === this.detailData?.homeTeamUid);
    if (!home) return '-';
    return `${home.wins || 0}승 ${home.draws || 0}무 ${home.losses || 0}패`;
  }

  get participants(): any[] {
    const d = this.detailData;
    if (!d) return [];
    if (d.participants && Array.isArray(d.participants)) return d.participants;
    if (d.guests && Array.isArray(d.guests)) return d.guests;
    if (d.members && Array.isArray(d.members)) return d.members;
    // Generate sample participants from names if available
    return [];
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

  private getInitial(name: string): string {
    return (name || '?').substring(0, 1);
  }

  private getAvatarColor(name: string): string {
    let hash = 0;
    for (let i = 0; i < (name || '').length; i += 1) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash); // eslint-disable-line no-bitwise
    }
    return this.avatarColors[Math.abs(hash) % this.avatarColors.length];
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
}

.badge {
  padding: 4px 14px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 700;
  color: #fff;
}

.badge--type {
  background: #061da1;
}

.badge--format {
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(4px);
}

/* Info Card */
.info-card {
  background: #fff;
  padding: 18px 20px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #333;
  margin-bottom: 6px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-row i {
  color: #061da1;
  font-size: 16px;
  width: 18px;
  text-align: center;
  flex-shrink: 0;
}

.info-row--sub {
  padding-left: 26px;
  font-size: 13px;
  color: #888;
}

.info-row--fee {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.fee-value {
  font-size: 20px;
  font-weight: 800;
  color: #061da1;
}

.fee-label {
  font-size: 13px;
  color: #888;
  margin-left: 4px;
}

.status-chip {
  margin-left: auto;
  padding: 3px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
}

.status--active {
  background: #67c23a;
}

.status--inactive {
  background: #909399;
}

.status--progress {
  background: #e6a23c;
}

/* Section Block */
.section-block {
  background: #fff;
  margin-top: 10px;
  padding: 20px;
}

.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  margin: 0 0 14px;
}

/* Versus Row (리그) */
.versus-row {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0 16px;
}

.versus-team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.versus-logo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.versus-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.versus-vs {
  font-size: 18px;
  font-weight: 800;
  color: #ccc;
  flex-shrink: 0;
  padding: 0 12px;
}

/* Match Host Card (친선/자유/게스트) */
.match-host-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 14px;
  margin-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.host-logo {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.host-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.host-name {
  font-size: 15px;
  font-weight: 700;
  color: #333;
}

.host-manner {
  font-size: 12px;
  color: #061da1;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: #f0f2ff;
  padding: 2px 8px;
  border-radius: 10px;
  width: fit-content;
}

/* Info List (label-value rows) */
.info-list {
  display: flex;
  flex-direction: column;
}

.info-list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 7px 0;
}

.info-list-label {
  font-size: 13px;
  color: #666;
}

.info-list-value {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  text-align: right;
  max-width: 60%;
}

/* Participant Grid */
.participant-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 64px);
  gap: 12px;
  justify-content: start;
}

.participant-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.participant-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
}

.participant-name {
  font-size: 11px;
  color: #555;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 64px;
}

.empty-participants {
  text-align: center;
  padding: 20px 0;
  font-size: 13px;
  color: #bbb;
}

/* Accordion */
.accordion-section {
  background: #fff;
  margin-top: 10px;
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
}

.accordion-header i {
  color: #e74c3c;
  font-size: 14px;
}

.accordion-body {
  padding: 0 20px 16px;
}

.accordion-text {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Apply Button (Fixed bottom) */
.apply-button-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.06);
  z-index: 99;
}

.apply-button {
  width: 100%;
  height: 50px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 700;
  background: #061da1 !important;
  border-color: #061da1 !important;
  color: #fff !important;
}

.apply-button:hover {
  background: #0a25c4 !important;
  border-color: #0a25c4 !important;
}

.apply-button--closed {
  background: #ccc !important;
  border-color: #ccc !important;
  color: #fff !important;
}

.apply-button--closed:hover {
  background: #ccc !important;
  border-color: #ccc !important;
}
</style>
