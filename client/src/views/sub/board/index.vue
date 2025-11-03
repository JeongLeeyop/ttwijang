<template>
  <div class="board-wrap">
    <div id="container_wr">
      <!-- 게시판 검색 -->
      <div v-if="board.headerHtml" v-html="board.headerHtml" />
      <!-- 일반 게시판 -->
      <BoardDefaultList v-if="board.skin === 'default'" v-loading="loading" :postList="postList"
        :listQuery="postListQuery" :totalElements="totalElements" :board="board" @search="handleSearch" />
      <BoardConsultList v-if="board.skin === 'consult'" v-loading="loading" :postList="postList"
        :listQuery="postListQuery" :totalElements="totalElements" :board="board" @search="handleSearch" />
      <BoardFeedList v-if="board.skin === 'feed'" v-loading="loading" :postList="postList" :listQuery="postListQuery"
        :totalElements="totalElements" :board="board" @search="handleSearchFeed" />
      <BoardQnaList v-if="board.skin === 'qna'" v-loading="loading" :postList="postList" :listQuery="postListQuery"
        :totalElements="totalElements" :board="board" @search="handleSearch" />
      <BoardChallengeList v-if="board.skin === 'challenge'" v-loading="loading" :postList="postList" :listQuery="postListQuery"
        :totalElements="totalElements" :board="board" @search="handleSearch" />
      <div class="pagination-block">
        <Pagination v-if="board.skin !== 'feed'" :total="totalElements" :page.sync="postListQuery.page"
          :limit.sync="postListQuery.size" @pagination="handleChangePaging" />
      </div>
      <div class="home4-6-1__tail">
        <a v-if="getWriteAuth()" href="#" @click.prevent="handleClickNewPost()">게시글 작성하기</a>
      </div>
      <div v-if="board.footerHtml" v-html="board.footerHtml" />
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
import { getPostList } from '@/api/post';
import { IBoard, IPost } from '@/types';
import { getBoard } from '@/api/board';
import BoardDefaultList from '@/components/board/default/list.vue';
import BoardConsultList from '@/components/board/consult/list.vue';
import BoardFeedList from '@/components/board/feed/list.vue';
import BoardQnaList from '@/components/board/qna/list.vue';
import BoardChallengeList from '@/components/board/challenge/list.vue';
import Pagination from '@/components/Pagination/index.vue';
import { UserModule } from '@/store/modules/user';
import { RoutingModule } from '@/store/modules/routing';

@Component({
  name: 'BoardIndex',
  components: {
    BoardDefaultList,
    BoardConsultList,
    BoardFeedList,
    BoardQnaList,
    BoardChallengeList,
    Pagination,
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
    if (this.board.skin === 'feed') this.addScrollListener();
  }

  private postList: any[] = [];

  private totalElements = 0;

  private totalPages = 0;

  private postListQuery = {
    page: 1,
    size: 10,
    searchType: 'titleOrContentOrTag',
    searchValue: '',
    boardUid: this.$route.params.boardUid,
    categoryList: [],
    postUid: '',
  };

  private loading = true;

  private init() {
    this.postListQuery = {
      page: 1,
      size: this.board.listSize,
      searchType: 'titleOrContentOrTag',
      searchValue: '',
      boardUid: this.$route.params.boardUid,
      categoryList: [],
      postUid: '',
    };
    if (this.$route.query && this.$route.query.page !== undefined) {
      this.postListQuery = {
        ...this.postListQuery,
        ...this.$route.query,
        page: Number(this.$route.query.page),
        size: this.board.listSize,
        boardUid: this.$route.params.boardUid,
      };
    }
    this.getPostList();
  }

  private getPostList() {
    if (this.board.skin === 'feed') {
      getPostList(this.postListQuery).then((response: any) => {
        this.totalElements = response.data.totalElements;
        this.postList = this.postList.concat(response.data.content);
        this.totalPages = response.data.totalPages;
        this.loading = false;
      });
    } else {
      this.loading = true;
      this.postList = [];
      getPostList(this.postListQuery).then((response: any) => {
        this.totalElements = response.data.totalElements;
        this.postList = response.data.content;
        this.totalPages = response.data.totalPages;
        this.loading = false;
      });
    }
  }

  private searchFeedPostList() {
    getPostList(this.postListQuery).then((response: any) => {
      this.totalElements = response.data.totalElements;
      this.postList = response.data.content;
      this.totalPages = response.data.totalPages;
      this.loading = false;
    });
  }

  private addScrollListener() {
    window.addEventListener('scroll', this.handleScroll);
  }

  private removeScrollListener() {
    window.removeEventListener('scroll', this.handleScroll);
  }

  private beforeDestroy() {
    this.removeScrollListener();
  }

  private handleScroll() {
    const scrollHeight = document.documentElement.scrollHeight;
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    const clientHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    if (scrollHeight - scrollTop <= clientHeight) {
      this.postListQuery.page += 1;
      this.getPostList();
    }
  }

  private handleSearch(search: any) {
    this.$router.replace({ ...this.$router, query: {} }).catch(() => {
      // do nothing.
    });
    this.postListQuery = {
      ...this.postListQuery,
      ...search,
      postUid: '',
      page: 1,
    };
    if (search.categoryList && search.categoryList.length > 0) this.postListQuery.categoryList = search.categoryList.join(',');
    this.getPostList();
  }

  private handleSearchFeed(search: any) {
    this.postListQuery = {
      ...this.postListQuery,
      ...search,
      postUid: '',
      page: 1,
    };
    if (search.categoryList && search.categoryList.length > 0) this.postListQuery.categoryList = search.categoryList.join(',');
    this.searchFeedPostList();
  }

  private handleChangePaging() {
    this.getPostList();
  }

  private handleChangeProgress(index: number) {
    this.handleSearch({ progress: index });
  }

  private handleChangeSort(orderBy: string) {
    this.handleSearch({ sort: orderBy });
  }

  private getWriteAuth() {
    const userRoles = UserModule.roles;
    if (this.board.authWrite === 'GUEST') return true;
    if (userRoles.indexOf('ROLE_ADMIN') > -1) return true;
    if (this.board.authWrite === 'MEMBER' && UserModule.isLogin) return true;
    return false;
  }

  private handleClickNewPost() {
    this.$router.push({ name: 'PostAdd', params: { boardUid: this.$route.params.boardUid } }).catch(() => {
      // do nothing.
    });
  }
}
</script>
