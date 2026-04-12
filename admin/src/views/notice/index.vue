<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">공지사항 관리</p>
        <p class="usernumber">총 {{ notices.length }}건</p>
      </div>
      <el-button type="primary" size="small" @click="openCreateDialog">+ 공지 등록</el-button>
    </div>

    <el-table :data="notices" v-loading="loading" border style="margin-top: 16px">
      <el-table-column prop="title" label="제목" />
      <el-table-column label="등록일" width="130" align="center">
        <template slot-scope="{ row }">{{ row.createdDate && row.createdDate.substring(0, 10) }}</template>
      </el-table-column>
      <el-table-column label="관리" width="140" align="center">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="openEditDialog(row)">수정</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row.uid)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogMode === 'create' ? '공지사항 등록' : '공지사항 수정'" :visible.sync="dialogVisible" width="560px">
      <el-form ref="noticeForm" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="제목" prop="title">
          <el-input v-model="form.title" placeholder="제목을 입력하세요" />
        </el-form-item>
        <el-form-item label="내용">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="내용을 입력하세요" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">저장</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getNoticeList, createNotice, updateNotice, deleteNotice } from '@/api/notice';

@Component
export default class NoticeManage extends Vue {
  private loading = false;
  private saving = false;
  private notices: any[] = [];
  private dialogVisible = false;
  private dialogMode: 'create' | 'edit' = 'create';
  private editUid = '';
  private form = { title: '', content: '' };

  private rules = {
    title: [{ required: true, message: '제목을 입력하세요', trigger: 'blur' }],
  };

  created(): void {
    this.fetchNotices();
  }

  private async fetchNotices(): Promise<void> {
    this.loading = true;
    try {
      const res = await getNoticeList();
      this.notices = res.data || [];
    } finally {
      this.loading = false;
    }
  }

  private openCreateDialog(): void {
    this.dialogMode = 'create';
    this.editUid = '';
    this.form = { title: '', content: '' };
    this.dialogVisible = true;
  }

  private openEditDialog(row: any): void {
    this.dialogMode = 'edit';
    this.editUid = row.uid;
    this.form = { title: row.title, content: row.content || '' };
    this.dialogVisible = true;
  }

  private async handleSave(): Promise<void> {
    const formRef = this.$refs.noticeForm as any;
    const valid = await formRef.validate().catch(() => false);
    if (!valid) return;

    this.saving = true;
    try {
      if (this.dialogMode === 'create') {
        await createNotice(this.form);
        this.$message.success('등록되었습니다.');
      } else {
        await updateNotice(this.editUid, this.form);
        this.$message.success('수정되었습니다.');
      }
      this.dialogVisible = false;
      await this.fetchNotices();
    } finally {
      this.saving = false;
    }
  }

  private async handleDelete(uid: string): Promise<void> {
    await this.$confirm('삭제하시겠습니까?', '확인', { type: 'warning' });
    await deleteNotice(uid);
    this.$message.success('삭제되었습니다.');
    await this.fetchNotices();
  }
}
</script>
