<template>
  <div>
    <div class="step4-head">
      <p class="tl">고객님이 알려주신<br>정보를 토대로 맞춤식단 관리가<br>이루어 집니다.</p>
    </div>

    <div class="join-wr">
      <el-form ref="form" :model="form" :rules="rules" onsubmit="return false;">
        <div class="step4-wr">
          <div class=step4__box>
            <p class="tl">식단관리를 해보신 경험이 있으신가요?</p>
            <el-form-item prop="dietExperience">
              <el-radio-group class="myradiogroup" v-model="form.dietExperience">
                <el-radio-button :label="true">네</el-radio-button>
                <el-radio-button :label="false">아니요</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </div>

          <p class="tl">키와 몸무게를 입력해주세요.</p>
          <div class="step4__box2">
            <el-form-item prop="height">
              <div class="input__box2">
                <label for="">cm</label>
                <el-input placeholder="키" type="text" class="" v-model="form.height" />
              </div>
            </el-form-item>

            <el-form-item prop="weight">
              <div class="input__box2">
                <label for="userKg">kg</label>
                <el-input placeholder="몸무게" type="text" class="kg" v-model="form.weight" />
              </div>
            </el-form-item>
          </div>

          <div class="step4__box3">
            <p class="tl">목표로 하는 몸무게를 입력해주세요.</p>
            <el-form-item prop="goalWeight">
              <div class="input__box2">
                <label for="userKg2">kg</label>
                <el-input placeholder="몸무게" @change="computeGoalDate" type="text" class="kg" v-model="form.goalWeight" />
              </div>
            </el-form-item>

            <p class="tl">목표달성을 언제까지 하시길 원하십니까?</p>
            <el-form-item prop="goalDate">
              <div class="input__box__date">
                <p v-if="tipVisible" class="tip">웨일리잇에서 권장하는 날짜입니다.</p>
                <el-date-picker type="date" placeholder="" @change="tipVisible = false" class="ca" v-model="form.goalDate" value-format="yyyy-MM-dd">
                </el-date-picker>
              </div>
            </el-form-item>
          </div>

          <p class="tl">당신의 활동 수준은 어느 정도인가요?</p>
          <el-form-item prop="activityLevel">
            <div class="step4__box4">
              <el-radio-group v-model="form.activityLevel">
                <el-radio-button :label="1" class="rb1">
                  <p class="acl1">비활동적</p>
                  <p class="aclb">일상생활엔 쉬기, 책상업무 또는 운전 등<br>최소한의 노력이 필요합니다.</p>
                </el-radio-button>
                <el-radio-button :label="2" class="rb2">
                  <p class="acl1 color2">저활동적</p>
                  <p class="aclb">일상생활엔 단시간 서있기, 집안일 또는<br>가벼운 운동과 같은 몇가지 노력이 필요합니다</p>
                </el-radio-button>
                <el-radio-button :label="3" class="rb3">
                  <p class="acl1 color3">활동적</p>
                  <p class="aclb">일상생활엔 서있기, 근육운동 또는 적당한<br>운동과 같은 저당한 노력이 필요합니다.</p>
                </el-radio-button>
                <el-radio-button :label="4" class="rb4">
                  <p class="acl1 color4">매우 활동적</p>
                  <p class="aclb">일상생활엔 공사일 또는 일반 활발한 운동<br>등의 격렬한 육체적 노력을 필요합니다.</p>
                </el-radio-button>
              </el-radio-group>
            </div>
          </el-form-item>

          <div class="step4__box5">
            <p class="tl">식단관리를 하는 목적은 무엇인가요?</p>
            <el-form-item prop="dietPurpose">
              <el-radio-group class="myradiogroup" v-model="form.dietPurpose">
                <el-radio-button label="다이어트"></el-radio-button>
                <el-radio-button label="영양균형"></el-radio-button>
                <el-radio-button label="체중증량"></el-radio-button>
              </el-radio-group>
            </el-form-item>
          </div>
          <div class="step3-bot">
            <a href="#" class="agree" @click="handleJoin()">가입하기</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style>
  .tip {
    font-family: Noto Sans KR;
    font-size: 15px;
    line-height: 18px;
    padding: 0 0 4px 0;
    color: red;
    position: absolute;
    top: -22px;
    left: 110px;
    text-align: center;
  }
</style>

<script lang="ts">
import { JoinModule } from '@/store/join';
import { Vue, Component, Watch } from 'vue-property-decorator';
import { Form } from 'element-ui';
import { updateUserInfo } from '@/api/user';
import { UserModule } from '@/store/modules/user';
import { getJwtInfo, removeToken } from '@/utils/cookies';
import moment from 'moment';
import { saveFcmToken } from '@/api/fcm-token';

@Component({
  name: 'joinStep4',
  components: {
  },
})

export default class extends Vue {
  get form() {
    return JoinModule.form;
  }

  async mounted() {
    // To-do
    // 플러터 웹뷰가 응답할 준비가 되었는지 체크하는 부분이지만, 응답이 오지 않아서 주석처리함.
    // 일부 환경에서 flutter_inappwebview.callHandler 부분을 찾을 수 없다고 에러가 뜰 가능성이 있음
    // await window.addEventListener('flutterInAppWebViewPlatformReady', async (event) => {
      //  await (window as any).flutter_inappwebview.callHandler('fcmToken').then((result: any) => {
        // this.fcmToken.token = result.fcmToken;
      // }).catch((error: any) => {
        // console.error('Error getting FCM Token:', error); // 에러 로그 확인
      // });
    // });
    this.checkSocialUser();
  }

  private fcmToken = {
    token: '',
  }

  private tipVisible = false;

  private rules: any = {
    dietExperience: [
      { required: true, message: '식단관리 경험을 체크하세요.', trigger: ['blur', 'change'] },
    ],
    height: [
      { required: true, message: '키를 입력하세요.', trigger: ['blur', 'change'] },
    ],
    weight: [
      { required: true, message: '몸무게를 입력하세요.', trigger: ['blur', 'change'] },
    ],
    goalWeight: [
      { required: true, message: '목표 몸무게를 입력하세요.', trigger: ['blur', 'change'] },
    ],
    goalDate: [
      { required: true, message: '목표 달성 날짜를 입력하세요.', trigger: ['blur', 'change'] },
    ],
    activityLevel: [
      { required: true, message: '활동 수준을 선택하세요.', trigger: ['blur', 'change'] },
    ],
    dietPurpose: [
      { required: true, message: '식단관리 목적을 선택하세요.', trigger: ['blur', 'change'] },
    ],
  };

  private handleJoin() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        updateUserInfo(this.form).then(async () => {
          this.$message.success('회원가입이 완료되었습니다.');
          if (this.fcmToken.token !== '') await saveFcmToken(this.fcmToken);
          window.localStorage.removeItem('isRegister');
          this.$router.push({ name: 'Login' });
        });
      }
    });
  }

  private computeGoalDate() {
    let gap = (this.form.weight - this.form.goalWeight);
    if (gap < 0) {
      gap *= -1;
    }
    this.form.goalDate = (moment().add(gap / 0.5, 'weeks').format('YYYY-MM-DD'));
    this.tipVisible = true;
  }

  private checkSocialUser() {
    /* eslint-disable */
    const token = UserModule.token;
    if (token) {
      const status = getJwtInfo('status');
      if (status) {
        this.$router.push({ name: 'Home' });
      } else {
        if (this.form.step !== 3) {
          this.$router.push({ name: 'Agree' });
        }
      }
    } else {
      this.$router.push({ name: 'Login' });
    }
    /* eslint-enable */
  }
}
</script>
