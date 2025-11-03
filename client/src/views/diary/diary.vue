<template>
  <div>
    <div v-loading="loading" class="diary2-2__mid__wr">
      <div class="diary2-2__mid">
        <div @click="handleAddDiaryMeal(1)" class="diary2-2__mid__a1" :class="{be1: diaryData.breakfast.mealList.length === 0}">

          <el-button>
            <div class="diary2-2__mid__b1">
              <p class="ti">아침</p>
              <p class="tti">{{ diaryData.breakfast.totalCalorie | parseKrw }} Kcal</p>
            </div>
          </el-button>

          <div v-if="diaryData.breakfast.mealList.length > 0" @click.stop class="diary2-2__mid__ulm">
            <ul class="image">
              <li v-for="meal in diaryData.breakfast.mealList" :key="meal.idx" @click="handleDeleteDiaryMeal(meal, '아침')">
                <p class="ttti">{{ meal.menuName}} {{ meal.amount | parseKrw }}{{ meal.amountUnit }}({{ meal.calorie | parseKrw }}kcal)</p>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="diary2-2__mid">
        <div @click="handleAddDiaryMeal(2)" class="diary2-2__mid__a1" :class="{be1: diaryData.lunch.mealList.length === 0}">
          <el-button>
            <div class="diary2-2__mid__b1 color2">
              <p class="ti">점심</p>
              <p class="tti">{{ diaryData.lunch.totalCalorie | parseKrw }} Kcal</p>
            </div>
          </el-button>

          <div v-if="diaryData.lunch.mealList.length > 0" @click.stop class="diary2-2__mid__ulm">
            <ul class="image">
              <li v-for="meal in diaryData.lunch.mealList" :key="meal.idx" @click="handleDeleteDiaryMeal(meal, '점심')">
                <p class="ttti">{{ meal.menuName}} {{ meal.amount | parseKrw }}{{ meal.amountUnit }}({{ meal.calorie | parseKrw }}kcal)</p>
              </li>
            </ul>
          </div>

        </div>
      </div>

      <div class="diary2-2__mid">
        <div @click="handleAddDiaryMeal(3)" class="diary2-2__mid__a1" :class="{be1: diaryData.dinner.mealList.length === 0}">
          <el-button>
            <div class="diary2-2__mid__b1 color3">
              <p class="ti">저녁</p>
              <p class="tti">{{ diaryData.dinner.totalCalorie | parseKrw }} Kcal</p>
            </div>
          </el-button>
          <div v-if="diaryData.dinner.mealList.length > 0" @click.stop class="diary2-2__mid__ulm">
            <ul class="image">
              <li v-for="meal in diaryData.dinner.mealList" :key="meal.idx" @click="handleDeleteDiaryMeal(meal, '저녁')">
                <p class="ttti">{{ meal.menuName}} {{ meal.amount | parseKrw }}{{ meal.amountUnit }}({{ meal.calorie | parseKrw }}kcal)</p>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="" :visible.sync="formVisible">
      <div class="diary2-2-1">
        <div class="diary2-2-1__a">
          <div class="diary2-2-3__top">
            <el-button  @click="handleAddDiaryMeal(0)">
              <img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
            </el-button>
          </div>

          <div class="diary2-2-1__mid">
            <p class="ti">어떻게<br>기록할까요?</p>
          </div>

          <div class="diary2-2-1__bot foodSelect">
            <el-button class="top">
              <router-link :to="{ name:'FoodSearch', query: { diaryDate: diaryDate, type: type } }"> 음식검색 </router-link>
            </el-button>
            <el-button>
              <router-link :to="{ name:'FoodRecent', query: { diaryDate: diaryDate, type: type }  }">최근기록</router-link>
            </el-button>
            <el-button>
              <router-link :to="{ name:'FoodDirect', query: { diaryDate: diaryDate, type: type }  }">직접기록</router-link>
            </el-button>
            <el-button class="bottom">
              <router-link :to="{ name:'FoodHistory', query: { diaryDate: diaryDate, type: type }  }">주문내역</router-link>
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { deleteDiaryMeal, getDiaryMealList } from '@/api/diary';
import router from '@/router';
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';

@Component({
})

export default class extends Vue {
  @Prop({ required: true }) private diaryDate!: any;

  @Watch('diaryDate')
  private handleChangeDiaryDate() {
    this.getDiaryMealList();
  }

  mounted() {
    this.getDiaryMealList();
  }

  private loading = true;

  private diaryData: any = {
    breakfast: {
      totalCalorie: 0,
      mealList: [],
    },
    lunch: {
      totalCalorie: 0,
      mealList: [],
    },
    dinner: {
      totalCalorie: 0,
      mealList: [],
    },
  }

  private type = 0;

  private formVisible = false;

  private getDiaryMealList() {
    getDiaryMealList({ searchDate: this.diaryDate }).then((res) => {
      this.diaryData = res.data;
      this.loading = false;
    });
  }

  private handleAddDiaryMeal(value: number) {
    this.type = value;
    this.formVisible = !this.formVisible;
  }

  private handleDeleteDiaryMeal(meal: any, type: string) {
    this.$confirm(`${type}식단 ${meal.menuName}(${meal.calorie}kcal)을 제거하시겠습니까?`, {
      confirmButtonText: '제거',
      cancelButtonText: '취소',
      type: 'error',
    }).then(() => {
      deleteDiaryMeal(meal.idx).then(() => {
        this.$message.success('다이어리에서 식단이 제거되었습니다.');
        this.getDiaryMealList();
      });
    });
  }
}
</script>

<style>

</style>
