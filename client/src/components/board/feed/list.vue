<template>
  <div>
    <div class="home4-6-4__wr">
      <div class="home4-6-3__top a" v-if="board.categoryList.length > 0">
        <el-radio-group class="myradiogroup" v-model="search.categoryList" @change="handleChangeCategory">
          <el-radio-button :label="null" :class="listQuery.categoryList.length === 0 ? 'selected' : ''">전체</el-radio-button>
          <el-radio-button
            v-for="(category, index) in board.categoryList[0].category.children"
            :key="index"
            :label="category.uid"
            :class="listQuery.categoryList.indexOf(category.uid) > -1 ? 'selected' : ''"
          >
            {{ category.name }}
          </el-radio-button>
        </el-radio-group>
      </div>

      <slot v-if="postList.length > 0">
        <div class="home4-6-4__mid" v-for="post in postList" :key="post.uid" style="margin-bottom: 60px;">
          <div class="p4-6-4-img">
            <VueSlickCarousel :arrows="false" :dots="true">
              <div @dblclick="handleLikePost(post)" v-for="(fileUid, index) in post.fileList" :key="index">
                <img :src="`${commonApiUrl}/attached-file/${fileUid}`" class="img464-1" alt="">
              </div>
            </VueSlickCarousel>
            <div class="like-button">
              <img v-if="post.likeStatus" src="@/assets/images/heart-full.png" alt="좋아요" @click="handleLikePost(post)">
              <img v-else src="@/assets/images/heart.png" alt="좋아요" @click="handleLikePost(post)">
              <span>{{ post.likeCount | parseKrw }}</span>
            </div>
          </div>
          <div class="p4-6-4 a">
            <p v-html="post.content" class="content"/>
          </div>
          <div class="post-tag">
            <span v-for="(tag, index) in post.tags" :key="index" class="post-tag__item">#{{ tag }}</span>
          </div>
        </div>
      </slot>
      <div v-else>
        게시글이 존재하지 않습니다.
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { UserModule } from '@/store/modules/user';
import { IPost, IBoard } from '@/types';
import { Vue, Component, Prop } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import { likePost } from '@/api/postLike';
import { parseKrw } from '@/filters';
import { getBestTagList } from '@/api/postTag';

@Component({
  name: '',
  components: {
    VueSlickCarousel,
  },
})
export default class extends Vue {
  @Prop({ required: true }) private postList!: IPost[]

  @Prop({ required: true }) private listQuery!: any

  @Prop({ default: 0 }) private totalElements!: number

  @Prop({ required: true }) private board!: IBoard

  mounted() {
    this.init();
    this.getBestTagList();
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

  private bestTagList = [];

  private searchFieldList: any[] = [];

  private commonApiUrl = process.env.VUE_APP_COMMON_API;

  private search: any = {
    searchType: 'titleOrContentOrTag',
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
    this.$router.push({ name: 'PostDetail', params: { boardUid: this.$route.params.boardUid, postUid: post.uid }, query: { ...this.listQuery } });
  }

  private handleClickNewPost() {
    this.$router.push({ name: 'PostAdd' });
  }

  private handleChangeCategory(categoryUid: any) {
    if (categoryUid) this.search.categoryList = [categoryUid];
    else this.search.categoryList = [];
    this.handleSearch();
  }

  private handleLikePost(post: any) {
    if (UserModule.isLogin) {
      likePost(post.uid).then(() => {
        post.likeStatus = !post.likeStatus;
        if (post.likeStatus) post.likeCount += 1;
        else post.likeCount -= 1;
      });
    } else {
      this.$router.push({ name: 'Login' });
    }
  }

  private handleClickTag(tag: any) {
    this.search.searchValue = `#${tag}`;
    this.handleSearch();
  }

  private getBestTagList() {
    getBestTagList().then((res) => {
      this.bestTagList = res.data;
    });
  }
}
</script>

<style lang="scss">
.font--border { font-weight: 700; color: black; font-size: 18px; }
.post-tag {
  margin-top: 10px;
  margin-left: 5px;
  text-align: left;
  &__item {
    color: #437dff;
    margin-right: 5px;
    font-weight: bold;
  }
}
</style>
