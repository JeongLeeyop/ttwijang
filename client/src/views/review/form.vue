<template>
  <div>
    <home417></home417>
    <div class="home4-8-2__wr">
      <div class="diary2-2-3__top">
        <el-button @click="goBack()">
          <img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
        </el-button>
      </div>
      <div class="home4-8-2__top">
        <p class="home482top">식단은 어떠셨나요?</p>
        <!--<p class="home482topa">샐러드 / 체중감량 / 2주플랜</p>-->
        <el-rate v-model="form.score"></el-rate>
      </div>
      <div class="home4-8-2__mid">
        <textarea v-model="form.content" placeholder="후기 내용을 입력해주세요." />
        <el-upload
          :action="`${apiUrl}/review/upload`"
          list-type="picture-card"
          :file-list="form.photoes"
          :before-upload="(file) => handleBeforeUpload(file, true)"
          :on-success="handleSuccessUploadImageFile"
          :on-remove="handleRemoveImageFile"
          :auto-upload="true"
          :limit="8"
        >
        <img  class="el-upload-list__item-thumbnail" src="/img/qna.7f98cb5f.png" alt="">
        <!-- <i slot="default" class="el-icon-plus">이미지 첨부</i> -->
          <template slot="file" slot-scope="{file}">
            <img
              class="el-upload-list__item-thumbnail"
              :src="`/api/attached-file/${file.fileUid}`" alt=""
            >
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="handleRemoveImageFile(file)">
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </template>
        </el-upload>
        <!--
        <ul>
          <li><img src="~@/assets/images/qna.png" class="" alt=""></li>
          <li><img src="~@/assets/images/qna-img.png" class="" alt=""></li>
          <li><img src="~@/assets/images/qna-img.png" class="" alt=""></li>
        </ul>
        -->
      </div>
      <div class="home4-8-2__tail">
        <a @click="handleSave()">작성완료</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { addReview } from '@/api/review';

@Component({
})
export default class extends Vue {
  private apiUrl = process.env.VUE_APP_BASE_API;

  private fileSizeLimit = 10;

  private form: any = {
    orderGroupIdx: Number(this.$route.params.idx),
    content: '',
    score: 0,
    photoes: [],
  }

  private goBack() {
    this.$router.push({ name: 'DeliveryDetail', params: { orderGroupId: this.$route.params.idx } });
  }

  private handleBeforeUpload(uploadFile:File, isPhoto:boolean) {
    const fileSizeLimitByMb = this.fileSizeLimit * 1024 * 1024;
    if (isPhoto) {
      const isImageFile = uploadFile.type.split('/')[0] === 'image';
      if (!isImageFile) {
        this.$message.warning('이미지 파일만 업로드 가능합니다.');
        return false;
      }
    }
    if (uploadFile.size > fileSizeLimitByMb) {
      this.$message.warning(`파일 업로드 최대용량은 ${this.fileSizeLimit}MB입니다.`);
      return false;
    }
    return true;
  }

  private handleRemoveImageFile(file: any) {
    const index = this.form.photoes.findIndex((imgFile: any) => imgFile.fileUid === file.fileUid);
    if (index > -1) this.form.photoes.splice(index, 1);
  }

  private handleSuccessUploadImageFile(res: any) {
    this.form.photoes.push({ fileUid: res.uid });
  }

  /* eslint-disable */
  private handleSave() {
    if (this.form.score < 1) {
      this.$message.warning('별점을 선택해주세요.');
      return false;
    }
    if (!this.form.content) {
      this.$message.warning('후기 내용을 입력해주세요.');
      return false;
    }
    if (this.form.photoes.length < 1) {
      this.$message.warning('후기 사진을 1장 이상 등록해주세요.');
      return false;
    }
    addReview(this.form).then(() => {
      this.$message.success('후기 작성이 완료되었습니다.');
      this.$router.go(-1);
    });
  }
}
</script>
