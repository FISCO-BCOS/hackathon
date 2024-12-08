<template>
  <div class="chart-container">
    <div class="chart-header">
      <el-radio-group v-model="currentView" @change="handleViewChange">
        <el-radio-button label="weekly">周收入</el-radio-button>
        <el-radio-button label="monthly">月收入</el-radio-button>
      </el-radio-group>
    </div>
    <div id="income-chart" :style="{ width: '100%', height: '400px' }"></div>
  </div>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts'
import { useUserStore } from '@/pinia/modules/user'
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

const userStore = useUserStore()

const walletInfo = ref({
  ID: 1,
  name: 'test',
  balance: 0,
  CreatedAt: '2024-01-01',
  UpdatedAt: '2024-01-01'
})

// 获取钱包信息
const getWalletInfo = async () => {
  try {
    const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/', {
      user_name: userStore.userInfo.userName
    })
    walletInfo.value = response.data.data
    console.log('钱包信息:', walletInfo.value)
  } catch (error) {
    console.error('获取钱包信息失败:', error)
    ElMessage.error('获取钱包信息失败')
  }
}

export default {
  name: 'DashboardChart',
  data() {
    return {
      chart: null,
      weeklyIncome: [],
      monthlyIncome: [],
      currentView: 'weekly'
    }
  },
  mounted() {
    getWalletInfo()
    this.initChart()
    this.fetchIncomeData()
  },
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById('income-chart'))
      this.updateChart()
    },
    async fetchIncomeData() {
      try {
        const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/trend', {
          wallet_id: walletInfo.value.ID
        })
        console.log('API响应数据:', response.data)

        if (response.data.code === 0) {
          this.weeklyIncome = response.data.data?.weeklyData || []
          this.monthlyIncome = response.data.data?.monthlyData || []
          this.updateChart()
        }
      } catch (error) {
        console.error('获取收入数据失败:', error)
      }
    },
    handleViewChange() {
      this.updateChart()
    },
    updateChart() {
      if (!Array.isArray(this.weeklyIncome) || !Array.isArray(this.monthlyIncome)) {
        console.error('收入数据格式不正确')
        return
      }

      const option = {
        title: {
          text: this.currentView === 'weekly' ? '周收入趋势' : '月收入趋势'
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            return `${params[0].name}<br/>${params[0].seriesName}: ¥${params[0].value}`
          }
        },
        xAxis: {
          type: 'category',
          data: this.currentView === 'weekly'
            ? this.weeklyIncome.map(item => item?.date || '')
            : this.monthlyIncome.map(item => item?.date || '')
        },
        yAxis: {
          type: 'value',
          name: '收入（元）',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: this.currentView === 'weekly' ? '周收入' : '月收入',
            type: 'line',
            data: this.currentView === 'weekly'
              ? this.weeklyIncome.map(item => item?.income || 0)
              : this.monthlyIncome.map(item => item?.income || 0),
            smooth: true,
            itemStyle: {
              color: this.currentView === 'weekly' ? '#409EFF' : '#67C23A'
            }
          }
        ]
      }
      this.chart && this.chart.setOption(option)
    }
  },
  beforeDestroy() {
    this.chart && this.chart.dispose()
  }
}
</script>

<style scoped>
.chart-container {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-header {
  margin-bottom: 20px;
  text-align: center;
}
</style>
