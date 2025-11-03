<template>
  <div>
    <div class="diary2-2-5">
      <div class="diary2-2-5__top">
        <router-link :to="{ name: 'FoodRecent' }" class="diary225__link__a" alt="최근순">최근순</router-link>
        <router-link :to="{ name: 'FoodFrequency' }" class="diary225__link__b" alt="빈도순">빈도순</router-link>
      </div>

      <div class="diary2-2-5__mid">
        <el-radio-group class="search fr" v-model="search">
          <el-radio :label="1"><span class="fir">1</span>떡볶이</el-radio>
          <el-radio :label="2"><span class="sec">2</span>떡볶이 떡</el-radio>
          <el-radio :label="3"><span class="thi">3</span>열떡볶이면(오뚜기)</el-radio>
          <el-radio :label="4"><span class="fir">4</span>쌀떡볶이면(오뚜기)</el-radio>
          <el-radio :label="5"><span class="sec">5</span>해물 떡볶이</el-radio>
        </el-radio-group>
      </div>

      <div class="diary2-2-2__bot">
        <div class="diary__b__bot">
          <el-button @click="handleVisibleSelect()">등록하기</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="" width="360px" :visible.sync="selectModalVisible">
      <foodSelect v-if="selectModalVisible" @close="handleVisibleSelect()"></foodSelect>
    </el-dialog>
  </div>
</template>

<script>
import foodSelect from '@/views/diary/components/foodSelect.vue';

export default {
  components: {
    foodSelect,
  },
  data() {
    return {
      search: '',
      selectModalVisible: false,
    };
  },
  methods: {
    handleVisibleSelect() {
      this.selectModalVisible = !this.selectModalVisible;
    },
  },
};
</script>
