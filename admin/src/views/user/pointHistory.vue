<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <el-button size="small" icon="el-icon-arrow-left" @click="$router.back()">목록으로</el-button>
        <p class="tl" style="margin-left:12px">포인트 사용내역</p>
        <p class="usernumber">{{ userName }}</p>
      </div>
    </div>

    <!-- 잔액 카드 -->
    <div v-if="wallet" class="balance-card">
      <div class="balance-row">
        <div class="balance-item">
          <div class="balance-label">현재 잔액</div>
          <div class="balance-value">{{ wallet.balance.toLocaleString() }}<span class="balance-unit">원</span></div>
        </div>
        <div class="balance-item">
          <div class="balance-label">총 충전</div>
          <div class="balance-value small">{{ wallet.totalCharged.toLocaleString() }}원</div>
        </div>
        <div class="balance-item">
          <div class="balance-label">총 사용</div>
          <div class="balance-value small">{{ wallet.totalUsed.toLocaleString() }}원</div>
        </div>
      </div>
    </div>

    <!-- 필터 -->
    <div class="filter-row">
      <el-select v-model="typeFilter" placeholder="유형 전체" size="small" clearable style="width:130px" @change="applyFilter">
        <el-option label="충전" value="CHARGE" />
        <el-option label="지급(적립)" value="EARN" />
        <el-option label="사용(차감)" value="USE" />
        <el-option label="환불" value="REFUND" />
        <el-option label="취소" value="CANCEL" />
      </el-select>
    </div>

    <div v-loading="loading" class="user-content">
      <el-table
        border
        :data="transactions"
        :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      >
        <el-table-column label="유형" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="typeTagType(row.transactionType)" size="small" effect="dark">
              {{ typeLabel(row.transactionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="금액" width="130" align="right">
          <template slot-scope="{ row }">
            <span :class="row.transactionType === 'USE' ? 'amount-neg' : 'amount-pos'">
              {{ row.transactionType === 'USE' ? '-' : '+' }}{{ row.amount.toLocaleString() }}원
            </span>
          </template>
        </el-table-column>
        <el-table-column label="거래 후 잔액" width="140" align="right">
          <template slot-scope="{ row }">{{ row.balanceAfter.toLocaleString() }}원</template>
        </el-table-column>
        <el-table-column prop="description" label="내용" min-width="200" show-overflow-tooltip />
        <el-table-column label="일시" width="170">
          <template slot-scope="{ row }">{{ row.createdDate | parseDate }}</template>
        </el-table-column>
      </el-table>

      <Pagination
        :total="totalElements"
        :page.sync="currentPage"
        :limit.sync="pageSize"
        @pagination="fetchTransactions"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getAdminUserWallet, getAdminUserTransactions } from '@/api/cash';
import Pagination from '@/components/Pagination/index.vue';

@Component({ name: 'UserPointHistory', components: { Pagination } })
export default class extends Vue {
  private loading = false;

  private transactions: any[] = [];

  private totalElements = 0;

  private currentPage = 1;

  private pageSize = 20;

  private typeFilter = '';

  private wallet: { balance: number, totalCharged: number, totalUsed: number } | null = null;

  get userUid(): string {
    return this.$route.params.userUid;
  }

  get userName(): string {
    return this.$route.params.userName || this.userUid;
  }

  async created() {
    await Promise.all([this.fetchWallet(), this.fetchTransactions()]);
  }

  async fetchWallet() {
    try {
      const res = await getAdminUserWallet(this.userUid);
      if (res.data) {
        this.wallet = {
          balance: res.data.balance || 0,
          totalCharged: res.data.totalCharged || 0,
          totalUsed: res.data.totalUsed || 0,
        };
      }
    } catch {
      // 지갑 없으면 표시 생략
    }
  }

  async fetchTransactions() {
    this.loading = true;
    try {
      const res = await getAdminUserTransactions(this.userUid, {
        page: this.currentPage,
        size: this.pageSize,
      });
      const { data } = res;
      const all = data.content || data || [];
      this.transactions = this.typeFilter ? all.filter((t: any) => t.transactionType === this.typeFilter) : all;
      this.totalElements = data.totalElements || this.transactions.length;
    } catch {
      this.$message.error('내역을 불러오는데 실패했습니다.');
    } finally {
      this.loading = false;
    }
  }

  applyFilter() {
    this.currentPage = 1;
    this.fetchTransactions();
  }

  typeLabel(type: string): string {
    const map: Record<string, string> = {
      CHARGE: '충전', USE: '차감', REFUND: '환불', EARN: '지급', CANCEL: '취소',
    };
    return map[type] || type;
  }

  typeTagType(type: string): string {
    const map: Record<string, string> = {
      CHARGE: 'primary', EARN: 'success', USE: 'danger', REFUND: 'warning', CANCEL: 'info',
    };
    return map[type] || '';
  }
}
</script>

<style scoped>
.balance-card {
  background: #0f2027;
  border-radius: 8px;
  padding: 20px 24px;
  margin-bottom: 16px;
  color: #fff;
}
.balance-row { display: flex; gap: 40px; }
.balance-label { font-size: 12px; opacity: 0.7; margin-bottom: 4px; }
.balance-value { font-size: 24px; font-weight: 700; }
.balance-value.small { font-size: 18px; }
.balance-unit { font-size: 14px; margin-left: 2px; }
.filter-row { margin-bottom: 12px; }
.amount-pos { color: #67c23a; font-weight: 600; }
.amount-neg { color: #f56c6c; font-weight: 600; }
</style>
