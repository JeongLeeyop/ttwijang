<template>
  <div>
    <TfseHeader/>
    <router-view/>
    <TfseFooter/>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import TfseHeader from '@/Layout/components/tfseHeader.vue';
import TfseFooter from '@/Layout/components/tfseFooter.vue';

@Component({
  name: 'TfseLayout',
  components: {
    TfseHeader,
    TfseFooter,
  },
})
export default class extends Vue {
}
</script>
