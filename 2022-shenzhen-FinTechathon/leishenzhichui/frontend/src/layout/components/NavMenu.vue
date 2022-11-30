<template>
  <div id="NavMenu">
    <el-menu router
             :default-active="activePath"
             class="el-menu-vertical-demo"
             background-color="#545c64"
             text-color="#fff"
             active-text-color="#ffd04b">

      <el-menu-item index="/layout">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item index="/loan-application">
        <i class="el-icon-notebook-2"></i>
        <span slot="title">添加充电桩</span>
      </el-menu-item>
      <el-menu-item index="/loan-management">
        <i class="el-icon-s-management"></i>
        <span slot="title">充电桩管理</span>
      </el-menu-item>

      <el-submenu index="/fault-management">
        <template slot="title"><i class="el-icon-s-platform"></i>故障管理</template>
<!--        <el-menu-item index="/fault-management/faultModel">训练模型</el-menu-item>-->
        <el-menu-item index="/fault-management/fault">故障设备</el-menu-item>
<!--        <el-menu-item index="/fault-management/faultModelNB">性能对比</el-menu-item>-->
      </el-submenu>

      <el-submenu index="/maintain-management">
        <template slot="title"><i class="el-icon-s-platform"></i>维修管理</template>
        <el-menu-item index="/maintain-management/createStaff">维修注册</el-menu-item>
        <el-menu-item index="/maintain-management/staff">维修人员</el-menu-item>
        <el-menu-item index="/maintain-management/equipmentRepair">设备维修</el-menu-item>
      </el-submenu>

      <el-submenu index="/transactions">
        <template slot="title"><i class="el-icon-s-platform"></i>资源交易</template>
        <el-menu-item index="/transactions/createUser">创建用户</el-menu-item>
<!--        <el-menu-item index="/transactions/model">博弈训练</el-menu-item>-->
        <el-menu-item index="/transactions/resourceTransaction">资源交易</el-menu-item>
<!--        <el-menu-item index="/transactions/modelNB">性能对比</el-menu-item>-->
      </el-submenu>

    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activePath: ''
    }
  },
  methods: {
    isHome() {
      if (this.activePath == '/home') {
        this.activePath = '/layout';
      }
    }
  },
  mounted() {
    this.activePath = this.$route.path;
    this.isHome();
  },
  watch: {
    $route(to, from) {
      this.activePath = to.matched[to.matched.length - 1].path;
      this.isHome();
    }
  }
}
</script>

<style scoped lang="scss">
#NavMenu {
  height: 100%;

  .el-menu {
    border: none;
  }
}
</style>
