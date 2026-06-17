<template>
    <div class="chart-container">
      <div class="chart-header">
        <el-radio-group v-model="currentView" @change="handleViewChange">
          <el-radio-button label="weekly">周支出</el-radio-button>
          <el-radio-button label="monthly">月支出</el-radio-button>
        </el-radio-group>
      </div>
      <div id="income-chart" :style="{width: '100%', height: '400px'}"></div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import * as echarts from 'echarts'
  
  export default {
    name: 'TransactionsChart',
    props: {
      walletId: {
        type: Number,
        required: true
      }
    },
    data() {
      return {
        chart: null,
        weeklyExpense: [],
        monthlyExpense: [],
        currentView: 'weekly'
      }
    },
    watch: {
      walletId: {
        immediate: true,
        handler(newVal) {
          if (newVal) {
            this.fetchExpenseData()
          }
        }
      }
    },
    mounted() {
      this.initChart()
    },
    methods: {
      initChart() {
        this.chart = echarts.init(document.getElementById('income-chart'))
        this.updateChart()
      },
      async fetchExpenseData() {
        try {
          const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/trend', {
            wallet_id: this.walletId
          })
          console.log('API响应数据:', response.data)
          
          if (response.data.code === 0) {
            this.weeklyExpense = response.data.data?.weeklyData || []
            this.monthlyExpense = response.data.data?.monthlyData || []
            this.updateChart()
          }
        } catch (error) {
          console.error('获取支出数据失败:', error)
        }
      },
      handleViewChange() {
        this.updateChart()
      },
      updateChart() {
        if (!this.chart) {
          return
        }
  
        if (!Array.isArray(this.weeklyExpense) || !Array.isArray(this.monthlyExpense)) {
          console.error('支出数据格式不正确')
          return
        }
  
        const option = {
          title: {
            text: this.currentView === 'weekly' ? '周支出趋势' : '月支出趋势'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: this.currentView === 'weekly'
              ? this.weeklyExpense.map(item => item?.date || '')
              : this.monthlyExpense.map(item => item?.date || '')
          },
          yAxis: {
            type: 'value',
            name: '支出（元）'
          },
          series: [
            {
              name: this.currentView === 'weekly' ? '周支出' : '月支出',
              type: 'line',
              data: this.currentView === 'weekly'
                ? this.weeklyExpense.map(item => item?.expense || 0)
                : this.monthlyExpense.map(item => item?.expense || 0),
              smooth: true,
              itemStyle: {
                color: this.currentView === 'weekly' ? '#409EFF' : '#67C23A'
              }
            }
          ]
        }
        this.chart.setOption(option)
      }
    },
    beforeDestroy() {
      if (this.chart) {
        this.chart.dispose()
        this.chart = null
      }
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
  