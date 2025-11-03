<template>
  <div v-loading="loading">
    <div class="diary2-2-4">
      <!--
      <div class="diary2-2-4__top">
        <router-link :to="{ name: 'BodyComposition' }" class="diary224__link__a" alt="최근순">최근순</router-link>
        <router-link :to="{ name: 'FoodFrequency' }" class="diary224__link__b" alt="빈도순">빈도순</router-link>
      </div>
      <div class="diary2-2-4__mid">
        <div class="select-box__month">
          <el-select placeholder="3개월" id="" :popper-append-to-body="false" v-model="value">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
      </div>
      -->

      <div class="diary2-2-2__mid">
        <el-radio-group class="search" v-model="selectedIdx">
          <el-radio
            v-for="(meal, index) in historyList"
            :label="index"
            :key="index"
          >
          {{ meal.menuName }} {{ meal.amount }}{{ meal.amountUnit }} ({{ meal.calorie }}kcal)
          </el-radio>
        </el-radio-group>
      </div>

      <Pagination
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getLastDiaryMealList"
      />

      <div class="diary2-2-4__bot">
        <!--
        <div class="diary2-2-4__mid__a">
          <p class="ti">MORE</p>
        </div>
        -->

        <div class="diary__b__bot">
          <el-button @click="handleSubmit()">등록하기</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="" :visible.sync="formVisible" width="90%">
      <FoodRecentForm
        v-if="formVisible"
        :meal="selectedItem"
        @success="handleSuccessForm()"
        @close="handleCloseForm()"
      />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { getLastDiaryMealList } from '@/api/diaryMeal';
import { Component, Vue } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';
import FoodRecentForm from './foodRecentForm.vue';

@Component({
  components: {
    FoodRecentForm,
    Pagination,
  },
})
export default class extends Vue {
  mounted() {
    this.getLastDiaryMealList();
  }

  private loading = true;

  private listQuery = {
    page: 0,
    size: 10,
  }

  private totalElements = 0;

  private historyList: any = [];

  private selectedIdx: any = null;

  private selectedItem: any = null;

  private formVisible = false;

  private getLastDiaryMealList() {
    this.loading = true;
    this.selectedIdx = null;
    this.selectedItem = null;

    getLastDiaryMealList(this.listQuery).then((res) => {
      this.loading = false;
      this.historyList = res.data.content;
      this.totalElements = res.data.totalElements;
    });
  }

  /* eslint-disable */
  private handleSubmit() {
    if (this.selectedIdx == null) {
      this.$message.warning('등록할 최근 기록을 선택해주세요.');
      return false;
    }
    this.selectedItem = this.historyList[this.selectedIdx];
    this.formVisible = !this.formVisible;
  }

  private handleCloseForm() {
    this.selectedIdx = null;
    this.selectedItem = null;
    this.formVisible = false;
  }

  private handleSuccessForm() {
    this.handleCloseForm();
    this.$router.push({ name: 'Diary', query: { ...this.$route.query } });
    // this.getLastDiaryMealList();
  }
}
</script>
