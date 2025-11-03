<template>
  <div>
    <div class="tfse">
      <div class="tfse__top">
        <p class="tfse__top-date">
          {{ tfseDate | parseDate("YYYY년 MM월 DD일") }}
        </p>

        <div class="tfse__nav-box">
          <router-link :to = " { name: 'Tfse' } " class="tfse__nav color2">TFSE 일지</router-link>
          <router-link :to = " { name: 'SelfFeedback' } " class="tfse__nav">셀프피드백</router-link>
        </div>
      </div>
      <div class="tfse__content">
        <div class="tfse__write-list">
          <div class="tfse__write-item">
            <div class="tfse__write-ttl sft">시간선택</div>
            <div class="tfse__write-select">
              <select v-model="timeAt" name="ampm" class="ampm">
                <option value="오전">오전</option>
                <option value="오후">오후</option>
              </select>
              <select @change="handleTfseDate" v-model="hour" name="hour" class="hour">
                <option value="01">01</option>
                <option value="02">02</option>
                <option value="03">03</option>
                <option value="04">04</option>
                <option value="05">05</option>
                <option value="06">06</option>
                <option value="07">07</option>
                <option value="08">08</option>
                <option value="09">09</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
              </select>
              <select @change="handleTfseDate" v-model="minute" name="minute" class="minute">
                <option value="00">00</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="30">30</option>
                <option value="40">40</option>
                <option value="50">50</option>
              </select>
            </div>
          </div>
          <div class="tfse__write-item">
            <div class="tfse__write-ttl sft">포만감지수</div>
            <div class="unid__count">
              <el-input-number
                v-model="tfse.satietyScore"
                :min="0"
                :max="9"
              ></el-input-number>
            </div>
          </div>
					<div class="tfse__write-textarea">
							<textarea v-model="tfse.foodText" name="ate" id="ate" cols="30" rows="8" required placeholder="먹은 음식을 입력해주세요"></textarea>
							<textarea v-model="tfse.emotionText" name="feeling" id="feeling" cols="30" rows="8" required placeholder="느낀 감정을 입력해주세요"></textarea>
						</div>
        </div>
      </div>
      <button @click="addTfse" class="unid__btn">
        <img src="~@/assets/images/unid_btn_check.svg" alt="" /> 작성완료
      </button>
      <button @click="delTfse" v-if="this.$route.params.tfseIdx" class="unid__del-btn">삭제하기</button>

    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import moment from 'moment';
import { addTfse, getTfse, delTfse } from '@/api/tfse';

@Component({
  components: {},
})
export default class extends Vue {
  private count = Number(this.$route.params.cnt);

  private tfseDate: any = this.$route.query.tfseDate ? this.$route.query.tfseDate : moment().format('YYYY-MM-DD');

  private type: any = 'diary';

  private timeAt: any = moment().format('A');

  private hour: any = moment().format('hh');

  private minute: any = this.roundToNearest10Minutes();

  mounted() {
    this.handleTfseDate();
    this.handleTfseForm();
  }

  private roundToNearest10Minutes() {
      const remainder = (moment().minute() % 10) * -1; // 10분 간격으로 조정
      return moment().add(remainder, 'minutes').startOf('minute').format('mm');
  }

  private handleTfseDate() {
    let hour = this.hour;
    if (this.timeAt === '오후' && this.hour < 12) {
      hour = parseInt(this.hour, 10) + 12; // 오후 시간으로 변환
    }
    this.tfse.tfseDate = `${this.tfseDate}T${hour}:${this.minute}:00`;
  }

  private tfse: any = {
    tfseDate: '',
    foodText: '',
    emotionText: '',
    satietyScore: 0,
    secretStatus: true,
  }

  private addTfse() {
    if (this.$route.params.tfseIdx) {
      addTfse(this.tfse).then(() => {
        this.$message.info('정상적으로 수정되었습니다.');
      });
    } else {
      addTfse(this.tfse).then(() => {
        this.$message.info('정상적으로 등록되었습니다.');
      });
    }
    this.$router.push({ name: 'Tfse', query: { tfseDate: this.tfseDate } });
  }

  private delTfse() {
    this.$confirm('정말 삭제하시겠습니까?', 'TFSE일지 삭제', {
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
      type: 'error',
    }).then(() => {
    if (this.$route.params.tfseIdx) {
      delTfse(this.$route.params.tfseIdx).then(() => {
        this.$message.info('정상적으로 삭제되었습니다.');
      });
    }
    this.$router.push({ name: 'Tfse', query: { tfseDate: this.tfseDate } });
    });
  }

  private handleTfseForm() {
    if (this.$route.params.tfseIdx) {
      getTfse(this.$route.params.tfseIdx).then((res) => {
        this.tfse = res.data;
        this.timeAt = moment(this.tfse.tfseDate).format('A');
        this.hour = moment(this.tfse.tfseDate).format('hh');
        const remainder = (moment(this.tfse.tfseDate).minute() % 10) * -1; // 10분 간격으로 조정
        this.minute = moment(this.tfse.tfseDate).add(remainder, 'minutes').startOf('minute').format('mm');
      });
    }
  }

  private handleAddDay(days: number) {
    this.tfseDate = moment(this.tfseDate, 'YYYY-MM-DD')
      .add(days, 'days')
      .format('YYYY-MM-DD');
    this.$router.replace({
      ...this.$router,
      query: { tfseDate: this.tfseDate },
    });
  }
}
</script>
