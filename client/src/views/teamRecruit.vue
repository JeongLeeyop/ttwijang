<template>
  <div class="team-recruit-page">
    <div class="recruit-header">
      <h2 class="recruit-title">회원 모집 중</h2>
      <p class="recruit-subtitle">새로운 팀원을 찾고 있는 팀이에요</p>
    </div>

    <!-- Filter Section -->
    <div class="filter-section">
      <!-- Row 1: 지역 -->
      <div class="filter-row">
        <span class="filter-row-label">지역</span>
        <div class="filter-row-content">
          <el-select
            v-model="filterRegionSido"
            placeholder="시/도"
            size="mini"
            clearable
            class="filter-select"
            @change="onSidoFilterChange"
          >
            <el-option
              v-for="sido in sidoList"
              :key="sido.code"
              :label="sido.name"
              :value="sido.name"
            ></el-option>
          </el-select>
          <el-select
            v-model="filterRegionSigungu"
            placeholder="시/군/구"
            size="mini"
            clearable
            class="filter-select"
          >
            <el-option
              v-for="sigungu in sigunguList"
              :key="sigungu.code"
              :label="sigungu.name"
              :value="sigungu.name"
            ></el-option>
          </el-select>
        </div>
      </div>
      <!-- Row 2: 성별 -->
      <div class="filter-row">
        <span class="filter-row-label">성별</span>
        <div class="filter-row-content">
          <button
            v-for="opt in genderFilterOptions"
            :key="opt.value"
            class="filter-chip"
            :class="{ active: filterGender === opt.value }"
            @click="toggleGender(opt.value)"
          >
            {{ opt.label }}
          </button>
        </div>
      </div>
      <!-- Row 3: 요일 -->
      <div class="filter-row">
        <span class="filter-row-label">요일</span>
        <div class="filter-row-content">
          <button
            v-for="day in dayFilterOptions"
            :key="day.label"
            class="filter-chip"
            :class="{ active: isFilterDaySelected(day.bit) }"
            @click="toggleFilterDay(day.bit)"
          >
            {{ day.label }}
          </button>
        </div>
      </div>
      <!-- Row 4: 시간대 -->
      <div class="filter-row">
        <span class="filter-row-label">시간대</span>
        <div class="filter-row-content">
          <button
            v-for="slot in timeFilterOptions"
            :key="slot.label"
            class="filter-chip"
            :class="{ active: isFilterTimeSelected(slot.bit) }"
            @click="toggleFilterTime(slot.bit)"
          >
            {{ slot.label }}
          </button>
        </div>
      </div>
    </div>

    <!-- Team List -->
    <div class="recruit-list">
      <div v-if="isLoading && teams.length === 0" class="loading-wrap">
        <i class="el-icon-loading"></i>
        <span>로딩 중...</span>
      </div>
      <div
        v-else-if="teams.length === 0"
        class="empty-wrap"
      >
        <i class="el-icon-info"></i>
        <span>모집 중인 팀이 없습니다.</span>
      </div>
      <div
        v-for="team in teams"
        :key="team.uid"
        class="recruit-card"
        @click="goToDetail(team.uid)"
      >
        <div class="card-top">
          <div class="team-logo-wrap">
            <img
              v-if="team.logoUrl"
              :src="team.logoUrl"
              alt="팀 로고"
              class="team-logo"
            >
            <div v-else class="team-logo-placeholder">
              <i class="el-icon-football"></i>
            </div>
          </div>
          <div class="team-info">
            <div class="team-name-row">
              <span class="team-name">{{ team.name }}</span>
              <span class="manner-score">매너 {{ team.mannerScore || '-' }}</span>
            </div>
            <div class="team-meta">
              <span v-if="team.region" class="meta-item">
                <i class="el-icon-location-outline"></i> {{ team.region }}
              </span>
              <span class="meta-item">
                <i class="el-icon-user"></i> {{ team.memberCount || 0 }}명
              </span>
            </div>
          </div>
        </div>
        <div class="card-tags">
          <span
            v-for="tag in parseFeatureTags(team.featureTags)"
            :key="tag"
            class="recruit-tag"
          >
            {{ tag }}
          </span>
        </div>
        <div class="card-bottom">
          <span v-if="formatGender(team.genderType)" class="info-chip">
            {{ formatGender(team.genderType) }}
          </span>
          <span v-if="formatAge(team.ageGroups)" class="info-chip">
            {{ formatAge(team.ageGroups) }}
          </span>
          <span v-if="formatDays(team.activeDays)" class="info-chip">
            {{ formatDays(team.activeDays) }}
          </span>
          <span v-if="team.monthlyFee != null" class="info-chip">
            월 {{ team.monthlyFee === 0 ? '무료' : formatNumber(team.monthlyFee) + '원' }}
          </span>
        </div>
      </div>

      <!-- Load More -->
      <div
        v-if="hasMore"
        class="load-more"
        @click="loadMore"
      >
        <span v-if="isLoading"><i class="el-icon-loading"></i></span>
        <span v-else>더 보기</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getRecruitingTeams } from '@/api/team';
