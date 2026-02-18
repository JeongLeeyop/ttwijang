<template>
  <div class="recruitment-create">
    <!-- Step Indicator -->
    <div class="step-indicator">
      <div
        v-for="s in 3"
        :key="s"
        class="step-dot"
        :class="{ active: step === s, completed: step > s }"
      ></div>
    </div>

    <!-- Step 1: 팀 정보 -->
    <div v-if="step === 1" class="step-content">
      <h2 class="step-title">팀 정보를 확인하세요!</h2>

      <div class="form-section">
        <label class="form-label">팀 특징</label>
        <div class="tag-group">
          <button
            v-for="tag in featureTagOptions"
            :key="tag"
            class="tag-button"
            :class="{ selected: selectedFeatureTags.includes(tag) }"
            @click="toggleFeatureTag(tag)"
          >
            {{ tag }}
          </button>
        </div>
      </div>

      <div class="form-section">
        <label class="form-label">활동 요일</label>
        <div class="tag-group">
          <button
            v-for="(day, index) in dayOptions"
            :key="day.label"
            class="tag-button day-button"
            :class="{ selected: isDaySelected(index) }"
            @click="toggleDay(index)"
          >
            {{ day.label }}
          </button>
        </div>
      </div>

      <div class="form-section">
        <label class="form-label">활동 시간</label>
        <div class="tag-group">
          <button
            v-for="(slot, index) in timeSlotOptions"
            :key="slot.label"
            class="tag-button time-button"
            :class="{ selected: isTimeSlotSelected(index) }"
            @click="toggleTimeSlot(index)"
          >
            <span class="time-label">{{ slot.label }}</span>
            <span class="time-range">{{ slot.range }}</span>
          </button>
        </div>
      </div>

      <div class="form-section">
        <div class="region-row">
          <div class="region-field">
            <label class="form-label">활동 도시</label>
            <el-select
              v-model="regionSido"
              placeholder="시/도"
              size="small"
              @change="onSidoChange"
            >
              <el-option
                v-for="sido in sidoList"
                :key="sido.code"
                :label="sido.name"
                :value="sido.name"
              ></el-option>
            </el-select>
          </div>
          <div class="region-field">
            <label class="form-label">지역</label>
            <el-select
              v-model="regionSigungu"
              placeholder="시/군/구"
              size="small"
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
      </div>

      <div class="form-section">
        <label class="form-label">월 회비</label>
        <div class="fee-input-row">
          <el-input
            v-model="monthlyFeeInput"
            placeholder="금액 입력"
            size="small"
            class="fee-input"
          ></el-input>
        </div>
        <div class="tag-group">
          <button
            v-for="fee in feeOptions"
            :key="fee.value"
            class="tag-button"
            :class="{ selected: monthlyFee === fee.value }"
            @click="selectFee(fee.value)"
          >
            {{ fee.label }}
          </button>
        </div>
      </div>

      <button class="next-button" @click="goToStep(2)">다음</button>
    </div>

    <!-- Step 2: 모집 대상 -->
    <div v-if="step === 2" class="step-content">
      <h2 class="step-title">어떤 회원을 영입할까요?</h2>

      <div class="form-section">
        <label class="form-label">성별</label>
        <div class="tag-group">
          <button
            v-for="g in genderOptions"
            :key="g.value"
            class="tag-button"
            :class="{ selected: genderType === g.value }"
            @click="genderType = g.value"
          >
            {{ g.label }}
          </button>
        </div>
      </div>

      <div class="form-section">
        <label class="form-label">나이</label>
        <div class="tag-group">
          <button
            v-for="age in ageOptions"
            :key="age.value"
            class="tag-button"
            :class="{ selected: isAgeSelected(age.value) }"
            @click="toggleAge(age.value)"
          >
            {{ age.label }}
          </button>
        </div>
      </div>

      <button class="next-button" @click="goToStep(3)">다음</button>
    </div>

    <!-- Step 3: 추가 사항 -->
    <div v-if="step === 3" class="step-content">
      <h2 class="step-title">추가 사항을 입력하세요!</h2>

      <p class="step-notice">
        가입 신청이 들어오면 알림으로 알려드려요.<br>
        개인 정보 보호를 위해 연락처를 공개하지 마세요.
      </p>

      <div class="form-section">
        <label class="form-label">팀 단체 사진 추가하기</label>
        <div
          class="photo-upload-area"
          @click="triggerPhotoUpload"
        >
          <img
            v-if="teamPhotoPreview"
            :src="teamPhotoPreview"
            alt="팀 사진"
            class="photo-preview"
          >
          <div v-else class="photo-placeholder">
            <i class="el-icon-camera"></i>
            <span>사진 추가</span>
          </div>
        </div>
        <input
          ref="photoInput"
          type="file"
          accept="image/*"
          style="display:none"
          @change="handlePhotoUpload"
        >
      </div>

      <div class="form-section">
        <label class="form-label">추가사항</label>
        <el-input
          v-model="recruitmentDescription"
          type="textarea"
          :rows="5"
          placeholder="여기를 누르고 팀을 소개하세요.&#10;그 외 추가 사항을 입력해주세요."
        ></el-input>
      </div>

      <p class="privacy-notice">
        팀 가입을 신청하면 신청자의 연락처를 확인할 수 있어요.
        제공된 연락처는 '회원 모집'을 위한 목적으로만 활용할 수
        있으며, 이용자의 과실로 인한 개인정보 유출 시 책임은
        이용자에게 있습니다.
      </p>

      <button
        class="next-button submit-button"
        :disabled="isSubmitting"
        @click="submitRecruitment"
      >
        등록하기
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';
import { Message } from 'element-ui';
import {
  getMyTeams, getTeamDetail, saveRecruitment, uploadTeamPhoto,
} from '@/api/team';
import { getSidoList, getSigunguList } from '@/api/region';

