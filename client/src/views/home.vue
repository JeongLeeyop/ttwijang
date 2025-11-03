<template>
  <div>
    <div class="home-top">
      <router-link :to="{ name: 'Home' }" class="top1"
        ><img
          src="~@/assets/images/logo-bg.png"
          class="logot_img"
          alt="뛰장"
      /></router-link>
      <a @click="handlePickupForm()" class="top2"
        ><i class="el-icon-location"></i>
        <slot v-if="pickUpShop">{{ pickUpShop ? `픽업 : ${pickUpShop.name}` : "픽업장소 선택" }}</slot>
        <slot v-else>{{ station ? `배달 : ${station.name}` : "배달장소 선택" }}</slot>
      </a>
      <el-popover
        v-model="showPopover"
        placement="top"
        width="450"
        trigger="click"
        popper-class="alarm"
        place-
        :popper-append-to-body="false"
        :title="alarmList.length > 0 ? '띵동! 알림이 도착했어요 🎶' : ''"
      >
        <div @click="showPopover = false" class="alarm-close">
          <i class="el-icon-close"></i>
        </div>
        <div v-if="alarmList2.length > 0">
          <div
            class="alarm-item-day-wr"
            v-for="(item, index) in alarmList2"
            :key="index"
          >
            <div class="alarm-item-day">
              {{ item[0].createDate | parseDate("YYYY-MM-DD") }}
            </div>
            <div v-if="item.length > 0">
              <a
                :href="item2.link"
                class="alarm-item-wr"
                v-for="item2 in item"
                :key="item2.id"
              >
                <div class="alarm-info">
                  <div class="title">{{ item2.title }}</div>
                  <div class="date">
                    {{ item2.createDate | parseDate("YYYY-MM-DD HH:mm") }}
                  </div>
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
        <el-button
          type="text"
          slot="reference"
          class="diary-header__user alarm"
        ></el-button>
      </el-popover>
      <router-link
        :to="{ name: 'Mypage' }"
        class="home-header__user"
        :class="{ alert: newAlarmCount >= 1 }"
        ><img src="~@/assets/images/user.png" alt="마이페이지"
      /></router-link>
    </div>
    <div class="home__wr">
      <div class="home-main">
        <el-input
          v-model="searchValue"
          @keyup.native.enter="handleSearch"
          placeholder="검색어를 입력하세요"
        >
          <a class="search" slot="suffix" @click="handleSearch"></a>
        </el-input>
        <router-link
          v-for="(item, index) in bestTagList"
          :key="index"
          :to="{
            name: 'BoardIndex',
            params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' },
            query: { searchValue: `#${item.tag}` },
          }"
        >
          #{{ item.tag }}
        </router-link>
      </div>

      <div class="home-main__a">
        <VueSlickCarousel v-bind="set3">
          <div class="carousel__item img1"></div>
          <div class="carousel__item img2"></div>
          <div class="carousel__item img3"></div>
        </VueSlickCarousel>
      </div>
      <div class="home-main__w">
        <!-- <a @click="handleGoDiary()">
          <img src="~@/assets/images/main-ico02.png" />
          <p>다이어리</p>
          <span>체중 감량을 위한 핵심!</span>
        </a> -->
        <a @click="handleOrderTypeForm()">
          <img src="~@/assets/images/main-ico01.png" />
          <p>주문하기</p>
          <span>나에게 꼭! 맞는 맞춤식단</span>
        </a>
        <router-link :to="{ name: 'Mission' }">
          <img src="~@/assets/images/main-ico06.png" />
          <p>맞춤미션</p>
          <span>목표 달성을 위한 맞춤미션!</span>
        </router-link>
      </div>
      <!-- <div class="home-main__w r">
        <router-link :to="{ name: 'Review' }">
          <img src="~@/assets/images/main-ico03.png" />
          <p>식단후기</p>
        </router-link>
        <router-link
          :to="{
            name: 'BoardIndex',
            params: { boardUid: '8ed8c768-93e0-4502-a906-9c62bd44d26d' },
          }"
          class=""
        >
          <img src="~@/assets/images/main-ico04.png" />
          <p>공지사항</p>
        </router-link>
        <router-link
          :to="{
            name: 'BoardIndex',
            params: { boardUid: 'c4f7bf8f-d05a-4135-a512-f1745a9b5d9d' },
          }"
          class=""
        >
          <img src="~@/assets/images/main-ico05.png" />
          <p>서비스 문의하기</p>
        </router-link>
      </div> -->

      <!-- 메인 링크박스 -->
      <div class="main__link-list">
        <div class="main__link-item">
            <div class="main__link-item__con">
              <p class="main__link-item__ttl">다이어리</p>
              <p class="main__link-item__txt sft bd6">
                체중 감량을 위한 <br> 핵심! 다이어리
              </p>
              <p class="main__link-item__btn">
                <a @click="handleGoDiary()" class="btn bg01 sft">작성하기</a>
              </p>
            </div>
            <div class="main__link-item__img">
              <img src="~@/assets/images/main-ico02.png" alt="">
            </div>
          </div>
        <!-- <div class="main__link-item">
            <div class="main__link-item__con">
              <p class="main__link-item__ttl">요즘 핫한 제품들</p>
              <p class="main__link-item__txt sft bd6">
                와로샐러드에 <br>
                입점한 제품을 알아보세요
              </p>
              <p class="main__link-item__btn">
                <router-link :to="{ name: 'CustomProductList' }" class="btn bg01 sft"></router-link>
              </p>
            </div>
            <div class="main__link-item__img">
              <img src="~@/assets/images/main_hot.svg" alt="">
            </div>
          </div> -->
        <div class="main__link-item">
          <div class="main__link-item__con">
            <p class="main__link-item__ttl">TFSE</p>
            <p class="main__link-item__txt sft bd6">
              기록이 쌓이면 <br />
              뭐든 된다! TFSE
            </p>
            <p class="main__link-item__btn">
              <router-link :to="{ name: 'Tfse' }" class="btn bg01 sft">작성하기</router-link>
            </p>
          </div>
          <div class="main__link-item__img">
						<img src="~@/assets/images/main_tfse.svg" alt="">
          </div>
        </div>
        <div class="main__link-item">
          <div class="main__link-item__con">
            <p class="main__link-item__ttl">너도 나도 우리 모두!</p>
            <p class="main__link-item__txt sft bd6">
							오늘부터 우리 <br>
							30일간 챌린지 시작
						</p>
            <p class="main__link-item__btn" style="width:210px">
							<router-link :to="{ name: 'Challenge', params: { myFlag: false } }" style="flex:0 1 calc(40% - 5px)" class="btn sft bd6 bg01">참여하기</router-link>
              <router-link :to="{ name: 'Challenge', params: { myFlag: true } }" style="flex:0 1 calc(51% - 2px);" class="btn bg02 sft bd6">내 챌린지</router-link>
						</p>
          </div>
          <div class="main__link-item__img">
						<img src="~@/assets/images/main_challenge.svg" alt="">
          </div>
        </div>
        <div class="main__link-item">
          <div class="main__link-item__con">
            <p class="main__link-item__ttl">식단후기</p>
            <p class="main__link-item__txt sft bd6">
							고객님들의 생생한 식단후기
						</p>
            <p class="main__link-item__btn" style="width:210px">
							<router-link :to="{ name: 'Review', params: { myFlag: false } }" style="flex:0 1 calc(40% - 5px)" class="btn sft bd6 bg01">식단후기</router-link>
						</p>
          </div>
          <div class="main__link-item__img">
						<img src="~@/assets/images/main-ico03.png" alt="">
          </div>
        </div>
      </div>
      <!-- <div class="home-main__b">
        <div class="home-main__b__txt">
          <p class="tii">영양상담</p>
          <router-link
            :to="{
              name: 'BoardIndex',
              params: { boardUid: 'd485f6c8-ea3b-439e-9308-80a5eaf3ffe0' },
            }"
          ></router-link>
        </div>
        <VueSlickCarousel v-if="consultList.length > 0" v-bind="settings">
          <div
            v-for="post in consultList"
            class="slick-content"
            :key="post.uid"
          >
            <router-link
              :to="{
                name: 'PostDetail',
                params: { boardUid: post.boardUid, postUid: post.uid },
              }"
              class="slide_wrap"
            >
              <div class="slide_box">
                <p
                  v-for="(category, index) in post.categoryList"
                  :key="index"
                  class="ti"
                >
                  {{ category }}
                </p>
                <p class="tti" v-if="post.replyStatus > 0">전문가 답변</p>
                <p class="ttti">{{ post.title }}</p>
                <p class="tttti">
                  {{ post.createDate | parseDate("YYYY-MM-DD") }}
                </p>
              </div>
            </router-link>
          </div>
        </VueSlickCarousel>
      </div> -->

      <div class="home-main__b">
        <div class="home-main__b__txt">
          <p class="tii">TFSE</p>
          <router-link
            :to="{
              name: 'Community',
            }"
          ></router-link>
        </div>

        <VueSlickCarousel v-if="tfseList.length > 0" v-bind="settings">
          <div
            v-for="tfse in tfseList"
            class="slick-content"
            :key="tfse.idx"
          >
            <router-link
              :to="{
                name: 'CommunityDetail',
                params: { idx: tfse.idx },
              }"
              class="slide_wrap"
            >
              <div class="slide_box">
                <p class="tti">일지</p>
                <p class="ttti">{{ tfse.foodText }}</p>
                <p class="emotion-txt">{{ tfse.emotionText }}</p>
                <p class="tttti">
                  {{ tfse.tfseDate | parseDate("YYYY-MM-DD") }}
                </p>
              </div>
            </router-link>
          </div>
        </VueSlickCarousel>
      </div>

      <div class="home-main__c">
        <div class="home-main__b__txt">
          <p class="tii">건강피드</p>
          <router-link
            :to="{
              name: 'BoardIndex',
              params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' },
            }"
          ></router-link>
        </div>

        <VueSlickCarousel
          v-if="feedList.length > 0"
          v-bind="set2"
          class="imgca"
        >
          <div v-for="post in feedList" :key="post.uid">
            <router-link
              :to="{
                name: 'BoardIndex',
                params: { boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76' },
                query: { postUid: post.uid },
              }"
              class="slide_wrap"
            >
              <div class="slide_box2">
                <img
                  v-if="post.fileList.length > 0"
                  :src="`${commonApiUrl}/attached-file/${post.fileList[0]}`"
                />
              </div>
              <div class="main__c__a">
                <p
                  v-for="(category, index) in post.categoryList"
                  :key="index"
                  class="main__c__ti"
                >
                  {{ category }}
                </p>
                <p class="main__c__tii">{{ post.title }}</p>
              </div>
            </router-link>
          </div>
        </VueSlickCarousel>
      </div>
    </div>
    <div class="home-footer">
      <button class="footer_link_a" @click="handleGoDiary()">
        <img
          src="~@/assets/images/footer-diary.png"
          class="footer_img"
          alt=""
        />
        다이어리
      </button>
      <a href="#" class="footer_home">
        <img
          src="~@/assets/images/logo-bg.png"
          class="logo_img"
          alt="뛰장"
        />
      </a>
      <button class="footer_link_b" @click="handleOrderTypeForm()">
        <img
          src="~@/assets/images/footer-plan.png"
          class="footer_img"
          alt="뛰장"
        />식단 주문
      </button>
    </div>
    <!-- <homeFooter /> -->

  <el-dialog title="" :visible.sync="formVisible">
      <div class="diary2-2-1">
        <div class="diary2-2-1__a">
          <div class="diary2-2-3__top">
            <el-button  @click="handleOrderTypeForm()">
              <img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
            </el-button>
          </div>

          <div class="diary2-2-1__mid">
            <p class="ti">어떻게<br>주문할까요?</p>
          </div>

          <div class="diary2-2-1__bot foodSelect">
            <el-button class="top">
              <a @click="handleOrderPick('station')">배달장소 선택</a>
            </el-button>
            <el-button class="bottom">
              <a @click="handleOrderPick('pickup')">픽업매장 방문</a>
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import homeFooter from '@/Layout/components/homeFooter.vue';
import { storageKey } from '@/enums/localStorage';
import axios from 'axios';
import { getPostList } from '@/api/post';
import { getTfseCommunityList } from '@/api/tfse';
import { getAlarmCount, getAlarmList } from '@/api/pushAlarm';
import { getNewAlarmCount } from '@/api/newAlarm';
import { getBestTagList } from '@/api/postTag';
import { RoutingModule } from '@/store/modules/routing';
import 'popper.js';
import { UserModule } from '@/store/modules/user';

