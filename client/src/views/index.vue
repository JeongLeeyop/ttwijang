<template>
  <div class="main" v-loading="loading">
    <div id="logo">
      <router-link to="/">
        <img src="~@/assets/images/logo.png" alt="웨일리잇">
      </router-link>
    </div>
    <div class="login-form__title">간편 로그인</div>
    <div class="login-form__snsbox">
      <div class="kakao" @click="kakaoLogin">
        <img src="~@/assets/images/kakao.png" alt="" >
        <p class="kakao-msg">카카오톡으로 계속하기</p>
      </div>
      <div class="naver"  @click="naverLogin">
        <img src="~@/assets/images/naver.png" alt="">
        <p class="naver-msg">네이버로 계속하기</p>
      </div>
      <div class="apple" @click="appleLogin">
        <img src="~@/assets/images/apple.png" alt="">
        <p class="apple-msg">애플로 계속하기</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import homeFooter from '@/Layout/components/homeFooter.vue';
import { UserModule } from '@/store/modules/user';
import { saveFcmToken } from '@/api/fcm-token';
import { getUserInfo } from '@/api/user';
import { getShopList } from '@/api/shop';
import { getStationList } from '@/api/station';
import { storageKey } from '@/enums/localStorage';

@Component({
  components: {
    VueSlickCarousel,
    homeFooter,
  },
})

export default class extends Vue {
  mounted() {
    UserModule.LogOut();
    window.addEventListener('flutterInAppWebViewPlatformReady', (event) => {
      (window as any).flutter_inappwebview.callHandler('fcmToken').then((result: any) => {
          this.fcmToken.token = result.fcmToken;
        });
      // (window as any).flutter_inappwebview.callHandler('AppMessage', '############hello#############');
    });
    this.loginProcess();
  }

  private fcmToken = {
		token: '',
	}

  private loading = false;

  private shopList: any = [];

  private stationList: any = [];

  private async loginSuccess() {
    // 테스트를 위해 토큰 강제 지정
  //   this.fcmToken = {
	// 	token: 'f1p50GeSQiW7KR3wDOyPiE:APA91bGZba9jaOOxC364u3qS5rFcfV623M8nNem4kk_Na6tAS6b6bSK_OhM3x38UTMSPSD3-9yCzcCDm6OOcO1IjQ3dfDy6rEDseeIHXMzVHU7L-6-N2nLgxPI30VvJ-5E4xRnnI3aVY',
  // };
    getUserInfo().then(async (res) => {
      if (res.data.registerInfoStatus) {
        if (this.fcmToken.token !== '') await saveFcmToken(this.fcmToken);
        await getShopList().then(async (res2) => {
          this.shopList = res2.data;
          this.shopList.forEach((shop: any) => {
            if (shop.idx === res.data.shopIdx) {
              window.localStorage.setItem(storageKey.pickUpPlace, JSON.stringify(shop));
            }
          });
        });
        if (this.shopList === '') {
          await getStationList().then(async (res3) => {
            this.stationList = res3.data;
            this.stationList.forEach((station: any) => {
              if (station.idx === res.data.stationIdx) {
                window.localStorage.setItem(storageKey.stationPlace, JSON.stringify(station));
              }
            });
          });
        }
        this.$router.push({ name: 'Home' });
      } else {
        window.localStorage.setItem('isRegister', 'false');
        this.$router.push({ name: 'Agree' });
      }
    });
  }

  private kakaoLogin() {
    const clientId = process.env.VUE_APP_KAKAO_CLIENT_ID;
    const redirectUri = process.env.VUE_APP_KAKAO_REDIRECT_URL;
    (window as any).Kakao.init(clientId);
    (window as any).Kakao.Auth.authorize({
      redirectUri,
    });
  }

  private naverLogin() {
    window.location.href = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${process.env.VUE_APP_NAVER_CLIENT_ID}&redirect_uri=${process.env.VUE_APP_NAVER_REDIRECT_URL}`;
  }

  private appleLogin() {
    // localhost로도 콜백이 안된다.
    window.location.href = 'https://appleid.apple.com/auth/authorize?client_id=unids.weilyeat&redirect_uri=https://weilyeat.co.kr/login&response_type=code id_token';
  }

  /* eslint-disable */
  private async loginProcess() {
    const { code, state, id_token } = (this.$route.query as any);
    if (code && state) {
      this.loading = true;
      await UserModule.NaverLogin(code);
      const routeName2: any = UserModule.NaverMe();
      const routeName3: any = await UserModule.NaverAccess();
      this.loading = false;
      this.loginSuccess();
    } else if (code && id_token) {
      this.loading = true;
      await UserModule.AppleLogin({code: code, idToken: id_token });
      this.loading = false;
      this.loginSuccess();
    } else if (code) {
      this.loading = true;
      const routeName: any = await UserModule.KakaoLogin(code); 
      this.loading = false;
      this.loginSuccess();
      // this.$router.push({ name: routeName });
    }
  }
  /* eslint-enable */
}
</script>
