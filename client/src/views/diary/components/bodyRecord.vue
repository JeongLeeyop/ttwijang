<template>
  <div>
    <div v-loading="loading" class="diary-content__b">
      <el-form ref="form" :model="form" :rules="rules" onsubmit="return false">
        <div class="diary__b__top">
          <p class="ti">체중</p>
          <div class="input__box">
            <el-form-item prop="weight">
              <label for="weight">Kg</label>
              <el-input type="text" id="" class="b1" v-model="form.weight" />
            </el-form-item>
          </div>
          <p class="ti">골격근량</p>
          <div class="input__box">
            <el-form-item prop="muscleWeight">
              <label for="muscle_rate">Kg</label>
              <el-input type="text" id="" class="b1" v-model="form.muscleWeight" />
            </el-form-item>
          </div>
          <p class="ti">체지방율</p>
          <div class="input__box">
            <el-form-item prop="fatRate">
              <label for="fat_rate">%</label>
              <el-input type="text" id="" class="b2" v-model="form.fatRate" />
            </el-form-item>
          </div>
        </div>
      </el-form>

      <div class="diary__b__bot">
        <button @click="handleSavePhysicalCondition()" class="agree">기록하기</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getPhysicalCondition, savePhysicalCondition } from '@/api/diary';
import { Form } from 'element-ui';
import { Component, Vue } from 'vue-property-decorator';

@Component({
})

export default class extends Vue {
  mounted() {
    this.getPhysicalCondition();
  }

  private loading = true;

  private form: any = {
    muscleWeight: 0,
    fatRate: 0,
    weight: this.$route.query.weight,
    purposeWeight: this.$route.query.goalWeight,
    goalDate: this.$route.query.goalDate,
    diaryDate: '',
  }

  private rules = {
    weight: [
      { required: true, message: '체중을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    muscleWeight: [
      { required: true, message: '골격근량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    fatRate: [
      { required: true, message: '체지방율을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
  }

  private handleSavePhysicalCondition() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        this.form.diaryDate = this.$route.query.diaryDate;
        savePhysicalCondition(this.form).then(() => {
          this.$message.success('체성분 상태가 기록되었습니다.');
          this.$router.push({ name: 'Diary', query: { ...this.$route.query, tab: 'body' } });
        });
      }
    });
  }

  private getPhysicalCondition() {
    if (this.$route.query.diaryDate) {
      this.loading = true;
      getPhysicalCondition({ searchDate: this.$route.query.diaryDate }).then((res) => {
        // this.form = res.data;
        this.form = {
          ...this.form,
          idx: res.data.idx,
          fatRate: res.data.fatRate,
          muscleWeight: res.data.muscleWeight,
          purposeFatRate: res.data.purposeFatRate,
          purposeMuscleWeight: res.data.purposeMuscleWeight,
        };
        this.loading = false;
      });
    } else {
      this.$message.warning('잘못된 접근입니다.');
      this.$router.push({ name: 'Diary' });
    }
  }
}
</script>
