<template>
  <div>
    <div class="join-wr">
      <el-form ref="form" class="step3" onsubmit="return false;" :model="form" :rules="rules">
        <div class="step3-container">
          <div class="step-box">
            <el-form-item label="이름" prop="actualName">
              <el-input type="text" v-model="form.actualName" />
            </el-form-item>
          </div>

          <div class="step-box">
            <el-form-item label="생년월일" prop="birth">
              <el-date-picker type="date" v-model="form.birth" value-format="yyyy-MM-dd" />
            </el-form-item>
          </div>

          <div class="step-box">
            <div class="select-box__gen">
              <el-form-item label="성별" prop="gender">
                <el-select placeholder="성별" class="gen" :popper-append-to-body="false" v-model="form.gender">
                  <el-option label="남" :value="0" />
                  <el-option label="여" :value="1" />
                </el-select>
              </el-form-item>
            </div>
          </div>
         <div class="step-box">
            <el-form-item prop="concatNumber" label="연락처">
              <el-input placeholder='"-"를 빼고 적어주세요.' maxlength="13" v-model="form.concatNumber" @input="filterConcatNumber()" />
            </el-form-item>
          </div>

          <div class="step-box">
            <el-form-item prop="email" label="이메일">
              <el-input v-model="form.email" placeholder="이메일을 입력해주세요." />
            </el-form-item>
          </div>

          <div class="step-box">
            <el-form-item prop="address" label="주소">
              <el-input placeholder="주소를 입력해주세요." v-model="form.address" readonly @click.native="handleVisibleAddress()" />
            </el-form-item>
            <el-form-item prop="addressDetail">
              <el-input placeholder="상세 주소를 입력해주세요." v-model="form.addressDetail" />
            </el-form-item>
          </div>

          <div class="step-box marketing-agree">
            <el-form-item prop="marketingStatus" label="마케팅 정보수신">
              <el-checkbox v-model="form.marketingStatus">동의</el-checkbox>
            </el-form-item>
          </div>
        </div>
        <div class="info-wr">
          <div class="step4-wr">
            <div class=step4__box>
              <el-form-item prop="dietExperience">
                <p class="tl">식단관리를 해보신 경험이 있으신가요?</p>
                <el-radio-group class="myradiogroup" v-model="form.dietExperience">
                  <el-radio-button :label="true">네</el-radio-button>
                  <el-radio-button :label="false">아니요</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </div>
            <p class="tl">키와 몸무게를 입력해주세요.</p>
            <div class="step4__box2">
              <div class="input__box2">
                <el-form-item prop="height">
                  <label for="" style="z-index: 1">cm</label>
                  <el-input-number placeholder="키" v-model="form.height" :controls="false" :min="0" />
                </el-form-item>
              </div>
              <div class="input__box2">
                <el-form-item prop="weight">
                  <label for="userKg" style="z-index: 1">kg</label>
                  <el-input-number placeholder="몸무게" class="kg" v-model="form.weight" :controls="false" :min="0" />
                </el-form-item>
              </div>
            </div>

            <div class="step4__box3">
              <p class="tl">목표로 하는 몸무게를 입력해주세요.</p>
              <div class="input__box2">
                <el-form-item prop="goalWeight">
                  <label for="userKg2" style="z-index: 1; right: 33%;">kg</label>
                  <el-input-number placeholder="몸무게" class="kg" v-model="form.goalWeight" :controls="false" :min="0" />
                </el-form-item>
              </div>

              <p class="tl">목표달성을 언제까지 하시길 원하십니까?</p>
              <div class="input__box__date">
                <el-form-item prop="goalDate">
                  <el-date-picker type="date" class="left" value-format="yyyy-MM-dd" placeholder="" v-model="form.goalDate" />
                </el-form-item>
              </div>
            </div>

            <p class="tl">당신의 활동 수준은 어느 정도인가요?</p>
            <div class="step4__box4">
              <el-form-item prop="activityLevel">
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
              </el-form-item>
            </div>

            <div class="step4__box5">
              <p class="tl">식단관리를 하는 목적은 무엇인가요?</p>
              <el-form-item prop="dietPurpose">
                <el-radio-group class="myradiogroup" v-model="form.dietPurpose">
                  <el-radio-button label="다이어트">다이어트</el-radio-button>
                  <el-radio-button label="영양균형">영양균형</el-radio-button>
                  <el-radio-button label="체중증량">체중증량</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </div>

            <div class="step3-bot">
              <button @click="handleUpdateUserInfo()" class="home43">저장</button>
            </div>
          </div>
        </div>
      </el-form>
      <el-dialog width="90%" :visible.sync="addressVisible">
        <vue-daum-postcode style="width: 100%" @complete="handleComplete" />
      </el-dialog>
    </div>
  </div>
</template>

  <style>
    .el-form.step3 .select-box__gen .el-form-item__content {
      justify-content: end;
      display: flex;
    }
    .el-form.step3 .step-box .el-input__inner {
      padding-right: 30px;
    }
  </style>

<script lang="ts">
import { getUserInfo, updateUserInfo } from '@/api/user';
import { parseConcatNumber } from '@/filters';
import { Form } from 'element-ui';
import { Component, Vue } from 'vue-property-decorator';
import { VueDaumPostcode } from 'vue-daum-postcode';

@Component({
  components: {
    VueDaumPostcode,
  },
})
export default class extends Vue {
  mounted() {
    this.getUserInfo();
  }

  private form = {
    actualName: '',
    birth: '',
    gender: 0,
    concatNumber: '',
    dietExperience: false,
    weight: 0,
    height: 0,
    goalWeight: 0,
    goalDate: '',
    activityLevel: 0,
    dietPurpose: '',
    address: '',
    addressDetail: '',
    postCode: '',
    marketingStatus: false,
  }

  private addressVisible = false;

  private rules: any = {
    actualName: [
      { required: true, message: '이름을 입력해주세요.', trigger: ['blur', 'change'] },
    ],
    concatNumber: [
      { required: 'true', message: '연락처를 입력하세요', trigger: ['blur', 'change'] },
    ],
    email: [
      { required: 'true', message: '이메일을 입력하세요', trigger: ['blur', 'change'] },
      { pattern: /^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, message: 'email@email.com 형태로 입력해주세요', trigger: ['blur', 'change'] },
    ],
    address: [
      { required: 'true', message: '주소를 입력하세요', trigger: ['blur', 'change'] },
    ],
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

  private getUserInfo() {
    getUserInfo().then((res) => {
      this.form = res.data;
    });
  }

  private filterConcatNumber() {
    this.form.concatNumber = parseConcatNumber(this.form.concatNumber);
  }

  private handleUpdateUserInfo() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        updateUserInfo(this.form).then(() => {
          this.$message.success('회원정보가 수정되었습니다.');
          // this.getUserInfo();
        });
      }
    });
  }

  private handleVisibleAddress() {
    this.addressVisible = !this.addressVisible;
  }

  private handleComplete(result: any) {
    this.form.postCode = result.zonecode;
    this.form.address = result.address;
    this.handleVisibleAddress();
  }
}
</script>