@Component
export default class RecruitmentCreate extends Vue {
  private step = 1

  private teamUid = ''

  private teamName = ''

  // Step 1
  private featureTagOptions = [
    '함께 실력을 키워요',
    '뛰장 리그에 나가요',
    '5 vs 5 풋살 정식룰로 해요',
    '자체전 위주로 경기해요',
    '대회를 준비해요',
    '전문 코치님이 있어요',
    '팀 친선경기 위주로 해요',
  ]

  private selectedFeatureTags: string[] = []

  private dayOptions = [
    { label: '일', bit: 1 },
    { label: '월', bit: 2 },
    { label: '화', bit: 4 },
    { label: '수', bit: 8 },
    { label: '목', bit: 16 },
    { label: '금', bit: 32 },
    { label: '토', bit: 64 },
  ]

  private activeDays = 0

  private timeSlotOptions = [
    { label: '아침', range: '6~10시', bit: 1 },
    { label: '낮', range: '10~18시', bit: 2 },
    { label: '저녁', range: '18~24시', bit: 4 },
    { label: '심야', range: '24~6시', bit: 8 },
  ]

  private activeTimeSlots = 0

  private regionSido = ''

  private regionSigungu = ''

  private sidoList: { code: string, name: string }[] = []

  private sigunguList: { code: string, name: string }[] = []

  private monthlyFee: number | null = null

  private monthlyFeeInput = ''

  private feeOptions = [
    { label: '무료', value: 0 },
    { label: '5,000', value: 5000 },
    { label: '8,000', value: 8000 },
    { label: '10,000', value: 10000 },
    { label: '20,000', value: 20000 },
    { label: '30,000', value: 30000 },
    { label: '50,000', value: 50000 },
    { label: '직접입력', value: -1 },
  ]

  // Step 2
  private genderOptions = [
    { label: '남자', value: 0 },
    { label: '여자', value: 1 },
    { label: '남녀무관', value: 2 },
  ]

  private genderType: number | null = null

  private ageOptions = [
    { label: '10대', value: 1 },
    { label: '20대', value: 2 },
    { label: '30대', value: 4 },
    { label: '40대', value: 8 },
    { label: '50대 이상', value: 16 },
    { label: '나이 무관', value: 31 },
  ]

  private ageGroups = 0

  // Step 3
  private teamPhotoFileUid = ''

  private teamPhotoPreview = ''

  private recruitmentDescription = ''

  private isSubmitting = false

  async created() {
    await this.loadMyTeam();
    await this.loadSidoList();
  }

