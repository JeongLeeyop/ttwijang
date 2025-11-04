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

@Component
export default class UploadTeamMark extends Vue {
  private teamMarkImage = ''

  private goBack(): void {
    this.$router.go(-1);
  }

  private handleImageChange(file: any): void {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.teamMarkImage = e.target.result;
    };
    reader.readAsDataURL(file.raw);
  }

  private handleSubmit(): void {
    if (!this.teamMarkImage) {
      this.$message.warning('팀 마크를 업로드해주세요.');
      return;
    }
    console.log('Team mark image uploaded');

    // 팀 데이터 준비
    const teamFormData = JSON.parse(sessionStorage.getItem('teamFormData') || '{}');
    const completeTeamData = {
      ...teamFormData,
      logo: this.teamMarkImage,
    };

    // 세션 스토리지에 저장
    sessionStorage.setItem('teamFormData', JSON.stringify(completeTeamData));

    this.$message.success('팀 마크가 등록되었습니다!');
    // 팀 정보 입력 페이지로 이동
    this.$router.push('/team-info');
  }
}
</script>

<style scoped>
/* Styles moved to style.css */
</style>
