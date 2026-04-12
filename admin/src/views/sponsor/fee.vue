<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">구단주 금액 설정</p>
        <p class="usernumber">구단주 신청 시 결제 금액을 설정합니다</p>
      </div>
    </div>

    <el-card style="max-width: 480px; margin-top: 16px" shadow="never">
      <el-form ref="feeForm" :model="form" :rules="rules" label-width="160px" v-loading="loading">
        <el-form-item label="구단주 신청 금액" prop="amount">
          <el-input v-model.number="form.amount" type="number" min="0" step="1000">
            <template slot="append">원</template>
          </el-input>
          <div class="input-tip">사용자가 마이페이지 → 구단 후원 신청에서 결제할 금액입니다.</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">저장</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getSponsorFee, updateSponsorFee } from '@/api/sponsor';
import { ElForm } from 'element-ui/types/form';

@Component({ name: 'SponsorFee' })
export default class extends Vue {
  private loading = false;

  private saving = false;

  private form = { amount: 0 };

  private rules = {
    amount: [
      { required: true, message: '금액을 입력하세요', trigger: 'blur' },
      {
 type: 'number', min: 0, message: '0 이상의 숫자를 입력하세요', trigger: 'blur',
},
    ],
  };

  async created() {
    this.loading = true;
    try {
      const res = await getSponsorFee();
      this.form.amount = res.data?.amount ?? 0;
    } catch {
      // 백엔드 미구현 시 무시
    } finally {
      this.loading = false;
    }
  }

  handleSave() {
    (this.$refs.feeForm as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.saving = true;
      try {
        await updateSponsorFee({ amount: this.form.amount });
        this.$message.success('저장되었습니다.');
      } catch {
        this.$message.error('저장 중 오류가 발생했습니다.');
      } finally {
        this.saving = false;
      }
    });
  }
}
</script>

<style scoped>
.input-tip {
  font-size: 12px;
  color: #888;
  margin-top: 4px;
}
</style>
