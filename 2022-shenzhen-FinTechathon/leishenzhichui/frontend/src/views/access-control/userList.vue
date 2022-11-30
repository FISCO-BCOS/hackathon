<template>
  <div class="userList" v-loading="loading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-input prefix-icon="el-icon-search" v-model="queryList" @input="searchInput" clearable placeholder="请输入姓名" style="width: 200px;" class="filter-item" />
        <span style="margin-left: 20px;">共有<el-tag effect="plain" size="small">{{listData.length}}</el-tag>位用户</span>
      </div>
      <el-collapse v-model="activeNames" accordion>
        <el-collapse-item v-for="(v, i) in listData" :key="i" :title="v.account" :name='i'>
          <el-descriptions title="用户信息">
            <el-descriptions-item label="用户名">{{v.account}}</el-descriptions-item>
            <el-descriptions-item label="密码">{{v.password}}</el-descriptions-item>
            <el-descriptions-item label="权限类型">{{v.role_name | permissionType}}</el-descriptions-item>
            <el-descriptions-item label="创建者">{{v.creator | createType}}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{v.reg_time | getDate}}</el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
import { getUserList } from '@/api/api.js'

export default {
  data() {
    return {
      activeNames: ['0'],
      lists: [],
      loading: true,
      queryList: '',
      listData: []
    }
  },
  methods: {
    getList() {
      let userLists = this.$store.getters.getUserLists;
      if (userLists.length>0) {
        this.lists = userLists;
        this.searchInput();
        this.loading = false;
      } else {
        getUserList().then(res => {
          let { code } = res.data;
          if (code == 20000) {
            this.$store.dispatch('SETUSERLISTS', res.data.data);
            this.lists = res.data.data;
            this.loading = false;
            this.searchInput();
          }
        })
      } 
    },
    searchInput() {
      if (this.queryList == '') {
        this.listData = this.lists;
      } else {
        this.listData = this.lists.filter(v => v.account.includes(this.queryList));
      }
      this.activeNames = ['0'];
    }
  },
  mounted() {
    this.getList();
  },
  filters: {
    getDate(time){ // 时间过滤
      var d = new Date(time);
      var times=d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
      return times;
    },
    permissionType(data) {
      switch(data) {
        case 'input':
          return '销售人员';
        case 'approve':
          return '初审人员';
        default:
          return '管理人员';
      }
    },
    createType(data) {
      switch(data) {
        case 'null':
          return 'admin';
        default:
          return 'admin';
      }
    }
  }
};
</script>