  private async loadMyTeam(): Promise<void> {
    try {
      // query param으로 teamUid가 넘어온 경우 바로 사용
      const queryTeamUid = this.$route.query.teamUid as string;

      if (queryTeamUid) {
        this.teamUid = queryTeamUid;
        const detailRes = await getTeamDetail(queryTeamUid);
        const detail = detailRes.data || detailRes;
        this.teamName = detail.name;
        this.loadTeamFields(detail);
      } else {
        // 내 팀 조회 (UID 파악)
        const myRes = await getMyTeams();
        const myData = myRes.data;
        let team = null;
        if (Array.isArray(myData)) {
          team = myData.length > 0 ? myData[0] : null;
        } else {
          team = myData;
        }

        if (!team || !team.uid) {
          Message.error('소속 팀이 없습니다. 먼저 팀을 생성해주세요.');
          this.$router.push('/create-team');
          return;
        }

        this.teamUid = team.uid;
        this.teamName = team.name;

        const detailRes = await getTeamDetail(team.uid);
        const detail = detailRes.data || detailRes;
        this.loadTeamFields(detail);
      }

      if (this.regionSido) {
        await this.loadSigunguBySido();
      }
    } catch (error) {
      Message.error('팀 정보를 불러오지 못했습니다.');
      this.$router.push('/');
    }
  }

  private loadTeamFields(detail: any): void {
    if (detail.featureTags) {
      try {
        this.selectedFeatureTags = JSON.parse(detail.featureTags);
      } catch (e) {
        this.selectedFeatureTags = [];
      }
    }
    if (detail.activeDays) this.activeDays = detail.activeDays;
    if (detail.activeTimeSlots) this.activeTimeSlots = detail.activeTimeSlots;
    if (detail.regionSido) this.regionSido = detail.regionSido;
    if (detail.regionSigungu) this.regionSigungu = detail.regionSigungu;
    if (detail.monthlyFee != null) {
      this.monthlyFee = detail.monthlyFee;
      this.monthlyFeeInput = String(detail.monthlyFee);
    }
    if (detail.genderType != null) this.genderType = detail.genderType;
    if (detail.ageGroups) this.ageGroups = detail.ageGroups;
    if (detail.recruitmentDescription) {
      this.recruitmentDescription = detail.recruitmentDescription;
    }
  }

  private async loadSidoList(): Promise<void> {
    try {
      const res = await getSidoList();
      this.sidoList = res.data || [];
    } catch (e) {
      console.warn('시/도 목록 로드 실패', e);
    }
  }

  private async onSidoChange(): Promise<void> {
    this.regionSigungu = '';
    this.sigunguList = [];
    await this.loadSigunguBySido();
  }

  private async loadSigunguBySido(): Promise<void> {
    const sido = this.sidoList.find((s) => s.name === this.regionSido);
    if (sido) {
      try {
        const res = await getSigunguList(sido.code);
        this.sigunguList = res.data || [];
      } catch (e) {
        console.warn('시/군/구 목록 로드 실패', e);
      }
    }
  }

  private toggleFeatureTag(tag: string): void {
    const idx = this.selectedFeatureTags.indexOf(tag);
    if (idx >= 0) {
      this.selectedFeatureTags.splice(idx, 1);
    } else {
      this.selectedFeatureTags.push(tag);
    }
  }

  /* eslint-disable no-bitwise */
  private isDaySelected(index: number): boolean {
    return (this.activeDays & this.dayOptions[index].bit) !== 0;
  }

  private toggleDay(index: number): void {
    this.activeDays ^= this.dayOptions[index].bit;
  }

  private isTimeSlotSelected(index: number): boolean {
    return (this.activeTimeSlots & this.timeSlotOptions[index].bit) !== 0;
  }

  private toggleTimeSlot(index: number): void {
    this.activeTimeSlots ^= this.timeSlotOptions[index].bit;
  }

  private isAgeSelected(value: number): boolean {
    if (value === 31) {
      return this.ageGroups === 31;
    }
    return (this.ageGroups & value) !== 0;
  }

  private toggleAge(value: number): void {
    if (value === 31) {
      this.ageGroups = this.ageGroups === 31 ? 0 : 31;
    } else {
      this.ageGroups ^= value;
    }
  }
  /* eslint-enable no-bitwise */

  private selectFee(value: number): void {
    if (value === -1) {
      this.monthlyFee = null;
      this.monthlyFeeInput = '';
      return;
    }
    this.monthlyFee = value;
    this.monthlyFeeInput = String(value);
  }

  private goToStep(target: number): void {
    if (target === 2 && this.step === 1) {
      if (this.selectedFeatureTags.length === 0) {
        Message.warning('팀 특징을 하나 이상 선택해주세요.');
        return;
      }
      // 직접입력인 경우 입력값 반영
      if (this.monthlyFee === null && this.monthlyFeeInput) {
        const parsed = parseInt(this.monthlyFeeInput.replace(/,/g, ''), 10);
        if (!Number.isNaN(parsed)) {
          this.monthlyFee = parsed;
        }
      }
    }
    this.step = target;
    window.scrollTo(0, 0);
  }

