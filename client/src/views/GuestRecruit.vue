<template>
  <div class="guest-recruit-page">
    <div class="content">
      <!-- Step 1: 매치 선택 -->
      <div v-if="step === 1" class="step-content">
        <h2 class="page-title">언제 게스트를 <br> 모집할까요?</h2>
        <p class="page-desc">게스트는 7일 이내의 일정에만 모집할 수 있어요.</p>

        <div v-if="isLoadingMatches" class="loading-area">
          <i class="el-icon-loading"></i>
          <span>매치 일정을 불러오는 중...</span>
        </div>

        <div v-else-if="upcomingMatches.length === 0" class="empty-area">
          <p>7일 이내 매치 일정이 없습니다.</p>
          <button class="link-button" @click="goToMatchCreate">
            매치 일정 먼저 만들기
          </button>
        </div>

        <div v-else class="match-list">
          <div
            v-for="match in upcomingMatches"
            :key="match.uid"
            class="match-card"
            :class="{ selected: selectedMatchUid === match.uid }"
            @click="selectMatch(match)"
          >
            <div class="match-card-header">
              <span class="match-type-badge">
                {{ match.matchType === 'FRIENDLY' ? '친선경기' : '자체경기' }}
              </span>
              <span class="match-format">{{ formatLabel(match.matchFormat) }}</span>
            </div>
            <div class="match-card-body">
              <div class="match-date-info">
                <i class="el-icon-date"></i>
                <span>{{ formatDate(match.matchDate) }} {{ match.matchTime }}</span>
              </div>
              <div class="match-location-info">
                <i class="el-icon-location-outline"></i>
                <span>{{ match.stadiumName }}</span>
              </div>
            </div>
            <div v-if="selectedMatchUid === match.uid" class="selected-check">
              <i class="el-icon-check"></i>
            </div>
          </div>
        </div>

        <button
          class="next-button"
          :disabled="!selectedMatchUid && !hasQueryMatch"
          @click="goToStep2"
        >
          다음
        </button>
      </div>

      <!-- Step 2: 모집 조건 설정 -->
      <div v-if="step === 2" class="step-content">
        <h2 class="page-title">이런 게스트를 <br> 모집해요!</h2>

        <!-- 성별 -->
        <div class="form-group">
          <label>성별</label>
          <div class="option-buttons">
            <button
              v-for="opt in genderOptions"
              :key="opt.value"
              class="option-button"
              :class="{ active: genderType === opt.value }"
              @click="genderType = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 나이 -->
        <div class="form-group">
          <label>나이</label>
          <div class="option-buttons wrap">
            <button
              v-for="opt in ageOptions"
              :key="opt.value"
              class="option-button"
              :class="{ active: ageGroups === opt.value }"
              @click="ageGroups = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 포지션 -->
        <div class="form-group">
          <label>포지션</label>
          <div class="option-buttons">
            <button
              v-for="opt in positionOptions"
              :key="opt.value"
              class="option-button"
              :class="{ active: positionType === opt.value }"
              @click="positionType = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 참가비 -->
        <div class="form-group">
          <label>참가비</label>
          <div class="option-buttons">
            <button
              v-for="opt in feeOptions"
              :key="opt.value"
              class="option-button"
              :class="{ active: fee === opt.value }"
              @click="fee = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 보장시간 -->
        <div class="form-group">
          <label>보장시간</label>
          <div class="option-buttons">
            <button
              v-for="opt in guaranteedOptions"
              :key="opt.value"
              class="option-button"
              :class="{ active: guaranteedMinutes === opt.value }"
              @click="guaranteedMinutes = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <button class="next-button" @click="goToStep3">다음</button>
      </div>

      <!-- Step 3: 추가 사항 입력 -->
      <div v-if="step === 3" class="step-content">
        <h2 class="page-title">추가 사항을 <br> 입력하세요!</h2>

        <div class="info-box">
          <div class="info-title">
            <i class="el-icon-info"></i>
            안내사항
          </div>
          <ul class="info-list">
            <li>게스트 모집 등록 후 매치 시작 1시간 전까지 취소가 가능합니다.</li>
            <li>무단 불참 시 패널티가 부여될 수 있습니다.</li>
            <li>매치 당일 구장에서 참가비를 직접 수금해주세요.</li>
          </ul>
        </div>

        <div class="form-group">
          <label>추가 안내사항 (선택)</label>
          <textarea
            v-model="additionalInfo"
            class="form-textarea"
            placeholder="게스트에게 전달할 추가 안내사항을 입력해주세요.&#10;(예: 주차 안내, 유니폼 색상, 집합 장소 등)"
            rows="5"
          ></textarea>
        </div>

        <button class="submit-button" @click="handleSubmit">등록하기</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getMyTeamMatches } from '@/api/match';
