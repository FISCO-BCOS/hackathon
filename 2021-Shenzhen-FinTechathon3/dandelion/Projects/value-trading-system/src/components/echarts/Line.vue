<template>
  <div ref="instance"></div>
</template>

<script>
import MxEchart from "./MxEchart";
export default {
  name: "lines",
  mixins: [MxEchart],
  computed: {
    option() {
      return {
        // tooltip: {
        //   trigger: "axis",
        // },
        grid: {
          left: 30,top: 30,right:20,bottom:20
        },
        legend: {
          icon: 'circle'
        },
        xAxis: {
          type: "category",
          data: ["Mon", "", "", "", "", "", "Sun"],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: '积分',
            data: [0, 10, 15, 16, 20, 25, 40],
            type: "line",
          },
          {
            name: '现金',
            data: [0, 5, 10, 11, 20, 30, 35],
            type: "line",
          }
        ],
      };
    },
  },
};
</script>