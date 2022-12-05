<template>
  <div class="bar">
    <div id="main" ref="bar" style="height: 400px;"></div>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  data() {
    return {
      option: {
        backgroundColor: '#ecd9bc',
        title: {
          text: '各年龄段信贷百分比',
          left: 'center',
          top: 20,
          textStyle: {
              color: '#fff'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {            
            type: 'shadow'       
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
            type: 'category',
            data: ['15-20', '20-25', '25-30', '30-35', '35-40', '>40'],
            axisTick: {
              alignWithLabel: true
            }
          }],
        yAxis: [{
          type: 'value'
        }],
        series: [
          {
            name: '占百分比',
            type: 'bar',
            barWidth: '60%',
            data: [0.50, 17.76, 34.70, 23.87, 12.13, 11.03]
          }
        ]
      }
    }
  },
  methods: {
    init() {
      var myChart = echarts.init(this.$refs.bar);
      myChart.setOption(this.option);
    }
  },
  mounted() {
    this.init();
  }
}
</script>

<style scoped>
.bar {
  width: 49%;
  display: inline-block;
  margin-left: 1%;
}
</style>