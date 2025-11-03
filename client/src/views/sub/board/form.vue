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
      <slot v-if="board">
        <template v-for="(useCategory, index) in board.categoryList">
          <el-form-item
            v-if="formData.categoryList[index] && !$route.query.parentUid"
            class="error-form-position form-item-side"
            :key="useCategory.uid"
            :prop="`categoryList.${index}.categoryUid`"
            :rules="{ required: true, message: `카테고리를을 선택하세요.`, tigger: 'blur' }"
          >
            <el-select
              v-model="formData.categoryList[index].categoryUid"
              :placeholder="useCategory.category.name"
              :disabled="$route.params.postUid"
              :popper-append-to-body="false"
            >
              <el-option
                v-for="category in useCategory.category.children"
                class="form-item-side"
                :key="category.uid"
                :label="category.name"
                :value="category.uid"
              />
            </el-select>
          </el-form-item>
        </template>
      </slot>
      <el-form-item prop="title" class="error-form-position form-item-title">
        <el-input v-model="formData.title" placeholder="제목을 입력해주세요."></el-input>
      </el-form-item>
    </div>
      <!--
      <el-form-item prop="writer" class="error-form-position">
        <el-input v-model="formData.writer" placeholder="작성자를 입력해주세요."></el-input>
      </el-form-item>
      -->
      <el-form-item class="board_form" prop="content">
        <!-- <SummerNote
          v-if="board"
          :contents="formData.content"
          @change="handleChangeContents"
        /> -->
        <el-input
          type="textarea"
          resize="none"
          placeholder="내용을 입력해주세요."
          v-model="formData.content"
          v-if="board"
          @change="handleChangeContents">
        </el-input>
      </el-form-item>
      <el-form-item v-for="(postData, index) in formData.dataList" :key="postData.fieldUid">
        <BoardField
          ref="boardField"
          :boardField="postData.field"
          :fieldValue="postData.inputValue"
          :fileList="getFileList(index)"
          @change="(value) => handleChangeInputValue(value, index)"
          @uploadFile="(file) => handleUploadFile(file, index)"
          @removeFile="handleRemoveFile"
        />
      </el-form-item>
      <el-form-item v-if="board.fileUseState" class="notice-file">
        <div class="notice-file__label">
          <figure>
            <!--
            <img src="@/assets/img/download-icon.png" alt="">
            -->
          </figure>
          <p class="gft">첨부파일</p>
        </div>
        <label for="attached-file" class="upload-btn">파일선택</label>
        <input
          id="attached-file"
          type="file"
          style="display: none;"
          v-on:change="handleChangeFile"
        />
        <ul class="notice-file__list">
          <li v-if="getAttachedFileList().length < 1">파일 없음.</li>
          <li v-for="postFile in getAttachedFileList()" :key="postFile.file.uid">
            {{ postFile.file.originalName }} &nbsp;&nbsp;
            <el-button @click="handleDeleteFile(postFile.fileUid)" type="text">x</el-button>
          </li>
        </ul>
      </el-form-item>
      <el-form-item class="notice-form__btn">
        <el-button class="gft" style="border-color: #58c0d6; background-color: #fff; color: #58c0d6;" @click="handleClickCancle()">취소</el-button>
        <el-button class="submit gft" style="border-color:#58C0D6; background-color: #58C0D6; color: #fff;" @click="handleClickSubmit()">작성완료</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Watch,
Prop,
} from 'vue-property-decorator';
import {
  addPost,
  checkPostPassword,
  getPost,
  updatePost,
  updateSecretPost,
} from '@/api/post';
import { uploadFile } from '@/api/attachedFile';
import { IBoardCategory } from '@/types/boardCategory';
import { UserModule } from '@/store/modules/user';
import BoardField from '@/components/board/field.vue';
import SummerNote from '@/components/SummerNote/index.vue';
import { Form } from 'element-ui';
import { getBoard } from '@/api/board';

@Component({
  name: 'BoardForm',
  components: {
    BoardField,
    SummerNote,
  },
})
export default class extends Vue {
  @Prop({ required: true }) private board!: any;

  @Watch('$route')
  private handleChangeMenu() {
    this.init();
  }

  mounted() {
    this.init();
  }

  get isAdmin() {
    return UserModule.roles.indexOf('ROLE_ADMIN') > -1;
  }

