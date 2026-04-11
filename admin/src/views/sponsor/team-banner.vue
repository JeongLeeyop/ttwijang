<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">팀별 배너 설정</p>
        <p class="usernumber">나의 팀 → 후원내역에 표시될 배너를 관리합니다</p>
      </div>
      <div class="user__tab">
        <button class="tool-btn" @click="openForm()">배너 등록 +</button>
      </div>
    </div>

    <el-table
      :data="bannerList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
    >
      <el-table-column label="팀명" prop="teamName" min-width="160" />
      <el-table-column label="배너 이미지" width="140">
        <template slot-scope="scope">
          <img v-if="scope.row.imageUrl" :src="scope.row.imageUrl" style="width:100px;height:50px;object-fit:cover;border-radius:4px" />
          <span v-else style="color:#ccc">없음</span>
        </template>
      </el-table-column>
      <el-table-column label="설명" prop="description" min-width="200" />
      <el-table-column label="등록일" width="160">
        <template slot-scope="scope">
          {{ scope.row.createdDate | parseDate }}
        </template>
      </el-table-column>
      <el-table-column label="관리" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="openForm(scope.row)">수정</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 등록/수정 다이얼로그 -->
    <el-dialog
      :title="form.uid ? '배너 수정' : '배너 등록'"
      :visible.sync="dialogVisible"
      width="520px"
      @close="resetForm"
    >
      <el-form ref="bannerForm" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="팀 선택" prop="teamUid">
          <el-select
            v-model="form.teamUid"
            filterable
            placeholder="팀을 선택하세요"
            style="width:100%"
            @change="onTeamChange"
          >
            <el-option v-for="t in teamList" :key="t.uid" :label="t.name" :value="t.uid" />
          </el-select>
        </el-form-item>
        <el-form-item label="이미지 URL" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="배너 이미지 URL" />
          <div v-if="form.imageUrl" style="margin-top:8px">
            <img :src="form.imageUrl" style="max-width:100%;height:80px;object-fit:cover;border-radius:4px" />
          </div>
        </el-form-item>
        <el-form-item label="설명">
          <el-input v-model="form.description" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">저장</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getTeamBannerList, createTeamBanner, updateTeamBanner, deleteTeamBanner } from '@/api/sponsor';
import { getTeamList } from '@/api/team';
import { ElForm } from 'element-ui/types/form';

@Component({ name: 'TeamBanner' })
export default class extends Vue {
  private loading = false;
  private saving = false;
  private dialogVisible = false;
  private bannerList: any[] = [];
  private teamList: any[] = [];

  private form: any = { uid: null, teamUid: '', teamName: '', imageUrl: '', description: '' };

  private rules = {
    teamUid: [{ required: true, message: '팀을 선택하세요', trigger: 'change' }],
    imageUrl: [{ required: true, message: '이미지 URL을 입력하세요', trigger: 'blur' }],
  };

  async created() {
    await Promise.all([this.fetchList(), this.fetchTeams()]);
  }

  async fetchList() {
    this.loading = true;
    try {
      const res = await getTeamBannerList();
      this.bannerList = res.data?.content || res.data || [];
    } catch {
      // 백엔드 미구현 시 silent fail
    } finally {
      this.loading = false;
    }
  }

  async fetchTeams() {
    try {
      const res = await getTeamList({ size: 200 });
      this.teamList = res.data?.content || res.data || [];
    } catch { /* ignore */ }
  }

  onTeamChange(uid: string) {
    const team = this.teamList.find((t) => t.uid === uid);
    if (team) this.form.teamName = team.name;
  }

  openForm(row?: any) {
    if (row) {
      this.form = { ...row };
    } else {
      this.resetForm();
    }
    this.dialogVisible = true;
  }

  resetForm() {
    this.form = { uid: null, teamUid: '', teamName: '', imageUrl: '', description: '' };
  }

  handleSave() {
    (this.$refs.bannerForm as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.saving = true;
      try {
        if (this.form.uid) {
          await updateTeamBanner(this.form.uid, this.form);
          this.$message.success('수정되었습니다.');
        } else {
          await createTeamBanner(this.form);
          this.$message.success('등록되었습니다.');
        }
        this.dialogVisible = false;
        await this.fetchList();
      } catch {
        this.$message.error('저장 중 오류가 발생했습니다.');
      } finally {
        this.saving = false;
      }
    });
  }

  async handleDelete(row: any) {
    await this.$confirm(`"${row.teamName}" 팀 배너를 삭제하시겠습니까?`, '삭제 확인', { type: 'warning' });
    try {
      await deleteTeamBanner(row.uid);
      this.$message.success('삭제되었습니다.');
      await this.fetchList();
    } catch {
      this.$message.error('삭제 중 오류가 발생했습니다.');
    }
  }
}
</script>
