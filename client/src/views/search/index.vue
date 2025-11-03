<template>
  <div>
    <div class="home-header">
      <button @click="handleClickBefore()" class="home-header__arrow"><img src="~@/assets/images/arrow-ico.png" alt="뒤로가기"></button>
      {{ $route.meta.title || 'ttwijang' }}
      <el-popover v-model="showPopover" placement="top" width="450" trigger="click" popper-class="alarm" place- :popper-append-to-body="false"
        :title="alarmList.length > 0 ? '띵동! 알림이 도착했어요 🎶' : ''">
        <div @click="showPopover = false" class="alarm-close">
            <i class="el-icon-close"></i>
          </div>
        <div v-if="alarmList2.length > 0">
          <div class="alarm-item-day-wr" v-for="(item, index) in alarmList2" :key="index">
            <div class="alarm-item-day">{{ item[0].createDate | parseDate('YYYY-MM-DD') }}</div>
            <div v-if="item.length > 0">
              <a :href="item2.link" class="alarm-item-wr" v-for="item2 in item" :key="item2.id">
                <div class="alarm-info">
                  <div class="title">{{ item2.title }}</div>
                  <div class="date">{{ item2.createDate | parseDate('YYYY-MM-DD HH:mm') }}</div>
                </div>
                <div class="alarm-content">{{ item2.content }}</div>
              </a>
            </div>
          </div>
        </div>
        <div class="empty" v-else>
          <div class="logo"><img src="@/assets/images/logo2.png" /></div>
          <div class="txt">도착한 알림이 없어요</div>
        </div>
        <el-button type="text" slot="reference" class="diary-header__user alarm"></el-button>
      </el-popover>
      <router-link :to="{ name: 'Mypage' }" class="home-header__user" :class="{'alert': newAlarmCount >= 1}"><img src="~@/assets/images/user.png"
          alt="마이페이지"></router-link>
    </div>

      <div class="home__wr">
      <div class="home-main">
        <el-input v-model="searchValue" @keyup.native.enter="handleSearch()" placeholder="검색어를 입력하세요">
          <a class="search" slot="suffix" @click="handleSearch()"></a>
        </el-input>
        <router-link
          v-for="(item, index) in bestTagList"
          :key="index"
          :to="{ name: 'BoardIndex', params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' }, query: { searchValue: `#${item.tag}` } }"
        >
          #{{ item.tag }}
        </router-link>
      </div>

      <div class="home-search__a">
        <p class="ti" @click="handleClickConsult()">영양상담</p>
        <div class="home4-6-1__mid" v-loading="consultLoading">
          <slot v-if="consultList && consultList.length > 0">
            <p class="b2" v-for="consult in consultList" :key="consult.uid">
              <router-link :to="{ name: 'PostDetail', params: { boardUid: consult.boardUid, postUid: consult.uid } }">
              <span class="tti b">{{ consult.title }}</span>
              <span class="tti">{{ consult.createDate | parseDate('YYYY-MM-DD') }}</span>
              </router-link>
            </p>
          </slot>
          <div class="b" v-else style="text-align: center;">
            <span style="color: #787878">게시글이 존재하지 않습니다.</span>
          </div>
        </div>
        <div class="pagination-block">
          <Pagination
            :total="consultTotalElements"
            :page.sync="consultQuery.page"
            :limit.sync="consultQuery.size"
            @pagination="getConsultList"
          />
        </div>
      </div>

      <div class="home-search__b">
        <p class="ti" @click="handleClickFeed()">건강피드</p>
        <div class="home4-6-1__mid" v-loading="feedLoading">
          <slot v-if="feedList && feedList.length > 0">
            <p class="b" v-for="feed in feedList" :key="feed.uid">
              <router-link
              :to="{ name: 'BoardIndex', params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' }, query: { postUid: feed.uid } }" class="slide_wrap">
              <span class="tti b">{{ feed.title }}</span>
              <span class="tti">{{ feed.createDate | parseDate('YYYY-MM-DD') }}</span>
              </router-link>
            </p>
          </slot>
          <div class="b" v-else style="text-align: center;">
            <span style="color: #787878">게시글이 존재하지 않습니다.</span>
          </div>
        </div>
        <div class="pagination-block">
          <Pagination
            :total="feedTotalElements"
            :page.sync="feedQuery.page"
            :limit.sync="feedQuery.size"
            @pagination="getFeedList"
          />
        </div>
      </div>
    </div>
    <homeFooter />
  </div>
