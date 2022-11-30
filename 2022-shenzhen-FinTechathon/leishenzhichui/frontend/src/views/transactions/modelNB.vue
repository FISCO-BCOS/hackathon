<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">基于博弈论的充电桩市场均衡架构</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">多智能体强化学习算法</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">排队因子影响</span></center>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/系统架构图.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <video id="myVideo1" class="video-js" Controls Autoplay style="height: 300px;width: 420px">
            <source src="../../assets/mp4/多智能体强化学习算法.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <video id="myVideo2" class="video-js" Controls Autoplay style="height: 300px;width: 420px">
            <source src="../../assets/mp4/排队因子.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">抽成比例影响</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">充电用户数量</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">性能展示</span></center>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <video id="myVideo3" class="video-js" Controls Autoplay style="height: 300px;width: 420px">
            <source src="../../assets/mp4/电费抽成比例.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <video id="myVideo4" class="video-js" Controls Autoplay style="height: 300px;width: 420px">
            <source src="../../assets/mp4/充电用户数量.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <video id="myVideo5" class="video-js" Controls Autoplay style="height: 300px;width: 420px">
            <source src="../../assets/mp4/性能展示.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Pagination from '@/components/pagination'

export default {
  components: {
    Pagination
  },
  data() {
    return {
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], // 播放速度
        autoplay: false, // 如果true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 导致视频一结束就重新开始。
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: 'video/mp4', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
          src: '../../assets/mp4/排队因子.mp4' // url地址
        }],
        poster: '', // 你的封面地址
        width: document.documentElement.clientWidth, // 播放器宽度
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        }
      }
    }
  },
  methods: {},
  mounted() {
  }
}
</script>
<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;

  & :last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-body {
  background: #99a9bf;
  height: 300px;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.video-js .vjs-icon-placeholder {
  width: 80%;
  height: 80%;
  display: block;
}

/* .video-player {
  width: 50%;
} */
</style>
