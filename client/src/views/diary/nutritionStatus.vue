<template>
  <div>
    <div v-loading="loading" class="diary2-3__wr">
      <el-tabs v-model="activeTab">
        <el-tab-pane name="0" label="전체">
          <MealStatistics :data="data" />
        </el-tab-pane>
        <el-tab-pane name="1" label="아침">
          <MealStatistics :data="data" />
        </el-tab-pane>
        <el-tab-pane name="2" label="점심">
          <MealStatistics :data="data" />
        </el-tab-pane>
        <el-tab-pane name="3" label="저녁">
          <MealStatistics :data="data" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script lang="ts">
import { getDiaryMealStatistics } from '@/api/diary';
import {
  Vue,
  Component,
  Watch,
  Prop,
} from 'vue-property-decorator';
import MealStatistics from './components/mealStatistics.vue';

@Component({
  components: {
    MealStatistics,
  },
})

export default class extends Vue {
  @Prop({ required: true }) diaryDate!: any;

  @Watch('activeTab')
  private handleChangeActiveTab() {
    this.listQuery.type = Number(this.activeTab);
    this.getDiaryMealStatistics();
  }

  mounted() {
    this.getDiaryMealStatistics();
  }

  @Watch('diaryDate')
  private handleChangeDiaryDate() {
    this.listQuery.diaryDate = this.diaryDate;
    this.getDiaryMealStatistics();
  }

  private listQuery = {
    diaryDate: this.diaryDate,
    type: 0,
  }

  private activeTab = '0';

  private loading = true;

  private data = {
    amount: 0,
    calorie: 0,
    carbohydrate: 0, // 탄수화물(g)
    protein: 0, // 단백질(g)
    fat: 0, // 지방(g)
    sodium: 0, // 나트륨(mg)
    sugar: 0, // 당(g)
    saturatedFattyAcids: 0, // 포화지방(g)
    vitaminC: 0, // 비타민c(mg)
    transFat: 0, // 트랜스지방(mg)
    cholesterol: 0, // 콜레스테롤(mg)
    calcium: 0, // 칼슘(mg)
    iron: 0, // 철(mg)
    potassium: 0, // 컬륨(mg)
    dietaryFiber: 0, // 식이섬유(g)
  }

  private getDiaryMealStatistics() {
    this.loading = true;
    getDiaryMealStatistics(this.listQuery).then((res) => {
      console.log(res.data);
      this.data = res.data;
      this.loading = false;
    });
  }
}
</script>
