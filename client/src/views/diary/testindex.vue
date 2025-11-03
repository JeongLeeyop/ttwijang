<template>
  <div>
    <div>123</div>
    <el-button @click="handleVisibleItem()"></el-button>
    <el-dialog title="일이삼" width="40%" :visible.sync="itemModalVisible">
      <MyModal v-if="itemModalVisible" @close="handleVisibleItem()"></MyModal>
    </el-dialog>
  </div>
</template>

<script>
import MyModal from '@/views/diary/MyModal.vue';

export default {
  components: {
    MyModal,
  },
  data() {
    return {
      itemModalVisible: false,
    };
  },
  methods: {
    handleVisibleItem() {
      this.itemModalVisible = !this.itemModalVisible;
    },
  },
};
</script>
