<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">팀 삭제 요청</p>
        <p class="usernumber">운영자가 삭제를 요청한 팀 목록입니다. 검토 후 승인 또는 거절하세요.</p>
      </div>
    </div>

    <!-- <el-button type="primary" @click="fetchList" :loading="loading" style="margin-bottom:16px">
      새로고침
    </el-button> -->

    <el-table
      :data="list"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
      empty-text="삭제 요청이 없습니다"
    >
      <el-table-column label="팀명" prop="name" min-width="140" />
      <el-table-column label="팀 코드" prop="teamCode" width="120" />
      <el-table-column label="지역" width="130">
        <template slot-scope="scope">
          {{ scope.row.regionSido }} {{ scope.row.regionSigungu }}
        </template>
      </el-table-column>
      <el-table-column label="멤버 수" prop="memberCount" width="80" align="center" />
      <el-table-column label="환불 계좌" min-width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.refundBankName">
            {{ scope.row.refundBankName }} {{ scope.row.refundBankAccount }}
          </span>
          <el-tag v-else type="warning" size="small">미등록</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="운영자 UID" prop="ownerUid" min-width="150" show-overflow-tooltip />
      <el-table-column label="액션" width="200" align="center">
        <template slot-scope="scope">
          <div style="display:flex;gap:6px;justify-content:center;flex-wrap:nowrap;">
            <el-button
              size="mini"
              type="danger"
              @click="handleApprove(scope.row)"
            >승인 (삭제)</el-button>
            <el-button
              size="mini"
              @click="handleReject(scope.row)"
            >거절</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getDeleteRequestedTeams, approveDeleteTeam, rejectDeleteTeam } from '@/api/team';

interface TeamDetail {
  uid: string
  name: string
  teamCode: string
  regionSido: string
  regionSigungu: string
  memberCount: number
  refundBankName: string
  refundBankAccount: string
  ownerUid: string
}

@Component({ name: 'TeamDeleteRequest' })
export default class TeamDeleteRequest extends Vue {
  list: TeamDetail[] = []

  loading = false

  created() {
    this.fetchList();
  }

  async fetchList() {
    this.loading = true;
    try {
      const res: any = await getDeleteRequestedTeams();
      this.list = Array.isArray(res.data) ? res.data : [];
    } catch (e) {
      this.$message.error('목록 조회 실패');
    } finally {
      this.loading = false;
    }
  }

  async handleApprove(row: TeamDetail) {
    try {
      await this.$confirm(
        `"${row.name}" 팀을 삭제 승인하시겠습니까? 이 작업은 되돌릴 수 없습니다.`,
        '삭제 승인',
        { confirmButtonText: '승인', cancelButtonText: '취소', type: 'error' },
      );
    } catch {
      return;
    }
    try {
      await approveDeleteTeam(row.uid);
      this.$message.success('삭제 승인 완료');
      this.fetchList();
    } catch (e) {
      this.$message.error('처리 실패');
    }
  }

  async handleReject(row: TeamDetail) {
    try {
      await this.$confirm(
        `"${row.name}" 팀의 삭제 요청을 거절하시겠습니까?`,
        '삭제 거절',
        { confirmButtonText: '거절', cancelButtonText: '취소', type: 'warning' },
      );
    } catch {
      return;
    }
    try {
      await rejectDeleteTeam(row.uid);
      this.$message.success('거절 처리 완료');
      this.fetchList();
    } catch (e) {
      this.$message.error('처리 실패');
    }
  }
}
</script>
