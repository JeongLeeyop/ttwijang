<template>
  <div class="team-profile-edit-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

    <!-- Content -->
    <div class="content">
      <div class="form-container">
        <!-- Team Header -->
        <div class="team-header-info">
          <div class="team-logo-small" @click="triggerLogoUpload">
            <img v-if="logoPreview" :src="logoPreview" alt="팀 로고" class="logo-image">
            <div v-else class="logo-placeholder">
              <i class="el-icon-camera"></i>
            </div>
            <div class="logo-edit-badge">
              <i class="el-icon-edit"></i>
            </div>
          </div>
          <div class="team-name-text">{{ teamName }}</div>
          <input
            ref="logoInput"
            type="file"
            accept="image/*"
            style="display:none"
            @change="handleLogoChange"
          >
        </div>

        <!-- 팀 이름 (읽기 전용) -->
        <div class="form-group">
          <label>팀 이름</label>
          <input
            :value="teamName"
            type="text"
            class="form-input disabled"
            disabled
          >
          <p class="form-hint">팀 이름은 변경할 수 없습니다.</p>
        </div>

        <!-- 팀 코드 (읽기 전용) -->
        <div class="form-group">
          <label>팀 코드</label>
          <input
            :value="teamCode"
            type="text"
            class="form-input disabled"
            disabled
          >
          <p class="form-hint">팀 코드는 변경할 수 없습니다.</p>
        </div>

        <!-- 후원 계좌 -->
        <div class="form-group">
          <label>팀 후원 계좌번호</label>
          <input
            v-model="bankName"
            type="text"
            placeholder="은행명 (예: 국민은행, 신한은행)"
            class="form-input"
          >
        </div>
        <div class="form-group">
          <input
            v-model="bankAccount"
            type="text"
            placeholder="계좌번호"
            class="form-input"
          >
        </div>

        <div class="section-divider"></div>

        <!-- 활동 요일 -->
        <div class="form-group">
          <h2 class="page-title">주로 언제<br>운동하나요?</h2>
          <label>활동 요일</label>
          <div class="option-buttons df">
            <button
              v-for="day in activeDayOptions"
              :key="day.value"
              class="option-button"
              :class="{ active: selectedDays.includes(day.value) }"
              @click="toggleOption(selectedDays, day.value)"
            >
              {{ day.label }}
            </button>
          </div>
        </div>

        <!-- 활동 시간대 -->
        <div class="form-group">
          <label>활동 시간대</label>
          <div class="option-buttons df">
            <button
              v-for="time in activeTimeOptions"
              :key="time.value"
              class="option-button"
              :class="{ active: selectedTimes.includes(time.value) }"
              @click="toggleOption(selectedTimes, time.value)"
            >
              <div>
                <b>{{ time.value }}</b>
              </div>
              <div>
                {{ time.label }}
              </div>
            </button>
          </div>
        </div>

        <!-- 성별 -->
        <div class="form-group">
          <h2 class="page-title">어떤 사람들이<br>모여있나요?</h2>
          <label>성별</label>
          <div class="option-buttons">
            <button
              v-for="gender in genderOptions"
              :key="gender.value"
              class="option-button"
              :class="{ active: selectedGender === gender.value }"
              @click="selectedGender = gender.value"
            >
              {{ gender.label }}
            </button>
          </div>
        </div>

        <!-- 나이대 -->
        <div class="form-group">
          <label>나이대</label>
          <div class="option-buttons">
            <button
              v-for="age in ageRangeOptions"
              :key="age.value"
              class="option-button"
              :class="{ active: selectedAges.includes(age.value) }"
              @click="toggleOption(selectedAges, age.value)"
            >
              {{ age.label }}
            </button>
          </div>
        </div>

        <div class="section-divider"></div>

        <!-- 홈 구장 -->
        <div class="form-group">
          <h2 class="page-title">주로 사용하는<br>구장을 알려주세요!</h2>
          <label>구장 이름</label>
          <input
            v-model="homeStadium"
            type="text"
            class="form-input"
            placeholder="ex) 진주종합경기장 풋살장"
          >
        </div>

        <!-- 도시 / 지역 -->
        <div class="location-row">
          <div class="form-group location-item">
            <label>도시</label>
            <el-select
              :popper-append-to-body="false"
              v-model="selectedCity"
              placeholder="선택"
              class="location-select"
              @change="onCityChange"
            >
              <el-option
                v-for="city in cities"
                :key="city.value"
                :label="city.label"
                :value="city.value"
              />
            </el-select>
          </div>
          <div class="form-group location-item">
            <label>지역</label>
            <el-select
              :popper-append-to-body="false"
              v-model="selectedDistrict"
              placeholder="선택"
              class="location-select"
              :disabled="!selectedCity"
            >
              <el-option
                v-for="district in districts"
                :key="district.value"
                :label="district.label"
                :value="district.value"
              />
            </el-select>
          </div>
        </div>

        <!-- 저장 버튼 -->
        <button
          class="submit-button"
          :disabled="isSaving"
          @click="handleSave"
        >
          {{ isSaving ? '저장 중...' : '저장하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import {
  getTeamDetail, getMyTeams, updateTeam, uploadTeamPhoto,
} from '@/api/team';

@Component
export default class TeamProfileEdit extends Vue {
  private teamUid = ''

  private teamName = ''

  private teamCode = ''

  private logoPreview = ''

  private logoFileUid = ''

  private newLogoFile: File | null = null

  private bankName = ''

  private bankAccount = ''

  private selectedDays: string[] = []

  private selectedTimes: string[] = []

  private selectedGender = ''

  private selectedAges: string[] = []

  private homeStadium = ''

  private selectedCity = ''

  private selectedDistrict = ''

  private isSaving = false

  private activeDayOptions = [
    { label: '일', value: 'sunday' },
    { label: '월', value: 'monday' },
    { label: '화', value: 'tuesday' },
    { label: '수', value: 'wednesday' },
    { label: '목', value: 'thursday' },
    { label: '금', value: 'friday' },
    { label: '토', value: 'saturday' },
  ]

  private activeTimeOptions = [
    { label: '6시-10시', value: '아침' },
    { label: '10시-18시', value: '낮' },
    { label: '18시-24시', value: '저녁' },
    { label: '24시-6시', value: '심야' },
  ]

  private genderOptions = [
    { label: '남성', value: 'male' },
    { label: '여성', value: 'female' },
    { label: '남녀 모두', value: 'mixed' },
  ]

  private ageRangeOptions = [
    { label: '10대', value: '10s' },
    { label: '20대', value: '20s' },
    { label: '30대', value: '30s' },
    { label: '40대', value: '40s' },
    { label: '50대', value: '50s' },
    { label: '60대', value: '60s' },
  ]

  private cities = [
    { label: '서울', value: '서울' },
    { label: '경기', value: '경기' },
    { label: '인천', value: '인천' },
    { label: '부산', value: '부산' },
    { label: '대구', value: '대구' },
    { label: '대전', value: '대전' },
    { label: '광주', value: '광주' },
    { label: '울산', value: '울산' },
    { label: '세종', value: '세종' },
    { label: '경남', value: '경남' },
    { label: '경북', value: '경북' },
    { label: '전남', value: '전남' },
    { label: '전북', value: '전북' },
    { label: '충남', value: '충남' },
    { label: '충북', value: '충북' },
    { label: '강원', value: '강원' },
    { label: '제주', value: '제주' },
  ]

  private districtsByCity: Record<string, Array<{ label: string, value: string }>> = {
    경남: [
      { label: '진주시', value: '진주시' },
      { label: '창원시', value: '창원시' },
      { label: '김해시', value: '김해시' },
      { label: '양산시', value: '양산시' },
      { label: '거제시', value: '거제시' },
      { label: '통영시', value: '통영시' },
      { label: '사천시', value: '사천시' },
      { label: '밀양시', value: '밀양시' },
    ],
    서울: [
      { label: '강남구', value: '강남구' },
      { label: '서초구', value: '서초구' },
      { label: '송파구', value: '송파구' },
      { label: '강동구', value: '강동구' },
      { label: '마포구', value: '마포구' },
      { label: '영등포구', value: '영등포구' },
    ],
    경기: [
      { label: '수원시', value: '수원시' },
      { label: '성남시', value: '성남시' },
      { label: '고양시', value: '고양시' },
      { label: '용인시', value: '용인시' },
      { label: '부천시', value: '부천시' },
      { label: '안산시', value: '안산시' },
    ],
    부산: [
      { label: '해운대구', value: '해운대구' },
      { label: '부산진구', value: '부산진구' },
      { label: '동래구', value: '동래구' },
      { label: '남구', value: '남구' },
      { label: '사하구', value: '사하구' },
    ],
    대구: [
      { label: '수성구', value: '수성구' },
      { label: '달서구', value: '달서구' },
      { label: '북구', value: '북구' },
    ],
    인천: [
      { label: '남동구', value: '남동구' },
      { label: '부평구', value: '부평구' },
      { label: '서구', value: '서구' },
    ],
  }

  // Day bitmask mapping
  private dayBitmask: Record<string, number> = {
    sunday: 1,
    monday: 2,
    tuesday: 4,
    wednesday: 8,
    thursday: 16,
    friday: 32,
    saturday: 64,
  }

  // Time bitmask mapping
  private timeBitmask: Record<string, number> = {
    아침: 1,
    낮: 2,
    저녁: 4,
    심야: 8,
  }

  // Age bitmask mapping
  private ageBitmask: Record<string, number> = {
    '10s': 1,
    '20s': 2,
    '30s': 4,
    '40s': 8,
    '50s': 16,
    '60s': 32,
  }

  get districts() {
    return this.selectedCity
      ? this.districtsByCity[this.selectedCity] || []
      : [];
  }

  async created(): Promise<void> {
    this.teamUid = (this.$route.query.teamUid as string) || '';
    if (!this.teamUid) {
      try {
        const res = await getMyTeams();
        const team = Array.isArray(res.data) ? res.data[0] : res.data;
        if (team && team.uid) {
          this.teamUid = team.uid;
        }
      } catch (e) {
        console.warn('내 팀 조회 실패:', e);
      }
    }
    if (this.teamUid) {
      await this.loadTeamData();
    }
  }

  private async loadTeamData(): Promise<void> {
    try {
      const res = await getTeamDetail(this.teamUid);
      const team = res.data || {};

      this.teamName = team.name || '';
      this.teamCode = team.teamCode || '';
      this.logoPreview = team.logoUrl || '';
      this.logoFileUid = team.logoFileUid || '';
      this.bankName = team.bankName || '';
      this.bankAccount = team.bankAccount || '';
      this.homeStadium = team.homeStadium || '';
      this.selectedCity = team.regionSido || '';
      this.selectedDistrict = team.regionSigungu || '';

      // Decode bitmask → selected arrays
      if (team.activeDays) {
        this.selectedDays = this.decodeBitmask(team.activeDays, this.dayBitmask);
      }
      if (team.activeTimeSlots) {
        this.selectedTimes = this.decodeBitmask(team.activeTimeSlots, this.timeBitmask);
      }
      if (team.genderType !== undefined && team.genderType !== null) {
        const genderMap: Record<number, string> = {
          0: 'male',
          1: 'female',
          2: 'mixed',
        };
        this.selectedGender = genderMap[team.genderType] || '';
      }
      if (team.ageGroups) {
        this.selectedAges = this.decodeBitmask(team.ageGroups, this.ageBitmask);
      }
    } catch (error) {
      console.error('팀 정보 로드 실패:', error);
      this.$message.error('팀 정보를 불러오는데 실패했습니다.');
    }
  }

  private decodeBitmask(value: number, mapping: Record<string, number>): string[] {
    const result: string[] = [];
    Object.entries(mapping).forEach(([key, bit]) => {
      // eslint-disable-next-line no-bitwise
      if (value & bit) {
        result.push(key);
      }
    });
    return result;
  }

  private encodeBitmask(selected: string[], mapping: Record<string, number>): number {
    let result = 0;
    selected.forEach((key) => {
      if (mapping[key]) {
        // eslint-disable-next-line no-bitwise
        result |= mapping[key];
      }
    });
    return result;
  }

  private toggleOption(array: string[], value: string): void {
    const index = array.indexOf(value);
    if (index > -1) {
      array.splice(index, 1);
    } else {
      array.push(value);
    }
  }

  private triggerLogoUpload(): void {
    (this.$refs.logoInput as HTMLInputElement).click();
  }

  private handleLogoChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    this.newLogoFile = input.files[0];
    const reader = new FileReader();
    reader.onload = (e) => {
      this.logoPreview = (e.target?.result as string) || '';
    };
    reader.readAsDataURL(this.newLogoFile);
  }

  private onCityChange(): void {
    this.selectedDistrict = '';
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private async handleSave(): Promise<void> {
    this.isSaving = true;
    try {
      // 새 로고가 있으면 업로드
      if (this.newLogoFile) {
        const uploadRes = await uploadTeamPhoto(this.newLogoFile);
        if (uploadRes.data && uploadRes.data.uid) {
          this.logoFileUid = uploadRes.data.uid;
        }
      }

      const genderMap: Record<string, number> = {
        male: 0,
        female: 1,
        mixed: 2,
      };

      await updateTeam({
        uid: this.teamUid,
        logoFileUid: this.logoFileUid || undefined,
        bankName: this.bankName || undefined,
        bankAccount: this.bankAccount || undefined,
        activeDays: this.encodeBitmask(this.selectedDays, this.dayBitmask) || undefined,
        activeTimeSlots: this.encodeBitmask(this.selectedTimes, this.timeBitmask) || undefined,
        genderType: this.selectedGender ? genderMap[this.selectedGender] : undefined,
        ageGroups: this.encodeBitmask(this.selectedAges, this.ageBitmask) || undefined,
        homeStadium: this.homeStadium || undefined,
        regionSido: this.selectedCity || undefined,
        regionSigungu: this.selectedDistrict || undefined,
      });

      this.$message.success('팀 프로필이 수정되었습니다.');
      this.$router.go(-1);
    } catch (error) {
      console.error('팀 프로필 수정 실패:', error);
      this.$message.error('팀 프로필 수정에 실패했습니다.');
    } finally {
      this.isSaving = false;
    }
  }
}
</script>

<style scoped>
.team-profile-edit-page {
  min-height: 100vh;
  background: #f5f6fa;
}

.header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.back-button {
  background: none;
  border: none;
  font-size: 22px;
  color: #333;
  cursor: pointer;
  padding: 4px;
}

.content {
  padding: 16px;
}

.form-container {
  overflow-y: scroll;
  max-width: 500px;
  margin: 0 auto;
}

/* Team Header */
.team-header-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0 24px;
}

