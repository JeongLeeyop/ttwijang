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
        <el-table-column label="번호" width="60">
          <template slot-scope="scope">
            {{ totalElements - ((listQuery.page - 1) * listQuery.size) - scope.$index }}
          </template>
        </el-table-column>
        <el-table-column label="이름" min-width="110">
          <template slot-scope="scope">
            {{ scope.row.actualName }}
            <el-tag v-if="scope.row.provider === 'KAKAO'" type="warning" effect="dark" size="mini">카카오</el-tag>
            <el-tag v-else-if="scope.row.provider === 'NAVER'" type="success" effect="dark" size="mini">네이버</el-tag>
            <el-tag v-else-if="scope.row.provider === 'APPLE'" type="info" effect="dark" size="mini">애플</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="concatNumber" label="연락처" width="130" />
        <el-table-column prop="birth" label="생년월일" width="110" />
        <el-table-column label="성별" width="60">
          <template slot-scope="scope">
            {{ scope.row.gender === 'MALE' || scope.row.gender === 0 ? '남' : scope.row.gender === 'FEMALE' || scope.row.gender === 1 ? '여' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="이메일" min-width="180" show-overflow-tooltip />
        <el-table-column label="가입일" width="150">
          <template slot-scope="scope">
            {{ scope.row.createDate | parseDate }}
          </template>
        </el-table-column>
        <el-table-column label="보유 포인트" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="balanceLoading" class="balance-loading"><i class="el-icon-loading" /></span>
            <span v-else class="balance-text">{{ (balances[scope.row.uid] || 0).toLocaleString() }}원</span>
          </template>
        </el-table-column>
        <el-table-column label="상태" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'danger'" size="small">
              {{ scope.row.enabled ? '활성' : '비활성' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="포인트 관리" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openPointModal(scope.row)">지급/차감</el-button>
            <el-button size="mini" @click="goToPointHistory(scope.row)">내역</el-button>
          </template>
        </el-table-column>
        <el-table-column label="관리" width="130" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDetail(scope.row)">상세</el-button>
            <el-button
              size="mini"
              :type="scope.row.enabled ? 'danger' : 'success'"
              @click="handleToggleEnable(scope.row)"
            >
              {{ scope.row.enabled ? '비활성' : '활성' }}
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

    <!-- 포인트 지급/차감 다이얼로그 -->
    <el-dialog
      title="포인트 지급 / 차감"
      :visible.sync="pointModalVisible"
      width="420px"
      :close-on-click-modal="false"
      @close="resetPointForm"
    >
      <div v-if="pointTargetUser" class="point-user-info">
        <div class="point-user-name">
          {{ pointTargetUser.actualName }}
          <span class="point-user-email">({{ pointTargetUser.userId }})</span>
        </div>
        <div class="point-balance">
          현재 잔액:
          <span v-if="walletLoading"><i class="el-icon-loading" /></span>
          <strong v-else>{{ currentBalance.toLocaleString() }}원</strong>
        </div>
      </div>

      <el-form :model="pointForm" label-position="top" size="small" style="margin-top:16px">
        <el-form-item label="유형">
          <el-radio-group v-model="pointForm.type" size="small">
            <el-radio-button label="EARN">지급</el-radio-button>
            <el-radio-button label="USE">차감</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="금액 (원)">
          <el-input-number
            v-model="pointForm.amount"
            :min="1"
            :step="1000"
            style="width:100%"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="사유">
          <el-input
            v-model="pointForm.description"
            placeholder="예: 이벤트 보상, 오류 수정 등"
            maxlength="100"
          />
        </el-form-item>
      </el-form>

      <div slot="footer">
        <el-button size="small" @click="pointModalVisible = false">취소</el-button>
        <el-button
          size="small"
          :type="pointForm.type === 'EARN' ? 'primary' : 'danger'"
          :loading="pointSubmitting"
          @click="submitPointAdjust"
        >
          {{ pointForm.type === 'EARN' ? '지급하기' : '차감하기' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getUserList, getUser, enableUser } from '@/api/user';
import { getAdminUserWallet, adminAdjustPoint } from '@/api/cash';
import Pagination from '@/components/Pagination/index.vue';

@Component({ name: 'UserList', components: { Pagination } })
export default class extends Vue {
  private loading = false;

  private balanceLoading = false;

  private userList: any[] = [];

  private totalElements = 0;

  private balances: Record<string, number> = {};

  private detailVisible = false;

  private selectedUser: any = null;

  private pointModalVisible = false;

  private pointTargetUser: any = null;

  private walletLoading = false;

  private pointSubmitting = false;

  private currentBalance = 0;

  private pointForm = {
    type: 'EARN' as 'EARN' | 'USE',
    amount: 1000,
    description: '',
  };

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
    this.fetchAllBalances();
  }

  private async fetchAllBalances() {
    if (!this.userList.length) return;
    this.balanceLoading = true;
    const results = await Promise.allSettled(
      this.userList.map((u) => getAdminUserWallet(u.uid).then((r) => ({ uid: u.uid, balance: r.data?.balance || 0 }))),
    );
    const map: Record<string, number> = {};
    results.forEach((r) => {
      if (r.status === 'fulfilled') map[r.value.uid] = r.value.balance;
    });
    this.balances = map;
    this.balanceLoading = false;
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

  async openPointModal(row: any) {
    this.pointTargetUser = row;
    this.pointModalVisible = true;
    this.currentBalance = this.balances[row.uid] || 0;
    this.walletLoading = true;
    try {
      const res = await getAdminUserWallet(row.uid);
      this.currentBalance = res.data?.balance || 0;
    } catch {
      // 기존 캐시값 유지
    } finally {
      this.walletLoading = false;
    }
  }

  async submitPointAdjust() {
    if (!this.pointTargetUser) return;
    if (!this.pointForm.amount || this.pointForm.amount < 1) {
      this.$message.warning('금액을 1원 이상 입력해주세요.');
      return;
    }
    this.pointSubmitting = true;
    try {
      await adminAdjustPoint({
        userUid: this.pointTargetUser.uid,
        amount: this.pointForm.amount,
        type: this.pointForm.type,
        description: this.pointForm.description,
      });
      const label = this.pointForm.type === 'EARN' ? '지급' : '차감';
      this.$message.success(`${this.pointForm.amount.toLocaleString()}원 ${label} 완료`);
      // 테이블 잔액 즉시 갱신
      const delta = this.pointForm.type === 'EARN' ? this.pointForm.amount : -this.pointForm.amount;
      this.$set(this.balances, this.pointTargetUser.uid, (this.balances[this.pointTargetUser.uid] || 0) + delta);
      this.pointModalVisible = false;
    } catch (error: any) {
      const msg = error?.response?.data?.message || '처리에 실패했습니다.';
      this.$message.error(msg);
    } finally {
      this.pointSubmitting = false;
    }
  }

  goToPointHistory(row: any) {
    this.$router.push({
      name: 'UserPointHistory',
      params: { userUid: row.uid, userName: row.actualName },
    });
  }

  resetPointForm() {
    this.pointForm = { type: 'EARN', amount: 1000, description: '' };
    this.pointTargetUser = null;
  }
}
</script>

<style scoped>
.point-user-info {
  background: #f5f7fa;
  border-radius: 6px;
  padding: 12px 16px;
}
.point-user-name {
  font-weight: 600;
  margin-bottom: 6px;
}
.point-user-email {
  font-weight: 400;
  color: #909399;
  font-size: 13px;
}
.point-balance {
  font-size: 14px;
  color: #606266;
}
.point-balance strong {
  color: #409EFF;
  font-size: 16px;
  margin-left: 4px;
}
.balance-text {
  font-weight: 600;
  color: #303133;
}
.balance-loading {
  color: #909399;
}
</style>
