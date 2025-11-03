<template>
  <div>
    <div class="home4-10-1__wr" v-if="postList">
      <slot v-if="postList.length > 0">
        <div v-for="post in postList" class="home4-9-1__box" :key="post.uid" @click="handleClickPost(post)">
          <p v-if="post.replyStatus" class="home4101__ti">답변이 완료되었습니다.</p>
          <p v-else class="home4101__ti">답변을 기다리고 있습니다.</p>
          <p class="home4101__text">{{ post.title }}</p>
          <p class="home4101__day">{{ post.createDate | parseDate('YYYY-MM-DD') }}</p>
        </div>
      </slot>
      <div v-else>게시글이 존재하지 않습니다.</div>
    </div>
  </div>
</template>

<script lang="ts">
import { UserModule } from '@/store/modules/user';
import { IPost, IBoard } from '@/types';
import { Pagination } from 'element-ui';
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component({
  name: 'BoardPhotoList',
})
export default class extends Vue {
  @Prop({ required: true }) private postList!: any[]

  @Prop({ required: true }) private listQuery!: any

  @Prop({ default: 0 }) private totalElements!: number

  @Prop({ required: true }) private board!: IBoard

  mounted() {
    this.init();
  }

  private init() {
    if (this.$route.query) {
      this.search = {
        ...this.search,
        ...this.$route.query,
      };
    }
    if (this.board.fieldList) {
      this.searchFieldList = this.board.fieldList.filter((field: any) => {
        const searchField = field.searchState;
        return searchField;
      });
    }
  }

  private searchFieldList: any[] = [];

  private search = {
    searchType: 'title',
    searchValue: '',
  };

  private getPostNumber(index: number) {
    let totalSize = (this.listQuery.page - 1) * this.listQuery.size;
    if (totalSize < 0) totalSize = 0;
    return this.totalElements - totalSize - index;
  }

  private getWriteAuth() {
    const userRoles = UserModule.roles;
    if (this.board.authWrite === 'GUEST') return true;
    if (userRoles.indexOf('ROLE_ADMIN') > -1) return true;
    if (this.board.authWrite === 'MEMBER' && UserModule.isLogin) return true;
    return false;
  }

  private filterSearchType(type: string) {
    let rt = false;
    this.board.fieldList.forEach((flist: any) => {
      if (flist.fieldType.typeCode === type) rt = true;
    });
    return rt;
  }

  private handleSearch() {
    this.$emit('search', this.search);
  }

  private handleClickPost(post: any) {
    this.$router.push({
      name: 'PostDetail',
      params: {
        boardUid: this.$route.params.boardUid,
        postUid: post.uid,
      },
      query: {
        ...this.listQuery,
      },
    }).catch(() => {
      // do nothing.
    });
  }

  private handleClickNewPost() {
    this.$router.push({ name: 'PostAdd' }).catch(() => {
      // do nothing.
    });
  }
}
</script>

<style lang="scss">
.font--border { font-weight: 700; color: black; font-size: 18px; }
</style>
