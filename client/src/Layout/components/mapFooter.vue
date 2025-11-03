<template>
  <div class="map-footer">
      <div class="txt">
      <a v-if="$route.query.type === 'pickup'" target="_blank" href="https://docs.google.com/forms/d/e/1FAIpQLSdfhkIiNO1r1G1rUHq3n2vnDRv536XxPhkt4WqF43HsoYplIQ/viewform" class="google-form">
        <p class="ttl">집 근처에 픽업 장소가 없나요?</p>
        <p class="small">서비스 지역을 추천 해주시면 열심히 준비 할게요!</p>
      </a>
      <a v-else-if="$route.query.type === 'station'" class="google-form">
        <p class="ttl">거점 배달 장소를 선택해 주세요</p>
        <p class="small">서비스 지역을 추천 해주시면 열심히 준비 할게요!</p>
      </a>
      </div>
  </div>
</template>

<script lang="ts">
</script>
