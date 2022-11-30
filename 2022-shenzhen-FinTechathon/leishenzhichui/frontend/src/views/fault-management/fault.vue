<template>
  <div id="loan-application">
    <div class="filter-container">
      <el-input prefix-icon="el-icon-search" v-model="firstQuery.name" @input="searchInput" clearable
                placeholder="请输入设备名" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px">
        搜索
      </el-button>
    </div>
    <el-table :data="tableData" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="序号" width="100"></el-table-column>
      <el-table-column align="center" prop="name" label="设备名称" width="160"></el-table-column>
      <el-table-column align="center" prop="station" label="充电站" width="250"></el-table-column>
      <el-table-column align="center" prop="parkid" label="车位号" width="150"></el-table-column>
      <el-table-column align="center" prop="lon_lat" label="经纬度" width="150"></el-table-column>
      <el-table-column align="center" prop="status" label="状态" >
        <template v-slot="{row}">
          <el-tag type="warning" effect="dark">{{ "等待维修" }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="操作">-->
<!--        <template v-slot="{row}">-->
<!--          <el-button size="mini" type="primary" @click="query(row)">详情</el-button>-->
<!--          <el-button size="mini" type="success" @click="pass(row)">提交</el-button>-->
<!--          <el-button size="mini" type="danger" @click="reject(row)">撤销</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <el-dialog title="充电桩详情" :visible.sync="dialogFormVisible" center>
      <el-descriptions title="充电桩信息">
        <el-descriptions-item label="ID">{{ temp.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.name }}</el-descriptions-item>
        <el-descriptions-item label="充电站">{{ temp.station }}</el-descriptions-item>
        <el-descriptions-item label="车位号">{{ temp.parkid }}</el-descriptions-item>
        <el-descriptions-item label="交流/直流">{{ temp.ac_dc }}</el-descriptions-item>
        <el-descriptions-item label="开/关枪">{{ temp.gun_status }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ temp.start_time }}</el-descriptions-item>
        <el-descriptions-item label="关闭时间">{{ temp.end_time }}</el-descriptions-item>
        <el-descriptions-item label="价格（元/度）">{{ temp.price }}k/月</el-descriptions-item>
        <el-descriptions-item label="服务费">{{ temp.service_charge }}</el-descriptions-item>
        <el-descriptions-item label="经纬度">{{ temp.lon_lat }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 分页 -->
    <Pagination
        :current-page.sync="firstQuery.pageNo"
        :page-size.sync="firstQuery.pageSize"
        :total="rows"
        @getList="getList">
    </Pagination>
  </div>
</template>

<script>
import {failureList, readyReject, readyPass} from '@/api/api'
import Pagination from '@/components/pagination'

export default {
  components: {
    Pagination
  },
  data() {
    return {
      firstQuery: {
        pageNo: 1,
        pageSize: 10,
        name: ''
      },
      tableData: [],
      rows: 1,
      temp: {},
      dialogFormVisible: false
    }
  },
  methods: {
    getList() {
      failureList(this.firstQuery).then(res => {
        let {code} = res.data;
        if (code === 20000) {
          let {data, rows} = res.data.data;
          this.tableData = data;
          this.rows = rows;
        }
      })
    },
    searchInput() {
      this.firstQuery.pageNo = 1;
      this.getList();
    },
    reject(row) {
      this.$confirm('是否提交维修?', '故障管理', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        readyReject({id: row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '成功拒绝',
              duration: 2000,
              showClose: true
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration: 2000,
          showClose: true
        })
      })
    },
    pass(row) {
      this.$confirm('是否通过维修?', '故障管理', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        readyPass({id: row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '充电桩申请-身份认证通过！',
              duration: 2000,
              showClose: true
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration: 2000,
          showClose: true
        })
      })
    },
    query(row) {
      this.dialogFormVisible = true;
      this.temp = row
    },
    evaluate(row) {
      alert("这是信用评估：" + row)
    }
  },
  mounted() {
    this.getList();
  },
  filters: {
    getSex(data) { // 性别过滤
      switch (data) {
        case 'man':
          return '男';
        case 'woman':
          return '女';
        default:
          return data;
      }
    },
    getEducation(data) { // 学历过滤
      switch (data) {
        case 'college':
          return '大学';
        case 'highschool':
          return '高中';
        default:
          return data;
      }
    },
    getDate(date) { // 时间过滤
      const nDate = new Date(date);
      //将不满10的数字自动补0
      const year = nDate.getFullYear().toString().padStart(2, 0);
      const month = nDate.getMonth().toString().padStart(2, 0);
      const day = nDate.getDay().toString().padStart(2, 0);
      return year + '-' + month + '-' + day;
    },
  }
}
</script>
