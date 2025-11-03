<template>
  <div>
    <div v-for="(useCategory, index) in board.categoryList" class="home4-6-1__top" :key="index">
      <button @click="handleClickCategory(null)" :class="listQuery.categoryList.length === 0 ? 'selected' : ''">전체</button>
      <button
        v-for="category in useCategory.category.children"
        :key="category.uid"
        :class="listQuery.categoryList.indexOf(category.uid) > -1 ? 'selected' : ''"
        @click="handleClickCategory(category.uid)"
      >
        {{ category.name }}
      </button>
    </div>

    <div class="home4-6-1__wr">
      <p class="ti">인기글</p>

      <div class="home4-6-1__mid">
        <slot v-if="hotPostList.length > 0">
          <p class="b" v-for="(post, index) in hotPostList" :key="index" @click="handleClickPost(post)">
            <span class="tti b">{{ post.title }}</span>
            <span class="tti">{{ post.createDate | parseDate }}</span>
          </p>
        </slot>
        <p v-else>
          <span class="tti b">게시글이 존재하지 않습니다.</span>
        </p>
      </div>
      <p class="ti">게시글</p>

      <slot v-if="postList.length > 0">
        <div v-for="(post, index) in postList" :key="index" class="home4-6-1__bot a">
          <p class="boxa">
            <span v-for="category in post.categoryList" class="boti" :key="category">{{ category }}</span>
            <span v-if="post.replyStatus" class="boti a">전문가 답변</span>
          </p>
          <p class="boxb">
            <a href="#" @click="handleClickPost(post)">
              <span class="botiii">{{ post.title }}</span>
            </a>
          </p>
          <p class="boxc">
            <span class="botiv">
              <img v-if="post.likeStatus" src="@/assets/images/heart-full.png" alt="좋아요" width="25px" height="22px">
              <img v-else src="@/assets/images/heart.png" alt="좋아요" width="25px" height="22px">
              {{ post.likeCount | parseKrw }}
            </span>
            <span class="botv">{{ post.createDate | parseDate }}</span>
          </p>
        </div>
      </slot>
      <div v-else class="home4-6-1__bot a">
        게시글이 존재하지 않습니다.
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getPostList } from '@/api/post';
import { parseKrw } from '@/filters';
import { UserModule } from '@/store/modules/user';
import { IPost, IBoard } from '@/types';
import { Pagination } from 'element-ui';
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component({
  name: 'BoardPhotoList',
})
export default class extends Vue {
  @Prop({ required: true }) private postList!: IPost[]

  @Prop({ required: true }) private listQuery!: any

  @Prop({ default: 0 }) private totalElements!: number

  @Prop({ required: true }) private board!: IBoard

  mounted() {
    this.init();
    this.getHotPostList({});
  }

  private hotPostList: any = [];

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

  private search: any = {
    searchType: 'title',
    searchValue: '',
    categoryList: [],
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

  private handleClickCategory(categoryUid: any) {
    if (categoryUid) {
      this.search.categoryList = [categoryUid];
    } else {
      this.search.categoryList = [];
    }
    this.$emit('search', this.search);
    this.getHotPostList(this.search);
  }

  private getHotPostList(search: any) {
    const listQuery: any = {
      ...this.listQuery,
      page: 1,
      size: 3,
      sort: 'hot',
    };
    if (search && search.categoryList && search.categoryList.length > 0) {
      listQuery.categoryList = search.categoryList[0];
    } else {
      listQuery.categoryList = '';
    }
    getPostList(listQuery).then((res) => {
      this.hotPostList = res.data.content;
    });
  }
}
</script>

<style lang="scss">
.font--border { font-weight: 700; color: black; font-size: 18px; }
</style>
