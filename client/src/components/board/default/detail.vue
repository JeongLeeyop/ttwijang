<template>
  <div class="board-wrap">
    <div class="home4-9-2__top">
      <div class="home4-9-2__box">
        <p class="home492__text">{{ post.title }}</p>
        <p class="home492__day">{{ post.createDate | parseDate('YYYY.MM.DD') }}</p>
      </div>
      <div class="home4-9-2__bot">
        <p class="home492__btext" v-html="post.content" />
      </div>
      <div class="home4-9-2__tail">
        <a href="#" @click="handleList()">목록</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { checkPostPassword, deletePost, deleteSecretPost } from '@/api/post';
import { UserModule } from '@/store/modules/user';
import { IBoard, IPost } from '@/types';
import { Vue, Component, Prop } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';

@Component({
  name: 'BoardDefaultDetail',
  components: {
    Pagination,
  },
})
export default class extends Vue {
  @Prop({ required: true }) private board!: IBoard

  @Prop({ required: true }) private post!: IPost

  private passwordCheckVisible = false;

  private mode = '';

  private password = '';

  private commentForm = {
    contents: '',
  }

  private subCommentForm = {
    contents: '',
  }

  private commentList = [
    {
      uid: 'sdfg',
      writer: 'abcd***e',
      contents: '댓글내용댓글내용1',
      createDate: '2020-06-24 22:57:36',
      recommenNum: 3,
      childView: true,
      info: false,
      comments: [
        {
          uid: 'sadfagdf',
          writer: 'abcd***e',
          contents: '답글내용답글내용답글내용1',
          createDate: '2022-07-21 16:19',
          recommenNum: 0,
          info: false,
        },
        {
          uid: 'dfahwedf',
          writer: 'abcd***e',
          contents: '답글내용답글내용답글내용1',
          createDate: '2022-07-21 16:19',
          recommenNum: 0,
          info: false,
        },
      ],
    },
    {
      uid: 'dasgadfwedf',
      writer: 'abcd***e',
      contents: '댓글내용댓글내용1',
      createDate: '2022-07-21 16:19',
      recommenNum: 3,
      childView: false,
      info: false,
      comments: [
        {
          uid: 'sadfagasgaedf',
          writer: 'abcd***e',
          contents: '답글내용답글내용답글내용1',
          createDate: '2022-07-21 16:19',
          recommenNum: 0,
          info: false,
        },
        {
          uid: 'dfahwehsaegdf',
          writer: 'abcd***e',
          contents: '답글내용답글내용답글내용1',
          createDate: '2022-07-21 16:19',
          recommenNum: 0,
          info: false,
        },
      ],
    },
  ];

  private handleCommentBox(index: number) {
    this.commentList[index].childView = !this.commentList[index].childView;
  }

  private handleAmendBox(index: number, subIndex: number) {
    if (subIndex === undefined) this.commentList[index].info = !this.commentList[index].info;
    else this.commentList[index].comments[subIndex].info = !this.commentList[index].comments[subIndex].info;
  }

  get isAdmin() {
    return UserModule.roles.indexOf('ROLE_ADMIN') > -1;
  }

  get fileUrl() {
    return `${process.env.VUE_APP_COMMON_API}/attached-file`;
  }

  private handleClickDelete() {
    if (this.board.authWrite === 'GUEST' && !this.isAdmin) {
      this.passwordCheckVisible = true;
      this.mode = 'delete';
      return;
    }
    this.$confirm('정말 게시글을 삭제하시겠습니까?').then(() => {
      deletePost(this.$route.params.postUid).then(() => {
        this.$message.success('게시글이 삭제되었습니다.');
        this.$router.push({ name: 'BoardIndex', params: { boardUid: this.$route.params.boardUid } });
      }).catch((error) => {
        this.$message.error(error.response.data.message || '게시글을 삭제하는데 실패했습니다.');
      });
    });
  }

  private handleClickUpdate() {
    if (this.board.authWrite === 'GUEST' && !this.isAdmin) {
      this.passwordCheckVisible = true;
      this.mode = 'update';
    } else {
      this.$router.push({
        name: 'PostUpdate',
        params: {
          boardUid: this.$route.params.boardUid,
          postUid: this.$route.params.postUid,
        },
      });
    }
  }

  private getWriteAuth() {
    let hasWriteAuth = false;
    const userRoles = UserModule.roles;
    if (this.board.authWrite === 'GUEST') return true;
    if (userRoles.indexOf('ROLE_ADMIN') > -1) return true;
    userRoles.forEach((role: string) => {
      if (this.board.writeRoles.indexOf(role) > -1) hasWriteAuth = true;
    });
    return hasWriteAuth;
  }

  private handleList() {
    this.$router.push({
      name: 'BoardIndex',
      params: { boardUid: this.$route.params.boardUid },
      query: { ...this.$route.query },
    });
  }
}
</script>

<style lang="scss">
  .file-link {
    color: #4285ec;
    display: inline-block;
  }
.notice-answer pre { padding: 20px; font-size: 16px; }
.notice-title { width: 100%; font-size: 20px; font-weight: 400; padding: 20px 20px; border-top: 4px solid #0269b4; }
.notice-info {
  padding: 0 20px 30px; font-weight: 400; display: flex; height: 100%; border-bottom: 1px solid #c8c8c8; flex-wrap: wrap;
  p { font-size: 15px;  display: flex; align-items: center; margin: 0 20px 10px 0; font-weight: 400; span { color: #fff; padding: 10px 20px; border-radius: 30px; margin-right: 10px; }  &:first-child span{background: #0097d8;} &:last-child span { background: #006bb1; } } }
.notice-download {
  padding: 10px 10px; background-color: #f7f7f7; width: 100%!important; border-top: 1px solid #c8c8c8;
  display: flex; flex-wrap: wrap; justify-content: center; list-style-type: none; &__item { padding: 20px 10px; text-align: center; }
}
</style>