@Component({
  components: {
    VueSlickCarousel,
    homeFooter,
  },
})

export default class extends Vue {
  async mounted() {
    RoutingModule.setPoint('Home');
    await this.getMainPostList();
    await this.getTfseCommunityList();
    await this.getAlarmList();
    this.getNewAlarmCount();
    this.getBestTagList();
    // console.log(
      // JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace)),
    // );
    // console.log((window as any).localStorage.getItem('jwttoken'));
  }

  private formVisible = false;

  private newAlarmCount = 0;

  private pickUpShop = JSON.parse(
    (window as any).localStorage.getItem(storageKey.pickUpPlace),
  );

  private station = JSON.parse(
    (window as any).localStorage.getItem(storageKey.stationPlace),
  );

  private commonApiUrl = process.env.VUE_APP_COMMON_API;

  private settings = {
    dots: true,
    centerMode: true,
    centerPadding: '20px',
    focusOnSelect: true,
    infinite: true,
    slidesToShow: 1,
    speed: 500,
  };

  private set2 = {
    dots: true,
    focusOnSelect: true,
    infinite: true,
    speed: 500,
    slidesToShow: 2,
    slidesToScroll: 2,
    touchThreshold: 5,
  };

  private set3 = {
    dots: true,
    infinite: true,
    autoplay: true,
    autoplaySpeed: 5000,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  private consultListQuery = {
    boardUid: 'd485f6c8-ea3b-439e-9308-80a5eaf3ffe0',
    page: 1,
    size: 4,
  };

  private feedListQuery = {
    boardUid: 'bb4db5fe-8083-4d87-96f7-7c1ae66b7e76',
    page: 1,
    size: 8,
  };

  private tfseListQuery = {
    communitySearch: true,
    page: 0,
    size: 8,
  };

  private showPopover = false;

  private consultList: any = [];

  private feedList: any = [];

  private tfseList: any = [];

  private searchValue = '';

  private bestTagList = [];

  private getNewAlarmCount() {
    getNewAlarmCount().then((res) => {
      this.newAlarmCount = res.data;
    });
  }

  private handleSearch() {
    if (this.searchValue) {
      this.$router.push({
        name: 'Search',
        query: { searchValue: this.searchValue },
      });
    } else {
      this.$message.warning('검색어를 입력하세요.');
    }
  }

  private async getMainPostList() {
    await getPostList(this.consultListQuery).then((res) => {
      this.consultList = res.data.content;
    });
    await getPostList(this.feedListQuery).then((res) => {
      this.feedList = res.data.content;
    });
  }

  private async getTfseCommunityList() {
    await getTfseCommunityList(this.tfseListQuery).then((res) => {
      this.tfseList = res.data.content;
    });
  }

  private getBestTagList() {
    getBestTagList().then((res) => {
      this.bestTagList = res.data;
    });
  }

  private alarmCount = 0;

  private alarmList: any[] = [];

  private alarmList2: any[][] = [];

  private totalElements = 0;

  private totalPages = 0;

  private listQuery = {
    page: 0,
    size: 5,
  };

  private handleOrderTypeForm() {
    this.formVisible = !this.formVisible;
  }

  private handlePickupForm() {
    this.handleOrderTypeForm();
    // this.$confirm('현재 선택된 픽업매장을 변경하시겠습니까?', '매장 선택', {
    //       confirmButtonText: '네',
    //       cancelButtonText: '아니요',
    // }).then((result) => {
    //   this.$router.push({ name: 'Map', query: { type: 'pickup' } });
    // });
  }

  private handleOrderPick(type: string) {
      if (type === 'station') {
        if (window.localStorage.getItem(storageKey.stationPlace) == null) {
          this.$router.push({ name: 'Map2', query: { type: 'station' } });
        } else {
          this.$confirm('현재 선택된 배송지로 주문하시겠습니까?', '배송지 선택', {
            confirmButtonText: '네',
            cancelButtonText: '아니요',
          }).then((result) => {
            this.$router.push({ name: 'CustomProduct' });
          }).catch(() => {
            this.$router.push({ name: 'Map2', query: { type: 'station' } });
          });
        }
      } else if (type === 'pickup') {
        if (window.localStorage.getItem(storageKey.pickUpPlace) == null) {
          this.$router.push({ name: 'Map', query: { type: 'pickup' } });
        } else {
          this.$confirm('현재 선택된 픽업매장으로 주문하시겠습니까?', '매장 선택', {
            confirmButtonText: '네',
            cancelButtonText: '아니요',
          }).then((result) => {
            this.$router.push({ name: 'Order' });
          }).catch(() => {
            this.$router.push({ name: 'Map', query: { type: 'pickup' } });
          });
        }
          // if (window.localStorage.getItem(storageKey.pickUpPlace) == null) {
          // this.$router.push({ name: 'Map', query: { type: 'pickup' } });
        // } else {
          // this.$router.push({ name: 'Order' });
        // }
      }
  }

  private handleGoDiary() {
    // if (UserModule.isLogin) {
      this.$router.push({ name: 'Diary' });
    // } else {
      // this.$message.info('로그인이 필요한 서비스 입니다.');
      // this.$router.push({ name: 'Login' });
    // }
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
      const itemMap: Map<string, any[]> = new Map();
      if (this.alarmList !== undefined) {
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
    } else {
      this.alarmList = [];
      this.alarmList2 = [];
    }
  });
    // console.log(itemMap);
  }

  private handleChangePage(page: number) {
    this.listQuery.page = page;
    this.getAlarmList();
  }

  private handleClickMap() {
    // if (UserModule.isLogin) {
      this.$router.push({ name: 'Map' });
    // } else {
      // this.$message.info('로그인이 필요한 서비스 입니다.');
      // this.$router.push({ name: 'Login' });
    // }
  }
}
</script>
