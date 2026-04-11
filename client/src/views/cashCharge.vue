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
import { getWallet } from '@/api/cash';
import { preparePayment } from '@/api/payment';

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

  private loadTossScript(): Promise<void> {
    return new Promise((resolve, reject) => {
      if ((window as any).TossPayments) {
        resolve();
        return;
      }
      const script = document.createElement('script');
      script.src = 'https://js.tosspayments.com/v1/payment';
      script.onload = () => resolve();
      script.onerror = () => reject(new Error('Toss SDK 로드 실패'));
      document.head.appendChild(script);
    });
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

    this.isCharging = true;
    try {
      // 1. 결제 준비 (orderId 발급)
      const res = await preparePayment({ amount: this.selectedAmount, orderName: '캐쉬 충전' });
      const { orderId, orderName, amount } = res.data;

      // 2. Toss SDK 로드
      await this.loadTossScript();

      const tossKey = process.env.VUE_APP_TOSS_KEY as string;
      const successUrl = process.env.VUE_APP_TOSS_SUSSCESS as string;
      const failUrl = process.env.VUE_APP_TOSS_FAIL as string;

      const tossPayments = (window as any).TossPayments(tossKey);

      // 3. 결제 요청 (Toss가 successUrl로 리다이렉트)
      const tossMethod = this.paymentMethod === 'BANK_TRANSFER' ? '계좌이체' : '가상계좌';
      await tossPayments.requestPayment(tossMethod, {
        amount,
        orderId,
        orderName,
        successUrl,
        failUrl,
      });
    } catch (error: any) {
      // 사용자가 결제 취소한 경우는 조용히 처리
      if (error?.code === 'USER_CANCEL') {
        this.$message.info('결제를 취소하셨습니다.');
      } else {
        const errorMsg = error?.response?.data?.message || error?.message || '결제 요청에 실패했습니다.';
        this.$message.error(errorMsg);
        console.error('결제 요청 실패:', error);
      }
      this.isCharging = false;
    }
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
