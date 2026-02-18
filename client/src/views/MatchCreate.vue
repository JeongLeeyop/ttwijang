<template>
  <div class="match-create-page">
    <div class="content">
      <div class="form-container">
        <h2 class="page-title">어떤 매치 일정을 <br> 만들까요?</h2>

        <!-- 날짜 및 시간 -->
        <div class="form-group">
          <label>날짜 및 시간</label>
          <div class="date-display">
            {{ displayDateTime }}
          </div>
          <!-- 캘린더 -->
          <div class="calendar-wrapper">
            <div class="calendar-header">
              <button class="cal-nav" @click="prevMonth">
                <i class="el-icon-arrow-left"></i>
              </button>
              <span class="cal-title">{{ calYear }}.{{ String(calMonth + 1).padStart(2, '0') }}</span>
              <button class="cal-nav" @click="nextMonth">
                <i class="el-icon-arrow-right"></i>
              </button>
            </div>
            <div class="calendar-grid">
              <div class="cal-day-header">일</div>
              <div class="cal-day-header">월</div>
              <div class="cal-day-header">화</div>
              <div class="cal-day-header">수</div>
              <div class="cal-day-header">목</div>
              <div class="cal-day-header">금</div>
              <div class="cal-day-header">토</div>
              <div
                v-for="(day, idx) in calendarDays"
                :key="idx"
                class="cal-day-cell"
                :class="{
                  'other-month': !day.current,
                  'today': day.isToday,
                  'selected': day.isSelected,
                  'disabled': day.isPast,
                }"
                @click="selectDate(day)"
              >
                {{ day.day }}
              </div>
            </div>
          </div>

          <!-- 시/분 선택 -->
          <div class="time-selector">
            <div class="time-select-group">
              <select v-model="selectedHour" class="time-select">
                <option v-for="h in hours" :key="h" :value="h">{{ h }}</option>
              </select>
              <span class="time-label">시</span>
            </div>
            <div class="time-select-group">
              <select v-model="selectedMinute" class="time-select">
                <option v-for="m in minutes" :key="m" :value="m">{{ m }}</option>
              </select>
              <span class="time-label">분</span>
            </div>
          </div>
        </div>

        <!-- 매치 시간 -->
        <div class="form-group">
          <label>매치 시간</label>
          <div class="option-buttons">
            <button
              v-for="d in durationOptions"
              :key="d"
              class="option-button"
              :class="{ active: matchDuration === d }"
              @click="matchDuration = d"
            >
              {{ d }}시간
            </button>
          </div>
        </div>

        <!-- 장소 -->
        <div class="form-group">
          <label>장소</label>
          <div class="input-with-icon">
            <input
              v-model="stadium"
              type="text"
              class="form-input"
              placeholder="진주풋살장"
            >
            <!-- <i class="el-icon-search input-icon"></i> -->
          </div>
        </div>

        <!-- 매치 타입 -->
        <div class="form-group">
          <label>매치 타입</label>
          <div class="option-buttons">
            <button
              class="option-button"
              :class="{ active: matchType === 'FRIENDLY' }"
              @click="matchType = 'FRIENDLY'"
            >
              친선경기
            </button>
            <button
              class="option-button"
              :class="{ active: matchType === 'FREE' }"
              @click="matchType = 'FREE'"
            >
              자체경기
            </button>
          </div>
        </div>

        <!-- 매치 방식 -->
        <div class="form-group">
          <label>매치 방식</label>
          <div class="option-buttons">
            <button
              v-for="f in formatOptions"
              :key="f.value"
              class="option-button"
              :class="{ active: matchFormat === f.value }"
              @click="matchFormat = f.value"
            >
              {{ f.label }}
            </button>
          </div>
        </div>

        <!-- 등록하기 버튼 -->
        <button class="submit-button" @click="handleSubmit">등록하기</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { createMatch } from '@/api/match';

@Component
export default class MatchCreate extends Vue {
  private calYear = new Date().getFullYear()

  private calMonth = new Date().getMonth()

  private selectedDate = ''

  private selectedHour = '20'

  private selectedMinute = '00'

  private matchDuration = 2

  private stadium = ''

  private matchType = 'FRIENDLY'

  private matchFormat = 'FIVE_VS_FIVE'

  private durationOptions = [1, 2, 3, 4]

  private formatOptions = [
    { label: '4 vs 4', value: 'FOUR_VS_FOUR' },
    { label: '5 vs 5', value: 'FIVE_VS_FIVE' },
    { label: '6 vs 6', value: 'SIX_VS_SIX' },
    { label: '7 vs 7', value: 'SEVEN_VS_SEVEN' },
  ]

  get teamUid(): string {
    return (this.$route.query.teamUid as string) || '';
  }

  get hours(): string[] {
    const h: string[] = [];
    for (let i = 0; i < 24; i += 1) {
      h.push(String(i).padStart(2, '0'));
    }
    return h;
  }

  get minutes(): string[] {
    return ['00', '10', '20', '30', '40', '50'];
  }

