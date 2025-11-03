<template>
  <div
    class="wr-1400"
    style="margin-top: 80px;"
    v-loading="loading"
  >
    <el-form
      class="notice-form"
      ref="noticeForm"
      :model="formData"
      :rules="rules"
      @submit.native.prevent
    >
    <div class="board-head__box">
          <!-- <el-form-item
            class="error-form-position form-item-side"
            >
            <el-select
              v-model="formData.categoryUid"
              :rules="{ required: true, message: `카테고리를을 선택하세요.`, tigger: 'blur' }"
              :popper-append-to-body="false"
              placeholder="카테고리를 선택하세요"
            >
              <el-option
                v-for="category in board.categoryList"
                class="form-item-side"
                :key="category.uid"
                :label="category.name"
                :value="category.uid"
              />
            </el-select>
          </el-form-item> -->
      <!-- <el-form-item prop="title" class="error-form-position form-item-title"> -->
        <!-- <el-input v-model="formData.title" placeholder="제목을 입력해주세요."></el-input> -->
      <!-- </el-form-item> -->
      <!-- <el-switch v-model="formData.privateStatus"  active-text="전체공개" inactive-text="비공개"></el-switch> -->
    </div>
      <el-form-item class="board_form" prop="content">
        <el-input
          type="textarea"
          resize="none"
          placeholder="내용을 입력해주세요."
          v-model="formData.content"
          v-if="board"
          @change="handleChangeContents">
        </el-input>
      </el-form-item>
      <div>
      <el-upload ref="upload"
      :headers="requestHeaders"
          :action="`${apiUrl}/mission/record/upload`"
          list-type="picture-card"
          :file-list="formData.fileList"
          :before-upload="(file) => handleBeforeUpload(file, true)"
          :on-success="handleSuccessUploadImageFile"
          :on-remove="handleRemoveImageFile"
          :auto-upload="true"
          :on-exceed="handleExceed"
          :limit="1"
          >
        <img class="el-upload-list__item-thumbnail" src="/img/qna.7f98cb5f.png" alt="">
          <template slot="file" slot-scope="{file}">
            <img
              class="el-upload-list__item-thumbnail"
              :src="`/api/attached-file/${file.file.uid}`" alt=""
            >
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="handleRemoveImageFile(file.fileUid)">
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </template>
        </el-upload>
      <p>최대 1개, 10MB 까지 업로드 가능합니다.</p>
    </div>
      <el-form-item class="notice-form__btn">
        <el-button v-if="!this.uid" class="gft" style="border-color: #58c0d6; background-color: #fff; color: #58c0d6;" @click="handleClickCancel()">취소</el-button>
        <el-button v-else-if="this.uid" class="gft" style="border-color: red; background-color: red; color: #fff;" @click="handleClickRemove()">삭제</el-button>
        <el-button v-if="!this.uid" class="submit gft" style="border-color:#58C0D6; background-color: #58C0D6; color: #fff;" @click="handleClickSubmit()">작성완료</el-button>
        <el-button v-else-if="this.uid" class="submit gft" style="border-color:#58C0D6; background-color: #58C0D6; color: #fff;" @click="handleClickSubmit()">수정완료</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { uploadFile } from '@/api/attachedFile';
import {
addPost,
getPost,
updatePost,
updateSecretPost,
} from '@/api/post';
import {
  getMissionCategory,
  addMissionRecord,
  getMissionRecordDetail,
  updateMissionRecord,
  deleteMissionRecord,
} from '@/api/mission';
import SummerNote from '@/components/SummerNote/index.vue';
import BoardField from '@/components/board/field.vue';
import { UserModule } from '@/store/modules/user';
import { Form } from 'element-ui';
import { getToken } from '@/utils/cookies';
import {
Component,
Vue,
} from 'vue-property-decorator';

@Component({
  name: 'BoardIndex',
  components: {
    BoardField,
    SummerNote,
  },
})
export default class extends Vue {
  mounted() {
    this.init();
		this.getMissionCategory();
  }

  get requestHeaders() {
    return {
      Authorization: `Bearer ${getToken()}`,
    };
  }

  get isAdmin() {
    return UserModule.roles.indexOf('ROLE_ADMIN') > -1;
  }

  private apiUrl = process.env.VUE_APP_BASE_API;

  private board = {
    missionUid: '',
    fieldList: [],
    categoryList: [],
    fileSizeLimit: 0,
    fileCountLimit: 0,
  };

  private formData: any = {
    missionUid: '',
    title: '',
    content: '',
    categoryUid: '',
    fileList: [],
    privateStatus: false,
  };

  private categoryValidator: any = (rules: any, value: any, callback: Function) => {
    // if (this.selectCategory.length < 1) {
      // callback(new Error('분류를 선택하세요.'));
    // }
    // callback();
  };

  private rules = {
    // title: [
      // { required: true, message: '제목을 입력하세요.', trigger: 'blur' },
    // ],
    // categoryList: [
      // { validator: this.categoryValidator, trigger: 'blur' },
    // ],
    writer: [
      { required: true, message: '작성자를 입력하세요.', trigger: 'blur' },
    ],
  };

  private loading = false;

  private selectCategory: any = {};

  private uid: string = this.$route.params.uid;

  private init() {
    this.formData = {
      missionUid: '',
      content: '',
      categoryList: [],
      fileList: [],
    };
    this.loading = true;
    this.board.missionUid = this.$route.params.missionUid;
    this.formData.missionUid = this.$route.params.missionUid;
    this.loading = false;

    if (this.uid) {
      this.getMissionRecordDetail();
    }
  }

