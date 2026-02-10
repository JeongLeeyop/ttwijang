<template>
  <div class="main-layout">
    <main-header @region-change="handleRegionChange" />
    <router-view :key="$route.fullPath" :newAlarmCount="newAlarmCount" :selected-region="selectedRegion" @child="refreshAlarm" />
    <main-footer />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import MainHeader from './components/mainHeader.vue';
import MainFooter from './components/mainFooter.vue';

@Component({
  name: 'MainLayout',
  components: {
    MainHeader,
    MainFooter,
  },
})
export default class extends Vue {
  private selectedRegion = ''

  private newAlarmCount = 0

  private handleRegionChange(region: string): void {
    this.selectedRegion = region;
  }

  private refreshAlarm(): void {
    // Placeholder for alarm refresh
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
