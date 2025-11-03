<template>
  <div>
    456
    <button class="item-form__closebtn" @click="handleVisibleItem()"></button>
  </div>
</template>

<script>

export default {
  methods: {
    handleVisibleItem() {
      this.$emit('close');
    },
  },
};
</script>
