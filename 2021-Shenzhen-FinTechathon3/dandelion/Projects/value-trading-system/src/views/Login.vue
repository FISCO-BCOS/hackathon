<template>
  <v-app>
    <v-main>
      <v-card style="overflow: hidden; position: relative; height: 100%;" color="transparent;"
      class="pb-4">
        <grid-ball style="position: absolute; top: 10%; left: -10%; z-index: 0" :order="8"></grid-ball>
        <v-card-title style="z-index: 9999; position: relative;">账号设置</v-card-title>
        <v-card-text class="ma-0 py-0">
          <v-text-field label="用户ID" v-model="signUserId"></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text class="font-weight-bold"
          @click="enroll">
            注册
          </v-btn>
          <v-btn color="primary" class="font-weight-bold"
          @click="enter">
            进入
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-main>
  </v-app>
</template>

<script>
import GridBall from '@/components/decoration/GridBall.vue'
export default {
  name: '',
  created() {
    this.$store.commit('account', {})
  },
  components: {
    GridBall
  },
  data () {
    return {
      signUserId: ''
    }
  },
  methods: {
    enroll() {
      if(this.signUserId == '') {
        this.$prompt({
          type: 'warning',
          message: 'ID不能为空'
        })
        return
      }
      this.$api.account.newUser(this.signUserId)
      .then(res => {
        console.log(res)
        this.$prompt(res)
        return res
      })
      .then(({type}) => {
        if(type == 'success') {
          this.enter()
        }
      })
    },
    enter() {
      if(this.signUserId == '') {
        this.$prompt({
          type: 'warning',
          message: 'ID不能为空'
        })
        return
      }
      this.$api.account.userInfo(this.signUserId)
      .then(res => {
        console.log(res)
        this.$prompt(res)
        if(res.type == 'success') {
          this.$store.commit('account', res.data)
          this.$router.replace('/index')
        }
      })
    }
  }
}
</script>

<style scoped>
</style>