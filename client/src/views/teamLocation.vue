<template>
  <div class="team-location-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

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

        <!-- Main Title -->
        <h2 class="page-title">주로 사용하는<br>구장을 알려주세요!</h2>

        <!-- Region Input Section -->
        <div class="form-group">
          <label>구장 이름</label>
          <div class="search-input-wrapper" @click="openSearchDialog">
            <input
              v-model="stadiumName"
              type="text"
              class="search-input"
              placeholder="ex) 구장 찾기"
              readonly
            >
            <i class="el-icon-search search-icon"></i>
          </div>
        </div>

        <!-- City and District Selection (Same Row) -->
        <div class="location-row">
          <!-- City Selection -->
          <div class="form-group location-item">
            <label>도시</label>
            <el-select
              :popper-append-to-body="false"
              v-model="selectedCity"
              placeholder="경남"
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

          <!-- District Selection -->
          <div class="form-group location-item">
            <label>지역</label>
            <el-select
              :popper-append-to-body="false"
              v-model="selectedDistrict"
              placeholder="진주시"
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

        <!-- Submit Button -->
        <button class="submit-button" @click="handleNext">다 음</button>
      </div>
    </div>

    <!-- Stadium Search Dialog -->
    <el-dialog
      title="구장 검색"
      :visible.sync="searchDialogVisible"
      width="400px"
      :close-on-click-modal="true"
      custom-class="stadium-search-dialog"
    >
      <div class="dialog-search-wrapper">
        <el-input
          v-model="searchKeyword"
          placeholder="구장 이름을 입력하세요"
          prefix-icon="el-icon-search"
          @input="handleSearch"
          clearable
        />
      </div>

      <div class="search-results">
        <div
          v-if="filteredStadiums.length === 0"
          class="no-results"
        >
          <i class="el-icon-search"></i>
          <p>검색 결과가 없습니다.</p>
        </div>
        <div
          v-for="stadium in filteredStadiums"
          :key="stadium.id"
          class="stadium-item"
          @click="selectStadium(stadium)"
        >
          <div class="stadium-name">{{ stadium.name }}</div>
          <div class="stadium-location">{{ stadium.city }} {{ stadium.district }}</div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="searchDialogVisible = false">닫기</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class TeamLocationPage extends Vue {
  private teamName = ''

  private teamLogo = ''

  private stadiumName = ''

  private selectedCity = ''

  private selectedDistrict = ''

  private searchDialogVisible = false

  private searchKeyword = ''

  private stadiums = [
    {
        id: 1,
        name: '진주종합경기장 풋살장',
        city: '경남',
        district: '진주시',
    },
    {
        id: 2,
        name: '창원축구센터',
        city: '경남',
        district: '창원시',
    },
    {
        id: 3,
        name: '김해실내체육관 풋살장',
        city: '경남',
        district: '김해시',
    },
    {
        id: 4,
        name: '양산스포츠파크',
        city: '경남',
        district: '양산시',
    },
    {
        id: 5,
        name: '잠실종합운동장 풋살장',
        city: '서울',
        district: '송파구',
    },
    {
        id: 6,
        name: '강남풋살클럽',
        city: '서울',
        district: '강남구',
    },
    {
        id: 7,
        name: '마포풋살파크',
        city: '서울',
        district: '마포구',
    },
    {
        id: 8,
        name: '수원월드컵경기장 풋살장',
        city: '경기',
        district: '수원시',
    },
    {
        id: 9,
        name: '성남종합운동장',
        city: '경기',
        district: '성남시',
    },
    {
        id: 10,
        name: '고양실내체육관',
        city: '경기',
        district: '고양시',
    },
  ]

  private cities = [
    { label: '서울', value: 'seoul' },
    { label: '경기', value: 'gyeonggi' },
    { label: '인천', value: 'incheon' },
    { label: '부산', value: 'busan' },
    { label: '대구', value: 'daegu' },
    { label: '대전', value: 'daejeon' },
    { label: '광주', value: 'gwangju' },
    { label: '울산', value: 'ulsan' },
    { label: '세종', value: 'sejong' },
    { label: '경남', value: 'gyeongnam' },
    { label: '경북', value: 'gyeongbuk' },
    { label: '전남', value: 'jeonnam' },
    { label: '전북', value: 'jeonbuk' },
    { label: '충남', value: 'chungnam' },
    { label: '충북', value: 'chungbuk' },
    { label: '강원', value: 'gangwon' },
    { label: '제주', value: 'jeju' },
  ]

  private districtsByCity: { [key: string]: Array<{ label: string, value: string }> } = {
    gyeongnam: [
      { label: '진주시', value: 'jinju' },
      { label: '창원시', value: 'changwon' },
      { label: '김해시', value: 'gimhae' },
      { label: '양산시', value: 'yangsan' },
      { label: '거제시', value: 'geoje' },
      { label: '통영시', value: 'tongyeong' },
      { label: '사천시', value: 'sacheon' },
      { label: '밀양시', value: 'miryang' },
    ],
    seoul: [
      { label: '강남구', value: 'gangnam' },
      { label: '서초구', value: 'seocho' },
      { label: '송파구', value: 'songpa' },
      { label: '강동구', value: 'gangdong' },
      { label: '마포구', value: 'mapo' },
      { label: '영등포구', value: 'yeongdeungpo' },
    ],
    gyeonggi: [
      { label: '수원시', value: 'suwon' },
      { label: '성남시', value: 'seongnam' },
      { label: '고양시', value: 'goyang' },
      { label: '용인시', value: 'yongin' },
      { label: '부천시', value: 'bucheon' },
      { label: '안산시', value: 'ansan' },
    ],
  }

  get districts() {
    return this.selectedCity ? this.districtsByCity[this.selectedCity] || [] : [];
  }

  get filteredStadiums() {
    if (!this.searchKeyword) {
      return this.stadiums;
    }
    const keyword = this.searchKeyword.toLowerCase();
    return this.stadiums.filter((stadium) => stadium.name.toLowerCase().includes(keyword) || stadium.city.toLowerCase().includes(keyword) || stadium.district.toLowerCase().includes(keyword));
  }

  mounted() {
    this.loadTeamData();
  }

  private openSearchDialog(): void {
    this.searchDialogVisible = true;
    this.searchKeyword = '';
  }

  private handleSearch(): void {
    // 검색은 computed property에서 자동으로 처리됨
  }

  private selectStadium(stadium: any): void {
    this.stadiumName = stadium.name;
    this.searchDialogVisible = false;
    this.$message.success(`${stadium.name}이(가) 선택되었습니다.`);
  }

  private loadTeamData(): void {
    // 세션 스토리지에서 팀 정보 가져오기
    const storedTeamData = sessionStorage.getItem('teamFormData');
    if (storedTeamData) {
      const teamData = JSON.parse(storedTeamData);
      this.teamName = teamData.name || '';
      this.teamLogo = teamData.logo || '';
    }

    // 이전에 저장된 팀 정보 데이터 가져오기
    const storedTeamInfo = sessionStorage.getItem('teamInfoData');
    if (storedTeamInfo) {
      const teamInfo = JSON.parse(storedTeamInfo);
      // 필요시 이전 데이터 활용
    }
  }

  private onCityChange(): void {
    // 도시가 변경되면 지역 초기화
    this.selectedDistrict = '';
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private handleNext(): void {
    if (!this.stadiumName) {
      this.$message.warning('구장 이름을 입력해주세요.');
      return;
    }
    if (!this.selectedCity) {
      this.$message.warning('도시를 선택해주세요.');
      return;
    }
    if (!this.selectedDistrict) {
      this.$message.warning('지역을 선택해주세요.');
      return;
    }

    const locationInfo = {
      stadiumName: this.stadiumName,
      city: this.selectedCity,
      district: this.selectedDistrict,
    };

    console.log('Location Info:', locationInfo);
    sessionStorage.setItem('teamLocationData', JSON.stringify(locationInfo));
    this.$message.success('팀 활동 지역이 저장되었습니다!');
    this.$router.push('/team-complete');
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
