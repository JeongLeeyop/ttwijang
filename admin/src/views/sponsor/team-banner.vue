<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">팀별 배너 설정</p>
        <p class="usernumber">나의 팀 → 후원내역에 표시될 배너를 관리합니다 (팀당 1개 배너, 이미지 복수 등록 가능)</p>
      </div>
      <div class="user__tab">
        <button class="tool-btn" @click="openRegisterDialog">배너 등록 +</button>
      </div>
    </div>

    <el-table
      :data="bannerList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
    >
      <el-table-column label="팀명" prop="teamName" min-width="140" />
      <el-table-column label="대표 이미지" width="140">
        <template slot-scope="scope">
          <img
            v-if="scope.row.imageUrls && scope.row.imageUrls.length > 0"
            :src="scope.row.imageUrls[0]"
            style="width:100px;height:50px;object-fit:cover;border-radius:4px"
          />
          <span v-else style="color:#ccc">없음</span>
        </template>
      </el-table-column>
      <el-table-column label="이미지 수" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" type="info">
            {{ scope.row.imageUrls ? scope.row.imageUrls.length : 0 }}개
          </el-tag>
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
          <el-button size="mini" @click="openEditDialog(scope.row)">수정</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 등록 다이얼로그 -->
    <el-dialog
      title="배너 등록"
      :visible.sync="registerDialogVisible"
      width="580px"
      @close="resetRegister"
    >
      <el-form label-width="90px" style="margin-bottom:8px">
        <el-form-item label="팀 선택" required>
          <el-select
            v-model="registerTeamUid"
            filterable
            placeholder="팀을 선택하세요"
            style="width:100%"
            @change="onRegisterTeamChange"
          >
            <el-option v-for="t in teamList" :key="t.uid" :label="t.name" :value="t.uid" />
          </el-select>
        </el-form-item>
        <el-form-item label="설명">
          <el-input v-model="registerDescription" type="textarea" :rows="2" placeholder="배너 설명 (선택)" />
        </el-form-item>
      </el-form>

      <div class="image-list-header">
        <span class="image-list-label">이미지 목록</span>
        <el-button type="text" icon="el-icon-plus" @click="addImageItem(registerImages)">이미지 추가</el-button>
      </div>

      <div v-for="(item, idx) in registerImages" :key="idx" class="image-row">
        <div class="image-row__index">{{ idx + 1 }}</div>
        <div class="image-row__body">
          <el-radio-group v-model="item.inputType" size="mini" @change="item.imageUrl = ''" style="margin-bottom:6px">
            <el-radio-button label="url">URL</el-radio-button>
            <el-radio-button label="file">파일</el-radio-button>
          </el-radio-group>
          <div v-if="item.inputType === 'url'">
            <el-input v-model="item.imageUrl" placeholder="이미지 URL을 입력하세요" size="small" />
            <div v-if="item.imageUrl" class="img-preview-box">
              <img
                :src="item.imageUrl"
                class="img-preview"
                @error="(e) => e.target.style.display='none'"
                @load="(e) => e.target.style.display='block'"
              />
            </div>
          </div>
          <div v-else>
            <el-upload
              action=""
              :http-request="(opt) => handleFileUpload(opt, item)"
              :show-file-list="false"
              accept="image/*"
              :disabled="item.uploading"
            >
              <el-button size="mini" type="primary" :loading="item.uploading">
                {{ item.uploading ? '업로드 중...' : '파일 선택' }}
              </el-button>
            </el-upload>
            <div v-if="item.imageUrl" style="margin-top:6px">
              <img :src="item.imageUrl" style="max-width:160px;max-height:60px;object-fit:cover;border-radius:4px;border:1px solid #eee" />
              <el-button size="mini" type="text" style="color:#f56c6c;margin-left:6px" @click="item.imageUrl = ''">제거</el-button>
            </div>
          </div>
        </div>
        <el-button
          size="mini"
          type="text"
          icon="el-icon-delete"
          style="color:#f56c6c;margin-left:8px;flex-shrink:0"
          :disabled="registerImages.length === 1"
          @click="registerImages.splice(idx, 1)"
        />
      </div>

      <div slot="footer">
        <el-button @click="registerDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="handleRegister">
          저장 (이미지 {{ registerImages.length }}개)
        </el-button>
      </div>
    </el-dialog>

    <!-- 수정 다이얼로그 -->
    <el-dialog
      title="배너 수정"
      :visible.sync="editDialogVisible"
      width="580px"
      @close="resetEdit"
    >
      <el-form label-width="90px" style="margin-bottom:8px">
        <el-form-item label="팀">
          <span style="font-weight:600">{{ editTeamName }}</span>
        </el-form-item>
        <el-form-item label="설명">
          <el-input v-model="editDescription" type="textarea" :rows="2" placeholder="배너 설명 (선택)" />
        </el-form-item>
      </el-form>

      <div class="image-list-header">
        <span class="image-list-label">이미지 목록</span>
        <el-button type="text" icon="el-icon-plus" @click="addImageItem(editImages)">이미지 추가</el-button>
      </div>

      <div v-for="(item, idx) in editImages" :key="idx" class="image-row">
        <div class="image-row__index">{{ idx + 1 }}</div>
        <div class="image-row__body">
          <el-radio-group v-model="item.inputType" size="mini" @change="item.imageUrl = ''" style="margin-bottom:6px">
            <el-radio-button label="url">URL</el-radio-button>
            <el-radio-button label="file">파일</el-radio-button>
          </el-radio-group>
          <div v-if="item.inputType === 'url'">
            <el-input v-model="item.imageUrl" placeholder="이미지 URL을 입력하세요" size="small" />
            <div v-if="item.imageUrl" class="img-preview-box">
              <img
                :src="item.imageUrl"
                class="img-preview"
                @error="(e) => e.target.style.display='none'"
                @load="(e) => e.target.style.display='block'"
              />
            </div>
          </div>
          <div v-else>
            <el-upload
              action=""
              :http-request="(opt) => handleFileUpload(opt, item)"
              :show-file-list="false"
              accept="image/*"
              :disabled="item.uploading"
            >
              <el-button size="mini" type="primary" :loading="item.uploading">
                {{ item.uploading ? '업로드 중...' : '파일 선택' }}
              </el-button>
            </el-upload>
            <div v-if="item.imageUrl" style="margin-top:6px">
              <img :src="item.imageUrl" style="max-width:160px;max-height:60px;object-fit:cover;border-radius:4px;border:1px solid #eee" />
              <el-button size="mini" type="text" style="color:#f56c6c;margin-left:6px" @click="item.imageUrl = ''">제거</el-button>
            </div>
          </div>
        </div>
        <el-button
          size="mini"
          type="text"
          icon="el-icon-delete"
          style="color:#f56c6c;margin-left:8px;flex-shrink:0"
          :disabled="editImages.length === 1"
          @click="editImages.splice(idx, 1)"
        />
      </div>

      <div slot="footer">
        <el-button @click="editDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="handleEditSave">
          저장 (이미지 {{ editImages.length }}개)
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  getTeamBannerList, createTeamBanner, updateTeamBanner, deleteTeamBanner,
} from '@/api/sponsor';
import { getTeamList } from '@/api/team';
import { uploadBannerImage } from '@/api/banner';

