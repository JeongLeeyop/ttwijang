<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">배너 관리</p>
        <p class="usernumber">리그 페이지 상단 배너를 관리합니다</p>
      </div>
      <div class="user__tab">
        <button class="tool-btn" @click="openForm()">배너 추가 +</button>
      </div>
    </div>

    <el-table
      :data="bannerList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
    >
      <el-table-column label="미리보기" width="100">
        <template slot-scope="scope">
          <img v-if="scope.row.imageUrl" :src="scope.row.imageUrl" style="width:80px;height:40px;object-fit:cover;border-radius:4px" />
          <span v-else style="color:#ccc">없음</span>
        </template>
      </el-table-column>
      <el-table-column label="제목" prop="title" min-width="160" />
      <el-table-column label="노출 기간" min-width="200">
        <template slot-scope="scope">
          {{ scope.row.startDate | parseDate }} ~ {{ scope.row.endDate | parseDate }}
        </template>
      </el-table-column>
      <el-table-column label="지역" width="140">
        <template slot-scope="scope">
          {{ scope.row.regionSido }} {{ scope.row.regionSigungu || '' }}
        </template>
      </el-table-column>
      <el-table-column label="상태" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
            {{ scope.row.status === 'ACTIVE' ? '활성' : '비활성' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="관리" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="openForm(scope.row)">수정</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 배너 생성/수정 다이얼로그 -->
    <el-dialog
      :title="form.uid ? '배너 수정' : '배너 추가'"
      :visible.sync="dialogVisible"
      width="560px"
      @close="resetForm"
    >
      <el-form ref="bannerForm" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="제목" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="이미지 입력방식">
          <el-radio-group v-model="imageInputType" @change="onImageInputTypeChange">
            <el-radio label="url">URL 입력</el-radio>
            <el-radio label="file">파일 업로드</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="이미지 URL" prop="imageUrl" v-if="imageInputType === 'url'">
          <el-input v-model="form.imageUrl" placeholder="이미지 URL을 입력하세요" />
        </el-form-item>
        <el-form-item label="이미지 파일" v-if="imageInputType === 'file'">
          <el-upload
            action=""
            :http-request="handleFileUpload"
            :show-file-list="false"
            accept="image/*"
            :disabled="uploadingImage"
          >
            <el-button size="small" type="primary" :loading="uploadingImage">
              {{ uploadingImage ? '업로드 중...' : '파일 선택' }}
            </el-button>
          </el-upload>
          <div v-if="form.imageUrl" style="margin-top:8px">
            <img :src="form.imageUrl" style="max-width:200px;max-height:80px;object-fit:cover;border-radius:4px;border:1px solid #eee" />
            <el-button size="mini" type="text" style="margin-left:8px;color:#f56c6c" @click="form.imageUrl = ''">제거</el-button>
          </div>
          <div v-else style="color:#aaa;font-size:12px;margin-top:4px">이미지 파일을 선택하세요 (jpg, png, gif 등)</div>
        </el-form-item>
        <el-form-item label="링크 URL" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="클릭 시 이동할 URL" />
        </el-form-item>
        <!-- targetPage는 항상 LEAGUE로 고정 -->
        <el-form-item label="노출 기간" prop="startDate">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="~"
            start-placeholder="시작일"
            end-placeholder="종료일"
            value-format="yyyy-MM-dd"
            style="width:100%"
            @change="onDateChange"
          />
        </el-form-item>
        <el-form-item label="지역 (시/도)" prop="regionSido">
          <el-select v-model="form.regionSido" clearable placeholder="전체" style="width:100%" @change="onSidoChange">
            <el-option v-for="r in sidoList" :key="r.code" :label="r.name" :value="r.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="지역 (시/군/구)">
          <el-select v-model="form.regionSigungu" clearable placeholder="전체" style="width:100%">
            <el-option v-for="r in sigunguList" :key="r.code" :label="r.name" :value="r.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="노출 순서">
          <el-input-number v-model="form.displayOrder" :min="1" />
        </el-form-item>
        <el-form-item label="상태">
          <el-select v-model="form.status" style="width:100%">
            <el-option label="활성" value="ACTIVE" />
            <el-option label="비활성" value="INACTIVE" />
          </el-select>
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
import { getBannerList, createBanner, updateBanner, deleteBanner, uploadBannerImage } from '@/api/banner';
import { getSidoList, getSigunguList } from '@/api/region';
import { ElForm } from 'element-ui/types/form';

@Component({ name: 'BannerList' })
export default class extends Vue {
  private loading = false;
  private saving = false;
  private uploadingImage = false;
  private dialogVisible = false;
  private bannerList: any[] = [];
  private sidoList: any[] = [];
  private sigunguList: any[] = [];
  private dateRange: string[] = [];
  private imageInputType: 'url' | 'file' = 'url';

  private form: any = {
    uid: null, title: '', imageUrl: '', linkUrl: '',
    targetPage: 'LEAGUE', startDate: '', endDate: '',
    regionSido: '', regionSigungu: '', displayOrder: 1, status: 'ACTIVE',
  };

  private rules = {
    title: [{ required: true, message: '제목을 입력하세요', trigger: 'blur' }],
    imageUrl: [{ required: true, message: '이미지 URL을 입력하세요', trigger: 'blur' }],
    targetPage: [{ required: true, message: '노출 페이지를 선택하세요', trigger: 'change' }],
  };

  async created() {
    await Promise.all([this.fetchList(), this.fetchSido()]);
  }

  async fetchList() {
    this.loading = true;
    try {
      const res = await getBannerList();
      this.bannerList = res.data?.content || res.data || [];
    } finally {
      this.loading = false;
    }
  }

  async fetchSido() {
    const res = await getSidoList();
    this.sidoList = res.data || [];
  }

  async onSidoChange(code: string) {
    this.form.regionSigungu = '';
    this.sigunguList = [];
    if (code) {
      const res = await getSigunguList(code);
      this.sigunguList = res.data || [];
    }
  }

  onDateChange(val: string[]) {
    if (val) {
      this.form.startDate = val[0];
      this.form.endDate = val[1];
    }
  }

  openForm(row?: any) {
    if (row) {
      this.form = { ...row };
      this.dateRange = [row.startDate, row.endDate];
      if (row.regionSido) this.onSidoChange(row.regionSido);
      this.imageInputType = row.imageUrl ? 'url' : 'url';
    } else {
      this.resetForm();
    }
    this.dialogVisible = true;
  }

  resetForm() {
    this.form = {
      uid: null, title: '', imageUrl: '', linkUrl: '',
      targetPage: 'LEAGUE', startDate: '', endDate: '',
      regionSido: '', regionSigungu: '', displayOrder: 1, status: 'ACTIVE',
    };
    this.dateRange = [];
    this.imageInputType = 'url';
  }

  onImageInputTypeChange() {
    this.form.imageUrl = '';
  }

  async handleFileUpload(options: any) {
    const formData = new FormData();
    formData.append('file', options.file);
    this.uploadingImage = true;
    try {
      const res = await uploadBannerImage(formData);
      const data = res.data;
      // url 필드 우선, 없으면 uid로 경로 구성
      this.form.imageUrl = data.url || `/api/attached-file/${data.uid}`;
      this.$message.success('이미지가 업로드되었습니다.');
    } catch {
      this.$message.error('이미지 업로드 중 오류가 발생했습니다.');
    } finally {
      this.uploadingImage = false;
    }
  }

  handleSave() {
    (this.$refs.bannerForm as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.saving = true;
      try {
        if (this.form.uid) {
          await updateBanner(this.form.uid, this.form);
          this.$message.success('수정되었습니다.');
        } else {
          await createBanner(this.form);
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
    await this.$confirm(`"${row.title}" 배너를 삭제하시겠습니까?`, '삭제 확인', { type: 'warning' });
    try {
      await deleteBanner(row.uid);
      this.$message.success('삭제되었습니다.');
      await this.fetchList();
    } catch {
      this.$message.error('삭제 중 오류가 발생했습니다.');
    }
  }
}
</script>
