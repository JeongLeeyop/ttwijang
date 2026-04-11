<template>
  <div class="home-main-wrap">
    <div class="home-graph-wrap">
      <!-- <div class="home-graph">
        <div class="home-graph-area">
          <div class="graph__tl">
            <p class="today-order">오늘의 주문현황 [2022-01-04]</p>
          </div>
          <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
            <p class="graph__text">주문</p>
          </div>
          <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="85">133
            </Progress>
            <p class="graph__text">배송완료</p>
          </div>
          <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="60">146
            </Progress>
            <p class="graph__text">신규회원</p>
          </div>
        </div>
        <div class="home-chart">
          <BarChart></BarChart>
        </div>
      </div> -->
    </div>
    <div class="home-stat-wrap">
      <!-- <div class="home-stat">
        <ul>
          <li>
            <p class="title">Average revenue per user (사용자당 평균 수익)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">Purchases (구매수)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">Time to first purchase (첫구매시간)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">User Lifetime value (LTV)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">Retention rate (보유율)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">Churn rate (이탈률)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">DAU & MAU</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">Session length & depth (App 이용시간)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </div>
          </Progress>
          </li>
          <li>
            <p class="title">Average screens per visit (방문당 페이지뷰)</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
          <li>
            <p class="title">재방문율</p>
            <div class="home-graph__box">
            <Progress strokeColor="#58C0D4" :transitionDuration="5000" :radius="50" :strokeWidth="10" value="75">233
            </Progress>
          </div>
          </li>
        </ul>
      </div> -->
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { UserModule } from '@/store/modules/user';

@Component({
  name: '',
})
export default class extends Vue {
  mounted() {
    this.$router.push({ name: 'Dashboard' });
  }
}
</script>
