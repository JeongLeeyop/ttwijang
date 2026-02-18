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
        <button class="submit-button" @click="handleSubmit">다 음</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { uploadFile } from '@/api/attachedFile';

@Component
export default class UploadTeamMark extends Vue {
  private teamMarkImage = ''

  private teamMarkFile: File | null = null

  private goBack(): void {
    this.$router.go(-1);
  }

  private handleImageChange(file: any): void {
    this.teamMarkFile = file.raw;
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.teamMarkImage = e.target.result;
    };
    reader.readAsDataURL(file.raw);
  }

  private async handleSubmit(): Promise<void> {
    if (!this.teamMarkImage || !this.teamMarkFile) {
      this.$message.warning('팀 마크를 업로드해주세요.');
      return;
    }

    try {
      // 서버에 파일 업로드
      const formData = new FormData();
      formData.append('file', this.teamMarkFile);

      const uploadResponse = await uploadFile('team', formData);
      const uploadedFile = uploadResponse.data;

      if (uploadedFile && uploadedFile.uid) {
        // 팀 데이터 준비
        const teamFormData = JSON.parse(sessionStorage.getItem('teamFormData') || '{}');
        const completeTeamData = {
          ...teamFormData,
          logo: this.teamMarkImage, // 미리보기용
          logoFileUid: uploadedFile.uid, // 서버에 저장된 파일 UID
        };

        // 세션 스토리지에 저장
        sessionStorage.setItem('teamFormData', JSON.stringify(completeTeamData));

        this.$message.success('팀 마크가 등록되었습니다!');
        this.$router.push('/team-info');
      } else {
        this.$message.error('파일 업로드에 실패했습니다.');
      }
    } catch (error) {
      console.error('File upload failed:', error);
      this.$message.error('파일 업로드 중 오류가 발생했습니다.');
    }
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
