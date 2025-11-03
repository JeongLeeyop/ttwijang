<template>
  <div class="diary2-2-3">
    <div class="diary2-2-3__top">
      <el-button>
        <img src="~@/assets/images/cancel-gray.png" class="cancel_img" alt="닫기" @click="$emit('close')">
      </el-button>
    </div>
    <div class="diary2-2-3__mid">
      <p class="ti">{{ food.name }}</p>
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
          <p>{{ this.valueFromWeight(food.kcal) }}</p>
          <p>{{ this.valueFromWeight(food.carbohydrate) }}</p>
          <p>{{ this.valueFromWeight(food.protein) }}</p>
          <p>{{ this.valueFromWeight(food.fat) }}</p>
          <p>{{ this.valueFromWeight(food.natrium) }}</p>
          <p>{{ this.valueFromWeight(food.sugar) }}</p>
          <p>{{ this.valueFromWeight(food.stFattyAcid) }}</p>
          <p>{{ this.valueFromWeight(food.transFat) }}</p>
          <p>{{ this.valueFromWeight(food.cholesterol) }}</p>
          <p>{{ this.valueFromWeight(food.calcium) }}</p>
          <p v-if="food.iron === 0">{{ this.valueFromWeight(food.iron2) }}</p>
          <p>{{ this.valueFromWeight(food.potassium) }}</p>
          <p>{{ this.valueFromWeight(food.dietaryFiber) }}</p>
          <p>{{ this.valueFromWeight(food.vitaminC) }}</p>
        </div>
      </div>

      <p class="tti">출처</p>
      <p class="ttti">{{ food.ref }}</p>

      <div class="diary2-2-3__mid__abcd">
        <el-input-number v-model="form.weight" :min="1"></el-input-number>
        <p class="tttti">1회 제공량 {{ food.weightOnce}}{{ food.weightType}}({{ food.kcal }}kcal)</p>
      </div>
    </div>

    <div class="diary2-2-3__bot a">
      <el-button @click="handleAddDiaryMeal()">등록</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { addDiaryMealByFood } from '@/api/diary';

@Component({
  components: {
  },
})
export default class extends Vue {
  @Prop({ required: true }) private food!: any;

  mounted() {
    this.form = {
      weight: this.food.weightOnce,
      foodIdx: this.food.idx,
    };
  }

  private form: any = {
    foodIdx: null,
    weight: 0,
    type: this.$route.query.type,
    diaryDate: this.$route.query.diaryDate,
  }

  /* eslint-disable */
  private valueFromWeight(val: any) {
    if (!val || val === 0) return 0;
    const v1 = val / this.food.weightOnce;
    return (v1 * this.form.weight).toFixed(2);
  }
  /* eslint-enable */

  private handleAddDiaryMeal() {
    this.form = {
      ...this.form,
      foodIdx: this.food.idx,
      type: this.$route.query.type,
      diaryDate: this.$route.query.diaryDate,
    };
    addDiaryMealByFood(this.form).then(() => {
      this.$message.success('다이어리에 추가되었습니다.');
      this.$emit('success');
    });
  }
}

</script>
