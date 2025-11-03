<template>
  <div class="delivery_step2">
    <div class="home3-3__wr">
      <p class="ti a"> 맞춤식단 서비스를 준비중입니다</p>
      <p class="ti color2">주문 정보</p>
      <div class="home3-3__top__a">
        <div class="home3-3__top__a-1">
          <p class="tti">식단형태</p>
          <p class="ttti">: 샐러드</p>
          <p class="tti">식단목적</p>
          <p class="ttti">: 체중감량</p>
          <p class="tti">특이사항</p>
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
        <div class="home4-1-2__head">
        <el-steps :active="0" align-center>
          <el-step title="주문접수"></el-step>
          <el-step title="매장배달중"></el-step>
          <el-step title="픽업대기"></el-step>
          <el-step title="픽업완료"></el-step>
        </el-steps>
      </div>
      <router-link :to="{ name: 'cancelStep1' }">
          <p class="cancel_box">부분취소</p>
        </router-link>

        <p class="tti">[ 2주차 ]<span class="tti color2"> 2022년 01월 01일 ~ 01월 07일</span></p>
        <div class="home3-3__box">
          <p class="ttti">쉬림프 샐러드 X1개</p>
          <p class="ttti a">7,000원</p>
          <p class="ttti">닭목살 샐러드 X2개</p>
          <p class="ttti a">14,000원</p>
        </div>
        <div class="home4-1-2__head">
        <el-steps :active="0" align-center>
          <el-step title="주문접수"></el-step>
          <el-step title="매장배달중"></el-step>
          <el-step title="픽업대기"></el-step>
          <el-step title="픽업완료"></el-step>
        </el-steps>
      </div>
      <router-link :to="{ name: 'cancelStep1' }">
          <p class="cancel_box">부분취소</p>
        </router-link>
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
           <!--  <tr>
              <th>장소 선택</th>
              <td><router-link to="/">다른 픽업장소 선택하기</router-link></td>
            </tr> -->
            <tr>
              <th>픽업 시간</th>
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
      <div class="home4-1-2__tail">
        <router-link :to="{ name: 'cancelStep2' }" class="">주문취소</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class extends Vue {
  times = [
    {
      value: '오후 4:00 ~ 오후 4:15',
      label: '오후 4:00 ~ 오후 4:15',
    },
    {
      value: '오후 4:15 ~ 오후 4:30',
      label: '오후 4:15 ~ 오후 4:30',
    },
  ];

  time = '오후 4:00 ~ 오후 4:15';
}
</script>
