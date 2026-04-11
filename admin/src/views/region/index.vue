<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">지역 관리</p>
        <p class="usernumber">시/도 {{ sidoList.length }}개 지역</p>
      </div>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openSidoDialog">시/도 추가</el-button>
    </div>

    <el-table
      :data="sidoList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
      row-key="code"
      :expand-row-keys="expandedRows"
      @expand-change="onExpandChange"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div class="sigungu-wrap" v-loading="scope.row._loading">
            <div class="sigungu-header">
              <span class="sigungu-label">시/군/구</span>
              <el-button size="mini" type="primary" icon="el-icon-plus" @click.stop="openSigunguDialog(scope.row)">시/군/구 추가</el-button>
            </div>
            <div class="sigungu-tags">
              <el-tag
                v-for="sg in (scope.row._sigungu || [])"
                :key="sg.code"
                size="small"
                style="margin: 3px"
                :type="sg.enabled ? '' : 'info'"
              >
                {{ sg.name }} ({{ sg.code }})
              </el-tag>
              <span v-if="scope.row._sigungu && scope.row._sigungu.length === 0" class="no-data">등록된 시/군/구가 없습니다</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="코드" prop="code" width="90" />
      <el-table-column label="지역명" prop="name" min-width="140" />
      <el-table-column label="활성화" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'info'" size="small">
            {{ scope.row.enabled ? '활성' : '비활성' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="관리" width="260">
        <template slot-scope="scope">
          <el-button size="mini" @click="toggleExpand(scope.row)">
            시/군/구 {{ expandedRows.includes(scope.row.code) ? '닫기' : '보기' }}
          </el-button>
          <el-button size="mini" type="info" @click="openLeagueDialog(scope.row)">리그 확인</el-button>
          <el-button size="mini" type="success" @click="openTeamDialog(scope.row)">팀 확인</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 시/도 추가 다이얼로그 -->
    <el-dialog title="시/도 추가" :visible.sync="sidoDialogVisible" width="400px">
      <el-form :model="sidoForm" label-width="100px" size="small">
        <el-form-item label="코드">
          <el-input v-model="sidoForm.code" placeholder="예: 48" />
        </el-form-item>
        <el-form-item label="지역명">
          <el-input v-model="sidoForm.name" placeholder="예: 경남" />
        </el-form-item>
        <el-form-item label="정렬 순서">
          <el-input-number v-model="sidoForm.sortOrder" :min="1" :max="99" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="sidoDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="submitSido">추가</el-button>
      </div>
    </el-dialog>

    <!-- 시/군/구 추가 다이얼로그 -->
    <el-dialog :title="`시/군/구 추가 — ${sigunguTargetName}`" :visible.sync="sigunguDialogVisible" width="400px">
      <el-form :model="sigunguForm" label-width="100px" size="small">
        <el-form-item label="코드">
          <el-input v-model="sigunguForm.code" placeholder="예: 4817" />
        </el-form-item>
        <el-form-item label="지역명">
          <el-input v-model="sigunguForm.name" placeholder="예: 진주시" />
        </el-form-item>
        <el-form-item label="정렬 순서">
          <el-input-number v-model="sigunguForm.sortOrder" :min="1" :max="99" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="sigunguDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="submitSigungu">추가</el-button>
      </div>
    </el-dialog>

    <!-- 리그 확인 다이얼로그 -->
    <el-dialog :title="`${detailRegionName} — 리그 목록`" :visible.sync="leagueDialogVisible" width="700px">
      <el-table :data="regionLeagues" v-loading="detailLoading" size="small" border>
        <el-table-column label="리그명" prop="name" min-width="160" />
        <el-table-column label="시/군/구" prop="regionSigungu" width="120" />
        <el-table-column label="시즌" prop="season" width="100" />
        <el-table-column label="팀 수" width="90">
          <template slot-scope="scope">{{ scope.row.currentTeams }}/{{ scope.row.maxTeams }}</template>
        </el-table-column>
        <el-table-column label="상태" width="110">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="!detailLoading && regionLeagues.length === 0" class="empty-msg">등록된 리그가 없습니다</div>
    </el-dialog>

    <!-- 팀 확인 다이얼로그 -->
    <el-dialog :title="`${detailRegionName} — 팀 목록`" :visible.sync="teamDialogVisible" width="700px">
      <el-table :data="regionTeams" v-loading="detailLoading" size="small" border>
        <el-table-column label="팀명" prop="name" min-width="160" />
        <el-table-column label="시/군/구" prop="regionSigungu" width="120" />
        <el-table-column label="팀원 수" prop="memberCount" width="90" />
        <el-table-column label="상태" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
              {{ scope.row.status === 'ACTIVE' ? '활성' : scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="!detailLoading && regionTeams.length === 0" class="empty-msg">등록된 팀이 없습니다</div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  getSidoList, getSigunguList,
  createSido, createSigungu,
  getRegionLeagues, getRegionTeams,
} from '@/api/region';

@Component({ name: 'RegionList' })
export default class extends Vue {
  private loading = false;
  private saving = false;
  private detailLoading = false;

  private sidoList: any[] = [];
  private expandedRows: string[] = [];

  // 시/도 추가
  private sidoDialogVisible = false;
  private sidoForm = { code: '', name: '', sortOrder: 99 };

  // 시/군/구 추가
  private sigunguDialogVisible = false;
  private sigunguTargetName = '';
  private sigunguForm = { code: '', name: '', parentCode: '', sortOrder: 99 };

  // 리그/팀 확인
  private leagueDialogVisible = false;
  private teamDialogVisible = false;
  private detailRegionName = '';
  private regionLeagues: any[] = [];
  private regionTeams: any[] = [];

  async created() {
    await this.loadSido();
  }

  async loadSido() {
    this.loading = true;
    try {
      const res = await getSidoList();
      this.sidoList = (res.data || []).map((r: any) => ({ ...r, _sigungu: null, _loading: false }));
    } finally {
      this.loading = false;
    }
  }

  async toggleExpand(row: any) {
    const idx = this.expandedRows.indexOf(row.code);
    if (idx >= 0) {
      this.expandedRows.splice(idx, 1);
    } else {
      if (!row._sigungu) {
        await this.loadSigungu(row);
      } else {
        this.expandedRows.push(row.code);
      }
    }
  }

  async onExpandChange(row: any, expanded: boolean) {
    if (expanded && !row._sigungu) {
      await this.loadSigungu(row);
    }
  }

  async loadSigungu(row: any) {
    row._loading = true;
    try {
      const res = await getSigunguList(row.code);
      row._sigungu = res.data || [];
      if (!this.expandedRows.includes(row.code)) {
        this.expandedRows.push(row.code);
      }
    } finally {
      row._loading = false;
    }
  }

  // ── 시/도 추가 ──
  openSidoDialog() {
    this.sidoForm = { code: '', name: '', sortOrder: 99 };
    this.sidoDialogVisible = true;
  }

  async submitSido() {
    if (!this.sidoForm.code || !this.sidoForm.name) {
      this.$message.warning('코드와 지역명을 입력하세요');
      return;
    }
    this.saving = true;
    try {
      await createSido(this.sidoForm);
      this.$message.success('시/도가 추가되었습니다');
      this.sidoDialogVisible = false;
      await this.loadSido();
    } catch (e: any) {
      this.$message.error(e?.response?.data?.message || '추가 실패');
    } finally {
      this.saving = false;
    }
  }

  // ── 시/군/구 추가 ──
  openSigunguDialog(row: any) {
    this.sigunguTargetName = row.name;
    this.sigunguForm = { code: '', name: '', parentCode: row.code, sortOrder: 99 };
    this.sigunguDialogVisible = true;
  }

  async submitSigungu() {
    if (!this.sigunguForm.code || !this.sigunguForm.name) {
      this.$message.warning('코드와 지역명을 입력하세요');
      return;
    }
    this.saving = true;
    try {
      await createSigungu(this.sigunguForm);
      this.$message.success('시/군/구가 추가되었습니다');
      this.sigunguDialogVisible = false;
      // 해당 시도의 시/군/구 목록 갱신
      const row = this.sidoList.find((r: any) => r.code === this.sigunguForm.parentCode);
      if (row) {
        row._sigungu = null;
        await this.loadSigungu(row);
      }
    } catch (e: any) {
      this.$message.error(e?.response?.data?.message || '추가 실패');
    } finally {
      this.saving = false;
    }
  }

  // ── 리그 확인 ──
  async openLeagueDialog(row: any) {
    this.detailRegionName = row.name;
    this.leagueDialogVisible = true;
    this.regionLeagues = [];
    this.detailLoading = true;
    try {
      const res = await getRegionLeagues(row.code);
      this.regionLeagues = res.data || [];
    } finally {
      this.detailLoading = false;
    }
  }

  // ── 팀 확인 ──
  async openTeamDialog(row: any) {
    this.detailRegionName = row.name;
    this.teamDialogVisible = true;
    this.regionTeams = [];
    this.detailLoading = true;
    try {
      const res = await getRegionTeams(row.code);
      this.regionTeams = res.data || [];
    } finally {
      this.detailLoading = false;
    }
  }

  // ── 헬퍼 ──
  statusLabel(status: string) {
    const map: Record<string, string> = {
      RECRUITING: '모집중',
      IN_PROGRESS: '진행중',
      COMPLETED: '완료',
    };
    return map[status] || status;
  }

  statusType(status: string) {
    const map: Record<string, string> = {
      RECRUITING: 'warning',
      IN_PROGRESS: 'success',
      COMPLETED: 'info',
    };
    return map[status] || '';
  }
}
</script>

<style scoped>
.sigungu-wrap {
  padding: 12px 16px;
  background: #f8f9fa;
  min-height: 48px;
}
.sigungu-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.sigungu-label {
  font-size: 13px;
  font-weight: 600;
  color: #606266;
}
.sigungu-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}
.no-data {
  font-size: 12px;
  color: #999;
}
.empty-msg {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 13px;
}
</style>
