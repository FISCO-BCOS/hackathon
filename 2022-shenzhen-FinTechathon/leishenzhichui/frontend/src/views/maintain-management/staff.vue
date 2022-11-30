<template>
  <div id="loan-application">
    <div class="filter-container">
      <el-input prefix-icon="el-icon-search" v-model="firstQuery.name" @input="searchInput" clearable
                placeholder="请输入姓名" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px">
        搜索
      </el-button>
    </div>
    <el-table :data="tableData.slice(0,1)" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="序号" width="100"></el-table-column>
      <el-table-column align="center" prop="name" label="姓名" width="160">
        赵维
      </el-table-column>
      <el-table-column align="center" prop="id" label="职工编号" width="150">
        work3214667
      </el-table-column>
      <el-table-column align="center" prop="money" label="维修奖励(总计/元)" width="150">
        1420
      </el-table-column>
      <el-table-column align="center" prop="credit" label="信用分数" width="150">
        72
      </el-table-column>
      <el-table-column align="center" prop="" label="身份认证结果" width="150">
        <el-tag :type="flag == true ? 'success' : 'info'" effect="dark">{{ flag == true ? "认证成功" : "待认证" }}</el-tag>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template v-slot="{row}">
          <el-button size="mini" type="primary" @click="evaluate">身份认证</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="申请管理-编辑" :visible.sync="dialogFormVisible" center>
      <el-descriptions title="用户信息">
        <el-descriptions-item label="ID">{{ temp.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.name }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ temp.birthday | getDate }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ temp.sex | getSex }}</el-descriptions-item>
        <el-descriptions-item label="居住地址">{{ temp.address1 }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ temp.identity_card }}</el-descriptions-item>
        <el-descriptions-item label="教育程度">{{ temp.education | getEducation }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.phone }}</el-descriptions-item>
        <el-descriptions-item label="收支">{{ temp.income }}k/月</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="公司详情">
        <el-descriptions-item label="公司名称">{{ temp.company }}</el-descriptions-item>
        <el-descriptions-item label="公司地址">{{ temp.address3 }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ temp.position }}</el-descriptions-item>
        <el-descriptions-item label="公司电话">{{ temp.company_phone }}</el-descriptions-item>
        <el-descriptions-item label="公司邮箱">{{ temp.company_email }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="家庭联系人">
        <el-descriptions-item label="关系">{{ temp.contact }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.contact_name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.contact_phone }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="工作证明人">
        <el-descriptions-item label="关系">{{ temp.contact2 }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.contact2_name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.contact2_phone }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ temp.contact2_dep }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ temp.contact2_pos }}</el-descriptions-item>
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
import {firstList, firstReject, firstPass, staffList} from '@/api/api'
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
      dialogFormVisible: false,
      flag: false
    }
  },
  methods: {
    getList() {
      staffList().then(res => {
        let {code} = res.data;
        if (code === 20000) {
          let {data, rows} = res.data.data;
          this.tableData = data;
          this.rows = rows;
        }
      })
      // staffList({'id': 'node_3'}).then(res => {
      //   this.tableData = res.data.data.data;
      // })
    },
    searchInput() {
      this.firstQuery.pageNo = 1;
      this.getList();
    },
    reject(row) {
      this.$confirm('是否拒绝充电桩？', '充电桩审批-初审', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        firstReject({id: row.id}).then(res => {
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
      this.$confirm('是否通过充电桩？', '充电桩审批-初审', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        firstPass({id: row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '充电桩申请-初审通过',
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
      this.temp = row;
    },
    evaluate(row) {
      this.flag = true
      this.getList();
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
