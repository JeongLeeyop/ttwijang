<template>
  <div class="faq-page">
    <!-- 헤더 -->
    <div class="page-header">
      <button class="btn-back" @click="$router.go(-1)">
        <i class="el-icon-arrow-left"></i>
      </button>
      <span class="page-title">자주 묻는 질문</span>
    </div>

    <!-- 카테고리 필터 -->
    <div v-if="categories.length > 1" class="category-bar">
      <div class="category-scroll">
        <button
          v-for="cat in categories"
          :key="cat"
          class="cat-chip"
          :class="{ active: activeCategory === cat }"
          @click="activeCategory = cat"
        >
          {{ cat === '__all__' ? '전체' : cat }}
        </button>
      </div>
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="state-center">
      <div class="spinner"></div>
    </div>

    <!-- 빈 상태 -->
    <div v-else-if="faqs.length === 0" class="state-center">
      <div class="empty-icon">
        <i class="el-icon-question"></i>
      </div>
      <p class="empty-text">등록된 FAQ가 없습니다</p>
    </div>

    <!-- 목록 -->
    <div v-else class="faq-list">
      <div
        v-for="(item, idx) in filteredFaqs"
        :key="item.uid"
        class="faq-card"
        :class="{ open: openUids.includes(item.uid) }"
        :style="{ animationDelay: idx * 40 + 'ms' }"
      >
        <div class="faq-question" @click="toggle(item.uid)">
          <div class="q-mark">Q</div>
          <span class="question-text">{{ item.question }}</span>
          <div class="toggle-wrap">
            <i :class="openUids.includes(item.uid) ? 'el-icon-minus' : 'el-icon-plus'"></i>
          </div>
        </div>
        <transition name="expand">
          <div v-show="openUids.includes(item.uid)" class="faq-answer">
            <div class="a-mark">A</div>
            <span class="answer-text">{{ item.answer }}</span>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getFaqList, Faq } from '@/api/faq';

@Component
export default class FaqPage extends Vue {
  private isLoading = false;

  private faqs: Faq[] = [];

  private openUids: string[] = [];

  private activeCategory = '__all__';

  created(): void {
    this.fetchFaqs();
  }

  get categories(): string[] {
    const cats = this.faqs.map((f) => f.category).filter((c): c is string => !!c);
    const unique = Array.from(new Set(cats));
    return unique.length > 0 ? ['__all__', ...unique] : [];
  }

  get filteredFaqs(): Faq[] {
    if (this.activeCategory === '__all__') return this.faqs;
    return this.faqs.filter((f) => f.category === this.activeCategory);
  }

  private async fetchFaqs(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getFaqList();
      this.faqs = res.data || [];
    } finally {
      this.isLoading = false;
    }
  }

  private toggle(uid: string): void {
    const idx = this.openUids.indexOf(uid);
    if (idx >= 0) {
      this.openUids.splice(idx, 1);
    } else {
      this.openUids.push(uid);
    }
  }
}
</script>

<style scoped>
.faq-page {
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

/* 카테고리 바 */
.category-bar {
  padding: 14px 0 2px;
  background: #fff;
  border-bottom: 1px solid #edf0f7;
}

.category-scroll {
  display: flex;
  gap: 8px;
  padding: 0 16px;
  overflow-x: auto;
  scrollbar-width: none;
}

.category-scroll::-webkit-scrollbar {
  display: none;
}

.cat-chip {
  flex-shrink: 0;
  padding: 6px 16px;
  border-radius: 20px;
  border: 1px solid #e8ecf5;
  background: transparent;
  color: #aab2cc;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.cat-chip.active {
  background: #3949ab;
  border-color: #3949ab;
  color: #fff;
  font-weight: 700;
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

/* FAQ 목록 */
.faq-list {
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.faq-card {
  background: #fff;
  border: 1px solid #edf0f7;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(57, 73, 171, 0.04);
  animation: fadeUp 0.35s ease both;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.faq-card.open {
  border-color: #c5cae9;
  box-shadow: 0 4px 16px rgba(57, 73, 171, 0.1);
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

.faq-question {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px 18px;
  cursor: pointer;
}

.q-mark {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border-radius: 8px;
  background: linear-gradient(135deg, #3949ab, #5c6bc0);
  color: #fff;
  font-size: 13px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1px;
}

.question-text {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: #1a2340;
  line-height: 1.5;
  letter-spacing: -0.2px;
}

.toggle-wrap {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: #f4f6fb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #aab2cc;
  margin-top: 1px;
  transition: background 0.2s, color 0.2s;
}

.faq-card.open .toggle-wrap {
  background: #e8eaf6;
  color: #3949ab;
}

.faq-answer {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 18px 18px;
  border-top: 1px solid #edf0f7;
  background: #f9fafd;
}

.a-mark {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border-radius: 8px;
  background: #e8eaf6;
  color: #3949ab;
  font-size: 13px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1px;
}

.answer-text {
  font-size: 14px;
  color: #4a5473;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 확장 트랜지션 */
.expand-enter-active,
.expand-leave-active {
  transition: opacity 0.2s;
}

.expand-enter,
.expand-leave-to {
  opacity: 0;
}
</style>
