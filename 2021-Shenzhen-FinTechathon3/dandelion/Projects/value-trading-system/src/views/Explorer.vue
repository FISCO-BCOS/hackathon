<template>
  <v-app>
    <v-app-bar app dark color="primary">
      <v-btn icon
      @click="$router.back()">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <v-toolbar-title>{{title}}</v-toolbar-title>
    </v-app-bar>
    <v-main app>
      <router-view @title-declare="titleDeclare"></router-view>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: '',
  data () {
    return {
      title: '详情'
    }
  },
  methods: {
    titleDeclare(title) {
      this.title = title
    }
  }
}
</script>

<style scoped>
</style>