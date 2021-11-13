<template>
  <v-main class="grey lighten-4">
    <v-card color="primary" dark tile>
      <v-card-actions class="pb-0">
        <v-spacer></v-spacer>
        <v-btn icon @click="sheet.show=true">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn icon @click="assets.hide=!assets.hide">
          <v-icon>{{assets.hide?'mdi-eye-off-outline':'mdi-eye-outline'}}</v-icon>
        </v-btn>
      </v-card-actions>
      <v-card-subtitle class="py-0">可流通积分</v-card-subtitle>
      <v-card-title class="pt-0">
        <v-icon>mdi-coins</v-icon>
        <span class="text-h5 pl-1">{{assets.hide?'****':$splited(assets.points)}}</span>
      </v-card-title>
      <v-card-subtitle class="pb-0">已消耗积分*</v-card-subtitle>
      <v-card-text class="white--text">
        <v-icon small>mdi-coins</v-icon>
        <span class="pl-1">{{assets.hide?'****':'987,654,321'}}</span>
      </v-card-text>
      <v-divider></v-divider>
    </v-card>
    <v-list subheader v-for="list in lists" :key="list.subheader"
    class="mb-1">
      <v-subheader>{{list.subheader}}</v-subheader>
        <v-list-item v-for="item in list.items" :key="item.title"
        @click="$router.push(item.path)">
          <v-list-item-icon>
            <v-icon>{{item.icon}}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>
              {{item.title}}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
    </v-list>
    
    <v-bottom-sheet v-model="sheet.show">
      <v-sheet>
        <v-card>
          <v-card-actions class="px-4">
            <v-card-subtitle class="pa-0">申请积分</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-btn icon @click="sheet.show=!sheet.show">
              <v-icon small>mdi-close</v-icon>
            </v-btn>
          </v-card-actions>
          <v-card-title>
            <v-text-field :label="`数额 (${$splited(sheet.amount)})`" v-model="sheet.amount"></v-text-field>
            <v-spacer></v-spacer>
            <v-btn icon color="primary" @click="applyPoints">
              <v-icon>mdi-check</v-icon>
            </v-btn>
          </v-card-title>
        </v-card>
      </v-sheet>
    </v-bottom-sheet>
  </v-main>
</template>

<script>
export default {
  name: '',
  created() {
    this.getBalance()
  },
  data () {
    return {
      sheet: {
        show: false,
        amount: ''
      },
      assets: {
        points: 0,
        hide: false
      },
      lists: [
        {
          subheader: '资产',
          items: [
            {
              icon: 'mdi-cube-outline',
              title: '数字资产',
              path: '/explorer/composition'
            },
            {
              icon: 'mdi-chart-line',
              title: '统计*',
              path: '/explorer/statistic'
            }
          ]
        },
        {
          subheader: '交易',
          items: [
            {
              icon: 'mdi-history',
              title: '历史记录*',
              path: '/explorer/transactions'
            },
          ]
        },
        {
          subheader: '账号',
          items: [
            {
              icon: 'mdi-exit-to-app',
              title: '退出',
              path: '/login'
            }
          ]
        }
      ]
    }
  },
  methods: {
    getPlat() {
      this.$api.contract.platOwner.select('',{})
      .then(res => {
        console.log(res)
      })
    },
    async getBalance() {
      // .then(({data}) => {
        this.assets.points = await this.$balance()
      // })
    },
    applyPoints() {
      let {address} = this.$store.getters.account
      if(address == undefined) {
        console.log('address: ', address)
        return
      }
      this.$api.contract.integral.addIntegral(address, this.sheet.amount)
      .then(res => {
        console.log(res)
        let {data} = res
        if(data.message == 'Success') {
          this.sheet.show = false
          this.$prompt({type: 'success', message: '申请成功'})
        }
        else {
          this.$prompt({type: 'error', message: '申请失败'})
        }
        this.getBalance()
      })
    }
  }
}
</script>

<style scoped>
</style>