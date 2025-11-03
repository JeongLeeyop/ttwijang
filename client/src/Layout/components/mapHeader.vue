<template>
  <div class="map-header">
    <i class="el-icon-location"></i>
    <slot>
      {{ $route.query.type === 'station' ? '거점' : '픽업' }}
      장소 선택</slot>
    <button @click="$router.push({ name: 'Home' })" class="close"><i class="el-icon-close"></i></button>
  </div>
</template>
<script lang="ts">
import {
  Vue, Component,
} from 'vue-property-decorator';

@Component({
  name: 'AppHeader',
})

export default class extends Vue {
}
