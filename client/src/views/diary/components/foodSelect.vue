<template>
  <div class="diary2-2-3">
    <div class="diary2-2-3__top">
      <el-button @click="handleVisibleSelect()">
        <img src="~@/assets/images/cancel-gray.png" class="cancel_img" alt="닫기">
      </el-button>
    </div>
    <div class="diary2-2-3__mid">
      <p class="ti">해물 떡볶이1</p>
      <p class="tti">영양정보</p>
      <div class="diary2-2-3__mid__a">
        <div class="diary2-2-3__mid__ab">
          <p>칼로리(Kcal)</p>
          <p>탄수화물(g)</p>
          <p>단백질(g)</p>
          <p>지방(g)</p>
          <p>나트륨(mg)</p>
          <p>당(g)</p>
          <p>포화지방산(g)</p>
          <p>트랜스지방(mg)</p>
          <p>콜레스테롤(mg)</p>
          <p>칼슘(mg)</p>
          <p>철(mg)</p>
          <p>칼륨(mg)</p>
          <p>식이섬유(g)</p>
          <p>비타민C(mg)</p>
        </div>
        <div class="diary2-2-3__mid__abc">
{{ foods }}
          <p>{{ foods.kcal }}</p>
          <p>{{ foods.carbohydrate }}</p>
          <p>{{ foods.protein }}</p>
          <p>{{ foods.fat }}</p>
          <p>{{ foods.natrium }}</p>
          <p>{{ foods.sugar }}</p>
          <p>{{ foods.stFattyAcid }}</p>
          <p>{{ foods.transFat }}</p>
          <p>{{ foods.cholesterol }}</p>
          <p>{{ foods.calcium }}</p>
          <p>{{ foods.iron }}</p>
          <p>{{ foods.potassium }}</p>
          <p>{{ foods.dietaryFiber }}</p>
          <p>{{ foods.vitaminC }}</p>
        </div>
      </div>

      <p class="tti">출처</p>
      <p class="ttti">{{ foods.ref }}</p>

      <div class="diary2-2-3__mid__abcd">
        <el-input-number v-model="num" @change="handleChange" :min="1" :max="10"></el-input-number>
        <p class="tttti">1인분 : 1그릇(280g)</p>
      </div>

    </div>

    <div class="diary2-2-3__bot a">
      <el-button @click="handleVisibleSelect()">등록</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'foods',
  props: {
    foods: Object,
  },
  components: {
  },
  data() {
    return {
      num: '',
    };
  },
  methods: {
    handleVisibleSelect() {
      this.$emit('close');
    },
    handleChange(num) {
      // console.log(num);
    },
  },
};
</script>