</template>

<script lang="ts">
import { getPostList } from '@/api/post';
import { Component, Vue } from 'vue-property-decorator';
import Pagination from '@/components/Pagination/index.vue';
import { getBestTagList } from '@/api/postTag';
import { getAlarmCount, getAlarmList } from '@/api/pushAlarm';
import { getNewAlarmCount } from '@/api/newAlarm';
import homeFooter from '@/Layout/components/homeFooter.vue';

@Component({
  components: {
    Pagination,
    homeFooter,
  },
})
export default class extends Vue {
  mounted() {
    if (this.$route.query.searchValue) {
      this.searchValue = this.$route.query.searchValue;
      this.handleSearch();
      this.getBestTagList();
      this.getAlarmList();
      this.getNewAlarmCount();
    } else {
      this.$router.push({ name: 'Home' });
    }
  }

  private newAlarmCount = 0;

  private showPopover = false;

  private alarmCount = 0;

  private alarmList = [];

  private alarmList2: any[][] = [];

  private totalElements = 0;

  private totalPages = 0;

  private listQuery = {
    page: 0,
    size: 5,
  };

  private searchValue: any = '';

  private consultList: any = [];

  private feedList: any = [];

  private consultLoading = false;

  private feedLoading = false;

  private consultTotalElements = 0;

  private feedTotalElements = 0;

  private bestTagList = [];

  private consultQuery = {
    boardUid: 'd485f6c8-ea3b-439e-9308-80a5eaf3ffe0',
    page: 1,
    size: 5,
    searchType: 'titleOrContentOrTag',
    searchValue: '',
  }

  private feedQuery = {
    boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76',
    page: 1,
    size: 5,
    searchType: 'titleOrContentOrTag',
    searchValue: '',
  }

  private getNewAlarmCount() {
    getNewAlarmCount().then((res) => {
      this.newAlarmCount = res.data;
    });
  }

  private getConsultList() {
    this.consultLoading = true;
    getPostList(this.consultQuery).then((res) => {
      console.log(res);
      this.consultList = res.data.content;
      this.consultTotalElements = res.data.totalElements;
      this.consultLoading = false;
    });
  }

  private getFeedList() {
    this.feedLoading = true;
    getPostList(this.feedQuery).then((res) => {
      console.log(res);
      this.feedList = res.data.content;
      this.feedTotalElements = res.data.totalElements;
      this.feedLoading = false;
    });
  }

  private handleSearch() {
    if (this.searchValue) {
      this.$router.replace({ ...this.$router, query: { searchValue: this.searchValue } });
      this.consultQuery.page = 1;
      this.consultQuery.searchValue = this.searchValue;
      this.getConsultList();
      this.feedQuery.page = 1;
      this.feedQuery.searchValue = this.searchValue;
      this.getFeedList();
    } else {
      this.$message.warning('검색어를 입력해주세요.');
    }
  }

  private handleClickConsult() {
    this.$router.push({ name: 'BoardIndex', params: { boardUid: 'd485f6c8-ea3b-439e-9308-80a5eaf3ffe0' } });
  }

  private handleClickFeed() {
    this.$router.push({ name: 'BoardIndex', params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' } });
  }

  private getBestTagList() {
    getBestTagList().then((res) => {
      this.bestTagList = res.data;
    });
  }

  private getAlarmCount() {
    getAlarmCount().then((res) => {
      this.alarmCount = res.data;
    });
  }

  private async getAlarmList() {
    await getAlarmList(this.listQuery).then((res) => {
      this.alarmList = res.data.content;
      this.totalElements = res.data.totalElements;
      this.totalPages = res.data.totalPages;
    });
    const itemMap: Map<string, any[]> = new Map();
    this.alarmList.forEach((item: any) => {
      const date: string = item.createDate.substring(0, 10); // 날짜 부분만 추출
      if (!itemMap.has(date)) {
        itemMap.set(date, []);
      }
      const itemList: any[] | undefined = itemMap.get(date);
      if (itemList) {
        itemList.push(item);
      }
    });
    this.alarmList2 = Array.from(itemMap.values());
  }

  private handleChangePage(page: number) {
    this.listQuery.page = page;
    this.getAlarmList();
  }

  private handleClickBefore() {
    this.$router.push({ name: 'Home' });
  }
}
</script>
