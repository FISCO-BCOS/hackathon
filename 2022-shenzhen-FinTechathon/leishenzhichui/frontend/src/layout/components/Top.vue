<template>
  <div id="Top">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <i class="el-icon-s-unfold"></i>
      <el-divider direction="vertical" content-position="left"></el-divider>
      <div class="breadcrumb">
        <el-breadcrumb-item v-for="v in list" :key="v.name" :to="{ path: v.path }">{{v.meta.title}}</el-breadcrumb-item>
      </div>
    </el-breadcrumb>

    <!-- 下拉框 -->
    <el-dropdown>
      <span class="el-dropdown-link">
         <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
         <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <span @click="goBack">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import { getMessage } from '@/utils/getMessage.js'

export default {
  data() {
    return {
      list: []
    }
  },
  methods: {
    goBack() {
      this.$store.dispatch('LOGOUT').then(res => {
        this.$router.push('/login');
        getMessage('success', '退出成功');
      }).catch(err => {
        console.log(err);
      })
    },
    isHome() {
      if (this.list[1].path == '/home') {
        this.list.splice(1,1);
      }
    }
  },
  watch: {
    $route(to, from) {
      this.list = to.matched.filter(item => item.meta.title && item.meta);
      this.isHome();
    }
  },
  mounted() {
    this.list = this.$route.matched.filter(item => item.meta.title && item.meta);
    this.isHome();
  }
}
</script>

<style scoped lang="scss">
#Top {
  position: relative;
  width: 100%;
  height: 100%;
}

.el-icon-s-unfold {
  position: absolute;
  top: 0;
  left: 5px;
  font-size: 25px;
  line-height: 60px;
}

.breadcrumb {
  position: absolute;
  top: 0;
  left: 50px;
  line-height: 60px;
}

.el-dropdown {
  position: absolute;
  right: 5px;
  .el-avatar {
    position: absolute;
    top: 50%;
    right: 100%;
    transform: translateY(-50%);
  }
}

.el-divider {
  position: absolute;
  left: 30px;
  top: 22px;
}
</style>