<template>
  <div>
    <div class="home4-1-3__a">

      <el-form ref="form" class="" onsubmit="return false;" :model="form" :rules="rules">
      <div class="diary2-2-3__top">
        <router-link :to="{ name: 'Delivery' }" class="el-button el-button--default"><img src="~@/assets/images/cancel.png" class="cancle_img" alt="닫기">
        </router-link>
      </div>
      <div class="home4-1-4__top">
        <p class="ab">주문을 취소하시는 이유는 무엇입니까?</p>
      </div>

      <div class="home4-1-4__mid">
        <el-form-item prop="category">
        <el-select :popper-append-to-body="false" v-model="form.category" @change="categoryEvent()" placeholder="카테고리를 선택해주세요.">
            <el-option value="1" label="단순변심" />
            <el-option value="2" label="다른 상품 추가 후 재주문 예정" />
            <el-option value="3" label="다른 상품 잘못 주문" />
            <el-option value="4" label="기타 ( 직접입력 )" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item prop="content"> -->
        <textarea ref="content" prop="content" v-model="form.content" placeholder="상세 사유를 입력해주세요." maxlength="200">
        </textarea>
        <!-- </el-form-item> -->

      </div>

    </el-form>
      <div class="home4-1-4__bot">
        <a href="#" @click="handleCancel()" class="link__a">취소 완료</a>
      </div>

    </div>
  </div>
</template>

<script lang="ts">
/* eslint-disable */
import { Vue, Component } from 'vue-property-decorator';
import { JoinModule } from '@/store/join';
import { Form } from 'element-ui';

@Component({
  name: 'cancelStep2',
})

export default class extends Vue {

  get form() {
    return JoinModule.form;
  }

  private categoryEvent() {
    if(this.form.category==4){
      this.categoryFlag = true;
    } else {
      this.categoryFlag = false;
    }
  }

  private categoryFlag: boolean = false;


  private rules: any = {
    category: [
      { required: 'true', message: '카테고리를 선택해주세요.', trigger: ['blur', 'change'] },
    ],
    content: [
      { required: 'true', message: '상세사유를 입력해주세요.', trigger: ['blur', 'change'] },
    ],
  };

  private handleCancel() {
    (this.$refs.form as Form).validate((valid: boolean) => {
      if (valid) {
        if(this.categoryFlag){
          const str = (this.$refs.content as any).value;
          if(str.length==0){
            alert("상세사유는 필수 값입니다.");
            return false;
          }
        }
        this.$router.push({ name: 'cancelStep3' }).catch(() => {
         this.$router.push({ name: 'cancelStep3' });
        });
      }
    });
  }
}
</script>