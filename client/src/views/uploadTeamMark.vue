<template>
  <div class="upload-team-mark-page">
    <!-- Header -->
    <div class="header">
      <button class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
    </div>

    <!-- Content -->
    <div class="content">
      <div class="form-container">
        <h2 class="page-title">팀 마크를 만들어 주세요!</h2>

        <!-- Team Mark Upload -->
        <div class="form-group">
          <label>팀 마크</label>
          <el-upload
            class="team-mark-upload"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :limit="1"
            accept="image/*"
          >
            <div class="upload-area" v-if="!teamMarkImage">
              <i class="el-icon-picture-outline"></i>
              <span>마크를 업로드</span>
            </div>
            <div class="preview-area" v-else>
              <img :src="teamMarkImage" :alt="teamMarkImage" class="preview-image">
            </div>
          </el-upload>
        </div>

        <!-- Submit Button -->
        <button class="submit-button" @click="handleSubmit" :disabled="isSubmitting">
          {{ isSubmitting ? '업로드 중...' : '다 음' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { uploadFile } from '@/api/attachedFile';

const MAX_FILE_SIZE = 100 * 1024 * 1024;

@Component
export default class UploadTeamMark extends Vue {
  private teamMarkImage = ''

  private teamMarkFile: File | null = null

  private isSubmitting = false

  beforeDestroy(): void {
    if (this.teamMarkImage.startsWith('blob:')) {
      URL.revokeObjectURL(this.teamMarkImage);
    }
  }

  private goBack(): void {
    this.$router.go(-1);
  }

  private handleImageChange(file: any): void {
    if (file.raw.size > MAX_FILE_SIZE) {
      this.$message.error('파일 용량은 100MB를 초과할 수 없습니다.');
      return;
    }
    if (this.teamMarkImage.startsWith('blob:')) {
      URL.revokeObjectURL(this.teamMarkImage);
    }
    this.teamMarkFile = file.raw;
    this.teamMarkImage = URL.createObjectURL(file.raw);
  }

  private async handleSubmit(): Promise<void> {
    if (!this.teamMarkImage || !this.teamMarkFile) {
      this.$message.warning('팀 마크를 업로드해주세요.');
      return;
    }
    if (this.isSubmitting) return;
    this.isSubmitting = true;

    try {
      const formData = new FormData();
      formData.append('file', this.teamMarkFile);

      const uploadResponse = await uploadFile('team', formData);
      const uploadedFile = uploadResponse.data;

      if (uploadedFile && uploadedFile.uid) {
        const teamFormData = this.$store.state.teamCreation.formData;
        const completeTeamData = {
          ...teamFormData,
          logo: this.teamMarkImage,
          logoFileUid: uploadedFile.uid,
        };

        this.$store.commit('teamCreation/SET_FORM_DATA', completeTeamData);

        this.$message.success('팀 마크가 등록되었습니다!');
        this.$router.push('/team-info');
      } else {
        this.$message.error('파일 업로드에 실패했습니다.');
      }
    } catch (error) {
      console.error('File upload failed:', error);
      if ((error as any).code === 'ECONNABORTED') {
        this.$message.error('업로드 시간이 초과되었습니다. 네트워크 상태를 확인해주세요.');
      } else if ((error as any).response?.status === 413) {
        this.$message.error('파일 용량이 너무 큽니다.');
      } else {
        this.$message.error('파일 업로드 중 오류가 발생했습니다.');
      }
    } finally {
      this.isSubmitting = false;
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