.team-logo-small {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: visible;
  cursor: pointer;
  margin-bottom: 10px;
}

.logo-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #eee;
}

.logo-placeholder {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid #eee;
}

.logo-placeholder i {
  font-size: 28px;
  color: #999;
}

.logo-edit-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: #061da1;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  border: 2px solid #fff;
}

.team-name-text {
  font-size: 18px;
  font-weight: 800;
  color: #222;
  letter-spacing: -0.3px;
}

/* Form */
.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #555;
  margin-bottom: 6px;
}

.form-input {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  border-color: #061da1;
}

.form-input.disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}

.form-hint {
  font-size: 12px;
  color: #999;
  margin: 4px 0 0;
}

.section-divider {
  height: 1px;
  background: #e0e0e0;
  margin: 24px 0;
}

.page-title {
  font-size: 20px;
  font-weight: 800;
  color: #222;
  line-height: 1.4;
  margin: 0 0 16px;
  letter-spacing: -0.3px;
}

/* Option Buttons */
.option-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}

.option-buttons.df {
  display: flex;
  flex-wrap: nowrap;
  gap: 6px;
}

.option-button {
  padding: 10px 16px;
  border: 1.5px solid #ddd;
  border-radius: 8px;
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  color: #555;
  cursor: pointer;
  transition: all 0.2s;
  text-align: center;
  min-width: 44px;
}

.option-button.active {
  background: #061da1;
  color: #fff;
  border-color: #061da1;
}

.option-button:active {
  transform: scale(0.97);
}

/* Location Row */
.location-row {
  display: flex;
  gap: 12px;
}

.location-item {
  flex: 1;
}

.location-select {
  width: 100%;
}

/* Submit Button */
.submit-button {
  width: 100%;
  padding: 14px;
  background: #061da1;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  margin-top: 24px;
  margin-bottom: 40px;
  transition: opacity 0.2s;
}

.submit-button:active {
  opacity: 0.85;
}

.submit-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* Element UI Override */
::v-deep .el-select {
  width: 100%;
}

::v-deep .el-input__inner {
  border-radius: 8px;
}
</style>