  private formData: any = {
    boardUid: '',
    title: '',
    content: '',
    parentUid: null,
    writer: '',
    password: '',
    categoryList: [],
    dataList: [],
    fileList: [],
    noticeStatus: false,
  };

  private categoryValidator: any = (rules: any, value: any, callback: Function) => {
    if (this.selectCategory.length < 1) {
      callback(new Error('분류를 선택하세요.'));
    }
    callback();
  };

  private rules = {
    title: [
      { required: true, message: '제목을 입력하세요.', trigger: 'blur' },
    ],
    categoryList: [
      { validator: this.categoryValidator, trigger: 'blur' },
    ],
    writer: [
      { required: true, message: '작성자를 입력하세요.', trigger: 'blur' },
    ],
  };

  private loading = false;

  private selectCategory: any = {};

  private handleChangeInputValue(value: string, index: number) {
    this.formData.dataList[index].inputValue = value;
  }

  private init() {
    this.formData = {
      boardUid: '',
      title: '',
      content: '',
      parentUid: null,
      writer: '',
      password: '',
      categoryList: [],
      dataList: [],
      fileList: [],
      noticeStatus: false,
    };
    this.loading = true;
    this.formData.boardUid = this.$route.params.boardUid;
    if (!this.$route.params.postUid) {
      this.board.fieldList.forEach((boardField: any) => {
        const postData: any = {
          field: boardField,
          fieldUid: boardField.uid,
          inputValue: '',
        };
        this.formData.dataList.push(postData);
      });
      this.board.categoryList.forEach((useCategory: any) => {
        this.formData.categoryList.push({
          categoryUid: '',
        });
      });
      if (this.$route.query.parentUid) {
        getPost(String(this.$route.query.parentUid)).then((response) => {
          this.formData.title = `Re : ${response.data.title}`;
          this.formData.content = `${response.data.title}<p>========================================</p>`;
          this.formData.parentUid = response.data.uid;
          this.formData.categoryList = response.data.categoryList;
        });
      }
      this.loading = false;
    } else {
      getPost(this.$route.params.postUid).then((res: any) => {
        this.formData = res.data;
        /*
        if (response.data.categoryList.length > 0) {
          this.selectCategory = {
            categoryUid: response.data.categoryList[0].categoryUid,
            category: response.data.categoryList[0].category,
          };
        }
        */
        const newDataList: any[] = [];
        this.board.fieldList.forEach((boardField: any, index: number) => {
          const dataIndex = this.formData.dataList.findIndex((data: any) => data.fieldUid === boardField.uid);
          if (dataIndex < 0) {
            const postData: any = {
              field: boardField,
              fieldUid: boardField.uid,
              inputValue: '',
            };
            newDataList.push(postData);
          } else {
            let postData = this.formData.dataList[dataIndex];
            postData = {
              field: boardField,
              fieldUid: postData.fieldUid,
              inputValue: postData.inputValue,
            };
            newDataList.push(postData);
          }
        });
        this.formData.dataList = newDataList;
        if (this.formData.categoryList.length !== this.board.categoryList.length) {
          this.board.categoryList.forEach((useCategory: any, idx: number) => {
            if (this.formData.categoryList.legnth < idx) {
              this.formData.categoryList.push({
                categoryUid: '',
              });
            }
          });
        }
      });
      this.loading = false;
    }
  }

  private handleClickSubmit() {
    (this.$refs.noticeForm as Form).validate((valid: boolean) => {
      if (valid) {
        this.loading = true;
        // this.convertCategory();
        if (this.$route.params.postUid) {
          if (this.$route.query.password) {
            updateSecretPost(this.$route.params.postUid, this.formData, String(this.$route.query.password)).then((res) => {
              this.loading = false;
              this.$router.push({ name: 'PostDetail', params: { boardUid: this.$route.params.boardUid, postUid: res.data.uid } });
            });
            return;
          }
          updatePost(this.$route.params.postUid, this.formData).then((res: any) => {
            this.loading = false;
            this.$router.push({ name: 'PostDetail', params: { boardUid: this.$route.params.boardUid, postUid: res.data.uid } });
          }).catch((error) => {
            alert(error.response.data.message || '게시글을 수정하는데 실패했습니다.');
            this.loading = false;
          });
        } else {
          addPost(this.formData).then((res: any) => {
            this.loading = false;
            this.$router.push({ name: 'PostDetail', params: { boardUid: this.$route.params.boardUid, postUid: res.data.uid } });
          }).catch((error) => {
            alert(error.response.data.message || '게시글을 추가하는데 실패했습니다.');
            this.loading = false;
          });
        }
      }
    });
  }

