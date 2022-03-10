export default {
  props: {
    item: {
      type: Object,
      default: function () {
        return {};
      }
    }
  },
  data() {
    return {
      echartInstance: null,
      // 轮播图数据
      timer: null,
      seriesIndex: 0,
      dataIndex: 0
    };
  },
  mounted() {
    this.draw()
    window.addEventListener('resize', this.echartInstance.resize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.echartInstance.resize)
    clearInterval(this.timer)
  },
  methods: {
    draw() {
      let ref = this.$refs.instance;
      if (ref == null) return;
      this.echartInstance = this.$echarts.init(ref);
      this.echartInstance.setOption(this.option);
      // this.replay(line, this.option, this.timer)
    },
    replay() {
      let { option, echartInstance } = this
      //轮播展示
      this.timer = setInterval(() => {
        let seriesLen = option.series.length
        let dataLen = option.series[this.seriesIndex].data.length;
        // 取消之前高亮的图形
        echartInstance.dispatchAction({
          type: "downplay",
          seriesIndex: (this.seriesIndex - 1 + seriesLen) % seriesLen, //取消上一个series的效果
          dataIndex: (this.dataIndex - 1 + dataLen) % dataLen
        });
        // 高亮当前图形
        echartInstance.dispatchAction({
          type: "highlight",
          seriesIndex: this.seriesIndex,
          dataIndex: this.dataIndex,
        });
        // 显示 tooltip
        echartInstance.dispatchAction({
          type: "showTip",
          seriesIndex: this.seriesIndex,
          dataIndex: this.dataIndex,
        });

        if (++this.dataIndex == dataLen) {
          this.dataIndex = 0
          if (++this.seriesIndex == seriesLen) {
            this.seriesIndex = 0
          }
        }
      }, 2000);
    },
  }
}