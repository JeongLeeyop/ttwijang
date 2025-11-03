<template>
  <div>
    <div class="home40__wr">
      <div class="diary2-2-4__mid">
        <!--
        <div class="select-box__month">
          <el-select placeholder="3개월" id="" :popper-append-to-body="false" v-model="value">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
        -->
      </div>

      <div class="home40__mid__wr">
        <slot v-if="historyList.length > 0">
          <div v-for="(history, index) in historyList" :key="index" class="home40__mid">
            <p class="mida"><span class="ti">{{ history.reason }}</span>
              <span class="tti">{{ history.createDate | parseDate('YYYY-MM-DD') }}</span>
            </p>
            <p class="midb" :class="history.point < 0 ? 'color2' : ''">{{ history.point > 0 ? '+' : '' }}{{ history.point | parseKrw }}</p>
          </div>
        </slot>
        <slot v-else>
          <div>조회된 내역이 없습니다.</div>
        </slot>
      </div>
      <Pagination
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getHistoryList"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { getPointHistoryList } from '@/api/pointHistory';
import Pagination from '@/components/Pagination/index.vue';

@Component({
  components: {
    Pagination,
  },
})
export default class extends Vue {
  mounted() {
    this.getHistoryList();
  }

  private loading = true;

  private historyList: any = [];

  private listQuery = {
    page: 1,
    size: 10,
  }

  private totalElements = 0;

  private getHistoryList() {
    this.loading = true;
    getPointHistoryList(this.listQuery).then((res) => {
      this.historyList = res.data.content;
      this.totalElements = res.data.totalElements;
      this.loading = false;
    });
  }
}
</script>
