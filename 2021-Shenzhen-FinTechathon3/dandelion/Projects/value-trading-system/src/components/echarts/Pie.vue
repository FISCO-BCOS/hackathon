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
        tooltip: {},
        // legend: {
        //   top: "bottom",
        // },
        grid: {
          left: 30,top: 30,right:20,bottom:10
        },
        legend: {
          icon: 'circle'
        },
        series: [
          {
            type: "pie",
            radius: ["10%", "70%"],
            center: ["50%", "50%"],
            roseType: "area",
            itemStyle: {
              borderRadius: 5,
            },
            data: [
              { value: 40, name: "腾讯视频" },
              { value: 38, name: "哔哩哔哩" },
              { value: 32, name: "抖音" },
              // { value: 30, name: "rose 4" },
              // { value: 28, name: "rose 5" },
              // { value: 26, name: "rose 6" },
              // { value: 22, name: "rose 7" },
              // { value: 18, name: "rose 8" },
              // { value: 22, name: "rose 9" }
            ],
          },
        ],
      };
    },
  },
};
</script>