  /* eslint-disable */
  private handleClickCancle() {
    if (this.$route.params.postUid) {
      this.$router.push({ name: 'PostDetail', params: { boardUid: this.$route.params.boardUid, postUid: this.$route.params.postUid } });
    } else {
      this.$router.push({ name: 'BoardIndex', params: { boardUid: this.$route.params.boardUid } });
    }
  }
  /* eslint-enable */

  private handleUploadFile(data: any, index: number) {
    const boardField: any = this.board.fieldList[index];
    let postFile = {};
      postFile = {
        file: data,
        fileUid: data.uid,
        fieldUid: boardField.uid,
        viewOrder: 0,
      };
    this.formData.fileList.push(postFile);
    this.formData.dataList[index].inputValue = data.uid;
  }

  private getFileList(index: number) {
    const boardField: any = this.board.fieldList[index];
    const fileList: any[] = [];
    this.formData.fileList.forEach((file: any) => {
      if (file.fieldUid === boardField.uid) fileList.push(file);
    });
    return fileList;
  }

  private handleRemoveFile(fileUid: string) {
    const fileIndex = this.formData.fileList.findIndex((file: any) => file.file.uid === fileUid);
    if (fileIndex > -1) this.formData.fileList.splice(fileIndex, 1);
  }

  private handleChangeFile(e: any) {
    const file = e.target.files[0];
    if (this.board.fileSizeLimit * 1024 * 1024 < file.size) {
      alert(`최대 ${this.board.fileSizeLimit}MB 까지 업로드 가능합니다.`);
      return;
    }
    if (this.board.fileCountLimit <= this.getAttachedFileList().length) {
      alert(`최대 ${this.board.fileCountLimit}개까지 업로드 가능합니다.`);
      return;
    }
    const formData = new FormData();
    formData.append('file', e.target.files[0]);
    uploadFile('post', formData).then((res: any) => {
      const postFile = {
        fileUid: res.data.uid,
        fieldUid: null,
        viewOrder: 0,
        file: res.data,
      };
      this.formData.fileList.push(postFile);
    });
  }

  private getAttachedFileList() {
    const attachedFileList: any[] = [];
    this.formData.fileList.forEach((postFile: any) => {
      if (!postFile.fieldUid) attachedFileList.push(postFile);
    });
    return attachedFileList;
  }

  private handleDeleteFile(fileUid: string) {
    const fileIndex = this.formData.fileList.findIndex((postFile: any) => postFile.fileUid === fileUid);
    if (fileIndex > -1) this.formData.fileList.splice(fileIndex, 1);
  }

  private convertCategory() {
    this.formData.categoryList = [];
    if (this.selectCategory.uid) {
      const postCategory: any = {
        categoryUid: this.selectCategory.uid,
        category: this.selectCategory.category,
      };
      this.formData.categoryList.push(postCategory);
    }
  }

  private handleChangeContents(value: string) {
    this.formData.content = value;
  }

  private fileSizeLimit = 10;

  private form: any = {
    orderGroupIdx: Number(this.$route.params.idx),
    content: '',
    score: 0,
    photoes: [],
  }

  private handleBeforeUpload(uploadFile2:File, isPhoto:boolean) {
    const fileSizeLimitByMb = this.fileSizeLimit * 1024 * 1024;
    if (isPhoto) {
      const isImageFile = uploadFile2.type.split('/')[0] === 'image';
      if (!isImageFile) {
        this.$message.warning('이미지 파일만 업로드 가능합니다.');
        return false;
      }
    }
    if (uploadFile2.size > fileSizeLimitByMb) {
      this.$message.warning(`파일 업로드 최대용량은 ${this.fileSizeLimit}MB입니다.`);
      return false;
    }
    return true;
  }

  private handleSuccessUploadImageFile(res: any) {
    this.form.photoes.push({ fileUid: res.uid });
  }

  private handleRemoveImageFile(file: any) {
    const index = this.form.photoes.findIndex((imgFile: any) => imgFile.fileUid === file.fileUid);
    if (index > -1) this.form.photoes.splice(index, 1);
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
