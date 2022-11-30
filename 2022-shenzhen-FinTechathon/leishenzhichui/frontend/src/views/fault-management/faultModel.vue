<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content1"></div>
      </el-col>
      <el-col :span="8">
        <div class="">
          <!--          <el-button type="primary" @click="playVideo">训练模型</el-button>-->
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content2"></div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div>
          <center><img src="../../assets/img/硬件部署.png" alt=""></center>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">群组1</span></center>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">群组2</span></center>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">群组3</span></center>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">群组4</span></center>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="6">
        <div>
          <video id="myVideo" class="video-js" autoplay Controls style="height:200px;width: 100%">
            <source src="../../assets/mp4/nodesE_1.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="">
          <video id="myVideo2" class="video-js" autoplay Controls style="height:200px;width: 100%">
            <source src="../../assets/mp4/nodesE_2.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="6">
        <div>
          <video id="myVideo3" class="video-js" autoplay Controls style="height:200px;width: 100%">
            <source src="../../assets/mp4/nodesE_3.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
      <el-col :span="6">
        <div>
          <video id="myVideo4" class="video-js" autoplay Controls style="height:200px;width: 100%">
            <source src="../../assets/mp4/nodesE_4.mp4" type="video/mp4">
          </video>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      autoplayFlag: false
    }
  },
  methods: {
  },
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


</style>
