<template>
  <div class="notice-page">
    <!-- 헤더 -->
    <div class="page-header">
      <button class="btn-back" @click="$router.go(-1)">
        <i class="el-icon-arrow-left"></i>
      </button>
      <span class="page-title">공지사항</span>
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="state-center">
      <div class="spinner"></div>
    </div>

    <!-- 빈 상태 -->
    <div v-else-if="notices.length === 0" class="state-center">
      <div class="empty-icon">
        <i class="el-icon-bell"></i>
      </div>
      <p class="empty-text">등록된 공지사항이 없습니다</p>
    </div>

    <!-- 목록 -->
    <div v-else class="notice-list">
      <div
        v-for="(item, idx) in notices"
        :key="item.uid"
        class="notice-card"
        :style="{ animationDelay: idx * 40 + 'ms' }"
        @click="openDetail(item)"
      >
        <div class="card-inner">
          <div class="card-num">{{ String(notices.length - idx).padStart(2, '0') }}</div>
          <div class="card-body">
            <p class="card-title">{{ item.title }}</p>
            <p class="card-date">{{ formatDate(item.createdDate) }}</p>
          </div>
          <i class="el-icon-arrow-right card-arrow"></i>
        </div>
      </div>
    </div>

    <!-- 상세 오버레이 -->
    <transition name="slide-up">
      <div v-if="detailVisible" class="detail-overlay" @click.self="closeDetail">
        <div class="detail-sheet">
          <div class="detail-handle"></div>
          <div class="detail-header">
            <h2 class="detail-title">{{ selected && selected.title }}</h2>
            <p class="detail-date">{{ selected && formatDate(selected.createdDate) }}</p>
          </div>
          <div class="detail-divider"></div>
          <div class="detail-content">{{ selected && selected.content }}</div>
          <button class="btn-close-detail" @click="closeDetail">닫기</button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getNoticeList, Notice } from '@/api/notice';

@Component
export default class NoticePage extends Vue {
  private isLoading = false;

  private notices: Notice[] = [];

  private selected: Notice | null = null;

  private detailVisible = false;

  created(): void {
    this.fetchNotices();
  }

  private async fetchNotices(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getNoticeList();
      this.notices = res.data || [];
    } finally {
      this.isLoading = false;
    }
  }

  private openDetail(item: Notice): void {
    this.selected = item;
    this.detailVisible = true;
  }

  private closeDetail(): void {
    this.detailVisible = false;
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    return dateStr.substring(0, 10);
  }
}
</script>

<style scoped>
.notice-page {
  min-height: 100vh;
  background: #f4f6fb;
}

/* 헤더 */
.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #edf0f7;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.btn-back {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f4f6fb;
  border: none;
  color: #3d4a6b;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-back:active {
  background: #e8ecf5;
}

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a2340;
  letter-spacing: -0.3px;
  flex: 1;
  text-align: left;
}

/* 공통 상태 */
.state-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 16px;
}

.spinner {
  width: 36px;
  height: 36px;
  border: 3px solid #e8ecf5;
  border-top-color: #3949ab;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #edf0f7;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #aab2cc;
}

.empty-text {
  font-size: 14px;
  color: #aab2cc;
  margin: 0;
}

/* 목록 */
.notice-list {
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notice-card {
  background: #fff;
  border: 1px solid #edf0f7;
  border-radius: 16px;
  cursor: pointer;
  transition: transform 0.18s, box-shadow 0.18s, border-color 0.18s;
  box-shadow: 0 2px 8px rgba(57, 73, 171, 0.04);
  animation: fadeUp 0.35s ease both;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.notice-card:active {
  transform: scale(0.98);
  border-color: #c5cae9;
  box-shadow: 0 4px 16px rgba(57, 73, 171, 0.1);
}

.card-inner {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 18px;
}

.card-num {
  font-size: 22px;
  font-weight: 800;
  color: #e8ecf5;
  font-variant-numeric: tabular-nums;
  min-width: 32px;
  line-height: 1;
}

.card-body {
  flex: 1;
  min-width: 0;
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a2340;
  margin: 0 0 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  letter-spacing: -0.2px;
}

.card-date {
  font-size: 12px;
  color: #aab2cc;
  margin: 0;
}

.card-arrow {
  font-size: 13px;
  color: #c5cae9;
  flex-shrink: 0;
}

/* 상세 오버레이 */
.detail-overlay {
  position: fixed;
  inset: 0;
  background: rgba(26, 35, 64, 0.35);
  z-index: 100;
  display: flex;
  align-items: flex-end;
}

.detail-sheet {
  width: 100%;
  max-height: 80vh;
  background: #fff;
  border-radius: 24px 24px 0 0;
  padding: 12px 24px 40px;
  overflow-y: auto;
  box-shadow: 0 -8px 32px rgba(57, 73, 171, 0.1);
}

.detail-handle {
  width: 36px;
  height: 4px;
  background: #e8ecf5;
  border-radius: 2px;
  margin: 0 auto 20px;
}

.detail-header {
  margin-bottom: 16px;
}

.detail-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a2340;
  line-height: 1.4;
  margin: 0 0 8px;
  letter-spacing: -0.3px;
}

.detail-date {
  font-size: 12px;
  color: #aab2cc;
  margin: 0;
}

.detail-divider {
  height: 1px;
  background: #edf0f7;
  margin-bottom: 20px;
}

.detail-content {
  font-size: 14px;
  color: #4a5473;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
  margin-bottom: 28px;
}

.btn-close-detail {
  width: 100%;
  padding: 14px;
  border-radius: 12px;
  background: #f4f6fb;
  border: 1px solid #edf0f7;
  color: #6b7699;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-close-detail:active {
  background: #e8ecf5;
}

/* 트랜지션 */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: opacity 0.25s;
}

.slide-up-enter,
.slide-up-leave-to {
  opacity: 0;
}

.slide-up-enter .detail-sheet,
.slide-up-leave-to .detail-sheet {
  transform: translateY(100%);
}

.slide-up-enter-active .detail-sheet,
.slide-up-leave-active .detail-sheet {
  transition: transform 0.3s cubic-bezier(0.32, 0.72, 0, 1);
}
</style>
