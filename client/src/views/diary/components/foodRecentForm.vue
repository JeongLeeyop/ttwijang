<template>
  <div class="diary2-2-3">
    <div class="diary2-2-3__top">
      <el-button>
        <img src="~@/assets/images/cancel-gray.png" class="cancel_img" alt="닫기" @click="$emit('close')">
      </el-button>
    </div>
    <div class="diary2-2-3__mid">
      <p class="ti">{{ meal.menuName }}</p>
      <p class="tti">영양정보</p>
      <div class="diary2-2-3__mid__a">
        <div class="diary2-2-3__mid__ab">
          <p>칼로리(Kcal)</p>
          <p>탄수화물(g)</p>
          <p>단백질(g)</p>
          <p>지방(g)</p>
          <p>나트륨(mg)</p>
          <p>당(g)</p>
          <p>포화지방산(g)</p>
          <p>트랜스지방(g)</p>
          <p>콜레스테롤(mg)</p>
          <p>칼슘(mg)</p>
          <p>철(mg)</p>
          <p>칼륨(mg)</p>
          <p>식이섬유(g)</p>
          <p>비타민C(mg)</p>
        </div>
        <div class="diary2-2-3__mid__abc">
          <p>{{ this.valueFromWeight(meal.calorie) }}</p>
          <p>{{ this.valueFromWeight(meal.carbohydrate) }}</p>
          <p>{{ this.valueFromWeight(meal.protein) }}</p>
          <p>{{ this.valueFromWeight(meal.fat) }}</p>
          <p>{{ this.valueFromWeight(meal.natrium) }}</p>
          <p>{{ this.valueFromWeight(meal.sugar) }}</p>
          <p>{{ this.valueFromWeight(meal.stFattyAcid) }}</p>
          <p>{{ this.valueFromWeight(meal.transFat) }}</p>
          <p>{{ this.valueFromWeight(meal.cholesterol) }}</p>
          <p>{{ this.valueFromWeight(meal.calcium) }}</p>
          <p>{{ this.valueFromWeight(meal.iron) }}</p>
          <p>{{ this.valueFromWeight(meal.potassium) }}</p>
          <p>{{ this.valueFromWeight(meal.dietaryFiber) }}</p>
          <p>{{ this.valueFromWeight(meal.vitaminC) }}</p>
        </div>
      </div>

      <div class="diary2-2-3__mid__abcd">
        <el-input-number v-model="form.weight" :min="1"></el-input-number>
      </div>
    </div>

    <div class="diary2-2-3__bot a">
      <el-button @click="handleAddDiaryMeal()">등록</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { addDiaryMealByHistory } from '@/api/diary';

@Component({
})
export default class extends Vue {
  @Prop({ required: true }) private meal!: any;

  mounted() {
    this.form = {
      weight: this.meal.amount,
      mealIdx: this.meal.idx,
      type: this.$route.query.type,
      diaryDate: this.$route.query.diaryDate,
    };
  }

  private form = {
    weight: 0,
    mealIdx: this.meal.idx,
    type: this.$route.query.type,
    diaryDate: this.$route.query.diaryDate,
  }

  /* eslint-disable */
  private valueFromWeight(val: any) {
    if (!val || val === 0) return 0;
    const v1 = val / this.meal.amount;
    return (v1 * this.form.weight).toFixed(2);
  }
  /* eslint-enable */

  private handleAddDiaryMeal() {
    this.form = {
      ...this.form,
      mealIdx: this.meal.idx,
      type: this.$route.query.type,
      diaryDate: this.$route.query.diaryDate,
    };
    addDiaryMealByHistory(this.form).then(() => {
      this.$message.success('다이어리에 추가되었습니다.');
      this.$emit('success');
    });
  }
}
</script>
