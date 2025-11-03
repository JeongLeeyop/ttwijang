<template>
  <div>
    <div class="join-wr mission-inquiry">
      <el-form ref="form" class="step1" onsubmit="return false;" :model="form" :rules="rules">
        <div class="info-wr">
          <div class="step4-wr">
            <div class="step4__title">
              <p>{{ pageTitle }}</p>
            </div>
            <div
              v-for="(question, index) in questions"
              :key="question.id"
              class="step4__box inquiry"
            >
            <el-form-item :prop="`question${question.id}`" v-if="question.type === '1'">
                <p class="num">Q.{{ index + 1 }}</p>
                <p class="tl">{{ question.title }}</p>
                <el-checkbox-group class="mycheckboxgroup" v-model="form[`question${question.id}`]" @change="(value) => handleSingleSelection(question.id, value)">
                  <el-checkbox-button v-for="item in question.options" :label="item.text" :key="item.value">{{item.text}}</el-checkbox-button>
                </el-checkbox-group>
            </el-form-item>
            <el-form-item :prop="`question${question.id}`" v-else-if="question.type === '2'">
                <p class="num">Q.{{ index + 1 }}</p>
                <p class="tl">{{ question.title }}</p>
                <el-checkbox-group class="mycheckboxgroup" v-model="form[`question${question.id}`]">
                  <el-checkbox-button v-for="item in question.options" :label="item.text" :key="item.value">{{item.text}}</el-checkbox-button>
                </el-checkbox-group>
            </el-form-item>
            <el-form-item :prop="`question${question.id}`" v-else-if="question.type === '3'">
              <p class="num">Q.{{ index + 1 }}</p>
              <p class="tl">{{ question.title }}</p>
              <el-radio-group class="myradiogroup" v-model="form[`question${question.id}`]">
                  <el-radio-button
                    v-for="option in question.options"
                    :key="option.value"
                    :label="option.value"
                  >
                    .
                  </el-radio-button>
                </el-radio-group>
                <div class="txt-wr">
                  <p v-for="label in question.labels" :key="label">{{ label }}</p>
                </div>
              </el-form-item>
              <el-form-item :prop="`question${question.id}`" v-else-if="question.type === '2'">
                <p class="tl">#{{ question.title }}</p>
                <el-checkbox-group class="mycheckboxgroup" v-model="form[`question${question.id}`]">
                  <el-checkbox-button v-for="item in question.options" :label="item.text" :key="item.value">{{item.text}}</el-checkbox-button>
                </el-checkbox-group>
              </el-form-item>
            </div>

            <div class="step3-bot">
              <button @click="handleUpdateUserInfo()" class="">다음</button>
            </div>
          </div>
        </div>
      </el-form>
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
import { getUserInfo } from '@/api/user';
import { getMissionInquiryList, addMissionUserInquiry } from '@/api/missionInquiry';
import { Component, Vue } from 'vue-property-decorator';
import { MissionInquiryModule } from '@/store/modules/missionInquiry';

@Component({
  components: {},
})
export default class extends Vue {
  mounted() {
    this.getUserInfo();
    this.getMissionInquiryList(); // 이 메소드에서 initializeForm()을 호출함
    MissionInquiryModule.setPageClean(3);
    console.log(MissionInquiryModule.missionInquiryForm);
  }

  private form: any = {};

  private pageTitle = '';

  private questions: any[] = [];

  private rules: any = {};

  private initializeForm() {
    // 동적으로 form 객체와 validation rules 생성
    this.questions.forEach((question) => {
      this.$set(this.form, `question${question.id}`, (question.type === '1' || question.type === '2') ? [] : '');
    });
  }

