<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">유저 관리</p>
        <p class="usernumber">전체 {{ totalElements }}명</p>
      </div>
      <div class="user__tab">
        <div class="user__subtab">
          <el-select v-model="listQuery.searchType" style="width:130px">
            <el-option label="이름" value="actualName" />
            <el-option label="연락처" value="concatNumber" />
            <el-option label="이메일" value="userId" />
          </el-select>
          <el-input
            v-model="listQuery.searchValue"
            placeholder="검색어"
            style="width:200px"
            @keyup.enter.native="handleSearch"
          />
          <el-button @click="handleSearch"><img src="~@/assets/images/search.png" alt=""></el-button>
        </div>
      </div>
    </div>

    <div v-loading="loading" class="user-content">
      <el-table
        border
        :data="userList"
        :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      >
        <el-table-column label="번호" width="70">
          <template slot-scope="scope">
            {{ totalElements - ((listQuery.page - 1) * listQuery.size) - scope.$index }}
          </template>
        </el-table-column>
        <el-table-column label="이름" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.actualName }}
            <el-tag v-if="scope.row.provider === 'KAKAO'" type="warning" effect="dark" size="mini">카카오</el-tag>
            <el-tag v-else-if="scope.row.provider === 'NAVER'" type="success" effect="dark" size="mini">네이버</el-tag>
            <el-tag v-else-if="scope.row.provider === 'APPLE'" type="info" effect="dark" size="mini">애플</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="concatNumber" label="연락처" width="140" />
        <el-table-column prop="birth" label="생년월일" width="120" />
        <el-table-column label="성별" width="80">
          <template slot-scope="scope">
            {{ scope.row.gender === 'MALE' || scope.row.gender === 0 ? '남' : scope.row.gender === 'FEMALE' || scope.row.gender === 1 ? '여' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="이메일" min-width="200" />
        <el-table-column label="가입일" width="160">
          <template slot-scope="scope">
            {{ scope.row.createDate | parseDate }}
          </template>
        </el-table-column>
        <el-table-column label="상태" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'danger'" size="small">
              {{ scope.row.enabled ? '활성' : '비활성' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="관리" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDetail(scope.row)">상세</el-button>
            <el-button
              size="mini"
              :type="scope.row.enabled ? 'danger' : 'success'"
              @click="handleToggleEnable(scope.row)"
            >
              {{ scope.row.enabled ? '비활성화' : '활성화' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="fetchList"
      />
    </div>

    <!-- 유저 상세 다이얼로그 -->
    <el-dialog title="유저 상세" :visible.sync="detailVisible" width="520px">
      <el-descriptions v-if="selectedUser" :column="2" border size="small">
        <el-descriptions-item label="이름">{{ selectedUser.actualName }}</el-descriptions-item>
        <el-descriptions-item label="이메일">{{ selectedUser.userId }}</el-descriptions-item>
        <el-descriptions-item label="성별">{{ selectedUser.gender }}</el-descriptions-item>
        <el-descriptions-item label="생년월일">{{ selectedUser.birth }}</el-descriptions-item>
        <el-descriptions-item label="연락처">{{ selectedUser.concatNumber }}</el-descriptions-item>
        <el-descriptions-item label="가입방식">{{ selectedUser.provider || '이메일' }}</el-descriptions-item>
        <el-descriptions-item label="상태">{{ selectedUser.enabled ? '활성' : '비활성' }}</el-descriptions-item>
        <el-descriptions-item label="매너점수">{{ selectedUser.mannerScore }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getUserList, getUser, enableUser } from '@/api/user';
import Pagination from '@/components/Pagination/index.vue';

@Component({ name: 'UserList', components: { Pagination } })
export default class extends Vue {
  private loading = false;

  private userList: any[] = [];

  private totalElements = 0;

  private detailVisible = false;

  private selectedUser: any = null;

  private listQuery = {
    page: 1,
    size: 20,
    searchType: 'actualName',
    searchValue: '',
  };

  async created() {
    await this.fetchList();
  }

  async fetchList() {
    this.loading = true;
    try {
      const params: any = { page: this.listQuery.page, size: this.listQuery.size };
      if (this.listQuery.searchValue) params[this.listQuery.searchType] = this.listQuery.searchValue;
      const res = await getUserList(params);
      const { data } = res;
      this.userList = data.content || data || [];
      this.totalElements = data.totalElements || this.userList.length;
    } finally {
      this.loading = false;
    }
  }

  handleSearch() {
    this.listQuery.page = 1;
    this.fetchList();
  }

  async handleDetail(row: any) {
    try {
      const res = await getUser(row.uid);
      this.selectedUser = res.data;
      this.detailVisible = true;
    } catch {
      this.$message.error('상세 정보를 불러올 수 없습니다.');
    }
  }

  async handleToggleEnable(row: any) {
    try {
      await enableUser(row.uid, !row.enabled);
      row.enabled = !row.enabled;
      this.$message.success(`${row.enabled ? '활성화' : '비활성화'} 처리 완료`);
    } catch {
      this.$message.error('처리 중 오류가 발생했습니다.');
    }
  }
}
</script>
