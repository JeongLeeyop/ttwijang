<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">지역 관리</p>
        <p class="usernumber">시/도 {{ sidoList.length }}개 지역</p>
      </div>
    </div>

    <el-table
      :data="sidoList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
      row-key="code"
      :expand-row-keys="expandedRows"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div class="sigungu-wrap" v-loading="scope.row._loading">
            <el-tag
              v-for="sg in (scope.row._sigungu || [])"
              :key="sg.code"
              size="small"
              style="margin: 3px"
              :type="sg.enabled ? '' : 'info'"
            >
              {{ sg.name }}
            </el-tag>
            <span v-if="!scope.row._sigungu">시/군/구 불러오는 중...</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="시/도 코드" prop="code" width="120" />
      <el-table-column label="지역명" prop="name" min-width="160" />
      <el-table-column label="레벨" prop="level" width="80" />
      <el-table-column label="활성화" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'info'" size="small">
            {{ scope.row.enabled ? '활성' : '비활성' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="관리" width="120">
        <template slot-scope="scope">
          <el-button size="mini" @click="loadSigungu(scope.row)">시/군/구 보기</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getSidoList, getSigunguList } from '@/api/region';

@Component({ name: 'RegionList' })
export default class extends Vue {
  private loading = false;
  private sidoList: any[] = [];
  private expandedRows: string[] = [];

  async created() {
    this.loading = true;
    try {
      const res = await getSidoList();
      this.sidoList = (res.data || []).map((r: any) => ({ ...r, _sigungu: null, _loading: false }));
    } finally {
      this.loading = false;
    }
  }

  async loadSigungu(row: any) {
    if (row._sigungu) {
      const idx = this.expandedRows.indexOf(row.code);
      if (idx >= 0) this.expandedRows.splice(idx, 1);
      else this.expandedRows.push(row.code);
      return;
    }
    row._loading = true;
    try {
      const res = await getSigunguList(row.code);
      row._sigungu = res.data || [];
      this.expandedRows.push(row.code);
    } finally {
      row._loading = false;
    }
  }
}
</script>

<style scoped>
.sigungu-wrap {
  padding: 12px 16px;
  background: #f8f9fa;
  min-height: 48px;
}
</style>
