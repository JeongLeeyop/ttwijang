<template>
  <div class="main" v-loading="loading">
    <div class="background-wave"></div>
    <div class="content-container">
      <div class="title-section">
        <router-link to="/">
          <h1 class="main-title">뛰장</h1>
        </router-link>
        <p class="sub-title">풋살도 이제는 리그로 재미있게 뛰장!</p>
      </div>

      <div class="login-form__snsbox">
        <div class="kakao" @click="kakaoLogin">
          <img src="~@/assets/images/kakao.png" alt="" >
          <p class="kakao-msg">카카오톡 로그인</p>
        </div>
        <div class="naver" @click="naverLogin">
          <img src="~@/assets/images/naver.png" alt="">
          <p class="naver-msg">네이버 로그인</p>
        </div>
        <div class="apple" @click="appleLogin">
          <p class="apple-msg">이메일 로그인</p>
        </div>
      </div>

      <div class="welcome-text">Welcome!</div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';
import { UserModule } from '@/store/modules/user';
import { getUserInfo } from '@/api/user';
import { storageKey } from '@/enums/localStorage';

@Component({
  components: {
    VueSlickCarousel,
  },
})

export default class extends Vue {
  mounted() {
    UserModule.LogOut();
    window.addEventListener('flutterInAppWebViewPlatformReady', (event) => {
      (window as any).flutter_inappwebview.callHandler('fcmToken').then((result: any) => {
          this.fcmToken.token = result.fcmToken;
        });
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
    // 이메일 로그인 페이지로 이동
    this.$router.push({ name: 'EmailLogin' });
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

<style scoped lang="scss">
@font-face {
    font-family: 'PartialSans';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2307-1@1.1/PartialSansKR-Regular.woff2') format('woff2');
    font-weight: normal;
    font-display: swap;
}

.main {
  position: relative;
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #061da1 0%, #374ab3 100%);
  overflow: hidden;

  .background-wave {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 200%;
      height: 100%;
      background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 100" preserveAspectRatio="none"><defs><linearGradient id="waveGradient" x1="0%" y1="0%" x2="100%" y2="0%"><stop offset="0%" style="stop-color:rgba(0,10,80,0.6);stop-opacity:1" /><stop offset="50%" style="stop-color:rgba(10,30,120,0.8);stop-opacity:1" /><stop offset="100%" style="stop-color:rgba(0,10,80,0.6);stop-opacity:1" /></linearGradient></defs><g><path d="M 140,-50 L 120,-40 L 105,-25 L 90,-5 Q 70,20 50,35 T 20,60 Q 10,75 0,90 L -10,105 L -20,125 L -20,150 L 0,150 L 15,135 L 30,115 Q 45,95 60,80 T 85,55 Q 95,40 110,20 L 125,0 L 135,-20 L 140,-35 Z" fill="url(%23waveGradient)" opacity="0.75"/><path d="M 145,-60 L 125,-48 L 108,-30 L 92,-8 Q 70,22 48,40 T 15,68 Q 5,82 -5,98 L -15,115 L -25,135 L -25,160 L -5,160 L 10,143 L 25,122 Q 42,100 60,83 T 88,56 Q 100,38 118,15 L 132,-5 L 142,-25 L 145,-42 Z" fill="url(%23waveGradient)" opacity="0.7"/><path d="M 150,-70 L 128,-55 L 110,-35 L 92,-10 Q 68,25 43,45 T 8,78 Q -3,93 -12,108 L -22,128 L -30,148 L -30,170 L -8,170 L 8,150 L 25,128 Q 43,105 63,86 T 92,58 Q 105,38 125,10 L 140,-12 L 148,-32 L 150,-50 Z" fill="url(%23waveGradient)" opacity="0.65"/><path d="M 155,-80 L 130,-62 L 112,-40 L 92,-12 Q 65,28 38,50 T 0,88 Q -10,103 -20,120 L -30,142 L -35,160 L -35,180 L -12,180 L 5,158 L 23,133 Q 43,108 65,88 T 95,58 Q 110,35 130,5 L 145,-18 L 152,-40 L 155,-58 Z" fill="url(%23waveGradient)" opacity="0.6"/><path d="M 160,-90 L 132,-68 L 113,-43 L 90,-13 Q 62,32 32,56 T -8,98 Q -18,115 -28,133 L -38,155 L -40,175 L -40,190 L -15,190 L 2,165 L 20,138 Q 42,110 67,90 T 98,58 Q 115,32 135,0 L 150,-25 L 157,-48 L 160,-68 Z" fill="url(%23waveGradient)" opacity="0.55"/></g><g transform="translate(100, 0)"><path d="M 140,-50 L 120,-40 L 105,-25 L 90,-5 Q 70,20 50,35 T 20,60 Q 10,75 0,90 L -10,105 L -20,125 L -20,150 L 0,150 L 15,135 L 30,115 Q 45,95 60,80 T 85,55 Q 95,40 110,20 L 125,0 L 135,-20 L 140,-35 Z" fill="url(%23waveGradient)" opacity="0.75"/><path d="M 145,-60 L 125,-48 L 108,-30 L 92,-8 Q 70,22 48,40 T 15,68 Q 5,82 -5,98 L -15,115 L -25,135 L -25,160 L -5,160 L 10,143 L 25,122 Q 42,100 60,83 T 88,56 Q 100,38 118,15 L 132,-5 L 142,-25 L 145,-42 Z" fill="url(%23waveGradient)" opacity="0.7"/><path d="M 150,-70 L 128,-55 L 110,-35 L 92,-10 Q 68,25 43,45 T 8,78 Q -3,93 -12,108 L -22,128 L -30,148 L -30,170 L -8,170 L 8,150 L 25,128 Q 43,105 63,86 T 92,58 Q 105,38 125,10 L 140,-12 L 148,-32 L 150,-50 Z" fill="url(%23waveGradient)" opacity="0.65"/><path d="M 155,-80 L 130,-62 L 112,-40 L 92,-12 Q 65,28 38,50 T 0,88 Q -10,103 -20,120 L -30,142 L -35,160 L -35,180 L -12,180 L 5,158 L 23,133 Q 43,108 65,88 T 95,58 Q 110,35 130,5 L 145,-18 L 152,-40 L 155,-58 Z" fill="url(%23waveGradient)" opacity="0.6"/><path d="M 160,-90 L 132,-68 L 113,-43 L 90,-13 Q 62,32 32,56 T -8,98 Q -18,115 -28,133 L -38,155 L -40,175 L -40,190 L -15,190 L 2,165 L 20,138 Q 42,110 67,90 T 98,58 Q 115,32 135,0 L 150,-25 L 157,-48 L 160,-68 Z" fill="url(%23waveGradient)" opacity="0.55"/></g></svg>');
      background-size: 100% 100%;
      background-repeat: repeat-x;
      animation: wave-slide 20s linear infinite;
    }
  }

  @keyframes wave-slide {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(-50%);
    }
  }

  .content-container {
    position: relative;
    z-index: 2;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    max-width: 400px;
    padding: 40px 20px;
    text-align: center;
  }

  .title-section {
    margin-bottom: 80px;
    cursor: default;
    .main-title {
      font-size: 85px;
      font-weight: 300;
      color: #ffffff;
      margin: 0 0 16px 0;
      letter-spacing: 2px;
      font-family: PartialSans;
    }

    .sub-title {
      font-size: 23px;
      color: #ffffff;
      margin: 0;
      line-height: 1.5;
      font-weight: 400;
    }
  }

  .login-form__snsbox {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-bottom: 60px;

    .kakao,
    .naver,
    .apple {
      width: 100%;
      padding: 16px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: none;
      font-weight: 600;
      font-size: 16px;

      img {
        width: 24px;
        height: 24px;
        margin-right: 12px;
      }

      p {
        margin: 0;
      }
    }

    .kakao {
      background-color: #ffeb00;
      color: #000000;

      &:hover {
        opacity: 0.9;
        transform: translateY(-2px);
      }
    }

    .naver {
      background-color: #00c73c;
      color: #ffffff;

      &:hover {
        opacity: 0.9;
        transform: translateY(-2px);
      }
    }

    .apple {
      background-color: #ffffff;
      color: #000000;

      &:hover {
        opacity: 0.9;
        transform: translateY(-2px);
      }
    }
  }

  .welcome-text {
    font-size: 24px;
    font-weight: 600;
    color: #ffffff;
    margin-top: auto;
    margin-bottom: 40px;
    cursor:default;
  }
}

@media (max-width: 768px) {
  .main {
    .content-container {
      padding: 30px 20px;
    }

    .main-title {
      font-size: 48px;
    }

    .sub-title {
      font-size: 13px;
    }
  }
}
</style>
