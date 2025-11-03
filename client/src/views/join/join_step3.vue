<template>
  <div>
    <div class="join-wr">
      <el-form ref="form" class="step3" onsubmit="return false;" :model="form" :rules="rules">
        <div class="step3-container">
          <div class="step3-box">
            <p class="ttl sft">이름*</p>
              <el-form-item prop="actualName">
                <el-input placeholder="홍길동" v-model="form.actualName" />
              </el-form-item>
          </div>
          <div class="step3-box">
            <p class="ttl sft">생년월일</p>
            <el-form-item prop="birth">
              <el-date-picker v-model="form.birth" size="large" :default-value="new Date('2000-01-01')" type="date" placeholder="생년월일" class="day" value-format="yyyy-MM-dd" />
            </el-form-item>
          </div>
          <div class="step3-box">
            <p class="ttl sft">성별</p>
            <el-form-item prop="gender">
              <el-select placeholder="성별" class="gen" :popper-append-to-body="false" v-model="form.gender">
                <el-option label="남" :value="0" />
                <el-option label="여" :value="1" />
              </el-select>
            </el-form-item>
          </div>
          <div class="step3-box">
            <p class="ttl sft">연락처*</p>
            <el-form-item prop="concatNumber">
              <el-input placeholder="010-0000-0000" v-model="form.concatNumber" @input="filterConcatNumber" :maxlength="13"/>
            </el-form-item>
          </div>
          <div class="step3-box address">
            <p class="ttl sft">주소*</p>
            <el-form-item prop="address">
              <el-input placeholder="주소를 입력해주세요." v-model="form.address" readonly @click.native="handleVisibleAddress()" />
            </el-form-item>
            <el-form-item prop="addressDetail">
              <el-input class="addressDetail" placeholder="상세 주소를 입력해주세요." v-model="form.addressDetail" />
            </el-form-item>
          </div>
        </div>
      </el-form>
      <div class="step3-bot">
        <a href="#" class="agree" @click="handleJoin()">가입하기</a>
      </div>
    </div>
      <div class="join-address">
        <el-dialog title="주소검색" :visible.sync="addressVisible">
          <vue-daum-postcode style="width: 100%" @complete="handleComplete" />
        </el-dialog>
      </div>
  </div>
</template>

<script lang="ts">
import { JoinModule } from '@/store/join';
import { Vue, Component, Watch } from 'vue-property-decorator';
import { parseConcatNumber } from '@/filters';
import { Form } from 'element-ui';
import { VueDaumPostcode } from 'vue-daum-postcode';
import { UserModule } from '@/store/modules/user';
import { getJwtInfo } from '@/utils/cookies';

@Component({
  name: 'joinStep3',
  components: {
    VueDaumPostcode,
  },
})

export default class extends Vue {
  get form() {
    JoinModule.form.actualName = UserModule.infoName;
    JoinModule.form.concatNumber = UserModule.infoTel;
    return JoinModule.form;
  }

  mounted() {
    this.checkSocialUser();
  }

  private rules: any = {
    actualName: [
      { required: 'true', message: '이름를 입력하세요', trigger: ['blur', 'change'] },
    ],
    concatNumber: [{
        required: 'true', message: '연락처를 입력하세요', trigger: ['blur', 'change'], min: 13,
    }],
    address: [
      { required: 'true', message: '주소를 입력하세요', trigger: ['blur', 'change'] },
    ],
    addressDetail: [
      { required: 'true', message: '상세 주소를 입력하세요', trigger: ['blur', 'change'] },
    ],
  };

  private addressVisible = false;

  private filterConcatNumber() {
    this.form.concatNumber = parseConcatNumber(this.form.concatNumber);
  }

  private handleJoin() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        this.form.step = 3;
        this.$router.push({ name: 'Info' }).catch(() => {
          // console.log('');
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

  private checkSocialUser() {
    /* eslint-disable */
		const token = UserModule.token;
		if (token) {
			const status = getJwtInfo('status');
			if (status) {
				this.$router.push({ name: 'Home' });
			} else {
				if (this.form.step !== 2) {
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

<style>
.el-select.gen{
  width:100%;
}
.el-date-picker {
    top: 220px !important;
}
</style>
