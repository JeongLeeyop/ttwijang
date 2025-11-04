<template>
  <div class="futsal-layout">
    <main-header
      :title="headerTitle"
      :show-notification="showNotification"
      :show-back="showBack"
      @notification-click="handleNotificationClick"
    />
    <router-view :newAlarmCount="newAlarmCount" @child="refreshAlarm" />
    <main-footer />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import MainHeader from './components/backHeader.vue';
import MainFooter from './components/mainFooter.vue';

@Component({
  name: 'FutsalLayout',
  components: {
    MainHeader,
    MainFooter,
  },
})
export default class extends Vue {
  @Prop({ default: '' }) private headerTitle!: string;

  @Prop({ default: false }) private showNotification!: boolean;

  @Prop({ default: true }) private showBack!: boolean;

  private handleNotificationClick() {
    this.$emit('notification-click');
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
