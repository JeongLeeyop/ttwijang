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
import { getMissionInquiryList } from '@/api/missionInquiry';
import { Component, Vue } from 'vue-property-decorator';
import { MissionInquiryModule } from '@/store/modules/missionInquiry';

@Component({
  components: {},
})
export default class extends Vue {
  mounted() {
    this.getUserInfo();
    this.getMissionInquiryList(); // 이 메소드에서 initializeForm()을 호출함
    MissionInquiryModule.setPageClean(1);
    console.log(MissionInquiryModule.missionInquiryForm);
  }

  private form: any = {};

  private pageTitle = '';

  private questions: any[] = [];

  private rules: any = {};

  private initializeForm() {
    // 동적으로 form 객체와 validation rules 생성
    this.questions.forEach((question) => {
      // type '1', '2'는 배열, type '3'는 문자열
      this.$set(this.form, `question${question.id}`, (question.type === '1' || question.type === '2') ? [] : '');
      // this.$set(this.rules, `question${question.id}`, [
      //   { required: true, message: `질문 ${question.id}에 답해주세요.`, trigger: 'change' },
      // ]);
    });
  }

  private getMissionInquiryList() {
    getMissionInquiryList(1).then((res) => {
      if (res.data) {
        // 페이지 제목 설정
        this.pageTitle = res.data.title || '질문을 읽고 답을 선택해 주세요.';
        // API 데이터를 questions 형식으로 변환
        this.questions = res.data.inquiries.map((inquiry: any, index: number) => ({
          id: inquiry.idx,
          title: inquiry.question,
          type: inquiry.type,
          options: this.generateOptionsForType(inquiry.type, inquiry.options),
          labels: this.generateLabelsForType(inquiry.type),
        }));
        // form 초기화 (questions가 업데이트된 후)
        this.initializeForm();
      }
    });
  }

  // 타입에 따른 옵션 생성
  private generateOptionsForType(type: string, options: string | null): any[] {
    if (type === '1' || type === '2') {
      // type1(단일선택), type2(다중선택) - 커스텀 옵션이 있으면 사용, 없으면 기본값
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

  // type1(단일선택)에서 하나만 선택되도록 처리
  private handleSingleSelection(questionId: number, value: any[]) {
    if (value.length > 1) {
      // 마지막으로 선택된 항목만 유지
      const lastSelected = value[value.length - 1];
      this.$set(this.form, `question${questionId}`, [lastSelected]);
    }
  }

  private handleUpdateUserInfo() {
    // 모든 질문에 답했는지 확인
    const unansweredQuestions = this.questions.filter((question) => {
      const answer = this.form[`question${question.id}`];
      // type '1', '2'는 배열이므로 length 확인, type '3'는 값 존재 확인
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

    MissionInquiryModule.setPage({ page: 1, content: data });
    this.$router.push({ name: 'MissionInquiry2' });
  }
}
</script>