  private getMissionCategory() {
		getMissionCategory().then((res) => {
			this.board.categoryList = res.data;
		}).catch(() => {
			this.$message.warning('미션 리스트를 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

  private getMissionRecordDetail() {
		getMissionRecordDetail(this.uid).then((res) => {
      this.formData = res.data;

      let postFile = {};
      postFile = [{
        file: res.data.fileList[0].file,
        fileUid: res.data.fileList[0].fileUid,
        missionUid: this.$route.params.missionUid,
        url: `${this.apiUrl}/attached-file/${res.data.fileList[0].fileUid}`,
        viewOrder: 0,
      }];
      this.formData.fileList = postFile;
		}).catch(() => {
			this.$message.warning('미션 작성글을 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

  private handleClickSubmit() {
    (this.$refs.noticeForm as Form).validate((valid: boolean) => {
      if (valid) {
        this.loading = true;
          if (this.formData.fileList.length === 0) {
            this.$message.warning('사진을 첨부하세요.');
            this.loading = false;
          } else {
          console.log(1);
          if (this.uid) {
            updateMissionRecord(this.uid, this.formData).then((res: any) => {
              this.loading = false;
              this.$router.push({ name: 'MissionDetail', params: { missionUid: this.$route.params.missionUid } });
            }).catch((error) => {
              alert(error.response.data.message || '미션 수정을 작성하는데 실패했습니다.');
            this.loading = false;
          });
          } else {
            addMissionRecord(this.formData).then((res: any) => {
              this.loading = false;
              this.$router.push({ name: 'MissionDetail', params: { missionUid: this.$route.params.missionUid } });
            }).catch((error) => {
              alert(error.response.data.message || '미션 기록을 작성하는데 실패했습니다.');
            this.loading = false;
          });
            }
        }
      }
    });
  }

  private handleClickRemove() {
    this.loading = true;
    this.$confirm('정말 미션 기록을 삭제하시겠습니까?', {
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
    }).then(() => {
      deleteMissionRecord(this.uid).then((res: any) => {
        this.$message.warning('미션 기록을 삭제하였습니다.');
        this.$router.push({ name: 'MissionDetail', params: { missionUid: this.$route.params.missionUid } });
      }).catch((error) => {
        alert(error.response.data.message || '미션 기록을 작성하는데 실패했습니다.');
      });
    });
    this.loading = false;
  }

  /* eslint-disable */
  private handleClickCancel() {
      this.$router.push({ name: 'Mission' });
  }
  /* eslint-enable */

  private handleChangeContents(value: string) {
    this.formData.content = value;
  }

  private handleExceed(files: any, fileList: any[]) {
    this.$message.warning('최대 첨부가능한 수는 1개 입니다.');
  }

  private handleBeforeUpload(uploadFile2:any, isPhoto:boolean) {
    const fileSizeLimitByMb = 10 * 1024 * 1024;
    if (isPhoto) {
      const isImageFile = uploadFile2.type.split('/')[0] === 'image';
      if (!isImageFile) {
        this.$message.warning('이미지 파일만 업로드 가능합니다.');
        return false;
      }
    }
    if (uploadFile2.size > fileSizeLimitByMb) {
      this.$message.warning('파일 업로드 최대용량은 10MB입니다.');
      return false;
    }
    if (this.formData.fileList.length >= 1) {
      this.$message.warning('최대 1개까지 업로드 가능합니다.');
      return false;
    }
    return true;
  }

  private handleSuccessUploadImageFile(data: any) {
    let postFile = {};
      postFile = {
        file: data,
        fileUid: data.uid,
        missionUid: this.board.missionUid,
        viewOrder: 0,
      };
    this.formData.fileList.push(postFile);
  }

  private handleRemoveImageFile(fileUid: string) {
    const fileIndex = this.formData.fileList.findIndex((postFile: any) => postFile.fileUid === fileUid);
    console.log(fileUid);
    if (fileIndex > -1) this.formData.fileList.splice(fileIndex, 1);
  }
}
</script>

<style lang="scss">
.notice-form {
  margin-bottom: 5rem;
  padding:10px;
  .upload-btn {
    display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    background: #FFF;
    border: 1px solid #DCDFE6;
    color: #606266;
    font-weight: 500;
    padding: 12px 20px;
    font-size: 14px;
    border-radius: 10px;
  }
  .form-item-side{
    display: inline-block;
    width: 100%;
    margin-bottom:5px !important;
    .el-input{
      font-size:13px;
      .el-input__inner{
        padding:0 10px;
      }
    }
  }
  .form-item-title{
    display: inline-block;
    // width: calc(100% - 160px);
  }
}
.notice-file {
  padding: 30px 25px; background-color: #f9f9f9; border-top: 2px solid #ebebeb; border-bottom: 2px solid #ebebeb ;
  &__label { display: flex; align-items: center; margin-right: 15px; figure { margin-right: 10px; } p { font-size: 17px; margin-top: 6px; }}
  .el-form-item__content { display: flex; align-items: center; flex-wrap: wrap;} &__list { display: flex; flex-wrap: wrap; li { margin-left: 15px; span { color: rgb(165, 165, 165); &:hover { color: black; } } } }
  .el-button { border-radius: 10px; }
}
.editor-wrapper { .el-tabs--border-card>.el-tabs__content { padding: 0px; } .el-tabs__nav { float: right; } }
.form-item-side.el-select-dropdown__item{
  font-size: 14px !important;
  line-height: 35px !important;
  height: 35px !important;
  padding: 0 10px;
  margin: 0 10px;
}
</style>