import { getSidoList, getSigunguList } from '@/api/region';

@Component
export default class TeamRecruit extends Vue {
  private isLoading = false

  private teams: any[] = []

  private page = 0

  private size = 10

  private totalPages = 0

  private filterRegionSido = ''

  private filterRegionSigungu = ''

  private filterGender: number | null = null

  private filterDays = 0

  private filterTimeSlots = 0

  private sidoList: { code: string, name: string }[] = []

  private sigunguList: { code: string, name: string }[] = []

  private genderFilterOptions = [
    { label: '남자', value: 0 },
    { label: '여자', value: 1 },
    { label: '남녀무관', value: 2 },
  ]

  private dayFilterOptions = [
    { label: '일', bit: 1 },
    { label: '월', bit: 2 },
    { label: '화', bit: 4 },
    { label: '수', bit: 8 },
    { label: '목', bit: 16 },
    { label: '금', bit: 32 },
    { label: '토', bit: 64 },
  ]

  private timeFilterOptions = [
    { label: '아침', bit: 1 },
    { label: '낮', bit: 2 },
    { label: '저녁', bit: 4 },
    { label: '심야', bit: 8 },
  ]

  get hasMore(): boolean {
    return this.page + 1 < this.totalPages;
  }

  async created() {
    await this.loadSidoList();
    await this.fetchTeams();
  }

  private async loadSidoList(): Promise<void> {
    try {
      const res = await getSidoList();
      this.sidoList = res.data || [];
    } catch (e) {
      // silent
    }
  }

  private async onSidoFilterChange(): Promise<void> {
    this.filterRegionSigungu = '';
    this.sigunguList = [];
    const sido = this.sidoList.find((s) => s.name === this.filterRegionSido);
    if (sido) {
      try {
        const res = await getSigunguList(sido.code);
        this.sigunguList = res.data || [];
      } catch (e) {
        // silent
      }
    }
    this.resetAndFetch();
  }

  private resetAndFetch(): void {
    this.page = 0;
    this.teams = [];
    this.fetchTeams();
  }

  private async fetchTeams(): Promise<void> {
    if (this.isLoading) return;
    this.isLoading = true;
    try {
      const params: any = {
        page: this.page,
        size: this.size,
      };
      if (this.filterRegionSido) params.regionSido = this.filterRegionSido;
      if (this.filterRegionSigungu) params.regionSigungu = this.filterRegionSigungu;
      if (this.filterGender != null) params.genderType = this.filterGender;
      if (this.filterDays) params.activeDays = this.filterDays;
      if (this.filterTimeSlots) params.activeTimeSlots = this.filterTimeSlots;

      const res = await getRecruitingTeams(params);
      const data = res.data || res;
      if (data.content) {
        this.teams = [...this.teams, ...data.content];
        this.totalPages = data.totalPages || 0;
      }
    } catch (e) {
      // silent
    } finally {
      this.isLoading = false;
    }
  }

  private loadMore(): void {
    this.page += 1;
    this.fetchTeams();
  }

  /* eslint-disable no-bitwise */
  private isFilterDaySelected(bit: number): boolean {
    return (this.filterDays & bit) !== 0;
  }

  private toggleFilterDay(bit: number): void {
    this.filterDays ^= bit;
    this.resetAndFetch();
  }

