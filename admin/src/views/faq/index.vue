<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">자주 묻는 질문 관리</p>
        <p class="usernumber">총 {{ faqs.length }}건</p>
      </div>
      <el-button type="primary" size="small" @click="openCreateDialog">+ FAQ 등록</el-button>
    </div>

    <el-table :data="faqs" v-loading="loading" border style="margin-top: 16px">
      <el-table-column prop="displayOrder" label="순서" width="70" align="center" />
      <el-table-column prop="category" label="카테고리" width="110" align="center">
        <template slot-scope="{ row }">{{ row.category || '-' }}</template>
      </el-table-column>
      <el-table-column prop="question" label="질문" />
      <el-table-column label="관리" width="140" align="center">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="openEditDialog(row)">수정</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row.uid)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 등록/수정 다이얼로그 -->
    <el-dialog :title="dialogMode === 'create' ? 'FAQ 등록' : 'FAQ 수정'" :visible.sync="dialogVisible" width="560px">
      <el-form ref="faqForm" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="질문" prop="question">
          <el-input v-model="form.question" placeholder="질문을 입력하세요" />
        </el-form-item>
        <el-form-item label="답변">
          <el-input v-model="form.answer" type="textarea" :rows="5" placeholder="답변을 입력하세요" />
        </el-form-item>
        <el-form-item label="카테고리">
          <el-input v-model="form.category" placeholder="예: 결제, 팀, 매치" />
        </el-form-item>
        <el-form-item label="표시 순서">
          <el-input-number v-model="form.displayOrder" :min="0" :step="1" />
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
import { getFaqList, createFaq, updateFaq, deleteFaq } from '@/api/faq';

@Component
export default class FaqManage extends Vue {
  private loading = false;
  private saving = false;
  private faqs: any[] = [];
  private dialogVisible = false;
  private dialogMode: 'create' | 'edit' = 'create';
  private editUid = '';
  private form = { question: '', answer: '', category: '', displayOrder: 0 };

  private rules = {
    question: [{ required: true, message: '질문을 입력하세요', trigger: 'blur' }],
  };

  created(): void {
    this.fetchFaqs();
  }

  private async fetchFaqs(): Promise<void> {
    this.loading = true;
    try {
      const res = await getFaqList();
      this.faqs = res.data || [];
    } finally {
      this.loading = false;
    }
  }

  private openCreateDialog(): void {
    this.dialogMode = 'create';
    this.editUid = '';
    this.form = { question: '', answer: '', category: '', displayOrder: 0 };
    this.dialogVisible = true;
  }

  private openEditDialog(row: any): void {
    this.dialogMode = 'edit';
    this.editUid = row.uid;
    this.form = {
      question: row.question,
      answer: row.answer || '',
      category: row.category || '',
      displayOrder: row.displayOrder || 0,
    };
    this.dialogVisible = true;
  }

  private async handleSave(): Promise<void> {
    const formRef = this.$refs.faqForm as any;
    const valid = await formRef.validate().catch(() => false);
    if (!valid) return;

    this.saving = true;
    try {
      if (this.dialogMode === 'create') {
        await createFaq(this.form);
        this.$message.success('등록되었습니다.');
      } else {
        await updateFaq(this.editUid, this.form);
        this.$message.success('수정되었습니다.');
      }
      this.dialogVisible = false;
      await this.fetchFaqs();
    } finally {
      this.saving = false;
    }
  }

  private async handleDelete(uid: string): Promise<void> {
    await this.$confirm('삭제하시겠습니까?', '확인', { type: 'warning' });
    await deleteFaq(uid);
    this.$message.success('삭제되었습니다.');
    await this.fetchFaqs();
  }
}
</script>
