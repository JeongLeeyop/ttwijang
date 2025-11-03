<template>
  <div>
    <CustomProductHeader/>
    <router-view/>
    <CustomProductFooter/>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import CustomProductHeader from '@/Layout/components/customProductHeader.vue';
import CustomProductFooter from '@/Layout/components/customProductFooter.vue';

@Component({
  name: 'CustomProductLayout',
  components: {
    CustomProductHeader,
    CustomProductFooter,
  },
})
export default class extends Vue {
}
</script>
