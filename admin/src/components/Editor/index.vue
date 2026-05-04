<template>
  <div>
    <div id="editor"/>
  </div>
</template>
<script lang="ts">
import {
  Component,
  Vue,
  Prop,
  Watch,
} from 'vue-property-decorator';
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import Editor from '@toast-ui/editor';
import 'highlight.js/styles/github.css';
import '@toast-ui/editor/dist/i18n/ko-kr';
import codeSyntaxHightlight from '@toast-ui/editor-plugin-code-syntax-highlight';
import hljs from 'highlight.js';
import { upload } from '@/api/attached-file';

@Component({
  name: 'toastEditor',
})

export default class extends Vue {
  @Watch('inputValue')
  private handleChangeInputValue(inputValue: string) {
    this.editor.setHTML(inputValue);
  }

  @Prop({ required: true }) private inputValue!: string;

  private handleChangeEditor() {
    this.$emit('changeValue', this.editor.getHTML());
  }

  private editor: any = null;

  private editorValue = '';

  mounted() {
    const el: any = document.querySelector('#editor');
    let ready = false;
    this.editor = new Editor({
      el,
      language: 'ko-KR',
      previewStyle: 'vertical',
      height: '500px',
      hooks: {
        addImageBlobHook: async (blob: any, callback: any) => {
          const fileUid = await this.handleUploadImage(blob);
          if (fileUid) {
            callback(`${process.env.VUE_APP_BASE_API}/attached-file/${fileUid}`, '');
          }
          return false;
        },
      },
      events: {
        change: () => { if (ready) this.handleChangeEditor(); },
      },
    });
    if (this.inputValue) {
      this.editor.setHTML(this.inputValue);
    }
    ready = true;
  }

  private async handleUploadImage(blob: any): Promise<string | false> {
    const formData = new FormData();
    formData.append('file', blob);
    try {
      const res = await upload('attached-file', formData);
      return res.data.uid;
    } catch {
      alert('이미지를 업로드 하는데 실패했습니다.');
      return false;
    }
  }
}
</script>

<style>
#editor {
  text-align: left;
}
</style>