  get displayDateTime(): string {
    if (!this.selectedDate) return '날짜를 선택해주세요';
    const d = new Date(this.selectedDate);
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}년 ${m}월 ${day}일 ${this.selectedHour}시 ${this.selectedMinute}분`;
  }

  get calendarDays(): any[] {
    const days: any[] = [];
    const firstDay = new Date(this.calYear, this.calMonth, 1);
    const lastDay = new Date(this.calYear, this.calMonth + 1, 0);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // 이전 달
    const startDow = firstDay.getDay();
    const prevLast = new Date(this.calYear, this.calMonth, 0).getDate();
    for (let i = startDow - 1; i >= 0; i -= 1) {
      days.push({
        day: prevLast - i,
        current: false,
        isPast: true,
        isToday: false,
        isSelected: false,
        dateStr: '',
      });
    }

    // 현재 달
    for (let d = 1; d <= lastDay.getDate(); d += 1) {
      const dt = new Date(this.calYear, this.calMonth, d);
      const dateStr = `${this.calYear}-${String(this.calMonth + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      days.push({
        day: d,
        current: true,
        isPast: dt < today,
        isToday: dt.getTime() === today.getTime(),
        isSelected: this.selectedDate === dateStr,
        dateStr,
      });
    }

    // 다음 달
    const remaining = 42 - days.length;
    for (let i = 1; i <= remaining; i += 1) {
      days.push({
        day: i,
        current: false,
        isPast: false,
        isToday: false,
        isSelected: false,
        dateStr: '',
      });
    }

    return days;
  }

  private prevMonth(): void {
    if (this.calMonth === 0) {
      this.calMonth = 11;
      this.calYear -= 1;
    } else {
      this.calMonth -= 1;
    }
  }

  private nextMonth(): void {
    if (this.calMonth === 11) {
      this.calMonth = 0;
      this.calYear += 1;
    } else {
      this.calMonth += 1;
    }
  }

  private selectDate(day: any): void {
    if (!day.current || day.isPast) return;
    this.selectedDate = day.dateStr;
  }

  private async handleSubmit(): Promise<void> {
    if (!this.selectedDate) {
      this.$message.warning('날짜를 선택해주세요.');
      return;
    }
    if (!this.stadium) {
      this.$message.warning('장소를 입력해주세요.');
      return;
    }

    try {
      const matchData: any = {
        hostTeamUid: this.teamUid,
        matchDate: this.selectedDate,
        matchTime: `${this.selectedHour}:${this.selectedMinute}`,
        matchDuration: this.matchDuration,
        stadiumName: this.stadium,
        matchType: this.matchType,
        matchFormat: this.matchFormat,
      };

      const response = await createMatch(matchData);
      this.$message.success('매치 일정이 등록되었습니다!');

      // 게스트 모집 여부 묻기
      this.$confirm('게스트 모집도 함께 하시겠습니까?', '게스트 모집', {
        confirmButtonText: '게스트 모집하기',
        cancelButtonText: '나중에',
        type: 'info',
      }).then(() => {
        this.$router.push({
          path: '/guest-recruit',
          query: {
            teamUid: this.teamUid,
            matchUid: response.data?.uid || '',
            matchDate: this.selectedDate,
            matchTime: `${this.selectedHour}:${this.selectedMinute}`,
            stadium: this.stadium,
          },
        });
      }).catch(() => {
        this.$router.go(-1);
      });
    } catch (error) {
      console.error('Failed to create match:', error);
      this.$message.error('매치 일정 등록에 실패했습니다.');
    }
  }
}
</script>

<style scoped>
.match-create-page {
  background: #fff;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.match-create-page > .content {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  max-width: 600px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.form-container {
  padding: 74px 20px 40px;
}

.page-title {
  font-size: 22px;
  font-weight: 800;
  color: #333;
  margin: 0 0 28px;
  line-height: 1.4;
}

.form-group {
  margin-bottom: 28px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

.date-display {
  font-size: 15px;
  font-weight: 600;
  color: #061da1;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 16px;
}

/* Calendar */
.calendar-wrapper {
  border: 1px solid #eee;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fafafa;
}

.cal-nav {
  background: none;
  border: none;
  font-size: 16px;
  color: #333;
  cursor: pointer;
  padding: 4px 8px;
}

.cal-title {
  font-size: 15px;
  font-weight: 700;
  color: #333;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0;
  padding: 8px;
}

.cal-day-header {
  text-align: center;
  font-size: 12px;
  font-weight: 600;
  color: #999;
  padding: 8px 0;
}

.cal-day-cell {
  text-align: center;
  font-size: 13px;
  padding: 10px 0;
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.15s ease;
  color: #333;
}

.cal-day-cell.other-month {
  color: #ddd;
  pointer-events: none;
}

.cal-day-cell.disabled {
  color: #ccc;
  pointer-events: none;
}

.cal-day-cell.today {
  color: #061da1;
  font-weight: 700;
}

.cal-day-cell.selected {
  background: #061da1;
  color: #fff;
  font-weight: 700;
}

.cal-day-cell:active:not(.disabled):not(.other-month) {
  background: #e8ecff;
}

/* Time Selector */
.time-selector {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.time-select-group {
  display: flex;
  align-items: center;
  gap: 6px;
}

.time-select {
  width: 100px;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  background: #fff;
  appearance: auto;
  -webkit-appearance: auto;
  text-align: center;
}

.time-label {
  font-size: 14px;
  font-weight: 600;
  color: #666;
}

/* Option Buttons */
.option-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.option-button {
  flex: 1;
  min-width: calc(25% - 6px);
  padding: 12px 10px;
  border: 1.5px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  color: #555;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
}

.option-button:active {
  transform: scale(0.97);
}

.option-button.active {
  background: #061da1;
  color: #fff;
  border-color: #061da1;
}

/* Input */
.input-with-icon {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 12px 40px 12px 14px;
  border: 1.5px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  outline: none;
  box-sizing: border-box;
}

.form-input:focus {
  border-color: #061da1;
}

.input-icon {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  font-size: 16px;
}

/* Submit */
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

.submit-button:active {
  background: #051580;
}
</style>
