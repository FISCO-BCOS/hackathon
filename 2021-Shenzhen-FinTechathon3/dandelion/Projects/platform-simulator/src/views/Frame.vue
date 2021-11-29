<template>
  <div>
    <v-app-bar app clipped-left dark>
      <v-app-bar-nav-icon @click="viewFlag.drawer = !viewFlag.drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>视频共享平台模拟器</v-toolbar-title>
      <v-spacer></v-spacer>
      <div style="width: 200px; position: relative; top: 15px;">
        <v-text-field dense label="用户" v-model="signUserId" prepend-icon="mdi-account"
        @input="setUser"></v-text-field>
      </div>
      <!-- <v-btn text>
        <v-icon left>mdi-account</v-icon> {{'hcl'}}
      </v-btn> -->
    </v-app-bar>
    <v-navigation-drawer app clipped disable-resize-watcher
    v-model="viewFlag.drawer">
      <v-list>
        <v-list-item-group v-model="item" color="primary">
          <v-list-item v-for="(item, i) in menu" :key="i"
          @click="clickListItem(item)">
            <v-list-item-icon>
              <v-icon v-text="item.icon"></v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title v-text="item.name"></v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
      <!-- <c-list :menu="menu" @click-list-item="clickListItem"></c-list> -->
    </v-navigation-drawer>
    <v-main>
      <router-view></router-view>
    </v-main>
  </div>
</template>

<script>
// import 
export default {
  name: "App",
  computed: {
    menu() {
      return this.$router.options.routes[1].children
    }
  },
  created() {
    this.signUserId = this.$store.getters.account.signUserId
  },
  methods: {
    clickListItem({path}) {
      this.$router.push(path)
    },
    setUser(info) {
      this.$store.commit('account', {signUserId: info})
    }
  },
  data: () => ({
    item: '',
    signUserId: '',
    viewFlag: {
      drawer: true
    },
  }),
};
</script>

<style scoped>
</style>
