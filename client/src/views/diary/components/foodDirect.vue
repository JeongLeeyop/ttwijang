<template>
  <div>
    <div class="diary2-2-6">
      <el-form ref="form" :model="form" :rules="rules" onsubmit="return false">
        <div class="diary2-2-6__top">
          <p class="ti">메뉴명</p>
          <label for="name"></label>
          <el-form-item prop="menuName">
            <el-input type="text" id="name" v-model="form.menuName" />
          </el-form-item>

          <div class="diary2-2-6__top__a">
            <p class="ti">섭취량</p>
            <label for=""></label>
            <el-form-item prop="amount">
              <el-input type="text" id="" class="a1" v-model="form.amount" />
            </el-form-item>
            <div class="select-box__gen">
              <el-select placeholder="양 단위 선택" class="b1" :popper-append-to-body="false" v-model="form.amountUnit">
                <el-option v-for="unit in unitList" :key="unit" :label="unit" :value="unit"></el-option>
              </el-select>
            </div>
          </div>

          <p class="ti">칼로리</p>
          <div class="input__box a">
            <label for="">Kcal</label>
            <el-form-item prop="calorie">
              <el-input type="text" id="" class="c1" v-model="form.calorie" />
            </el-form-item>
          </div>
          <p class="ti">영양정보</p>
        </div>

        <div class="diary2-2-6__mid">
          <div class="diary2-2-6__mid__a">
            <label for="carbohydrate">탄수화물(g)</label>
            <el-form-item prop="carbohydrate">
              <el-input type="text" id="ts" v-model="form.carbohydrate" />
            </el-form-item>
            <label for="protein">단백질(g)</label>
            <el-form-item prop="protein">
              <el-input type="text" id="protein" v-model="form.protein" />
            </el-form-item>
            <label for="fat">지방(g)</label>
            <el-form-item prop="fat">
              <el-input type="text" id="fat" v-model="form.fat" />
            </el-form-item>
            <label for="natrium">나트륨(mg)</label>
            <el-form-item prop="sodium">
              <el-input type="text" id="natrium" v-model="form.sodium" />
            </el-form-item>
            <label for="sugar">당(g)</label>
            <el-form-item prop="sugar">
              <el-input type="text" id="sugar" v-model="form.sugar" />
            </el-form-item>
            <label for="saturated_fatty_acid">포화지방산(g)</label>
            <el-form-item prop="saturatedFattyAcids">
              <el-input type="text" id="saturated_fatty_acid" v-model="form.saturatedFattyAcids" />
            </el-form-item>
            <label for="vitamin_c">비타민C(mg)</label>
            <el-form-item prop="vitaminC">
              <el-input type="text" id="vitamin_c" v-model="form.vitaminC" />
            </el-form-item>
          </div>

          <div class="diary2-2-6__mid__b">
            <label for="trans_fat">트랜스지방(mg)</label>
            <el-form-item prop="transFat">
              <el-input type="text" id="trans_fat" v-model="form.transFat" />
            </el-form-item>
            <label for="cholesterol">콜레스테롤(mg)</label>
            <el-form-item prop="cholesterol">
              <el-input type="text" id="cholesterol" v-model="form.cholesterol" />
            </el-form-item>
            <label for="calcium">칼슘(mg)</label>
            <el-form-item prop="calcium">
              <el-input type="text" id="calcium" v-model="form.calcium" />
            </el-form-item>
            <label for="iron">철(mg)</label>
            <el-form-item prop="iron">
              <el-input type="text" id="iron" v-model="form.iron" />
            </el-form-item>
            <label for="potassium">칼륨(mg)</label>
            <el-form-item prop="potassium">
              <el-input type="text" id="potassium" v-model="form.potassium" />
            </el-form-item>
            <label for="dietary_fiber">식이섬유(g)</label>
            <el-form-item prop="dietaryFiber">
              <el-input type="text" id="dietary_fiber" v-model="form.dietaryFiber" />
            </el-form-item>
          </div>
        </div>

        <div class="diary__b__bot">
          <el-button @click="handleAddDiaryMeal()">등록하기</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { addDiaryMeal } from '@/api/diary';
import { Form } from 'element-ui';
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';

@Component({
})

export default class extends Vue {
  mounted() {
    if (this.$route.query.diaryDate && this.$route.query.type) {
      this.form.diaryDate = this.$route.query.diaryDate;
      this.form.type = this.$route.query.type;
    } else {
      this.$message.warning('잘못된 접근입니다.');
      this.$router.push({ name: 'Diary' });
    }
  }

  private form: any = {
    menuName: '',
    amount: 0,
    amountUnit: 'g',
    calorie: 0,
    carbohydrate: 0, // 탄수화물(g)
    protein: 0, // 단백질(g)
    fat: 0, // 지방(g)
    sodium: 0, // 나트륨(mg)
    sugar: 0, // 당(g)
    saturatedFattyAcids: 0, // 포화지방(g)
    vitaminC: 0, // 비타민c(mg)
    transFat: 0, // 트랜스지방(mg)
    cholesterol: 0, // 콜레스테롤(mg)
    calcium: 0, // 칼슘(mg)
    iron: 0, // 철(mg)
    potassium: 0, // 컬륨(mg)
    dietaryFiber: 0, // 식이섬유(g)
    type: 0,
    diaryDate: '',
  }

  private rules = {
    menuName: [
      { required: true, message: '메뉴명을 입력해주세요.', trigger: ['blur', 'change'] },
    ],
    amount: [
      { required: true, message: '섭취량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    calorie: [
      { required: true, message: '칼로리를 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    carbohydrate: [
      { required: true, message: '탄수화물 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    protein: [
      { required: true, message: '단백질 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    fat: [
      { required: true, message: '지방 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    sodium: [
      { required: true, message: '나트륨 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    sugar: [
      { required: true, message: '당 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    saturatedFattyAcids: [
      { required: true, message: '포화지방 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    vitaminC: [
      { required: true, message: '비타민C 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    transFat: [
      { required: true, message: '트랜스 지방 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    cholesterol: [
      { required: true, message: '콜레스테롤 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    calcium: [
      { required: true, message: '칼슘 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    iron: [
      { required: true, message: '철 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    potassium: [
      { required: true, message: '칼륨 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
    dietaryFiber: [
      { required: true, message: '식이섬유 함량을 입력해주세요.', trigger: ['blur', 'change'] },
      { pattern: /^[0-9]+(.[0-9]+)?$/, message: '숫자만 입력 가능합니다.', trigger: ['blur', 'change'] },
    ],
  }

  private unitList = ['g', 'ml'];

  private handleAddDiaryMeal() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        addDiaryMeal(this.form).then(() => {
          this.$message.success('다이어리에 추가되었습니다.');
          this.$router.push({ name: 'Diary', query: { ...this.$route.query } });
        });
      }
    });
  }
}
</script>
