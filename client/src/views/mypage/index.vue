<template>
  <div>
    <div class="home4__wr">
      <div class="home4__top">
        <p class="ti"> {{ userInfo.actualName }} </p>
        <router-link :to="{ name: 'Point' }">
          <p class="tii"><img src="~@/assets/images/point.png" class="point" alt="포인트">
            {{ userInfo.point | parseKrw }} 포인트</p>
        </router-link>
      </div>

      <div class="home4__mid">
        <ul>
          <li><router-link :to="{ name: 'Point' }"><img src="~@/assets/images/point2.png" class="address" alt="배송지 관리">
            <p class="ti">포인트 관리</p>
          </router-link></li>
          <li class="line"><router-link :to="{ name: 'Coupon' }"><img src="~@/assets/images/coupon.png" class="coupon" alt="나의쿠폰">
              <p class="ti">나의쿠폰</p>
            </router-link></li>
          <li class="sub" ><router-link :to="{ name: 'UserInfo' }"><img src="~@/assets/images/info-re.png" class="info-re" alt="정보수정">
              <p class="ti">정보수정</p>
            </router-link></li>
            <li class="line sub"><router-link :to="{ name: 'Delivery' }"><img src="~@/assets/images/car.png" class="car" alt="배송조회">
            <p class="ti">주문조회</p>
            </router-link></li>
          <!-- <li class="linr"><router-link :to="{ name: 'Card' }"><img src="~@/assets/images/card-re.png" class="card-re"
                alt="결제카드 관리">
              <p class="ti">결제카드 관리</p>
            </router-link></li> -->
        </ul>
      </div>

      <div class="home4__bot">
        <router-link :to="{ name: 'BoardIndex', params: { boardUid: '8ed8c768-93e0-4502-a906-9c62bd44d26d', previousUrl: 'Mypage' } }" class="">
          <span v-if="newAlarmCount >= 1" class="alert"><i class="el-icon-message-solid"></i></span>
        공지사항</router-link>
        <router-link :to="{ name: 'MyReview' }">내 식단후기</router-link>
        <router-link :to="{ name: 'BoardIndex', params: { boardUid: 'c4f7bf8f-d05a-4135-a512-f1745a9b5d9d', previousUrl: 'Mypage' }, }" class="">서비스 문의</router-link>
        <!-- <router-link :to="{ name: '' }" class="">앱설정</router-link> -->
        <router-link :to="{ name: 'Term' }" class="">이용약관</router-link>
        <a href="#" @click="handleWithdraw()" class="">회원탈퇴</a>
        <router-link :to="{ name: '' }" @click.native="logout()" class="">로그아웃</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import { UserModule } from '@/store/modules/user';
import { getJwtInfo } from '@/utils/cookies';
import { getUserInfo } from '@/api/user';
import { getRemainCount } from '@/api/order';
import { storageKey } from '@/enums/localStorage';
import { RoutingModule } from '@/store/modules/routing';
import { getNewAlarmCount } from '@/api/newAlarm';

@Component({
  components: {
  },
})

export default class extends Vue {
  mounted() {
    this.getNewAlarmCount();
    RoutingModule.setPoint('Mypage');
    this.getUserInfo();
  }

  private userInfo = {
    point: 0,
  };

  private newAlarmCount = 0;

  private getNewAlarmCount() {
    getNewAlarmCount().then((res) => {
      this.newAlarmCount = res.data;
    });
  }

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.userInfo = res.data;
    });
  }

  private async logout() {
    await UserModule.LogOut();
    window.localStorage.removeItem(storageKey.pickUpPlace);
    this.$router.push({ name: 'Home' });
  }

  private handleWithdraw() {
    getRemainCount().then((res) => {
      if (res.data > 0) {
        this.$message.warning('픽업을 완료하지 않은 주문건이 있어 회원탈퇴가 불가합니다.');
      } else {
        window.localStorage.removeItem(storageKey.pickUpPlace);
        this.$router.push({ name: 'Withdrawal' });
      }
    });
  }
}
</script>
