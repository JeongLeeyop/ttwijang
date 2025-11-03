<template>
  <div>
    <ChallengeHeader/>
    <router-view/>
    <ChallengeFooter/>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import ChallengeHeader from '@/Layout/components/challengeHeader.vue';
import ChallengeFooter from '@/Layout/components/challengeFooter.vue';

@Component({
  name: 'ChallengeLayout',
  components: {
    ChallengeHeader,
    ChallengeFooter,
  },
})
export default class extends Vue {
}
</script>
