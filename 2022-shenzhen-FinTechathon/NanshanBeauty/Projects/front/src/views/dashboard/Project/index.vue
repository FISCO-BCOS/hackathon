<template>
  <div class="component-container scroll-y">
    <el-card class="project-card">
      <template #header>
        <span class="fw-b">B-Recycle项目简介</span>
      </template>
      <div class="project-card__main">
        <!-- 项目简介 -->
        <el-link target="_blank" type="primary" href="https://github.com/cmgun/B-Recycle.git">
          B-Recycle
        </el-link>
        是一个基于
        <el-link target="_blank" type="warning"
          href="https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/community.html">FISCO BCOS</el-link>
        、的区块链电池回收管理平台。
        该平台使用了
        <el-link target="_blank" type="success" href="https://cn.vuejs.org/">Vue</el-link>
        +
        <el-link target="_blank" type="danger" href="https://spring.io/projects/spring-boot">SpringBoot
        </el-link>进行前后端搭建，集成了<el-link target="_blank" type="warning"
          href="https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/community.html">FISCO BCOS</el-link>
        的Java-SDK、区块链消息中间件<el-link target="_blank" type="success"
          href="https://weeventdoc.readthedocs.io/zh_CN/latest/">WeEvent</el-link>
        。
        <el-divider />

        <!-- 源码地址 -->
        <el-row :gutter="10">
          <el-col :span="6">
            <el-badge value="免费开源" class="fw-b"> 项目地址 </el-badge>
          </el-col>

          <el-col :span="6">
            <el-link target="_blank" type="danger" href="https://github.com/cmgun/B-Recycle.git">Github</el-link>
          </el-col>

        </el-row>
        <el-divider />
        <!-- 技术栈 -->
        <el-row :gutter="10">
          <el-col :span="6" class="fw-b"> 后端技术栈 </el-col>
          <el-col :span="18">
            Spring Boot、Gradle、WeBASE、WeEvent、Mybatis-plus、JWT、Shiro、MySQL、Solidity...
          </el-col>
        </el-row>
        <el-divider />
        <el-row :gutter="10">
          <el-col :span="6" class="fw-b"> 前端技术栈 </el-col>
          <el-col :span="18">
            Vue3、TypeScript、Element-Plus、uni-app、vue3-element-admin ...
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'index'
};
</script>

<style lang="scss" scoped>
.component-container {
  .project-card {
    font-size: 14px;

    &__main {
      line-height: 28px;
      height: 320px;
      overflow-y: auto;
      overflow-x: hidden;
    }
  }

  .fw-b {
    font-weight: bold;
  }
}
</style>