  private getMissionInquiryList() {
    getMissionInquiryList(3).then((res) => {
      if (res.data) {
        this.pageTitle = res.data.title || '질문을 읽고 답을 선택해 주세요.';
        this.questions = res.data.inquiries.map((inquiry: any, index: number) => ({
          id: inquiry.idx,
          title: inquiry.question,
          type: inquiry.type,
          options: this.generateOptionsForType(inquiry.type, inquiry.options),
          labels: this.generateLabelsForType(inquiry.type),
        }));
        this.initializeForm();
      }
    });
  }

  // 타입에 따른 옵션 생성
  private generateOptionsForType(type: string, options: string | null): any[] {
    if (type === '1' || type === '2') {
      if (options && options.trim()) {
        return options.split(',').map((opt, index) => ({
          value: `${index + 1}`,
          text: opt.trim(),
        }));
      }
      return [
        { value: '1', text: '' },
        { value: '2', text: '' },
        { value: '3', text: '' },
      ];
    }
    // type3 (라디오) - 기본 5점 척도
    return [
      { value: '1', text: '전혀 아니다' },
      { value: '2', text: '조금 아니다' },
      { value: '3', text: '보통이다' },
      { value: '4', text: '약간 그렇다' },
      { value: '5', text: '매우 그렇다' },
    ];
  }

  // 타입에 따른 라벨 생성
  private generateLabelsForType(type: string): string[] {
    if (type === '1' || type === '2') {
      return []; // type1(단일선택), type2(다중선택)는 라벨 없음
    }
    return ['전혀 아니다', '보통', '매우 그렇다']; // type3(라디오)는 라벨 있음
  }

  private getUserInfo() {
    getUserInfo().then((res) => {
      // API에서 받은 데이터가 있다면 form에 설정
      if (res.data) {
        Object.keys(res.data).forEach((key) => {
          if (key.startsWith('question')) {
            this.$set(this.form, key, res.data[key]);
          }
        });
      }
    });
  }

  private handleSingleSelection(questionId: number, value: any[]) {
    if (value.length > 1) {
      const lastSelected = value[value.length - 1];
      this.$set(this.form, `question${questionId}`, [lastSelected]);
    }
  }

  private async handleUpdateUserInfo() {
    const unansweredQuestions = this.questions.filter((question) => {
      const answer = this.form[`question${question.id}`];
      return (question.type === '1' || question.type === '2') ? (!answer || answer.length === 0) : !answer;
    });
    if (unansweredQuestions.length > 0) {
      this.$message.warning('모든 항목을 선택해주세요.');
      return;
    }

    // 답변 데이터 생성
    const data: any = [];
    this.questions.forEach((question) => {
      data.push({
        inquiryIdx: question.id,
        answer: this.form[`question${question.id}`],
      });
    });

    MissionInquiryModule.setPage({ page: 3, content: data });

      const form: any = [];

      if (MissionInquiryModule.missionInquiryForm.page1) {
        MissionInquiryModule.missionInquiryForm.page1.forEach((item: any) => {
          const answer = Array.isArray(item.answer) ? item.answer.join(',') : item.answer;
          form.push({ page: 1, inquiryIdx: item.inquiryIdx, answer });
        });
      }

      if (MissionInquiryModule.missionInquiryForm.page2) {
        MissionInquiryModule.missionInquiryForm.page2.forEach((item: any) => {
          const answer = Array.isArray(item.answer) ? item.answer.join(',') : item.answer;
          form.push({ page: 2, inquiryIdx: item.inquiryIdx, answer });
        });
      }

      if (MissionInquiryModule.missionInquiryForm.page3) {
        MissionInquiryModule.missionInquiryForm.page3.forEach((item: any) => {
          const answer = Array.isArray(item.answer) ? item.answer.join(',') : item.answer;
          form.push({ page: 3, inquiryIdx: item.inquiryIdx, answer });
        });
      }

      await addMissionUserInquiry(form).then(() => {
        this.$message.success('설문 조사가 완료되었습니다.');
        this.$router.push({ name: 'Mission' });
      }).catch((error: any) => {
        this.$message.error(error.response.data.message);
      });
  }
}
</script>
