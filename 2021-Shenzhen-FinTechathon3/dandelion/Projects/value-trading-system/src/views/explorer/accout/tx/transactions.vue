<template>
  <div>
    <v-list>
      <v-list-item v-for="(tx, index) in txList" :key="index" class="mb-1"
      :style="`background: linear-gradient(-40deg, rgba(${positive(tx)?255:0}, ${positive(tx)?0:255}, 0, 0.03), transparent)`"
      @click="">
        <v-list-item-avatar>
          <v-icon>{{tx.icon}}</v-icon>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title>
            {{tx.type}}
          </v-list-item-title>
          <v-list-item-subtitle class="my-1">
            {{tx.project}}
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action class="ma-0 py-3">
          <v-list-item-subtitle :class="`my-1 font-weight-bold ${positive(tx)?'red--text':'green--text'}`">
            {{tx.amount}}
          </v-list-item-subtitle>
          <v-list-item-subtitle class="mb-1">
            {{tx.time}}
          </v-list-item-subtitle>
        </v-list-item-action>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
export default {
  name: '',
  created() {
    this.$emit('title-declare', '交易')
    this.$api.contract.tradeHistory.select("",{})
    .then(res => {
      console.log(res)
    })
  },
  methods: {
    positive: tx => tx.amount.startsWith('+')
  },
  data () {
    return {
      txList: [
        {
          icon: 'mdi-cube-outline',
          type: '数字资产转移',
          project: '哔哩哔哩 => 腾讯视频',
          time: '2021-10-2 10:31',
          amount: '-2000'
        },
        {
          icon: 'mdi-coins',
          type: '积分平台分红',
          project: '腾讯视频',
          time: '2021-10-2 10:30',
          amount: '+20000'
        },
        // {
        //   icon: 'mdi-cube-outline',
        //   type: '数字资产转移',
        //   project: '腾讯视频 => 哔哩哔哩',
        //   time: '2021-10-1 10:30',
        //   amount: '-2000'
        // },
        {
          icon: 'mdi-coins',
          type: '积分结现',
          project: '步步高点读机',
          time: '2021-10-1 10:05',
          amount: '-2000'
        },
        {
          icon: 'mdi-coins',
          type: '积分结现',
          project: '小霸王游戏机',
          time: '2021-10-1 10:02',
          amount: '-2000'
        },
        {
          icon: 'mdi-coins',
          type: '积分结现',
          project: '8848钛金手机',
          time: '2021-10-1 10:00',
          amount: '-2000'
        },
      ]
    }
  }
}
</script>

<style scoped>
</style>