<template>
  <div>
    <el-input
      v-if="boardField && boardField.fieldTypeCode === 'TEXT'"
      v-model="inputValue"
      name="inputValue"
      style="width: 100%"
      :placeholder="`${boardField.fieldName}을(를) 입력하세요`"
      :data-vv-as="`${boardField.fieldName} `"
      :maxlength="boardField.inputLimit"
    />
    <el-input
      v-if="boardField && boardField.fieldTypeCode === 'TEXT_AREA'"
      type="textarea"
      :rows="12"
      :contents="inputValue"
      @change="handleChangeContents"
    />
    <el-input
      v-if="boardField && boardField.fieldTypeCode === 'DATE'"
      v-model="inputValue"
      type="date"
      name="inputValue"
      :data-vv-as="`${boardField.fieldName} `"
    />
    <div v-if="boardField && boardField.fieldTypeCode === 'FILE'">
      <div
        v-for="(postFile, index) in fileList"
        :key="postFile.file.uid"
      >
        {{ postFile.file.originalName }}
        ({{ Math.round(postFile.file.fileSize / 1024)}}KB)
        <button @click="handleClickRemoveFile(index)">삭제</button>
      </div>
      <label :for="boardField.uid" style="cursor: pointer; padding: 0 10px; border: 1px solid #a1a1a1;">업로드</label>
      <p>최대 {{ boardField.inputLimit }}개, {{ boardField.fileSizeLimit }}MB 까지 업로드 가능합니다.</p>
      <input
        :id="boardField.uid"
        ref="inputFile"
        type="file"
        style="display: none;"
        @change="(e) => handleChangeFile(e, false)"
      />
    </div>
    <div v-if="boardField && boardField.fieldTypeCode === 'PHOTO'">
      <el-upload ref="upload"
          :action="`${apiUrl}/post/upload`"
          list-type="picture-card"
          :file-list="fileList"
          :before-upload="(file) => handleBeforeUpload(file, true)"
          :on-success="handleSuccessUploadImageFile"
          :on-remove="handleRemoveImageFile"
          :auto-upload="true"
          :limit="boardField.inputLimit"
          >
        <img class="el-upload-list__item-thumbnail" src="/img/qna.7f98cb5f.png" alt="">
          <template slot="file" slot-scope="{file}">
            <img
              class="el-upload-list__item-thumbnail"
              :src="`/api/attached-file/${file.file.uid}`" alt=""
            >
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="handleRemoveImageFile(file)">
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </template>
        </el-upload>
      <p>최대 {{ boardField.inputLimit }}개, {{ boardField.fileSizeLimit }}MB 까지 업로드 가능합니다.</p>
    </div>
      </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';
import { uploadFile } from '@/api/attachedFile';
import { IBoardField } from '@/types';

@Component({
  name: 'BoardField',
 })
export default class extends Vue {
  @Prop({ required: true }) private boardField!: any

  @Prop({ required: true }) private fieldValue!: string

  @Prop({ default: [] }) private fileList!: any[]

  @Watch('inputValue')
  private onInputValueChange() {
    this.$emit('change', this.inputValue);
  }

  private tossPayments = (window as any).TossPayments('live_ck_4vZnjEJeQVxGBjMzwEM3PmOoBN0k');

  mounted() {
    this.inputValue = this.fieldValue;
  }

  private apiUrl = process.env.VUE_APP_COMMON_API;

  private inputValue: string | number = '';

   private handleChangeFile(e: any, isImageUpload: boolean) {
    const file = e.target.files[0];
    if (this.boardField.fileSizeLimit * 1024 * 1024 < file.size) {
      this.$message.warning(`최대 ${this.boardField.fileSizeLimit}MB 까지 업로드 가능합니다.`);
      return;
    }
    if (this.boardField.inputLimit <= this.fileList.length) {
      this.$message.warning(`최대 ${this.boardField.inputLimit}개까지 업로드 가능합니다.`);
      return;
    }
    if (this.boardField.fieldTypeCode === 'PHOTO') {
      if (!file.type.startsWith('image')) {
        this.$message.warning('이미지 형식만 업로드 가능합니다.');
        return;
      }
    }
    const formData = new FormData();
    formData.append('file', e.target.files[0]);
    uploadFile('post', formData).then((res) => {
      this.$emit('uploadFile', res.data);
    });
  }

  private handleClickRemoveFile(index: number) {
    this.$emit('removeFile', this.fileList[index].file.uid);
  }

  private getValidator() {
    let validators = '';
    if (this.boardField.requiredState) validators = 'required';
    return validators;
  }

  private handleChangeContents(contents: string) {
    this.inputValue = contents;
  }

  private base64toFile(base64String: string, filename: string, mimeType: string): File | null {
  const byteCharacters = atob(base64String);
  const byteArrays = [];

  for (let offset = 0; offset < byteCharacters.length; offset += 512) {
    const slice = byteCharacters.slice(offset, offset + 512);

    const byteNumbers = new Array(slice.length);
    for (let i = 0; i < slice.length; i += 1) {
      byteNumbers[i] = slice.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);
    byteArrays.push(byteArray);
  }

  const blob = new Blob(byteArrays, { type: mimeType });
  try {
    const file = new File([blob], filename, { type: mimeType });
    return file;
  } catch (e) {
    // For some browsers that do not support the File constructor (e.g., Safari)
    const file = new Blob([blob], { type: mimeType });
    return file as File;
  }
}

  private handleBeforeUpload(uploadFile2:any, isPhoto:boolean) {
    const fileSizeLimitByMb = this.boardField.fileSizeLimit * 1024 * 1024;
    if (isPhoto) {
      const isImageFile = uploadFile2.type.split('/')[0] === 'image';
      if (!isImageFile) {
        this.$message.warning('이미지 파일만 업로드 가능합니다.');
        return false;
      }
    }
    if (uploadFile2.size > fileSizeLimitByMb) {
      this.$message.warning(`파일 업로드 최대용량은 ${this.boardField.fileSizeLimit}MB입니다.`);
      return false;
    }
    if (this.boardField.inputLimit <= this.fileList.length) {
      this.$message.warning(`최대 ${this.boardField.inputLimit}개까지 업로드 가능합니다.`);
      return false;
    }
    return true;
  }

  private handleRemoveImageFile(file: any) {
    this.$emit('removeFile', file.fileUid);
  }

  private handleSuccessUploadImageFile(res: any) {
    this.$emit('uploadFile', res);
  }
}
</script>
