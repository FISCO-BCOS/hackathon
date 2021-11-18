<template>
  <v-app>
    <v-snackbar bottom tile
      v-model="snackbar.show" :color="snackbar.type" :timeout="snackbar.timeout"
    >
      {{snackbar.message}}
      <template v-slot:action>
        <v-btn small icon @click="snackbar.show = false">
          <v-icon small="">mdi-close</v-icon>
        </v-btn>
      </template>
    </v-snackbar>
    <keep-alive exclude="ads">
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
  </v-app>
</template>

<script>
import Vue from 'vue'
export default {
  name: 'App',
  created() {
    Vue.prototype.$prompt = this.prompt
    Vue.prototype.$splited = this.splited
    Vue.prototype.$balance = this.balance
    Vue.prototype.$unit = this.unit
  },
  data: () => ({
    snackbar: {
      message: '',
      type: '',
      // timeout: 3000,
      show: false
    }
  }),
  methods: {
    prompt(payload) {
      this.snackbar.message = ''
      this.snackbar.type = ''
      for (const key in payload) {
        this.snackbar[key] = payload[key]
      }
      this.snackbar.show = true
    },
    splited(points) {
      let arr = (points||0).toString().split('').reverse()
      let str = ''
      // return arr
      for (let i = 0; i < arr.length; i++) {
        str = `${arr[i]}${i%3==0?',':''}${str}`
      }
      // str.length = str.length-1
      return str.slice(0,str.length-1)
    },
    unit(num) {
      
      let unit = ['','K','M','G','T']
      let index = 0
      while (num>=1000&& index<unit.length) {
        num = parseInt(num/1000)
        index += 1
      }
      return num + unit[index]
    },
    balance() {
      let {address} = this.$store.getters.account
      console.log(this.$store.getters.account)
      return this.$api.contract.integral.balance(address)
      .then(({data}) => data[0])
    }
  }
};
</script>