import {
  createGuestRecruitment,
  CreateGuestRecruitmentRequest,
} from '@/api/guest';

@Component
export default class GuestRecruit extends Vue {
  private step = 1

  private isLoadingMatches = false

  private upcomingMatches: any[] = []

  private selectedMatchUid = ''

  private selectedMatch: any = null

  // Step 2
  private genderType = 0

  private ageGroups = 0

  private positionType: 'FIELD' | 'GK' | 'ANY' = 'FIELD'

  private fee = 0

  private guaranteedMinutes = 60

  // Step 3
  private additionalInfo = ''

  private genderOptions = [
    { label: '남자', value: 1 },
    { label: '여자', value: 2 },
    { label: '남녀무관', value: 0 },
  ]

  private ageOptions = [
    { label: '10대', value: 10 },
    { label: '20대', value: 20 },
    { label: '30대', value: 30 },
    { label: '40대', value: 40 },
    { label: '50대 이상', value: 50 },
    { label: '나이 무관', value: 0 },
  ]

  private positionOptions = [
    { label: '필드', value: 'FIELD' as const },
    { label: '골키퍼', value: 'GK' as const },
  ]

  private feeOptions = [
    { label: '무료', value: 0 },
    { label: '5,000', value: 5000 },
    { label: '8,000', value: 8000 },
    { label: '10,000', value: 10000 },
  ]

  private guaranteedOptions = [
    { label: '30분', value: 30 },
    { label: '60분', value: 60 },
    { label: '80분', value: 80 },
    { label: '100분', value: 100 },
  ]

  get hasQueryMatch(): boolean {
    return !!(this.$route.query.matchUid);
  }

  get teamUid(): string {
    return (this.$route.query.teamUid as string) || '';
  }

  created(): void {
    // 매치 생성 후 바로 게스트 모집으로 넘어온 경우
    if (this.$route.query.matchUid) {
      this.selectedMatchUid = this.$route.query.matchUid as string;
      this.selectedMatch = {
        uid: this.selectedMatchUid,
        matchDate: this.$route.query.matchDate || '',
        matchTime: this.$route.query.matchTime || '',
        stadiumName: this.$route.query.stadium || '',
      };
      this.step = 2;
    } else {
      this.loadUpcomingMatches();
    }
  }

  private async loadUpcomingMatches(): Promise<void> {
    if (!this.teamUid) {
      this.upcomingMatches = [];
      return;
    }

    this.isLoadingMatches = true;
    try {
      const res = await getMyTeamMatches(this.teamUid, {
        page: 0,
        size: 50,
      });
      const matches = res.data?.content || res.data || [];
      // 7일 이내 매치만 필터링
      const now = new Date();
      const sevenDaysLater = new Date();
      sevenDaysLater.setDate(now.getDate() + 7);

      this.upcomingMatches = matches.filter((m: any) => {
        const matchDate = new Date(m.matchDate);
        return matchDate >= now && matchDate <= sevenDaysLater;
      });
    } catch (error) {
      console.error('Failed to load matches:', error);
      this.upcomingMatches = [];
    } finally {
      this.isLoadingMatches = false;
    }
  }

  private selectMatch(match: any): void {
    this.selectedMatchUid = match.uid;
    this.selectedMatch = match;
  }

  private goToStep2(): void {
    if (!this.selectedMatchUid && !this.hasQueryMatch) {
      this.$message.warning('매치를 선택해주세요.');
      return;
    }
    this.step = 2;
  }

  private goToStep3(): void {
    this.step = 3;
  }

  private goToMatchCreate(): void {
    this.$router.push({
      path: '/match-create',
      query: { teamUid: this.teamUid },
    });
  }

