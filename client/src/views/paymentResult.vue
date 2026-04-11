<template>
  <div class="content payment-result-page">
    <div class="result-container">
      <template v-if="isSuccess">
        <div class="result-icon success">
          <i class="el-icon-circle-check"></i>
        </div>
        <h2 class="result-title">충전 완료!</h2>
        <p class="result-amount">
          <strong>{{ formatCurrency(amount) }}원</strong>이 충전되었습니다.
        </p>
      </template>
      <template v-else>
        <div class="result-icon fail">
          <i class="el-icon-circle-close"></i>
        </div>
        <h2 class="result-title">결제 실패</h2>
        <p class="result-message">{{ message || '결제 처리 중 오류가 발생했습니다.' }}</p>
      </template>

      <div class="result-actions">
        <button class="submit-button" @click="$router.push('/cash-charge')">
          다시 충전하기
        </button>
        <button class="cancel-button" @click="$router.push('/mypage')">
          마이페이지로
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component({
  name: 'PaymentResult',
})
export default class PaymentResult extends Vue {
  get isSuccess(): boolean {
    return this.$route.query.success === 'true';
  }

  get amount(): number {
    return Number(this.$route.query.amount) || 0;
  }

  get message(): string {
    const msg = this.$route.query.message as string;
    try {
      return msg ? decodeURIComponent(msg) : '';
    } catch {
      return msg || '';
    }
  }

  private formatCurrency(value: number): string {
    return value.toLocaleString('ko-KR');
  }
}
</script>

<style scoped>
.payment-result-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 40px 20px;
}

.result-container {
  text-align: center;
  max-width: 360px;
  width: 100%;
}

.result-icon {
  font-size: 72px;
  margin-bottom: 16px;
}

.result-icon.success {
  color: #67c23a;
}

.result-icon.fail {
  color: #f56c6c;
}

.result-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 12px;
  color: #1a1a1a;
}

.result-amount {
  font-size: 18px;
  color: #444;
  margin-bottom: 32px;
}

.result-amount strong {
  color: #3d7aed;
}

.result-message {
  font-size: 14px;
  color: #666;
  margin-bottom: 32px;
  word-break: break-all;
}

.result-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.submit-button {
  width: 100%;
  padding: 14px;
  background: #3d7aed;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}

.cancel-button {
  width: 100%;
  padding: 14px;
  background: #f5f5f5;
  color: #444;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}
</style>
