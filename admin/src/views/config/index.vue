<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">전체설정</p>
        <p class="usernumber">서비스 전반에 적용되는 설정을 관리합니다</p>
      </div>
    </div>

    <el-card style="max-width: 520px; margin-top: 16px" shadow="never">
      <div slot="header" style="font-weight: 600;">매치 설정</div>
      <el-form
        ref="matchConfigForm"
        :model="matchForm"
        :rules="matchRules"
        label-width="200px"
        v-loading="loading"
      >
        <el-form-item label="매치 취소 가능 기준일" prop="cancelDaysBeforeMatch">
          <el-input
            v-model.number="matchForm.cancelDaysBeforeMatch"
            type="number"
            :min="0"
            style="width: 160px;"
          >
            <template slot="append">일 전까지</template>
          </el-input>
          <div class="input-tip">멤버가 경기 N일 전까지 신청을 취소할 수 있습니다. (0 = 당일 취소 가능)</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSaveMatchConfig">저장</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getMatchConfig, updateMatchConfig } from '@/api/match';
import { ElForm } from 'element-ui/types/form';

@Component({ name: 'GlobalConfig' })
export default class extends Vue {
  private loading = false;

  private saving = false;

  private matchForm = { cancelDaysBeforeMatch: 1 };

  private matchRules = {
    cancelDaysBeforeMatch: [
      { required: true, message: '값을 입력하세요', trigger: 'blur' },
      {
        type: 'number', min: 0, message: '0 이상의 숫자를 입력하세요', trigger: 'blur',
      },
    ],
  };

  async created() {
    this.loading = true;
    try {
      const res = await getMatchConfig();
      this.matchForm.cancelDaysBeforeMatch = res.data?.cancelDaysBeforeMatch ?? 1;
    } catch {
      // 초기값 유지
    } finally {
      this.loading = false;
    }
  }

  handleSaveMatchConfig() {
    (this.$refs.matchConfigForm as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.saving = true;
      try {
        await updateMatchConfig({ cancelDaysBeforeMatch: this.matchForm.cancelDaysBeforeMatch });
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
