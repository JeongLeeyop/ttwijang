<template>
  <div class="home3-2__bot">
    <ul>
      <li v-for="(item, i) in receivedData1" :key="i" class="home3-2__list">
        <div class="img__wrap">
          <div class="imgg">
            <img src="~@/assets/images/rice.png" alt="" />
          </div>
          <div class="img__black" v-if="testModel.salad < 1"></div>
          <el-input-number v-model="item.count" :min="0" :max="10"></el-input-number>
        </div>
        <div class="home3-2__box">
          <p class="ti color2">불고기 샐러드{{ salad }}</p>
          <p class="tti">저탄수화물 곡물밥, 당근,버섯,<br />애호박,양파 *특제 데리야끼 소스</p>
        </div>
      </li>
      <!-- <li class="home3-2__list">
            <div class="img__wrap">
              <div class="imgg">
                <img src="~@/assets/images/rice.png" alt="" />
              </div>
              <div class="img__black" v-if="num1 < 1"></div>
            </div>
            <div class="home3-2__box">
              <el-input-number
                v-model="num1"
                @change="handleChange"
                :min="0"
                :max="10"
              ></el-input-number>
              <p class="ti color2">불고기 샐러드</p>
              <p class="tti">저탄수화물 곡물밥, 당근,버섯,<br />애호박,양파 *특제 데리야끼 소스</p>
            </div>
          </li> -->

      <li></li>
    </ul>
  </div>
</template>
<script>
</script>
