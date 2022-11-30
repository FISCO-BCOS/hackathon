<template>
  <div class="map">
    <div ref="map" style="width: 100%;height:620px;"></div>
  </div>
</template>

<script>
import echarts from 'echarts'
import 'echarts/map/js/china'

export default {
  data() {
    return {
      option: {
        title: {
          text: '2021上半年全国各省人均可支配收入(不包含港澳台以及南海诸岛等地区)',
          left: 'center'
        },
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: '省（岛屿）名：{b}<br/>收入：{c}(元/每人)'
        },
        series: [{
          name: '人均可支配收入',
          type: 'map',
          map: 'china',
          label: {
            show: true
          },
          data: [
            {name:'上海', value:40357},
            {name:'北京', value:38138},
            {name:'浙江', value:30998},
            {name:'江苏', value:25119},
            {name:'天津', value:24625},
            {name:'广东', value:23604},
            {name:'福建', value:21419},
            {name:'山东', value:18204},
            {name:'辽宁', value:18083},
            {name:'重庆', value:17791},
            {name:'内蒙古', value:16259},
            {name:'安徽', value:15969},
            {name:'海南', value:15670},
            {name:'湖南', value:15183},
            {name:'湖北', value:15097},
            {name:'四川', value:14921},
            {name:'河北', value:14340},
            {name:'陕西', value:14287},
            {name:'江西', value:14195},
            {name:'吉林', value:13690},
            {name:'广西', value:13536},
            {name:'河南', value:12735},
            {name:'山西', value:12621},
            {name:'云南', value:12235},
            {name:'宁夏', value:12232},
            {name:'黑龙江', value:12205},
            {name:'青海', value:11729},
            {name:'贵州', value:11573},
            {name:'甘肃', value:10149},
            {name:'新疆', value:10114},
            {name:'西藏', value:9693}
          ]
        }],
        visualMap: {
          min: 8000,
          max: 42000,
          text: ['高', '低'],
          realtime: false,
          calculable: true,
          inRange: {
            color: ['lightskyblue', 'yellow', 'orangered']
          }
        },
      }
    }
  },
  methods: {
    init() {
      var myChart = echarts.init(this.$refs.map);
      myChart.setOption(this.option);
    }
  },
  mounted() {
    this.init();
  }
}
</script>

<style scoped>
.map {
  margin-top: 70px;
}
</style>