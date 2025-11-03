<template>
  <div>
    <div class="home4-8-1__wr">
      <!--
      <div class="home4-6-3__top b">
        <el-select placeholder="검색 범위" :popper-append-to-body="false" v-model="type" class="group">
          <el-option-group v-for="group in options" class="el-select-label__title" :key="group.label" :label="group.label" >
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-option-group>
        </el-select>
      </div>
      -->
      <div v-loading="loading">
        <template v-if="reviewList.length > 0">
          <div v-for="review in reviewList" :key="review.idx" class="home4-8-1__top" @click="handleDetail(review)">
            <p class="ti">
              <span class="topa">식단관리</span>
              <el-rate :value="review.score" disabled text-color="##FFCE00"></el-rate> <!-- v-model="value" -->
            </p>
            <p class="home481__user">{{ review.userName }} 고객님</p>
            <p class="home481__review">
              <img v-if="review.thumbUid" :src="`/api/attached-file/${review.thumbUid}`" class="review">
              {{ review.content }}
            </p>
            <p class="home481__like" @click.stop>
              <!-- <button class="like" @click="handleClickLike(review)">
                <img v-if="review.likeStatus" src="~@/assets/images/heart-full.png" class="likeimg">
                <img v-else src="~@/assets/images/heart.png" class="likeimg">
                {{ review.likeCount | parseKrw }}
              </button> -->
              <span class="home481__day">{{ review.createDate | parseDate }}</span>
            </p>
          </div>
        </template>
        <div v-else>
          후기가 존재하지 않습니다.
        </div>
      </div>
      <div class="pagination-block">
        <Pagination
          :total="totalElements"
          :page.sync="listQuery.page"
          :limit.sync="listQuery.size"
          @pagination="getReviewList()"
        />
      </div>
      <!--
      <div class="home4-1-2__tail">
        <router-link :to="{ name: 'ReviewList' }" class="">내 후기작성</router-link>
      </div>
      -->
    </div>
  </div>
</template>
<script lang="ts">
import { getMyReviewList } from '@/api/review';
import { likeReview } from '@/api/reviewLike';
import { UserModule } from '@/store/modules/user';
import { Component, Vue } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';

@Component({
  components: {
    Pagination,
  },
})
export default class extends Vue {
  mounted() {
    this.getReviewList();
  }

  private listQuery = {
    page: 1,
    size: 10,
  }

  private totalElements = 0;

  private reviewList = [];

  private loading = true;

  private getReviewList() {
    this.loading = true;
    getMyReviewList(this.listQuery).then((res) => {
      this.reviewList = res.data.content;
      this.totalElements = res.data.totalElements;
      this.loading = false;
    });
  }

  private handleClickLike(review: any) {
    if (UserModule.isLogin) {
      likeReview(review.idx).then(() => {
        review.likeStatus = !review.likeStatus;
        if (review.likeStatus) review.likeCount += 1;
        else review.likeCount -= 1;
      });
    } else {
      this.$message.info('로그인이 필요한 서비스입니다.');
      this.$router.push({ name: 'Login' });
    }
  }

  private handleDetail(review: any) {
    this.$router.push({ name: 'ReviewEditForm', params: { idx: review.idx } });
  }
}
</script>
