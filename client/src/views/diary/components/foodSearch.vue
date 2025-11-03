<template>
  <div v-loading="loading" class="diary2-2-2-wrap">
    <div class="diary2-2-2">
      <div class="diary2-2-2__top">
        <label for=""></label>
        <el-input placeholder="메뉴를 검색하세요" type="text" id="" v-model="listQuery.searchValue" @keyup.enter.native="handleSearch()">
          <a class="search" slot="suffix" @click="handleSearch"></a>
        </el-input>
      </div>
      <div v-loading="loading" class="diary2-2-2__mid">
        <slot v-if="allFoodList.length > 0">
          <label class="item" v-for="(food, index) in allFoodList" :key="index">
            <p>{{ food.name }} {{ food.weightOnce }}{{ food.weightType }}({{ food.kcal }}kcal)</p>
            <el-button size="mini" circle icon="el-icon-plus" @click="handleVisibleForm(food)" />
          </label>
        </slot>
        <slot v-else>
          <div style="padding:80px 0">조회된 음식이 존재하지 않습니다.</div>
        </slot>
      </div>
      <Pagination style="padding:3px 16px;min-height:100px;"
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getLastDiaryMealList"
      />
    </div>
    <el-dialog title="" width="360px" :visible.sync="formVisible">
      <FoodDiaryForm
        v-if="formVisible"
        :food="selectedFood"
        @success="handleSuccessForm()"
        @close="handleCloseForm()"
      />
    </el-dialog>
  </div>
</template>

<style>
  .el-pager li {
    font-size: 19px;
    min-width: 50.5px;
  }
</style>

<script lang="ts">
import { getFoodList } from '@/api/food';
import { Vue, Component } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';
import FoodDiaryForm from './foodDiaryForm.vue';

@Component({
  components: {
    FoodDiaryForm,
    Pagination,
  },
})
export default class extends Vue {
  private listQuery = {
    page: 1,
    size: 10,
    searchValue: '',
  };

  private totalElements = 0;

  private allFoodList: any[] = [];

  private foodList: any = [];

  private loading = true;

  private selectedFood = null;

  private formVisible = false;

  private mounted() {
    this.getLastDiaryMealList();
    // this.addScrollListener();
  }

  private async getFoodList() {
  try {
    const response = await getFoodList(this.listQuery);
    this.allFoodList = response.data.content;
    // this.allFoodList = this.allFoodList.concat(response.data.content);
  } catch (error) {
    console.error(error);
  } finally {
    this.loading = false;
  }
}

private getLastDiaryMealList() {
    this.loading = true;
    // this.selectedIdx = null;
    this.selectedFood = null;
    getFoodList(this.listQuery).then((res) => {
      this.loading = false;
      this.allFoodList = res.data.content;
      this.totalElements = res.data.totalElements;
    });
  }

  private handleSearch() {
    this.listQuery.page = 1;
    this.allFoodList = [];
    this.getLastDiaryMealList();
  }

  private handleVisibleForm(food: any) {
    this.formVisible = true;
    this.selectedFood = food;
  }

  private handleCloseForm() {
    this.selectedFood = null;
    this.formVisible = false;
  }

  private handleSuccessForm() {
    this.formVisible = false;
    this.$router.push({ name: 'Diary', query: { ...this.$route.query } });
  }

  private addScrollListener() {
    window.addEventListener('scroll', this.handleScroll);
  }

  private removeScrollListener() {
    window.removeEventListener('scroll', this.handleScroll);
  }

  private handleScroll() {
    const scrollHeight = document.documentElement.scrollHeight;
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    const clientHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;

    if (scrollHeight - scrollTop <= clientHeight) {
      this.listQuery.page += 1;
      this.getFoodList();
    }
  }

  private beforeDestroy() {
    // this.removeScrollListener();
  }
}
</script>
