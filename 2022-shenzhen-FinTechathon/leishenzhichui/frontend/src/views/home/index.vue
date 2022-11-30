<template>
  <el-main>
    <div id="home">
      <iframe src="./bigScreen.html" id="map-frame" ref="iframe"></iframe>
    </div>
  </el-main>
</template>


<script>
import panelProup from './panelProup'
import Pie from './pie'
import Map from './map'
import Bar from './bar'

export default {
  components: {
    panelProup,
    Pie,
    Map,
    Bar
  },
  data() {
    return {
      iframeWin: {},
    }
  },
  mounted() {
    /**
     * iframe-宽高自适应显示
     */
    const oIframe = document.getElementById('map-frame');
    const deviceWidth = document.documentElement.clientWidth;
    const deviceHeight = document.documentElement.clientHeight;
    oIframe.style.height = (Number(deviceHeight) + 20) + 'px'; //数字是页面布局高度差，其中的100可以根据自己的界面进行调整
    oIframe.style.width = (Number(deviceWidth) - 200) + 'px'; //数字是页面布局高度差，其中的100可以根据自己的界面进行调整
    // 在外部vue的window上添加postMessage的监听
    this.iframeWin = this.$refs.iframe.contentWindow;
  },
}
</script>

<style scoped lang="scss">
#map-frame {
  height: 90%;
  width: 100%;
}

.el-main {
  margin: 0;
  padding: 0;
}

.home {
  height: 90%;
  width: 100%;
}

</style>
