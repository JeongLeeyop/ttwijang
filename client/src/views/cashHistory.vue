<template>
  <div class="content cash-history-page">
    <div class="history-container">
      <!-- Balance Card -->
      <div class="balance-card">
        <div class="balance-label">나의 캐쉬 (포인트)</div>
        <div class="balance-display">
          <span class="balance-value">{{ formatCurrency(currentBalance) }}</span>
          <span class="balance-unit">원</span>
        </div>
        <div class="balance-actions">
          <button class="action-btn refund-btn" @click="handleRefund">
            캐쉬 환불
          </button>
          <button class="action-btn charge-btn" @click="goToCharge">
            충전하기
          </button>
        </div>
      </div>

      <!-- Filter Tabs -->
      <div class="filter-section">
        <div class="filter-label">캐쉬 상세 내역</div>
        <div class="filter-tabs">
          <button
            v-for="tab in filterTabs"
            :key="tab.value"
            :class="['filter-tab', { active: activeFilter === tab.value }]"
            @click="changeFilter(tab.value)"
          >
            {{ tab.label }}
          </button>
        </div>
      </div>

      <!-- Transaction List -->
      <div class="transaction-list">
        <div v-if="isLoading" class="loading-state">
          <i class="el-icon-loading"></i>
          <span>내역을 불러오는 중...</span>
        </div>

        <template v-else-if="filteredTransactions.length > 0">
          <div
            v-for="item in filteredTransactions"
            :key="item.uid"
            class="transaction-item"
          >
            <div class="transaction-info">
              <span class="transaction-desc">{{ item.description || getTypeLabel(item.transactionType) }}</span>
              <span class="transaction-date">{{ formatDate(item.createdDate) }}</span>
            </div>
            <div :class="['transaction-amount', getAmountClass(item.transactionType)]">
              {{ getAmountPrefix(item.transactionType) }}{{ formatCurrency(item.amount) }}원
            </div>
          </div>
        </template>

        <div v-else class="empty-state">
          <i class="el-icon-document"></i>
          <span>내역이 없습니다.</span>
        </div>
      </div>

      <!-- Load More -->
      <div v-if="hasMore && !isLoading" class="load-more">
        <button class="load-more-btn" @click="loadMore">
          <i v-if="isLoadingMore" class="el-icon-loading"></i>
          {{ isLoadingMore ? '불러오는 중...' : '더 보기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getWallet, getTransactions, Transaction } from '@/api/cash';

interface FilterTab {
  label: string
  value: string
}

@Component({
  name: 'CashHistory',
})
export default class CashHistory extends Vue {
  private currentBalance = 0;

  private transactions: Transaction[] = [];

  private activeFilter = 'ALL';

  private isLoading = false;

  private isLoadingMore = false;

  private currentPage = 0;

  private pageSize = 20;

  private hasMore = false;

  private filterTabs: FilterTab[] = [
    { label: '전체', value: 'ALL' },
    { label: '환불', value: 'REFUND' },
    { label: '충전/적립', value: 'CHARGE_EARN' },
    { label: '사용/취소', value: 'USE_CANCEL' },
  ];

  get filteredTransactions(): Transaction[] {
    if (this.activeFilter === 'ALL') {
      return this.transactions;
    }
    if (this.activeFilter === 'REFUND') {
      return this.transactions.filter((t) => t.transactionType === 'REFUND');
    }
    if (this.activeFilter === 'CHARGE_EARN') {
      return this.transactions.filter((t) => t.transactionType === 'CHARGE' || t.transactionType === 'EARN');
    }
    if (this.activeFilter === 'USE_CANCEL') {
      return this.transactions.filter((t) => t.transactionType === 'USE' || t.transactionType === 'CANCEL');
    }
    return this.transactions;
  }

  async created(): Promise<void> {
    await Promise.all([
      this.loadWallet(),
      this.loadTransactions(),
    ]);
  }

  private async loadWallet(): Promise<void> {
    try {
      const response = await getWallet();
      if (response.data) {
        this.currentBalance = response.data.balance || 0;
      }
    } catch (error) {
      console.warn('지갑 정보 로드 실패:', error);
    }
  }

  private async loadTransactions(): Promise<void> {
    this.isLoading = true;
    this.currentPage = 0;
    try {
      const response = await getTransactions({
        page: this.currentPage,
        size: this.pageSize,
      });
      if (response.data) {
        if (Array.isArray(response.data)) {
          this.transactions = response.data;
          this.hasMore = response.data.length >= this.pageSize;
        } else if (response.data.content) {
          this.transactions = response.data.content;
          this.hasMore = !response.data.last;
        } else {
          this.transactions = [];
          this.hasMore = false;
        }
      }
    } catch (error) {
      console.error('거래 내역 로드 실패:', error);
      this.$message.error('내역을 불러오는데 실패했습니다.');
    } finally {
      this.isLoading = false;
    }
  }

  private async loadMore(): Promise<void> {
    if (this.isLoadingMore) return;

    this.isLoadingMore = true;
    this.currentPage += 1;
    try {
      const response = await getTransactions({
        page: this.currentPage,
        size: this.pageSize,
      });
      if (response.data) {
        let newItems: Transaction[] = [];
        if (Array.isArray(response.data)) {
          newItems = response.data;
          this.hasMore = newItems.length >= this.pageSize;
        } else if (response.data.content) {
          newItems = response.data.content;
          this.hasMore = !response.data.last;
        }
        this.transactions = [...this.transactions, ...newItems];
      }
    } catch (error) {
      console.error('추가 내역 로드 실패:', error);
      this.currentPage -= 1;
    } finally {
      this.isLoadingMore = false;
    }
  }

  private changeFilter(filter: string): void {
    this.activeFilter = filter;
  }

  private formatCurrency(value: number): string {
    return value.toLocaleString('ko-KR');
  }

  private formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}.${month}.${day} ${hours}:${minutes}`;
  }

  private getTypeLabel(type: string): string {
    const labels: { [key: string]: string } = {
      CHARGE: '캐쉬 충전',
      USE: '캐쉬 사용',
      REFUND: '캐쉬 환불',
      EARN: '캐쉬 적립',
      CANCEL: '사용 취소',
    };
    return labels[type] || type;
  }

  private getAmountClass(type: string): string {
    if (type === 'CHARGE' || type === 'EARN' || type === 'REFUND' || type === 'CANCEL') {
      return 'positive';
    }
    return 'negative';
  }

  private getAmountPrefix(type: string): string {
    if (type === 'USE') {
      return '-';
    }
    return '+';
  }

  private handleRefund(): void {
    this.$message.info('캐쉬 환불 기능은 준비 중입니다.');
  }

  private goToCharge(): void {
    this.$router.push({ name: 'CashCharge' });
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
