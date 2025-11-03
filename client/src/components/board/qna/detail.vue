<template>
  <div class="board-wrap">
    <div class="home4-10-1__wr">
      <div class="home4-10-3__top">
        <div class="home4-10-3__box">
          <template v-if="post && post.categoryList.length > 0">
            <p v-for="(postCategory, index) in post.categoryList" class="home4103__ti" :key="index">
              {{ postCategory.category.name }}
            </p>
          </template>
          <p class="home4103__title">{{ post.title }}</p>
          <p class="home4103__content" v-html="post.content"></p>
          <img
            v-for="postFile in post.fileList"
            :key="postFile"
            :src="`${fileUrl}/${postFile.fileUid}`"
            class="fa"
            alt=""
          >
          <p class="home4101__day">{{ post.createDate | parseDate('YYYY.MM.DD') }}</p>
        </div>
      </div>

      <slot v-if="post.children && post.children.length > 0">
        <div v-for="reply in post.children" :key="reply.uid" class="home4-10-3__mid">
          <div class="home4-10-3__tibox">
            <p class="ti">작성해주신 문의에<br>답변이 도착하였습니다.</p>
          </div>
          <p class="home4103main" v-html="reply.content" />
          <p class="home4101__day a">{{ post.createDate | parseDate('YYYY.MM.DD') }}</p>
        </div>
      </slot>

      <div v-if="post.hasAuthority" class="home4-6-2__tail_2">
        <a href="#" class="modify" @click="handleClickUpdate()">수정</a>
        <a href="#" class="delete" @click="handleClickDelete()">삭제</a>
        <a href="#" class="" @click="handleList()">목록</a>
      </div>

      <div v-else class="home4-6-2__tail">
        <a href="#" class="" @click="handleList()">목록</a>
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
  components: {
  },
})
export default class extends Vue {
  @Prop({ required: true }) private board!: IBoard

  @Prop({ required: true }) private post!: IPost

  private passwordCheckVisible = false;

  private mode = '';

  private password = '';

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
      }).catch(() => {
        // do nothing.
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
