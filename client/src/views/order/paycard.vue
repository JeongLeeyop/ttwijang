<template>
  <div>
    <div class="home3-3__wr">
      <p class="ti">주문 정보</p>
      <div class="home3-3__top__a">
        <div class="home3-3__top__a-1">
          <p class="tti">식단형태</p>
          <p class="ttti">: 샐러드</p>
          <p class="tti">식단목적</p>
          <p class="ttti">: 체중감량</p>
          <p class="tti">식단 유의사항</p>
          <p class="ttti">: 없음</p>
        </div>
        <div class="home3-3__top__a-2">
          <p class="tti">주당 식사 수</p>
          <p class="ttti">: 5</p>
          <p class="tti">픽업요일</p>
          <p class="ttti">: 수요일</p>
          <p class="tti">식단관리 기간</p>
          <p class="ttti">: 2주</p>
        </div>
      </div>

      <p class="ti">식단 리스트</p>
      <div class="home3-3__top__b">
        <p class="tti">[ 1주차 ]<span class="tti color2"> 2022년 01월 01일 ~ 01월 07일</span></p>
        <div class="home3-3__box">
          <p class="ttti">쉬림프 샐러드 X1개</p>
          <p class="ttti a">7,000원</p>
          <p class="ttti">닭목살 샐러드 X2개</p>
          <p class="ttti a">14,000원</p>
        </div>
        <p class="tti">[ 2주차 ]<span class="tti color2"> 2022년 01월 01일 ~ 01월 07일</span></p>
        <div class="home3-3__box">
          <p class="ttti">쉬림프 샐러드 X1개</p>
          <p class="ttti a">7,000원</p>
          <p class="ttti">닭목살 샐러드 X2개</p>
          <p class="ttti a">14,000원</p>
        </div>
        <div class="home3-3__top__b-1">
          <p class="tii">총결제 금액</p>
          <p class="tiii">14,000원</p>
        </div>
      </div>

      <div class="home3-3__top__c">
        <table>
          <caption>선택된 픽업 장소</caption>
          <tbody>
            <tr>
              <th>장소 이름</th>
              <td>이름</td>
            </tr>
            <tr>
              <th>장소 주소</th>
              <td>주소</td>
            </tr>
            <tr>
              <th>장소 선택</th>
              <td><router-link to="/">다른 픽업장소 선택하기</router-link></td>
            </tr>
            <tr>
              <th>픽업 시간 선택</th>
              <td>
                <el-select placeholder="가능 시간 대" :popper-append-to-body="false" v-model="time">
                  <el-option v-for="item in times" :key="item.value" :label="item.label"
                    :value="item.value"></el-option>
                </el-select>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="home3-3-1__bot">
        <p class="tit">결제수단</p><router-link to="">수정</router-link>
      </div>

      <div class="home3-3-1__im">
        <img src="~@/assets/images/card.png" class="card" alt="결제카드">
      </div>

      <div class="diary__b__bot">
        <router-link :to="{ name: 'Password' }" class="">카드등록</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '',
      address: '',
      detail_address: '',
      memo: '',
      input1: '',
      input2: '',
      input3: '',
    };
  },
};
</script>
