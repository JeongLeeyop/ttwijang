<template>
  <div class="content cash-charge-page">
    <div class="form-container">
      <h3 class="section-title">미리 충전하고 편리하게 사용하세요!</h3>

      <div class="charge-section">
        <label class="section-label">충전할 금액</label>
        <div class="amount-grid">
          <button
            v-for="amount in chargeAmounts"
            :key="amount"
            :class="['amount-button', { active: selectedAmount === amount }]"
            @click="selectAmount(amount)"
          >
            {{ formatCurrency(amount) }}원
          </button>
        </div>
        <div class="current-cash">
          보유 캐쉬 <strong>{{ formatCurrency(currentBalance) }}원</strong>
        </div>
      </div>

      <div class="payment-section">
        <label class="section-label">결제 방법</label>
        <div class="payment-methods">
          <button
            :class="['payment-button', { active: paymentMethod === 'BANK_TRANSFER' }]"
            @click="paymentMethod = 'BANK_TRANSFER'"
          >
            퀵 계좌이체
          </button>
          <button
            :class="['payment-button', { active: paymentMethod === 'VIRTUAL_ACCOUNT' }]"
            @click="paymentMethod = 'VIRTUAL_ACCOUNT'"
          >
            가상 계좌
          </button>
        </div>
      </div>

      <button
        class="submit-button"
        :disabled="!selectedAmount || !paymentMethod || isCharging"
        @click="handleCharge"
      >
        <i v-if="isCharging" class="el-icon-loading"></i>
        {{ isCharging ? '처리 중...' : '충전 신청하기' }}
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { chargeCash, getWallet } from '@/api/cash';

@Component({
  name: 'CashCharge',
})
export default class CashCharge extends Vue {
  private chargeAmounts = [
    5000, 8000, 10000,
    12000, 15000, 20000,
    25000, 30000, 50000,
    60000, 80000, 100000,
  ];

  private selectedAmount: number | null = null;

  private paymentMethod = '';

  private currentBalance = 0;

  private isCharging = false;

  async created(): Promise<void> {
    await this.loadWallet();
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

  private selectAmount(amount: number): void {
    this.selectedAmount = amount;
  }

  private formatCurrency(value: number): string {
    return value.toLocaleString('ko-KR');
  }

  private async handleCharge(): Promise<void> {
    if (!this.selectedAmount) {
      this.$message.warning('충전할 금액을 선택해주세요.');
      return;
    }

    if (!this.paymentMethod) {
      this.$message.warning('결제 방법을 선택해주세요.');
      return;
    }

    try {
      await this.$confirm(
        `${this.formatCurrency(this.selectedAmount)}원을 충전하시겠습니까?`,
        '캐쉬 충전',
        {
          confirmButtonText: '충전하기',
          cancelButtonText: '취소',
          type: 'info',
        },
      );
    } catch {
      return;
    }

    this.isCharging = true;
    try {
      await chargeCash({
        amount: this.selectedAmount,
        paymentMethod: this.paymentMethod,
      });

      this.$message.success(`${this.formatCurrency(this.selectedAmount)}원이 충전되었습니다.`);
      this.currentBalance += this.selectedAmount;
      this.selectedAmount = null;
      this.paymentMethod = '';

      setTimeout(() => {
        this.$router.go(-1);
      }, 1500);
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || '충전에 실패했습니다.';
      this.$message.error(errorMsg);
      console.error('캐쉬 충전 실패:', error);
    } finally {
      this.isCharging = false;
    }
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
