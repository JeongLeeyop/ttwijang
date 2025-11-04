<template>
  <div class="team-info-page">
    <!-- Content -->
    <div class="content">
      <!-- Form Container -->
      <div class="form-container">
        <!-- Team Header with Logo and Name -->
        <div class="team-header-info">
          <div class="team-logo-small">
            <img v-if="teamLogo" :src="teamLogo" :alt="teamName" class="logo-image">
            <div v-else class="logo-placeholder">
              <i class="el-icon-picture"></i>
            </div>
          </div>
          <div class="team-name-text">{{ teamName }}</div>
        </div>

        <!-- Active Days Section -->
        <div class="form-group">
          <h2 class="page-title">주로 언제 <br> 운동하나요?</h2>
          <label>활동 요일</label>
          <div class="option-buttons df">
            <button
              v-for="day in activeDays"
              :key="day.value"
              class="option-button"
              :class="{ active: selectedDays.includes(day.value) }"
              @click="toggleOption(selectedDays, day.value)"
            >
              {{ day.label }}
            </button>
          </div>
        </div>

        <!-- Active Time Section -->
        <div class="form-group">
          <label>활동 시간대</label>
          <div class="option-buttons df">
            <button
              v-for="time in activeTimes"
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

        <!-- Gender Section -->
        <div class="form-group">
          <h2 class="page-title">어떤 사람들이 <br> 모여있나요?</h2>
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

        <!-- Age Range Section -->
        <div class="form-group">
          <label>나이대</label>
          <div class="option-buttons">
            <button
              v-for="age in ageRanges"
              :key="age.value"
              class="option-button"
              :class="{ active: selectedAges.includes(age.value) }"
              @click="toggleOption(selectedAges, age.value)"
            >
              {{ age.label }}
            </button>
          </div>
        </div>

        <!-- Submit Button -->
        <button class="submit-button" @click="handleNext">다 음</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class TeamInfoPage extends Vue {
  private teamName = ''

  private teamLogo = ''

  private selectedDays: string[] = []

  private selectedTimes: string[] = []

  private selectedGender = ''

  private selectedAges: string[] = []

  private activeDays = [
    { label: '일', value: 'sunday' },
    { label: '월', value: 'monday' },
    { label: '화', value: 'tuesday' },
    { label: '수', value: 'wednesday' },
    { label: '목', value: 'thursday' },
    { label: '금', value: 'friday' },
    { label: '토', value: 'saturday' },
  ]

  private activeTimes = [
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

  private ageRanges = [
    { label: '10대', value: '10s' },
    { label: '20대', value: '20s' },
    { label: '30대', value: '30s' },
    { label: '40대', value: '40s' },
    { label: '50대', value: '50s' },
    { label: '60대', value: '60s' },
  ]

  mounted() {
    this.loadTeamData();
  }

  private loadTeamData(): void {
    // 라우터 state에서 팀 정보 가져오기
    const state = (this as any).$router.currentRoute.params;
    if (state && state.teamData) {
      const teamData = JSON.parse(state.teamData);
      this.teamName = teamData.name || '';
      this.teamLogo = teamData.logo || '';
    } else {
      // 세션 스토리지에서 팀 정보 가져오기
      const storedTeamData = sessionStorage.getItem('teamFormData');
      if (storedTeamData) {
        const teamData = JSON.parse(storedTeamData);
        this.teamName = teamData.name || '';
        this.teamLogo = teamData.logo || '';
      }
    }
  }

  private toggleOption(array: string[], value: string): void {
    const index = array.indexOf(value);
    if (index > -1) {
      array.splice(index, 1);
    } else {
      array.push(value);
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private handleNext(): void {
    if (this.selectedDays.length === 0) {
      this.$message.warning('활동 요일을 선택해주세요.');
      return;
    }
    if (this.selectedTimes.length === 0) {
      this.$message.warning('활동 시간대를 선택해주세요.');
      return;
    }
    if (!this.selectedGender) {
      this.$message.warning('성별을 선택해주세요.');
      return;
    }
    if (this.selectedAges.length === 0) {
      this.$message.warning('나이대를 선택해주세요.');
      return;
    }

    const teamInfo = {
      name: this.teamName,
      logo: this.teamLogo,
      activeDays: this.selectedDays,
      activeTimes: this.selectedTimes,
      gender: this.selectedGender,
      ageRanges: this.selectedAges,
    };

    console.log('Team Info:', teamInfo);
    sessionStorage.setItem('teamInfoData', JSON.stringify(teamInfo));
    this.$message.success('팀 정보가 저장되었습니다!');
    this.$router.push('/team-location');
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
