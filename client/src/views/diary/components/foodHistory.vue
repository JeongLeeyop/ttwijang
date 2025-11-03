<template>
  <div v-loading="loading">
    <div class="diary2-2-7">
      <!--
      <div class="diary2-2-7__mid">
        <div class="select-box__month all">
          <el-select placeholder="3개월" id="" :popper-append-to-body="false" v-model="value">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
      </div>
      -->
      <div class="diary2-2-2__mid">
        <el-radio-group class="search" v-model="selectedIdx">
          <el-radio
            v-for="(order, index) in orderList"
            :label="index"
            :key="index"
          >
            {{ order.productName }}({{ order.calorie }}kcal)
          </el-radio>
        </el-radio-group>
      </div>

      <Pagination
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getOrderList()"
      />

      <div class="diary2-2-7__bot">
        <div class="diary__b__bot">
          <el-button @click="handleSubmit()">등록하기</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="" width="90%" :visible.sync="formVisible">
      <FoodHistoryForm
        v-if="formVisible"
        :order="selectedItem"
        @success="handleSuccessForm()"
        @close="handleCloseForm()"
      />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getOrderList } from '@/api/order';
import Pagination from '@/components/Pagination/index.vue';
import FoodHistoryForm from './foodHistoryForm.vue';

@Component({
  components: {
    Pagination,
    FoodHistoryForm,
  },
})
export default class extends Vue {
  mounted() {
    this.getOrderList();
  }

  private loading = true;

  private listQuery = {
    page: 0,
    size: 10,
  }

  private totalElements = 0;

  private orderList: any = [];

  private selectedIdx: any = null;

  private selectedItem: any = null;

  private formVisible = false;

  private getOrderList() {
    this.loading = true;
    this.selectedIdx = null;
    this.selectedItem = null;
    getOrderList(this.listQuery).then((res) => {
      this.loading = false;
      this.orderList = res.data.content;
      this.totalElements = res.data.totalElements;
    });
  }

  /* eslint-disable */
  private handleSubmit() {
    if (this.selectedIdx == null) {
      this.$message.warning('다이어리에 등록할 주문내역을 선택해주세요.');
      return false;
    }
    this.selectedItem = this.orderList[this.selectedIdx];
    this.formVisible = !this.formVisible;
  }

  private handleCloseForm() {
    this.selectedIdx = null;
    this.selectedItem = null;
    this.formVisible = false;
  }

  private handleSuccessForm() {
    this.handleCloseForm();
    this.getOrderList();
  }
};
</script>
