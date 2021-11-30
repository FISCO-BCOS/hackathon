<template>
  <div class="grey lighten-4" style="height: 100%;">
    <v-card class="my-2" elevation="0" tile>
      <v-card-subtitle>
        资产趋势
      </v-card-subtitle>
      <v-card-actions>
        <v-btn-toggle v-model="range" color="primary" dense group>
          <v-btn class="mr-0" value='0'>7天</v-btn>
          <v-btn class="mx-0" value='1'>30天</v-btn>
          <v-btn class="mx-0" value='2'>自定义</v-btn>
        </v-btn-toggle>
      </v-card-actions>
      <v-card-text>
        <ec-line style="width: 100%; height: 200px;"></ec-line>
      </v-card-text>
    </v-card>
    <v-card class="my-2" elevation="0" tile>
      <v-card-subtitle>
        积分来源
      </v-card-subtitle>
      <v-card-actions>
        <v-btn-toggle v-model="range" color="primary" dense group>
          <v-btn class="mr-0" value='0'>7天</v-btn>
          <v-btn class="mx-0" value='1'>30天</v-btn>
          <v-btn class="mx-0" value='2'>自定义</v-btn>
        </v-btn-toggle>
      </v-card-actions>
      <v-card-text>
        <ec-pie style="width: 100%; height: 200px;"></ec-pie>
      </v-card-text>
    </v-card>
    
    <v-card class="my-2" elevation="0" tile>
      <v-card-subtitle>
        热门视频
      </v-card-subtitle>
      <v-card-actions>
        <v-btn-toggle v-model="range" color="primary" dense group>
          <v-btn class="mr-0" value='0'>7天</v-btn>
          <v-btn class="mx-0" value='1'>30天</v-btn>
          <v-btn class="mx-0" value='2'>自定义</v-btn>
        </v-btn-toggle>
      </v-card-actions>
      <v-card-text>
        <div>量子波动速读法从入门到精通</div>
        <v-progress-linear :value="40"></v-progress-linear>
      </v-card-text>
      <v-card-text>
        <div>量子波动速读法实践-半分钟精通区块链</div>
        <v-progress-linear :value="30" color="success"></v-progress-linear>
      </v-card-text>
      <!-- <v-card-text>
        <v-progress-linear :value="20" color="orange"></v-progress-linear>
      </v-card-text>
      <v-card-text>
        <v-progress-linear :value="10" color="grey"></v-progress-linear>
      </v-card-text> -->
    </v-card>
  </div>
</template>

<script>
import EcLine from '@/components/echarts/Line'
import EcPie from '@/components/echarts/Pie'
export default {
  name: '',
  components: {
    EcLine,
    EcPie
  },
  created() {
    this.$emit('title-declare', '统计')
  },
  data () {
    return {
      range: '0'
    }
  }
}
</script>

<style scoped>
</style>