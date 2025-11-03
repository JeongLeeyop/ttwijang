<template>
  <div v-loading="loading">
    <div>
      <div class="home3-1">
        <el-form ref="form" :model="form">
          <div class="home3-1__top">
            <el-form-item v-for="(category, index) in categories" :key="index">
              <p class="ti">{{ category.name }}</p>
              <el-radio-group>
                <!-- <el-radio-button
                  v-if="config.type === 'dietPrecaution'"
                  :label="null"
                >
                  없음
                </el-radio-button> -->
                <el-radio-button
                  v-for="(field, index2) in category.fields"
                  :key="index2"
                  :label="field.idx"
                >
                  {{ field.name }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </div>

          <div class="home3-1__bot">
            <div class="home3-1__bot__a">
              <a style="cursor: pointer;" @click="handleAddDietConfig()" class="diet_next_button">맞춤식단 생성하기</a>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';
import { getProductCategoryList } from '@/api/productCategory';

@Component({
  name: 'DietCheck',
})

export default class extends Vue {
  private loading = true;

  private categories = [];

  private form = {};

  private getProductCategoryList() {
    getProductCategoryList().then((res) => {
      this.loading = false;
      this.categories = res.data;
    });
  }

  private getGroupClass(category: any) {
    let groupClass = 'myradiogroup';
    if (category.type === 'dietPrecaution') groupClass = 'ra1';
    if (category.items.length > 2) groupClass = 'ra1';
    if (category.items.length > 3) groupClass = 'ra2';
    return groupClass;
  }
}
</script>

<style>
.home3-1 .el-form-item__error {
  width: 100%;
  text-align: center;
  font-size: 14px;
  margin-top: 5px;
}
</style>
