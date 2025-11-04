<template>
  <div class="auth-layout">
    <auth-header />
    <router-view :newAlarmCount="newAlarmCount" @child="refreshAlarm" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import AuthHeader from './components/backHeader.vue';

@Component({
  name: 'AuthLayout',
  components: {
    AuthHeader,
  },
})
export default class extends Vue {}
</script>

<style scoped>
/* Styles are in style.css */
</style>
