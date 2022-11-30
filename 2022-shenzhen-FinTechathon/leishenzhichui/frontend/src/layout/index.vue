<template>
  <div id="layout">
    <el-container>
      <!-- 左侧导航 -->
      <el-aside width="200px">
        <NavMenu/>
      </el-aside>

      <!-- 右侧主体 -->
      <el-container>
        <!-- 头部分 -->
        <el-header>
          <Top/>
        </el-header>

        <!-- 主体部分 -->
        <el-main>
          <Main/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {NavMenu, Top, Main} from './components/index'

export default {
  components: {NavMenu, Top, Main}
}
</script>

<style>
#layout {
  height: 100%;
}

.el-container {
  height: 100%;
}

/* 导航栏样式 */
.el-header {
  background-color: #ffffff;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #545c64;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

/* 下拉框样式 */
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}

</style>
