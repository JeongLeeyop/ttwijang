<template>
  <div v-loading="loading">
    <div class="diary-content__b">
      <el-form ref="form" :model="form" :rules="rules" onsubmit="return false;">
        <div class="diary__b__top">
          <p class="ti">목표 날짜</p>
            <div class="input__box" style="width:180px;position: relative;left: 125px;">
              <el-form-item class="date" prop="goalDate">
                <el-date-picker type="date" placeholder="" style="width:180px;" class="ca" v-model="form.goalDate" value-format="yyyy-MM-dd"></el-date-picker>
              </el-form-item>
            </div>
          <p class="ti">목표 체중</p>
          <div class="input__box">
            <el-form-item prop="purposeWeight">
              <label for="">Kg</label>
              <el-input-number :controls="false" id="" class="b1" v-model="form.purposeWeight" :min="0" />
              </el-form-item>
          </div>
          <p class="ti">목표 골격근량</p>
          <div class="input__box">
            <el-form-item prop="purposeMuscleWeight">
              <label for="">Kg</label>
              <el-input-number :controls="false" id="" class="b1" v-model="form.purposeMuscleWeight" :min="0" />
            </el-form-item>
          </div>
          <p class="ti">목표 체지방율</p>
          <div class="input__box">
            <el-form-item prop="purposeFatRate">
              <label for="">%</label>
              <el-input-number :controls="false" id="" class="b2" v-model="form.purposeFatRate" :min="0" :max="100" />
            </el-form-item>
          </div>
        </div>
      </el-form>

      <div class="diary__b__bot">
        <a href="#" class="agree" @click="handleSave()">기록하기</a>
      </div>
    </div>
  </div>
</template>
<style>
.el-picker-panel.el-date-picker.el-popper{
  top: 155px !important;
  left: 60px !important;
}
.diary__b__top .el-form-item__error {
  left: 160px;
}

.diary__b__top .date .el-form-item__error {
  left: 43px;
}

.diary__b__top .el-input__inner{
  text-align: center;
}
</style>

<script lang="ts">
import { getPhysicalCondition, savePurposePhysical } from '@/api/diary';
import { Form } from 'element-ui';
import { Vue, Component, Watch } from 'vue-property-decorator';

@Component({
  components: {
  },
})

export default class extends Vue {
  mounted() {
    this.getPhysicalCondition();
  }

  private form: any = {
    purposeMuscleWeight: 0,
    purposeFatRate: 0,
    weight: this.$route.query.weight,
    purposeWeight: this.$route.query.goalWeight,
    goalDate: this.$route.query.goalDate,
    diaryDate: '',
  }

  private loading = false;

  private rules = {
    goalDate: [
      { required: true, message: '목표 날짜를 선택하세요.', trigger: ['blur', 'change'] },
    ],
    purposeWeight: [
      { required: true, message: '목표 체중을 입력하세요.', trigger: ['blur', 'change'] },
    ],
    purposeMuscleWeight: [
      { required: true, message: '목표 골격근량을 입력하세요.', trigger: ['blur', 'change'] },
    ],
    purposeFatRate: [
      { required: true, message: '목표 체지방율을 입력하세요.', trigger: ['blur', 'change'] },
    ],
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

  private handleSave() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        this.form.diaryDate = this.$route.query.diaryDate;
        savePurposePhysical(this.form).then(() => {
          this.$message.success('체성분 목표가 설정되었습니다.');
          this.$router.push({ name: 'Diary', query: { ...this.$route.query, tab: 'body' } });
        });
      }
    });
  }
}
</script>
