<template>
  <v-btn class="pa-0" text dark color="primary" style="overflow: hidden"
  :elevation="elevation">
    <v-progress-linear :value="value" color="primary" height="36">
      <slot>btn</slot>
    </v-progress-linear>
  </v-btn>
</template>

<script>
export default {
  name: "",
  data() {
    return {};
  },
  props: {
    value: Number,
    elevation: String
  }
};
</script>

<style scoped>
</style>