  private isFilterTimeSelected(bit: number): boolean {
    return (this.filterTimeSlots & bit) !== 0;
  }

  private toggleFilterTime(bit: number): void {
    this.filterTimeSlots ^= bit;
    this.resetAndFetch();
  }

  private toggleGender(val: number): void {
    this.filterGender = this.filterGender === val ? null : val;
    this.resetAndFetch();
  }

  private formatDays(val: number): string {
    if (!val) return '';
    const labels = ['일', '월', '화', '수', '목', '금', '토'];
    const days: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) days.push(label);
    });
    return days.join(', ');
  }

  private formatAge(val: number): string {
    if (!val) return '';
    if (val === 31) return '나이무관';
    const labels = ['10대', '20대', '30대', '40대', '50대+'];
    const ages: string[] = [];
    labels.forEach((label, i) => {
      if (val & (1 << i)) ages.push(label);
    });
    return ages.join(', ');
  }
  /* eslint-enable no-bitwise */

  private formatGender(val: number | null): string {
    if (val === 0) return '남자';
    if (val === 1) return '여자';
    if (val === 2) return '남녀무관';
    return '';
  }

  private formatNumber(n: number): string {
    return n.toLocaleString();
  }

  private parseFeatureTags(tags: string): string[] {
    if (!tags) return [];
    try {
      return JSON.parse(tags);
    } catch (e) {
      return [];
    }
  }

  private goToDetail(teamUid: string): void {
    this.$router.push(`/team-recruit-detail/${teamUid}`);
  }
}
</script>

<style scoped>
.team-recruit-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 80px;
  margin-top:60px;
}

.recruit-header {
  background: #061da1;
  padding: 24px 20px 20px;
  color: #fff;
}

.recruit-title {
  font-size: 22px;
  font-weight: 700;
  margin: 0 0 4px;
}

.recruit-subtitle {
  font-size: 13px;
  opacity: 0.8;
  margin: 0;
}

.filter-section {
  background: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  border-bottom: 1px solid #f5f5f5;
}

.filter-row:last-child {
  border-bottom: none;
}

.filter-row-label {
  font-size: 12px;
  font-weight: 600;
  color: #888;
  flex-shrink: 0;
  min-width: 34px;
}

.filter-row-content {
  display: flex;
  gap: 6px;
  flex-wrap: nowrap;
  align-items: center;
}

.filter-select {
  width: 110px;
  flex-shrink: 0;
}

.filter-chip {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 16px;
  background: #fff;
  font-size: 12px;
  color: #555;
  white-space: nowrap;
  cursor: pointer;
  flex-shrink: 0;
  transition: all 0.2s;
}

.filter-chip.active {
  background: #061da1;
  color: #fff;
  border-color: #061da1;
}

.recruit-list {
  padding: 12px 16px;
}

.loading-wrap,
.empty-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 60px 0;
  color: #999;
  font-size: 14px;
}

.loading-wrap i,
.empty-wrap i {
  font-size: 28px;
}

.recruit-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: transform 0.15s;
}

.recruit-card:active {
  transform: scale(0.98);
}

.card-top {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
}

.team-logo-wrap {
  width: 48px;
  height: 48px;
  flex-shrink: 0;
}

.team-logo {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.team-logo-placeholder {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #adb5bd;
  font-size: 20px;
}

.team-info {
  flex: 1;
  min-width: 0;
}

.team-name-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 4px;
}

.team-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.manner-score {
  font-size: 12px;
  color: #061da1;
  font-weight: 600;
}

.team-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #888;
}

.meta-item i {
  margin-right: 2px;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 10px;
}

.recruit-tag {
  display: inline-block;
  padding: 4px 10px;
  background: #f0f2ff;
  color: #061da1;
  border-radius: 12px;
  font-size: 11px;
  white-space: nowrap;
}

.card-bottom {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.info-chip {
  display: inline-block;
  padding: 3px 8px;
  background: #f5f5f5;
  color: #666;
  border-radius: 10px;
  font-size: 11px;
  white-space: nowrap;
}

.load-more {
  text-align: center;
  padding: 16px;
  color: #061da1;
  font-size: 14px;
  cursor: pointer;
}
</style>