interface ImageItem {
  imageUrl: string
  inputType: 'url' | 'file'
  uploading: boolean
}

@Component({ name: 'TeamBanner' })
export default class extends Vue {
  private loading = false

  private saving = false

  private bannerList: any[] = []

  private teamList: any[] = []

  // 등록
  private registerDialogVisible = false

  private registerTeamUid = ''

  private registerTeamName = ''

  private registerDescription = ''

  private registerImages: ImageItem[] = []

  // 수정
  private editDialogVisible = false

  private editUid = ''

  private editTeamName = ''

  private editDescription = ''

  private editImages: ImageItem[] = []

  async created() {
    await Promise.all([this.fetchList(), this.fetchTeams()]);
  }

  async fetchList() {
    this.loading = true;
    try {
      const res = await getTeamBannerList();
      this.bannerList = res.data?.content || res.data || [];
    } catch {
      // silent fail
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

  newImageItem(): ImageItem {
    return { imageUrl: '', inputType: 'url', uploading: false };
  }

  addImageItem(list: ImageItem[]) {
    list.push(this.newImageItem());
  }

  async handleFileUpload(options: any, item: ImageItem) {
    const formData = new FormData();
    formData.append('file', options.file);
    item.uploading = true;
    try {
      const res = await uploadBannerImage(formData);
      const { data } = res;
      item.imageUrl = data.url || `/api/attached-file/${data.uid}`;
      this.$message.success('이미지가 업로드되었습니다.');
    } catch {
      this.$message.error('이미지 업로드 중 오류가 발생했습니다.');
    } finally {
      item.uploading = false;
    }
  }

  // ===== 등록 =====

  openRegisterDialog() {
    this.registerTeamUid = '';
    this.registerTeamName = '';
    this.registerDescription = '';
    this.registerImages = [this.newImageItem()];
    this.registerDialogVisible = true;
  }

  onRegisterTeamChange(uid: string) {
    const team = this.teamList.find((t) => t.uid === uid);
    if (team) this.registerTeamName = team.name;
  }

  resetRegister() {
    this.registerTeamUid = '';
    this.registerTeamName = '';
    this.registerDescription = '';
    this.registerImages = [];
  }

  async handleRegister() {
    if (!this.registerTeamUid) {
      this.$message.warning('팀을 선택하세요.');
      return;
    }
    if (this.registerImages.some((i) => !i.imageUrl)) {
      this.$message.warning('모든 이미지를 입력하세요.');
      return;
    }
    this.saving = true;
    try {
      await createTeamBanner({
        teamUid: this.registerTeamUid,
        teamName: this.registerTeamName,
        imageUrls: this.registerImages.map((i) => i.imageUrl),
        description: this.registerDescription,
      });
      this.$message.success('배너가 등록되었습니다.');
      this.registerDialogVisible = false;
      await this.fetchList();
    } catch {
      this.$message.error('저장 중 오류가 발생했습니다.');
    } finally {
      this.saving = false;
    }
  }

  // ===== 수정 =====

  openEditDialog(row: any) {
    this.editUid = row.uid;
    this.editTeamName = row.teamName;
    this.editDescription = row.description || '';
    this.editImages = (row.imageUrls && row.imageUrls.length > 0)
      ? row.imageUrls.map((url: string) => ({ imageUrl: url, inputType: 'url' as const, uploading: false }))
      : [this.newImageItem()];
    this.editDialogVisible = true;
  }

  resetEdit() {
    this.editUid = '';
    this.editTeamName = '';
    this.editDescription = '';
    this.editImages = [];
  }

  async handleEditSave() {
    if (this.editImages.some((i) => !i.imageUrl)) {
      this.$message.warning('모든 이미지를 입력하세요.');
      return;
    }
    this.saving = true;
    try {
      await updateTeamBanner(this.editUid, {
        imageUrls: this.editImages.map((i) => i.imageUrl),
        description: this.editDescription,
      });
      this.$message.success('수정되었습니다.');
      this.editDialogVisible = false;
      await this.fetchList();
    } catch {
      this.$message.error('저장 중 오류가 발생했습니다.');
    } finally {
      this.saving = false;
    }
  }

  // ===== 삭제 =====

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

<style scoped>
.image-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.image-list-label {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.image-row {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  margin-bottom: 8px;
  background: #fafafa;
}

.image-row__index {
  width: 20px;
  height: 20px;
  background: #0f2027;
  color: #fff;
  border-radius: 50%;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 4px;
}

.image-row__body {
  flex: 1;
  min-width: 0;
}

.img-preview-box {
  margin-top: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  overflow: hidden;
  display: inline-block;
  background: #f5f7fa;
}

.img-preview {
  display: block;
  max-width: 260px;
  max-height: 100px;
  object-fit: contain;
}
</style>
