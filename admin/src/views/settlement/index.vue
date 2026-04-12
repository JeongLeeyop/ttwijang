<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">정산 관리</p>
        <p class="usernumber">매치 참가비를 팀 운영자 계좌로 정산합니다</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" type="border-card" @tab-click="onTabClick">
      <!-- 월별 정산 탭 -->
      <el-tab-pane label="월별 정산" name="monthly">
        <div class="filter-row">
          <el-date-picker
            v-model="monthlyPeriod"
            type="month"
            placeholder="월 선택"
            value-format="yyyy-MM"
            style="width:160px"
          />
          <el-button type="primary" @click="fetchMonthlySummary" :loading="monthlyLoading" style="margin-left:8px">
            조회
          </el-button>
          <span v-if="settlementDays" style="margin-left:16px;color:#909399;font-size:13px">
            경기 완료 후 {{ settlementDays }}일 경과한 건만 집계됩니다
          </span>
        </div>

        <el-table
          :data="monthlySummary"
          v-loading="monthlyLoading"
          :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
          border
          style="margin-top:16px"
        >
          <el-table-column label="팀명" prop="teamName" min-width="130" />
          <el-table-column label="건수" width="70" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="openModal(scope.row)">
                {{ scope.row.itemCount }}건
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="정산금액" width="130" align="right">
            <template slot-scope="scope">
              {{ Number(scope.row.totalAmount).toLocaleString() }}원
            </template>
          </el-table-column>
          <el-table-column label="은행" prop="bankName" width="100" />
          <el-table-column label="계좌번호" prop="bankAccount" min-width="150" />
          <el-table-column label="정산 상태" width="100" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.alreadySettled" type="success" size="small">완료</el-tag>
              <el-tag v-else type="warning" size="small">미완료</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="액션" width="120" align="center">
            <template slot-scope="scope">
              <el-button
                v-if="!scope.row.alreadySettled"
                size="mini"
                type="primary"
                @click="openCompleteDialog(scope.row)"
              >
                정산하기
              </el-button>
              <span v-else style="color:#67c23a;font-size:13px">완료됨</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 건별 내역 탭 -->
      <el-tab-pane label="건별 내역" name="items">
        <div class="filter-row">
          <el-date-picker
            v-model="itemDateRange"
            type="daterange"
            range-separator="~"
            start-placeholder="시작일"
            end-placeholder="종료일"
            value-format="yyyy-MM-dd"
            style="width:260px"
          />
          <el-input
            v-model="itemTeamName"
            placeholder="팀명 검색 (선택)"
            style="width:160px;margin-left:8px"
            clearable
          />
          <el-button type="primary" @click="fetchDetailItems" :loading="itemsLoading" style="margin-left:8px">
            조회
          </el-button>
        </div>

        <el-table
          :data="detailItems"
          v-loading="itemsLoading"
          :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
          border
          style="margin-top:16px"
        >
          <el-table-column label="결제일시" width="160">
            <template slot-scope="scope">{{ scope.row.createdDate | parseDate }}</template>
          </el-table-column>
          <el-table-column label="팀명" prop="teamName" width="130" />
          <el-table-column label="경기장" prop="stadiumName" min-width="150" show-overflow-tooltip />
          <el-table-column label="매치일" prop="matchDate" width="110" align="center" />
          <el-table-column label="결제자" prop="userName" width="110" />
          <el-table-column label="금액" width="110" align="right">
            <template slot-scope="scope">{{ Number(scope.row.amount).toLocaleString() }}원</template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-if="itemsTotal > 0"
          :current-page="itemsPage + 1"
          :page-size="itemsSize"
          :total="itemsTotal"
          layout="prev, pager, next"
          style="margin-top:16px;text-align:right"
          @current-change="onItemsPageChange"
        />
      </el-tab-pane>

      <!-- 정산 이력 탭 -->
      <el-tab-pane label="정산 이력" name="history">
        <el-button type="primary" @click="fetchHistory" :loading="historyLoading" style="margin-bottom:16px">
          새로고침
        </el-button>

        <el-table
          :data="historyList"
          v-loading="historyLoading"
          :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
          border
        >
          <el-table-column label="기간" prop="period" width="100" align="center" />
          <el-table-column label="팀명" prop="teamName" min-width="130" />
          <el-table-column label="건수" prop="itemCount" width="70" align="center" />
          <el-table-column label="정산금액" width="130" align="right">
            <template slot-scope="scope">{{ Number(scope.row.totalAmount).toLocaleString() }}원</template>
          </el-table-column>
          <el-table-column label="은행" prop="bankName" width="100" />
          <el-table-column label="계좌번호" prop="bankAccount" min-width="150" />
          <el-table-column label="상태" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'" size="small">
                {{ scope.row.status === 'COMPLETED' ? '완료' : '대기' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="완료일" width="160">
            <template slot-scope="scope">{{ scope.row.settledAt | parseDate }}</template>
          </el-table-column>
          <el-table-column label="메모" prop="adminNote" min-width="160" show-overflow-tooltip />
        </el-table>

        <el-pagination
          v-if="historyTotal > 0"
          :current-page="historyPage + 1"
          :page-size="historySize"
          :total="historyTotal"
          layout="prev, pager, next"
          style="margin-top:16px;text-align:right"
          @current-change="onHistoryPageChange"
        />
      </el-tab-pane>

      <!-- 정산 설정 탭 -->
      <el-tab-pane label="정산 설정" name="config">
        <div style="max-width:520px;padding:8px 0">
          <el-form :model="configForm" label-width="180px" size="small">
            <el-form-item label="정산 대기 일수 (N일)">
              <el-input-number
                v-model="configForm.settlementDays"
                :min="0"
                :max="30"
                controls-position="right"
                style="width:120px"
              />
              <span style="margin-left:8px;color:#909399;font-size:12px">
                경기 완료 후 이 일수가 지난 건만 정산 집계됩니다
              </span>
            </el-form-item>

            <el-form-item label="Toss 월별 자동이체">
              <el-switch
                v-model="configForm.useTossPayout"
                active-text="사용"
                inactive-text="수동"
              />
              <div style="margin-top:4px;color:#909399;font-size:12px">
                OFF: 관리자가 직접 이체 후 완료 처리<br>
                ON: 매월 지정일 오전 2시에 전월 정산을 Toss API로 자동 이체 (Enterprise 계약 필요)
              </div>
            </el-form-item>

            <el-form-item label="자동 이체 실행 일" v-if="configForm.useTossPayout">
              <el-input-number
                v-model="configForm.autoSettleDay"
                :min="1"
                :max="28"
                controls-position="right"
                style="width:120px"
              />
              <span style="margin-left:8px;color:#909399;font-size:12px">
                매월 {{ configForm.autoSettleDay }}일에 전월 정산 자동 실행 (최대 28일)
              </span>
            </el-form-item>

            <el-form-item label="안내문">
              <el-input
                v-model="configForm.description"
                type="textarea"
                :rows="3"
                placeholder="팀 운영자에게 표시할 안내문 (선택)"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="configSaving" @click="saveConfig">저장</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 건별 상세 모달 -->
    <el-dialog
      :title="`${modalTeamName} · ${modalPeriod} 건별 내역`"
      :visible.sync="modalVisible"
      width="700px"
    >
      <div v-loading="modalLoading">
        <el-table
          :data="modalItems"
          :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
          border
          size="small"
        >
          <el-table-column label="결제일시" width="155">
            <template slot-scope="scope">{{ scope.row.createdDate | parseDate }}</template>
          </el-table-column>
          <el-table-column label="경기장" prop="stadiumName" min-width="140" show-overflow-tooltip />
          <el-table-column label="매치일" prop="matchDate" width="100" align="center" />
          <el-table-column label="결제자" prop="userName" width="100" />
          <el-table-column label="금액" width="100" align="right">
            <template slot-scope="scope">{{ Number(scope.row.amount).toLocaleString() }}원</template>
          </el-table-column>
        </el-table>
        <div v-if="modalItems.length === 0 && !modalLoading" style="text-align:center;padding:24px;color:#909399">
          해당 조건의 내역이 없습니다
        </div>
      </div>
      <span slot="footer">
        <el-button @click="modalVisible = false">닫기</el-button>
      </span>
    </el-dialog>

    <!-- 정산하기 다이얼로그 -->
    <el-dialog title="정산하기" :visible.sync="completeDialogVisible" width="460px">
      <div v-if="selectedRow" style="margin-bottom:16px">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="팀명">{{ selectedRow.teamName }}</el-descriptions-item>
          <el-descriptions-item label="정산금액">
            <strong>{{ selectedRow.totalAmount ? Number(selectedRow.totalAmount).toLocaleString() : 0 }}원</strong>
            ({{ selectedRow.itemCount }}건)
          </el-descriptions-item>
          <el-descriptions-item label="입금 은행">{{ selectedRow.bankName }}</el-descriptions-item>
          <el-descriptions-item label="계좌번호">{{ selectedRow.bankAccount }}</el-descriptions-item>
        </el-descriptions>
        <el-alert
          v-if="!selectedRow.bankAccount"
          type="error"
          title="환불 계좌 미등록"
          description="팀 운영자가 환불 계좌를 등록해야 정산이 가능합니다."
          :closable="false"
          style="margin-top:12px"
        />
      </div>
      <el-input
        v-model="adminNote"
        type="textarea"
        :rows="2"
        placeholder="메모 (선택)"
      />
      <span slot="footer">
        <el-button @click="completeDialogVisible = false">취소</el-button>
        <el-button
          type="primary"
          :loading="completing"
          :disabled="!selectedRow || !selectedRow.bankAccount"
          @click="handleComplete"
        >
          Toss 이체 실행
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  getMonthlySummary, getModalItems, getDetailItems,
  getSettlementHistory, getSettlementConfig, updateSettlementConfig,
  createSettlement, completeSettlement,
} from '@/api/settlement';

interface MonthlySummary {
  teamUid: string
  teamName: string
  ownerUid: string
  bankName: string
  bankAccount: string
  totalAmount: number
  itemCount: number
  period: string
  alreadySettled: boolean
  settlementUid: string | null
}

interface ItemResponse {
  teamName: string
  stadiumName: string
  matchDate: string
  userName: string
  amount: number
  createdDate: string
}

interface HistoryResponse {
  uid: string
  teamUid: string
  teamName: string
  bankName: string
  bankAccount: string
  totalAmount: number
  itemCount: number
  period: string
  status: string
  settledAt: string
  adminNote: string
  createdDate: string
}

@Component({ name: 'SettlementIndex' })
export default class SettlementIndex extends Vue {
  activeTab = 'monthly'

  // 월별
  monthlyPeriod = ''
  monthlySummary: MonthlySummary[] = []
  monthlyLoading = false
  settlementDays = 3

  // 모달
  modalVisible = false
  modalLoading = false
  modalTeamName = ''
  modalPeriod = ''
  modalTeamUid = ''
  modalItems: ItemResponse[] = []

  // 건별
  itemDateRange: string[] = []
  itemTeamName = ''
  detailItems: ItemResponse[] = []
  itemsLoading = false
  itemsPage = 0
  itemsSize = 20
  itemsTotal = 0

  // 이력
  historyList: HistoryResponse[] = []
  historyLoading = false
  historyPage = 0
  historySize = 20
  historyTotal = 0

  // 완료 다이얼로그
  completeDialogVisible = false
  selectedRow: MonthlySummary | null = null
  adminNote = ''
  completing = false

  // 설정
  configForm = { settlementDays: 3, useTossPayout: false, autoSettleDay: 1, description: '' }
  configSaving = false

  created() {
    const now = new Date();
    this.monthlyPeriod = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
    this.fetchConfig();
    this.fetchMonthlySummary();
    this.fetchHistory();
  }

  onTabClick(tab: any) {
    if (tab.name === 'config') this.fetchConfig();
  }

  // ── 설정 ───────────────────────────────

  async fetchConfig() {
    try {
      const res: any = await getSettlementConfig();
      const d = res.data || {};
      this.settlementDays = d.settlementDays || 3;
      this.configForm = {
        settlementDays: d.settlementDays || 3,
        useTossPayout: !!d.useTossPayout,
        autoSettleDay: d.autoSettleDay || 1,
        description: d.description || '',
      };
    } catch (e) { /* ignore */ }
  }

  async saveConfig() {
    this.configSaving = true;
    try {
      await updateSettlementConfig(this.configForm);
      this.settlementDays = this.configForm.settlementDays;
      this.$message.success('설정이 저장되었습니다');
    } catch (e) {
      this.$message.error('저장 실패');
    } finally {
      this.configSaving = false;
    }
  }

  // ── 월별 ───────────────────────────────

  async fetchMonthlySummary() {
    if (!this.monthlyPeriod) return;
    this.monthlyLoading = true;
    try {
      const res: any = await getMonthlySummary(this.monthlyPeriod);
      this.monthlySummary = res.data || [];
    } catch (e) {
      this.$message.error('월별 정산 조회 실패');
    } finally {
      this.monthlyLoading = false;
    }
  }

  // ── 건별 모달 ──────────────────────────

  async openModal(row: MonthlySummary) {
    this.modalTeamName = row.teamName;
    this.modalPeriod = row.period || this.monthlyPeriod;
    this.modalTeamUid = row.teamUid;
    this.modalVisible = true;
    this.modalLoading = true;
    try {
      const res: any = await getModalItems(row.teamUid, this.modalPeriod);
      this.modalItems = res.data || [];
    } catch (e) {
      this.$message.error('건별 내역 조회 실패');
    } finally {
      this.modalLoading = false;
    }
  }

  // ── 건별 탭 ────────────────────────────

  async fetchDetailItems() {
    if (!this.itemDateRange || this.itemDateRange.length < 2) {
      this.$message.warning('기간을 선택해주세요');
      return;
    }
    this.itemsLoading = true;
    try {
      const res: any = await getDetailItems({
        teamName: this.itemTeamName || undefined,
        startDate: this.itemDateRange[0],
        endDate: this.itemDateRange[1],
        page: this.itemsPage,
        size: this.itemsSize,
      });
      const page = res.data || {};
      this.detailItems = page.content || [];
      this.itemsTotal = page.totalElements || 0;
    } catch (e) {
      this.$message.error('건별 내역 조회 실패');
    } finally {
      this.itemsLoading = false;
    }
  }

  onItemsPageChange(page: number) {
    this.itemsPage = page - 1;
    this.fetchDetailItems();
  }

  // ── 이력 ───────────────────────────────

  async fetchHistory() {
    this.historyLoading = true;
    try {
      const res: any = await getSettlementHistory({ page: this.historyPage, size: this.historySize });
      const page = res.data || {};
      this.historyList = page.content || [];
      this.historyTotal = page.totalElements || 0;
    } catch (e) {
      this.$message.error('정산 이력 조회 실패');
    } finally {
      this.historyLoading = false;
    }
  }

  onHistoryPageChange(page: number) {
    this.historyPage = page - 1;
    this.fetchHistory();
  }

  // ── 정산하기 ────────────────────────────

  openCompleteDialog(row: MonthlySummary) {
    this.selectedRow = row;
    this.adminNote = '';
    this.completeDialogVisible = true;
  }

  async handleComplete() {
    if (!this.selectedRow) return;
    this.completing = true;
    try {
      let uid = this.selectedRow.settlementUid;
      if (!uid) {
        const createRes: any = await createSettlement({
          teamUid: this.selectedRow.teamUid,
          period: this.selectedRow.period || this.monthlyPeriod,
        });
        uid = createRes.data.uid;
      }
      await completeSettlement(uid!, { adminNote: this.adminNote });
      this.$message.success('Toss 계좌이체가 완료되었습니다');
      this.completeDialogVisible = false;
      this.fetchMonthlySummary();
      this.fetchHistory();
    } catch (e: any) {
      this.$message.error(e?.response?.data?.message || '정산 처리 실패');
    } finally {
      this.completing = false;
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 8px;
}
</style>