  private triggerPhotoUpload(): void {
    (this.$refs.photoInput as HTMLInputElement).click();
  }

  private async handlePhotoUpload(e: Event): Promise<void> {
    const target = e.target as HTMLInputElement;
    if (!target.files || target.files.length === 0) return;
    const file = target.files[0];

    // 미리보기
    const reader = new FileReader();
    reader.onload = (ev) => {
      this.teamPhotoPreview = ev.target?.result as string;
    };
    reader.readAsDataURL(file);

    // 업로드
    try {
      const res = await uploadTeamPhoto(file);
      if (res.data && res.data.uid) {
        this.teamPhotoFileUid = res.data.uid;
      }
    } catch (error) {
      Message.error('사진 업로드에 실패했습니다.');
    }
  }

  private async submitRecruitment(): Promise<void> {
    if (this.isSubmitting) return;
    this.isSubmitting = true;

    try {
      // 직접입력 반영
      if (this.monthlyFee === null && this.monthlyFeeInput) {
        const parsed = parseInt(this.monthlyFeeInput.replace(/,/g, ''), 10);
        if (!Number.isNaN(parsed)) {
          this.monthlyFee = parsed;
        }
      }

      await saveRecruitment(this.teamUid, {
        featureTags: JSON.stringify(this.selectedFeatureTags),
        activeDays: this.activeDays,
        activeTimeSlots: this.activeTimeSlots,
        regionSido: this.regionSido,
        regionSigungu: this.regionSigungu,
        monthlyFee: this.monthlyFee ?? 0,
        genderType: this.genderType ?? 2,
        ageGroups: this.ageGroups,
        teamPhotoFileUid: this.teamPhotoFileUid || undefined,
        recruitmentDescription: this.recruitmentDescription,
      });

      Message.success('회원 모집이 등록되었습니다!');
      this.$router.push(`/team/${this.teamUid}`);
    } catch (error) {
      Message.error('등록에 실패했습니다.');
    } finally {
      this.isSubmitting = false;
    }
  }
}
</script>

<style scoped>
.recruitment-create {
  padding: 20px;
  max-width: 480px;
  margin: 0 auto;
  margin-top: 20px;
  min-height: 100vh;
  background: #fff;
}

.step-indicator {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 24px;
}

.step-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #e0e0e0;
  transition: background 0.3s;
}

.step-dot.active {
  background: #061da1;
  width: 24px;
  border-radius: 4px;
}

.step-dot.completed {
  background: #061da1;
}

.step-title {
  font-size: 22px;
  font-weight: 700;
  color: #000;
  margin-bottom: 24px;
}

.step-notice {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 24px;
}

.form-section {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 14px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: #fff;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.tag-button.selected {
  background: #061da1;
  color: #fff;
  border-color: #061da1;
}

.tag-button:active {
  transform: scale(0.96);
}

.day-button {
  width: 40px;
  height: 40px;
  padding: 0;
  border-radius: 50%;
  font-size: 14px;
  font-weight: 600;
}

.time-button {
  flex-direction: column;
  padding: 10px 16px;
  border-radius: 12px;
  gap: 2px;
}

.time-label {
  font-weight: 600;
  font-size: 14px;
}

.time-range {
  font-size: 11px;
  color: #999;
}

.time-button.selected .time-range {
  color: rgba(255, 255, 255, 0.7);
}

.region-row {
  display: flex;
  gap: 12px;
}

.region-field {
  flex: 1;
}

.region-field .el-select {
  width: 100%;
}

.fee-input-row {
  margin-bottom: 10px;
}

.fee-input {
  width: 100%;
}

.next-button {
  display: block;
  width: 100%;
  padding: 14px;
  background: #061da1;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 20px;
  transition: background 0.2s;
}

.next-button:active {
  background: #0415a0;
}

.next-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.photo-upload-area {
  width: 100%;
  height: 200px;
  border: 2px dashed #ddd;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;
  background: #f9f9f9;
  transition: border-color 0.2s;
}

.photo-upload-area:active {
  border-color: #061da1;
}

.photo-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #999;
  font-size: 14px;
}

.photo-placeholder i {
  font-size: 32px;
}

.photo-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.privacy-notice {
  font-size: 12px;
  color: #999;
  line-height: 1.6;
  margin-top: 20px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
}

.submit-button {
  margin-top: 16px;
}
</style>
