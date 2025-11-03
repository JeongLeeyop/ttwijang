<template>
  <div>
    <MissionHeader/>
    <router-view/>
    <MissionFooter/>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import MissionHeader from '@/Layout/components/missionHeader.vue';
import MissionFooter from '@/Layout/components/missionFooter.vue';

@Component({
  name: 'MissionLayout',
  components: {
    MissionHeader,
    MissionFooter,
  },
})
export default class extends Vue {
}
</script>
