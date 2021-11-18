<template>
  <v-app>
    <v-app-bar app dark color="primary" height="0" style="display: none;">
    </v-app-bar>
    <!-- <v-main app> -->
      <router-view />
    <!-- </v-main> -->

    <v-bottom-navigation app grow horizontal color="primary" v-model="bottomNav">
      <v-btn v-for="(opt, index) in navList" :key="index"
      @click.native="changeRoot(opt)">
        <span>{{opt.label}}</span>
        <v-icon>{{opt.icon}}</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
export default {
  name: "",
  created() {
    this.activateBottomNav()
  },
  watch: {
    $route: {
      deep: true,
      handler: function() {
        this.activateBottomNav()
      }
    }
  },
  data() {
    return {
      bottomNav: 0,
      navList: [
        {
          label: '首页',
          icon: 'mdi-apps',
          path: '/index'
        },
        {
          label: '账号',
          icon: 'mdi-account',
          path: '/account'
        },
      ]
    };
  },
  methods: {
    changeRoot(value) {
      if(this.$route.path!=value.path)
      this.$router.replace(value.path)
    },
    activateBottomNav() {
      this.bottomNav = this.navList.map(e => e.path).indexOf(this.$route.path)
    }
  }
};
</script>

<style scoped>
</style>