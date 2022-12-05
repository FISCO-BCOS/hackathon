<template >
  <div class="dashboard-container scroll-y">
    <el-row :gutter="40">
      <el-col class="card-panel__col" >
        <div class="component-container ">
        <el-card class="project-card">
                <el-col class="A" > 欢迎使用B-Recycle电池回收管理平台！当前用户：{{ userStore.username }}</el-col>
        </el-card>
    </div>
      </el-col>
    </el-row>
 
    <!-- <el-row :gutter="40"> -->
      <!-- 项目介绍 -->
      <el-col :md="24" :lg="24" class="card-panel__col">
        <Project />
      </el-col>
    <!-- </el-row> -->
    <!-- Echarts 图表 -->

    <!-- <el-row :gutter="40" style="margin-top: 20px">
      <el-col :sm="24" :lg="12" class="card-panel__col">
        <BarChart id="barChart" height="400px" width="100%" class="chart-container" />
      </el-col>
    </el-row> -->
  </div>

</template>

<script lang="ts">
export default { name: 'Dashboard' };
</script>

<script setup lang="ts">
import { useUserStore } from '@/store/user'
import Project from './Project/index.vue';
// import welcome from './welcome.vue';
// import GithubCorner from '@/components/GithubCorner/index.vue';
// import SvgIcon from '@/components/SvgIcon/index.vue';
// import BarChart from './Chart/BarChart.vue';
// import PieChart from './components/Chart/PieChart.vue';
// import RadarChart from './components/Chart/RadarChart.vue';
// import Team from './Team/index.vue';
const userStore = useUserStore()

</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 24px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
    z-index: 99;
  }

  .box-center {
    margin: 0 auto;
    display: table;
  }

  .user-profile {
    .box-center {
      padding-top: 10px;
    }

    .user-role {
      padding-top: 10px;
      font-weight: 400;
      font-size: 14px;
    }

    .box-social {
      padding-top: 30px;

      .el-table {
        border-top: 1px solid #dfe6ec;
      }
    }

    .user-follow {
      padding-top: 20px;
    }
  }

  .card-panel__col {
    margin-bottom: 12px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);

    .icon-message {
      color: #36a3f7;
    }

    .icon-user {
      color: #40c9c6;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3;
    }

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-user {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3;
      }
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 20px 0;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
        text-align: right;
      }
    }
  }

  .chart-container {
    background: #ffffff;
  }
.A{
  color: "FF9900";
}

.A{
    padding-right: 20px;
    padding-left: 20px;
    color: #1f5a7b !important; 
}
}
</style>