  private formatLabel(format: string): string {
    const map: Record<string, string> = {
      FOUR_VS_FOUR: '4 vs 4',
      FIVE_VS_FIVE: '5 vs 5',
      SIX_VS_SIX: '6 vs 6',
      SEVEN_VS_SEVEN: '7 vs 7',
    };
    return map[format] || format;
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const m = d.getMonth() + 1;
    const day = d.getDate();
    const dow = days[d.getDay()];
    return `${m}월 ${day}일 (${dow})`;
  }

  private async handleSubmit(): Promise<void> {
    try {
      const data: CreateGuestRecruitmentRequest = {
        teamUid: this.teamUid,
        matchUid: this.selectedMatchUid || undefined,
        matchDate: this.selectedMatch?.matchDate || '',
        matchTime: this.selectedMatch?.matchTime || '',
        stadiumName: this.selectedMatch?.stadiumName || '',
        genderType: this.genderType,
        ageGroups: this.ageGroups,
        positionType: this.positionType,
        maxGuests: 3,
        fee: this.fee,
        guaranteedMinutes: this.guaranteedMinutes,
        additionalInfo: this.additionalInfo || undefined,
      };

      await createGuestRecruitment(data);
      this.$message.success('게스트 모집이 등록되었습니다!');
      this.$router.go(-1);
    } catch (error) {
      console.error('Failed to create guest recruitment:', error);
      this.$message.error('게스트 모집 등록에 실패했습니다.');
    }
  }
}
</script>

<style scoped>
.guest-recruit-page {
  background: #fff;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.guest-recruit-page > .content {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  max-width: 600px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.step-content {
  padding: 74px 20px 40px;
  width: 100%;
}

.page-title {
  font-size: 22px;
  font-weight: 800;
  color: #333;
  margin: 0 0 8px;
  line-height: 1.4;
}

.page-desc {
  font-size: 13px;
  color: #999;
  margin: 0 0 24px;
}

/* Match List */
.match-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}

.match-card {
  position: relative;
  border: 1.5px solid #e0e0e0;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.match-card:active {
  transform: scale(0.98);
}

.match-card.selected {
  border-color: #061da1;
  background: #f5f7ff;
}

.match-card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.match-type-badge {
  display: inline-block;
  font-size: 11px;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 4px;
  background: #061da1;
  color: #fff;
}

.match-format {
  font-size: 12px;
  font-weight: 600;
  color: #666;
}

.match-card-body {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.match-date-info,
.match-location-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #555;
}

.match-date-info i,
.match-location-info i {
  color: #999;
  font-size: 14px;
}

.selected-check {
  position: absolute;
  top: 14px;
  right: 14px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #061da1;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
}

/* Loading & Empty */
.loading-area,
.empty-area {
  text-align: center;
  padding: 40px 0;
  color: #999;
  font-size: 14px;
}

.loading-area i {
  font-size: 24px;
  margin-bottom: 8px;
  display: block;
}

.link-button {
  margin-top: 12px;
  background: none;
  border: none;
  color: #061da1;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  text-decoration: underline;
}

/* Form Group */
.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

/* Option Buttons */
.option-buttons {
  display: flex;
  gap: 8px;
}

.option-buttons.wrap {
  flex-wrap: wrap;
}

.option-button {
  flex: 1;
  min-width: 0;
  padding: 12px 8px;
  border: 1.5px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  color: #555;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
  white-space: nowrap;
}

.option-buttons.wrap .option-button {
  flex: 0 0 calc(33.33% - 6px);
}

.option-button:active {
  transform: scale(0.97);
}

.option-button.active {
  background: #061da1;
  color: #fff;
  border-color: #061da1;
}

/* Info Box */
.info-box {
  background: #f8f9fc;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
}

.info-title {
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.info-title i {
  color: #061da1;
}

.info-list {
  margin: 0;
  padding: 0 0 0 18px;
  list-style-type: disc;
}

.info-list li {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 4px;
}

/* Textarea */
.form-textarea {
  width: 100%;
  padding: 14px;
  border: 1.5px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  resize: none;
  outline: none;
  box-sizing: border-box;
  font-family: inherit;
  line-height: 1.5;
}

.form-textarea:focus {
  border-color: #061da1;
}

.form-textarea::placeholder {
  color: #bbb;
}

/* Buttons */
.next-button,
.submit-button {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 8px;
  background: #061da1;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  margin-top: 12px;
  transition: background 0.2s ease;
}

.next-button:active,
.submit-button:active {
  background: #051580;
}

.next-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
