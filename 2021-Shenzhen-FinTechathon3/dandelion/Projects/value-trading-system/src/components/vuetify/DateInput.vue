<template>
  <v-menu
    v-model="menu2"
    :close-on-content-click="false"
    :nudge-right="40"
    transition="scale-transition"
    offset-y
    min-width="290px"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="date"
        :label="label"
        readonly
        v-bind="attrs"
        v-on="on"
        :prepend-icon="prependIcon"
      ></v-text-field>
    </template>
    <v-date-picker v-model="date"
    locale="zh-cn"
    no-title
    @input="menu2 = false"
    @change="$emit('change', date)">
    </v-date-picker>
  </v-menu>
</template>

<script>
export default {
  name: "",
  model: {
    prop: 'date',
    event: 'change'
  },
  props: {
    date: String,
    label: String,
    prependIcon: String
  },
  data() {
    return {
      menu2: false,
    };
  },
};
</script>

<style scoped>
</style>