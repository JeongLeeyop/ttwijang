<template>
  <div>
    <home417></home417>
    <div class="home4-8-2__wr">
      <div class="diary2-2-3__top">
        <el-button @click="goBack()">
          <img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
        </el-button>
      </div>
      <div v-loading="loading">
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
            :limit="1"
            :on-exceed="handleExceed"
          >
            <i slot="default" class="el-icon-plus"></i>
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
        </div>
      </div>
      <div class="home4-8-2__tail">
        <a class="delete" @click="handleDelete()">삭제</a>
        <a class="modify" @click="handleUpdate()">수정</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getReviewDetail, updateReview, deleteReview } from '@/api/review';
import { Component, Vue } from 'vue-property-decorator';

@Component({})
export default class extends Vue {
  mounted() {
    this.getReviewDetail();
  }

  private loading = true;

  private apiUrl = process.env.VUE_APP_BASE_API;

  private fileSizeLimit = 10;

  private form: any = {
    content: '',
    score: 0,
    photoes: [],
  }

  private goBack() {
    this.$router.push({ name: 'MyReview' });
  }

  private getReviewDetail() {
    this.loading = true;
    getReviewDetail(this.$route.params.idx).then((res) => {
      this.loading = false;
      this.form = res.data;
    });
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

  private handleExceed(files: any, fileList: any) {
      this.$message.warning('최대 파일 개수를 초과하였습니다.');
  }

  /* eslint-disable */
  private handleUpdate() {
    if (this.form.score < 1) {
      this.$message.warning('별점을 선택해주세요.');
      return false;
    }
    if (!this.form.content) {
      this.$message.warning('후기 내용을 입력해주세요.');
      return false;
    }
    // if (this.form.photoes.length < 1) {
      // this.$message.warning('후기 사진을 1장 이상 등록해주세요.');
      // return false;
    // }
    this.$confirm('정말 후기를 수정하시겠습니까?', {
      confirmButtonText: '수정',
      cancelButtonText: '취소',
    }).then(() => {
      updateReview(this.$route.params.idx, this.form).then(() => {
        this.$message.success('후기가 수정되었습니다.');
        this.$router.push({ name: 'MyReview' });
        // this.$router.push({ name: 'DeliveryDetail', params: { orderGroupId: this.$route.params.groupIdx, orderStatus: this.$route.params.orderStatus }});
      });
    });
  }

  private handleDelete() {
    this.$confirm('정말 후기를 삭제하시겠습니까?', {
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
    }).then(() => {
      deleteReview(this.$route.params.idx).then(() => {
        this.$message.success('후기가 삭제되었습니다.');
        this.$router.push({ name: 'MyReview' });
        // this.$router.go(-1);
      });
    });
  }
};
</script>
