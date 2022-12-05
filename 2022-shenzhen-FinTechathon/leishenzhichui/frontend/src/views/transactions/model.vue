<template>
  <div style="display: flex;flex-direction: column">
    <el-button type="primary" style="width: 150px">博弈训练</el-button>
    <img src="../../assets/img/系统架构图.svg" alt="">
  </div>
</template>

<script>
export default {
  data() {
    return {
      autoplayFlag: false
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


</style>
