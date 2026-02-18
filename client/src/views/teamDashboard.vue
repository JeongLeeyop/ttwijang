<template>
  <div class="team-dashboard-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

    <!-- Content -->
    <div class="content">
      <div class="form-container">
        <!-- League Dashboard -->
        <div class="dashboard-section">
          <div class="section-badge">
            <!-- <span class="badge-text">25-26</span> -->
          </div>
          <h2 class="section-title">리그 대시보드</h2>

          <div class="dashboard-list">
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">리그 등급</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">리그 전적</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">리그 승률</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">리그 성적</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">매너 점수</span>
              <span class="item-value">-</span>
            </div>
          </div>
        </div>

        <!-- Match Dashboard -->
        <div class="dashboard-section">
          <h2 class="section-title">매치 대시보드</h2>

          <div class="dashboard-list">
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">전체 경기수</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">매치 전적</span>
              <span class="item-value">-</span>
            </div>
            <div class="dashboard-item" @click="showPreparing">
              <span class="item-label">매치 승률</span>
              <span class="item-value">-</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class TeamDashboard extends Vue {
  private goBack(): void {
    this.$router.go(-1);
  }

  private showPreparing(): void {
    this.$message.info('준비 중인 기능입니다.');
  }
}
</script>

<style scoped>
.team-dashboard-page {
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
  max-width: 500px;
  margin: 0 auto;
}

.dashboard-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.section-badge {
  display: inline-block;
  margin-bottom: 8px;
}

.badge-text {
  display: inline-block;
  padding: 4px 12px;
  background: #061da1;
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 700;
}

.section-title {
  font-size: 18px;
  font-weight: 800;
  color: #222;
  margin: 0 0 16px;
  letter-spacing: -0.3px;
}

.dashboard-list {
  display: flex;
  flex-direction: column;
}

.dashboard-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.15s;
}

.dashboard-item:last-child {
  border-bottom: none;
}

.dashboard-item:active {
  background: #f9f9f9;
}

.item-label {
  font-size: 14px;
  color: #555;
  padding-left: 8px;
}

.item-label::before {
  content: '-';
  margin-right: 6px;
  color: #999;
}

.item-value {
  font-size: 14px;
  color: #999;
  font-weight: 500;
}
</style>
