<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">面向分布式充电桩的协同故障检测架构图</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">模型准确率</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">与其他模型准确率对比</span></center>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/BGFL.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/learning.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/accuracy.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">工厂节点间距离</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">工厂节点数</span></center>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">工厂恶意节点数</span></center>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/dis.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/com.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
      <el-col :span="8">
        <div class="grid-content bg-purple-body">
          <img src="../../assets/img/node.svg" style="width: 420px;height: 300px">
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">面向分布式充电桩的协同故障检测步骤图</span></center>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <center><span style="font-size: 20px;font-family: 微软雅黑">面向分布式充电桩的协同故障检测步骤图</span></center>
        </div>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div>
          <center><img src="../../assets/img/procedure.svg" style="width: 500px;height: 100%;margin-top: 10px"></center>
        </div>
      </el-col>
      <el-col :span="12">
        <div>
          <center><img src="../../assets/img/frame.svg" style="width: 500px;height: 100%;margin-top: 50px"></center>
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
    return {}
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

</style>
