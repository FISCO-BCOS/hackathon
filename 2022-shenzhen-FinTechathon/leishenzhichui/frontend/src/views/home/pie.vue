<template>
  <div class="pie">
    <div ref="pie" style="height: 400px;"></div>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  data() {
    return {
      option: {
        backgroundColor: '#e0c8ca',
        title: {
          text: '信贷原因',
          left: 'center',
          top: 20,
          textStyle: {
              color: '#fff'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '原因：{b}<br/>占比：{c}%'
        },
        visualMap: {
          show: false,
          min: 0,
          max: 50,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [{
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          data: [
            {value: 25, name: '钱生钱是快车道'},
            {value: 18, name: '有压力才有动力'},
            {value: 20, name: '经济基础决定上层建筑'},
            {value: 10, name: '面子工程'},
            {value: 15, name: '积累信用，树立好口碑'},
            {value: 12, name: '拓展人脉'}
          ].sort(function (a, b) { return a.value - b.value; }),
          roseType: 'radius',
          label: {
            color: 'rgba(255, 255, 255, 1)'
          },
          labelLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 1)'
            },
            smooth: 0.2,
            length: 10,
            length2: 20
          },
          itemStyle: {
            color: '#c23531',
            shadowBlur: 200,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          },
          animationType: 'scale',
          animationEasing: 'elasticOut',
          animationDelay: function (idx) {
            return Math.random() * 200;
          }
        }]
      }
    }
  },
  methods: {
    init() {
      var myChart = echarts.init(this.$refs.pie);
      myChart.setOption(this.option);
    }
  },
  mounted() {
    this.init();
  }
}
</script>

<style scoped>
.pie {
  width: 49%;
  display: inline-block;
  margin-right: 1%;
}
</style>